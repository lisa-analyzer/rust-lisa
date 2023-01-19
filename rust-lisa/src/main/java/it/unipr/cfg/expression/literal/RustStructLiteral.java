package it.unipr.cfg.expression.literal;

import it.unipr.cfg.expression.literal.enums.RustEnumTupleLiteral;
import it.unipr.cfg.type.composite.RustStructType;
import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.lattices.ExpressionSet;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.Global;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.ProgramPoint;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.heap.AccessChild;
import it.unive.lisa.symbolic.heap.HeapDereference;
import it.unive.lisa.symbolic.heap.HeapReference;
import it.unive.lisa.symbolic.heap.MemoryAllocation;
import it.unive.lisa.symbolic.value.Variable;
import it.unive.lisa.type.ReferenceType;
import it.unive.lisa.type.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Rust struct literal.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustStructLiteral extends NaryExpression {

	/**
	 * Computes the semantic of the TupleLiteral, wheter it is a
	 * {@link RustTupleLiteral} or a {@link RustEnumTupleLiteral}.
	 * 
	 * @param location        the codelocation where this semantics is going to
	 *                            be calculated
	 * @param type            the type of this tuple literal
	 * @param pp              the program point of where this statement's
	 *                            semantic is going to be calculated
	 * @param <A>             the type of {@link AbstractState}
	 * @param <H>             the type of the {@link HeapDomain}
	 * @param <V>             the type of the {@link ValueDomain}
	 * @param <T>             the type of {@link TypeDomain}
	 * @param interprocedural the interprocedural analysis of the program to
	 *                            analyze
	 * @param state           the analysis state where the expression is to be
	 *                            evaluated
	 * @param keys            the name of each sub-expression
	 * @param subExprs        the sub-expression contained in this statement
	 * @param params          the symbolic expressions representing the computed
	 *                            values of the sub-expressions of this
	 *                            expression
	 * @param expressions     the cache where analysis states of intermediate
	 *                            expressions are stored and that can be
	 *                            accessed to query for post-states of
	 *                            parameters expressions
	 * 
	 * @return the {@link AnalysisState} representing the abstract result of the
	 *             execution of this expression
	 * 
	 * @throws SemanticException if something goes wrong during the computation
	 */
	public static <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> semantic(CodeLocation location, ProgramPoint pp,
					Type type, InterproceduralAnalysis<A, H, V, T> interprocedural,
					AnalysisState<A, H, V, T> state, String[] keys, Expression[] subExprs,
					ExpressionSet<SymbolicExpression>[] params, StatementStore<A, H, V, T> expressions)
					throws SemanticException {

		String structName = type.toString();
		if (type.toString().contains("::")) {
			String[] names = type.toString().split(Pattern.quote("::"), -1);
			structName = names[names.length - 1];
		}

		Collection<Global> globals = RustStructType.get(structName).getUnit().getInstanceGlobals(true);

		MemoryAllocation allocation = new MemoryAllocation(type, location, true);
		AnalysisState<A, H, V, T> allocationState = state.smallStepSemantics(allocation, pp);

		ExpressionSet<SymbolicExpression> containers = allocationState.getComputedExpressions();

		AnalysisState<A, H, V, T> result = state.bottom();
		for (SymbolicExpression container : containers) {
			HeapReference ref = new HeapReference(new ReferenceType(type), container, location);
			HeapDereference deref = new HeapDereference(type, ref, location);

			AnalysisState<A, H, V, T> startingState = allocationState;

			for (int i = 0; i < keys.length; ++i) {
				Expression expression = subExprs[i];
				Type expressionType = expression.getStaticType();
				String key = keys[i];

				Optional<Global> optionalGlobal = globals.stream().filter(
						g -> key.equals(g.getName())
								&& (expressionType.canBeAssignedTo(g.getStaticType())
										|| g.getStaticType().canBeAssignedTo(expressionType)))
						.findFirst();
				if (optionalGlobal.isPresent()) {
					Global accessedGlobal = optionalGlobal.get();
					Variable variable = accessedGlobal.toSymbolicVariable(location);

					AccessChild child = new AccessChild(subExprs[i].getStaticType(), deref, variable,
							location);

					AnalysisState<A, H, V, T> tmp = result.bottom();

					AnalysisState<A, H, V, T> accessedChildState = startingState.smallStepSemantics(child, pp);
					for (SymbolicExpression childIdentifier : accessedChildState.getComputedExpressions())
						for (SymbolicExpression exprParam : params[i])
							tmp = tmp.lub(startingState.assign(childIdentifier, exprParam, pp));

					startingState = tmp;
				} else
					throw new IllegalAccessError("The field " + key + " in struct "
							+ RustStructType.get(type.toString()) + " does not exists");
			}

			result = result.lub(startingState.smallStepSemantics(ref, pp));
		}

		return result;
	}

	private String[] keys;

	/**
	 * Build the tuple literal.
	 * 
	 * @param cfg      the {@link CFG} where this literal lies
	 * @param location the location where this literal is defined
	 * @param struct   the struct type of this literal
	 * @param keys     the keys used to identify fields of this struct literal
	 * @param values   the values inside the literal
	 */
	public RustStructLiteral(CFG cfg, CodeLocation location, RustStructType struct, String[] keys,
			Expression[] values) {
		super(cfg, location, struct.toString(), struct, values);
		this.keys = keys;
	}

	@Override
	public String toString() {
		return getStaticType() + "{"
				+ Arrays.asList(getSubExpressions()).stream().map(e -> e.toString()).collect(Collectors.joining(", "))
				+ "}";
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> expressionSemantics(
					InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
					ExpressionSet<SymbolicExpression>[] params, StatementStore<A, H, V, T> expressions)
					throws SemanticException {

		return semantic(getLocation(), this, getStaticType(), interprocedural, state, keys, getSubExpressions(), params,
				expressions);
	}

}

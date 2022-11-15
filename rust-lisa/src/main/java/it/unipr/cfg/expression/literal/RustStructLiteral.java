package it.unipr.cfg.expression.literal;

import it.unipr.cfg.expression.RustVariableRef;
import it.unipr.cfg.statement.RustAssignment;
import it.unipr.cfg.type.composite.RustReferenceType;
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
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.heap.AccessChild;
import it.unive.lisa.symbolic.heap.HeapAllocation;
import it.unive.lisa.symbolic.heap.HeapDereference;
import it.unive.lisa.symbolic.heap.HeapReference;
import it.unive.lisa.symbolic.value.Variable;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Rust struct literal.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustStructLiteral extends NaryExpression {

	/**
	 * Build the tuple literal.
	 * 
	 * @param cfg      the {@link CFG} where this literal lies
	 * @param location the location where this literal is defined
	 * @param struct   the struct type of this literal
	 * @param values   the values inside the literal
	 */
	public RustStructLiteral(CFG cfg, CodeLocation location, RustStructType struct, Expression[] values) {
		super(cfg, location, struct.toString(), struct, values);
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

		HeapAllocation allocation = new HeapAllocation(getStaticType(), getLocation());
		AnalysisState<A, H, V, T> allocationState = state.smallStepSemantics(allocation, this);

		ExpressionSet<SymbolicExpression> containerExprs = allocationState.getComputedExpressions();

		AnalysisState<A, H, V, T> result = state.bottom();
		for (SymbolicExpression container : containerExprs) {
			HeapReference ref = new HeapReference(new RustReferenceType(getStaticType(), false), container,
					getLocation());
			HeapDereference deref = new HeapDereference(getStaticType(), ref, getLocation());

			AnalysisState<A, H, V, T> startingState = allocationState;
			for (int i = 0; i < getSubExpressions().length; ++i) {

				// The types of inner expression are RustLetAssignment
				RustAssignment assigment = (RustAssignment) getSubExpressions()[i];
				String variableName = ((RustVariableRef) assigment.getLeft()).getName();

				Variable variable = new Variable(assigment.getStaticType(), variableName, getLocation());
				
				Collection<Global> globals = RustStructType.get(getStaticType().toString()).getUnit().getInstanceGlobals(true);				
				boolean present = globals.stream().anyMatch(
						g -> g.getName().equals(variableName) && g.getStaticType().equals(variable.getStaticType()));				
				if (present) {
					AccessChild child = new AccessChild(getSubExpressions()[i].getStaticType(), deref, variable,
							getLocation());

					AnalysisState<A, H, V, T> tmp = result.bottom();

					AnalysisState<A, H, V, T> accessedChildState = startingState.smallStepSemantics(child, this);
					for (SymbolicExpression childIdentifier : accessedChildState.getComputedExpressions())
						for (SymbolicExpression exprParam : params[i])
							tmp = tmp.lub(startingState.assign(childIdentifier, exprParam, this));

					startingState = tmp;
				} else
					throw new IllegalAccessError("The field " + variableName + " in struct "
							+ RustStructType.get(getStaticType().toString()) + " does not exists");
			}

			result = result.lub(startingState.smallStepSemantics(ref, this));
		}

		return result;
	}
}

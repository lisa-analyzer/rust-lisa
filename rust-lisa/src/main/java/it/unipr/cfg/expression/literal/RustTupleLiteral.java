package it.unipr.cfg.expression.literal;

import it.unipr.cfg.type.RustType;
import it.unipr.cfg.type.composite.RustReferenceType;
import it.unipr.cfg.type.composite.RustTupleType;
import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.lattices.ExpressionSet;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.heap.AccessChild;
import it.unive.lisa.symbolic.heap.HeapDereference;
import it.unive.lisa.symbolic.heap.HeapReference;
import it.unive.lisa.symbolic.heap.MemoryAllocation;
import it.unive.lisa.symbolic.value.Variable;
import it.unive.lisa.type.Type;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Rust tuple literal.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustTupleLiteral extends NaryExpression {

	/**
	 * Build the tuple literal.
	 * 
	 * @param cfg      the {@link CFG} where this literal lies
	 * @param location the location where this literal is defined
	 * @param types    the static types of the tuples elements
	 * @param values   the values inside the literal
	 */
	public RustTupleLiteral(CFG cfg, CodeLocation location, RustType[] types, Expression[] values) {
		super(cfg, location, "()", RustTupleType.lookup(new RustTupleType(Arrays.asList(types))), values);
	}

	@Override
	public String toString() {
		return "("
				+ Arrays.asList(getSubExpressions()).stream().map(e -> e.toString()).collect(Collectors.joining(", "))
				+ ")";
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> expressionSemantics(
					InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
					ExpressionSet<SymbolicExpression>[] params, StatementStore<A, H, V, T> expressions)
					throws SemanticException {

		MemoryAllocation allocation = new MemoryAllocation(getStaticType(), getLocation(), true);
		AnalysisState<A, H, V, T> allocationState = state.smallStepSemantics(allocation, this);

		ExpressionSet<SymbolicExpression> containerExprs = allocationState.getComputedExpressions();

		AnalysisState<A, H, V, T> result = state.bottom();
		for (SymbolicExpression container : containerExprs) {
			HeapReference ref = new HeapReference(new RustReferenceType(getStaticType(), false), container,
					getLocation());
			HeapDereference deref = new HeapDereference(getStaticType(), ref, getLocation());

			AnalysisState<A, H, V, T> startingState = allocationState;
			for (int i = 0; i < getSubExpressions().length; ++i) {
				Type tupleComponentType = ((RustTupleType) getStaticType()).getTypes().get(i);

				if (tupleComponentType.canBeAssignedTo(getSubExpressions()[i].getStaticType())) {
					Variable variable = new Variable(tupleComponentType, i + "", getLocation());
					AccessChild child = new AccessChild(getSubExpressions()[i].getStaticType(), deref, variable,
							getLocation());
					AnalysisState<A, H, V, T> accessedChildState = startingState.smallStepSemantics(child, this);

					AnalysisState<A, H, V, T> tmp = state.bottom();
					for (SymbolicExpression childIdentifier : accessedChildState.getComputedExpressions())
						for (SymbolicExpression exprParam : params[i])
							tmp = tmp.lub(startingState.assign(childIdentifier, exprParam, this));

					startingState = tmp;
				} else
					throw new IllegalAccessError(
							"Element " + i + " of the tuple " + getStaticType() + " cannot be found");
			}

			result = result.lub(startingState.smallStepSemantics(ref, this));
		}

		return result;
	}

}

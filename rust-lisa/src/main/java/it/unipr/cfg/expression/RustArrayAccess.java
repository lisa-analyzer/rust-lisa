package it.unipr.cfg.expression;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.BinaryExpression;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.heap.AccessChild;
import it.unive.lisa.symbolic.heap.HeapDereference;
import it.unive.lisa.type.Untyped;

/**
 * Rust array access expression (e.g., x[y]).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustArrayAccess extends BinaryExpression {

	/**
	 * Builds the array access expression.
	 * 
	 * @param cfg        the {@link CFG} where this expression lies
	 * @param location   the location where this expression is defined
	 * @param identifier the identifier of the array
	 * @param access     the expression that lead to the integer position
	 */
	public RustArrayAccess(CFG cfg, CodeLocation location, Expression identifier,
			Expression access) {
		// TODO: need to change type of this expression
		// once we have modeled Rust types
		super(cfg, location, "[]", identifier, access);
	}

	@Override
	public String toString() {
		return getLeft() + "[" + getRight() + "]";
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
					InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
					SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
					throws SemanticException {
		AnalysisState<A, H, V, T> result = state.bottom();

		AnalysisState<A, H, V, T> rec = state.smallStepSemantics(left, this);
		for (SymbolicExpression expr : rec.getComputedExpressions()) {
			AnalysisState<A, H, V, T> tmp = rec.smallStepSemantics(
					new AccessChild(Untyped.INSTANCE,
							new HeapDereference(getStaticType(), expr, getLocation()), right, getLocation()),
					this);
			result = result.lub(tmp);
		}

		return result;
	}

}

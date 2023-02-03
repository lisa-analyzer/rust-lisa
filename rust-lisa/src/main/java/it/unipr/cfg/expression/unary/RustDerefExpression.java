package it.unipr.cfg.expression.unary;

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
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.UnaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.heap.HeapDereference;
import it.unive.lisa.type.PointerType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;

/**
 * Rust unary deref expression (e.g., *x).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustDerefExpression extends UnaryExpression {

	/**
	 * Builds the unary deref expression.
	 * 
	 * @param cfg      the {@link CFG} where this expression lies
	 * @param location the location where this expression is defined
	 * @param expr     the inner
	 */
	public RustDerefExpression(CFG cfg, CodeLocation location, Expression expr) {
		super(cfg, location, "*", expr.getStaticType().equals(Untyped.INSTANCE) ? Untyped.INSTANCE
				: ((PointerType) expr.getStaticType()).getInnerType(), expr);
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
					InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
					SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {
		
		AnalysisState<A, H, V, T> result = state.bottom();
		for (Type type : expr.getRuntimeTypes(getProgram().getTypes())) {
			HeapDereference deref = new HeapDereference(type, expr, getLocation());
						
			AnalysisState<A, H, V, T> tmp = state.smallStepSemantics(deref, this);
			for (SymbolicExpression stateExpr : tmp.getComputedExpressions()) {
				tmp = tmp.lub(result.smallStepSemantics(stateExpr, this));
			}
			result = result.lub(tmp.smallStepSemantics(deref, this));
		}
		
		return result;
	}

}

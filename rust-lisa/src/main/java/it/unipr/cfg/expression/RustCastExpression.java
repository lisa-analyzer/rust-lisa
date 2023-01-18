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
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.UnaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.Constant;
import it.unive.lisa.symbolic.value.operator.binary.TypeCast;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeTokenType;
import java.util.Collections;

/**
 * Rust type cast expression (e.g., x as Type).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustCastExpression extends UnaryExpression {

	/**
	 * Builds the cast expression.
	 * 
	 * @param cfg      the {@link CFG} where this expression lies
	 * @param location the location where this expression is defined
	 * @param type     the type to cast to
	 * @param expr     the expression to apply the cast
	 */
	public RustCastExpression(CFG cfg, CodeLocation location, Type type, Expression expr) {
		super(cfg, location, "as", type, expr);
	}

	@Override
	public String toString() {
		return getSubExpression() + " as " + getStaticType();
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
					InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
					SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {

		Constant typeCast = new Constant(new TypeTokenType(Collections.singleton(getStaticType())), getStaticType(),
				expr.getCodeLocation());

		SymbolicExpression castExpr = new it.unive.lisa.symbolic.value.BinaryExpression(getStaticType(), expr, typeCast,
				TypeCast.INSTANCE, this.getLocation());
		return state.smallStepSemantics(castExpr, this);
	}

}
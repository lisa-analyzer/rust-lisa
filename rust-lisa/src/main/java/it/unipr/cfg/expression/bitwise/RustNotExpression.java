package it.unipr.cfg.expression.bitwise;

import it.unipr.cfg.RustTyper;
import it.unipr.cfg.type.RustBooleanType;
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
import it.unive.lisa.symbolic.value.operator.unary.LogicalNegation;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;

/**
 * Rust unary not expression (e.g., !x).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustNotExpression extends UnaryExpression {

	/**
	 * Builds the unary not expression.
	 * 
	 * @param cfg      the {@link CFG} where this expression lies
	 * @param location the location where this expression is defined
	 * @param expr     the inner
	 */
	public RustNotExpression(CFG cfg, CodeLocation location,
			Expression expr) {
		// TODO: need to change type of this expression
		// once we have modeled Rust types
		super(cfg, location, "!", RustTyper.resultType(expr), expr);
	}

	@Override
	public <A extends AbstractState<A, H, V, T>,
			H extends HeapDomain<H>,
			V extends ValueDomain<V>,
			T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
					InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
					SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {
		AnalysisState<A, H, V, T> result = state.bottom();

		TypeSystem types = getProgram().getTypes();

		for (Type type : expr.getRuntimeTypes(types))
			if (type instanceof RustBooleanType)
				result = result
						.lub(state.smallStepSemantics(new it.unive.lisa.symbolic.value.UnaryExpression(getStaticType(),
								expr, LogicalNegation.INSTANCE, getLocation()), this));

		return result;
	}

}

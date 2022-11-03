package it.unipr.cfg.expression.comparison;

import it.unipr.cfg.expression.literal.RustBoolean;
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
import it.unive.lisa.program.cfg.statement.BinaryExpression;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.operator.binary.LogicalAnd;
import it.unive.lisa.type.Type;

/**
 * Rust and expression (e.g., x && y).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustAndExpression extends BinaryExpression {

	/**
	 * Builds the and expression.
	 * 
	 * @param cfg      the {@link CFG} where this expression lies
	 * @param location the location where this expression is defined
	 * @param left     the left-hand side of this expression
	 * @param right    the right-hand side of this expression
	 */
	public RustAndExpression(CFG cfg, CodeLocation location,
			Expression left, Expression right) {
		super(cfg, location, "&&", RustBooleanType.getInstance(), left, right);
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
		RustBoolean falseBoolean = new RustBoolean(getCFG(), getLocation(), false);

		for (Type leftType : left.getRuntimeTypes())
			for (Type rightType : right.getRuntimeTypes()) {
				if (leftType.isBooleanType() && rightType.isBooleanType() &&
						expressions.getState(getLeft()).getComputedExpressions().size() == 1 &&
						expressions.getState(getLeft()).satisfies(left, falseBoolean).isTop())
					return state.bottom();

				if (leftType.canBeAssignedTo(rightType) && rightType.canBeAssignedTo(leftType))
					result = result
							.lub(state.smallStepSemantics(new it.unive.lisa.symbolic.value.BinaryExpression(leftType,
									left, right, LogicalAnd.INSTANCE, getLocation()), this));
			}
		return result;
	}

}

package it.unipr.cfg.expression.numeric;

import it.unipr.cfg.RustTyper;
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
import it.unive.lisa.symbolic.value.operator.binary.NumericNonOverflowingDiv;
import it.unive.lisa.type.Type;

/**
 * Rust division expression (e.g., x / y).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustDivExpression extends BinaryExpression {

	/**
	 * Builds the division expression.
	 * 
	 * @param cfg      the {@link CFG} where this expression lies
	 * @param location the location where this expression is defined
	 * @param left     the left-hand side of this expression
	 * @param right    the right-hand side of this expression
	 */
	public RustDivExpression(CFG cfg, CodeLocation location,
			Expression left, Expression right) {
		// TODO: need to change type of this expression
		// once we have modeled Rust types
		super(cfg, location, "/", RustTyper.resultType(left, right), left, right);
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

		for (Type leftType : left.getRuntimeTypes())
			for (Type rightType : right.getRuntimeTypes())
				if (leftType.canBeAssignedTo(rightType) && rightType.canBeAssignedTo(leftType))
					result = result
							.lub(state.smallStepSemantics(new it.unive.lisa.symbolic.value.BinaryExpression(leftType,
									left, right, NumericNonOverflowingDiv.INSTANCE, getLocation()), this));

		return result;
	}

}

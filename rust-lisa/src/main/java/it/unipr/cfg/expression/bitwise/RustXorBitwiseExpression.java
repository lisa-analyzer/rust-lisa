package it.unipr.cfg.expression.bitwise;

import it.unipr.cfg.RustTyper;
import it.unipr.cfg.type.numeric.RustUnconstrainedFloat;
import it.unipr.cfg.type.numeric.RustUnconstrainedInt;
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
import it.unive.lisa.symbolic.value.operator.binary.BitwiseXor;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;

/**
 * Rust xor bitwise expression (e.g., x ^ y).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustXorBitwiseExpression extends BinaryExpression {

	/**
	 * Builds the xor bitwise expression.
	 * 
	 * @param cfg      the {@link CFG} where this expression lies
	 * @param location the location where this expression is defined
	 * @param left     the left-hand side of this expression
	 * @param right    the right-hand side of this expression
	 */
	public RustXorBitwiseExpression(CFG cfg, CodeLocation location,
			Expression left, Expression right) {
		// TODO: need to change type of this expression
		// once we have modeled Rust types
		super(cfg, location, "^", RustTyper.resultType(left, right), left, right);
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

		TypeSystem types = getProgram().getTypes();

		for (Type leftType : left.getRuntimeTypes(types))
			for (Type rightType : right.getRuntimeTypes(types)) {
				Type correctType = leftType;
				if (rightType.isNumericType() && ((leftType instanceof RustUnconstrainedInt) || (leftType instanceof RustUnconstrainedFloat)))
					correctType = rightType;
			
				if (leftType.canBeAssignedTo(correctType) && correctType.canBeAssignedTo(leftType))
					result = result
							.lub(state.smallStepSemantics(new it.unive.lisa.symbolic.value.BinaryExpression(leftType,
									left, right, BitwiseXor.INSTANCE, getLocation()), this));
			}
		
		return result;
	}

}

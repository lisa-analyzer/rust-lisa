package it.unipr.cfg;

import it.unipr.cfg.type.numeric.RustUnconstrainedFloat;
import it.unipr.cfg.type.numeric.RustUnconstrainedInt;
import it.unipr.cfg.type.numeric.signed.RustI32Type;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.BinaryExpression;
import it.unive.lisa.symbolic.value.Constant;
import it.unive.lisa.symbolic.value.operator.binary.TypeConv;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeTokenType;
import it.unive.lisa.type.Untyped;
import java.util.Collections;

/**
 * Used to statically infer the type of a expression given the types of the
 * subcomponents.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustTyper {

	/**
	 * Infers the static type of the binary expression.
	 * 
	 * @param left  the left-hand side of this expression
	 * @param right the right-hand side of this expression
	 * 
	 * @return {@link Untyped} if one of the expressions are {@link Untyped},
	 *             the common static type otherwise
	 */
	public static Type resultType(Expression left, Expression right) {
		if (left.getStaticType().equals(right.getStaticType()))
			return left.getStaticType();

		return Untyped.INSTANCE;
	}

	/**
	 * Infers the static type of the unary expression.
	 * 
	 * @param expr the left-hand side of this expression
	 * 
	 * @return {@link Untyped} if the expression is {@link Untyped}, the static
	 *             type otherwise
	 */
	public static Type resultType(Expression expr) {
		if (expr.getStaticType() instanceof Untyped)
			return Untyped.INSTANCE;

		return expr.getStaticType();
	}

	/**
	 * Types an expression: if it is unconstrained (int or float), then this
	 * method returns it as correctly typed.
	 * 
	 * @param symbolicExpression the expression to type
	 * @param type               the type to cast to
	 * 
	 * @return the typed expression
	 */
	public static SymbolicExpression type(SymbolicExpression symbolicExpression, Type type) {
		if (symbolicExpression.getDynamicType() instanceof RustUnconstrainedInt
				|| symbolicExpression.getDynamicType() instanceof RustUnconstrainedFloat) {

			Type correctType = type.commonSupertype(symbolicExpression.getDynamicType());
			Constant typeCast = new Constant(new TypeTokenType(Collections.singleton(correctType)), correctType,
					symbolicExpression.getCodeLocation());

			return new BinaryExpression(RustI32Type.getInstance(), symbolicExpression, typeCast, TypeConv.INSTANCE,
					symbolicExpression.getCodeLocation());

		} else
			return symbolicExpression;
	}
}

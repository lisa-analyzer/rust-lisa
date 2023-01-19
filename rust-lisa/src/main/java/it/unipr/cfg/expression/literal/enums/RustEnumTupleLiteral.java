package it.unipr.cfg.expression.literal.enums;

import it.unipr.cfg.type.composite.RustTupleType;
import it.unipr.cfg.type.composite.enums.RustEnumType;
import it.unipr.cfg.type.composite.enums.RustEnumVariant;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.type.Type;
import java.util.List;

/**
 * Rust enum tuple literal.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustEnumTupleLiteral extends RustEnumLiteral<NaryExpression> {

	private String variantName;

	/**
	 * Build the enum struct literal.
	 * 
	 * @param cfg         the {@link CFG} where this literal lies
	 * @param location    the location where this literal is defined
	 * @param expressions the values to be used inside the literal. During
	 *                        destructuring they can be variables, in (almost)
	 *                        any other case they can be other literals
	 * @param variantName the name of this variant
	 * @param enumType    the enum type of this literal
	 */
	public RustEnumTupleLiteral(CFG cfg, CodeLocation location, Expression[] expressions, String variantName,
			RustEnumType enumType) {
		super(cfg, location, new RustMultipleExpression(cfg, location, expressions), enumType);
		this.variantName = variantName;
	}

	@Override
	public String toString() {
		return getStaticType() + "::" + variantName + "(" + getValue() + ")";
	}

	@Override
	public boolean isInstanceOf(RustEnumVariant variant) {
		if (variant instanceof RustTupleType) {
			List<Type> types = ((RustTupleType) variant).getTypes();
			Expression[] multiple = getValue().getSubExpressions();

			if (multiple.length != types.size())
				return false;

			return true;
		}
		return false;
	}
}

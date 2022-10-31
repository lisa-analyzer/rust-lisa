package it.unipr.cfg.expression.literal.enums;

import it.unipr.cfg.expression.RustMultipleExpression;
import it.unipr.cfg.statement.RustAssignment;
import it.unipr.cfg.type.composite.RustStructType;
import it.unipr.cfg.type.composite.enums.RustEnumType;
import it.unipr.cfg.type.composite.enums.RustEnumVariant;
import it.unive.lisa.program.CompilationUnit;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Rust enum struct literal.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustEnumStructLiteral extends RustEnumLiteral<RustMultipleExpression> {

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
	public RustEnumStructLiteral(CFG cfg, CodeLocation location, RustMultipleExpression expressions, String variantName,
			RustEnumType enumType) {
		super(cfg, location, expressions, enumType);
		this.variantName = variantName;
	}

	@Override
	public String toString() {
		return getStaticType() + "::" + variantName + "{" + getValue() + "}";
	}

	@Override
	public boolean isInstanceOf(RustEnumVariant variant) {
		if (variant instanceof RustStructType) {
			CompilationUnit structUnit = ((RustStructType) variant).getUnit();

			Set<String> globalSet = structUnit.getInstanceGlobals(false).stream().map(g -> g.getName())
					.collect(Collectors.toSet());
			Set<String> fieldSet = Arrays.asList(getValue().getSubExpressions()).stream()
					.map(g -> ((RustAssignment) g).getLeft().toString()).collect(Collectors.toSet());

			return globalSet.equals(fieldSet);
		}
		return false;
	}
}

package it.unipr.cfg.expression.literal;

import it.unipr.cfg.type.numeric.RustUnconstrainedFloat;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.literal.Literal;

/**
 * Rust float literal.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustFloat extends Literal<Float> {

	/**
	 * Build the float literal.
	 * 
	 * @param cfg      the {@link CFG} where this literal lies
	 * @param location the location where this literal is defined
	 * @param value    the float value
	 */
	public RustFloat(CFG cfg, CodeLocation location, Float value) {
		super(cfg, location, value, RustUnconstrainedFloat.getInstance());
	}
}
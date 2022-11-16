package it.unipr.cfg.expression.literal;

import it.unipr.cfg.type.numeric.RustUnconstrainedInt;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.literal.Literal;

/**
 * Rust integer literal.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustInteger extends Literal<Integer> {

	/**
	 * Builds the integer literal.
	 * 
	 * @param cfg      the {@link CFG} where this literal lies
	 * @param location the location where this literal is defined
	 * @param value    the integer value
	 */
	public RustInteger(CFG cfg, CodeLocation location, Integer value) {
		super(cfg, location, value, RustUnconstrainedInt.getInstance());
	}
}
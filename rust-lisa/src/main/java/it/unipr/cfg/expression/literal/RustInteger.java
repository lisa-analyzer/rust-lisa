package it.unipr.cfg.expression.literal;

import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.literal.Literal;
import it.unive.lisa.type.Untyped;

public class RustInteger extends Literal<Integer> {

	public RustInteger(CFG cfg, CodeLocation location, Integer value) {
		// TODO: need to change type of this expression
		// once we have modeled Rust types
		super(cfg, location, value, Untyped.INSTANCE);
	}
}
package it.unipr.cfg.statement;

import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.statement.Assignment;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.Type;

/**
 * Rust assignment expression (e.g., x = y).
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustAssignment extends Assignment {

	/**
	 * Builds the assignment expression.
	 * 
	 * @param cfg      the {@link CFG} where this expression lies
	 * @param location the location where this expression is defined
	 * @param left     the left-hand side of this expression
	 * @param type     the type of this expression
	 * @param right    the right-hand side of this expression
	 */
	public RustAssignment(CFG cfg, CodeLocation location,
			Expression left, Type type, Expression right) {
		super(cfg, location, type, left, right);
	}
}

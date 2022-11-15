package it.unipr.cfg.program.unit;

import it.unive.lisa.program.AbstractClassUnit;
import it.unive.lisa.program.Program;
import it.unive.lisa.program.cfg.CodeLocation;

/**
 * Rust trait derived from {@link AbstractClassUnit}.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustTraitUnit extends AbstractClassUnit {
	private final boolean unsafe;

	/**
	 * Constructs a {@link RustTraitUnit}.
	 * 
	 * @param location the location where the trait is define within the source
	 *                     file
	 * @param program  the program where this trait is defined
	 * @param name     the name of the trait
	 * @param unsafe   whether this trait unit is unsafe or not
	 */
	public RustTraitUnit(CodeLocation location, Program program, String name, boolean unsafe) {
		super(location, program, name, false);
		this.unsafe = unsafe;
	}

	/**
	 * Yields the safety of the trait.
	 * 
	 * @return true if the trait is unsafe
	 */
	public boolean isUnsafe() {
		return unsafe;
	}
}

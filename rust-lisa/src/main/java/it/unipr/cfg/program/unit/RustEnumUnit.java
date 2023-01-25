package it.unipr.cfg.program.unit;

import it.unipr.cfg.type.composite.enums.RustEnumVariant;
import it.unive.lisa.program.ClassUnit;
import it.unive.lisa.program.Program;
import it.unive.lisa.program.cfg.CodeLocation;
import java.util.Collection;
import java.util.HashSet;

/**
 * Rust compilation unit for enums.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustEnumUnit extends ClassUnit {

	private final Collection<RustEnumVariant> variants;

	/**
	 * Construct the {@link RustEnumUnit} object.
	 * 
	 * @param location the location where the enum is define within the source
	 *                     file
	 * @param program  the program to which add this ClassUnit
	 * @param name     the name of the compilation unit
	 * @param sealed   true if the compilation unit is sealead
	 */
	public RustEnumUnit(CodeLocation location, Program program, String name, boolean sealed) {
		super(location, program, name, sealed);
		variants = new HashSet<>();
	}

	/**
	 * Yields a collection containing the variants of the enum.
	 * 
	 * @return a collection of {@link RustEnumVariant}
	 */
	public Collection<RustEnumVariant> getVariants() {
		Collection<RustEnumVariant> result = new HashSet<>();
		for (RustEnumVariant variant : variants)
			result.add(variant);

		return result;
	}

	/**
	 * Add an {@link RustEnumVariant} to the collection of this compilation
	 * unit.
	 * 
	 * @param variant the {@link RustEnumVariant} to be put inside
	 */
	public void addVariant(RustEnumVariant variant) {
		variants.add(variant);
	}

}

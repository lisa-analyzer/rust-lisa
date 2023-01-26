package it.unipr.cfg.program.cfg;

import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.Parameter;
import it.unive.lisa.type.Type;

/**
 * Implementation of a {@link Parameter} of Rust.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustParameter extends Parameter {

	private boolean mutable;

	/**
	 * Builds the parameter.
	 * 
	 * @param location   the code location of this parameter
	 * @param name       the name of this parameter
	 * @param staticType the type of this parameter
	 * @param mutable    the mutability of this parameter
	 */
	public RustParameter(CodeLocation location, String name, Type staticType, boolean mutable) {
		super(location, name, staticType);
		this.mutable = mutable;
	}

	/**
	 * Whether this parameter is mutable or not.
	 * 
	 * @return true if the parameter is mutable
	 */
	public boolean isMutable() {
		return mutable;
	}
}

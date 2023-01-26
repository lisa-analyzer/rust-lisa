package it.unipr.cfg.type.primitive;

import it.unive.lisa.type.Type;

/**
 * Interface for Rust Type.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public interface RustType extends Type {
	
	/**
	 * Yields true if this type is copiable.
	 * 
	 * @return true if this type is copiable
	 */
	public boolean isCopiable();
}

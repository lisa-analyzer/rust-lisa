package it.unipr.cfg.type.numeric.unsigned;

import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import java.util.Collection;
import java.util.Collections;

/**
 * Unique instance of the Rust usize type.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustUsizeType implements NumericType {

	/**
	 * Unique instance of Rust usize type.
	 */
	public static final RustUsizeType INSTANCE = new RustUsizeType();

	private RustUsizeType() {
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other instanceof RustUsizeType || other instanceof Untyped;
	}

	@Override
	public Type commonSupertype(Type other) {
		// Rust cast ought to be explicit by design
		// https://doc.rust-lang.org/rust-by-example/types/cast.html
		if (other instanceof RustUsizeType)
			return other;
		return Untyped.INSTANCE;
	}

	@Override
	public Collection<Type> allInstances() {
		return Collections.singleton(INSTANCE);
	}

	@Override
	public boolean is8Bits() {
		return false;
	}

	@Override
	public boolean is16Bits() {
		return false;
	}

	@Override
	public boolean is32Bits() {
		return true;
	}

	@Override
	public boolean is64Bits() {
		return true;
	}

	@Override
	public boolean isUnsigned() {
		return true;
	}

	@Override
	public boolean isIntegral() {
		return true;
	}	
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof RustUsizeType) ? true : false;
	}
	
	@Override
	public int hashCode() {
		return System.identityHashCode(INSTANCE);
	}

}

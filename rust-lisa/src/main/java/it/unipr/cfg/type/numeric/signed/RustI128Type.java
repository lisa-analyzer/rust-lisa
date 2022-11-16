package it.unipr.cfg.type.numeric.signed;

import it.unipr.cfg.type.RustType;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import java.util.Collections;
import java.util.Set;

/**
 * Unique instance of the Rust i128 type.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustI128Type implements NumericType, RustType {
	// TODO LiSA does not support 128 bits type. So no method isXXBits will be
	// true.

	private static final RustI128Type INSTANCE = new RustI128Type();

	/**
	 * Yields the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static RustI128Type getInstance() {
		return INSTANCE;
	}

	private RustI128Type() {
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other instanceof RustI128Type || other instanceof Untyped;
	}

	@Override
	public Type commonSupertype(Type other) {
		// Rust cast ought to be explicit by design
		// https://doc.rust-lang.org/rust-by-example/types/cast.html
		if (other instanceof RustI128Type)
			return other;
		return Untyped.INSTANCE;
	}

	@Override
	public Set<Type> allInstances(TypeSystem types) {
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
		return false;
	}

	@Override
	public boolean is64Bits() {
		return false;
	}

	@Override
	public boolean isUnsigned() {
		return false;
	}

	@Override
	public boolean isIntegral() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof RustI128Type;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(INSTANCE);
	}

	@Override
	public String toString() {
		return "i128";
	}

	@Override
	public boolean isIntegerType() {
		return true;
	}

	@Override
	public boolean isFloatType() {
		return false;
	}

}

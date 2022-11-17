package it.unipr.cfg.type.numeric.unsigned;

import it.unipr.cfg.type.RustType;
import it.unipr.cfg.type.numeric.RustUnconstrainedInt;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import java.util.Collections;
import java.util.Set;

/**
 * Unique instance of the Rust u32 type.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustU32Type implements NumericType, RustType {

	private static final RustU32Type INSTANCE = new RustU32Type();

	/**
	 * Yields the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static RustU32Type getInstance() {
		return INSTANCE;
	}

	private RustU32Type() {
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other instanceof RustU32Type || other instanceof Untyped;
	}

	@Override
	public Type commonSupertype(Type other) {
		// Rust cast ought to be explicit by design
		// https://doc.rust-lang.org/rust-by-example/types/cast.html
		if (other instanceof RustU32Type)
			return other;
		else if (other instanceof RustUnconstrainedInt)
			return this;
		else
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
		return true;
	}

	@Override
	public boolean is64Bits() {
		return false;
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
		return obj instanceof RustU32Type;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(INSTANCE);
	}

	@Override
	public String toString() {
		return "u32";
	}
}

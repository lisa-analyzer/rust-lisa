package it.unipr.cfg.type.numeric.unsigned;

import it.unipr.cfg.type.RustType;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import java.util.Collection;
import java.util.Collections;

/**
 * Unique instance of the Rust u64 type.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustU64Type implements NumericType, RustType {

	private static final RustU64Type INSTANCE = new RustU64Type(false);
	private static final RustU64Type MUTABLE_INSTANCE = new RustU64Type(true);

	public static RustU64Type getInstance(boolean mutability) {
		return mutability? MUTABLE_INSTANCE : INSTANCE;
	}
	
	private final boolean mutable;

	private RustU64Type(boolean mutability) {
		mutable = mutability;
	}
	
	@Override
	public boolean canBeAssignedTo(Type other) {
		return other instanceof RustU64Type || other instanceof Untyped;
	}

	@Override
	public Type commonSupertype(Type other) {
		// Rust cast ought to be explicit by design
		// https://doc.rust-lang.org/rust-by-example/types/cast.html
		if (other instanceof RustU64Type)
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
		return false;
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
		return obj instanceof RustU64Type && ((RustU64Type)obj).mutable == this.mutable;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(INSTANCE);
	}

	@Override
	public String toString() {
		return (mutable? "mut " : "") + "u64";
	}
	
	@Override
	public boolean isMutable() {
		return mutable;
	}

}

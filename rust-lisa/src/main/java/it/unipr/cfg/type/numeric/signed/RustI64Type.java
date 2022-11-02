package it.unipr.cfg.type.numeric.signed;

import java.util.Collections;
import java.util.Set;

import it.unipr.cfg.type.RustType;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;

/**
 * Unique instance of the Rust i64 type.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustI64Type implements NumericType, RustType {

	private static final RustI64Type INSTANCE = new RustI64Type();

	/**
	 * Yields the singleton instance based on mutability.
	 * 
	 * @return the correct instance based on the type mutability
	 */
	public static RustI64Type getInstance() {
		return INSTANCE;
	}

	private RustI64Type() {
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other instanceof RustI64Type || other instanceof Untyped;
	}

	@Override
	public Type commonSupertype(Type other) {
		// Rust cast ought to be explicit by design
		// https://doc.rust-lang.org/rust-by-example/types/cast.html
		if (other instanceof RustI64Type)
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
		return true;
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
		return obj instanceof RustI64Type;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(INSTANCE);
	}

	@Override
	public String toString() {
		return "i64";
	}

}

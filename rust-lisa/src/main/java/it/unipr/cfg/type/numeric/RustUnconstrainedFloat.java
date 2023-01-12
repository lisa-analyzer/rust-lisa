package it.unipr.cfg.type.numeric;

import it.unipr.cfg.type.RustType;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import java.util.Collections;
import java.util.Set;

/**
 * Unique instance of the Rust integer literal type. This class is mainly used
 * for literal parsing, since the documentation states that a literal could have
 * an unkown type until it is constrained.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustUnconstrainedFloat implements NumericType, RustType {

	private static final RustUnconstrainedFloat INSTANCE = new RustUnconstrainedFloat();

	/**
	 * Yields the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static RustUnconstrainedFloat getInstance() {
		return INSTANCE;
	}

	private RustUnconstrainedFloat() {
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other.isUntyped())
			return true;
		if (other.isNumericType())
			if (!((NumericType) other).isIntegral())
				return true;

		return false;
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other.isNumericType())
			if (!((NumericType) other).isIntegral())
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
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof RustUnconstrainedFloat;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(INSTANCE);
	}

	@Override
	public String toString() {
		return "unconstrained float";
	}

}

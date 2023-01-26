package it.unipr.cfg.type.primitive;

import it.unive.lisa.type.BooleanType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import java.util.Collections;
import java.util.Set;

/**
 * Unique instance of the Rust boolean type.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustBooleanType implements BooleanType, RustType {

	private static final RustBooleanType INSTANCE = new RustBooleanType();

	/**
	 * Yields the singleton instance.
	 * 
	 * @return the singleton instance
	 */
	public static RustBooleanType getInstance() {
		return INSTANCE;
	}

	private RustBooleanType() {
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other instanceof RustBooleanType || other instanceof Untyped;
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof RustBooleanType)
			return other;
		return Untyped.INSTANCE;
	}

	@Override
	public Set<Type> allInstances(TypeSystem types) {
		return Collections.singleton(INSTANCE);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof RustBooleanType;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(INSTANCE);
	}

	@Override
	public String toString() {
		return "bool";
	}
}

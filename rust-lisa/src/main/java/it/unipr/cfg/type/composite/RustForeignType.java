package it.unipr.cfg.type.composite;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.unipr.cfg.type.RustType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;

/**
 * Builds the a foreign type for Rust.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustForeignType implements RustType {
	private static final Map<String, RustForeignType> INSTANCES = new HashMap<>();
	
	/**
	 * Yields a unique instance (either an existing one or a fresh one) of
	 * {@link RustForeingType} representing a foreign type with the given
	 * {@code name}.
	 * 
	 * @param name the name of the foreign type
	 * 
	 * @return the unique instance of {@link RustStructType} representing the
	 *             foreign type with the given name
	 */
	public static RustForeignType lookup(String name, RustForeignType type) {
		return INSTANCES.computeIfAbsent(name, x -> new RustForeignType(name));
	}
	
	/**
	 * Retrieve a single instance of a Rust foreign types.
	 * 
	 * @param name the name of the foreign
	 * 
	 * @return all instances of a Rust foreign types
	 * 
	 * @throws IllegalArgumentException if there is no foreign with such name
	 */
	public static RustForeignType get(String name) {
		if (INSTANCES.get(name) == null)
			throw new IllegalArgumentException("There is no foreign type with name " + name);
		return INSTANCES.get(name);
	}
	
	/**
	 * Checks whether a foreign type named {@code name} has been already built.
	 * 
	 * @param name the name of the foreign type
	 * 
	 * @return whether a foreign type named {@code name} has been already built.
	 */
	public static boolean has(String name) {
		return INSTANCES.containsKey(name);
	}

	/**
	 * Remove all instances of Rust foreign type.
	 */
	public static void clearAll() {
		INSTANCES.clear();
	}

	/**
	 * Yields all instances of Rust foreign types.
	 * 
	 * @return all instances of a Rust foreign types
	 */
	public static Collection<Type> all() {
		Collection<Type> result = new HashSet<>();
		for (Type t : INSTANCES.values()) {
			result.add(t);
		}
		return result;
	}
	
	private final String name;
	
	public RustForeignType(String name) {
		this.name = name;
	}
	
	@Override
	public Type commonSupertype(Type other) {
		return (other.toString().equals(name)) ? other : Untyped.INSTANCE;
	}
	
	@Override
	public boolean canBeAssignedTo(Type other) {
		return (other.toString().equals(name));
	}
	
	@Override
	public Set<Type> allInstances(TypeSystem types) {
		Set<Type> instances = new HashSet<>();
		for (RustForeignType in : INSTANCES.values())
			instances.add(in);
		return instances;
	}
	
	@Override
	public String toString() {
		return name;
	}
}

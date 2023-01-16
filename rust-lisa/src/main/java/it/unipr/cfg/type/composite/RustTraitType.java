package it.unipr.cfg.type.composite;

import it.unipr.cfg.program.unit.RustTraitUnit;
import it.unipr.cfg.type.RustType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.UnitType;
import it.unive.lisa.type.Untyped;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Builds the Rust trait type.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustTraitType implements UnitType, RustType {

	private static final Map<String, RustTraitType> INSTANCES = new HashMap<>();

	/**
	 * Yields a unique instance (either an existing one or a fresh one) of
	 * {@link RustTraitType} representing a trait with the given {@code name},
	 * representing the given {@code unit}.
	 * 
	 * @param name the name of the trait type
	 * @param unit the {@link RustTraitUnit} underlying this type
	 * 
	 * @return the unique instance of {@link RustTraitType} representing the
	 *             trait type with the given name
	 */
	public static RustTraitType lookup(String name, RustTraitUnit unit) {
		return INSTANCES.computeIfAbsent(name, x -> new RustTraitType(name, unit));
	}

	/**
	 * Retrieve a single instance of a Rust trait.
	 * 
	 * @param name the name of the trait
	 * 
	 * @return all instances of a Rust trait types
	 * 
	 * @throws IllegalArgumentException if there is no trait with such name
	 */
	public static RustTraitType get(String name) {
		if (INSTANCES.get(name) == null)
			throw new IllegalArgumentException("There is no trait with name " + name);
		return INSTANCES.get(name);
	}

	/**
	 * Remove all instances of Rust trait types.
	 */
	public static void clearAll() {
		INSTANCES.clear();
	}

	/**
	 * Checks whether a trait type named {@code name} has been already built.
	 * 
	 * @param name the name of the trait type
	 * 
	 * @return whether a trait type named {@code name} has been already built.
	 */
	public static boolean has(String name) {
		return INSTANCES.containsKey(name);
	}

	/**
	 * Yields all instances of Rust trait types.
	 * 
	 * @return all instances of a Rust trait types
	 */
	public static Collection<Type> all() {
		Collection<Type> result = new HashSet<>();
		for (Type t : INSTANCES.values()) {
			result.add(t);
		}
		return result;
	}

	private final String name;
	private final RustTraitUnit unit;

	/**
	 * Builds the trait type.
	 * 
	 * @param name  the name of the trait type
	 * @param unit  the unit {@link RustTraitUnit} of the trait type
	 * @param types an ordered list of types inside the trait
	 */
	private RustTraitType(String name, RustTraitUnit unit) {
		this.name = name;
		this.unit = unit;
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof RustTraitType) {
			RustTraitType o = (RustTraitType) other;
			return (name.equals(o.name) && unit.equals(o.unit));
		}
		return other instanceof Untyped;
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof RustTraitType) {
			RustTraitType o = (RustTraitType) other;
			if (name.equals(o.name) && unit.equals(o.unit))
				return o;
		}
		return Untyped.INSTANCE;
	}

	@Override
	public Set<Type> allInstances(TypeSystem types) {
		Set<Type> instances = new HashSet<>();
		for (RustTraitType in : INSTANCES.values())
			instances.add(in);
		return instances;
	}

	@Override
	public RustTraitUnit getUnit() {
		return unit;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		RustTraitType other = (RustTraitType) obj;

		if (name == null) {
			if (other.name != null)
				return false;

		} else if (!name.equals(other.name))
			return false;

		if (unit == null) {
			if (other.unit != null)
				return false;

		} else if (!unit.equals(other.unit))
			return false;

		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}
}

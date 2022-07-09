package it.unipr.cfg.type.composite;

import it.unipr.cfg.type.RustType;
import it.unive.lisa.caches.Caches;
import it.unive.lisa.type.PointerType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.lisa.util.collections.externalSet.ExternalSet;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;

/**
 * Builds the Rust reference type.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustReferenceType implements PointerType, RustType {

	private ExternalSet<Type> innerTypes;

	// TODO the purpose of this field is to avoid using the cache
	// when defining the descriptors of cfgs, we have to find another
	// workaround to this problem
	private final Type innerType;
	private final boolean mutable;

	/**
	 * Builds the type for a reference to a location containing values of types
	 * {@code innerType}.
	 * 
	 * @param innerType  the type of the referenced location
	 * @param mutability the mutability of the reference
	 */
	public RustReferenceType(Type innerType, boolean mutability) {
		this.innerType = innerType;
		this.mutable = mutability;
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other instanceof PointerType;
	}

	@Override
	public Type commonSupertype(Type other) {
		return equals(other) ? this : Untyped.INSTANCE;
	}

	@Override
	public Collection<Type> allInstances() {
		return Caches.types().mkSingletonSet(this);
	}

	@Override
	public ExternalSet<Type> getInnerTypes() {
		return innerTypes != null ? innerTypes : Caches.types().mkSingletonSet(innerType);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		if (innerTypes != null)
			if (innerTypes.size() == 1)
				result = prime * result + innerTypes.first().hashCode();
			else
				result = prime * result + ((innerTypes == null) ? 0 : innerTypes.hashCode());
		else
			result = prime * result + ((innerType == null) ? 0 : innerType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof RustReferenceType))
			return false;
		RustReferenceType other = (RustReferenceType) obj;

		if (innerType == null) {
			if (other.innerType != null)
				return false;
		} else if (!innerType.equals(other.innerType))
			return false;

		if (mutable != other.mutable)
			return false;

		return true;
	}

	@Override
	public String toString() {
		if (innerTypes != null)
			if (innerTypes.size() == 1)
				return innerTypes.first() + "*";
			else
				return "[" + StringUtils.join(innerTypes, ", ") + "]*";
		return innerType + "*";
	}

	@Override
	public boolean isMutable() {
		return mutable;
	}
}

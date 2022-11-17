package it.unipr.cfg.type.composite;

import it.unipr.cfg.type.RustType;
import it.unive.lisa.type.ReferenceType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import java.util.Collections;
import java.util.Objects;

/**
 * Builds the Rust reference type.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustReferenceType extends ReferenceType implements RustType {

	private final boolean mutable;

	/**
	 * Builds the type for a reference to a location containing values of types
	 * {@code innerType}.
	 * 
	 * @param innerType the type of the referenced location
	 * @param mutable   the mutability of the reference
	 */
	public RustReferenceType(Type innerType, boolean mutable) {
		super(Collections.singleton(innerType));
		this.mutable = mutable;
	}

	@Override
	public String toString() {
		return "&" + (mutable ? "mut" : "") + getInnerTypes();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(mutable);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RustReferenceType other = (RustReferenceType) obj;
		return mutable == other.mutable;
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof Untyped)
			return true;

		if (!(other instanceof RustReferenceType))
			return false;

		ReferenceType o = (RustReferenceType) other;

		for (Type type : getInnerTypes())
			if (!(o.getInnerTypes().stream().anyMatch(otherType -> type.canBeAssignedTo(otherType))))
				return false;

		return true;
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof Untyped)
			return Untyped.INSTANCE;

		if (!(other instanceof RustReferenceType))
			return Untyped.INSTANCE;

		ReferenceType o = (RustReferenceType) other;

		for (Type type : getInnerTypes())
			if (!(o.getInnerTypes().stream().anyMatch(otherType -> type.equals(otherType))))
				return Untyped.INSTANCE;

		return this;
	}
}

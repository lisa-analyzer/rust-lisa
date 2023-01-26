package it.unipr.cfg.type.composite;

import it.unipr.cfg.type.primitive.RustType;
import it.unive.lisa.type.ReferenceType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import java.util.Objects;

/**
 * Builds the Rust sclice type.
 *
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustSliceType extends ReferenceType implements RustType {

	private final boolean mutable;

	/**
	 * Builds the type for a slice to a location containing values of types
	 * {@code innerType}.
	 * 
	 * @param innerType the type of the referenced slice
	 * @param mutable   the mutability of the slice
	 */
	public RustSliceType(Type innerType, boolean mutable) {
		super(innerType);
		this.mutable = mutable;
	}

	@Override
	public String toString() {
		return "&" + (mutable ? "mut" : "") + "[" + getInnerType() + "]";
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
		RustSliceType other = (RustSliceType) obj;
		return mutable == other.mutable;
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof Untyped)
			return true;

		if (!(other instanceof RustSliceType))
			return false;

		RustSliceType o = (RustSliceType) other;

		return getInnerType().canBeAssignedTo(o.getInnerType());
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof Untyped)
			return Untyped.INSTANCE;

		if (!(other instanceof RustSliceType))
			return Untyped.INSTANCE;

		RustSliceType o = (RustSliceType) other;

		return getInnerType().canBeAssignedTo(o.getInnerType()) ? this : Untyped.INSTANCE;
	}
}
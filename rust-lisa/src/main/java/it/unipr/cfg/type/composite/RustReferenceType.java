package it.unipr.cfg.type.composite;

import it.unipr.cfg.type.RustType;
import it.unive.lisa.type.ReferenceType;
import it.unive.lisa.type.Type;
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
	public boolean isIntegerType() {
		return false;
	}

	@Override
	public boolean isFloatType() {
		return false;
	}
	
}

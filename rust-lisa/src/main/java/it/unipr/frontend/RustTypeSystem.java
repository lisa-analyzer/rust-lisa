package it.unipr.frontend;

import it.unipr.cfg.type.RustBooleanType;
import it.unipr.cfg.type.RustStrType;
import it.unipr.cfg.type.numeric.signed.RustI32Type;
import it.unive.lisa.type.BooleanType;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.StringType;
import it.unive.lisa.type.TypeSystem;

public class RustTypeSystem extends TypeSystem {

	@Override
	public BooleanType getBooleanType() {
		return RustBooleanType.getInstance();
	}

	@Override
	public StringType getStringType() {
		return RustStrType.getInstance();
	}

	@Override
	public NumericType getIntegerType() {
		return RustI32Type.getInstance();
	}

}

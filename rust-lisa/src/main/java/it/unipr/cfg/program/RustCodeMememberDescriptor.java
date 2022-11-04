package it.unipr.cfg.program;

import it.unive.lisa.program.Unit;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.CodeMemberDescriptor;
import it.unive.lisa.program.cfg.Parameter;
import it.unive.lisa.type.Type;

/**
 * Rust code member descriptor {@link CodeMemberDescriptor}.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustCodeMememberDescriptor extends CodeMemberDescriptor {
	private final boolean unsafe;

	/**
	 * Constructs a {@link RustCodeMememberDescriptor}.
	 */
	public RustCodeMememberDescriptor(CodeLocation location, Unit unit, boolean instance, String name, Type returnType, boolean unsafe, Parameter[] formals) {
		super(location, unit, instance, name, returnType, formals);
		this.unsafe = unsafe;
	}

	/**
	 * Yields the safety of the descriptor.
	 * 
	 * @return true if the descriptor is unsafe
	 */
	public boolean isUnsafe() {
		return unsafe;
	}
}

package it.unipr.cfg.program.cfg;

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
	 * 
	 * @param location   the location where the cfg associated is define within
	 *                       the program
	 * @param unit       the {@link Unit} containing the cfg associated to this
	 *                       descriptor
	 * @param instance   whether or not the cfg associated to this descriptor is
	 *                       an instance cfg
	 * @param name       the name of the CFG associated with this descriptor
	 * @param returnType the return type of the CFG associated with this
	 *                       descriptor
	 * @param unsafe     whether the associated cfg is unsafe
	 * @param formals    the formal parameters of the CFG associated with this
	 *                       descriptor
	 */
	public RustCodeMememberDescriptor(CodeLocation location, Unit unit, boolean instance, String name, Type returnType,
			boolean unsafe, Parameter[] formals) {
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

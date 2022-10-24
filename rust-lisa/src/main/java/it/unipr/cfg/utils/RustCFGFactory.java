package it.unipr.cfg.utils;

import it.unipr.cfg.RustCFG;
import it.unive.lisa.program.cfg.CFGDescriptor;

/**
 * Produce a {@link RustCFG} with the current specified settings and
 * decorations.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustCFGFactory {
	private RustFunctionDecoratorKeeper decorators;
	private CFGDescriptor descriptor;

	/**
	 * Yields a factory for constructing a {@link RustCFG}.
	 */
	public RustCFGFactory() {

	}

	/**
	 * Set the decorators of the function.
	 * 
	 * @param decorators the new decorators
	 */
	public void setDecorators(RustFunctionDecoratorKeeper decorators) {
		this.decorators = decorators;
	}

	/**
	 * Set the descriptor used during construction of the CFG.
	 * 
	 * @param descriptor the new descriptor
	 */
	public void setDescriptor(CFGDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Constructs the {@link RustCFG} using the settings granted before.
	 * 
	 * @return the constructed CFG
	 */
	public RustCFG produce() {
		RustCFG rustCFG = new RustCFG(descriptor, decorators.getUnsafe());
		return rustCFG;
	}

}

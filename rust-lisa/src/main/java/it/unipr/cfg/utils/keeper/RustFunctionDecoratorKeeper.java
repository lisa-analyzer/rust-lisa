package it.unipr.cfg.utils.keeper;

/**
 * Keeper the decorators of the fuction.
 * 
 * @author <a href="mailto:vincenzo.arceri@unipr.it">Vincenzo Arceri</a>
 * @author <a href="mailto:simone.gazza@studenti.unipr.it">Simone Gazza</a>
 */
public class RustFunctionDecoratorKeeper {
	private String name;
	private boolean unsafe;

	/**
	 * Constructor for a {@link RustFunctionDecoratorKeeper}.
	 */
	public RustFunctionDecoratorKeeper() {

	}

	/**
	 * Yields the name of the function.
	 * 
	 * @return the name of the function
	 */
	public String getName() {
		return name;
	}

	/**
	 * Yields the safety of the function.
	 * 
	 * @return true if the function is unsafe
	 */
	public boolean isUnsafe() {
		return unsafe;
	}

	/**
	 * Set the name of the function.
	 * 
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the safety of the function.
	 * 
	 * @param unsafe the new safety
	 */
	public void setUnsafe(boolean unsafe) {
		this.unsafe = unsafe;
	}
}

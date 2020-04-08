/*
 * 
 */
package org.easylibs.options;

// TODO: Auto-generated Javadoc
/**
 * Confirmation that registration or addition succeeded. This interface allows
 * easy removal or undoing of the registration by calling the {@link #remove}
 * method.
 */
public interface Registration {

	/**
	 * Removes the registration where the original object registetered will now be
	 * unregistered. Can be called multiple times which will have no affect after
	 * the first call.
	 */
	void remove();

}

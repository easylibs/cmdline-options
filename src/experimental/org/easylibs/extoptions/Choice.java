package org.easylibs.extoptions;

public interface Choice {

	String name();

	boolean isPresent();

	default boolean isEmpty() {
		return !isPresent();
	}

}
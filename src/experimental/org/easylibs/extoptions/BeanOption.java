package org.easylibs.extoptions;

import java.lang.reflect.Field;

public interface BeanOption extends ComplexOption<Object> {

	static BeanOption of(String name, Class<?> cl) throws NoSuchFieldException, SecurityException {
		throw new UnsupportedOperationException();
	}

	static BeanOption of(String name, Object obj) throws NoSuchFieldException, SecurityException {
		throw new UnsupportedOperationException();
	}

	static BeanOption ofField(Field field) {
		throw new UnsupportedOperationException();
	}

	static BeanOption ofField(Field field, Object obj) {
		throw new UnsupportedOperationException();
	}

	static BeanOption ofField(String name, Class<?> cl) throws NoSuchFieldException, SecurityException {
		throw new UnsupportedOperationException();
	}

	static BeanOption ofField(String name, Object obj) throws NoSuchFieldException, SecurityException {
		throw new UnsupportedOperationException();
	}

	static BeanOption ofMethod(String name, Object obj, Class<?>... parameterTypes)
			throws NoSuchFieldException, SecurityException {
		throw new UnsupportedOperationException();
	}

}

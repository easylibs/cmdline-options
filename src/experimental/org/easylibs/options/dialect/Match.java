package org.easylibs.options.dialect;

import java.util.List;

import org.easylibs.extoptions.ComplexOption;

public interface Match {

	int size();

	List<String> matchedArgs();

	List<String> allArgs();

	ComplexOption<?> option();
}

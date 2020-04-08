package org.easylibs.options;

import static org.junit.Assert.*;

import org.easylibs.options.BeanUtils;
import org.junit.Test;

public class TestBeanUtils {

	@Test
	public void test_beanToName_file() {
		assertEquals("setFile", BeanUtils.beanToSetter("file"));
	}

	@Test
	public void test_beanToName_fileAndJustice() {
		assertEquals("setFileAndJustice", BeanUtils.beanToSetter("fileAndJustice"));
	}

	@Test
	public void test_beanToName_a() {
		assertEquals("setA", BeanUtils.beanToSetter("a"));
	}

	@Test
	public void test_beanToName_A() {
		assertEquals("setA", BeanUtils.beanToSetter("A"));
	}

	@Test
	public void test_setAbc() {
		assertEquals("abc", BeanUtils.dashSplitter("setAbc"));
	}

	@Test
	public void test_abcDef() {
		assertEquals("abc-def", BeanUtils.dashSplitter("abcDef"));
	}

	@Test
	public void test_setAbcDef() {
		assertEquals("abc-def", BeanUtils.dashSplitter("setAbcDef"));
	}

	@Test
	public void test_setABCuDef() {
		assertEquals("abc-u-def", BeanUtils.dashSplitter("setABCuDef"));
	}

	@Test
	public void test_ABCuDef() {
		assertEquals("abc-u-def", BeanUtils.dashSplitter("ABCuDef"));
	}

	@Test
	public void test_ABCuDef35() {
		assertEquals("abc-u-def35", BeanUtils.dashSplitter("ABCuDef35"));
	}

	@Test
	public void test_ABC35uDef35() {
		assertEquals("abc35-u-def35", BeanUtils.dashSplitter("ABC35uDef35"));
	}

}

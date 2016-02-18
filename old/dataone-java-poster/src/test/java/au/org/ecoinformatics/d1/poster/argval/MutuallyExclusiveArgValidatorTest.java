package au.org.ecoinformatics.d1.poster.argval;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.junit.Test;

import au.org.ecoinformatics.d1.poster.argval.MutuallyExclusiveArgValidator;

public class MutuallyExclusiveArgValidatorTest {

	/**
	 * Is the expected exception thrown when no arguments are supplied?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate01() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		String unsetPlaceholder = "NOT_SET";
		objectUnderTest.setArgUnsetPlaceholder(unsetPlaceholder);
		objectUnderTest.setObjectFilePath(unsetPlaceholder);
		objectUnderTest.setSysmetaFilePath(unsetPlaceholder);
		objectUnderTest.setDirectoryPath(unsetPlaceholder);
		objectUnderTest.validate();
	}

	/**
	 * Is everything ok when we only supply the Object and Sysmeta file paths?
	 */
	@Test
	public void testValidate02() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		objectUnderTest.setObjectFilePath("/some/path/to/object.xml");
		objectUnderTest.setSysmetaFilePath("/some/path/to/sysmeta.xml");
		objectUnderTest.validate();
	}
	
	/**
	 * Is everything ok when we only supply the directory path?
	 */
	@Test
	public void testValidate03() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		objectUnderTest.setDirectoryPath("/some/path/to/dir/");
		objectUnderTest.validate();
	}
	
	/**
	 * Is the expected exception thrown when all arguments are supplied?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate04() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		objectUnderTest.setObjectFilePath("/some/path/to/object.xml");
		objectUnderTest.setSysmetaFilePath("/some/path/to/sysmeta.xml");
		objectUnderTest.setDirectoryPath("/some/path/to/dir/");
		objectUnderTest.validate();
	}
	
	/**
	 * Is the expected exception thrown when an Object file is supplied but no Sysmeta file?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate05() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		objectUnderTest.setObjectFilePath("/some/path/to/object.xml");
		// don't set Sysmeta file
		objectUnderTest.validate();
	}
	
	/**
	 * Is the expected exception thrown when an Sysmeta file is supplied but no object file?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate06() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		// don't set Object file
		objectUnderTest.setSysmetaFilePath("/some/path/to/sysmeta.xml");
		objectUnderTest.validate();
	}
	
	/**
	 * Is the expected exception thrown when an Sysmeta file is supplied with a directory?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate07() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		objectUnderTest.setDirectoryPath("/some/path/to/dir/");
		objectUnderTest.setSysmetaFilePath("/some/path/to/sysmeta.xml");
		objectUnderTest.validate();
	}
	
	/**
	 * Is the expected exception thrown when an object file is supplied with a directory?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate08() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		objectUnderTest.setDirectoryPath("/some/path/to/dir/");
		objectUnderTest.setObjectFilePath("/some/path/to/object.xml");
		objectUnderTest.validate();
	}
}

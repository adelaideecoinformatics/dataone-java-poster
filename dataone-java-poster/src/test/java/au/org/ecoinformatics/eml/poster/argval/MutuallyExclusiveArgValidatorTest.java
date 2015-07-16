package au.org.ecoinformatics.eml.poster.argval;

import org.apache.jcs.access.exception.InvalidArgumentException;
import org.junit.Test;

public class MutuallyExclusiveArgValidatorTest {

	/**
	 * Is the expected exception thrown when no arguments are supplied?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate01() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		String unsetPlaceholder = "NOT_SET";
		objectUnderTest.setArgUnsetPlaceholder(unsetPlaceholder);
		objectUnderTest.setEmlFilePath(unsetPlaceholder);
		objectUnderTest.setSmdFilePath(unsetPlaceholder);
		objectUnderTest.setDirectoryPath(unsetPlaceholder);
		objectUnderTest.validate();
	}

	/**
	 * Is everything ok when we only supply the EML and SMD file paths?
	 */
	@Test
	public void testValidate02() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		objectUnderTest.setEmlFilePath("/some/path/to/eml.xml");
		objectUnderTest.setSmdFilePath("/some/path/to/smd.xml");
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
		objectUnderTest.setEmlFilePath("/some/path/to/eml.xml");
		objectUnderTest.setSmdFilePath("/some/path/to/smd.xml");
		objectUnderTest.setDirectoryPath("/some/path/to/dir/");
		objectUnderTest.validate();
	}
	
	/**
	 * Is the expected exception thrown when an EML file is supplied but no SMD file?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate05() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		objectUnderTest.setEmlFilePath("/some/path/to/eml.xml");
		// don't set SMD file
		objectUnderTest.validate();
	}
	
	/**
	 * Is the expected exception thrown when an SMD file is supplied but no EML file?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate06() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		// don't set EML file
		objectUnderTest.setSmdFilePath("/some/path/to/smd.xml");
		objectUnderTest.validate();
	}
	
	/**
	 * Is the expected exception thrown when an SMD file is supplied with a directory?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate07() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		objectUnderTest.setDirectoryPath("/some/path/to/dir/");
		objectUnderTest.setSmdFilePath("/some/path/to/smd.xml");
		objectUnderTest.validate();
	}
	
	/**
	 * Is the expected exception thrown when an EML file is supplied with a directory?
	 */
	@Test(expected=InvalidArgumentException.class)
	public void testValidate08() throws InvalidArgumentException {
		MutuallyExclusiveArgValidator objectUnderTest = new MutuallyExclusiveArgValidator();
		objectUnderTest.setDirectoryPath("/some/path/to/dir/");
		objectUnderTest.setEmlFilePath("/some/path/to/eml.xml");
		objectUnderTest.validate();
	}
}

package au.org.ecoinformatics.eml;

import java.math.BigInteger;

import org.junit.Test;

import au.org.ecoinformatics.eml.jaxb.sysmeta.AccessPolicy;
import au.org.ecoinformatics.eml.jaxb.sysmeta.AccessRule;
import au.org.ecoinformatics.eml.jaxb.sysmeta.Checksum;
import au.org.ecoinformatics.eml.jaxb.sysmeta.Identifier;
import au.org.ecoinformatics.eml.jaxb.sysmeta.Permission;
import au.org.ecoinformatics.eml.jaxb.sysmeta.Subject;
import au.org.ecoinformatics.eml.jaxb.sysmeta.SystemMetadata;

public class SystemMetadataValidatorTest {
	
	/**
	 * Is an empty SysMeta document invalid?
	 */
	@Test(expected=EmlValidationException.class)
	public void testValidate01() throws EmlValidationException {
		SystemMetadataValidator objectUnderTest = new SystemMetadataValidator();
		SystemMetadata sysMeta = new SystemMetadata();
		objectUnderTest.validate(sysMeta);
	}

	/**
	 * Is the bare minimum SysMeta document valid?
	 */
	@Test
	public void testValidate02() throws EmlValidationException {
		SystemMetadataValidator objectUnderTest = new SystemMetadataValidator();
		SystemMetadata sysMeta = new SystemMetadata()
			.withIdentifier(new Identifier()
				.withValue("some.sysmeta.1"))
			.withFormatId("eml://ecoinformatics.org/eml-2.1.1")
			.withSize(new BigInteger("123"))
			.withChecksum(new Checksum()
				.withAlgorithm("md5")
				.withValue("6f5ae290b715124ebf6f2a4debf3544c"))
			.withSubmitter(new Subject()
				.withValue("uid=ltereurope,o=LTER-Europe,dc=ecoinformatics,dc=org"))
			.withRightsHolder(new Subject()
				.withValue("uid=ltereurope,o=LTER-Europe,dc=ecoinformatics,dc=org"))
				.withAccessPolicy(new AccessPolicy()
					.withAllows(new AccessRule()
						.withSubjects(new Subject()
							.withValue("uid=ltereurope,o=LTER-Europe,dc=ecoinformatics,dc=org"))
						.withPermissions(Permission.READ, Permission.CHANGE_PERMISSION, Permission.WRITE)));
		objectUnderTest.validate(sysMeta);
	}
}

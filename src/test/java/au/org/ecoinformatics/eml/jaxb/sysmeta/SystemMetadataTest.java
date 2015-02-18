package au.org.ecoinformatics.eml.jaxb.sysmeta;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import org.junit.Test;

import au.org.ecoinformatics.eml.EmlValidationException;

public class SystemMetadataTest {

	private static final String testWrite01_EXPECTATION = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
			"<d1:systemMetadata xmlns:d1=\"http://ns.dataone.org/service/types/v1\">\n" +
			"    <identifier>someId</identifier>\n" +
			"    <formatId>eml://ecoinformatics.org/eml-2.1.1</formatId>\n" +
			"    <size>1234</size>\n" +
			"    <checksum algorithm=\"MD5\">6f5ae290b715124ebf6f2a4debf3544c</checksum>\n" +
			"    <submitter>uid=TERN,o=Ecoinformatics,dc=ecoinformatics,dc=org</submitter>\n" +
			"    <rightsHolder>uid=TERN,o=Ecoinformatics,dc=ecoinformatics,dc=org</rightsHolder>\n" +
			"    <accessPolicy>\n" +
			"        <allow>\n" +
			"            <subject>uid=TERN,o=Ecoinformatics,dc=ecoinformatics,dc=org</subject>\n" +
			"            <permission>write</permission>\n" +
			"        </allow>\n" +
			"    </accessPolicy>\n" +
			"    <replicationPolicy replicationAllowed=\"false\"/>\n" +
			"</d1:systemMetadata>\n";

	/**
	 * Can we write a SystemMetadata object out?
	 */
	@Test
	public void testWrite01() {
		SystemMetadata objectUnderTest = new SystemMetadata();
		Subject owner = new Subject().withValue("uid=TERN,o=Ecoinformatics,dc=ecoinformatics,dc=org");
		objectUnderTest
			.withIdentifier(new Identifier().withValue("someId"))
			.withFormatId("eml://ecoinformatics.org/eml-2.1.1")
			.withSize(new BigInteger("1234"))
			.withChecksum(new Checksum()
				.withValue("6f5ae290b715124ebf6f2a4debf3544c")
				.withAlgorithm("MD5"))
			.withSubmitter(owner)
			.withRightsHolder(owner)
			.withAccessPolicy(new AccessPolicy()
				.withAllows(new AccessRule()
					.withSubjects(owner)
					.withPermissions(Permission.WRITE)))
			.withReplicationPolicy(new ReplicationPolicy().withReplicationAllowed(false));
		OutputStream out = new ByteArrayOutputStream();
		objectUnderTest.write(out);
		assertEquals(testWrite01_EXPECTATION, out.toString());
	}
	
	/**
	 * Is the bare minimum SysMeta document valid?
	 */
	@Test
	public void testValidate01() throws EmlValidationException {
		SystemMetadata objectUnderTest = new SystemMetadata()
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
		objectUnderTest.validate();
	}
}

package au.org.ecoinformatics.eml.jaxb.sysmeta;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import org.junit.Test;

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
				.withAllow(new AccessRule()
					.withSubject(owner)
					.withPermission(Permission.WRITE)))
			.withReplicationPolicy(new ReplicationPolicy().withReplicationAllowed(false));
		OutputStream out = new ByteArrayOutputStream();
		objectUnderTest.write(out);
		assertEquals(testWrite01_EXPECTATION, out.toString());
	}
}

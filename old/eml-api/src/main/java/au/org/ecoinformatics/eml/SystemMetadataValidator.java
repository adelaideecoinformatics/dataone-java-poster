package au.org.ecoinformatics.eml;

import au.org.ecoinformatics.eml.jaxb.sysmeta.SystemMetadata;

public class SystemMetadataValidator extends AbstractValidator<SystemMetadata> {

	public SystemMetadataValidator() {
		super(JaxbUtils.newSysMetaInstance());
	}
}

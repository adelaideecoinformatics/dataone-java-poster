package au.org.ecoinformatics.eml.builder;

import au.org.ecoinformatics.eml.jaxb.DistributionType;
import au.org.ecoinformatics.eml.jaxb.OnlineType;
import au.org.ecoinformatics.eml.jaxb.ViewType.References;

public class DistributionTypeBuilder {

	private OnlineType online;
	private References references;

	public DistributionTypeBuilder(OnlineTypeBuilder onlineBuilder) {
		this.online = onlineBuilder.build();
	}

	// TODO support an 'offline' element
	
	// TODO support an 'inline' element
	
	public DistributionTypeBuilder(ReferencesBuilder referencesBuilder) {
		this.references = referencesBuilder.build();
	}

	public DistributionType build() {
		DistributionType result = new DistributionType();
		if (isSupplied(online)) {
			result.setOnline(online);
		}
		if (isSupplied(references)) {
			result.setReferences(references);
		}
		return result;
	}

	private boolean isSupplied(Object obj) {
		return obj != null;
	}
}

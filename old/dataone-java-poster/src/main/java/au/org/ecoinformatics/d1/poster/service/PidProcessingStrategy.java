package au.org.ecoinformatics.d1.poster.service;

public interface PidProcessingStrategy {
	boolean canHandle(String pid);
	
	String trimVersionFromPid(String pid);
	
	int extractVersionFromPid(String pid);
}

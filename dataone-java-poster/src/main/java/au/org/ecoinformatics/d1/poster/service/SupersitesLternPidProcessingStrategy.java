package au.org.ecoinformatics.d1.poster.service;

/**
 * A PID processing strategy that works for Supersites and LTERN PIDs
 */
public class SupersitesLternPidProcessingStrategy implements PidProcessingStrategy {

	private static final String POST_VERSION_SUFFIX = "/html";
	private static final String PID_VERSION_SUFFIX_REGEX = "\\.\\d{1,4}"+POST_VERSION_SUFFIX+"$";
	private static final int MIN_LENGTH_OF_PID_VERSION = 2;
	private static final int MIN_LENGTH_OF_PID_VERSION_SUFFIX = MIN_LENGTH_OF_PID_VERSION + POST_VERSION_SUFFIX.length();
	
	@Override
	public boolean canHandle(String pid) {
		if (pid.length() < MIN_LENGTH_OF_PID_VERSION_SUFFIX) {
			return false;
		}
		String lastSection = pid.substring(pid.lastIndexOf("."));
		return lastSection.matches(PID_VERSION_SUFFIX_REGEX);
	}

	@Override
	public String trimVersionFromPid(String pid) {
		return pid.replaceAll(PID_VERSION_SUFFIX_REGEX, POST_VERSION_SUFFIX);
	}

	@Override
	public int extractVersionFromPid(String pid) {
		String lastSection = pid.substring(pid.lastIndexOf(".")+1);
		return Integer.parseInt(lastSection.replace(POST_VERSION_SUFFIX, ""));
	}
}

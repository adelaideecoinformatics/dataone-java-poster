package au.org.ecoinformatics.d1.poster.service;

public class AekosPidProcessingStrategy implements PidProcessingStrategy {

	private static final String NOTHING = "";
	private static final String PID_VERSION_SUFFIX_REGEX = "\\.(20|19)\\d{6}$"; // not supporting anything from pre-1900s
	private static final int LENGTH_OF_AEKOS_PID_VERSION = 8;
	private static final int LENGTH_OF_AEKOS_PID_VERSION_SUFFIX = LENGTH_OF_AEKOS_PID_VERSION + ".".length();
	
	@Override
	public boolean canHandle(String pid) {
		if (pid.length() < LENGTH_OF_AEKOS_PID_VERSION_SUFFIX) {
			return false;
		}
		String lastSection = pid.substring(pid.length() - LENGTH_OF_AEKOS_PID_VERSION_SUFFIX);
		return lastSection.matches(PID_VERSION_SUFFIX_REGEX);
	}

	@Override
	public String trimVersionFromPid(String pid) {
		return pid.replaceAll(PID_VERSION_SUFFIX_REGEX, NOTHING);
	}

	@Override
	public int extractVersionFromPid(String pid) {
		return Integer.parseInt(pid.substring(pid.length() - LENGTH_OF_AEKOS_PID_VERSION));
	}
}

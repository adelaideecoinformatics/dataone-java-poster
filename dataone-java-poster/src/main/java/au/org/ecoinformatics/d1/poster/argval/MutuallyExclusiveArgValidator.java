package au.org.ecoinformatics.d1.poster.argval;

import java.util.HashMap;
import java.util.Map;

import org.apache.jcs.access.exception.InvalidArgumentException;

public class MutuallyExclusiveArgValidator implements ArgValidator {

	private String objectFilePath;
	private String sysmetaFilePath;
	private String directoryPath;
	/**
	 * The value that arguments that haven't been supplied will have
	 */
	private String argUnsetPlaceholder;
	private static final Map<ArgsSuppliedKey, ValidationOutcomeStrategy> validationStrategies = initStrategyMap();
	
	@Override
	public void validate() throws InvalidArgumentException {
		boolean objectFilePathSupplied = !(objectFilePath == argUnsetPlaceholder);
		boolean sysmetaFilePathSupplied = !(sysmetaFilePath == argUnsetPlaceholder);
		boolean directoryPathSupplied = !(directoryPath == argUnsetPlaceholder);
		ArgsSuppliedKey key = new ArgsSuppliedKey(objectFilePathSupplied, sysmetaFilePathSupplied, directoryPathSupplied);
		ValidationOutcomeStrategy strategy = validationStrategies.get(key);
		strategy.doOutcome();
	}

	public void setObjectFilePath(String objectFilePath) {
		this.objectFilePath = objectFilePath;
	}

	public void setSysmetaFilePath(String sysmetaFilePath) {
		this.sysmetaFilePath = sysmetaFilePath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
	
	public void setArgUnsetPlaceholder(String argUnsetPlaceholder) {
		this.argUnsetPlaceholder = argUnsetPlaceholder;
	}

	private static class ArgsSuppliedKey {
		private final boolean objectFilePathSupplied;
		private final boolean sysmetaFilePathSupplied;
		private final boolean directoryPathSupplied;

		public ArgsSuppliedKey(boolean objectFilePathSupplied, boolean sysmetaFilePathSupplied, boolean directoryPathSupplied) {
			this.objectFilePathSupplied = objectFilePathSupplied;
			this.sysmetaFilePathSupplied = sysmetaFilePathSupplied;
			this.directoryPathSupplied = directoryPathSupplied;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (directoryPathSupplied ? 1231 : 1237);
			result = prime * result + (objectFilePathSupplied ? 1231 : 1237);
			result = prime * result + (sysmetaFilePathSupplied ? 1231 : 1237);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ArgsSuppliedKey other = (ArgsSuppliedKey) obj;
			if (directoryPathSupplied != other.directoryPathSupplied)
				return false;
			if (objectFilePathSupplied != other.objectFilePathSupplied)
				return false;
			if (sysmetaFilePathSupplied != other.sysmetaFilePathSupplied)
				return false;
			return true;
		}
	}
	
	private interface ValidationOutcomeStrategy {
		void doOutcome() throws InvalidArgumentException;
	}
	
	private static Map<ArgsSuppliedKey, ValidationOutcomeStrategy> initStrategyMap() {
		Map<ArgsSuppliedKey, ValidationOutcomeStrategy> result = new HashMap<ArgsSuppliedKey, ValidationOutcomeStrategy>();
		result.put(new ArgsSuppliedKey(false, false, false), failWithFragment("but nothing was supplied"));
		result.put(new ArgsSuppliedKey(true,  false, false), failWithFragment("only Object was supplied"));
		result.put(new ArgsSuppliedKey(false, true,  false), failWithFragment("only Sysmeta was supplied"));
		result.put(new ArgsSuppliedKey(true,  true,  false), pass());
		result.put(new ArgsSuppliedKey(false, false, true),  pass());
		result.put(new ArgsSuppliedKey(false, true,  true),  failWithFragment("directory was supplied with Sysmeta"));
		result.put(new ArgsSuppliedKey(true,  false, true),  failWithFragment("directory was supplied with Object"));
		result.put(new ArgsSuppliedKey(true,  true,  true),  failWithFragment("both were supplied"));
		return result;
	}

	private static ValidationOutcomeStrategy failWithFragment(final String fragment) {
		return new ValidationOutcomeStrategy() {
			@Override
			public void doOutcome() throws InvalidArgumentException {
				throw new InvalidArgumentException("User error: you must supply either "
						+ "(object file path AND sysmeta file path) OR (directory path), " + fragment);
			}};
	}

	private static ValidationOutcomeStrategy pass() {
		return new ValidationOutcomeStrategy() {
			@Override
			public void doOutcome() throws InvalidArgumentException {
				// do nothing
			}
		};
	}
}

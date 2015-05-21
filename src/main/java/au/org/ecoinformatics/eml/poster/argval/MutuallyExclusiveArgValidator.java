package au.org.ecoinformatics.eml.poster.argval;

import java.util.HashMap;
import java.util.Map;

import org.apache.jcs.access.exception.InvalidArgumentException;

public class MutuallyExclusiveArgValidator implements ArgValidator {

	private String emlFilePath;
	private String smdFilePath;
	private String directoryPath;
	/**
	 * The value that argument that haven't been supplied will have
	 */
	private String argUnsetPlaceholder;
	private static final Map<ArgsSuppliedKey, ValidationOutcomeStrategy> validationStrategies = initStrategyMap();
	
	@Override
	public void validate() throws InvalidArgumentException {
		boolean emlFilePathSupplied = !(emlFilePath == argUnsetPlaceholder);
		boolean smdFilePathSupplied = !(smdFilePath == argUnsetPlaceholder);
		boolean directoryPathSupplied = !(directoryPath == argUnsetPlaceholder);
		ArgsSuppliedKey key = new ArgsSuppliedKey(emlFilePathSupplied, smdFilePathSupplied, directoryPathSupplied);
		ValidationOutcomeStrategy strategy = validationStrategies.get(key);
		strategy.doOutcome();
	}

	public void setEmlFilePath(String emlFilePath) {
		this.emlFilePath = emlFilePath;
	}

	public void setSmdFilePath(String smdFilePath) {
		this.smdFilePath = smdFilePath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}
	
	public void setArgUnsetPlaceholder(String argUnsetPlaceholder) {
		this.argUnsetPlaceholder = argUnsetPlaceholder;
	}

	private static class ArgsSuppliedKey {
		private final boolean emlFilePathSupplied;
		private final boolean smdFilePathSupplied;
		private final boolean directoryPathSupplied;

		public ArgsSuppliedKey(boolean emlFilePathSupplied, boolean smdFilePathSupplied, boolean directoryPathSupplied) {
			this.emlFilePathSupplied = emlFilePathSupplied;
			this.smdFilePathSupplied = smdFilePathSupplied;
			this.directoryPathSupplied = directoryPathSupplied;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (directoryPathSupplied ? 1231 : 1237);
			result = prime * result + (emlFilePathSupplied ? 1231 : 1237);
			result = prime * result + (smdFilePathSupplied ? 1231 : 1237);
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
			if (emlFilePathSupplied != other.emlFilePathSupplied)
				return false;
			if (smdFilePathSupplied != other.smdFilePathSupplied)
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
		result.put(new ArgsSuppliedKey(true,  false, false), failWithFragment("only EML was supplied"));
		result.put(new ArgsSuppliedKey(false, true,  false), failWithFragment("only SMD was supplied"));
		result.put(new ArgsSuppliedKey(true,  true,  false), pass());
		result.put(new ArgsSuppliedKey(false, false, true),  pass());
		result.put(new ArgsSuppliedKey(false, true,  true),  failWithFragment("directory was supplied with SMD"));
		result.put(new ArgsSuppliedKey(true,  false, true),  failWithFragment("directory was supplied with EML"));
		result.put(new ArgsSuppliedKey(true,  true,  true),  failWithFragment("both were supplied"));
		return result;
	}

	private static ValidationOutcomeStrategy failWithFragment(final String fragment) {
		return new ValidationOutcomeStrategy() {
			@Override
			public void doOutcome() throws InvalidArgumentException {
				throw new InvalidArgumentException("User error: you must supply either "
						+ "(eml file path AND smd file path) OR (directory path), " + fragment);
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

package au.org.ecoinformatics.d1.poster.argval;

import org.apache.jcs.access.exception.InvalidArgumentException;

public interface ArgValidator {

	/**
	 * Performs the required validation and has no side effects if it was
	 * a success otherwise an exception is thrown.
	 */
	void validate() throws InvalidArgumentException;
}

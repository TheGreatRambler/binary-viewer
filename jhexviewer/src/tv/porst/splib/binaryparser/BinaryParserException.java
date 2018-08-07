package tv.porst.splib.binaryparser;

/**
 * Exception class used to signal exceptions during binary stream parsing.
 */
public final class BinaryParserException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception object.
	 * 
	 * @param message The exception message.
	 */
	public BinaryParserException(final String message) {
		super(message);
	}
}
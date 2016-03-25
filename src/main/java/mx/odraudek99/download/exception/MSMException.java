package mx.odraudek99.download.exception;


public class MSMException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MSMException(String key) {
		super(key);
	}
}
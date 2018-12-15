package com.job.manager.exception;

public class WrappedException extends RootException {
	private static final long serialVersionUID = -8312000342891208942L;

	public WrappedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public WrappedException(String msg) {
		super(msg);
	}

	public WrappedException(Throwable cause) {
		super(cause);
	}

	public String toString() {
		String localMsg = getClass().getName() + ": " + getLocalizedMessage();
		Throwable cause = getCause();
		return localMsg
				+ (cause == null ? "" : new StringBuilder(" ").append(
						cause.getLocalizedMessage()).toString());
	}
}
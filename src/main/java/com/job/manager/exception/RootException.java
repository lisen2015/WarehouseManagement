package com.job.manager.exception;

public abstract class RootException extends RuntimeException {
	private static final long serialVersionUID = 6635864548419638870L;

	public RootException(String msg) {
		super(msg);
	}

	public RootException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public RootException(Throwable cause) {
		super(cause == null ? "cause is null!" : cause.toString(), cause);
	}
}
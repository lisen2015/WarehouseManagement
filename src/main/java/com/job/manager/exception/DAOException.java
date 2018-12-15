package com.job.manager.exception;

public class DAOException extends RootException {
	private static final long serialVersionUID = -1071863907820615751L;

	public DAOException(String msg) {
		super(msg);
	}

	public DAOException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DAOException(Throwable cause) {
		super(cause);
	}
}
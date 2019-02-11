package net.anotheria.maf.bean;

import java.util.Arrays;

/**
 * Used to transport the error.
 *
 * @author lrosenberg
 * @since 02.09.12 22:27
 */
public class ErrorBean {

	public static final String NAME = "maf.error";

	/**
	 * The original exception message.
	 */
	private String message;

	/**
	 * The stack trace of the original exception.
	 */
	private String stackTrace;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStackTrace() {
		return stackTrace;
	}

	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

	public ErrorBean(Throwable e){
		message = e.getMessage();
		stackTrace = Arrays.toString(e.getStackTrace());
	}
}

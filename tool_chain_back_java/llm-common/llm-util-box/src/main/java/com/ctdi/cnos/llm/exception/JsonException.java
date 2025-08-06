package com.ctdi.cnos.llm.exception;

/**
 * json转换异常
 * 
 * @author lw
 *
 */
public class JsonException extends RuntimeException {

	private static final long serialVersionUID = 4939472687094040061L;

	public JsonException(String message) {
		super(message);
	}

	public JsonException(Throwable e) {
		super(e);
	}

	public JsonException(String message, Throwable cause) {
		super(message, cause);
	}
}

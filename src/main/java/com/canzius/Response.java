package com.canzius;

public class Response {

	private final String message;
	private final Object output;

	public Response(String message, Object output) {
		this.message = message;
		this.output = output;
	}

	public String getMessage() {
		return this.message;
	}

	public Object getOutput() {
		return this.output;
	}
}

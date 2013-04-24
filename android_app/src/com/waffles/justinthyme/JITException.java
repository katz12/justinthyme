package com.waffles.justinthyme;

@SuppressWarnings("serial")
public class JITException extends Exception {

	public JITException(String string) {
		super(string);
	}

	public JITException(String string, Exception e) {
		super(string, e);
	}

}

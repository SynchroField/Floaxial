package com.synchrofield.library.configure;

public class ConfigureException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConfigureException(String message) {

		super(message);
	}

	public static ConfigureException of(String message) {

		return new ConfigureException(message);
	}
}

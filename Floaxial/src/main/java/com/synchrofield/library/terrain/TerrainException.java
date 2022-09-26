package com.synchrofield.library.terrain;

public class TerrainException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected TerrainException(String text) {

		super(text);
	}

	public static TerrainException of(String text) {

		return new TerrainException(text);
	}
}

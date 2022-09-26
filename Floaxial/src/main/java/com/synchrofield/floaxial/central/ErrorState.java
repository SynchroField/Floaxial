package com.synchrofield.floaxial.central;

import java.util.Optional;

// If mod fails to load it needs some way to avoid future callbacks and inform
// client of the error.  This records the error so it can be displayed to client upon connect.
public class ErrorState {

	public final boolean initializeIs;
	public final boolean errorIs;
	public final Optional<String> errorText;

	protected ErrorState(boolean initializeIs, boolean errorIs, Optional<String> errorText) {

		this.initializeIs = initializeIs;
		this.errorIs = errorIs;
		this.errorText = errorText;
	}

	public static ErrorState ofStart() {

		return new ErrorState(false, false, Optional.empty());
	}

	public ErrorState withInitializeEnd() {

		assert !errorIs : "One or more errors not caught.";

		return new ErrorState(true, errorIs, errorText);
	}

	public ErrorState withErrorSet(String text) {

		return new ErrorState(initializeIs, true, Optional.of(text));
	}

	public ErrorState withErrorClear() {

		return new ErrorState(true, false, Optional.empty());
	}
}

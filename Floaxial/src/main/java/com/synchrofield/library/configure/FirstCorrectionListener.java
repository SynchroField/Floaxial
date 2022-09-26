package com.synchrofield.library.configure;

import java.util.List;
import java.util.Optional;

import com.electronwill.nightconfig.core.ConfigSpec.CorrectionAction;
import com.electronwill.nightconfig.core.ConfigSpec.CorrectionListener;
import com.google.common.base.Joiner;

// record first key with bad value
public class FirstCorrectionListener implements CorrectionListener {

	public Optional<String> errorKey;

	protected FirstCorrectionListener() {

		this.errorKey = Optional.empty();
	}

	public static FirstCorrectionListener of() {

		return new FirstCorrectionListener();
	}

	@Override
	public void onCorrect(CorrectionAction action, List<String> keyPath, Object incorrectValue,
			Object correctedValue) {

		if (errorKey.isPresent()) {

			// already recorded first error
			return;
		}

		if (keyPath.isEmpty()) {

			errorKey = Optional.of("[unknown key]");
		}
		else {

			// first error
			errorKey = Optional.of(Joiner.on(".")
					.join(keyPath));
		}
	}
}

package com.synchrofield.library.configure;

import java.util.List;

import com.electronwill.nightconfig.core.ConfigSpec.CorrectionAction;
import com.electronwill.nightconfig.core.ConfigSpec.CorrectionListener;

public class EmptyCorrectionListener implements CorrectionListener {

	protected EmptyCorrectionListener() {

	}

	public static EmptyCorrectionListener of() {

		return new EmptyCorrectionListener();
	}

	@Override
	public void onCorrect(CorrectionAction action, List<String> keyPath, Object incorrectValue,
			Object correctedValue) {

	}
}

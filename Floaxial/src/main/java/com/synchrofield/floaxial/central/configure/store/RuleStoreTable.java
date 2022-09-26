package com.synchrofield.floaxial.central.configure.store;

import java.util.Optional;

import com.synchrofield.floaxial.server.rule.RuleTable;
import com.synchrofield.library.configure.ConfigureStore;

public class RuleStoreTable {

	public final RuleStore list[];

	protected RuleStoreTable(RuleStore list[]) {

		this.list = list;
	}

	public static RuleStoreTable of(Optional<String> folder) {

		RuleStore list[] = new RuleStore[RuleTable.ListSize];

		for (int i = 0; i < RuleTable.ListSize; i++) {

			String filename = filenameDerive(i);
			list[i] = RuleStore.of(folder, filename, i);
		}

		return new RuleStoreTable(list);
	}

	public static String filenameDerive(int ruleIndex) {

		return RuleStore.FilenameBase + ruleIndex + "." + ConfigureStore.FileExtension;
	}

	public void copyFromFile() {

		for (RuleStore store : list) {

			store.copyFromFile();
		}
	}
}

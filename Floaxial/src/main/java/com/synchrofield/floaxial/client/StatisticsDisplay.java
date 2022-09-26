package com.synchrofield.floaxial.client;

import com.synchrofield.floaxial.central.droplet.MaterialTable;
import com.synchrofield.floaxial.central.statistics.ClientStatistics;
import com.synchrofield.floaxial.central.statistics.ServerStatistics;
import com.synchrofield.floaxial.server.rule.RuleTable;
import com.synchrofield.library.client.ClientChat;

public class StatisticsDisplay {

	public int HeaderColor = 0xff55cc99;
	public int TextColor = 0xffbbbbbb;
	public int DisableColor = 0xff777777;

	protected StatisticsDisplay() {

	}

	public static StatisticsDisplay of() {

		return new StatisticsDisplay();
	}

	public String statisticsIntegerToString(String name, int value) {

		return name + ": " + Integer.toString(value) + "\n";
	}

	public String statisticsIntegerToString(String name, int value, String unit) {

		return name + ": " + Integer.toString(value) + " " + unit + "\n";
	}

	public String statisticsLongToString(String name, long value) {

		return name + ": " + Long.toString(value) + "\n";
	}

	public String statisticsStringToString(String name, String value) {

		return name + ": " + value + "\n";
	}

	public String statisticsDataSizeToString(int value) {

		return Integer.toString(value) + " " + "byte";
	}

	public String integerToString(int item) {

		return Integer.toString(item);
	}

	public String dataSizeToString(int item) {

		return Integer.toString(item) + " byte";
	}

	public void headerDisplay(ClientChat chat, String text) {

		chat.chatAdd(text, HeaderColor);
	}

	public void textDisplay(ClientChat chat, String text) {

		chat.chatAdd(text, TextColor);
	}

	public void textDisplay(ClientChat chat, String text, boolean enableIs) {

		int color = enableIs ? TextColor : DisableColor;
		chat.chatAdd(text, color);
	}

	public void fullDisplay(ClientChat chat, MaterialTable materialTable,
			ServerStatistics serverStatistics, ClientStatistics clientStatistics) {

		textDisplay(chat, "Tick server " + Long.toString(serverStatistics.level.tick) + ", client "
				+ Long.toString(clientStatistics.tick));

		textDisplay(chat,
				"Network send "
						+ statisticsDataSizeToString(serverStatistics.level.drop.networkSendByte)
						+ ",  receive "
						+ statisticsDataSizeToString(clientStatistics.drop.networkReceiveByte));

		textDisplay(chat, "Gap " + serverStatistics.level.drop.gapQueueSize + ", addMean "
				+ String.format("%,.4f", serverStatistics.level.drop.gapAddPerTickMean)
				+ ", processMean "
				+ String.format("%,.4f", serverStatistics.level.drop.gapProcessPerTickMean));

		headerDisplay(chat, materialTableHeader());

		for (int materialIndex = 0; materialIndex < MaterialTable.Size; materialIndex++) {

			textDisplay(chat, materialToLine(materialTable, serverStatistics, clientStatistics,
					materialIndex));
		}

		headerDisplay(chat, ruleTableHeader());

		for (int ruleIndex = 0; ruleIndex < RuleTable.ListSize; ruleIndex++) {

			textDisplay(chat,
					ruleToLine(materialTable, serverStatistics, clientStatistics, ruleIndex));
		}
	}

	public String materialTableHeader() {

		return "materialIndex ghost droplet ghostMean dropletMean moveMean load send receive render renderAllocate";
	}

	public String materialToLine(MaterialTable materialTable, ServerStatistics serverStatistics,
			ClientStatistics clientStatistics, int materialIndex) {

		String result = "";

		result += materialIndex;
		result += " ";
		result += serverStatistics.level.drop.material[materialIndex].levelGhost;
		result += " ";
		result += serverStatistics.level.drop.material[materialIndex].levelDrop;

		// ghost process mean
		result += " ";
		if (serverStatistics.level.drop.material[materialIndex].ghostProcessPerTickMean == 0.0f) {

			result += "0";
		}
		else {

			result += String.format("%,.4f",
					serverStatistics.level.drop.material[materialIndex].ghostProcessPerTickMean);
		}

		// droplet process mean
		result += " ";
		if (serverStatistics.level.drop.material[materialIndex].dropletProcessPerTickMean == 0.0f) {

			result += "0";
		}
		else {

			result += String.format("%,.4f",
					serverStatistics.level.drop.material[materialIndex].dropletProcessPerTickMean);
		}

		// move mean
		result += " ";
		if (serverStatistics.level.drop.material[materialIndex].movePerTickMean == 0.0f) {

			result += "0";
		}
		else {

			result += String.format("%,.4f",
					serverStatistics.level.drop.material[materialIndex].movePerTickMean);
		}

		result += " ";
		result += serverStatistics.level.drop.material[materialIndex].loadProcessDrop;
		result += " ";
		result += serverStatistics.level.drop.material[materialIndex].networkSendDrop;
		result += " ";
		result += clientStatistics.drop.material[materialIndex].networkReceiveDrop;
		result += " ";
		result += clientStatistics.drop.material[materialIndex].animateUse;
		result += " ";
		result += clientStatistics.drop.material[materialIndex].animateAllocate;

		return result;
	}

	public String ruleTableHeader() {

		return "ruleIndex section sectionPerTick samplePerTickMean";
	}

	public String ruleToLine(MaterialTable materialTable, ServerStatistics serverStatistics,
			ClientStatistics clientStatistics, int ruleIndex) {

		String result = "";

		result += ruleIndex;
		result += " ";
		result += serverStatistics.level.rule.list[ruleIndex].sectionListSize;
		result += " ";
		result += serverStatistics.level.rule.list[ruleIndex].sectionPerTick;

		result += " ";
		if (serverStatistics.level.rule.list[ruleIndex].samplePerTickMean == 0.0f) {

			result += "0";
		}
		else {

			result += String.format("%,.4f",
					serverStatistics.level.rule.list[ruleIndex].samplePerTickMean);
		}

		return result;
	}
}

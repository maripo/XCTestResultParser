package org.maripo.xctestresultparser.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.maripo.xctestresultparser.model.Config;
import org.maripo.xctestresultparser.model.RunResult;
import org.maripo.xctestresultparser.model.UnitTestHistory;

public class UnitTestHistoryGenerator extends JSONPGenerator {

	private UnitTestHistory unitTestHistory;

	public UnitTestHistoryGenerator setUnitTestHistory(UnitTestHistory history) {
		this.unitTestHistory = history;
		return this;
	}

	public Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> historyMaps = new ArrayList<Map<String, Object>>();
		List<RunResult> results = unitTestHistory.getRunResults();
		for (RunResult run: results) {
			historyMaps.add(run.getSummaryMap());
		}
		map.put("runs", historyMaps);
		return map;
	}

	public UnitTestHistoryGenerator setConfig(Config config) {
		this.config = config;
		return this;
	}

	@Override
	protected String getFileName() {
		return "history.js";
	}

	@Override
	protected String getJSFunctionName() {
		return "renderHistory";
	}

}

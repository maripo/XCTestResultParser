package org.maripo.xctestresultparser.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.maripo.xctestresultparser.model.ClassHistory;
import org.maripo.xctestresultparser.model.Config;
import org.maripo.xctestresultparser.model.MethodHistory;
import org.maripo.xctestresultparser.model.MethodResult;
import org.maripo.xctestresultparser.model.UnitTestHistory;
import org.maripo.xctestresultparser.util.LOG;

public class ClassHistoryGenerator extends JSONPGenerator {

	private UnitTestHistory history;

	public ClassHistoryGenerator setUnitTestHistory(UnitTestHistory history) {
		this.history = history;
		return this;
	}

	public ClassHistoryGenerator setConfig(Config config) {
		this.config = config;
		return this;
	}

	@Override
	public Map<String, Object> getMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> classes = new ArrayList<Map<String, Object>>();
		map.put("classes", classes);
		for (ClassHistory classHistory: history.getClassHistories()) {
			Map<String, Object> classMap = new HashMap<String, Object>();
			List<Map<String, Object>> methods = new ArrayList<Map<String, Object>>();
			classMap.put("name", classHistory.getName());
			classMap.put("methods", methods);
			for (MethodHistory methodHistory: classHistory.getMethodHistories()) {
				Map<String, Object> methodMap = new HashMap<String, Object>();
				List<Integer> results = new ArrayList<Integer>();
				methodMap.put("name", methodHistory.getName());
				methodMap.put("results", results);
				for (MethodResult.Result result: methodHistory.getResults()) {
					results.add(result.ordinal());
				}
				methods.add(methodMap);
			}
			classes.add(classMap);
		}
		return map;
	}

	@Override
	protected String getFileName() {
		return "classHistory.js";
	}

	@Override
	protected String getJSFunctionName() {
		return "renderClassHistory";
	}
	
}
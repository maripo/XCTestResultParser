package org.maripo.xctestresultparser.generator;

import java.util.HashMap;
import java.util.Map;

import org.maripo.xctestresultparser.model.Config;
import org.maripo.xctestresultparser.model.RunResult;

public class RunResultReportGenerator extends JSONPGenerator {

	private RunResult unitTestRun;
	String template = null;

	public RunResultReportGenerator setTestRun(RunResult unitTestRun) {
		this.unitTestRun = unitTestRun;
		return this;
	}
	
	// Generate
	public Map<String, Object> getMap() {
		Map<String, Object> runMap = unitTestRun.getMap();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("run", runMap);
		return map;
	}

	public RunResultReportGenerator setConfig(Config config) {
		this.config = config;
		return this;
	}

	@Override
	protected String getFileName() {
		String dateLabel = Config.JS_PARSEABLE_DATE_FORMAT.format(unitTestRun.getDate());
		return "run_" + dateLabel + ".js";
	}

	@Override
	protected String getJSFunctionName() {
		return "renderRunReport";
	}

}

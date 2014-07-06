package org.maripo.xctestresultparser.model;

import java.util.HashMap;
import java.util.Map;



public class MethodResult {
	private String name;
	private String className;
	private Result result;

	public enum Result {
		SUCCEEDED,
		FAILED,
		SKIPPED,
		UNKNOWN,
		NONEXISTENT,
	};
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public String getClassName() {
		return this.className;
	}

	public Result getResult() {
		return result;
	}
	
	Map<String, Object> map = null;
	private String resultLabel;
	

	public Map<String, Object> getMap() {
		if (map==null) {
			map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("resultLabel", resultLabel);
		}
		return map;
	}

	public void setResultLabel(String resultLabel) {
		this.resultLabel = resultLabel;
	}
}

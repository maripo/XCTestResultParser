package org.maripo.xctestresultparser.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.maripo.xctestresultparser.model.MethodResult.Result;

public class ClassResult {
	private List<MethodResult> methodResults;
	private Stat stat = null;
	public String name;
	public String targetClassName;
	
	public Date executedDate;
	private Map<String, Object> map;
	
	public String getName() {
		return this.name;
	}
	
	public ClassResult () {
		methodResults = new ArrayList<MethodResult>();
	}
	
	public void setName(String className) {
		this.name = className;
	}
	
	public void add(MethodResult methodResult) {
		methodResults.add(methodResult);
	}
	
	private Stat getStat () {
		if (stat==null) {
			stat = new Stat();
			stat.totalCount = methodResults.size();
			for (MethodResult methodResult: methodResults) {
				if (methodResult.getResult()==Result.SUCCEEDED) {
					stat.successCount ++;
				}
				if (methodResult.getResult()==Result.FAILED) {
					stat.failCount ++;
				}
				if (methodResult.getResult()==Result.SKIPPED) {
					stat.skippedCount ++;
				}
			}
		}
		return stat;
	}
	private class Stat {
		int totalCount = 0;
		int failCount = 0;
		int successCount = 0;
		int skippedCount = 0;
	}
	public int getTotalCount() {
		return getStat().totalCount;
	}
	public int getSuccessCount() {
		return getStat().successCount;
	}
	public int getFailCount() {
		return getStat().failCount;
	}

	public int getSkippedCount() {
		return getStat().skippedCount;
	}

	public Map<String, Object> getMap() {
		if (map==null) {
			createMap();
		}
		return map;
	}
	private Map<String, Object> createMap() {
		Stat stat = getStat();
		map = new HashMap<String, Object>();
		map.put("name", getName());
		Map<String, Object> statMap = new HashMap<String, Object>();
		statMap.put("total", stat.totalCount);
		statMap.put("success", stat.successCount);
		statMap.put("fail", stat.failCount);
		statMap.put("skipped", stat.skippedCount);
		map.put("summary", statMap);
		List<Map<String, Object>> methodMaps = new ArrayList<Map<String, Object>>();
		for (MethodResult methodResult: methodResults) {
			methodMaps.add(methodResult.getMap());
		}
		map.put("methods", methodMaps);
		return map;
		
	}
	public List<MethodResult> getMethodResults () {
		return this.methodResults;
	}
}

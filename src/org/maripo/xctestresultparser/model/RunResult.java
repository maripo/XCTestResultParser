package org.maripo.xctestresultparser.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.maripo.xctestresultparser.model.MethodResult.Result;


public class RunResult {
	private List<ClassResult> classResults;
	private Stat stat;
	private Map<String, Object> map;
	private Date date;

	public RunResult () {
		classResults = new ArrayList<ClassResult>();
	}
	public static RunResult create() {
		return new RunResult();
	}

	public void add(MethodResult methodResult) {
		ClassResult classResult = this.findClassByName(methodResult.getClassName());
		if (classResult==null) {
			classResult = new ClassResult();
			classResult.setName(methodResult.getClassName());
			classResults.add(classResult);
			//LOG.println("### Class: " + classResult.getName() + ", className=" + methodResult.getClassName());
		}
		classResult.add(methodResult);
	}

	private ClassResult findClassByName(String className) {
		for (ClassResult classResult: classResults) {
			if (className.equals(classResult.getName())) {
				return classResult;
			}
		}
		return null;
	}
	public List<ClassResult> getUnitTestClassResults () {
		return this.classResults;
	}
	
	private Stat getStat () {
		if (stat==null) {
			stat = new Stat();
			for (ClassResult classResult: classResults) {
				stat.totalCount += classResult.getTotalCount();
				stat.failCount += classResult.getFailCount();
				stat.successCount += classResult.getSuccessCount();
				stat.skippedCount += classResult.getSkippedCount();
			}
		}
		return stat;
	}
	class Stat {
		int totalCount = 0;
		int successCount = 0;
		int failCount = 0;
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
		Map<String, Object> statMap = new HashMap<String, Object>();
		statMap.put("date", Config.JS_PARSEABLE_DATE_FORMAT.format(date));
		statMap.put("total", stat.totalCount);
		statMap.put("success", stat.successCount);
		statMap.put("fail", stat.failCount);
		statMap.put("skipped", stat.skippedCount);
		map.put("summary", statMap);
		List<Map<String, Object>> methodMaps = new ArrayList<Map<String, Object>>();
		for (ClassResult classResult: classResults) {
			methodMaps.add(classResult.getMap());
		}
		map.put("classes", methodMaps);
		return map;
		
	}
	public void setDate(Date executedDate) {
		this.date = executedDate;
	}
	public Date getDate() {
		return date;
	}
	public Map<String, Object> getSummaryMap() {
		stat = getStat();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("date", Config.JS_PARSEABLE_DATE_FORMAT.format(date));
		map.put("total", stat.totalCount);
		map.put("success", stat.successCount);
		map.put("fail", stat.failCount);
		return map;
	}
	public Result getResultForMethod(String className, String methodName) {
		for (ClassResult classResult: classResults) {
			if (!className.equals(classResult.getName())) continue;
			for (MethodResult methodResult: classResult.getMethodResults()) {
				if (methodName.equals(methodResult.getName())) {
					return methodResult.getResult();
				}
			}
		}
		return MethodResult.Result.NONEXISTENT;
	}
}

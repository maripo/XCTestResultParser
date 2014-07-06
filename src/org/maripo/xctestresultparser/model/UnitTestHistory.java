package org.maripo.xctestresultparser.model;

import java.util.ArrayList;
import java.util.List;

public class UnitTestHistory {

	List<RunResult> runResults;
	private List<ClassHistory> classHistories;
	public UnitTestHistory () {
		runResults = new ArrayList<RunResult>();
		classHistories = new ArrayList<ClassHistory>();
	}
	public static UnitTestHistory initWithCache(String path) {
		return new UnitTestHistory();
	}

	public boolean hasCache() {
		//TODO
		return false;
	}

	//Method map
	public void add(RunResult runResult) {
		runResults.add(runResult);
	}
	public List<RunResult> getRunResults() {
		return runResults;
	}
	public void setClassHistories(List<ClassHistory> classHistories) {
		this.classHistories = classHistories;
		
	}
	public List<ClassHistory> getClassHistories() {
		return classHistories;
	}
}

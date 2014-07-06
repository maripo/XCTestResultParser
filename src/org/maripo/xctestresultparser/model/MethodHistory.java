package org.maripo.xctestresultparser.model;

import java.util.ArrayList;
import java.util.List;

import org.maripo.xctestresultparser.model.MethodResult.Result;

public class MethodHistory {
	String name;
	List<MethodResult.Result> results;
	public MethodHistory (String name) {
		this.name = name;
		this.results = new ArrayList<MethodResult.Result>();
	}
	public String getName () {
		return name;
	}
	public void setResultForIndex(int runIndex, Result result) {
		results.add(runIndex, result);
	}
	public List<MethodResult.Result> getResults () {
		return results;
	}
}

package org.maripo.xctestresultparser.model;

import java.util.ArrayList;
import java.util.List;

public class ClassHistory {

	private String name;
	private List<MethodHistory> methodHistories;

	public ClassHistory(String name) {
		this.methodHistories = new ArrayList<MethodHistory>();
		this.name = name;
	}
	public String getName () {
		return this.name;
	}
	public List<MethodHistory> getMethodHistories() {
		return methodHistories;
	}

}

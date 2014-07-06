package org.maripo.xctestresultparser.parser;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.maripo.xctestresultparser.model.ClassHistory;
import org.maripo.xctestresultparser.model.ClassResult;
import org.maripo.xctestresultparser.model.Config;
import org.maripo.xctestresultparser.model.MethodHistory;
import org.maripo.xctestresultparser.model.MethodResult;
import org.maripo.xctestresultparser.model.RunResult;
import org.maripo.xctestresultparser.model.UnitTestHistory;
import org.maripo.xctestresultparser.util.LOG;

public class UnitTestHistoryParser {

	Config config;

	public UnitTestHistoryParser setConfig(Config config) {
		this.config = config;
		return this;
	}

	public UnitTestHistory parse() {
		String testResultDir = config.getTestResultsDir();
		FileFinder.getInstance().loadFiles(testResultDir);
		Iterator<File> ite = FileFinder.getInstance().getIterator();
		UnitTestHistory history = new UnitTestHistory();
		
		List<ClassHistory> classHistories;
		
		while (ite.hasNext()) {
			File file = ite.next();
			LOG.println(file.getAbsolutePath());
			RunResult runResult;
			try {
				runResult = new UnitTestRunParser().setFile(file).parse();
				
				history.add(runResult);
				
				
			} catch (ParseException e) {
				LOG.println("Failed to \"" + file.getAbsolutePath() + "\". Skip.");
				e.printStackTrace();
				continue;
			}
		}
		
		//Create class & method map
		classHistories = createClassMethodMap(history);
		
		//Fill classHistories
		for (ClassHistory classHistory: classHistories) {
			for (MethodHistory methodHistory: classHistory.getMethodHistories()) {
				for (int runIndex=0, l=history.getRunResults().size(); runIndex<l; runIndex++) {
					RunResult result = history.getRunResults().get(runIndex);
					MethodResult.Result methodResult = result.getResultForMethod(classHistory.getName(), methodHistory.getName());
					methodHistory.setResultForIndex(runIndex, methodResult);
				}
			}
		}
		
		history.setClassHistories(classHistories);
		// Create method history
		return history;
	}

	private List<ClassHistory> createClassMethodMap(UnitTestHistory history) {
		
		List<ClassHistory> classHistories = new ArrayList<ClassHistory>();
		for (RunResult runResult: history.getRunResults()) {
			for (ClassResult classResult: runResult.getUnitTestClassResults()) {
				if (classResult.getName()==null) continue;
				ClassHistory classHistory = null;
				for (ClassHistory existingClassHistory: classHistories) {
					if (classResult.getName().equals(existingClassHistory.getName())) {
						classHistory = existingClassHistory;
						break;
					}
				}
				if (classHistory==null) {
					classHistory = new ClassHistory(classResult.getName());
					classHistories.add(classHistory);
				}
				for (MethodResult methodResult: classResult.getMethodResults()) {
					if (methodResult.getName()==null) continue;
					MethodHistory methodHistory = null;
					//Add method map
					for (MethodHistory existingMethodHistory: classHistory.getMethodHistories()) {
						if (existingMethodHistory.getName().equals(methodResult.getName())) {
							methodHistory = existingMethodHistory;
							break;
						}
					}
					if (methodHistory==null) {
						classHistory.getMethodHistories().add(new MethodHistory(methodResult.getName()));
					}
				}
			}
		}
		return classHistories;
	}
}

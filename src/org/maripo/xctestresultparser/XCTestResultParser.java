package org.maripo.xctestresultparser;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.maripo.xctestresultparser.exception.XCTestResultParserException;
import org.maripo.xctestresultparser.generator.ClassHistoryGenerator;
import org.maripo.xctestresultparser.generator.RunResultReportGenerator;
import org.maripo.xctestresultparser.generator.UnitTestHistoryGenerator;
import org.maripo.xctestresultparser.model.Config;
import org.maripo.xctestresultparser.model.RunResult;
import org.maripo.xctestresultparser.model.UnitTestHistory;
import org.maripo.xctestresultparser.parser.UnitTestHistoryParser;
import org.maripo.xctestresultparser.util.LOG;

public class XCTestResultParser {

	public static void main (String[] args) {
		
		// Parse command line params
		Config config;
		try {
			config = Config.createWithArgs(args);
		} catch (XCTestResultParserException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		// Parse result.plist
		UnitTestHistory history = new UnitTestHistoryParser().setConfig(config).parse();
		
		// Generate jsonp files
		
		// Generate history JSONP for coverage graph
		try {
			new UnitTestHistoryGenerator().setUnitTestHistory(history).setConfig(config).generate();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//Finish with error
			return;
		}
		
		// Generate classes & method history JSONP
		try {
			new ClassHistoryGenerator().setUnitTestHistory(history).setConfig(config).generate();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			//Finish with error
			return;
		}
		
		// Generate detailed reports
		for (RunResult runResult: history.getRunResults()) {
			try {
				new RunResultReportGenerator().setTestRun(runResult).setConfig(config).generate();
			} catch (IOException e) {
				e.printStackTrace();
				//Finish with error
				return;
			}
		}
	}
}

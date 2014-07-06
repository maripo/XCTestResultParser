package org.maripo.xctestresultparser.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.maripo.xctestresultparser.exception.HelpRequiredException;
import org.maripo.xctestresultparser.exception.MissingParamException;
import org.maripo.xctestresultparser.exception.NoSuchParameterException;
import org.maripo.xctestresultparser.util.LOG;

public class Config {
	
	private String outputJsonpDir;
	private String testResultsDir;
	
	// Param keys
	private static final String PARAM_HELP = "help";
	private static final String PARAM_OUTPUT_JSONP_DIR = "outputJsonDir";
	private static final String PARAM_TEST_RESULTS_DIR = "testResultsDir";
	
	// Default values
	private static final String DEFAULT_OUTPUT_JSONP_DIR = "./xctestresultparser/jsonp";
	
	public static final DateFormat JS_PARSEABLE_DATE_FORMAT 
		= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

	static {
		JS_PARSEABLE_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
	};
	
	public static Config getDefaultConfig() {
		Config config = new Config();
		config.outputJsonpDir = DEFAULT_OUTPUT_JSONP_DIR;
		return config;
	}
	
	// Init with command-line args
	public static Config createWithArgs(String[] args) throws MissingParamException, NoSuchParameterException, HelpRequiredException {
		Config config = getDefaultConfig();
		
		Pattern pattern = Pattern.compile("--(.+?)=(.+?)");
		for (String arg: args) {
			Matcher matcher = pattern.matcher(arg);
			if (matcher.matches()) {
				String key = matcher.group(1);
				String value = matcher.group(2);
				
				if (PARAM_OUTPUT_JSONP_DIR.equals(key)) {
					LOG.println("output dir=" + value);
					config.outputJsonpDir = value;
				}
				else if (PARAM_TEST_RESULTS_DIR.equals(key)) {
					LOG.println("result dir=" + value);
					config.testResultsDir = value;
				}
				else {
					throw new NoSuchParameterException(arg);
				}
			} else {
				if (("--"+PARAM_HELP).equals(arg)) {
					throw new HelpRequiredException();
				} else {
					throw new NoSuchParameterException(arg);
				}
			}
		}
		
		if (config.getTestResultsDir()==null) {
			throw new MissingParamException(PARAM_TEST_RESULTS_DIR);
		}
		
		return config;
	}

	/**
	 * Returns message for "--help" option
	 * @return descriptions for all options
	 */
	public static String getHelpMessage() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("--");
		sb.append(PARAM_TEST_RESULTS_DIR);
		sb.append("\t");
		sb.append("[required] Specify container directory of \"result.plist\" files\n");
		
		sb.append("--");
		sb.append(PARAM_OUTPUT_JSONP_DIR);
		sb.append("\t");
		sb.append("Specify output directory path of JSONP files\n");
		
		sb.append("--");
		sb.append(PARAM_HELP);
		sb.append("\t");
		sb.append("Show help\n");
		return sb.toString();
	}
	
	public String getOutputJsonpDir () {
		return this.outputJsonpDir;
	}
	public String getTestResultsDir() {
		return this.testResultsDir;
	}

}

package org.maripo.xctestresultparser.parser;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.maripo.xctestresultparser.model.Config;

public class FileFinder {

	private static FileFinder instance = null;
	private List<File> resultFiles = null;
	private FileFinder () {
		
	}
	public static FileFinder getInstance() {
		if (instance==null) {
			instance = new FileFinder();
		}
		return instance;
	}
	
	DateFormat FILE_NAME_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss Z");
	
	public void loadFiles(String testResultDir, Config config) {
		Pattern patternTestResult = Pattern.compile("([^\\/]+)\\.xctestresults");
		resultFiles = new ArrayList<File>();
		File dir = new File(testResultDir);
		List<File> resultDirectories = new ArrayList<File>();
		for (File file: dir.listFiles()) {
			Matcher matcher = patternTestResult.matcher(file.getName());
			if (matcher.find()) {
				System.out.println(matcher.group(1));
				try {
					Date date = FILE_NAME_DATE_FORMAT.parse(matcher.group(1));
					if ((config.getSince()==null || config.getSince().getTime()<=date.getTime())
							&& (config.getUntil()==null || config.getUntil().getTime()>=date.getTime()))
					resultDirectories.add(file);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		Collections.sort(resultDirectories, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.lastModified() < o2.lastModified() ? -1:1;
			}
		});
		for (File resultDir: resultDirectories) {
			for (File resultFile: resultDir.listFiles()) {
				if ("results.plist".equals(resultFile.getName())) {
					resultFiles.add(resultFile);
				}
			}
		}
	}
	public Iterator<File> getIterator() {
		return new Iterator<File>() {
			int index = 0;
			@Override
			public boolean hasNext() {
				return index < resultFiles.size();
			}

			@Override
			public File next() {
				return resultFiles.get(index++);
			}

			@Override
			public void remove() {
				
			}
		};
	}

}

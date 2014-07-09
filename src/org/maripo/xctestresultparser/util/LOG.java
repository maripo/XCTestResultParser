package org.maripo.xctestresultparser.util;

import org.maripo.xctestresultparser.model.Config;

public class LOG {
	public static void println(String str, Config conf) {
		if (conf!=null && conf.isVerbose()) {
			System.out.println(str);
		}
	}
}
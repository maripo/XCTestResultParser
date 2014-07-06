package org.maripo.xctestresultparser.generator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;

import org.json.simple.JSONObject;
import org.maripo.xctestresultparser.model.Config;

public abstract class JSONPGenerator {

	protected Config config;

	public void generate() throws FileNotFoundException {
		StringBuilder sb = new StringBuilder();
		sb.append(getJSFunctionName());
		sb.append("(");
		sb.append(new JSONObject(getMap()).toJSONString());
		sb.append(");");
		PrintWriter out = new PrintWriter(config.getOutputJsonpDir() + "/" + getFileName());
		out.print(sb.toString());
		out.close();
	}
	abstract protected String getFileName();
	abstract protected String getJSFunctionName();
	abstract Map<String, Object> getMap();
}

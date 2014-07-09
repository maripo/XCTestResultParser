package org.maripo.xctestresultparser.parser;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.maripo.xctestresultparser.model.MethodResult;
import org.maripo.xctestresultparser.model.MethodResult.Result;
import org.maripo.xctestresultparser.model.RunResult;
import org.maripo.xctestresultparser.util.LOG;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UnitTestRunParser extends XMLParser {

	private File file;

	public UnitTestRunParser setFile(File file) {
		this.file = file;
		return this;
	}

	public static final String KEY_TEST_NAME = "Test Name";
	public static final String KEY_TEST_IDENTIFIER = "Test Identifier";
	public static final String KEY_TEST_RESULT = "Test Result";
	private static Pattern fileNamePattern = Pattern.compile(".*\\/([^\\/]+)\\.xctestresults");
	private static final DateFormat fileNameDateFormat = 
			new SimpleDateFormat("yyyy-MM-dd HH.mm.ss Z");
	/**
	 * @return
	 * @throws ParseException 
	 */
	public RunResult parse() throws ParseException {
		
		Matcher matcher = fileNamePattern.matcher(file.getPath());
		matcher.find();
		String datePart = matcher.group(1);
		Date executedDate = fileNameDateFormat.parse(datePart);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		
		RunResult unitTestRun = RunResult.create();
		unitTestRun.setDate(executedDate);
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			//plist.dict.array.dict.array
			NodeList plists = doc.getElementsByTagName("plist");
			NodeWrapper plistNode = new NodeWrapper(plists.item(0));
			
			Node arrayNode = plistNode.getChildNodeWithName("dict")
					.getChildNodeWithName("array")
					.getChildNodeWithName("dict")
					.getChildNodeWithName("array")
					.getNode();
			for (int i=0,l=arrayNode.getChildNodes().getLength(); i<l; i++) {
				NodeWrapper dict = new NodeWrapper(arrayNode.getChildNodes().item(i));
				if (!"dict".equals(dict.getNode().getNodeName())) continue;
				Map<String, String> map = dict.createKeyStringMap();
				
				MethodResult methodResult = new MethodResult();
				if (map.containsKey(KEY_TEST_NAME)) {
					methodResult.setName(map.get(KEY_TEST_NAME));
				}
				String className;
				if (map.containsKey(KEY_TEST_IDENTIFIER)) {
					String identifier = map.get(KEY_TEST_IDENTIFIER);
					String[] tokens = identifier.split("\\/");
					if (tokens.length==2) {
						className = tokens[0];
						methodResult.setClassName(className);
					}
				}
				String result = map.get(KEY_TEST_RESULT);
				methodResult.setResultLabel(result);
				if ("Succeeded".equals(result)) {
					methodResult.setResult(Result.SUCCEEDED);
				} else if ("Failed".equals(result)) {
					methodResult.setResult(Result.FAILED);
				} else if ("Skipped".equals(result)) {
					methodResult.setResult(Result.SKIPPED);
				} else {
					methodResult.setResult(Result.UNKNOWN);
				}
				unitTestRun.add(methodResult);
			}
			return unitTestRun;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public class NodeWrapper {
		public Node getNode() {
			return node;
		}
		public Map<String, String> createKeyStringMap() {
			String key = null;
			String value = null;
			Map<String, String> map = new HashMap<String, String>();
			for (int i=0, l=node.getChildNodes().getLength(); i<l; i++) {
				Node childNode = node.getChildNodes().item(i);
				if ("key".equals(childNode.getNodeName())){
					key = childNode.getFirstChild().getTextContent();
				} else if ("string".equals(childNode.getNodeName())) {
					value = childNode.getFirstChild().getTextContent();
					if (key!=null && value!=null) {
						map.put(key, value);
					}
					key = null;
				}
			}
			return map;
		}
		public NodeWrapper(Node node) {
			this.node = node;
		}
		public NodeWrapper getChildNodeWithName(String tagName) {
			for (int i=0, l=node.getChildNodes().getLength(); i<l; i++) {
				Node childNode = node.getChildNodes().item(i);
				if (tagName.equals(childNode.getNodeName())){
					return new NodeWrapper(childNode);
				}
			}
			return null;
		}
		public Node node;
	}
}
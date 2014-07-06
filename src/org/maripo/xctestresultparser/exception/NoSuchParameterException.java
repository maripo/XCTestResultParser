package org.maripo.xctestresultparser.exception;

public class NoSuchParameterException extends XCTestResultParserException {

	private String param;

	public NoSuchParameterException(String param) {
		this.param = param;
	}
	
	@Override
	public String getMessage () {
		return "Illegal option \"" + param + "\".\nFor further information, please use \"--help\" option."; 
	}

}

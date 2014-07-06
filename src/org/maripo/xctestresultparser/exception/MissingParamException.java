package org.maripo.xctestresultparser.exception;

public class MissingParamException extends XCTestResultParserException {

	private String param;

	public MissingParamException(String param) {
		this.param = param;
	}
	
	@Override
	public String getMessage () {
		return "Missing option \"" + param + "\".\nFor further information, please use \"--help\" option.";
	}

}

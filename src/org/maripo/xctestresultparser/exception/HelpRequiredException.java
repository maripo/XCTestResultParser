package org.maripo.xctestresultparser.exception;

import org.maripo.xctestresultparser.model.Config;

public class HelpRequiredException extends XCTestResultParserException {
	
	@Override
	public String getMessage() {
		return Config.getHelpMessage();
	}
}

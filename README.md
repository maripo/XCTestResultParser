#XCTestResultParser

stand-alone covarage graph creator for XCTest framework.

## Usage

1. Download & place xctestresultparser.jar
2. Download 
$ java -cp xctestresultparser.jar org.maripo.xctestresultparser.XCTestResultParser --testResultsDir=~/Library/Developer/Xcode/DerivedData/YOUR_SWEET_APP-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx/TestResults


## How can I find the path for test results?

Usually your project directory is located in ~/Library/Developer/Xcode/DerivedData.
After finding your project directory, you'll find a directory named "TestResults". It contains your test result files.

## Thanks to...
* json-simple https://code.google.com/p/json-simple/
* date.js http://www.coolite.com/datejs/
* famfamfam silk icons http://www.famfamfam.com/
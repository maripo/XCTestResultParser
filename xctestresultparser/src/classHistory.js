var ClassHistory = function (history) {
	this.methodMap = [];
	this.methods = [];
	for (var i=0, l=history.classes.length; i<l; i++) {
		var classHistory = history.classes[i];
		for (var j=0, ll=classHistory.methods.length; j<ll ; j++){
			var methodHistory = classHistory.methods[j];
			var key = classHistory.name + ":" + methodHistory.name;
			var obj = new MethodHistory(classHistory.name, methodHistory.name, methodHistory.results);
			this.methodMap[key] = obj;
			this.methods.push(obj);
		}
	}
};
ClassHistory.prototype.findByMethodName = function (key /* className:methodName */) {
	console.log(key)
	return this.methodMap[key];
}
ClassHistory.prototype.forEachIncident = function (index, callback) {
	for (var i=0; i<this.methods.length; i++) {
		var incident = this.methods[i].getIncident(index);
		if (incident) {
			callback(incident);
		}
	}
};

var RESULT_SUCCEEDED = 0;
var RESULT_FAILED = 1;
var RESULT_SKIPPED = 2;
var RESULT_UNKNOWN = 3;
var RESULT_NONEXISTENT = 4;
var RESULT_CREATED = 5;

var MethodHistory = function (className, methodName, results) {
	this.className = className;
	this.methodName = methodName;
	this.results = results;
};

MethodHistory.prototype.getLatestEvent = function () {
	if (this.latestEvent) return this.latestEvent;
	// "Green since XXX", "Red since XXX", ""
	latestResult = -1;
	for (var i=this.results.length-1; i>0; i--) {
		var result = this.results[i];
		var prevResult = this.results[i-1];
		if (latestResult < 0 && (result==RESULT_SUCCEEDED || result==RESULT_FAILED)) {
			latestResult = result;
		}
		if (latestResult >= 0 && prevResult != latestResult && 
				(prevResult==RESULT_SUCCEEDED || prevResult==RESULT_FAILED)) {
			this.latestEvent = 
			{
				result: latestResult,
				since: dates[i]
			};
			break;
		}
	}
	if (!this.latestEvent) {
		this.latestEvent = {result:-1, since:null};
	}
	return this.latestEvent;
	
};

MethodHistory.prototype.getCreated = function () {
	if (this.created) return this.created;
	for (var i=0; i<this.results.length; i++) {
		if (this.results[i] != RESULT_NONEXISTENT) {
			this.created = dates[i];
			return this.created;
		}
	}
};


MethodHistory.prototype.getIncident = function (index) {
	var result = this.results[index];
	var prevResult = -1;
	
	var isNew = (RESULT_SUCCEEDED==result || RESULT_FAILED==result);
	for (var i=index-1; i>=0; i--) {
		if (RESULT_SUCCEEDED==this.results[i] || RESULT_FAILED==this.results[i]) {
			prevResult = this.results[i];
			isNew = false;
			break;
		}
	}
	// Check green<=>red
	if (prevResult>=0 && result!=prevResult && (RESULT_SUCCEEDED==result || RESULT_FAILED==result)) {
		return {class:this.className, method:this.methodName, result:result};
	}
	// Check method creation
	if (isNew) {
		return {class:this.className, method:this.methodName, result:RESULT_CREATED};
	}
	return null;
};
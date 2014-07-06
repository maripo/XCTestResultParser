var ClassHistory = function (history) {
	this.methodMap = [];
	this.methods = [];
	for (var i=0, l=history.classes.length; i<l; i++) {
		var classHistory = history.classes[i];
		for (var j=0, ll=classHistory.methods.length; j<ll ; j++){
			var methodHistory = classHistory.methods[j];
			var key = classHistory + ":" + methodHistory;
			var obj = new MethodHistory(classHistory.name, methodHistory.name, methodHistory.results);
			this.methodMap[key] = obj;
			this.methods.push(obj);
		}
	}
};
ClassHistory.prototype.findByMethodName = function (key /* className:methodName */) {
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

var classHistory;
var selectedIndex = 0;
var isLoading = false;

var MethodHistory = function (className, methodName, results) {
	this.className = className;
	this.methodName = methodName;
	this.results = results;
};
var RESULT_SUCCEEDED = 0;
var RESULT_FAILED = 1;
var RESULT_SKIPPED = 2;
var RESULT_UNKNOWN = 3;
var RESULT_NONEXISTENT = 4;
var RESULT_CREATED = 5;

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

function renderClassHistory (history) {
	classHistory = new ClassHistory(history);
	loadJSONP("jsonp/history.js");
}

function renderRunReport (data) {
	var summary = data["run"]["summary"];
	
	write("labelRunDate", new Date(summary.date).toString("yyyy/MM/dd HH:mm:ss"));
	write("labelRunTotal", summary.total || 0);
	write("labelRunSuccess", summary.success || 0);
	write("labelRunSkipped", summary.skipped || 0);
	write("labelRunFail", summary.fail);
	
	var redPercentage = 100 * summary.fail / Math.max(1, summary.total);
	var greenPercentage = 100 * summary.success / Math.max(1, summary.total);
	element("summaryGraphRed").style.width = Math.round(redPercentage) + "%";
	element("summaryGraphGreen").style.width = Math.round(greenPercentage) + "%";
	
	renderRunDetailTable (data);
	element("alertList").innerHTML = "";
	classHistory.forEachIncident(selectedIndex, function(incident){
		var message = incident.class + "." + incident.method;
		var li = document.createElement("li");
		switch (incident.result) {
			case (RESULT_SUCCEEDED): {
					message += " turned GREEN";
					li.className = "turnedGreen";
					break;
				}
			case (RESULT_FAILED): {
					message += " turned RED";
					li.className = "turnedRed";
					break;
				}
			case (RESULT_CREATED): {
					message += " was CREATED";
					li.className = "created";
					break;
				}
			default: break;
		}
		li.innerHTML = message;
		element("alertList").appendChild(li);
	});
	isLoading = false;
}

function renderRunDetailTable (data) {
	var table = element("runTable");
	table.innerHTML = '';
	var classes = data["run"]["classes"];
	for (i=0; i<classes.length;i++) {
	
		var cls = classes[i];
		table.appendChild(createClassRow(cls));
		
		var methods = cls["methods"];
		for (j=0; j<methods.length; j++) {
			var method = methods[j];
			table.appendChild(createMethodRow(method));
		}
	}
}

function createClassRow (classResult) {
	var tr = document.createElement("tr");
	var td = createCell(classResult.name, "class");
	td.colSpan = 2;
	tr.appendChild(td);
	return tr;
}

function createMethodRow (methodResult) {
	var tr = document.createElement("tr");
	tr.className = 'methodRow ' + methodResult.resultLabel;
	tr.appendChild(createCell(methodResult.name, 'methodName'));
	tr.appendChild(createCell(methodResult.resultLabel, methodResult.resultLabel));
	return tr;
}

function createCell (text, styleClass) {
	var td = document.createElement("td");
	td.innerHTML = text;
	td.className = styleClass;
	return td;
}

function renderHistory (data) {
	var runs = data["runs"];
	var historyGraph = new HistoryGraph(runs, 
		element('runGraph'), 
		element('runGraphDates'));
	historyGraph.setOnFocus(element("canvasCover"), function (run) {
		if (!run) return;
	});
	historyGraph.setOnClick(element("canvasCover"), function (index, run) {
		if (isLoading) return;
		var jsonpURL = "jsonp/run_" + run.date + ".js";
		console.log(jsonpURL);
		selectedIndex = index;
		loadJSONP(jsonpURL);
		isLoading = true;
	});
	historyGraph.render();
}


function loadJSONP (src) {
	var elm = document.createElement("script");
	elm.src = src;
	elm.type = "text/javascript";
	document.body.appendChild(elm);
}

function testJSONP () {
	loadJSONP("jsonp/run_20140620104258.js");
}

function element (elementId) {
	var element = document.getElementById(elementId);
	return element;
}

function write (targetElement, text) {
	element(targetElement).innerHTML = text;
}
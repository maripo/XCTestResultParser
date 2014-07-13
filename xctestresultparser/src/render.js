var classHistory;
var selectedIndex = 0;
var isLoading = false;
var dates = [];

/** Render contents of "classHistory.js" */
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
	
	// Render alert event list
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
	clearTable(table);
	var classes = data["run"]["classes"];
	for (i=0; i<classes.length;i++) {
	
		var cls = classes[i];
		table.appendChild(createClassRow(cls));
		
		var methods = cls["methods"];
		for (j=0; j<methods.length; j++) {
			var method = methods[j];
			var methodHistory = classHistory.findByMethodName(cls.name + ":" + method.name);
			table.appendChild(createMethodRow(method, methodHistory));
		}
	}
}

function clearTable (table) {
	while (rows = table.getElementsByTagName("tr"), rows.length>1) {
		rows[1].parentNode.removeChild(rows[1]);
	}
}

function getFoldAction (classResult) {
	return function () {
		alert("todo");
	}
}


function createClassRow (classResult) {
	var tr = document.createElement("tr");
	var td = createCell(classResult.name, "class");
	td.colSpan = 4;
	tr.appendChild(td);
	
	//fold button
	var foldButton = document.createElement("input");
	foldButton.type = "button";
	foldButton.className = ("foldButton open");
	foldButton.addEventListener("click", getFoldAction(classResult));
	td.appendChild(foldButton);
	return tr;
}

function createMethodRow (methodResult, methodHistory) {
	var tr = document.createElement("tr");
	tr.className = 'methodRow ' + methodResult.resultLabel;
	tr.appendChild(createCell(methodResult.name, 'method'));
	tr.appendChild(createCell(methodResult.resultLabel, methodResult.resultLabel));
	
	// Created
	var created = (methodHistory && methodHistory.getCreated())?
			methodHistory.getCreated().toString("yyyy/MM/dd HH:mm"):"";
	tr.appendChild(createCell(created, "created"));
	// Latest event
	var latestEventLabel = "";
	if (methodHistory) {
		var latestEvent = methodHistory.getLatestEvent();
		if (latestEvent.since) {
			if (latestEvent.result==RESULT_SUCCEEDED) {
				latestEventLabel = "Turned Green at " + latestEvent.since.toString("yyyy/MM/dd HH:mm");
			}
			if (latestEvent.result==RESULT_FAILED) {
				latestEventLabel = "Turned Red at " + latestEvent.since.toString("yyyy/MM/dd HH:mm");
			}
		}
	}
	
	tr.appendChild(createCell(latestEventLabel, "latest"));
	return tr;
}

function createCell (text, styleClass) {
	var td = document.createElement("td");
	td.innerHTML = text;
	if (styleClass) {
		td.className = styleClass;
	}
	
	return td;
}

/** Render contents of "history.js" */
function renderHistory (data) {
	var runs = data["runs"];
	//dates
	for (var i=0; i<runs.length; i++) {
		dates[i] = new Date(runs[i].date);
	}
	
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

function element (elementId) {
	var element = document.getElementById(elementId);
	return element;
}

function write (targetElement, text) {
	element(targetElement).innerHTML = text;
}
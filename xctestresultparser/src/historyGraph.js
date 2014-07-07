var HistoryGraph = function (runs, canvas, dateContainer) {
	this.runs = runs;
	this.maxTotal = 0;
	this.dataSize = this.runs.length;
	for (var i=0; i<this.runs.length; i++) {
		this.maxTotal = Math.max(this.maxTotal, this.runs[i].total);
	}
	this.canvas = canvas;
	var self = this;
	this.dateContainer = dateContainer;
	this.canvas.parentElement.parentElement.addEventListener('scroll', function(){
		self.dateContainer.style.marginLeft = '-'+(self.canvas.parentElement.parentElement.scrollLeft) + 'px';
	});
};
HistoryGraph.prototype.setOnFocus = function (element, onFocus) {
	this.focusIndex = null;
	element.addEventListener('mousemove', this._getOnFocusFunc(onFocus));
	var self = this;
	element.addEventListener('mouseout', 
			function(){
				self.unfocus();
				self.focusIndex=null;
				onFocus(null);
			}
		);
}
HistoryGraph.prototype.setOnClick = function (element, onClick) {
	var self = this;
	self.onClick = onClick;
	element.addEventListener('click', 
			function(){self.selectResultAt(self.focusIndex);}
		);
}
HistoryGraph.prototype.selectResultAt = function (index) {
	for (var i=0; i<this.dateLinks.length; i++) {
		this.dateLinks[i].className = (i==index)? "selected dateLabel":"dateLabel";
		this.selectIndicator.style.left = this.getCoordinate(index).x + "px";
	}
	if (this.onClick) {
		this.onClick(index, this.runs[index]);
	}
};
HistoryGraph.prototype._getOnFocusFunc = function (onFocus) {
	var self = this;
	return function (event) {
		var x = self.canvas.parentElement.parentElement.scrollLeft + event.x 
			- self.canvas.parentElement.offsetLeft;
		var index = Math.floor(x * (self.dataSize-1) / self.width);
		if (self.focusIndex!=index) {
			self.focusIndex = index;
			self.focus(index);
			onFocus(self.runs[index]);
		}
	};
};
HistoryGraph.prototype.focus = function (index) {
	this.focusIndicator.style.display = 'block';
	this.focusIndicator.style.left = this.getCoordinate(index).x + "px";
};
HistoryGraph.prototype.unfocus = function () {
	this.focusIndicator.style.display = 'none';
};
HistoryGraph.prototype._getOnClick = function (index) {
	var self = this;
	return function () {
		self.selectResultAt(index);
	}
};
HistoryGraph.prototype._getOnMouseover = function (index) {
	var self = this;
	return function () {
		self.focusIndex = index;
		self.focus(index);
	}
};
HistoryGraph.prototype._getOnMouseout = function () {
	var self = this;
	return function () {
		self.focusIndex = null;
		self.unfocus();
	}
};
HistoryGraph.PIXEL_PER_RUN = 24;

HistoryGraph.prototype.render = function () {

	var focusIndicator = document.createElement("DIV");
	focusIndicator.className = "graphIndicator";
	this.canvas.parentNode.appendChild(focusIndicator);
	this.focusIndicator = focusIndicator;
	
	var selectIndicator = document.createElement("DIV");
	selectIndicator.className = "graphIndicator";
	this.canvas.parentNode.appendChild(selectIndicator);
	this.selectIndicator = selectIndicator;

	element("runGraphScaleMax").innerHTML = this.maxTotal;
	element("runGraphScaleHalf").innerHTML = Math.round(this.maxTotal/2);
	
	var canvas = this.canvas;
	this.height = canvas.height;
	
	// Stretch canvas
	var canvasWidth = HistoryGraph.PIXEL_PER_RUN * (this.dataSize-1);
	canvas.width = canvasWidth;
	this.dateContainer.style.width = (canvasWidth + 16) + "px";
	this.width = canvas.width;

	var context = canvas.getContext("2d");
	
	var runs = this.runs;
	
	//Red fill
	
	context.fillStyle = '#fdd';
	context.beginPath();
	context.moveTo(0, this.height);
	
	//dateContainer
	this.dateLinks = [];
	for (var i=0; i<runs.length; i++) {
		var run = runs[i];
		var a = document.createElement('a');
		this.dateLinks[i] = a;
		a.href = 'javascript:void(0)';
		var self = this;
		a.addEventListener('click', this._getOnClick(i));
		a.addEventListener('mouseover', this._getOnMouseover(i));
		//a.addEventListener('mouseout', this._getOnMouseout(i));
		var coordinate = this.getCoordinate(i, 0);
		var runDate = new Date(run.date).toString('yy/MM/dd HH:mm');
		a.innerHTML = runDate;
		a.className = 'dateLabel';
		a.style.left = Math.floor(coordinate.x+9)+'px';
		this.dateContainer.appendChild(a);
	}
	
	for (var i=0; i<runs.length; i++) {
		var coordinate = this.getCoordinate(i, runs[i].fail);
		context.lineTo(coordinate.x, coordinate.y);
	}
	context.moveTo(this.width, this.height);
	context.fill();
	
	//Green fill
	
	context.fillStyle = "#dfd";
	context.beginPath();
	for (var i=0; i<runs.length; i++) {
		var coordinate = this.getCoordinate(i, runs[i].fail);
		context.lineTo(coordinate.x, coordinate.y);
	}
	for (var i=runs.length-1; i>=0; i--) {
		var coordinate = this.getCoordinate(i, (runs[i].fail+runs[i].success));
		context.lineTo(coordinate.x, coordinate.y);
	}
	context.fill();
	
	context.lineWidth = "1";
	context.lineJoin = "round";
	
	//Red line
	context.strokeStyle = "#f00";
	context.beginPath();
	for (var i=0; i<runs.length; i++) {
		var coordinate = this.getCoordinate(i, runs[i].fail);
		context.lineTo(coordinate.x, coordinate.y);
	}
	context.stroke();
	
	//Green line
	context.strokeStyle = "#080";
	context.beginPath();
	for (var i=0; i<runs.length; i++) {
		var coordinate = this.getCoordinate(i, runs[i].fail+runs[i].success);
		context.lineTo(coordinate.x, coordinate.y);
	}
	context.stroke();
	
	//Vertical
	context.strokeStyle = "rgba(0,0,0,0.2)";
	context.lineWidth = "0.5";
	for (var i=0; i<runs.length; i++) {
		context.beginPath();
		var x = this.getCoordinate(i, 0).x;
		context.moveTo(x, 0);
		context.lineTo(x, this.height);
		context.stroke();
	}
	var lastIndex = runs.length-1;
	this.selectResultAt(lastIndex);
	
	// Scroll
	var scroller = canvas.parentElement.parentElement;
	var scrollAmount = Math.max(0, canvasWidth - scroller.clientWidth);
	
	console.log("scroll="+(Math.max(0, canvasWidth - scroller.clientWidth)));
	scroller.scrollLeft = (Math.max(0, canvasWidth - scroller.clientWidth));
	
	/*
	setTimeout(this.getScrollAnimation(scrollAmount), 25);
	*/
};
HistoryGraph.prototype.getScrollAnimation = function (scrollAmount) {
	var self = this;
	var scroller = this.canvas.parentElement.parentElement;
	return function () {
		if (scroller.scrollLeft < scrollAmount) {
			scroller.scrollLeft = Math.min(scroller.scrollLeft+scrollAmount/10, scrollAmount);
			if (scroller.scrollLeft < scrollAmount) {
				setTimeout(self.getScrollAnimation(scrollAmount), 25);
			}
		}
	}
}; 
HistoryGraph.prototype.getCoordinate = function (xValue, yValue) {
	var xCoord = this.width * xValue / (this.dataSize - 1);
	var yCoord = this.height * (1 - yValue / this.maxTotal);
	return {x: xCoord, y:yCoord};
};

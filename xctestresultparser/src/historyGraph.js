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
	this.canvas.parentElement.addEventListener('scroll', function(){
		self.dateContainer.style.marginLeft = '-'+self.canvas.parentElement.scrollLeft + 'px';
	});
};
HistoryGraph.prototype.setOnFocus = function (onFocus) {
	this.focusIndex = null;
	this.canvas.addEventListener('mousemove', this._getOnFocusFunc(onFocus));
	var self = this;
	this.canvas.addEventListener('mouseout', 
			function(){self.focusIndex=null;onFocus(null);}
		);
}
HistoryGraph.prototype.setOnClick = function (onClick) {
	var self = this;
	self.onClick = onClick;
	this.canvas.addEventListener('click', 
			function(){self.selectResultAt(self.focusIndex);}
		);
}
HistoryGraph.prototype.selectResultAt = function (index) {
	for (var i=0; i<this.dateLinks.length; i++) {
		this.dateLinks[i].className = (i==index)? "selected dateLabel":"dateLabel";
	}
	if (this.onClick) {
		this.onClick(index, this.runs[index]);
	}
};
HistoryGraph.prototype._getOnFocusFunc = function (onFocus) {
	var self = this;
	return function (event) {
		var x = self.canvas.parentElement.scrollLeft + event.x;
		var index = Math.floor(x * (self.dataSize-1) / self.width);
		if (self.focusIndex!=index) {
			self.focusIndex = index;
			onFocus(self.runs[index]);
		}
	};
};
HistoryGraph.prototype._getOnClick = function (index) {
	var self = this;
	return function () {
		self.selectResultAt(index);
	}
};
HistoryGraph.prototype.render = function () {
	var canvas = this.canvas;
	this.height = canvas.height;
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
		var coordinate = this.getCoordinate(i, 0);
		var runDate = new Date(run.date).toString('yy/MM/dd HH:mm');
		a.innerHTML = runDate;
		a.className = 'dateLabel';
		a.style.left = Math.floor(coordinate.x)+'px';
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
};

HistoryGraph.prototype.getCoordinate = function (xValue, yValue) {
	var xCoord = this.width * xValue / (this.dataSize - 1);
	var yCoord = this.height * (1 - yValue / this.maxTotal);
	return {x: xCoord, y:yCoord};
};

(function() {
	var pageElement = document.getElementById(statusPageId);
	var statusPageModel = undefined;
	
	var pageInitHandler = function() {
		function SingleLine(key, value) {
			var self = this;
			self.label = ko.observable(key);
			self.text = ko.observable(value);
		}
		
		function StatusQuery(queryUrl, label) {
			var self = this;
			
			self.header = ko.observable(label);
			
			self.loading = ko.observable(true);
			
			self.results = ko.observableArray([]);
			
			self.runQuery = function() {
				self.loading(true);
				self.results.removeAll();
				// in the demo the URL happens to be the same, use a random pointless parameter to prevent caching
				$.getJSON(queryUrl+"?a="+Math.random(), function(data) {
					// expected values for data should be like this:
					// [{label: "In Service", value: true}, {label: "Port-ID", value: "579825-Clausen-Colin"}]
					for (var i = 0; i < data.length; i++) {
						self.results.push(new SingleLine(data[i].label, data[i].value));
					}
					console.log(data.length);
					self.loading(false);
				}).fail(function(data) {
					// TODO
				});
			};
			
			self.refresh = function(data, e) {
				self.runQuery();
				console.log(e);
		        e.stopPropagation();
		        e.stopImmediatePropagation();
			};
		}
		
		function StatusQueryListModel() {
			var self = this;
			
			function loadQueries(callback) {
				self.loading(true);
				$.mobile.loading('show');
				$.getJSON(queryListUrl, function(data) {
					callback(data);
				}).complete(function() {
					self.loading(false);
					$.mobile.loading('hide');
				});
			}
			
			self.loading = ko.observable(false);
			
			self.loading.subscribe(function(v) {
				var cmd = v ? "show" : "hide";
				
				var interval = setInterval(function(){
			        $.mobile.loading(cmd);
			        clearInterval(interval);
			    },10);
			});
			
			self.queries = ko.observableArray([]);
			
			self.init = function() {
				loadQueries(function(data) {
					self.queries.removeAll();
					// expected data looks like this:
					// [{url: "<url>", header: "HeadText"}, {...}]
					for (var i = 0; i < data.length; i++) {
						var query = data[i];
						var model = new StatusQuery(query.url, query.header);
						model.runQuery();
						self.queries.push(model);
					}
				});
			};
		}
		
		statusPageModel = new StatusQueryListModel();
		statusPageModel.init();
		ko.applyBindings(statusPageModel, pageElement);				
	};
	
	var initEvent = "pageinit";
	var hideEvent = "pageremove";
	
	var pageRemoveHandler = function() {
		ko.cleanNode(pageElement);
		$(pageElement).unbind(initEvent, pageInitHandler);
		$(pageElement).unbind(hideEvent, pageRemoveHandler);
		statusPageModel = undefined;
	};
	
	$(pageElement).bind(initEvent, pageInitHandler);
	$(pageElement).bind(hideEvent, pageRemoveHandler);
})();

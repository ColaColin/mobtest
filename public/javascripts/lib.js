ko.bindingHandlers.refreshList = {
	update : function(element, valueAccessor, allBindingsAccessor) {
		$(element).listview({refresh : true});
	}
};

ko.bindingHandlers.refreshCollapsible = {
	update : function(element, valueAccessor, allBindingsAccessor) {
		$(element).collapsible({refresh : true});
	}
};
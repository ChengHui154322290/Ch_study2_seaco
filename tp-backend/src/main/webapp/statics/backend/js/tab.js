$().ready( function() {
	if ($.tools != null) {
		var $tab = $("#tab");
		// Tab效果
		$tab.tabs("table.tabContent, div.tabContent", {
			tabs: "input"
		});
	}

});


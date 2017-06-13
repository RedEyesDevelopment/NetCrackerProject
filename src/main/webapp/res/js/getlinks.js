	jQuery(document).ready(function($) {
			event.preventDefault();
			getLinks();
		});

	function getLinks() {
			var links = {};

		$.ajax({
			type : "GET",
			contentType : "application/json",
			url : "/auth/links",
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data);
				display(data);
			},
			error : function(e) {
				console.log("ERROR: ", e);
				display(e);
			},
			done : function(e) {
				console.log("DONE"+links);
			}
		});
	}

	function display(data) {
		var json = "<h4>Ajax Response</h4><pre>"
				+ JSON.stringify(data, null, 4) + "</pre>";
		$('#result').html(json);
	}
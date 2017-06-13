	jQuery(document).ready(function($) {
			event.preventDefault();
			isAuthorized();
			getLinks();
		});

	function isAuthorized() {
			var role = {};

		$.ajax({
			type : "GET",
			contentType : "application/json",
			url : "/auth/isauthorized",
			timeout : 100000,
			success : function(data) {
				console.log("SUCCESS: ", data);
			},
			error : function(e) {
				console.log("ERROR: ", e);
			},
			done : function(e) {
				console.log("DONE"+role);
			}
		});
	}

	function getLinks() {
    			var links = {};

    		$.ajax({
    			type : "GET",
    			contentType : "application/json",
    			url : "/auth/links",
    			timeout : 100000,
    			success : function(data) {
    				console.log("SUCCESS: ", data);
    			},
    			error : function(e) {
    				console.log("ERROR: ", e);
    			},
    			done : function(e) {
    				console.log("DONE"+links);
    			}
    		});
    	}

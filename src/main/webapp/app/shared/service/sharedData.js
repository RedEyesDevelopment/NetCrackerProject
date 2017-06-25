app.factory('sharedData', function(ROLE) {

    var auth = {};

    function setAuth(headerAuth) {
        auth = headerAuth;
    }




    return {
        getRole: auth.role,
	    getIsAuthorized: auth.isAuthorized,
	    getLinks: auth.links,

        setAuth: setAuth
    }
});
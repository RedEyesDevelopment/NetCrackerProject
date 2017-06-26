app.factory('sharedData', function(ROLE) {

    var auth;

    function setAuth(headerAuth) { auth = headerAuth; }


    function getRole() { return auth.role; }

    function getIsAuthorized() { return auth.isAuthorized; }

    function getLinks() { return auth.links; }

    function getIsAdmin() { return auth.isAdmin; }

    function getIsReception() { return auth.isReception; }

    function getIsClient() { return auth.isClient; }


    return {
        getRole: getRole,
	    getIsAuthorized: getIsAuthorized,
	    getLinks: getLinks,
        getIsAdmin: getIsAdmin,
        getIsReception: getIsReception,
        getIsClient: getIsClient,

        setAuth: setAuth
    }
});
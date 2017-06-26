app.factory('sharedData', function(ROLE) {

    var auth;
    var bookCtrlLimitAuth;

    function setAuth(headerAuth) { auth = headerAuth; }

    function setBookCtrlLimitAuth(limitAuth) { bookCtrlLimitAuth = limitAuth; }
    function changeBookCtrlLimitAuth() {
        if (bookCtrlLimitAuth !== undefined) {
            bookCtrlLimitAuth.isAuthorized = auth.isAuthorized;
            bookCtrlLimitAuth.role = auth.role;
        }
    }

    function getRole() { return auth.role; }

    function getIsAuthorized() { return auth.isAuthorized; }

    function getLinks() { return auth.links; }

    function getIsAdmin() { return auth.isAdmin; }

    function getIsReception() { return auth.isReception; }

    function getIsClient() { return auth.isClient; }


    return {
        setAuth: setAuth,

        getRole: getRole,
        getIsAuthorized: getIsAuthorized,
        getLinks: getLinks,
        getIsAdmin: getIsAdmin,
        getIsReception: getIsReception,
        getIsClient: getIsClient,

        setBookCtrlLimitAuth: setBookCtrlLimitAuth,
        changeBookCtrlLimitAuth: changeBookCtrlLimitAuth
    }
});
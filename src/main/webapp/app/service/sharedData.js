app.factory('sharedData', function(ROLE) {

    var auth;
    var bookCtrlLimitAuth;

    function setAuth(headerAuth) { auth = headerAuth; }

    function setBookCtrlLimitAuth(limitAuth) {
        bookCtrlLimitAuth = limitAuth;
        changeBookCtrlLimitAuth();
    }
    function changeBookCtrlLimitAuth() {
        if (bookCtrlLimitAuth !== undefined) {
            bookCtrlLimitAuth.isAuthorized = auth.isAuthorized;
            bookCtrlLimitAuth.role = auth.role;
        }
    }

    function getRole() { return auth.role; }

    function getIsAuthorized() { return auth.isAuthorized; }

    function getLinks() { return auth.links; }

    function getMyself() { return auth.myself }


    function getIsAdmin() { return auth.isAdmin; }

    function getIsReception() { return auth.isReception; }

    function getIsClient() { return auth.isClient; }



    return {
        setAuth: setAuth,

        getRole: getRole,
        getIsAuthorized: getIsAuthorized,
        getLinks: getLinks,
        getMyself: getMyself,

        getIsAdmin: getIsAdmin,
        getIsReception: getIsReception,
        getIsClient: getIsClient,

        setBookCtrlLimitAuth: setBookCtrlLimitAuth,
        changeBookCtrlLimitAuth: changeBookCtrlLimitAuth
    }
});
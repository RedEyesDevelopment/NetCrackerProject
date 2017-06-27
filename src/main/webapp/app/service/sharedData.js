app.factory('sharedData', function(ROLE) {

    var auth;
    var bookCtrlLimitAuth;
    var paMyself;


    function setAuth(headerAuth) { auth = headerAuth; }

    function setBookCtrlLimitAuth(limitAuth) {
        bookCtrlLimitAuth = limitAuth;
    }
    function setPersonalAreaMyself(self) { paMyself = self; }

    function updateAuth() {
        if (bookCtrlLimitAuth !== undefined) {
            bookCtrlLimitAuth.isAuthorized = auth.isAuthorized;
            bookCtrlLimitAuth.role = auth.role;
        }
    }
    function updateMyself() {
        if (paMyself !== undefined) {
            paMyself.objectId = auth.myself.objectId;
            paMyself.additionalInfo = auth.myself.additionalInfo;
            paMyself.email = auth.myself.email;
            paMyself.firstName = auth.myself.firstName;
            paMyself.lastName = auth.myself.lastName;
            paMyself.phones = new Array();
            auth.myself.phones.forEach(function(elem) {
                paMyself.phones.push({
                    objectId: elem.objectId,
                    phoneNumber: elem.phoneNumber
                });
            });
        }
    }

    function getRole() { return auth.role; }

    function getIsAuthorized() { return auth.isAuthorized; }

    function getLinks() { return auth.links; }

    function getMyself() { return auth.myself; }


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
        setPersonalAreaMyself: setPersonalAreaMyself,

        updateAuth: updateAuth,
        updateMyself: updateMyself
    }
});
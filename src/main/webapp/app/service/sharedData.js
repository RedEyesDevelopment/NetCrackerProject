app.factory('sharedData', function(ROLE) {

    var auth;
    var searchRoomTypes;
    var userIdForOrderFromAdmin

    function setAuth(headerAuth) { auth = headerAuth; }


    function getRole() { return auth.role; }

    function getIsAuthorized() { return auth.isAuthorized; }

    function getLinks() { return auth.links; }

    function getMyself() { return auth.myself; }

    function getMyObjectId() { return auth.myself.objectId; }


    function getIsAdmin() { return auth.isAdmin; }

    function getIsReception() { return auth.isReception; }

    function getIsClient() { return auth.isClient; }


    function setSearchRoomTypes(func) { searchRoomTypes = func; }
    function searchRoomTypes() { searchRoomTypes(); }


    function setUserIdForOrderFromAdmin(userId) { userIdForOrderFromAdmin = userId; }
    function getUserIdForOrderFromAdmin() { return userIdForOrderFromAdmin; }
    



    return {
        setAuth: setAuth,

        getRole: getRole,
        getIsAuthorized: getIsAuthorized,
        getLinks: getLinks,
        getMyself: getMyself,

        getIsAdmin: getIsAdmin,
        getIsReception: getIsReception,
        getIsClient: getIsClient,

        setSearchRoomTypes: setSearchRoomTypes,
        searchRoomTypes: searchRoomTypes,
        getMyObjectId: getMyObjectId,

        setUserIdForOrderFromAdmin: setUserIdForOrderFromAdmin,
        getUserIdForOrderFromAdmin: getUserIdForOrderFromAdmin
    }
});
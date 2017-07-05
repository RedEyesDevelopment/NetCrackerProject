app.controller('notificationTypesCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
    function($scope, $http, $location, sharedData, util){

	// All notificationTypes
    (function() {
        $http({
            url: sharedData.getLinks().https + '/notificationtypes',
            method: 'GET',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.originListOfNotificationTypes = data.data;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }());
    // All roles, except client
    (function() {
        $http({
            url: sharedData.getLinks().https + '/roles',
            method: 'GET',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfRoles = new Array();
            data.data.forEach(function(role) {
                if (role.objectId != 3) {
                    $scope.listOfRoles.push(role);
                }
            });
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }());
    /* редирект на главную если не админ и не рецепция */
    (function() {
        if (!sharedData.getIsAdmin() && !sharedData.getIsReception()) { $location.path('/') };
    }());

    $scope.isAdmin = sharedData.getIsAdmin();

	/* Инициализация служебных переменных */
    function resetFlags() {
        $scope.added = false;
        $scope.updated = false;
        $scope.deleted = false;
    }

    $scope.notificationType = {};
    resetFlags();

    $scope.stage = "looking";

    $scope.pager = {
        startPaging : 0,
        objectsOnPage : 6
    }

	/* Функции подготовки запросов */
    $scope.prepareToAddNotificationType = function() {
        $scope.indexForOperation = "";
        $scope.notificationType.idForOperation = "";
        $scope.notificationType.notificationTypeTitle = "";
        $scope.notificationType.orientedRole = "";
        resetFlags();
        $scope.stage = "adding";
        $scope.modificationMode = true;
    }

    $scope.prepareToEditNotificationType  = function(notificationTypeId, index) {
        $http({
            url: sharedData.getLinks().https + '/notificationtypes/' + notificationTypeId,
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function(data) {
            console.log(data);
            $scope.indexForOperation = index;
            $scope.notificationType.idForOperation = notificationTypeId;
            $scope.notificationType.notificationTypeTitle = data.data.notificationTypeTitle;
            $scope.notificationType.orientedRole = data.data.orientedRole.objectId;
            resetFlags();
            $scope.stage = "editing";
            $scope.modificationMode = true;

        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.prepareToDeleteNotificationType = function(notificationTypeId, index) {
        $scope.indexForOperation = index;
        $scope.notificationType.idForOperation = notificationTypeId;
        resetFlags();
        $scope.stage = "deleting"
    }

	/* Возврат на просмотр */
    $scope.back = function() {
        $scope.stage = "looking";
        $scope.modificationMode = false;
    }

	/* Вызывает нужный запрос в зависимости от типа операции */
    $scope.query = function() {
        switch ($scope.stage) {
            case 'adding': addNotificationType();
                break;
            case 'editing': editNotificationType();
                break;
            case 'deleting': deleteNotificationType();
                break;
        }
    }

	/* Функции, выполняющие запросы */
    var addNotificationType  = function() {
        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/notificationtypes/',
            method: 'POST',
            data: {
                notificationTypeTitle :  $scope.notificationType.notificationTypeTitle,
                orientedRole : $scope.notificationType.orientedRole
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.originListOfNotificationTypes.push({
                objectId : data.data.objectId,
                notificationTypeTitle :  $scope.notificationType.notificationTypeTitle,
                orientedRole : util.getObjectInArrayById($scope.listOfRoles, $scope.notificationType.orientedRole)
        });
            $scope.prepareToAddNotificationType();
            $scope.added = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = "serverErr";
        });
    }

    var editNotificationType  = function() {
        console.log($scope.notificationType.orientedRole.objectId);
        console.log($scope.notificationType.orientedRole);
        console.log($scope.notificationType);

        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/notificationtypes/' + $scope.notificationType.idForOperation,
            method: 'PUT',
            data: {
                notificationTypeTitle :  $scope.notificationType.notificationTypeTitle,
                orientedRole : $scope.notificationType.orientedRole
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            console.log($scope.notificationType);
            console.log($scope.indexForOperation);
            $scope.originListOfNotificationTypes[$scope.indexForOperation].notificationTypeTitle = $scope.notificationType.notificationTypeTitle;
            $scope.originListOfNotificationTypes[$scope.indexForOperation].orientedRole = util.getObjectInArrayById($scope.listOfRoles, $scope.notificationType.orientedRole);
            console.log($scope.originListOfNotificationTypes);
            $scope.updated = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = "serverErr";
        });
    }

    var deleteNotificationType  = function() {
        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/notificationtypes/' + $scope.notificationType.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.originListOfNotificationTypes.splice($scope.indexForOperation, 1);
            $scope.deleted = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = "serverErr";
        });
    }


    /* Filters */
    $scope.resetFilter = function() {
        $scope.filter = {};
        $scope.filteredListOfNotificationTypes = [];
    }   
    $scope.resetFilter();
    $scope.updateFilter = function() {
        $scope.filteredListOfNotificationTypes = $scope.originListOfNotificationTypes.filter(function(item) {
            return $scope.filter.roleId ? (item.orientedRole.objectId === $scope.filter.roleId) : true;
        });
    }


	/* Для листания страниц с объектами */
    $scope.nextNotificationTypes = function() {
        $scope.pager.startPaging = util.nextEntities($scope.originListOfNotificationTypes.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
    $scope.previousNotificationTypes = function() {
        $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
}]);
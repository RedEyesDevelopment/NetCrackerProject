app.controller('notificationTypesCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
    function($scope, $http, $location, sharedData, util){

		/* Функция на получения всех услуг, вызываются сразу */
        (function() {
            $http({
                url: sharedData.getLinks().https + '/notificationtypes',
                method: 'GET',
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                console.log(data);
                $scope.listOfNotificationTypes = data.data;
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        }());

        (function() {
            $http({
                url: sharedData.getLinks().https + '/roles',
                method: 'GET',
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                console.log(data);
                $scope.listOfRoles = data.data;
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        }());
		/* редирект на главную если не админ */
        (function() {
            if(!sharedData.getIsAdmin()) { $location.path('/') };
        }());

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
                $scope.listOfNotificationTypes.push({
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
                $scope.listOfNotificationTypes[$scope.indexForOperation].notificationTypeTitle = $scope.notificationType.notificationTypeTitle;
                $scope.listOfNotificationTypes[$scope.indexForOperation].orientedRole = util.getObjectInArrayById($scope.listOfRoles, $scope.notificationType.orientedRole);
                console.log($scope.listOfNotificationTypes);
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
                $scope.listOfNotificationTypes.splice($scope.indexForOperation, 1);
                $scope.deleted = true;
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = "serverErr";
            });
        }


		/* Для листания страниц с объектами */
        $scope.nextNotificationTypes = function() {
            $scope.pager.startPaging = util.nextEntities($scope.listOfNotificationTypes.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
        }
        $scope.previousNotificationTypes = function() {
            $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
        }
    }]);
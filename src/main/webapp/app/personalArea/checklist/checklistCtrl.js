app.controller('checklistCtrl', ['$scope', '$http', 'sharedData', 'util',
    function($scope, $http, sharedData, util) {

    /* All user's orders */
    (function() {
        $http({
            url: sharedData.getLinks().https + '/orders/user',
            method: 'GET',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfOrders = data.data;
            $scope.listOfOrders.forEach(function(order) {
                order.maintenanceSum = 0;
                if (order.journalRecords != null) {
                    order.journalRecords.forEach(function(record) {
                        order.maintenanceSum += record.cost;
                    });
                }
                order.total = order.sum + order.maintenanceSum;
            });
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }());

    // это для кнопок журнала использованных услуг (не удалять!)
    $scope.expand = {id : 0};

    $scope.editComment = {id : 0};

    $scope.stage = "looking";

    $scope.pager = {
        startPaging : 0,
        objectsOnPage : 6
    }

    $scope.query = function() {
        switch ($scope.stage) {
            case 'updateComment': editComment();
                break;
        }
    }

    $scope.prepareToEditComment = function(orderId, index) {
        $scope.errMessage = false;
        $scope.idForOperation = orderId;
        $scope.indexForOperation = index;
        $scope.stage = "updateComment";
    }

    var editComment = function() {
        $scope.errMessage = false;
        $http({
            url: sharedData.getLinks().https + '/orders/user/update/' + $scope.idForOperation,
            method: 'PUT',
            data: $scope.listOfOrders[$scope.indexForOperation].comment,
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log("OK");
            console.log(data);
            $scope.stage = "done";
            $scope.editComment = {id : 0};
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }




    /* Для листания страниц с объектами */
    $scope.nextOrders = function() {
        $scope.pager.startPaging = util.nextEntities($scope.listOfOrders.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
    $scope.previousOrders = function() {
        $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
}]);
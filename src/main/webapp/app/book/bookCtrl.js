app.controller('bookCrtl', ['$scope', '$http', '$location', 'sharedData',
    function ($scope, $http, $location, sharedData) {

    /* Сразу получить все категории */
    (function() {
        $http({
            url: 'http://localhost:8080/categories',
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        }).then(function(data) {
            console.log(data);
            $scope.listOfCategories = data.data;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }());

    $scope.book = {}
    $scope.limitAuth = {}
    $scope.stage = "booking";
    $scope.doesNeedToShowBookForm = true;
    sharedData.setBookCtrlLimitAuth($scope.limitAuth);


    $scope.searchRoomTypes = function () {
        $http({
            url: 'http://localhost:8080/orders/searchavailability',
            method: 'POST',
            data: {
                arrival :       $scope.book.from.getTime(),
                departure :     $scope.book.till.getTime(),
                livingPersons : parseInt($scope.book.adults),
                categoryId :    parseInt($scope.book.category)
            },
            headers: {
                'Content-Type' : 'application/json'
            }
        }).then(function(data) {
            console.log(data);
            $scope.listOfRoomTypes = data.data;
            $scope.stage = "choosingRoomType";
            setTimeout(function() {
                $('body').animate({ scrollTop : 770 }, 300);
            }, 600);
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.bookFormChange = function() {
        $scope.stage = "booking";
    }

    $scope.issueOder = function (roomTypeId) {
        if ($scope.limitAuth.isAuthorized) {            
            $http({
                url: 'http://localhost:8080/orders/book/' + roomTypeId,
                method: 'GET',
                headers: {
                    'Content-Type' : 'application/json'
                }
            }).then(function(data) {
                console.log(data);
                $scope.finishOrder = data.data;
                $scope.doesNeedToShowBookForm = false;
                $scope.stage = "finishOrdering";
                $('body').animate({ scrollTop : 500 }, 300);
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
        } else {
            $('#signupLink').trigger('click');
        }
    }

    $scope.acceptOrder = function() {
        $http({
            url: 'http://localhost:8080/orders/accept',
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        }).then(function(data) {
            console.log(data);
            sendOrderPdf(data.data.objectId);
            $scope.stage = "thanking";
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    var sendOrderPdf = function(orderId) {
        $http({
            url: 'http://localhost:8080/pdf/order/' + orderId,
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        }).then(function(data) {
            console.log(data);
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.cancelOrder = function() {
        $http({
            url: 'http://localhost:8080/orders/cancel',
            method: 'GET',
            headers: {
                'Content-Type' : 'application/json'
            }
        }).then(function(data) {
            console.log(data);
            $scope.searchRoomTypes();
            $scope.stage = "choosingRoomType";
            $scope.doesNeedToShowBookForm = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.thanks = function() {
        $scope.book = {};
        $scope.stage = "booking";
        $scope.doesNeedToShowBookForm = true;
    }

}]);
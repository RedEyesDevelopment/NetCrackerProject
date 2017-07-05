app.controller('bookCrtl', ['$scope', '$http', '$location', 'sharedData',
    function ($scope, $http, $location, sharedData) {

    /* Сразу получить все категории */
    (function() {
        $http({
            url: 'http://localhost:8080/categories/simpleList',
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
            $scope.errMessage = response.data.message;
        });
    }());

    $scope.book = {}
    $scope.stage = "booking";
    $scope.doesNeedToShowBookForm = true;

    $scope.checkIsAuthorized = function() {
        return sharedData.getIsAuthorized();
    }

    $scope.searchRoomTypes = function() {
        $scope.errMessage = false;
        if (Object.keys($scope.book).length !== 0) {
            var maxDate = new Date();
            maxDate.setFullYear(new Date().getFullYear() + 1);
            if ($scope.book.from.getTime() < $scope.book.till.getTime()
                && $scope.book.from.getTime() >= new Date().getTime() && $scope.book.till.getTime() <= maxDate.getTime()) {
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
                    $scope.errMessage = response.data.message;
                });
            } else {
                $scope.errMessage = "Invalid dates! Pls fix and try again!";
            }
        }
    }

    sharedData.setSearchRoomTypes($scope.searchRoomTypes);

    $scope.bookFormChange = function() {
        $scope.errMessage = false;
        $scope.stage = "booking";
    }

    $scope.issueOrder = function (roomTypeId) {
        $scope.errMessage = false;
        var userId;
        /* Проверяем на кого оформлять заказ,
            пробуем на того, чей id лежит в общих данных,
            но если там пусто, то на текущнго юзера */
        if (sharedData.getUserIdForOrderFromAdmin() == undefined) {
            userId = sharedData.getMyObjectId();
        } else {
            userId = sharedData.getUserIdForOrderFromAdmin();
        }

        if ($scope.checkIsAuthorized()) {         
            $http({
                url: 'http://localhost:8080/orders/book/',
                method: 'POST',
                data: {
                    userId: userId,
                    roomTypeId: roomTypeId
                },
                headers: {
                    'Content-Type' : 'application/json'
                }
            }).then(function(data) {
                console.log(data);
                $scope.finishOrder = data.data;
                $scope.doesNeedToShowBookForm = false;
                sharedData.setUserIdForOrderFromAdmin(undefined);
                $scope.stage = "finishOrdering";
                $('body').animate({ scrollTop : 500 }, 300);
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = response.data.message;
            });
        } else {
            $('#signupLink').trigger('click');
        }
    }

    $scope.acceptOrder = function() {
        $scope.errMessage = false;
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
            $scope.errMessage = response.data.message;
        });
    }

    var sendOrderPdf = function(orderId) {
        $scope.errMessage = false;
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
            $scope.errMessage = response.data.message;
        });
    }

    $scope.cancelOrder = function() {
        $scope.errMessage = false;
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
            $scope.errMessage = response.data.message;
        });
    }

    $scope.thanks = function() {
        $scope.errMessage = false;
        $scope.book = {};
        $scope.stage = "booking";
        $scope.doesNeedToShowBookForm = true;
    }

}]);
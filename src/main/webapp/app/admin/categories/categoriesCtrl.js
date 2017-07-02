app.controller('categoriesCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
     function($scope, $http, $location, sharedData, util){

    /* All categories */
    (function() {
        $http({
            url: sharedData.getLinks().https + '/categories',
            method: 'GET',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfCategories = data.data;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }());
    /* All maintenances */
    (function() {
        $http({
            url: sharedData.getLinks().https + '/maintenances',
            method: 'GET',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfMaintenances = data.data;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }());
    /* редирект на главную если не админ */
    (function() {
        if(!sharedData.getIsAdmin()) { $location.path('/') };
    }());

    // это для кнопок списка complimentaries (не удалять!)
    $scope.expand = {id : 0};

    $scope.category = {};
    $scope.newComplimentary = {};

    $scope.stage = "looking";
    $scope.modificationMode = false;

    $scope.pager = {
        startPaging : 0,
        objectsOnPage : 6
    }

    /* Возврат на просмотр */
    $scope.back = function() {
        $scope.stage = "looking";
        $scope.modificationMode = false;
    }

    /* Вызывает нужный запрос в зависимости от типа операции */
    $scope.query = function() {
        switch ($scope.stage) {
            case 'deleteComplimentary': deleteComplimentary();
                break;
            case 'adding': addCategory();
                break;
            case 'editing': editCategory();
                break;
            case 'deleting': deleteCategory();
                break;
        }
    }

    /* Для добавления complimentary */
    $scope.prepareToAddComplimentary = function(categoryId, index) {
        $scope.newComplimentary.categoryId = categoryId;
        $scope.indexForOperation = index;
    }   

    $scope.addComplimentary = function() {
        $http({
            url: sharedData.getLinks().https + '/complimentaries',
            method: 'POST',
            data: {
                categoryId: $scope.newComplimentary.categoryId,
                maintenanceId: $scope.newComplimentary.maintenanceId,
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $http({
                url: sharedData.getLinks().https + '/complimentaries/' + data.data.objectId,
                method: 'GET',
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                $scope.listOfCategories[$scope.indexForOperation].complimentaries.push(data.data);
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
            });
            $scope.newMaintenance = { count: 1 };
            $scope.stage = "looking";
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.prepareToDeleteComplimentary = function(complimentaryId, categoryIndex, complimentaryIndex) {
        $scope.idForOperation = complimentaryId;
        $scope.indexForOperation = categoryIndex;
        $scope.indexComplimentaryForOperation = complimentaryIndex;
        $scope.stage = "deleteComplimentary";
    }

    var deleteComplimentary = function() {
        $http({
            url: sharedData.getLinks().https + '/complimentaries/' + $scope.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfCategories[$scope.indexForOperation].complimentaries.splice($scope.indexComplimentaryForOperation, 1);
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }







    /* Функции подготовки запросов */
    $scope.prepareToAddCategory = function() {
        $scope.indexForOperation = undefined;
        $scope.category = {};
        $scope.stage = "adding";
        $scope.modificationMode = true;
    }

    var addCategory = function() {
        if (        $scope.category.dollars >= 0
                &&  $scope.category.cents >= 0
                &&  $scope.category.cents <= 99) {
            $http({
                url: sharedData.getLinks().https + '/categories/',
                method: 'POST',
                data: {
                    categoryTitle :     $scope.category.title,
                    categoryPrice :     ($scope.category.dollars * 100) + $scope.category.cents
                },
                headers: { 'Content-Type' : 'application/json' }
            }).then(function(data) {
                console.log(data);
                $scope.listOfCategories.push({
                    objectId :          data.data.objectId,
                    categoryTitle :     $scope.category.title,
                    categoryPrice :     ($scope.category.dollars * 100) + $scope.category.cents,
                    complimentaries: new Array()
                });
                $scope.prepareToAddCategory();
                $scope.stage = 'added';
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = "serverErr";
            });
        }
    }


    $scope.prepareToEditCategory = function(categoryId, index) {
        $http({
            url: sharedData.getLinks().https + '/categories/' + categoryId,
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function(data) {
            console.log(data);
            $scope.indexForOperation = index;
            $scope.idForOperation = categoryId;
            $scope.category.title = data.data.categoryTitle;
            $scope.category.price = data.data.categoryPrice;
            $scope.category.dollars = Math.floor(data.data.categoryPrice / 100);
            $scope.category.cents = data.data.categoryPrice % 100;
            $scope.stage = "editing";
            $scope.modificationMode = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    var editCategory = function() {
        $http({
            url: sharedData.getLinks().https + '/categories/' + $scope.idForOperation,
            method: 'PUT',
            data: {
                categoryTitle :     $scope.category.title,
                categoryPrice :     ($scope.category.dollars * 100) + $scope.category.cents
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfCategories[$scope.indexForOperation].categoryTitle = $scope.category.title;
            $scope.listOfCategories[$scope.indexForOperation].categoryPrice = ($scope.category.dollars * 100) + $scope.category.cents;
            $scope.stage = "updated";
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = "serverErr";
        });
    }


    $scope.prepareToDeleteCategory = function(categoryId, index) {
        $scope.indexForOperation = index;
        $scope.idForOperation = categoryId;
        $scope.stage = "deleting"
    }

    var deleteCategory = function() {
        $http({
            url: sharedData.getLinks().https + '/categories/' + $scope.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfCategories.splice($scope.indexForOperation, 1);
            $scope.deleted = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = "serverErr";
        });
    }


    /* Для листания страниц с объектами */
    $scope.nextCategories = function() {
        $scope.pager.startPaging = util.nextEntities($scope.listOfCategories.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
    $scope.previousCategories = function() {
        $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
}]);
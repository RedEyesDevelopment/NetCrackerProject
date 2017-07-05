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
            $scope.originListOfCategories = data.data;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);$scope.errMessage = response.data.message;
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
            console.log(response);$scope.errMessage = response.data.message;
        });
    }());
    /* редирект на главную если не админ и не рецепция */
    (function() {
        if (!sharedData.getIsAdmin() && !sharedData.getIsReception()) { $location.path('/') };
    }());

    $scope.isAdmin = sharedData.getIsAdmin();

    // это для кнопок списка complimentaries (не удалять!)
    $scope.expand = {id : 0};

    $scope.category = {};
    $scope.newComplimentary = {};

    $scope.stage = "looking";
    $scope.modificationMode = false;

    $scope.pager = {
        startPaging : 0,
        objectsOnPage : 4
    }

    /* Возврат на просмотр */
    $scope.back = function() {
        $scope.errMessage = false;
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
        $scope.errMessage = false;
        $scope.newComplimentary.categoryId = categoryId;
        $scope.indexForOperation = index;
    }   

    $scope.addComplimentary = function() {
        $scope.errMessage = false;
        var currCategory = util.getObjectInArrayById($scope.originListOfCategories, $scope.newComplimentary.categoryId);
        currCategory.complimentaries.forEach(function(complimentary) {
            if (complimentary.maintenance.objectId == $scope.newComplimentary.maintenanceId) {
                $scope.errMessage = "existMaintenance";
                return;
            }
        });
        // если всё ок, будет продолжение:
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
                $scope.originListOfCategories[$scope.indexForOperation].complimentaries.push(data.data);
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);$scope.errMessage = response.data.message;
            });
            $scope.newMaintenance = { count: 1 };
            $scope.stage = "looking";
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }

    $scope.prepareToDeleteComplimentary = function(complimentaryId, categoryIndex, complimentaryIndex) {
        $scope.errMessage = false;
        $scope.idForOperation = complimentaryId;
        $scope.indexForOperation = categoryIndex;
        $scope.indexComplimentaryForOperation = complimentaryIndex;
        $scope.stage = "deleteComplimentary";
    }

    var deleteComplimentary = function() {
        $scope.errMessage = false;
        $http({
            url: sharedData.getLinks().https + '/complimentaries/' + $scope.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.originListOfCategories[$scope.indexForOperation].complimentaries.splice($scope.indexComplimentaryForOperation, 1);
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }







    /* Функции подготовки запросов */
    $scope.prepareToAddCategory = function() {
        $scope.errMessage = false;
        $scope.indexForOperation = undefined;
        $scope.category = {};
        $scope.category.dollars = 0;
        $scope.category.cents = 0;
        $scope.stage = "adding";
        $scope.modificationMode = true;
    }

    var addCategory = function() {
        $scope.errMessage = false;
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
                $scope.originListOfCategories.push({
                    objectId :          data.data.objectId,
                    categoryTitle :     $scope.category.title,
                    categoryPrice :     ($scope.category.dollars * 100) + $scope.category.cents,
                    complimentaries: new Array()
                });
                $scope.prepareToAddCategory();
                $scope.stage = 'added';
                $scope.resetFilter();
            }, function(response) {
                console.log("Smth wrong!!");
                console.log(response);
                $scope.errMessage = response.data.message;
            });
        }
    }


    $scope.prepareToEditCategory = function(categoryId, index) {
        $scope.errMessage = false;
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
            console.log(response);$scope.errMessage = response.data.message;
        });
    }

    var editCategory = function() {
        $scope.errMessage = false;
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
            $scope.originListOfCategories[$scope.indexForOperation].categoryTitle = $scope.category.title;
            $scope.originListOfCategories[$scope.indexForOperation].categoryPrice = ($scope.category.dollars * 100) + $scope.category.cents;
            $scope.stage = "updated";
            $scope.resetFilter();
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }


    $scope.prepareToDeleteCategory = function(categoryId, index) {
        $scope.errMessage = false;
        $scope.indexForOperation = index;
        $scope.idForOperation = categoryId;
        $scope.stage = "deleting"
    }

    var deleteCategory = function() {
        $scope.errMessage = false;
        $http({
            url: sharedData.getLinks().https + '/categories/' + $scope.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.originListOfCategories.splice($scope.indexForOperation, 1);
            $scope.deleted = true;
            $scope.resetFilter();
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }


    /* Filters */
    $scope.resetFilter = function() {
        $scope.filter = {};
        $scope.filteredListOfCategories = [];
    }   
    $scope.resetFilter();
    $scope.updateFilter = function() {
        $scope.filteredListOfCategories = $scope.originListOfCategories.filter(function(item) {
            return $scope.filter.priceTop ? (Math.round(item.categoryPrice/100) <= $scope.filter.priceTop) : true;
        }).filter(function(item) {
            return $scope.filter.priceBottom ? ($scope.filter.priceBottom <= Math.round(item.categoryPrice/100)) : true;
        });
    }


    /* Для листания страниц с объектами */
    $scope.nextCategories = function() {
        $scope.pager.startPaging = util.nextEntities($scope.originListOfCategories.length, $scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
    $scope.previousCategories = function() {
        $scope.pager.startPaging = util.previousEntities($scope.pager.startPaging, $scope.pager.objectsOnPage);
    }
}]);
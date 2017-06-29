app.controller('categoriesCtrl', ['$scope', '$http', '$location', 'sharedData', 'util',
     function($scope, $http, $location, sharedData, util){

    /* Функция на получения всех категорий, вызываются сразу */
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

    $scope.category = {};
    resetFlags();

    $scope.stage = "looking";

    $scope.pager = {
        startPaging : 0,
        objectsOnPage : 6
    }

    /* Функции подготовки запросов */
    $scope.prepareToAddCategory = function() {
        $scope.indexForOperation = "";
        $scope.category.idForOperation = "";
        $scope.category.title = "";
        $scope.category.price = "";
        resetFlags();
        $scope.stage = "adding";
        $scope.modificationMode = true;
    }

    $scope.prepareToEditCategory = function(categoryId, index) {
        $http({
            url: sharedData.getLinks().https + '/categories/' + categoryId,
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function(data) {
            console.log(data);
            $scope.indexForOperation = index;
            $scope.category.idForOperation = categoryId;
            $scope.category.title = data.data.categoryTitle;
            $scope.category.price = data.data.categoryPrice;
            resetFlags();
            $scope.stage = "editing";
            $scope.modificationMode = true;

        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    $scope.prepareToDeleteCategory = function(categoryId, index) {
        $scope.indexForOperation = index;
        $scope.category.idForOperation = categoryId;
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
            case 'adding': addCategory();
                break;
            case 'editing': editCategory();
                break;
            case 'deleting': deleteCategory();
                break;
        }
    }

    /* Функции, выполняющие запросы */
    var addCategory = function() {
        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/categories/',
            method: 'POST',
            data: {
                categoryTitle :     $scope.category.title,
                categoryPrice :     parseInt($scope.category.price) * 100
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfCategories.push({
                objectId :          data.data.objectId,
                categoryTitle :     $scope.category.title,
                categoryPrice :     $scope.category.price
            });
            $scope.prepareToAddCategory();
            $scope.added = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    var editCategory = function() {
        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/categories/' + $scope.category.idForOperation,
            method: 'PUT',
            data: {
                categoryTitle :     $scope.category.title,
                categoryPrice :     parseInt($scope.category.price) * 100
            },
            headers: { 'Content-Type' : 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfCategories[$scope.indexForOperation].categoryTitle = $scope.category.title;
            $scope.listOfCategories[$scope.indexForOperation].categoryPrice = $scope.category.price * 100;
            $scope.updated = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
        });
    }

    var deleteCategory = function() {
        resetFlags();
        $http({
            url: sharedData.getLinks().https + '/categories/' + $scope.category.idForOperation,
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json' }
        }).then(function(data) {
            console.log(data);
            $scope.listOfCategories.splice($scope.indexForOperation, 1);
            $scope.deleted = true;
        }, function(response) {
            console.log("Smth wrong!!");
            console.log(response);
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
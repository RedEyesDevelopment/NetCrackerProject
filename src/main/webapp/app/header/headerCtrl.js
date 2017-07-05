app.controller('headerCtrl', ['$scope', '$rootScope', '$http', '$location', '$rootScope', 'sharedData', 'ROLE',
    function ($scope, $rootScope, $http, $location, $rootScope, sharedData, ROLE) {

    $scope.auth = {};
    $scope.registrationData = {}

    var setRoleFromSever = function() {
        $scope.errMessage = false;
        $http({
            url: 'http://localhost:8080/auth/isauthorized',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function (data) {
            console.log(data);
            $scope.auth.role = data.data.rolename;
        }, function (response) {
            console.log("Smth wrong!!")
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    };

    var setLinksFromServer = function() {
        $scope.errMessage = false;
        $http({
            url: 'http://localhost:8080/auth/links',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function (data) {
            console.log(data);
            $scope.auth.links = data.data;
        }, function (response) {
            console.log("Smth wrong!!")
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    };

    var setMyselfDataFromServer = function() {
        $scope.errMessage = false;
        $http({
            url: 'http://localhost:8080/users/myself',
            method: 'GET',
            headers: {'Content-Type': 'application/json'}
        }).then(function (data) {
            console.log(data);
            $scope.auth.myself = data.data;
        }, function (response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    };

    var setAuthDataFromServer = function() {
        $scope.errMessage = false;
        // Заменить на вызов функции получение ссылок в случае запуска реального сервера
        // setLinksFromServer();
        $scope.auth.links = { https : "http://localhost:8080" };

        setRoleFromSever();
        setMyselfDataFromServer();
    };

    /* Получение с сервера данных об авторизации */
    sharedData.setAuth($scope.auth); // добавить в общую фабрику объект данных об авторизации
    setAuthDataFromServer();

    /* Следит за изменениями роли, и в соответствии с ней меняет значение isAuthorized */
    $scope.$watch('auth.role', function(newRole) {
        $scope.auth.isAuthorized = false;
        $scope.auth.isAdmin = false;
        $scope.auth.isReception = false;
        $scope.auth.isClient = false;
        switch (newRole) {
            case ROLE.ADMIN: 
                $scope.auth.isAuthorized = true;
                $scope.auth.isAdmin = true;
                break;
            case ROLE.RECEPTION:
                $scope.auth.isAuthorized = true;
                $scope.auth.isReception = true;
                break;
            case ROLE.CLIENT:
                $scope.auth.isAuthorized = true;
                $scope.auth.isClient = true;
                break;
            case ROLE.NA:
                $location.path('/');
        }
    });

    $scope.login = function() {
        $scope.errMessage = false;
        $scope.hideFailAuthMessage();
        $http({
            url: 'http://localhost:8080/auth/login',
            method: 'POST',
            data: {
                "login" : $scope.auth.incoming.login,
                "password" : $scope.auth.incoming.password
            },
            headers: {'Content-Type' : 'application/json'}
        }).then(function (data) {
            console.log(data.data);
            setAuthDataFromServer();
            if (data.data) {
                // if authorized
                $('#loginFormClose').trigger('click');
                sharedData.searchRoomTypes();
            } else {
                // WARNING!! DRY VIOLATION!!!
                $scope.auth.incoming.failed = true;
            }
        }, function (response) {
            console.log(response);
            // WARNING!! DRY VIOLATION!!!
            $scope.auth.incoming.failed = true;
        });
    };

    $scope.resetPassword = function() {
        $scope.errMessage = false;
        $http({
            url: 'http://localhost:8080/cpass/for',
            method: 'POST',
            data: {
                "email": $scope.auth.resetEmail
            },
            headers: {'Content-Type' : 'application/json'}
        }).then(function (data) {
            console.log(data.data);
        }, function (response) {
            console.log("Smth wrong!!");
            console.log(response);
            $scope.errMessage = response.data.message;
        });
    }

    $scope.hideFailAuthMessage = function() {
        $scope.auth.incoming.failed = false;
    };

    $scope.hideNotTheSamePasswordsMessage = function() {
        $scope.registrationData.passwordsNotTheSame = false;
    };

    $scope.logout = function() {
        $scope.errMessage = false;
        $http({
            url: 'http://localhost:8080/logout',
            method: 'GET',
            headers: {'Content-Type' : 'application/json'}
        }).then(function (data) {
            console.log(data);
        }, function (response) {
            console.log(response);
            $scope.auth.role = ROLE.NA;
            $location.path('/');
        });
    };

    $scope.registration = function() {
        $scope.errMessage = false;
        $scope.hideNotTheSamePasswordsMessage();
        if ($scope.registrationData.userPasswordRepeat === $scope.registrationData.userPassword) {
            $http({
                url: 'http://localhost:8080/users/registration',
                method: 'POST',
                data: {
                    "objectId": 0,
                    "email": $scope.registrationData.userEmail,
                    "password": $scope.registrationData.userPassword,
                    "firstName": $scope.registrationData.userName,
                    "lastName": $scope.registrationData.userSurname,
                    "additionalInfo": $scope.registrationData.userInfo,
                    "enabled": true,
                    "role": {
                        "objectId": 0,
                        "roleName": ""
                    },
                    "phones": [
                        {
                            "objectId": 0,
                            "userId": 0,
                            "phoneNumber": $scope.registrationData.userPhone
                        }
                    ]
                },
                headers: {'Content-Type' : 'application/json'}
            }).then(function (data) {
                console.log(data);
                $('#registrationFormClose').trigger('click');
                $scope.auth.incoming.login = $scope.registrationData.userEmail;
                $scope.auth.incoming.password = $scope.registrationData.userPassword;
                $scope.registrationData = {}
                $scope.login();
            }, function (response) {
                console.log(response);
                console.log("Smth wrong!!");
                $scope.errMessage = response.data.message;
            });
        } else {
            $scope.registrationData.passwordsNotTheSame = true;
        }
    }

    $scope.getStatistic = function() {
        $scope.errMessage = false;
        $location.path('/admin/statistic');
    }

}]);
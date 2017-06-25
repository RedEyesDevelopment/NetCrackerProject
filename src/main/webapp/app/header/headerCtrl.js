app.controller('headerCtrl', ['$scope', '$http', '$location', 'sharedData', 'ROLE',
    function ($scope, $http, $location, sharedData, ROLE) {

    $scope.auth = {};

    var setRoleFromSever = function() {
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
        });
    };

    var setLinksFromServer = function() {
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
        });
    };

    var setAuthDataFromServer = function() {
        setRoleFromSever();
        setLinksFromServer();
    }

    /* Получение с сервера данных об авторизации */
    sharedData.setAuth($scope.auth); // добавить в общую фабрику объект данных об авторизации
    setAuthDataFromServer();

    /* Следит за изменениями роли, и в соответствии с ней меняет значение $scope.auth.isAuthorized */
    $scope.$watch('auth.role', function(newRole) {
        if (    newRole === ROLE.ADMIN
             || newRole === ROLE.RECEPTION
             || newRole === ROLE.CLIENT
            ) {
            $scope.auth.isAuthorized = true;
        } else {
            $scope.auth.isAuthorized = false;            
        }
    });

    $scope.login = function() {
        $scope.auth.incoming.failed = false;
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
                $('#loginformclose').trigger('click');
            } else {
                // WARNING!! DRY VIOLATION!!!
                $scope.auth.incoming.failed = true;
            }
        }, function (response) {
            console.log(response);
            // WARNING!! DRY VIOLATION!!!
            $scope.auth.incoming.failed = true;
           setAuthDataFromServer();
        });
    };

    $scope.logout = function() {
        // тут запрос на сервер  ------------------------------------------------ TODO
        $scope.auth.role = ROLE.NA;
    };






    $scope.submit = function(eve) {

        $('.windowRegistration').height(390);
        $('.errorMessage').hide();
        $('.passwordLabel').css({
            'color' : 'black'
        });
        $('.emailLabel').css({
            'color' : 'black'
        });

        if ($scope.userPasswordRepeat === $scope.userPassword) {
            $http({
                url: 'http://localhost:8080/users/registration',
                method: 'POST',
                data: {
                    "objectId": 0,
                    "email": $scope.userEmail,
                    "password": $scope.userPassword,
                    "firstName": $scope.userName,
                    "lastName": $scope.userSurname,
                    "additionalInfo": $scope.userInfo,
                    "enabled": true,
                    "role": {
                        "objectId": 0,
                        "roleName": ""
                    },
                    "phones": [
                        {
                            "objectId": 0,
                            "userId": 0,
                            "phoneNumber": $scope.userPhone
                        }
                    ]
                },
                //headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                headers: {'Content-Type': 'application/json'}
            }).then(function (data) {
                console.log(data);
                alert("Registration done!");
            }, function (response) {
                console.log(response);
                console.log("I_AM_TEAPOT!");
                $('.emailLabel').css({
                    'color' : 'red'
                });
                $('.windowRegistration').height(420);
                $('.errorMessage').text(response.data.message).show(300);
            });
        } else {
            $('.passwordLabel').css({
                'color' : 'red'
            });
            $('.errorMessage').text('Passwords not the same!').show(300);
            $('.windowRegistration').height(420);
        }
    }

}]);
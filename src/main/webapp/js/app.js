angular.module('starter', ['ui.router', 'angularSpinner'])

    .config(function($stateProvider, $urlRouterProvider){
        $stateProvider

            .state('starter', {
                url: "/",
                templateUrl: "index.html"
            })

            .state('inicio', {
                url: "/inicio",
                templateUrl: "templates/main/inicio.html",
                controller: "MainCtrl"
            })

            .state('about', {
                url: "/acerca",
                templateUrl: "templates/main/about.html"
            })

            .state('registro', {
                url: "/registro",
                templateUrl: "templates/main/registro.html",
                controller: "RegisterCtrl"
            })

            .state('p', {
                url: "/p/:_query",
                templateUrl: "templates/main/producciones.html",
                controller: "ProductionsCtrl"
            })

            .state('produccion', {
                url: "/p/id/:_id",
                templateUrl: "templates/main/produccion.html",
                controller: "ProductionCtrl"
            })

            .state('u', {
                url: "/u/:_query",
                templateUrl: "templates/main/usuarios.html",
                controller: "UsersCtrl"
            })

            .state('usuario', {
                url: "/u/id/:_id",
                templateUrl: "templates/main/usuario.html",
                controller: "UserCtrl"
            });

        $urlRouterProvider.otherwise('inicio');
    })

    .service('Login', function(){
        var logged = false;

        return {
            getLoggedStatus: function(){
                return logged;
            },
            setLoggedStatus: function(value){
                logged = value;
            }
        }
    })

    .controller('MainCtrl', [ '$scope', '$state', function($scope, $state){
        $scope.search = function(){
            var query = $scope.q;
            var type = $scope.t;
            if(type==='p'){
                $state.go('p', { _query: query});
            }else if(type==='u'){
                $state.go('u', { _query: query});
            }
        }
    }])

    .controller('LoginCtrl', ['$scope', '$http', '$state', '$stateParams', 'Login', function($scope,$http,$state,$stateParams,Login){
        $scope.logged = Login.getLoggedStatus();

        $scope.logout = function(){
            Login.setLoggedStatus(false);
            $state.go($state.current,$stateParams,{reload: true});
        }
    }])

    .controller('RegisterCtrl', ['$scope', '$http', '$state', 'Login', function($scope,$http,$state,Login){

        $scope.login = function () {
            console.log($scope.loginuser);
            console.log($scope.loginpassword);
            Login.setLoggedStatus(true);
            $state.go('inicio');
        }
        
        $scope.register = function () {
            console.log($scope.name);
            console.log($scope.lastname);
            console.log($scope.birthday);
            console.log($scope.address);
            console.log($scope.phone);
            console.log($scope.user);
            console.log($scope.password);
            console.log($scope.repassword);
            Login.setLoggedStatus(true);
            $state.go('inicio');
        }
    }])

    .controller('ProductionsCtrl', ['$scope', '$http', '$stateParams', function($scope,$http,$stateParams){
        $scope.movies = [ ];
        $scope.hideSpinner = false;

        $http({ method: 'GET', url: '/search?t=p&q='+$stateParams._query+'&p=1' }).success(function(data){
            $scope.hideSpinner = true;
            $scope.movies = data;
            console.log(data);
        });
    }])

    .controller('ProductionCtrl', ['$scope', '$http', '$state', '$stateParams', function($scope,$http,$state,$stateParams) {
        $scope.movie = [ ];
        $scope.hideSpinner = false;

        if(isNaN($stateParams._id)){
            $state.go('p');
        }else {
            $http({ method: 'GET', url: '/display?t=p&id=' + $stateParams._id }).success(function (data) {
                $scope.hideSpinner = true;
                $scope.movie = data;
                console.log(data);
            })
        };
    }])

    .controller('UsersCtrl', ['$scope', '$http', '$stateParams', function($scope,$http,$stateParams){
        $scope.users = [ ];
        $scope.hideSpinner = false;

        $http({ method: 'GET', url: '/search?t=u&q='+$stateParams._query+'&p=1' }).success(function(data){
            $scope.hideSpinner = true;
            $scope.users = data;
            console.log(data);
        });
    }])

    .controller('UserCtrl', ['$scope', '$http', '$state', '$stateParams', function($scope,$http,$state,$stateParams) {
        $scope.user = [ ];
        $scope.hideSpinner = false;

        if(isNaN($stateParams._id)){
            $state.go('u');
        }else {
            $http({ method: 'GET', url: '/display?t=u&id=' + $stateParams._id }).success(function (data) {
                $scope.hideSpinner = true;
                $scope.user = data;
                console.log(data);
            })
        };
    }])

    .directive('producciones', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/partials/movieTemplate.html'
        };
    })

    .directive('produccion', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/partials/movieBigTemplate.html'
        };
    })

    .directive('usuarios', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/partials/userTemplate.html'
        };
    })

    .directive('usuario', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/partials/userBigTemplate.html'
        };
    })

    .directive('footer', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/components/footer.html'
        }
    })

    .directive('searchbar', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/components/searchBar.html'
        }
    });
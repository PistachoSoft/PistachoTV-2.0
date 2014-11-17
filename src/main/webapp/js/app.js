angular.module('starter', ['ui.router'])

    .config(function($stateProvider, $urlRouterProvider){
        $stateProvider

            .state('starter', {
                url: "/",
                templateUrl: "index.html"
            })

            .state('inicio', {
                url: "/inicio",
                templateUrl: "templates/inicio.html",
                controller: "MainCtrl"
            })

            .state('about', {
                url: "/acerca",
                templateUrl: "templates/about.html"
            })

            .state('p', {
                url: "/p/:_query",
                templateUrl: "templates/producciones.html",
                controller: "ProductionsCtrl"
            })

            .state('produccion', {
                url: "/produccion/:_id",
                templateUrl: "templates/produccion.html",
                controller: "ProductionCtrl"
            })

            .state('u', {
                url: "/u/:_query",
                templateUrl: "templates/usuarios.html",
                controller: "UsersCtrl"
            })

            .state('usuario', {
                url: "/usuario/:_id",
                templateUrl: "templates/usuario.html",
                controller: "UserCtrl"
            });

        $urlRouterProvider.otherwise('inicio')
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

    .controller('ProductionsCtrl', ['$scope', '$http', '$stateParams', function($scope,$http,$stateParams){
        $scope.movies = [ ];

        $http({ method: 'GET', url: '/search?t=p&q='+$stateParams._query+'&p=1' }).success(function(data){
            $scope.movies = data;
            console.log(data);
        });
    }])

    .controller('ProductionCtrl', ['$scope', '$http', '$state', '$stateParams', function($scope,$http,$state,$stateParams) {
        $scope.movie = [ ];

        if(isNaN($stateParams._id)){
            $state.go('p');
        }else {
            $http({ method: 'GET', url: '/display?t=p&id=' + $stateParams._id }).success(function (data) {
                $scope.movie = data;
                console.log(data);
            })
        };
    }])

    .controller('UsersCtrl', ['$scope', '$http', '$stateParams', function($scope,$http,$stateParams){
        $scope.users = [ ];

        $http({ method: 'GET', url: '/search?t=u&q='+$stateParams._query+'&p=1' }).success(function(data){
            $scope.users = data;
            console.log(data);
        });
    }])

    .controller('UserCtrl', ['$scope', '$http', '$state', '$stateParams', function($scope,$http,$state,$stateParams) {
        $scope.user = [ ];

        if(isNaN($stateParams._id)){
            $state.go('u');
        }else {
            $http({ method: 'GET', url: '/display?t=u&id=' + $stateParams._id }).success(function (data) {
                $scope.user = data;
                console.log(data);
            })
        };
    }])

    .directive('producciones', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/movieTemplate.html'
        };
    })

    .directive('produccion', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/movieBigTemplate.html'
        };
    })

    .directive('usuarios', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/userTemplate.html'
        };
    })

    .directive('usuario', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/userBigTemplate.html'
        };
    });
angular.module('starter', ['ui.router', 'angularSpinner', 'infinite-scroll'])

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
            })

            .state('comentario', {
                url: "/post/id/:_id",
                templateUrl: "templates/main/comentario.html",
                controller: "CommentCtrl"
            })

            .state('editor', {
                url: "/post/id/:_id/edit",
                templateUrl: "templates/main/creador.html",
                controller: "EditorCtrl"
            });

        $urlRouterProvider.otherwise('inicio');
    })

    .service('Login', function(){
        var logged = false;
        var user = 'anon@not.need';
        var pass = '';

        return {
            getLoggedStatus: function(){
                return logged;
            },
            setLoggedStatus: function(value){
                logged = value;
            },
            getUser: function(){
                return user;
            },
            setUser: function(value){
                user = value;
            },
            getPass: function() {
                return pass;
            },
            setPass: function(value){
                pass = value;
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
        $scope.user = Login.getUser();

        $scope.logout = function(){
            var r = confirm('¿Quiere salir de su sesión?');
            if(r === true){
                Login.setLoggedStatus(false);
                Login.setUser('anon@not.need');
                Login.setPass('');
                $state.go($state.current,$stateParams,{reload: true});
            }
        }
    }])

    .controller('RegisterCtrl', ['$scope', '$http', '$state', 'Login', function($scope,$http,$state,Login){
        $scope.hideSpinner = true;

        $scope.login = function () {
            $scope.hideSpinner = false;
            var form_loginemail = $scope.loginemail;
            var form_loginpassword =  $scope.loginpassword;

            $http({
                method: 'GET',
                url: '/login?email='+form_loginemail+'&pass='+CryptoJS.SHA256(form_loginpassword)
            }).success(function(data,status){
                if(status === 200){
                    Login.setLoggedStatus(true);
                    Login.setUser(data);
                    Login.setPass(CryptoJS.SHA256(form_loginpassword));
                    $scope.hideSpinner = true;
                    $state.go('inicio');
                }else{
                    $scope.hideSpinner = true;
                    alert('Revise sus datos');
                }
            }).error(function(data,status){
                $scope.hideSpinner = true;
                alert('Revise sus datos');
            });
        }
        
        $scope.register = function () {
            $scope.hideSpinner = false;
            var form_name = $scope.name;
            var form_lastname = $scope.lastname;
            var form_birthday = $scope.birthday;
            var form_address = $scope.address;
            var form_phone = $scope.phone;
            var form_email = $scope.email;
            var form_pass = $scope.password;
            var form_repass = $scope.repassword;

            if(form_name.trim() !== "" &&
                form_lastname.trim() !== "" &&
                form_email.indexOf('@') > -1 &&
                form_phone.trim() !== "" &&
                !isNaN(form_phone) &&
                form_pass.trim() !== "" &&
                form_pass === form_repass){
                $http({
                    method: 'POST',
                    url: '/register',
                    data: 'name=' + form_name+'&' +
                            'surname='+form_lastname+'&' +
                            'birthday='+form_birthday+'&' +
                            'address='+form_address+'&' +
                            'phone='+form_phone+'&' +
                            'email='+form_email+'&' +
                            'password='+CryptoJS.SHA256(form_pass),
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }).success(function(data,status){
                    if(status === 200) {
                        Login.setLoggedStatus(true);
                        Login.setUser(data);
                        Login.setPass(CryptoJS.SHA256(form_pass));
                        $scope.hideSpinner = true;
                        $state.go('inicio');

                    }else{
                        $scope.hideSpinner = true;
                        alert('Revise sus datos');
                    }
                }).error(function(data,status){
                    $scope.hideSpinner = true;
                    alert('Revise sus datos');
                });
            }else{
                $scope.hideSpinner = true;
                alert('Revise sus datos');
            }
        }
    }])

    .controller('ProductionsCtrl', ['$scope', '$http', '$stateParams', function($scope,$http,$stateParams){
        $scope.movies = [ ];
        $scope.hideSpinner = false;
        $scope.currentPage = 1;

        $http({ method: 'GET', url: '/search?t=p&q='+$stateParams._query+'&p='+$scope.currentPage }).success(function(data){
            $scope.hideSpinner = true;
            $scope.movies = data;
            console.log(data);
        });

        $scope.loadMoreProductions = function() {
            $scope.currentPage += 1;
            $http({ method: 'GET', url: '/search?t=p&q='+$stateParams._query+'&p='+$scope.currentPage }).success(function(data){
                for(var i = 0; i < data.length; i++) {
                    $scope.movies.push(data[i])
                }
                console.log(data);
            });
        }
    }])

    .controller('ProductionCtrl', ['$scope', '$http', '$state', '$stateParams', 'Login', function($scope,$http,$state,$stateParams,Login) {
        $scope.movie = [ ];
        $scope.hideSpinner = false;
        $scope.showCommentForm = false;

        if(isNaN($stateParams._id)){
            $state.go('p');
        }else {
            $http({ method: 'GET', url: '/display?t=p&id=' + $stateParams._id }).success(function (data) {
                $scope.hideSpinner = true;
                $scope.movie = data;
                console.log(data);
            })
        };
		
		$scope.checkLogin = function(comment){
			if(Login.getLoggedStatus() & (comment.userMail === Login.getUser())){
				return true;
			}else{
				return false;
			}
		}

        $scope.submitComment = function(){
            $scope.hideSpinner = false;

            $http({
                method: 'POST',
                url: '/comment',
                data: 'pid='+$scope.movie._id +'&' +
                    'user='+Login.getUser()+'&' +
                    'title='+$scope.comformtitle+'&' +
                    'text='+$scope.comformtext
                ,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).success(function(data,status){
                $scope.hideSpinner = true;
                $scope.showCommentForm = false;
                $scope.movie.comments.push(data);
            }).error(function(data,status){
                alert('Revise el formulario, todos los campos son necesarios');
                $scope.hideSpinner = true;
            })
        };

        $scope.removeComment = function(id){
            $http({
                method: 'DELETE',
                url: '/comment/'+id
            }).success(function(){
                $state.go($state.current,$stateParams,{reload: true});
            })
        };

        //TEST
        /*
         $scope.submitComment = function(){
             $scope.movie.comments.push({
                 _id: 2,
                 userid: 1,
                 usermail: Login.getUser(),
                 date: '01/01/1993',
                 title: $scope.comformtitle,
                 text: $scope.comformtext
             })
         }

        $scope.movie.comments.push({
            _id: 0,
            userid: 0,
            usermail: 'anon@not.need',
            date: '01/01/1990',
            title: '2good4me',
            text: 'this'
        },
        {
            _id: 1,
            userid: 1,
            usermail: '6uitar6reat6od@gmail.com',
            date: '01/01/1991',
            title: 'wtf',
            text: 'was this shit about lol ggwp'
        })*/
    }])

    .controller('UsersCtrl', ['$scope', '$http', '$stateParams', function($scope,$http,$stateParams){
        $scope.users = [ ];
        $scope.hideSpinner = false;
        $scope.currentPage = 0;

        $http({ method: 'GET', url: '/search?t=u&q='+$stateParams._query+'&p=1'+$scope.currentPage }).success(function(data){
            $scope.hideSpinner = true;
            $scope.users = data;
            console.log(data);
        });

        $scope.loadMoreUsers = function() {
            $scope.currentPage += 1;
            $http({ method: 'GET', url: '/search?t=u&q='+$stateParams._query+'&p='+$scope.currentPage }).success(function(data){
                for(var i = 0; i < data.length; i++) {
                    $scope.users.push(data[i])
                }
                console.log(data);
            });
        }
    }])

    .controller('UserCtrl', ['$scope', '$http', '$state', '$stateParams', 'Login', function($scope,$http,$state,$stateParams,Login) {
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
		
		$scope.checkLogin = function(comment){
			if(Login.getLoggedStatus() & (comment.userMail === Login.getUser())){
				return true;
			}else{
				return false;
			}
		}
		
        $scope.removeComment = function(id){
            $http({
                method: 'DELETE',
                url: '/comment/'+id
            }).success(function(){
                $state.go($state.current,$stateParams,{reload: true});
            })
        };
    }])

    .controller('CommentCtrl', ['$scope', '$http', '$state', '$stateParams', 'Login', function($scope,$http,$state,$stateParams,Login){
        $scope.comment = [ ];
        $scope.hideSpinner = false;

        if(isNaN($stateParams._id)){
            $state.go('inicio');
        }else {
            $http({ method: 'GET', url: '/comment/' + $stateParams._id }).success(function (data) {
                $scope.hideSpinner = true;
                $scope.comment = data;
                console.log(data);
            })
        };

        $scope.removeComment = function(id){
            $http({
                method: 'DELETE',
                url: '/comment/'+id
            }).success(function(){
                $state.go($state.current,$stateParams,{reload: true});
            })
        };
    }])

    .controller('EditorCtrl', ['$scope', '$http', '$state', '$stateParams', 'Login', function($scope,$http,$state,$stateParams,Login){
        $scope.comment = [ ];
        $scope.hideSpinner = false;

        if(isNaN($stateParams._id)){
            $state.go('inicio');
        }else {
            $http({ method: 'GET', url: '/comment/' + $stateParams._id }).success(function (data) {
                $scope.comment = data;
                $scope.hideSpinner = true;
                console.log(data);
            })
        };

        $scope.commit = function(){
            $http({ method: 'PUT', url: '/comment/',
                data: 'id='+comment._id+'&' +
                    'title='+$scope.comformtitle+'&' +
                    'text='+$scope.comformtitle
            }).success(function (data) {
                $state.go('/post/id/:_id', {_id: $scope.comment.id});
            })
        };

        $scope.removeComment = function(){
            $http({
                method: 'DELETE',
                url: '/comment/'+comment._id
            }).success(function(){
                $state.go('inicio');
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
    })

    .directive('navlogin', function(){
        return {
            restrict: 'E',
            templateUrl: 'templates/components/navlogin.html'
        }
    });
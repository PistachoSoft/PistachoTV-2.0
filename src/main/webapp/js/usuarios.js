var app = angular.module('usuarios',[]);

(function(){
    //app.controller('mostrarController', function(){
    app.controller('mostrarController', [ '$http', function($http){
//		this.users = user;
        var mostrar = this;
        mostrar.users = [ ];

        var searchUrl = '';

        if(localStorage.query===undefined){
            searchUrl = '/search?t=u&p=1';
        }else{
            searchUrl = '/search?t=u&q='+localStorage.query+'&p=1';
        }

        localStorage.removeItem('user_id');

        $http({ method: 'GET', url: searchUrl }).success(function(data){
            mostrar.movies = data;
            console.log(data);
        });
    //});
    }]);
	
	/*var user = [
        {
            _id: 1,
        	name: 'David Recuenco',
        	email: 'david.recuencogadea@pistachosoft.com',
        	thumbnail: './img/ajax-loader.gif'
        },
        {
            _id: 2,
        	name: 'Adrian Reyes',
        	email: 'adrian@pistachosoft.com',
        	thumbnail: './img/ajax-loader.gif'
        },
        {
            _id: 3,
        	name: 'Pistacho Anon',
        	email: 'anon@pistachosoft.com',
        	thumbnail: './img/ajax-loader.gif'
        }
       ];*/
})();

(function(){
    app.controller('searchController', function($scope){
        $scope.searchClick = function(){
            var query = document.getElementById('searchInputQuery').value;
            var type = document.getElementById('searchInputType').value;
            localStorage.query=query;
            if(type==='p'){
                window.location.replace("producciones.html");
            }else if(type==='u'){
                window.location.replace("usuarios.html");
            }else{
                window.location.replace('404.html');
            }
        };
    });
})();

(function(){
    app.controller('clickController', function($scope){
        $scope.myClick = function(id){
            localStorage.user_id=id;
            window.location.replace("usuario.html");
        };
    });
})();

app.directive('usuario', function(){
	return {
		restrict: 'E',
		templateUrl: 'userTemplate.html'
	};
});
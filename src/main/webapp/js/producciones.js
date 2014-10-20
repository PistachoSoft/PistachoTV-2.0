var app = angular.module('producciones',[]);

(function(){
	//app.controller('mostrarController', function(){
	app.controller('mostrarController', [ '$http', function($http){
//		this.movies = movie;
		var mostrar = this;
		mostrar.movies = [ ];
		
		$http({ method: 'GET', url: '/search?t=p&id='+localStorage.query }).success(function(data){
			mostrar.movies = data;
		});
	//});
	}]);
	
	/*var movie = [
		{
            _id: 1,
			title: 'Braveheart',
			year: 1995,
			thumbnail: './img/braveheart_thumb.jpg'
		},
		{
            _id: 2,
			title: 'Brave',
			year: 2012,
			thumbnail: './img/brave_thumb.jpg'
		},
		{
            _id: 3,
			title: 'Como entrenar a tu dragon',
			year: 2010,
			thumbnail: './img/httyd_thumb.jpg'
		},
		{
            _id: 4,
			title: 'Como entrenar a tu dragon 2',
			year: 2014,
			thumbnail: './img/httyd_2_thumb.jpg'
		},
		{
            _id: 5,
			title: 'Paranormal Activity',
			year: 2007,
			thumbnail: './img/paranormal_activity_thumb.jpg'
		},
		{
            _id: 6,
			title: 'Paranormal Activity 2',
			year: 2010,
			thumbnail: './img/paranormal_activity_2_thumb.jpg'
		},
		{
            _id: 7,
			title: 'Paranormal Activity 3',
			year: 2011,
			thumbnail: './img/paranormal_activity_3_thumb.jpg'
		},
		{
            _id: 8,
			title: 'Paranormal Activity 4',
			year: 2012,
			thumbnail: './img/paranormal_activity_4_thumb.jpg'
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
                window.location.replace('404');
            }
        };
    });
})();

(function(){
    app.controller('clickController', function($scope){
        $scope.myClick = function(id){
            localStorage.prod_id=id;
            window.location.replace("produccion.html");
        };
    });
})();

app.directive('produccion', function(){
	return {
		restrict: 'E',
		templateUrl: 'movieTemplate.html'
	};
});
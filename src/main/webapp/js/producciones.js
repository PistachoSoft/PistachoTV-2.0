var app = angular.module('producciones',[]);

(function(){
	app.controller('mostrarController', function(){
	//app.controller('mostrarController', [ '$http', function($http){
		this.movies = movie;
		//var mostrar = this;
		//mostrar.movies = [ ];
		
		//$http({ method: 'GET', url: '/loadMovies' }).success(function(data){
		//	mostrar.movies = data;
		//});
	});
	//}]);
	
	var movie = [
		{
			title: 'Braveheart',
			year: 1997,
			cast: 'Mel Gibson',
			images: [
			         {
			        	thumb: './img/ajax-loader.gif' 
			         },
			        ],
		},
		{
			title: 'Brave',
			year: 2013,
			cast: 'Merida',
			images: [
			         {
			        	thumb: './img/ajax-loader.gif' 
			         },
			        ],
		},
	];
})();

app.directive('produccion', function(){
	return {
		restrict: 'E',
		templateUrl: 'movie.html'
	};
});
var app = angular.module('produccion',[]);

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
	
	var movie =
		{
			title: 'Braveheart',
			year: 1995,
			rated: 'R',
			released: '24 May 1995',
			runtime: '177 min',
			genre: 'Action, Biography, Drama',
			director: 'Mel Gibson',
			writer: 'Randall Wallace',
			actors: 'James Robinson, Sean Lawlor, Sandy Nelson, James Cosmo',
			plot: 'When his secret bride is executed for assaulting an English soldier who tried to rape her, a commoner begins a revolt and leads Scottish warriors against the cruel English tyrant who rules Scotland with an iron fist.',
			language: 'English, French, Latin, Scottish Gaelic',
			type: 'movie',
			images: [
			         {
			        	thumb: './img/braveheart_thumb.jpg',
			        	full: './img/braveheart_thumb.jpg',
			        	http: 'http:\/\/ia.media-imdb.com\/images\/M\/MV5BNjA4ODYxMDU3Nl5BMl5BanBnXkFtZTcwMzkzMTk3OA@@._V1_SX300.jpg',
			         },
			        ],
		};
})();

app.directive('produccion', function(){
	return {
		restrict: 'E',
		templateUrl: 'movieBigTemplate.html'
	};
});
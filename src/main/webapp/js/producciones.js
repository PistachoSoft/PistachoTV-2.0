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
			year: 1995,
			images: [
			         {
			        	thumb: './img/braveheart_thumb.jpg' 
			         },
			        ],
		},
		{
			title: 'Brave',
			year: 2012,
			images: [
			         {
			        	thumb: './img/brave_thumb.jpg' 
			         },
			        ],
		},
		{
			title: 'Como entrenar a tu dragon',
			year: 2010,
			images: [
			         {
			        	thumb: './img/httyd_thumb.jpg' 
			         },
			        ],
		},
		{
			title: 'Como entrenar a tu dragon 2',
			year: 2014,
			images: [
			         {
			        	thumb: './img/httyd_2_thumb.jpg' 
			         },
			        ],
		},
		{
			title: 'Paranormal Activity',
			year: 2007,
			images: [
			         {
			        	thumb: './img/paranormal_activity_thumb.jpg' 
			         },
			        ],
		},
		{
			title: 'Paranormal Activity 2',
			year: 2010,
			images: [
			         {
			        	thumb: './img/paranormal_activity_2_thumb.jpg' 
			         },
			        ],
		},
		{
			title: 'Paranormal Activity 3',
			year: 2011,
			images: [
			         {
			        	thumb: './img/paranormal_activity_3_thumb.jpg' 
			         },
			        ],
		},
		{
			title: 'Paranormal Activity 4',
			year: 2012,
			images: [
			         {
			        	thumb: './img/paranormal_activity_4_thumb.jpg' 
			         },
			        ],
		},
	];
})();

app.directive('produccion', function(){
	return {
		restrict: 'E',
		templateUrl: 'movieTemplate.html'
	};
});
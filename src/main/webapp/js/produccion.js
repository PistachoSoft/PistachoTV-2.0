var app = angular.module('produccion',[]);

(function(){
	//app.controller('mostrarController', function(){
	app.controller('mostrarController', [ '$http', function($http){
		//this.movies = movie;
		var mostrar = this;
		mostrar.movies = [ ];

        if(isNaN(localStorage.prod_id)){
            window.location.replace("producciones.html");
        }else {
            $http({ method: 'GET', url: '/display?t=p&id=' + localStorage.prod_id+'&p=1' }).success(function (data) {
                mostrar.movies = data;
                console.log(data);
            });
        }
	//});
	}]);
	
	/*var movie =
		{
            _id: 1,
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
			poster: './img/braveheart_thumb.jpg',
            comments: [
                {
                    user: 'David Recuenco',
                    date: '06/08/2000',
                    text: 'Esta película es la puta hostia, cómo mola!'
                },
                {
                    user: 'Rammus',
                    date: '19/10/2014',
                    text: 'Ok'
                }
            ]
		};*/
})();

app.directive('produccion', function(){
	return {
		restrict: 'E',
		templateUrl: 'movieBigTemplate.html'
	};
});
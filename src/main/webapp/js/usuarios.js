var app = angular.module('usuarios',[]);

(function(){
	app.controller('mostrarController', function(){
		this.users = user;
	});
	
	var user = [
        {
        	name: 'David Recuenco',
        	email: 'david.recuencogadea@pistachosoft.com',
        	avatar: [
        	         {
        	        	 full: './img/favicon.png',
        	        	 thumb: './img/ajax-loader.gif',
        	         },
        	        ],
        },
        {
        	name: 'Adrian Reyes',
        	email: 'adrian@pistachosoft.com',
        	avatar: [
        	         {
        	        	 full: './img/favicon.png',
        	        	 thumb: './img/ajax-loader.gif',
        	         },
        	        ],
        },
        {
        	name: 'Pistacho Anon',
        	email: 'anon@pistachosoft.com',
        	avatar: [
        	         {
        	        	 full: './img/favicon.png',
        	        	 thumb: './img/ajax-loader.gif',
        	         },
        	        ],
        },
       ];
})();

app.directive('usuario', function(){
	return {
		restrict: 'E',
		templateUrl: 'userTemplate.html'
	};
});
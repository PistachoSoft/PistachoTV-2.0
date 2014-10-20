var app = angular.module('usuarios',[]);

(function(){
	app.controller('mostrarController', function(){
		this.users = user;
	});
	
	var user = [
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
       ];
})();

app.directive('usuario', function(){
	return {
		restrict: 'E',
		templateUrl: 'userTemplate.html'
	};
});
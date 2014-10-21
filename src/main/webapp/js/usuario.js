var app = angular.module('usuario',[]);

(function(){
    //app.controller('mostrarController', function(){
    app.controller('mostrarController', [ '$http', function($http){
        //this.users = user;
        var mostrar = this;
        mostrar.movies = [ ];

        if(isNaN(localStorage.user_id)){
            window.location.replace("usuarios.html");
        }else{
            $http({ method: 'GET', url: '/display?t=u&id='+localStorage.user_id+'&p=1' }).success(function(data){
                mostrar.movies = data;
            });
        }
    //});
    }]);

    /*var user =
    {
        _id: 1,
        name: 'David',
        surname: 'Recuenco',
        email: 'david.recuencogadea@pistachosoft.com',
        avatar: '',
        year: '06/08/1993',
        address: 'Void',
        phone: '555-666-777',
        comments: [
            {
                production: 'Braveheart',
                date: '06/08/2000',
                text: 'Esta película es la puta hostia, cómo mola!'
            },
            {
                production: 'Brave',
                date: '06/08/2011',
                text: 'OP'
            }
        ]

    };*/
})();

app.directive('usuario', function(){
    return {
        restrict: 'E',
        templateUrl: 'userBigTemplate.html'
    };
});
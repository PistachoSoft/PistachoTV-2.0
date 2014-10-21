var app = angular.module('index',[]);

localStorage.removeItem('query');

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
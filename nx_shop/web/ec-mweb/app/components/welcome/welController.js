angular
    .module('app')
    .controller('WelController', WelController);

WelController.$inject = ['$rootScope','$scope', '$api','$stateParams'];
function WelController($rootScope,$scope, $api, $stateParams) {
    var events ={};
    $(".wel").css("min-height",screen.availHeight)
}
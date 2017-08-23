angular
	.module('app')
	.controller('AdduserController', AdduserController);

AdduserController.$inject = ['$scope', '$api'];

function AdduserController($scope, $api) {
	var events ={};
	    function addUser() {
	        // $api.postJson('sysuser/add').then(function(resp) {
	        //     $scope.user = resp.data;
	        //     console.log(resp)
	        // });
	    }

    // addUser();

  	events.save = function(form) {
        form.$submitted = true;
        if (form.$invalid) {
            // invalid为false表示通过验证
            return false;
        }
    }

    $scope.events = events;
}
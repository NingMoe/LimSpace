angular
	.module('app')
	.controller('LoginController',LoginController);

LoginController.$inject = ['$rootScope','$scope', '$api','$http'];
function LoginController($rootScope, $scope, $api, $http) {
	var user = {};
	$scope.user = user;
	$scope.isShow = true;	
	function log() {
		$api.get('sysuser/login', user).then(function(resp) {
			resp = resp.data;
			console.log(resp)
			if (resp.code == 200) {
				$scope.user = resp.user;
				localStorage.setItem('loginUser', user.userName);
//				location.href="../#/"+resp.user.url;
				location.href = "../#/welcome";
			} else {
				alert('用户名或密码错误！');
				localStorage.removeItem('loginUser');
				$scope.isShow = !$scope.isShow;
			}
		});
	}
	
	$scope.log = log;

}
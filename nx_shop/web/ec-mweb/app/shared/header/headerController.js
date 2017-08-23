angular
    .module('app')
    .controller('HeaderController', HeaderController);

HeaderController.$inject = ['$scope', '$rootScope', '$location', '$state','$api'];
function HeaderController($scope, $rootScope, $location, $state,$api) {
	var changepwd = [];
	$scope.isShow = false;
	$api.get('sysuser/flush',{}).then(function(resp){
		if(resp.data.code==200){
			$scope.user = resp.data.data;
//			console.log($scope.user)
		}else if(resp.data.code == 401){
			location.href ="../login.html";
		}
	}, function(resp) {
		if (resp.status == 401) {
			location.href ="../login.html";
		}
	});
	
    function activeMenuItem() {
        var path = $location.path();
        $scope.menuIndex = 0;
        if (path === '/ads') {
            $scope.menuIndex = 2;
        }
    }

    $scope.$on('$locationChangeSuccess', function(event, newValue, oldValue) {
        activeMenuItem();
    });
    activeMenuItem();
    
    var events={};
    /*修改密码弹窗*/
    events.change = function() {
    	$scope.changepwd = {};
       	$('#changeModal').modal('show');
    }
    
    /*修改密码*/
    function changePwd(){
    	if($scope.changepwd.newpwd == $scope.changepwd.newpwdagin){
       		changepwd['pwd']=$scope.changepwd.pwd;
    		changepwd['newPass'] = $scope.changepwd.newpwd;	
    		$api.put('sysuser/modifyPass?'+'pwd='+changepwd['pwd']+'&newPass='+changepwd['newPass'],{}).then(function(resp) {
				if(resp.data.code == 200){
					$('#changeModal').modal('toggle');
					alert('密码修改成功！请退出，重新登录');
				}else{
	    			alert(resp.data.message);
				}
	        });
    	}else{
    		alert('两次新密码输入不一致!')
    	}
    }
    /*保存 */
   	events.save=function(form){
   		if(form.invalid){
   			return false;
   		}else{
			changePwd();
   		}
   	}

	events.logout=function(){
		$api.get('/sysuser/logout',{}).then(function(resp){
			location.href ="../login.html";
		})
	}
	
	

	$scope.events=events;
}

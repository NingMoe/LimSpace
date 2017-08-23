angular
    .module('app')
    .controller('SystController', SystController);

SystController.$inject = ['$rootScope','$scope', '$api','$stateParams','$location'];
function SystController($rootScope,$scope, $api, $stateParams, $location) {
    var events ={};
	$scope.upDataId = "";
	var pagination = {
		pageSize:10,
		page:1,
		totalPage:1
	};
	$scope.pagination = pagination;
	$scope.searchParams = {
		page: pagination.page,
		rows:pagination.pageSize
	};
	/*刷新页面*/
	function loadding (index){
		var index = parseInt(index);
		if($scope.paggerInput <= 0 || $scope.paggerInput > $scope.pagination.totalPage || !index){
			alert("请输入正确的数字");
			return false;
		}
		$scope.pagination.page = index;
		$scope.searchParams.page = index;
		$api.get('sysusers',$scope.searchParams).then(function(resp){
			$scope.user=resp.data.data.rows;
			$scope.pagination.totalPage = Math.ceil(resp.data.data.total/$scope.pagination.pageSize);
		});

	 	$api.get('rolesAll',{}).then(function(resp){
	    	$scope.roles=resp.data.data;
	    })
	}
	loadding (1);

    /*添加用户弹窗 */
    events.add = function() {
    	$(".err-msg ul").addClass("ng-hide");
    	$scope.adduser ={};
    	$("input").attr("checked",false);
       	$('#addModal').modal('show');
   	}
    
    /* 修改用户弹窗 */
    events.modify = function(n) {
    	$(".err-msg ul").addClass("ng-hide"); 
    	$('#addModal').modal('show');
        $scope.upDataId = n.id;
		$api.get('sysuser/role',{id:n.id}).then(function(resp){
		    $scope.adduser= resp.data.data;	
		   	$scope.roles=resp.data.data.roles;
		});
    }

    /* 添加、修改用户的请求*/
    events.save = function(form) {
        form.$submitted = true;
        if (form.$invalid) {
            return false;
        }else{
         /*更新*/

          if($scope.upDataId){

		   	var RolesData = {};
		   	/*更新用户数据获取和处理 */
		    $.each($('.ng-scope input:checked'),function(index,item){
//				 console.log(item);
				RolesData[index] = $(item).val();
			})

	        $scope.adduser.roleIds="";
	        $.each(RolesData,function(index,item){
	          if(!item){
	          	item ="";
	          }else{
	          	item = "," + item;
	          }
	          $scope.adduser.roleIds = $scope.adduser.roleIds + item;
	        });
	        
	        $scope.adduser.roleIds = $scope.adduser.roleIds.substr(1);      
          	$api.postJson('sysuer/update',$scope.adduser).then(saveSuccess);
			
          /*添加*/	
          }else{
		   	var RolesData = {};
		   	/*添加用户时数据获取和处理 */
		    $.each($('.ng-scope input:checked'),function(index,item){
				console.log(item);
				RolesData[index] = $(item).val();
			})

	       	$scope.adduser.roleIds="";
	        $.each(RolesData,function(index,item){
	          if(!item){
	          	item ="";
	          }else{
	          	item = "," + item;
	          }
	          $scope.adduser.roleIds = $scope.adduser.roleIds + item;
	        });
	        
	        $scope.adduser.roleIds = $scope.adduser.roleIds.substr(1);
	         /*接口调用 */
	        $scope.adduser.name = $scope.adduser.name;
	        $scope.tel = $scope.adduser.tel;
          	$api.postJson('sysuser/add',$scope.adduser).then(saveSuccess);
          }
          
         	function saveSuccess(resp){
         		console.log(resp)
               	loadding (1);
          		$scope.upDataId ="";
                if(resp.data){
		          	if(resp.data.code == 200){
		          	   $scope.user = resp.data.data;
		          	   $('#addModal').modal('toggle');
		          	   $scope.form.$submitted = false;
		            }else{
		               alert(resp.data.message);
		            }
                }else{
                	if(resp.code == 200){
                		alert(resp.message);
                	}else{
                		alert(resp.message)
                	}
                }
         	}
        }
    }
    /* 重置密码*/
    events.reset=function(n){
    	$api.postJson('sysuser/pwd/reset',n.id).then(function(resp){
    		if(resp.data.code == 200){
    			alert(resp.data.message)
    		}else{
    			alert(resp.message)
    		}
    	});
    }
    /*启用、禁用*/
    events.enable = function(node) {
    	console.log(node.id)
    	var params=[];
   		params['id']=node.id;
   		if(node.status=="ENABLED"){
   			node.status="DISABLED";
   			$scope.isShow = false;
   		}else{
   			node.status="ENABLED";
   		}
   		params['status']=node.status;
   		console.log(params)
   	    $api.post('sysuser/changeStatus',params).then(function(resp) {
            console.log(resp);
        });
    }
    $scope.loadding = loadding;
    $scope.events = events;
}
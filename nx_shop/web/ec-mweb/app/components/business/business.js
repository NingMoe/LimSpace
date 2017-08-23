/**
 *商家管理
 **/
angular
    .module('app')
    .controller('BusinessManagerCtrl', BusinessManagerCtrl);

BusinessManagerCtrl.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope', '$http', '$location'];

function BusinessManagerCtrl($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope, $http, $location) {
    $rootScope.sideBarVisible = true;
    $scope.rankMax=1;

    $scope.getData = function() {
        $api.get('pickups').then(function(resp) {
            $scope.businessList = resp.data.pickupPoint;
            $scope.rankMax = $scope.businessList.length;
        });
    };
    $scope.getData();

    $scope.busineAdd = function(){
        $scope.business = {};
        $scope.business.status = 1;
        $('#addModal').modal('show');
        $("input[name=loginName]").prop("disabled",false);
    };
    $scope.businessSave = function(){
        $scope.form.$submitted = true;
        if($scope.business.password!=$scope.business.repeat){
            $scope.errorRepeat = "密码不一致";
        }else{
            $scope.errorRepeat = "";
        }
        if($scope.form.$invalid || $scope.errorRepeat){
            return false;
        }else{
            $scope.form.$submitted = false;
        }
        if($scope.business.id){ //修改
            $api.putJson('pickups', $scope.business).then(function(resp) {
                // 成功，刷新数据
                if (resp.data.code == 200) {
                    alert("修改成功！");
                    $('#addModal').modal('toggle');
                    $scope.getData();
                }else{
                    alert(resp.data.exception);
                }
            });
        }else{//新增
            $api.postJson('pickups', $scope.business).then(function(resp) {
                // 成功，刷新数据
                if (resp.data.code == 200) {
                    console.log("success!!");
                    alert("添加成功！");
                    $('#addModal').modal('toggle');
                    $scope.getData();
                }else{
                    alert(resp.data.exception);
                }
            });
        }

    };
    //编辑
    $scope.businessEdit = function(id){
        $api.get('pickups/'+id).then(function(resp) {
            $scope.business  = resp.data.pickupPoint;
        });
        $('#addModal').modal('show');
        $("input[name=loginName]").prop("disabled",true);
    };
    //停用 启用
    $scope.businessState = function(id){
        $api.put('pickups/'+id).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
                $scope.getData();
            }else{
                alert(resp.data.exception);
            }
        });
    };
    //排序
    $scope.businessOrder = function(info){
        $('#rankModal').modal('show');
        $scope.business = info;
    };
   
    $scope.doRank = function(){
        $scope.rankForm.$submitted = true;
        if($scope.rankForm.$invalid){
            return false;
        }else{
            $scope.rankForm.$submitted = false;
        }
        $api.put('pickups/'+$scope.business.id+"/rank/"+$scope.business.rank).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
                $scope.getData();
                $('#rankModal').modal('toggle');
            }else{
                alert(resp.data.exception);
            }
        });
    };

}

/**
 *审核员管理
 **/
angular
    .module('app')
    .controller('AuditorManagerCtrl', AuditorManagerCtrl);

AuditorManagerCtrl.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$rootScope', '$http', '$location'];

function AuditorManagerCtrl($scope, $api, $stateParams, $downloadRoot, $imgHost, $rootScope, $http, $location) {
    $rootScope.sideBarVisible = true;

    $scope.getData = function(index) {
        $api.get('pointusers').then(function(resp) {
            $scope.auditorList = resp.data.data;
        });
    };
    $scope.getData(1);

    $scope.auditorAdd = function(){
        $('#addModal').modal('show');
        $scope.auditor = {};
        $scope.auditor.status = 1;
        $scope.auditor.type = 2;
        $api.get('pickups').then(function(resp) {
            $scope.businessList = resp.data.pickupPoint;
        });
    };
    /* 添加或修改营业厅 */
    var str;
    $scope.chk= function (x,z) {
        if (z == true) {
        	str = str || "";
          	str = str + x + ',';
        }else{
    		strs = str.substr(0,str.length-1);
        	strs =  strs.split(",");
        	for ( i in strs ){
          		if( strs[i]== x ){
          			strs.splice(i,1);
          			i--;
          			str = strs.join(",");
          			str = str+",";
                }
            }
        }
        $scope.pickupPointId = str.substr(0,str.length-1)||str;
    };
    //保存
    $scope.auditorSave = function(){
        $scope.form.$submitted = true;
        if($scope.auditor.password!=$scope.auditor.repeat){
            $scope.errorRepeat = "密码不一致";
        }else{
            $scope.errorRepeat = "";
        }
        if($scope.form.$invalid || $scope.errorRepeat){
            return false;
        }else{
            $scope.form.$submitted = false;
        }
        $scope.auditor.pickupPointId = $scope.pickupPointId ;
        if($scope.auditor.id){ //修改
            $api.putJson('pointusers', $scope.auditor).then(function(resp) {
                // 成功，刷新数据
                if (resp.data.code == 200) {
                    alert("修改成功！");
                    $('#addModal').modal('toggle');
                    $scope.getData();
                }else{
                    alert(resp.data.exception);
                }
            });
        }else{//新增
            $api.postJson('pointusers', $scope.auditor).then(function(resp) {
                // 成功，刷新数据
                if (resp.data.code == 200) {
                    alert("添加成功！");
                    $('#addModal').modal('toggle');
                    $scope.getData();
                }else{
                    alert(resp.data.exception);
                }
            });
        }

    };
    //编辑
    $scope.auditorEdit = function(id){
    	$scope.businessList  = {}; 
        $api.get('pickups').then(function(resp) {
            $scope.businessList = resp.data.pickupPoint;
        });
        
        $api.get('pointusers/'+id).then(function(resp) {
            $scope.auditor  = resp.data.data;
            $scope.pickupPointId = resp.data.data.pickupPointId;
            str = resp.data.data.pickupPointId+",";
            $scope.pointChecked =  resp.data.data.pickupPointId.split(",");
            for ( i in $scope.pointChecked ){
               for( j in $scope.businessList ){
              	if( $scope.pointChecked[i]== $scope.businessList[j]["id"] ){
              		$scope.businessList[j].checked =true ;
                }
              }
            }
             
            console.log(' $scope.auditor', $scope.auditor)
        });
        
        $('#addModal').modal('show');
        $("input[name=loginName]").prop("disabled",true);
    };
    //停用
    $scope.auditorStop = function(id){
        $api.put('pointusers/'+id).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
                $scope.getData();
            }else{
                alert(resp.data.exception);
            }
        });
    };


}






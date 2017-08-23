angular
    .module('app')
    .controller('ReturnsDetailController', ReturnsDetailController);

ReturnsDetailController.$inject = ['$scope', '$api', '$stateParams', '$imgHost','$sys','$location', '$webHost'];
function ReturnsDetailController($scope, $api, $stateParams, $imgHost,$sys,$location,$webHost) {
    var params = {
        id: $stateParams.id
    };
    $scope.configSys = $sys;
    $scope.webHost = $webHost;

    function getData() {
        $api.get('orders/returns/details/'+params.id ,{}).then(function(resp) {
            $scope.data = resp.data.data;
            if(resp.data.data.pics != undefined)
            $scope.picarray = resp.data.data.pics.split(",") ;
           // console.log($scope.picarray)
            $scope.logs = resp.data.logs;
            $scope.discount = resp.data.discount;
            $scope.erpCode =resp.data.data.erpCode;
            if($scope.erpCode=='koo'){
            	$scope.data.erpCode=resp.data.data.thirdPartyId;
            	$scope.data.skuHeadThumbnail=$scope.data.skuHeadThumbnail;
            }else{
            	$scope.data.skuHeadThumbnail=$scope.imgHost+"/"+$scope.data.skuHeadThumbnail;
            }
            var totalCoupon=0;
            angular.forEach($scope.discount,function(item){
                totalCoupon = totalCoupon+item.discount;
            });
            $scope.data.totalCoupon  = totalCoupon;   //优惠价格
        });
    }
    getData();

   /**
     * 修改状态机的状态
     */
    function transferState(toState) {
       var ids=[];
	   ids[0] = $stateParams.id;
	if(toState==1){
		$api.putJson('orders/returns/enter', ids).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
                getData();
            }else{
				alert(resp.data.exception)
			}
        });
	}else if(toState==2){
        $api.putJson('orders/returns/refuse', ids).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
                getData();
            }else{
				alert(resp.data.exception)
			}
        });
    }else if(toState==4){

        $api.putJson('orders/returns/delivery', ids).then(function(resp) {
            // 成功，刷新数据

            if (resp.data.code == 200) {
                getData();
            }else{
				alert(resp.data.exception)
			}
        });
	}
	}

    $scope.transferState = transferState;
    $scope.JSON = JSON;
    $scope.imgHost = $imgHost;
}

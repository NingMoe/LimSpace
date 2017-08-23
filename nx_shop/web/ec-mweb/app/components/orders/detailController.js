angular
    .module('app')
    .controller('OrdersDetailController', OrdersDetailController);

OrdersDetailController.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost','$sys'];
function OrdersDetailController($scope, $api, $stateParams, $downloadRoot, $imgHost,$sys) {
    var params = {
        id: $stateParams.id
    };
    var orderStatusBus={
        ob_0:"初始",
            ob_1:"待收货",
            ob_2:"待提货",
            ob_3:"已完成",
            ob_4:"待退货",
            ob_5:"已驳回",
            ob_6:"已退货"};
    $scope.configSys = $sys;
    function getData() {
        $api.get('orders/'+params.id, {}).then(function(resp) {
            $scope.data = resp.data.data;
            $scope.discount = resp.data.discount;
            $scope.logs = resp.data.logs;
            var totalCoupon=0;
            angular.forEach($scope.discount,function(item){
                totalCoupon = totalCoupon+item.discount;
            });
            if($sys=="thb"){
                $scope.data.payText =$scope.data.downPaymentPayed?"已付款":"未付款";
            }else if($sys=="ceb"){
                $scope.data.payText =$scope.data.installmentPayed?"已付款":"未付款";
            }else{
                $scope.data.payText =$scope.data.payedStatus;
            }
            //if($scope.data.pickCode){
            //    $scope.data.statusText = orderStatusBus["ob_"+$scope.data.pickupStatus];
            //}

            $scope.data.totalCoupon  = totalCoupon;   //优惠价格
        });
    }
    getData();

    /**
	 * 制单导出
	 */
	function exportOrder() {
		var ids = [];
		ids[0] = $stateParams.id
		$api.get('excelsMessage', {
			excelId: 'uporders',
			ids: ids
		}).then(function(resp) {
			// 成功，刷新数据
			if(resp.data.code == 200) {
				// 打开Excel
				alert('操作成功');
				window.open($downloadRoot + "/excels?excelId=uporders&ids=" + ids);
				setTimeout(getData, 1000);
			} else {
				alert(resp.data.message);
				console.log(resp.data.exception)
			}
		});
	}

    /**
     * 修改状态机的状态
     */
    function transferState(toState) {
		var ids = [];
		ids[0]= $stateParams.id
		if(toState == 3){
			 $api.putJson('orders/enter', ids).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
            	alert('操作成功');
				getData();
            }else{
				alert(resp.data.exception)

			}
        });
		}else if(toState==5){
		$api.putJson('orders/send', ids).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
            	alert('操作成功');
				getData() ;
            }else{
				alert(resp.data.exception)
			}
        });
       }else if(toState==9){
       	$scope.data.disable = true ;
		$api.putJson('/orders/refuse', $stateParams.id).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
            	alert('操作成功');
				getData() ;
            }else{
				alert(resp.data.exception)
			}
        });
	   }
    }

    $scope.transferState = transferState;
    $scope.exportOrder = exportOrder;
    $scope.JSON = JSON;
    $scope.imgHost = $imgHost;
}

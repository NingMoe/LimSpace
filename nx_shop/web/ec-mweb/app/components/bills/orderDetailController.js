angular
    .module('app')
    .controller('OrderDetailController', OrderDetailController);

OrderDetailController.$inject = ['$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost','$sys'];
function OrderDetailController($scope, $api, $stateParams, $downloadRoot, $imgHost,$sys) {
    var params = {
        id: $stateParams.id,
        erpCode : "koo"
    };
    $scope.configSys = $sys;
    function getData() {
        $api.postJson('findKooOrders/',params).then(function(resp) {
        	if(resp.data.rows[0].cancelStatus==2){
        		resp.data.rows[0].cancelStatus=undefined;
        	}
        	console.log(resp);
            $scope.order = resp.data.rows[0];
        });
    }
    getData();
	function returnGoods(id,NO){
		$scope.order.cancelStatus=0;
		var orderCancellation = {};
		orderCancellation["orderId"] = id;
		orderCancellation["no"] = NO;
		$api.postJson('orders/cancellations',orderCancellation).then(function(resp) {
			 // 成功，刷新数据
            if (resp.data.code == 200) {
            	alert(resp.data.message);
                getData();
            } else {
            	$scope.order.cancelStatus=undefined;
            }
        	
        });
	}
	$scope.returnGoods = returnGoods;
}

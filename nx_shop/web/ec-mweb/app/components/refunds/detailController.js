
angular
    .module('app')
    .controller('RefundsDetailController', RefundsDetailController);

RefundsDetailController.$inject = ['$scope', '$api', '$stateParams', '$imgHost','$sys'];
function RefundsDetailController($scope, $api, $stateParams, $imgHost,$sys) {
    var params = {
        id: $stateParams.id
    };
    $scope.configSys = $sys;
    function getData() {
        $api.get('refunds/'+params.id, {}).then(function(resp) {
            $scope.data = resp.data.data;
            $scope.logs = resp.data.logs;
            $scope.discount = resp.data.discount;
            var totalCoupon=0;
            angular.forEach($scope.discount,function(item){
                totalCoupon = totalCoupon+item.discount;
            });
            $scope.data.totalCoupon  = totalCoupon;   //优惠价格
        });
        $api.get('wxpay/refund/'+params.id, {}).then(function(wxResp) {        	
        	if(wxResp.data.downPayment.PayNo){     
        		$scope.downPayment = wxResp.data.downPayment;
        	}
        	if(wxResp.data.billList && wxResp.data.billList.length && wxResp.data.billList!='不存在分期账单！')
        	{
              $scope.billList = wxResp.data.billList;
            }        	            
        });
    }
    getData();

    /**
     * 进行退款操作
     */
    function doRefund(refund) {
        console.log(refund);
        $api.putJson('refunds/enter',refund.id).then(function(resp) {
            // 成功，刷新数据
            if (resp.data.code == 200) {
                getData();
            } else {
                alert(resp.data.exception);
            }
        });
    }

    $scope.doRefund = doRefund;
    $scope.JSON = JSON;
    $scope.imgHost = $imgHost;
}


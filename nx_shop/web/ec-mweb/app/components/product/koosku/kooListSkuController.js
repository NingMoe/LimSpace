angular
    .module('app')
    .controller('ProductKooSkuController', ProductKooSkuController);

ProductKooSkuController.$inject = ['$scope', '$rootScope', '$stateParams', '$location', '$api', '$param', '$downloadRoot'];
function ProductKooSkuController($scope, $rootScope, $stateParams, $location, $api, $param, $downloadRoot) {
    var events = {};
    var categoryId = $stateParams.categoryId;
    $scope.currentId = categoryId || 0;
    var stateMaps = [
        {id: 1, name: '上架'},
        {id: 0, name: '下架'}
    ];

    /**
     * 递归的方式生成树
     */
    function formatTree(data, level) {
        var hasSelected = false;
        level = level || 0;
        $.each(data, function(index, node) {
            var selected = $scope.currentId == node.id;
            hasSelected = hasSelected || selected;
            node.level = level;
            node.state = {
                selected: selected
            };

            if (!node.nodes) {
                return;
            }
            if (node.nodes.length === 0) {
                delete node.nodes;
                return;
            }
            hasSelected = formatTree(node.nodes, level + 1) || hasSelected;
        });

        return hasSelected;
    }

    function getSelected() {
        var selectedNodes = $('#tree').treeview('getSelected');
        // 显示一级分类
        if (selectedNodes.length === 0) {
            $scope.currentNode = {
                nodes: $scope.tree
            };
            $scope.currentId = 0;
        } else {
            var node = selectedNodes[0];
            $scope.currentNode = node;
            $scope.currentId = node.id;
        }
    }

    function getSpus() {
        var params = null;
	   	var status=null;
	   	var supplerId = null;
	   	if($scope.searchInput!=undefined&&$scope.searchInput.supplier!=null){
	    	supplerId = $scope.searchInput.supplier.id;
	    }
	   	if($scope.searchStateMap!=undefined&&$scope.searchStateMap.stateMap!=null){
	   		status = $scope.searchStateMap.stateMap.id;
	   	}
        $api.get('findSpus',
        {categoryId: $scope.currentId,
   		isstatus:status,
   		supplerId:supplerId
   	    }).then(function(resp) {
            var spus = resp.data.data;
            $scope.spus = spus;
        });
    }
    /*****kong chui long  ******/
   function getSkus(){
   	var spuId=null;
   	var status=null;
   	var supplerId = null;
   	if($scope.searchInput!=undefined&&$scope.searchInput.supplier!=null){
    	supplerId = $scope.searchInput.supplier.id;
    }
   	if($scope.searchStateMap!=undefined&&$scope.searchStateMap.stateMap!=null){
   		status = $scope.searchStateMap.stateMap.id;
   	}
   	if($scope.spu!=undefined){
   		spuId=$scope.spu.id;
   	}
   	$api.get('findSkuList',
   	    {categoryId: $scope.currentId,
   		spuId:spuId,
   		isstatus:status,
   		supplerId:supplerId
   	    }).then(function(resp){
   	    	var skus = resp.data.data;
   	    	$scope.skus = skus;
   	    })
        var height=$(window).height();
        $('.sputable').css({'height':((height-240)/2),'display':'none'});
//      $('.skutable').css({'height':((height-240)/2),'display':'block'});
        $('.skutable').css({'height':((height-240)),'display':'block'});
   }
    function getTree() {
        // 获取树形
        $api.get('categorys/tree', {parentId: 0}).then(function(resp) {
            var tree = resp.data.data;
            // 刷新树后，是否有选中的节点
            var hasSelected = formatTree(tree);
            $scope.tree = tree;
            // 渲染左侧树
            $('#tree').treeview({
                data: tree,
                levels: 1
            });
            // 刷新右侧
            getSelected();
            getSpus();
            getSkus();

            $('#tree').on('nodeSelected', function(event, data) {
                getSelected();
                // 刷新右侧
                if ($scope.currentId) {
                    getSpus();
                    getSkus();
                }
                // https://segmentfault.com/q/1010000003796580
                $scope.$apply();
            });
            $('#tree').on('nodeUnselected', function(event, data) {
                getSelected();
                if (!$scope.currentId) {
                    getSpus();
                    getSkus();
                }
                $scope.$apply();
            });
        });
    }
    getTree();
    
    /******供应商*********/
   getSelectSupllier();
   function getSelectSupllier(){
   	$api.get("findListSupllierList",{}).then(function(resp){
   		var supplierList = resp.data.data;
   		$scope.suppliers = supplierList;
   	});
   }
    

    events.toggleTree = function() {
        $scope.showTree = !$scope.showTree;
    };
    events.display = function(){
    	if($scope.isShow){
            var height=$(window).height();
            $('.sputable').css({'height':((height-240)/2),'display':'block'});
            $('.skutable').css({'height':((height-240)/2),'display':'block'});
            $scope.isShow = false;
    	}else{
    	    var height=$(window).height();
            $('.sputable').css({'display':'none'});
            $('.skutable').css({'height':(height-240)});
            $scope.isShow = true;
    	}     
    }
    /***查询所有信息***/
    var tspu = {
        info:false
    };
    events.checkSpu=function(spu){
        
        tspu.info =false;
        spu.info = true;
        tspu = spu;
    	$scope.spu=spu;
    	getSkus();//查询数据
    }
    events.query=function(spu){
    	getSpus();
    	getSkus();//查询数据
    }
    var installments = {};
    events.editsku=function(sku){
    	var sku_id = sku.id;
    	$api.post("findSkuById",{id:sku_id}).then(function(data){
    		var originalPrice = data.data.kooSku.originalPrice;
    		var file9 = data.data.skuExt.field9;
    		var file10 = data.data.skuExt.field10;
    		$scope.originalPrice = originalPrice;
    		$scope.file9 = file9;
    		$scope.file10 = file10;
    		$scope.skuExt = data.data.skuExt;
    		$scope.sku = data.data.kooSku;
    		//显示要编辑的部分
//  		console.log($scope.sku.installment)
    		var installment = [];
    		$scope.installment = installment;
    		installment = $scope.sku.installment.split(",");
    		
    		for(var i=0;i<5;i++){
	    		for(var j=0;j<installment.length;j++){
	    			if($("input[type='checkbox']").eq(i).val()==installment[j]){
	    				installments[$("input[type='checkbox']").eq(i).val()]=installment[j];
	    			}
			    }
			}
			JSON.stringify(installments);
			$scope.installments = installments;
    		$('.editskubox').css({'display':'block'});
    	});
        
    }
    events.savesku=function(){
    	/*编辑修改*/
    	var sku = $scope.sku;
    	var months = [];
    	var strInstallment="";
    	$.each($('input:checkbox'),function(index,item){
			months[index] = $(item).val();
			if(item.checked==true){
				if(strInstallment==""){
					strInstallment = $(item).val();
				}else{
					strInstallment=strInstallment+","+$(item).val();
				}
			}
			
		})
    	$scope.sku.installment=strInstallment;
    	var kooSku = sku;
        $scope.kooSku = kooSku;
        var skuExt =$scope.skuExt;
    	$api.postJson('updateKooSku', kooSku).then(function(data){
    		var code = data.data.code;
    		if(code=="200"){
    			$api.postJson('updateSkuExt', skuExt).then(function(data){
    				var code = data.data.code;
    		           if(code=="200"){
//  		           	  alert('保存成功');
    	                 getSkus();//查询数据
    		           	 $('.editskubox').css({'display':'none'});
    		           }
    			});
    		}
    	});
        
    }
    events.close=function(){
       $('.editskubox').css({'display':'none'}); 
    }
    $scope.showTree = true;
    $scope.events = events;
    $scope.stateMaps = stateMaps;

};
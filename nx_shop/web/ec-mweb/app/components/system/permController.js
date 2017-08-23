angular
	.module('app')
	.controller('PermController',PermController);
PermController.$inject = ['$scope', '$api'];
function PermController($scope,$api){
	var events = {};
	$scope.currentId = 0;
	/* 递归的方式生成树 */
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
                nodes: $scope.menu
            };
            $scope.currentId = 0;
        } else {
            var node = selectedNodes[0];
            $scope.currentNode = node;
            $scope.currentId = node.id;
        }
    }
    function getTree() {
        // 获取树形
        $api.get('pre/tree', {parentId: 0}).then(function(resp) {
            var menu = resp.data.data;
            // 刷新树后，是否有选中的节点
            var hasSelected = formatTree(menu);
            $scope.menu = menu;
            // 渲染左侧树
            $('#tree').treeview({
	            data: $scope.menu,
	            show: true,
	            levels: 1
            });
            // 刷新右侧
            getSelected();
            $('#tree').on('nodeSelected', function(event, data) {
                console.log('nodeSelected');
                // 刷新右侧
                getSelected();
                // https://segmentfault.com/q/1010000003796580
                $scope.$apply();
            });
            $('#tree').on('nodeUnselected', function(event, data) {
                getSelected();
                $scope.$apply();
            });
        });
    }
    getTree();
    //  渲染树高度
    var $height= $(window).height();
	$('#tree-container').css({"height":$height-180});
    
    /* 添加分类弹窗 */
    events.add = function() {
        $scope.addperm = {};
        $('#addModal').modal('show');
    }
    
    /* 修改分类弹窗 */
       	var nodeId = [];
    events.modify = function(node) {
    	nodeId['id'] = node.id;
        $api.get('perm/get',nodeId).then(function(resp) {
//          console.log(nodeId);
            $scope.addperm = resp.data.data;
            $('#addModal').modal('show');
        });
    }

    function saveSuccess(resp) {
        getTree();
        $('#addModal').modal('toggle');
        $scope.form.$submitted = false;
    }
    
    /*添加权限*/
   function addPerm(){
   	var perm = [];
   	perm['name'] = $scope.addperm.name;
   	perm['code'] = $scope.addperm.code;
   	if($scope.addperm.describ){
   		perm['describ'] = $scope.addperm.describ;
   	}else{
   		perm['describ'] = "";
   	}
   	perm['parentId'] = $scope.addperm.parentId;
   	perm['url'] = $scope.addperm.url;
   	$api.post('perm/add',perm).then(function(resp){
	  	if(resp.data.code == 200){
	  		if($scope.addperm.describ){
		   				$scope.addperm = resp.data.data;
			  			saveSuccess();
		   			}else{
		   				$scope.addperm.describ = "";
		   				saveSuccess();
		   			}
	  	}else{
	  		alert(resp.data.message);
	  	}
       
   	})
   }
    
    /* 添加、修改分类的请求  */
    events.save = function(form) {
        form.$submitted = true;
        if (form.$invalid) {
            return false;
        }
        if($scope.addperm.id){
    		var perm = {};
		   	perm['name'] = $scope.addperm.name;
		   	perm['code'] = $scope.addperm.code;
		   	if($scope.addperm.describ){
		   		perm['describ'] = $scope.addperm.describ;
		   	}else{
		   		perm['describ'] = "";
		   	}
		   	perm['parentId'] = $scope.addperm.parentId;
		   	perm['id'] = $scope.addperm.id;
		   	perm['url'] = $scope.addperm.url;
		   	$api.postJson('perm/update',perm).then(function(resp){
		   		console.log(resp)
		   		if(resp.data.code == 200){
		   			if($scope.addperm.describ){
		   				$scope.addperm = resp.data.data;
			  			saveSuccess();
		   			}else{
		   				$scope.addperm.describ = "";
		   				saveSuccess();
		   			}
			  		
			  	}else{
			  		alert(resp.data.exception);
			  	}
		   	});
    	}else{
    		addPerm();		
    	}
    }
    
     /* 删除分类  */
    events.remove = function(node) {
    	var permId = [];
    	permId['id'] = node.id;
        $api.post('perm/delete',permId).then(function(resp) {
        	console.log(resp);
        	if(resp.data.code==400){
        		alert(resp.data.message);
        	}
            getTree();
        });
    };

    $scope.rankNode = {};
	
	$scope.events = events;
	
}
	
	

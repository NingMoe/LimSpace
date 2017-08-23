angular
    .module('app')
    .controller('ShowrolesController', ShowrolesController);

ShowrolesController.$inject = ['$scope', '$api'];
function ShowrolesController($scope, $api) {
    var events = {};
    var params = [];
    var upDataId = [];
    /* 递归的方式生成树 */
	function formatTree(data, level) {
        var hasSelected = false;
        level = level || 0;
        $.each(data, function(index, node) {
//      	console.log(node)
            var selected = $scope.currentId == node.id;
            var expanded = false;
            if(index!=0){
            	expanded = false;
            }
            hasSelected = hasSelected || selected;
            node.level = level;
            node.state = {
                selected: selected,
                expanded: expanded,
                checked: node.checked
            };
            
            if(node.nodes.length>0){
            	node.selectable = false;
            }

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
    /* 获取选中的复选框id */
	function getChecked(){
		var nodeid = [];
   		var permId = '';
		var perm = $("#tree").treeview('getCheckedNodes');
		for(i in perm){
			nodeid.push(perm[i].id);
			permId = nodeid.join(',');
		}
   		return permId;
	}
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
    function loadding(index){
    	/*查看角色*/
    	var index = parseInt(index);
		if($scope.paggerInput <= 0 || $scope.paggerInput > $scope.pagination.totalPage || !index){
			alert("请输入正确的数字");
			return false;
		}
    	$scope.pagination.page = index;
		$scope.searchParams.page = index;
    	$api.get('roles',$scope.searchParams).then(function(resp){
	    	$scope.roles=resp.data.data.rows;
	    	$scope.pagination.totalPage = Math.ceil(resp.data.data.total/$scope.pagination.pageSize);
	    })	
	    // 渲染树
		$api.get('/pre/tree',{}).then(function(resp){
			var menu = resp.data.data;
			var hasSelected = formatTree(menu);
			$scope.menu =menu;
	       	$('#tree').treeview({
	            data: $scope.menu,
	            showIcon: false,
	            showCheckbox: true,
	            levels: 1
	        });
			$('#tree').on('nodeChecked', function(event, data) {
	        	checkedParents(data,data.state.checked);
		        triggerCheck(data,data.state.checked);
			});
		    $('#tree').on('nodeUnchecked', function(event, data) {
		    	triggerCheck(data,data.state.checked);
		    	unChecked(data);
		    });
		});
    }
    loadding(1);
    /* 选中该节点的所有父节点 */
  	function checkedParents(node,checked){
  		var node = $("#tree").treeview('getParent', node.nodeId);
   		if (node.nodeId != null) {
			node.state.checked = checked;
			checkedParents(node,checked)
        }else{
        	return false;
        }
  	}
  	/* 当最后一个子节点取消时，取消他的父级节点 */
  	function unChecked(node){
  		var siblings = $("#tree").treeview('getSiblings', node);
    	var parentnode = $("#tree").treeview('getParent', node.nodeId);
    	for (var i = 0; i < siblings.length; i++) {
			if(siblings[i].state.checked){
				return false;
			}
		}
    	if (parentnode.nodeId != null) {
			parentnode.state.checked = false;
			unChecked(parentnode);
        }
  	}
    /* 选中该节点下的所有子节点 */
    function triggerCheck(node,checked){
        var childNodes = node.nodes;
        if(childNodes){
        	for (var i = 0; i < childNodes.length; i++) {
        		var temp = $('#tree').treeview('getNode', childNodes[i].nodeId);
            	temp.state.checked = checked;
            	if(temp.nodes){
            		triggerCheck(temp,checked);
            	}
        	}
        }else{
        	node.state.checked = checked;
        	return false;	
        }
    }
//  渲染树高度
    var $height= $(window).height();
	$('#tree-container').css({"height":$height-350});
	/*获取角色及其权限*/   
    function getRole(node){
    	var NodeId = [];
   		NodeId['id'] = node.id;
    	$api.get('/getRolePermis',NodeId).then(function(resp){
    		$('#addModal').modal('show');
			console.log(resp.data)
	   		var perms = resp.data.data;
	   		var hasSelected = formatTree(perms);
	   		$scope.addroles = resp.data.roles;
	   		$scope.perms = perms;
			$('#tree').treeview({
	            data: $scope.perms,
	            showIcon: false,
	            showCheckbox: true,
	            levels: 1
	        });
	        $('#tree').on('nodeChecked', function(event, data) {
	        	checkedParents(data,data.state.checked);
		        triggerCheck(data,data.state.checked);
			});
		    $('#tree').on('nodeUnchecked', function(event, data) {
				triggerCheck(data,data.state.checked);
		    	unChecked(data);
			});
	    })
    }
    
    /*添加分类弹窗*/
    events.add = function() {
    	loadding (1);
    	$scope.addroles = {};
       	$('#addModal').modal('show');
    }
    
    /*添加角色*/
    function addRoles(){
    	if($scope.addroles.name){
    		params['name']=$scope.addroles.name;
    	}else{
    		params['name']="";
    	}
    	params['code']=$scope.addroles.code;
    	params['permIds']  = getChecked();
    	console.log(params)
        $api.post('role/add',params).then(function(resp) {
            if(resp.data.code == 400){
            	alert(resp.data.message)
            }else{
            	$scope.addroles = resp.data;
           		saveSuccess();
            }
        });
    }
    
   	/*隐藏模态框*/
    function saveSuccess(resp) {
    	loadding (1);
 		$('#addModal').modal('toggle');
    }
  
    /*修改角色*/
    events.modify=function(node){
    	loadding (1);
    	$('#addModal').modal('show');
    	$scope.upDataId = node.id;
		getRole(node);
    }
    
    /*保存 */
   	events.save=function(form){
   		form.$submitted=true;
   		if(form.invalid){
   			return false;
   		}else{
   			var params = [];
			params['id']=$scope.addroles.id;
			if($scope.addroles.name){
	    		params['name']=$scope.addroles.name;
	    	}else{
	    		params['name']="";
	    	}
	    	params['code']=$scope.addroles.code;
	    	params['status']=$scope.addroles.status;
	    	params['permIds']  = getChecked();
   			if($scope.upDataId){
          		$api.post('role/update',params).then(saveSuccess);
   			}else{
   				addRoles();
   			}
   		}
   	}
   	 /*启用、禁用*/
    events.enable = function(node) {
    	var change=[];
   		change['id']=node.id;
   		if(node.status=="ENABLED"){
   			node.status="DISABLED";
   		}else{
   			node.status="ENABLED";
   		}
   		change['status']=node.status;
   	    $api.post('changeStatus',change).then(function(resp) {
            console.log(resp);
        });
    }
    /*删除角色*/
   	events.remove=function(node){
		var r=confirm("确定要删除吗？");
		if (r==true){
		 	$api.delete('deleRole/'+node.id).then(function(resp){
	   			console.log(resp);
	   			if(resp.data.code==400){
	   				alert(resp.data.message);
	   			}else if(resp.data.code==200){
	   				alert('删除成功！');
	   			}
	   			loadding(1);
			});
		}else{
		    alert("未删除！");
		}
   	}
   	/*查看角色当前权限*/
   	events.detail = function(node){
    	$scope.upDataId = node.id;
		var CheckId = [];
   		CheckId['id'] = node.id;
    	$api.get('/getRolePermis',CheckId).then(function(res){
    		$('#checkModal').modal('show');
			console.log(res.data)
	   		var perms = res.data.data;
	   		var hasSelected = formatTree(perms);
	   		$scope.checkroles = res.data.roles;
	   		$scope.checkperms = perms;
			$('#checktree').treeview({
	            data: $scope.checkperms,
	            showIcon: false,
	            showCheckbox: true,
	            levels: 1
	        });
	    })
   	}

   	$scope.loadding = loadding;
    $scope.events=events;
}
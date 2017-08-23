angular
    .module('app')
    .controller('ProductSpuListController', ProductSpuListController);

ProductSpuListController.$inject = ['$scope', '$rootScope', '$stateParams', '$location', '$api', '$param', '$downloadRoot'];
function ProductSpuListController($scope, $rootScope, $stateParams, $location, $api, $param, $downloadRoot) {
    var events = {};
    var searchInput= {};
    var categoryId = $stateParams.categoryId;
    $scope.currentId = categoryId || 0;

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
        $api.get('spus', $scope.currentId ? {
            categoryId: $scope.currentId
        } : {}).then(function(resp) {
            var spus = resp.data.data;
            $scope.spus = spus.rows;
        });
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

            $('#tree').on('nodeSelected', function(event, data) {
                getSelected();
                // 刷新右侧
                if ($scope.currentId) {
                    getSpus();
                }
                // https://segmentfault.com/q/1010000003796580
                $scope.$apply();
            });
//          $('#tree').on('nodeUnselected', function(event, data) {
//              getSelected();
//              if (!$scope.currentId) {
//                  getSpus();
//              }
//              $scope.$apply();
//          });
        });
    }
    getTree();

    events.toggleTree = function() {
        $scope.showTree = !$scope.showTree;
    };

    events.search = function (){
    	getSelected();
    	var params = {};
        if($scope.currentId){
        	params.categoryId = $scope.currentId ;
        }
        if($scope.searchInput.spuName){
        	params.name = $scope.searchInput.spuName;
        }
        $api.get('spus', params).then(function(resp) {
            var spus = resp.data.data;
            $scope.spus = spus.rows;
        });
    };

    $scope.showTree = true;
    $scope.events = events;
    $scope.searchInput = searchInput;

};
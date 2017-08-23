angular
    .module('app')
    .controller('ProductTagEditController', ProductTagEditController);

ProductTagEditController.$inject = ['$rootScope', '$location', '$scope', '$stateParams', '$api', '$imgHost', '$state', '$q', '$timeout'];
function ProductTagEditController($rootScope, $location, $scope, $stateParams, $api, $imgHost, $state, $q, $timeout) {
    var events = {};
    var tagId = $stateParams.tagId;
    $scope.currentId = 0;

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

    $api.get('tags/' + tagId).then(function(resp) {
        $scope.tag = resp.data.data;
    });

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
            // 刷新右侧
            if ($scope.currentId) {
                getSkus();
            }

            $('#tree').on('nodeSelected', function(event, data) {
                console.log('nodeSelected');
                // 刷新右侧
                getSelected();
                // 刷新右侧
                if ($scope.currentId) {
                    getSkus();
                }
                // https://segmentfault.com/q/1010000003796580
                $scope.$apply();
            });
            $('#tree').on('nodeUnselected', function(event, data) {
                getSelected();
                // 刷新右侧
                if ($scope.currentId) {
                    getSkus();
                }
                $scope.$apply();
            });
        });
    }
    getTree();

    var tagSkuMap = {};
    function getTagSkus() {
        $api.get('tags/' + tagId + '/skus').then(function(resp) {
            $scope.tagSkus = resp.data.data;
            tagSkuMap = {};
            angular.forEach($scope.tagSkus, function(tagSku) {
                tagSkuMap[tagSku.id] = 1;
            });
        });
    }
    getTagSkus();

    function getSkus() {
        var params = null;
        $api.get('categories/' + $scope.currentId + '/skus').then(function(resp) {
            var skus = resp.data.data;
            console.log(tagSkuMap);
            angular.forEach(skus, function(sku) {
                sku.checked = !!tagSkuMap[sku.id];
            });
            $scope.skus = skus;
        });
    }

    /**
     * 上传分类图标
     */
    events.uploadIcon = function() {

    }

    /**
     * 启用、停用分类
     */
    events.enable = function(node, enabled) {
        $api.put('categories/' + node.id + '/status').then(function(resp) {
            getTree();
        });
    }

    $('#addModal').on('shown.bs.modal', function() {
        $('[name="name"]').focus();
    });

    $('#addModal').on('hidden.bs.modal', function() {
        $scope.form.$submitted = false;
    });

    /**
     * 添加分类弹窗
     */
    events.change = function(sku) {
        // 选中
        if (sku.checked) {
            $scope.tagSkus.push(sku);
            tagSkuMap[sku.id] = 1;
        // 取消选中
        } else {
            for (var i = $scope.tagSkus.length - 1, tagSku; i >= 0; i--) {
                tagSku = $scope.tagSkus[i];
                if (tagSku.id == sku.id) {
                    $scope.tagSkus.splice(i, 1);
                    delete tagSkuMap[sku.id];
                    break;
                }
            }
        }
    };

    /**
     * 修改分类弹窗
     */
    events.modify = function(node) {
        $api.get('categories/' + node.id).then(function(resp) {
            console.log(node.id);
            $scope.category = resp.data.data;
            $('#addModal').modal('show');
        });

    }

    function saveSuccess(resp) {
        getTree();
        $('#addModal').modal('toggle');
    }

    /**
     * (全量)保存标签关联的SKU列表
     */
    events.save = function(event) {
        // 标签关联的 SKU ID 数组
        var tagSkuIds = [];
        angular.forEach($scope.tagSkus, function(tagSku) {
            tagSkuIds.push(tagSku.id);
        });
        var $btn = $(event.target).button('loading');
        $q.all([$api.postJson('tags/' + tagId + '/skus', tagSkuIds), $timeout(1000)]).then(function(resp) {
            console.log(resp);
            $('.alert-toast').text('保存成功').addClass('alert-success').removeClass('alert-danger');
        }).catch(function(resp) {
            $('.alert-toast').text('保存失败').addClass('alert-danger').removeClass('alert-success');
        }).finally(function() {
            $btn.button('reset');
            $('.alert-toast').addClass('in').removeClass('out');
            $timeout(function() {
                $('.alert-toast').addClass('out').removeClass('in');
            }, 2000);
        });
    };

    /**
     * 删除分类
     */
    events.remove = function(index) {
        delete tagSkuMap[$scope.tagSkus[index].id];
        $scope.tagSkus.splice(index, 1);
        angular.forEach($scope.skus, function(sku) {
            sku.checked = !!tagSkuMap[sku.id];
        });

    };

    $scope.rankNode = {};

    /**
     * 排序
     */
    events.rank = function(index) {
        $('#rankModal').modal('show');
        $scope.rankNode = $scope.tagSkus[index];
        $scope.tagSkus[index].rank = index + 1;
    };

    events.doRank = function(rankForm) {
        rankForm.$submitted = true;
        if (rankForm.$invalid) {
            return false;
        }

        var rank = $scope.rankNode.rank;
        var newRank = $scope.rankNode.newRank;

        // 不用排序
        if (newRank == rank) {
            $('#rankModal').modal('hide');
            return false;
        }
        var s =  $scope.tagSkus[parseInt(rank)-1] ; 
       	$scope.tagSkus.splice(parseInt(rank)-1,1);
        $scope.tagSkus.splice(newRank - 1, 0,s);
        $('#rankModal').modal('hide');
    };

    $('#rankModal').on('shown.bs.modal', function() {
        $('[name="rank"]').focus();
    });

    $('#rankModal').on('hidden.bs.modal', function() {
        $scope.rankForm.$submitted = false;
    });

    $scope.upload = {
        percent: 0,
        files: [],
        onFileUploaded: function(up, file) {
            $scope.tag.icon = file.path;
            $('.upload-progress[data-id="' + file.id + '"]').remove();
            up.removeFile(file);
            this.files = [];
        }
    };
    $scope.plupload = plupload;

    $scope.events = events;
    $scope.imgHost = $imgHost;
}

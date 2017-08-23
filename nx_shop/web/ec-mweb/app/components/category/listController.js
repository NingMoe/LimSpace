angular
    .module('app')
    .controller('CategoryListController', CategoryListController);

CategoryListController.$inject = ['$rootScope', '$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$uploader'];
function CategoryListController($rootScope, $scope, $api, $stateParams, $downloadRoot, $imgHost, $uploader) {
    var events = {};
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
    events.add = function() {
        $scope.category = {};
        $('#addModal').modal('show');
    }

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
     * 添加、修改分类的请求
     */
    events.save = function(form) {
        form.$submitted = true;
        if (form.$invalid) {
            return false;
        }

        $scope.category.status = $scope.category.status ? 1 : 0;
        $scope.category.parentId = $scope.currentId;
        if ($rootScope.icon) {
            $scope.category.icon = $rootScope.icon;
            $rootScope.icon = null;
        }

        // 修改
        if ($scope.category.id) {
            $api.putJson('categorys', $scope.category).then(saveSuccess);
        // 添加
        } else {
            $api.postJson('categorys', $scope.category).then(saveSuccess);
        }
    }

    /**
     * 删除分类
     */
    events.remove = function(node) {
        $api.delete('categories/' + node.id).then(function(resp) {
            if (resp.data.code != 200) {
                alert(resp.data.message + (resp.data.exception ? "\n" + resp.data.exception : ''));
                return;
            }
            getTree();
        });
    };

    $scope.rankNode = {};

    /**
     * 排序
     */
    events.rank = function(node) {
        $('#rankModal').modal('show');
        $scope.rankNode = node;
    };

    events.doRank = function(rankForm) {
        rankForm.$submitted = true;
        if (rankForm.$invalid) {
            return false;
        }

        // 不用排序
        if ($scope.rankNode.newRank == $scope.rankNode.rank) {
            $('#rankModal').modal('hide');
            return false;
        }

        $api.put('categorys/' + $scope.rankNode.id + '/ranks/' + $scope.rankNode.newRank).then(function(resp) {
            $('#rankModal').modal('hide');
            if (resp.data.code != 200) {
                alert(resp.data.message + (resp.data.exception ? "\n" + resp.data.exception : ''));
                return;
            }
            getTree();
        });
    }


    $('#rankModal').on('shown.bs.modal', function() {
        $('[name="rank"]').focus();
    });

    $('#rankModal').on('hidden.bs.modal', function() {
        $scope.rankForm.$submitted = false;
    });

    $scope.category = {
        id: 0
    };

    $scope.upload = {
        percent: 0,
        files: [],
        onFileUploaded: function(up, file) {
            $scope.category.icon = file.path;
            $('.upload-progress[data-id="' + file.id + '"]').remove();
            up.removeFile(file);
            this.files = [];
        }
    };
    $scope.plupload = plupload;

    $scope.events = events;
    $scope.imgHost = $imgHost;
}

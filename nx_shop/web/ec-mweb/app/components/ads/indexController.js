angular
	.module('app')
	.controller('AdsIndexController', AdsIndexController);

AdsIndexController.$inject = ['$rootScope', '$scope', '$api', '$stateParams', '$downloadRoot', '$imgHost', '$uploader'];

function AdsIndexController($rootScope, $scope, $api, $stateParams, $downloadRoot, $imgHost, $uploader) {
	var events = {};
	var adId = 0;
	getAdverts();

	function getAdverts() {
		$api.get('adverts').then(function(resp) {
			$scope.adverts = resp.data.data;
			console.log(resp)
		});
	}
	//广告位添加
	events.addAdvert = function() {
		$scope.advert = {};
		$('#addAdvertModal').modal('show');
	}
	events.modifyAdvert = function(advert) {
		$api.get('adverts/' + advert.id).then(function(resp) {
			$scope.advert = resp.data.data;
		});
		
		$('#addAdvertModal').modal('show');
	}
	events.saveAdvert = function() {
		if ($scope.advert.id != undefined) {
			$api.putJson('adverts', $scope.advert).then(saveSuccess);
		} else {
			$api.postJson('adverts', $scope.advert).then(saveSuccess);
		}

		function saveSuccess(resp) {
			if (resp.data.code == 200) {
				$('#addAdvertModal').modal('hide');
				getAdverts();
			} else {
				alert(resp.data.message);
				console.log(resp.data.exception);
			}
		}
	}

	//获取banner
	events.trHit = function(item) {
			$("#" + item.id).siblings().removeClass("active");
			$("#" + item.id).addClass("active")
			adId = item.id;
			getData(item.id);
		}
		//删除广告位
	events.removeAdvert = function(item) {
			$api.delete('adverts/' + item.id).then(function(resp) {
				if (resp.data.code == 200) {
					getAdverts();
				} else {
					alert(resp.data.exception)
				}
			});
		}
		//获取广告位下的banner
	function getData(adId) {
		$api.get('banners/ad/' + adId).then(function(resp) {
			$scope.banners = resp.data.data;
			$scope.currentNode = resp.data.data;
		});
	}
	/**
	 * 添加banner 窗口
	 */
	events.add = function() {
		$scope.banner = {};
		$('#addModal').modal('show');
	}

	/**
	 * 修改banner
	 */
	events.modify = function(node) {
		 var banner = {
            id: node.id,
            title: node.title,
            url: node.url,
            picUrl:node.picUrl,
            advertName:node.advertName
        };
        $scope.banner = banner ; 
        $('#addModal').modal('show');
	}

	/**
	 * 添加、修改banner
	 */
	events.save = function(form) {
		// console.log($scope.banner.picUrl)
		form.$submitted = true;
		if (form.$invalid) {
			// invalid为false表示通过验证
			return false;
		}
		if(adId == 0 ){
			alert("请选择广告位");
			return false ; 
		}
		$scope.banner.adId = adId;
		$scope.banner.invalid = $scope.banner.invalid ? 1 : 0;
		if($scope.banner.upload!=undefined && $scope.banner.upload != ""){
				$scope.banner.picUrl = $scope.banner.upload.files[$scope.banner.upload.files.length-1].path;
			}
		if($scope.banner.picUrl ==""){
			alert("请上传图片");
			return false ; 
		}
		// 修改
		if ($scope.banner.id) {
			 
			$api.putJson('banners', $scope.banner).then(saveSuccess);
			// 添加
		} else {
			
			console.log($scope.banner)
			$api.postJson('banners', $scope.banner).then(saveSuccess);
		}
	}

	$scope.events = events;

	function saveSuccess(resp) {
		getData(adId);
		$('#addModal').modal('toggle');
	}

	/**
	 * 删除banner
	 */
	events.remove = function(item) {
			$api.delete('banners/' + item.id).then(function(resp) {
				if (resp.data.code != 200) {
					alert(resp.data.message + (resp.data.exception ? "\n" + resp.data.exception : ''));
					return;
				} else {
					getData(adId);
				}
			});
		}
		// 初始化上传按钮
	$uploader().init();

    $scope.upload = {
        percent: 0,
        files: [],
        onFileUploaded: function(up, file) {
            $scope.banner.picUrl = file.path;
            $('.upload-progress[data-id="' + file.id + '"]').remove();
            up.removeFile(file);
            this.files = [];
        }
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
		var banner  = {};
		banner.id = $scope.rankNode.id ;
		banner.toRank = parseInt($scope.rankNode.newRank) ;
		console.log(banner);
        $api.putJson('banners/sort',banner).then(function(resp) {
            $('#rankModal').modal('hide');
            if (resp.data.code != 200) {
                alert(resp.data.message + (resp.data.exception ? "\n" + resp.data.exception : ''));
                return;
            }
            getData(adId);
        });
    }


    $('#rankModal').on('shown.bs.modal', function() {
        $('[name="rank"]').focus();
    });

    $('#rankModal').on('hidden.bs.modal', function() {
        $scope.rankForm.$submitted = false;
    });
	$scope.imgHost = $imgHost;
	$scope.plupload = plupload;
}
angular
	.module('app')
	.controller('fileController',fileController);
fileController.$inject = ['$scope', '$api','Upload','$downloadRoot','$location','$apiHost'];
function fileController($scope,$api,Upload,$downloadRoot,$location,$apiHost){
	var events = {};
	//订单导入
	var breakpoint = true ;
	events.impOrder = function(form) {
		
		if(!breakpoint){
           return false ;
		}
		breakpoint = false ;
		Upload.upload({
			method: 'POST',
			url: $apiHost + '/skus/imports',
			data: {
				file: $scope.impOrder.file
			},
			headers: {
				'Content-Type': 'application/x-www-form-urlencoded'
			},
			withCredentials: true
		}).then(function(resp) {
			breakpoint = true ;
			if (resp.data.code== 200){
				   alert(resp.data.data);
			} else if(resp.data.code == 400){
				alert(resp.data.message);
			}
		}, function(resp) {
			breakpoint = true ;
		}, function(evt) {
			//上传进度
			var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
			console.log('progress: ' + progressPercentage + '% '); //+ evt.config.data.file.name
		});
	}
	  events.exportclick = function(){
	     window.open($downloadRoot + "/excels?excelId=skus");  
	  }
$scope.events = events;


}
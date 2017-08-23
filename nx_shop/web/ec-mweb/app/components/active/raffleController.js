angular
	.module('app')
	.controller('RaffleController', RaffleController);

RaffleController.$inject = ['$scope', '$api', '$downloadRoot'];

function RaffleController($scope, $api, $downloadRoot) {
	$scope.page = {"pageSize":10,"currentPage":1};
	var events = {};
	$scope.events = events;
	$scope.init = init;
	$scope.search =search ;
	$scope.exportAll = exportAll;
	init();
	$api.get('promotions', {}).then(function(resp) {
			$scope.promotionList = resp.data.data.rows;
		});

	function init() {
		search();
//		$api.get('promotions/raffle', {}).then(function(resp) {
//			$scope.raffleList = resp.data.data.rows;
//		});
	}
    function search() {
		var searchInput = $scope.searchInput ; 
		if(searchInput == undefined ){
			searchInput = {};
		}
		searchInput.offset = ($scope.page.currentPage-1)*$scope.page.pageSize;
		searchInput.limit = $scope.page.pageSize ;
		//console.log(searchInput)
		$api.get('promotions/raffle', searchInput).then(function(resp) {
			$scope.raffleList = resp.data.data.rows;
			$scope.page.totalCount = resp.data.data.total;
			//console.log($scope.raffleList)
		});
	}

	/**
	 * ��ѯ����
	 */
	function exportAll() {
		window.open($downloadRoot+ "/excels?excelId=raffle") ;
	}
	
	//分页
	//教程 http://www.cnblogs.com/pilixiami/p/5634405.html
	$scope.pageChanged = function(){
		search(($scope.page.currentPage-1)*$scope.page.pageSize,$scope.page.pageSize);
	}
}
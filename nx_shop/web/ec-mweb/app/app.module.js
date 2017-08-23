angular
	.module('app', [
		'ui.router',
		'ngSanitize',
		'ngToast',
		'angular-drag',
		'ngFileUpload',
		'ui.bootstrap'
	])
	.config(['ngToastProvider', function(ngToastProvider) {
		ngToastProvider.configure({
			horizontalPosition: 'center',
			verticalPosition: 'bottom'
		});
	}]).directive('dateFormat', ['$filter', function($filter) {
		var dateFilter = $filter('date');
		return {
			require: 'ngModel',
			link: function(scope, elm, attrs, ctrl) {

				function formatter(value) {
					return dateFilter(value, 'yyyy-MM-dd HH:mm:ss'); //format
				}

				function parser() {
					return ctrl.$modelValue;
				}

				ctrl.$formatters.push(formatter);
				ctrl.$parsers.unshift(parser);

			}
		};
	}]).run(['$api','$rootScope', '$state', '$stateParams', '$location', '$sys',
		function($api,$rootScope, $state, $stateParams, $location, $sys) {
			var titleMap = {
				thb: '淘惠帮管理系统',
				ceb: '光大微商城管理系统',
				tyfq: '水手分期商城管理系统',
				fxb: '分信宝商城管理系统'
			};
			document.title = titleMap[$sys];

			// 根据用户权限，得到菜单数据
			var menu ;
			$api.get('getUserPermis',{}).then(function(resp){
				if(resp.data.code==200){
					menu = resp.data.menu;
					$rootScope.menu = menu;
					activeMenuItem()
					console.log(resp)

				}else if(resp.data.code == 401){

				}
			})
            // 根据用户操作 切换class
			function activeMenuItem() {
				$.each(menu, function(i, menuItem1) {
					menuItem1.active = false;
				   $.each(menuItem1.nodes, function(j, menuItem2) {
						$.each(menuItem2.nodes, function(j, menuItem3) {
							if (menuItem3.link && $location.url().indexOf('/' + menuItem3.link) === 0) {
								menuItem1.active = true;
								menuItem3.active = true;
							} else {
								menuItem3.active = false;
							}
						});
					})
				});
				//用户默认显示拥有权限的第一个权限菜单
				$.each(menu, function(i, menuItem1) {
					if(menuItem1.nodes.length > 0){
						$.each(menuItem1.nodes[0].nodes, function(j,menuItem2) {
							if (menuItem2.link) {
								menuItem1.link = menuItem2.link;
								return false;
							}
						})
			  		}
				});

			}

			


			$rootScope.$on('$locationChangeSuccess', function(event, newValue, oldValue) {
				activeMenuItem();
			});

		}
	]);
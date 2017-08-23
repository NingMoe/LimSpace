angular
    .module('app')
    .config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            // For any unmatched url, redirect to /
            $urlRouterProvider.otherwise('/welcome');

            // Now set up the states
            $stateProvider
                // 我的账户
                .state('ordersSearch', {
                    url: '/orders?status',
                    views: {
                        '': {
                            controller: 'OrdersSearchController',
                            templateUrl: 'app/components/orders/search.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('ordersDetail', {
                    url: '/orders/:id',
                    views: {
                        '': {
                            controller: 'OrdersDetailController',
                            templateUrl: 'app/components/orders/detail.html'
                        }
                    }
                }) .state('pickupSearch', {
                    url: '/pickup?status',
                    views: {
                        '': {
                            controller: 'OrdersSearchController',
                            templateUrl: 'app/components/orders/search.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('pickupDetail', {
                    url: '/pickup/:id',
                    views: {
                        '': {
                            controller: 'OrdersDetailController',
                            templateUrl: 'app/components/orders/detail.html'
                        }
                    }
                }).state('returnsSearch', {
                    url: '/returns?status',
                    views: {
                        '': {
                            controller: 'ReturnsSearchController',
                            templateUrl: 'app/components/returns/search.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('cancelSearch', {
                    url: '/cancel?status',
                    views: {
                        '': {
                            controller: 'ReturnsSearchController',
                            templateUrl: 'app/components/returns/search.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('returnsDetail', {
                    url: '/returns/:id',
                    views: {
                        '': {
                            controller: 'ReturnsDetailController',
                            templateUrl: 'app/components/returns/detail.html'
                        }
                    }
                }).state('refundsSearch', {
                    url: '/refunds?status',
                    views: {
                        '': {
                            controller: 'RefundsSearchController',
                            templateUrl: 'app/components/refunds/search.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('refundsDetail', {
                    url: '/refunds/:id',
                    views: {
                        '': {
                            controller: 'RefundsDetailController',
                            templateUrl: 'app/components/refunds/detail.html'
                        }
                    }
                // 促销
                }).state('ads', {
                    url: '/ads',
                    views: {
                        '': {
                            controller: 'AdsIndexController',
                            templateUrl: 'app/components/ads/index.html'
                        }
                    },
                    reloadOnSearch: false
                    //分享
                }).state('share', {
                    url: '/share',
                    views: {
                        '': {
                            controller: 'shareController',
                            templateUrl: 'app/components/active/share.html'
                        }
                    },
                    reloadOnSearch: false
                //优惠券
                }).state('coupon', {
                    url: '/coupon',
                    controller: 'CouponListCtrl',
                    templateUrl: 'app/components/coupon/couponInfoGrid.html'
                    /*views: { //注意这里的写法，当一个页面上带有多个ui-view的时候如何进行命名和视图模板的加载动作
                        '': {
                            controller: 'CouponListCtrl',
                            templateUrl: 'app/components/coupon/couponInfoGrid.html'
                        },*/
                    /*'couponcate@coupon': {
                        templateUrl: 'app/components/coupon/couponLeftBar.html'
                    },*/
                    /*'coupongrid@coupon': {
                        templateUrl: 'app/components/coupon/couponInfoGrid.html'
                        }
                    }*/,
                    reloadOnSearch: false
                }).state('couponInfoDetail', {
                    url: '/couponInfoDetail/:couponInfoId', //注意这里在路由中传参数的方式
                    controller: 'CouponInfoDetailCtrl',
                    templateUrl: 'app/components/coupon/couponInfoDetail.html',
                    reloadOnSearch: false
                }).state('couponAdd', {
                    url: '/couponAdd',
                    controller: 'AddCouponCtrl',
                    templateUrl: 'app/components/coupon/addCoupon.html',
                    reloadOnSearch: false
                }).state('couponEdit', {
                    url: '/couponEdit/:couponInfoId',
                    controller: 'EditCouponCtrl',
                    templateUrl: 'app/components/coupon/editCoupon.html',
                    reloadOnSearch: false
                }).state('couRecord', {
                    url: '/couRecord',
                    controller: 'CouponRecordCtrl',
                    templateUrl: 'app/components/coupon/couponRecord.html',
                    reloadOnSearch: false
                }).state('couponSend', {
                    url: '/couponSend/:couponInfoId/:couponInfoName',
                    controller: 'SendCoupCtrl',
                    templateUrl: 'app/components/coupon/sendCoupon.html',
                    reloadOnSearch: false
                }).state('couponRecordDetail', {
                    url: '/couponRecordDetail/:couponRecordId',
                    controller: 'CouponRecordDetailCtrl',
                    templateUrl: 'app/components/coupon/couponRecordDetail.html',
                    reloadOnSearch: false
                // 用户留言
                }).state('leMsg', {
                    url: '/leMsg',
                    views: {
                        '': {
                            controller: 'LeMsgCtrl',
                            templateUrl: 'app/components/leMsg/leMsg.html'
                        }
                    }
                }).state('survey', {
                    url: '/survey',
                    views: {
                        '': {
                            controller: 'leaveMessageController',
                            templateUrl: 'app/components/user/leaveMessage.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('category', {
                    url: '/product/category',
                    views: {
                        '': {
                            controller: 'CategoryListController',
                            templateUrl: 'app/components/category/list.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('categoryTemplate', {
                    url: '/product/category/template/:id',
                    views: {
                        '': {
                            controller: 'CategoryTemplateController',
                            templateUrl: 'app/components/category/template.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('listSpu', {
                    url: '/product/spu?categoryId',
                    views: {
                        '': {
                            controller: 'ProductSpuListController',
                            templateUrl: 'app/components/product/spu/list.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('addSpu', {
                    url: '/product/spu/add?categoryId',
                    views: {
                        '': {
                            controller: 'ProductSpuEditController',
                            templateUrl: 'app/components/product/spu/edit.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('updateSpu', {
                    url: '/product/spu/:spuId',
                    views: {
                        '': {
                            controller: 'ProductSpuEditController',
                            templateUrl: 'app/components/product/spu/edit.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('listTag', {
                    url: '/product/tag?tagId',
                    views: {
                        '': {
                            controller: 'ProductTagListController',
                            templateUrl: 'app/components/product/tag/list.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('tagSku', {
                    url: '/product/tag/sku?tagId',
                    views: {
                        '': {
                            controller: 'ProductTagEditController',
                            templateUrl: 'app/components/product/tag/edit.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('listKooSku',{//add kong chui long
                	url:'/product/koosku?skuId',
                	views:{
                		'':{
                			controller:'ProductKooSkuController',
                			templateUrl:'app/components/product/koosku/listKooSku.html'
                		}
                	},
                    reloadOnSearch: false
                   }).state('updatekoosku', {
                    url: '/product/koosku/:spuId',
                    views: {
                        '': {
                            controller: 'ProductSpuEditController',
                            templateUrl: 'app/components/product/spu/edit.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('system', {
                    url: '/system',
                    views: {
                        '': {
                            controller: 'SystController',
                            templateUrl: 'app/components/system/syst.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('showroles', {
                    url: '/showroles',
                    views: {
                        '': {
                            controller: 'ShowrolesController',
                            templateUrl: 'app/components/system/showroles.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('permission', {
                    url: '/permission',
                    views: {
                        '': {
                            controller: 'PermController',
                            templateUrl: 'app/components/system/perm.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('promotion', {
                    url: '/promotion',
                    views: {
                        '': {
                            controller: 'PromotionController',
                            templateUrl: 'app/components/active/promotion.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('topic', {
                    url: '/topic',
                    views: {
                        '': {
                            controller: 'TopicController',
                            templateUrl: 'app/components/topic/topic.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('prize', {
                    url: '/prize?promotionId',
                    views: {
                        '': {
                            controller: 'PrizeController',
                            templateUrl: 'app/components/active/prize.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('raffle', {
                    url: '/raffle',
                    views: {
                        '': {
                            controller: 'RaffleController',
                            templateUrl: 'app/components/active/raffle.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('createActivity', {
                    url: '/createActivity',
                    views: {
                        '': {
                            controller: 'createActivityController',
                            templateUrl: 'app/components/active/createActivity.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('seckill', {
                    url: '/seckill',
                    views: {
                        '': {
                            controller: 'seckillController',
                            templateUrl: 'app/components/active/seckill.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('purchase', {
                    url: '/purchase',
                    views: {
                        '': {
                            controller: 'purchaseController',
                            templateUrl: 'app/components/active/purchase.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('welcome', {
                    url: '/welcome',
                    views: {
                        '': {
                            controller: 'WelController',
                            templateUrl: 'app/components/welcome/welcome.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('favorite', {
                    url: '/favorite',
                    views: {
                        '': {
                            controller: 'favoriteController',
                            templateUrl: 'app/components/favorite/favorite.html'
                        }
                    },
                }).state('business', {  //商家
                    url: '/business',
                    views: {
                        '': {
                            controller: 'BusinessManagerCtrl',
                            templateUrl: 'app/components/business/businessList.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('auditor', {     //审核员
                    url: '/auditor',
                    views: {
                        '': {
                            controller: 'AuditorManagerCtrl',
                            templateUrl: 'app/components/business/auditorList.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('file', {     //审核员
                    url: '/files',
                    views: {
                        '': {
                            controller: 'fileController',
                            templateUrl: 'app/components/files/file.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('findBillList', {
                    url: '/findBillList',
                    views: {
                        '': {
                            controller: 'BillController',
                            templateUrl: 'app/components/bills/bill.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('findKooOrders', {
                    url: '/findKooOrders',
                    views: {
                        '': {
                            controller: 'OrderMgController',
                            templateUrl: 'app/components/bills/orderMg.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('orderDetail', {
                    url: '/findKooOrders/:id',
                    views: {
                        '': {
                            controller: 'OrderDetailController',
                            templateUrl: 'app/components/bills/orderDetail.html'
                        }
                    }
                }).state('popularsearch', {
                    url: '/popularsearch',
                    views: {
                        '': {
                            controller: 'popularsearchController',
                            templateUrl: 'app/components/search/popularsearch.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('warning', {
                    url: '/warning',
                    views: {
                        '': {
                            controller: 'warningController',
                            templateUrl: 'app/components/warning/warning.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('headline', {
                    url: '/headline',
                    views: {
                        '': {
                            controller: 'headlineController',
                            templateUrl: 'app/components/headline/headline.html'
                        }
                    },
                    reloadOnSearch: false
						//定时上下架列表
                }).state('shelves', {
                    url: '/shelves',
                    views: {
                        '': {
                            controller: 'ShelvesController',
                            templateUrl: 'app/components/shelves/list.html'
                        }
                    },
                    reloadOnSearch: false
                }).state('shelvesAdd', {
				      url: '/shelves/add',
				      views: {
					  '': {
						controller: 'ShelvesAddController',
						templateUrl: 'app/components/shelves/add.html'
					}
				}
			});
        }]);

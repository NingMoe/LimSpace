angular.
    module('app')
    .directive('tabview', tabview)
    .directive('slider', ['$timeout', '$absoluteUrl', slider])
    .directive('ngMin', ngMin)
    .directive('ngMax', ngMax)
    .directive('formLocator', formLocator)
    .directive("progressCircle", ['$rootScope', progressCircle])
    .directive("sideBar", sideBar)
    .directive("loading", loading)
    .directive("alertView", alertView)
    .directive('fileSelected', fileSelected)
    .directive("confirmView",["$rootScope",confirmView]);
// .directive('placeholder', placeholder);

function fileSelected() {
    return {
        restrict: 'A',
        link: function(scope, elem, attrs) {
            var onChangeHandler = scope.$eval(attrs.fileSelected);
            elem.find('input').on('change', onChangeHandler);
        }
    };
}

function tabview() {
    return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
            var tabItems = elem.children().eq(0).children();
            var viewItems = elem.children().eq(1).children();

            // 初始化高亮的标签，只需要一次，单向读取attrs.tabview
            var tabIndex = parseInt(attrs.tabview || 0, 10);
            tabItems.eq(tabIndex).addClass('active');
            viewItems.eq(tabIndex).addClass('active');

            // 一级菜单点击事件
            tabItems.on('click', function () {
                var tabItem = angular.element(this);
                var index = 0;

                for (var i = tabItems.length - 1; i >= 0; i--) {
                    if (tabItems[i] == tabItem[0]) {
                        index = i;
                        break;
                    }
                }

                // var index = tabItems.indexOf(tabItem);
                var viewItem = viewItems.eq(index);

                tabItems.removeClass('active');
                tabItem.addClass('active');

                viewItems.removeClass('active');
                viewItem.addClass('active');
            });
        }
    };
}

function slider($timeout, $absoluteUrl) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            banners: '='
        },
        link: function (scope, elem, attrs) {
            scope.$absoluteUrl = $absoluteUrl;

            // 当前显示的图片序号
            scope.currentIndex = 0;
            scope.next = function () {
                scope.currentIndex < scope.banners.length - 1 ?
                    scope.currentIndex++ :
                    scope.currentIndex = 0;
            };
            scope.prev = function () {
                scope.currentIndex > 0 ?
                    scope.currentIndex-- :
                    scope.currentIndex = scope.images.length - 1;
            };
            scope.go = function (i) {
                scope.currentIndex = i;
            };
            scope.$watch('currentIndex', function () {
                if (!scope.banners || scope.banners.length === 0) {
                    return;
                }

                // make every image invisible
                for (var i = 0, len = scope.banners.length; i < len; i++) {
                    scope.banners[i].visible = false;
                }

                // make the current image visible
                var currentBanner = scope.banners[scope.currentIndex];
                currentBanner.visible = true;

                sliderFunc(5000);
            });

            scope.$watch('banners', function (newValue, oldValue) {
                // 有可能 newValue 和 oldValue 都是 undefined
                if (!oldValue && newValue) {
                    var currentBanner = scope.banners[scope.currentIndex];
                }
            });

            var timer;
            var sliderFunc = function (time) {
                $timeout.cancel(timer);
                timer = $timeout(function () {
                    scope.next();
                }, time);
            };

            sliderFunc(10000);

            scope.$on('$destroy', function () {
                $timeout.cancel(timer); // when the scope is getting destroyed, cancel the timer
            });
        },
        templateUrl: 'app/shared/slider/sliderView.html'
    };
}

function formLocator() {
    return {
        link: function (scope) {
            scope.$emit('formLocator');
        }
    };
}


function isEmpty(value) {
    return angular.isUndefined(value) || value === '' || value === null || value !== value;
}

function ngMax() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attr, ctrl) {
            scope.$watch(attr.ngMax, function () {
                ctrl.$setViewValue(ctrl.$viewValue);
            });
            var maxValidator = function (value) {
                // var max = scope.$eval(attr.ngMax) || Infinity;
                // scope.$eval('') => undefined
                var max = scope.$eval(attr.ngMax);
                if (isNaN(max)) {
                    max = Infinity;
                }
                if (!isEmpty(value) && value > max) {
                    ctrl.$setValidity('ngMax', false);
                    return undefined;
                } else {
                    ctrl.$setValidity('ngMax', true);
                    return value;
                }
            };

            ctrl.$parsers.push(maxValidator);
            ctrl.$formatters.push(maxValidator);
        }
    };
}

function ngMin() {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attr, ctrl) {
            scope.$watch(attr.ngMin, function () {
                ctrl.$setViewValue(ctrl.$viewValue);
            });
            var minValidator = function (value) {
                var min = scope.$eval(attr.ngMin) || 0;
                if (!isEmpty(value) && value < min) {
                    ctrl.$setValidity('ngMin', false);
                    return undefined;
                } else {
                    ctrl.$setValidity('ngMin', true);
                    return value;
                }
            };

            ctrl.$parsers.push(minValidator);
            ctrl.$formatters.push(minValidator);
        }
    };
}
function progressCircle($rootScope) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            item: '='
        },
        link: function (scope, elem, attrs) {
            scope.ie8 = $rootScope.ie8;
            var svgHeight = attrs.heights;
            var svg_R = svgHeight / 2 + 7;
            var svg_r = svgHeight / 2;
            scope.item.svg_R = svg_R;
            scope.item.svg_r = svg_r;
            var computerX1 = function (progress) {
                if (progress >= 99.99) {
                    progress = 99.9999;
                }
                return svg_R - svg_R * ( Math.sin(2 * Math.PI * progress / 100));

            }
            var computerX2 = function (progress) {
                if (progress >= 99.99) {
                    progress = 99.9999;
                }
                return svg_R - svg_r * ( Math.sin(2 * Math.PI * progress / 100));

            }
            var computerY1 = function (progress) {
                if (progress >= 99.99) {
                    progress = 99.9999;
                }
                return svg_R * (1 + Math.cos(2 * Math.PI * progress / 100));

            }
            var computerY2 = function (progress) {
                if (progress >= 99.99) {
                    progress = 99.9999;
                }
                return svg_r * (1 + Math.cos(2 * Math.PI * progress / 100)) + 7;

            }
            scope.item.progress = Math.floor((100 - scope.item.canBidAmount * 100 / scope.item.financeAmount));
            scope.item.x1 = computerX1(scope.item.progress);
            scope.item.y1 = computerY1(scope.item.progress);
            scope.item.x2 = computerX2(scope.item.progress);
            scope.item.y2 = computerY2(scope.item.progress);
        },
        templateUrl: 'app/shared/progress/progressView.html'

    }

}
function sideBar() {
    return {
        restrict: 'AE',
        replace: true,
        link: function (scope, elem, attr) {
            scope.showText = function (className) {

            }
            scope.onscroll = function () {
                $window.scrollTo(0, 0)
            }

        },
        templateUrl: 'app/shared/sideBar/sideBarView.html'
    }
}

function loading() {
    return {
        restrict: 'E',
        replace: true,
        template: '<div class="loading"><img src="assets/images/loading.gif"/></div>',
        link: function (scope, element, attr) {
            /* scope.$watch('loading', function (val) {

             });*/
        }
    }
}
function alertView() {
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            info: '=info'

        },
        templateUrl: 'app/shared/alert/alertView.html',
        link: function (scope, element, attr) {
            scope.deleteDiv = function () {
                scope.info.Show = 2;
            }

        }
    }
}
function confirmView($rootScope) {
    return {
        restrict: 'AE',
        replace: true,
        scope: {
            info: '=info'

        },
        templateUrl: 'app/shared/alert/confirmView.html',
        link: function (scope, element, attr) {
            scope.deleteDiv = function () {
                scope.info.Show = 2;
            }
            scope.thenFun = $rootScope[scope.info.ThenFun];

        }
    }
}

function placeholder() {
    return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
            elem.value = attrs.placeholder;
        }
    }
}

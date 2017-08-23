angular
    .module('app')
    .factory('$env', [function() {
        var env = location.host.split('.')[0];
        if (env.indexOf('localhost') === 0) {
            env = 'dev';
        } else if (env !== 'dev' && env !== 'test'  && env !== 'test2' && env !== 'stage' && env !== 'dev3') {
            env = 'prod';
        }
        return env;
    }])
    .factory('$sys', [function() {
        var sys = 'ceb'; // ceb thb tyfq fxb
        angular.element('html').addClass(sys);
        return sys;
    }])
    .factory('$webHost', ['$env', '$sys', function($env, $sys) {
        var map = {
            thb: 'http://ec.taohuibang.com',
            tyfq: 'http://tianyaofenqi.com',
            ceb: 'https://cebshop.tyiti.com',
            fxb: 'http://fenxinbao.com'
        };

        if ($env === 'prod') {
            return map[$sys];
        }

        return map[$sys].replace(/\/\/(ec.)?/, '//' + $env + '.').replace(/https/, 'http');
    }])
    .factory('$apiHost', ['$env', '$sys', function($env, $sys) {
		return 'http://127.0.0.1:8882/mapi';
     //   return 'http://localhost:8080/mapi';  //     return 'http://192.168.23.123/mapi';
//	return 'http://192.168.50.32:8080/mapi';
//  return 'http://192.168.50.139:8080/mapi';
        var map = {
            thb: 'taohuibang.com',
            tyfq: 'tianyaofenqi.com',
            ceb: 'cebshop.tyiti.com',
            fxb: 'fenxinbao.com'
        };
        if ($env === 'prod') {
            if ($sys === 'tyfq') {
               return 'http://m.shuishoufenqi.com/api'

            }
            return 'http://m.' + map[$sys] + '/api';
        }
        return 'http://' + $env + '.m.' + map[$sys] + '/api';
    }])
    .factory('$imgHost', ['$env', function($env) {
        return 'http://tyiti.img-cn-beijing.aliyuncs.com';
    }])
    .factory('$apiRoot', ['$apiHost', function($apiHost) {
        return $apiHost;
    }])
    .factory('$downloadRoot', ['$apiHost', function($apiHost) {
        return $apiHost;
    }]).factory('$uploader', ['$rootScope', function ($rootScope) {
        return function() {
            var uploader = new plupload.Uploader({
                runtimes : 'html5,flash,silverlight,html4',
                browse_button : 'selectfiles',
                //multi_selection: false,
                container: document.getElementById('container'),
                flash_swf_url : 'lib/plupload-2.1.2/js/Moxie.swf',
                silverlight_xap_url : 'lib/plupload-2.1.2/js/Moxie.xap',
                url : 'http://oss.aliyuncs.com',

                filters: {
                    mime_types : [ //只允许上传图片和zip文件
                    { title : "Image files", extensions : "jpg,gif,png,bmp" },
                    { title : "Zip files", extensions : "zip,rar" }
                    ],
                    max_file_size : '10mb', //最大只能上传10mb的文件
                    prevent_duplicates : true //不允许选取重复文件
                },

                init: {
                    PostInit: function() {
                        document.getElementById('ossfile').innerHTML = '';
                        document.getElementById('postfiles').onclick = function() {
                        set_upload_param(uploader, '', false);
                        return false;
                        };
                    },

                    FilesAdded: function(up, files) {
                        plupload.each(files, function(file) {
                            document.getElementById('ossfile').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
                            +'<div class="progress"><div class="progress-bar" style="width: 0%"></div></div>'
                            +'</div>';
                        });
                    },

                    BeforeUpload: function(up, file) {
                        set_upload_param(up, file.name, true);
                    },

                    UploadProgress: function(up, file) {
                        var d = document.getElementById(file.id);
                        d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
                        var prog = d.getElementsByTagName('div')[0];
                        var progBar = prog.getElementsByTagName('div')[0]
                        progBar.style.width= 2*file.percent+'px';
                        progBar.setAttribute('aria-valuenow', file.percent);
                    },

                    FileUploaded: function(up, file, info) {
                        if (info.status == 200)
                        {
                            var imgSrc = "http://tyiti.oss-cn-beijing.aliyuncs.com/"+g_object_name;
                             $(".iis").attr("src",imgSrc);
                              $('[name="icon"]').val(imgSrc);
                              $('[name="pic"]').val(imgSrc);
                              $rootScope.icon= imgSrc;
                              $rootScope.pic= imgSrc;
                            document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = 'upload to oss success, object name:' + get_uploaded_object_name(file.name) + ' 回调服务器返回的内容是:' + info.response;
                        }
                        else if (info.status == 203)
                        {
                            document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '上传到OSS成功，但是oss访问用户设置的上传回调服务器失败，失败原因是:' + info.response;
                        }
                        else
                        {
                            document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = info.response;
                        }
                    },

                    Error: function(up, err) {
                        if (err.code == -600) {
                            document.getElementById('console').appendChild(document.createTextNode("\n选择的文件太大了,可以根据应用情况，在upload.js 设置一下上传的最大大小"));
                        }
                        else if (err.code == -601) {
                            document.getElementById('console').appendChild(document.createTextNode("\n选择的文件后缀不对,可以根据应用情况，在upload.js进行设置可允许的上传文件类型"));
                        }
                        else if (err.code == -602) {
                            document.getElementById('console').appendChild(document.createTextNode("\n这个文件已经上传过一遍了"));
                        }
                        else
                        {
                            document.getElementById('console').appendChild(document.createTextNode("\nError xml:" + err.response));
                        }
                    }
                }
            });

            return uploader;
        };
    }])
    /**
     * 传入接口的path和参数，返回完整URL。
     * @param  {String} path 如 ssl/payment/toTaste
     * @param  {Object} params 如 getUrl('invest/toInvest', {rows: 6})
     * @return {String} 完整的URL，包含GET参数
     */
    .factory('$getUrl', ['$apiRoot', '$param', function ($apiRoot, $param) {
        return function (path, params) {
            var search = typeof params === 'object' ? ('?' + $param(params)) : '';
            return $apiRoot + '/' + path + search;
        }
    }])
    /**
     * 功能等于jQuery的$.param(data)
     */
    .factory('$param', [function () {
        return function (data) {
            var query = [];
            var encode = encodeURIComponent;
            for (var paramName in data) {
                query.push(encode(paramName) + '=' + encode(data[paramName]));
            }
            return query.join('&');
        };
    }])
    .factory('$val', ['$rootScope', function ($rootScope) {
        return function (input, $scope) {
            $scope = $scope || $rootScope;
            var parts = input.split('.');
            var length = parts.length;
            if (length < 2) {
                return null;
            }

            var cursor = $scope;
            for (var i = 0, len = parts.length, part; i < len; i++) {
                part = parts[i];
                if (i === len - 1) {
                    return cursor[part];
                }

                if (angular.isObject(cursor[part]) ||
                    angular.isArray(cursor[part])) {
                    cursor = cursor[part];
                } else {
                    return null;
                }
            }
        };
    }])
    .factory('$api', ['$http', '$rootScope', '$param', '$getUrl', function ($http, $rootScope, $param, $getUrl) {
        var api = {};

        api.get = function (url, params) {
            var config = {};
            config.withCredentials = true;
            config.cache = false;

            return $http.get($getUrl(url, params), config);
        };

        api.post = function (url, data, config) {
            config = config || {};
            config.withCredentials = true;

            // 拼接公共参数
            data = data || {};
            data = $param(data);

            config.headers = config.headers || {};
            config.headers['Content-Type'] = 'application/x-www-form-urlencoded';

            return $http.post($getUrl(url), data, config);
        };

        api.postJson = function (url, data, config) {
            config = config || {};
            config.withCredentials = true;

            config.headers = config.headers || {};
            config.headers['Content-Type'] = 'application/json';

            return $http.post($getUrl(url), JSON.stringify(data), config);
        };

        api.put = function (url, data, config) {
            config = config || {};
            config.withCredentials = true;

            // 拼接公共参数
            data = data || {};
            data = $param(data);

            config.headers = config.headers || {};
            config.headers['Content-Type'] = 'application/x-www-form-urlencoded';

            return $http.put($getUrl(url), data, config);
        };

        api.putJson = function (url, data, config) {
            config = config || {};
            config.withCredentials = true;

            config.headers = config.headers || {};
            config.headers['Content-Type'] = 'application/json';

            return $http.put($getUrl(url), JSON.stringify(data), config);
        };

        api.delete = function(url, config) {
            config = config || {};
            config.withCredentials = true;

            return $http.delete($getUrl(url), config);
        };


        return api;
    }]);

'use strict';

angular.module('app')
    .provider('plUploadService', function() {

        var config = {
            flashPath: '/upload/lib/plupload-2.1.2/js/Moxie.swf',
            silverLightPath: '/upload/lib/plupload-2.1.2/js/Moxie.xap',
            uploadPath: 'http://oss.aliyuncs.com'
        };

        this.setConfig = function(key, val) {
                config[key] = val;
            };

        this.getConfig =  function(key) {
                return config[key];
            };

        var that = this;

        this.$get = [function(){

            return {
                getConfig: that.getConfig,
                setConfig: that.setConfig
            };

        }];

    })
    .directive('plUpload', ['$parse', '$log', '$http', 'plUploadService', '$api', function ($parse, $log, $http, plUploadService, $api) {
        return {
            restrict: 'A',
            scope: {
                'plProgressModel': '=',
                'plFilesModel': '=',
                'plFiltersModel': '=',
                'plMultiParamsModel':'=',
                'plInstance': '='
            },
            link: function (scope, iElement, iAttrs) {

                scope.randomString = function(len, charSet) {
                    charSet = charSet || 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
                    var randomString = '';
                    for (var i = 0; i < len; i++) {
                        var randomPoz = Math.floor(Math.random() * charSet.length);
                        randomString += charSet.substring(randomPoz,randomPoz+1);
                    }
                    return randomString;
                }

                if(!iAttrs.id){
                    var randomValue = scope.randomString(5);
                    iAttrs.$set('id',randomValue);
                }
                if(!iAttrs.plAutoUpload){
                    iAttrs.$set('plAutoUpload','true');
                }
                if(!iAttrs.plMultiSelection){
                    iAttrs.$set('plMultiSelection','true');
                }
                if(!iAttrs.plMaxFileSize){
                    iAttrs.$set('plMaxFileSize','10mb');
                }
                if(!iAttrs.plUrl){
                    iAttrs.$set('plUrl', plUploadService.getConfig('uploadPath'));
                }
                if(!iAttrs.plFlashSwfUrl){
                    iAttrs.$set('plFlashSwfUrl', plUploadService.getConfig('flashPath'));
                }
                if(!iAttrs.plSilverlightXapUrl){
                    iAttrs.$set('plSilverlightXapUrl', plUploadService.getConfig('silverLightPath'));
                }
                if(typeof scope.plFiltersModel=="undefined"){
                    scope.filters = {
                        mime_types: [
                            {
                                title: "Image files",
                                extensions : "jpg,jpeg,gif,png"
                            }
                        ],
                        prevent_duplicates: true
                    };
                } else{
                    scope.filters = scope.plFiltersModel;
                }


                var options = {
                    runtimes : 'html5,flash,silverlight',
                        browse_button : iAttrs.id,
                        multi_selection: iAttrs.plMultiSelection.toLowerCase() == 'true',
                //      container : 'abc',
                        max_file_size : iAttrs.plMaxFileSize,
                        url : iAttrs.plUrl,
                        flash_swf_url : iAttrs.plFlashSwfUrl,
                        silverlight_xap_url : iAttrs.plSilverlightXapUrl,
                        filters : scope.filters,
                        drop_element: iAttrs.plDropElement
                }


                if(scope.plMultiParamsModel){
                    options.multipart_params = scope.plMultiParamsModel;
                }


                var uploader = new plupload.Uploader(options);

                uploader.settings.headers = plUploadService.getConfig('headers');

                uploader.init();

                uploader.bind('Error', function(up, err) {
                    if(iAttrs.onFileError){
                        scope.$parent.$apply(iAttrs.onFileError);
                    }

                    $log.error("Cannot upload, error: " + err.message + (err.file ? ", File: " + err.file.name : "") + "");
                    alert("上传失败，错误原因：" + err.message + (err.file ? "，文件名：" + err.file.name : "") + "");
                    up.refresh(); // Reposition Flash/Silverlight
                });

                uploader.bind('FilesAdded', function(up,files) {
                    //uploader.start();
                    scope.$apply(function() {
                        if(iAttrs.plFilesModel) {
                            angular.forEach(files, function(file,key) {
                                if (!scope.plFilesModel) scope.plFilesModel=[];
                                scope.plFilesModel.push(file);
                            });
                        }

                        if(iAttrs.onFileAdded){
                            var fn = $parse(iAttrs.onFileAdded);
                            fn(scope.$parent, {$files:files});
                        }
                    });

                    if(iAttrs.plAutoUpload=="true"){
                        uploader.start();
                    }
                });

                uploader.bind('BeforeUpload', function(up, file) {
                    console.log(up, file, iAttrs.onBeforeUpload);
                    $api.get('oss/policy').then(function(resp) {
                        /*
                        accessid: "wMGOaJpcbDOlf5Mq"
                        callback : "eyJjYWxsYmFja1VybCI6Imh0dHA6Ly8yMjMuMjIzLjE4My40My95enkvbWFwaS9vc3MvY2FsbGJhY2siLCJjYWxsYmFja0hvc3QiOiJvc3MtY24tYmVpamluZy5hbGl5dW5jcy5jb20iLCJjYWxsYmFja0JvZHkiOiJmaWxlbmFtZT0ke29iamVjdH0mc2l6ZT0ke3NpemV9Jm1pbWVUeXBlPSR7bWltZVR5cGV9JmhlaWdodD0ke2ltYWdlSW5mby5oZWlnaHR9JndpZHRoPSR7aW1hZ2VJbmZvLndpZHRofSZ1c2VyaWQ9bnVsbCZpcEhvc3Q9MTkyLjE2OC4xLjEwMyIsImNhbGxiYWNrQm9keVR5cGUiOiJhcHBsaWNhdGlvbi9qc29uIn0="
                        dir: "tyfq/products/"
                        expire: "1462886609"
                        host: "http://tyiti.oss-cn-beijing.aliyuncs.com"
                        policy: "eyJleHBpcmF0aW9uIjoiMjAxNi0wNS0xMFQxMzoyMzoyOS44NDFaIiwiY29uZGl0aW9ucyI6W1siY29udGVudC1sZW5ndGgtcmFuZ2UiLDAsMTA0ODU3NjAwMF0sWyJzdGFydHMtd2l0aCIsIiRrZXkiLCJ0eWZxL3Byb2R1Y3RzLyJdXX0="
                        signature: "9JUlTZ5Ju66yMDVxzYNYLG2sdF0="
                        */
                        var data = resp.data;
                        var pos = file.name.lastIndexOf('.');
                        var suffix = '';
                        if (pos != -1) {
                            suffix = file.name.substring(pos);
                        }
                        var now = Math.floor(Date.now() / 1000);
                        if (data.expire < now) {
                            alert('过期了');
                        }
                        var multipartParams = {
                            key: data.dir + file.id.substr(2) + suffix,
                            policy: data.policy,
                            OSSAccessKeyId: data.accessid,
                            success_action_status: 200, // 让服务端返回200,不然，默认会返回204
                            callback: data.callback,
                            signature: data.signature
                        };
                        up.setOption({
                            url: data.host,
                            multipart_params: multipartParams
                        });

                        if(iAttrs.onBeforeUpload){
                            var fn = $parse(iAttrs.onBeforeUpload);
                            fn(scope.$parent, {$file:file});
                        }
                        up.start();

                        scope.files = scope.files || {};
                        scope.files[file.id] = data.host + '/' + multipartParams.key;
                        // http://stackoverflow.com/questions/22864949/ajax-inside-beforeupload-event-plupload
                        file.status = plupload.UPLOADING;
                        up.trigger("UploadFile", file);
                    });
                    return false;
                });

                uploader.bind('FileUploaded', function(up, file, res) {
                    file.url = scope.files[file.id];
                    // 把域名之前的去除之后存储
                    file.path = file.url.replace(/http:\/\/[^/]+\.com/, '');

                        // We are going to make some refactor here.
                                //The idea behind is always update files with the server response value
                                //And also launch the eventi if neeed

                                //If we have the model...
                                if(iAttrs.plFilesModel) {
                                    //Apply on scope...
                                    scope.$apply(function() {

                                        //All files are uploaded?
                                        scope.allUploaded = false;

                                        angular.forEach(scope.plFilesModel, function($file, key) {

                                            //Bug FIX, this logic will set allUploaded right
                                            if(file.percent != 100) {
                                                scope.allUploaded = false;
                                            } else if(file.id == $file.id) { //If the file is the same that we are reciving...
                                                //Set response on the file
                                                $file.response = JSON.parse(res.response);

                                                //Need throw event? throw it
                                                if(iAttrs.onFileUploaded) {
                                                    var fn = $parse(iAttrs.onFileUploaded);
                                                    fn(scope.$parent, {$up: up, $response:res, $file:file});
                                                }
                                            }

                                        });
                                    });
                                }
                                //We doesn't have model but we have the event
                                else if(!iAttrs.plFilesModel && iAttrs.onFileUploaded) {
                                    var fn = $parse(iAttrs.onFileUploaded);
                                    scope.$apply(function(){
                                        fn(scope.$parent, {$response:res, $up: up, $file:file});
                                    });
                                }
                });

                uploader.bind('UploadProgress',function(up,file){
                    if(!iAttrs.plProgressModel){
                        return;
                    }

                    if(iAttrs.plFilesModel){
                        scope.$apply(function() {
                            scope.sum = 0;

                            angular.forEach(scope.plFilesModel, function(file,key) {
                                scope.sum = scope.sum + file.percent;
                            });

                            scope.plProgressModel = scope.sum/scope.plFilesModel.length;
                        });
                    } else {
                        scope.$apply(function() {
                            scope.plProgressModel = file.percent;
                        });
                    }


                    if(iAttrs.onFileProgress){
                        var fn = $parse(iAttrs.onFileProgress);
                        scope.$apply(function(){
                            fn(scope.$parent, {$file:file});
                        });
                    }
                });

                if(iAttrs.plInstance){
                    scope.plInstance = uploader;
                }

                scope.$on("$destroy", function(){
                                uploader.destroy();
                        });

            }
        };
    }])
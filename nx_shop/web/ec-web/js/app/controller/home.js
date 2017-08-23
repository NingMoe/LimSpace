define([
  'underscore',
  'zepto',
  'layer',
    'md5',
  '../services',
  'ui',
  'config',
  'weixinjs',
  'text!../templates/home/index.html',
  'text!../templates/home/connects.html',
  'text!../templates/home/sign.html',
  'text!../templates/home/findpassword.html',
  'text!../templates/home/setpassword.html',
  'text!../templates/home/survey.html',
  'text!../templates/home/problem.html',
  'text!../templates/home/modify.html',
  'text!../templates/home/register.html',
  'text!../templates/home/Agreement.html',
  'text!../templates/home/MyBill.html',
  'text!../templates/home/BillingBetails.html',
  'text!../templates/home/BillSnapshot.html',
  'text!../templates/home/paymodify.html',
  'text!../templates/home/share.html',
  'text!../templates/home/sharefriend.html'
], function(_, $, layer, md5, services, ui, config, wx, indexTpl, connectsTpl, SignTpl,
  findpasswordTpl, setpasswordTpl, surveyTpl, problemTpl, modifyTpl, registerTpl, AgreementTpl, MyBillTpl,
  BillingBetailsTpl, BillSnapshotTpl, paymodifyTpl, shareTpl, sharefriendTpl) {
  function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = 'expires='+d.toUTCString();
    document.cookie = cname + '=' + cvalue + '; ' + expires;
  }
  function getCookie(name) {
    var arr,reg=new RegExp('(^| )'+name+'=([^;]*)(;|$)');
    if(arr=document.cookie.match(reg)){      return (arr[2]);
    } else {
      return null;
    }
  }
  function weixinpay(data) {
    services.http.postJson('weixin/unifiedorder',data).then(function(resp) {
      if(resp.wePayChatEntity) {
        resp = resp.wePayChatEntity;
        var params = {
           debug: true,
          'appId' : resp.appId,     //公众号名称，由商户传入
          'timeStamp' : ''+resp.timestamp,         //时间戳，自1970年以来的秒数
          'nonceStr' : resp.nocestr, //随机串
          'package' : resp.strpackage,
          'signType' : 'MD5',         //微信签名方式：
          'paySign' : resp.sign //微信签名
        };
        function onBridgeReady() {
          WeixinJSBridge.invoke(
            'getBrandWCPayRequest', params,
            function(res){
              if( res.err_msg == 'get_brand_wcpay_request:ok' ) {
                location.href='/?#order/type/0';
              }
              if( res.err_msg == 'get_brand_wcpay_request:cancel' ) {
                alert('您取消了支付！');
                location.href='/?#order/type/0';
              }
              if( res.err_msg == 'get_brand_wcpay_request:fail' ) {
                alert('支付失败,请稍后再试！');
                location.href='/?#order/type/0';
              }
            }
          );
        }
        if (typeof WeixinJSBridge == 'undefined') {
          if( document.addEventListener ) {
            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
          }else if (document.attachEvent) {
            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
          }
        } else {
          onBridgeReady();
        }
      } else {
        ui.makeToast(resp.message);
        location.reload();
      }
    });
  }
  var headlinetime;
  var homeController = {
    /* 首页*/
    homePage: function() {
      services.setTitle('商城首页');
      var compiled = _.template(indexTpl);
      var code='home';
      var level=1;
      var limit=4;
      var group = 'home';
      var stageRateData = {};
      localStorage.removeItem('userURl');
      localStorage.removeItem('SignURl');
      //进入首页获取位置信息
      // var thisULR = window.location.href;
      // services.http.get('/weixin/wxConfig?url='+thisULR).then(function(resp) {
      //   if(resp.code == 200) {
      //     resp = resp.data;
      //     var nonceStr = resp.nonceStr;
      //     var signature = resp.signature;
      //     var timestamp = resp.timestamp;
      //     var appId = resp.appId;
      //     wx.config({
      //       debug: false,
      //       appId: appId,
      //       timestamp: timestamp,
      //       nonceStr: nonceStr,
      //       signature: '0b4fbebfb368b7b0a31708748973d911dbf6fc8d',
      //       jsApiList: ['chooseImage','uploadImage','downloadImage','getLocation']
      //     });
      //     wx.getLocation({
      //       type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
      //       success: function (res) {
      //         var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
      //         var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
      //         var speed = res.speed; // 速度，以米/每秒计
      //         var accuracy = res.accuracy; // 位置精度
      //         alert('维度'+latitude+'经度'+longitude+'速度'+speed+'位置精度'+accuracy);
      //       }
      //     });
      //   }
      // });
      /*费率*/
      var stageRatePromise = services.http.get('stageRate','').then(function(resp) {
        $.each(resp,function(index,item) {
          stageRateData[index] = item ;
        });
      });
      services.http.get('tags/skus?code='+code+'&level='+level+'&limit='+limit).then(function(data) {
        services.http.get('adverts?group='+group+'&code='+'').then(function(resp) {
          if(resp.code == 400) {
            resp = '' ;
          }
          //头条
          services.http.get('/headline').then(function(head) {
            stageRatePromise.then(function() {
              var configsys ='http://tyiti.img-cn-beijing.aliyuncs.com';
              var rendered = compiled({
                banner: resp,
                allList: data,
                headline: head,
                config:configsys,
                stageRateData:stageRateData,
                configer:config
              });
              var $body = $('.viewport .body');
              clearInterval(headlinetime);
              $body.off();
              $body.html(rendered);
              //活动版块
              if(resp.data.activity) {
                $('.actived').show();
                $('.actived a').css({'display':'block','float':'left'});
                $('.actived a img').css({'width':'100%','float':'left'});
                if(resp.data.activity.length==1) {
                  $('.actived').css({'width':'100%','margin-bottom':'10px'});
                  $('.actived a').css({'width':'100%','height':'auto'});
                }else if(resp.data.activity.length==2) {
                  $('.actived').css({'width':'100%'});
                  $('.actived a').css({'width':'50%','height':'auto'});
                }else if(resp.data.activity.length==3) {
                  $('.actived').css({'width':'100%'});
                  $('.actived a').css({'width':'50%'});
                }else if(resp.data.activity.length==4) {
                  $('.actived').css({'width':'100%'});
                  $('.actived a').css({'width':'50%'});
                }else if(resp.data.activity.length==5) {
                  $('.actived').css({'width':'100%'});
                  $('.actived a').css({'width':'30%'});
                  $('.actived a:nth-child(1) ').css({'width':'40%','height':'auto'});
                }else if(resp.data.activity.length==6) {
                  $('.actived').css({'width':'100%'});
                  $('.actived a:nth-child(1) ,.actived a:nth-child(2) ').css({'width':'50%'});
                  $('.actived a:nth-child(3) ,.actived a:nth-child(4) ,.actived a:nth-child(5) ,.actived a:nth-child(6)').css({'width':'25%'});
                }
              } else {
                $('.actived').hide();
              }
              var hei=$('.headline .con').height();
              var len =$('.con').children('a').length;
              var i =1;
              if(len==0) {
                $('.headline').hide();
              }else if(len ==1) {
                clearInterval(headlinetime);
              }
              headlinetime =  setInterval(function() {
                $('.headline .con a').not($('.headline .con a').eq(i-1)).attr('style','top:100%');
                if(i == len) {
                  $('.headline .con a').eq(i-1).animate({top:'-100%'});
                  $('.headline .con a').eq(0).animate({top:'0%'});
                  i=0;
                } else {
                  $('.headline .con a').eq(i).prev().animate({top:'-100%'});
                  $('.headline .con a').eq(i).animate({top:'0%'});
                }
                i++;
              },3000);
              $('.m-scooch').scooch({
                infinite: true,
                autoplay: {
                  interval: 4000,
                  cancelOnInteraction: true
                }
              });
              if (config.sys === 'tyfq') {
                $('.pj-action-add > a.credit').show();
                $('.pj-action-add > a').css({'width':'25%'});
              }else if (config.sys === 'thb') {
                $('.home_box  .states').addClass('hidden');
                $('.home_box  .statesthb').removeClass('hidden');
              }else if (config.sys === 'ceb') {
                $('.home_box  .states').addClass('hidden');
                $('.home_box  .ceb-states').removeClass('hidden');
                $('.home_box  .statesceb').removeClass('hidden');
              }
            });
          });
        });
      });
    },
    /* 绑定*/
    connects:function(fwd,calback) {
      services.setTitle('绑定账号');
      var compiled = _.template(connectsTpl);
      var rendered = compiled();
      var $body = $('.viewport .body');
      $body.off();
      $body.html(rendered);
    	if(localStorage.getItem('productUrl')) {
     		localStorage.setItem('SignUrl',location.href);
    	}
      // TODO 为什么要是白色  因为绑定页设计稿背景白色
      $('body').css({'background':'#fff'});
      if (fwd) {
        if(fwd.substr(fwd.indexOf('#'))=='#lottery/prizelist') {
          localStorage.setItem('newfwd',fwd);
        }
      }
      var events = {
        obtain: function() {
          var regut = /^[1][0-9]{10}$/;
          var mobile = $('.binding .mobile-phone').val().replace(/\s+/g, '');
          if(!mobile || !regut.test(mobile)) {
            ui.makeToast('请输入正确的手机号');
            $('.binding .mobile-phone').addClass('active');
            return;
          }
          services.http.get('users/verify_code', {
            type: 'reg',
            mobile: mobile
          }).then(function(resp) {});
          var ppt=$(this);
          var text_time=60;
          $(this).addClass('state');
          $(this).attr({'disabled':'disabled'});
          var clearset = setInterval(function() {
            ppt.text(text_time+'秒后重发');
            text_time--;
            if(text_time <= 0) {
              clearTimeout(clearset);
              ppt.text('获取验证码');
              ppt.removeClass('state');
              ppt.removeAttr('disabled');
            }
          }, 1000);
        },
        focusclick:function() {
          $(this).removeClass('active');
        },
        submit:function() {
          var regut = /^[1][0-9]{10}$/;
          var mobile = $('.binding .mobile-phone').val().replace(/\s+/g, '');
          var verifyCode = $('.binding .verification-code').val().replace(/\s+/g, '');
          if(!mobile || !regut.test(mobile)) {
            alert('请输入正确的11位手机号');
            $('.binding .mobile-phone').addClass('active');
            return;
          }
          if(!verifyCode || !verifyCode.match(/^[0-9]{6}$/)) {
            alert('请输入正确的验证码');
            $('.binding .verification-code').addClass('active');
            return;
          }
          var data = {
            mobile:mobile,
            verifyCode:verifyCode
          };
          services.http.postJson('users/connections', data).then(function(resp) {
            if(resp.code ==200) {
              if(localStorage.getItem('productUrl') && localStorage.getItem('SignUrl')) {
                fwd = '?'+localStorage.getItem('productUrl').split('?')[1];
                localStorage.removeItem('SignUrl');
                localStorage.removeItem('productUrl');
              }
              fwd = fwd || '/?#home';
              history.replaceState({}, '', fwd);
              if(calback){calback();}
              $(window).trigger('route');
            } else {
              ui.makeToast('绑定失败：' + resp.message);
            }
          });
        }
      };
      $body.on('click', '.binding .obtain', events.obtain);
      $body.on('focus', '.binding input', events.focusclick);
      $body.on('click', '.binding .submit', events.submit);
    },
    /* 登录*/
    Sign:function(fwd,calback) {
      services.setTitle('登录');
      localStorage.setItem('SignURl',location.hash);
      services.http.get('users').then(function(resp) {
        fwd = fwd || '/?#home';
        history.replaceState({}, '', fwd);
        if(calback){calback();}
        $(window).trigger('route');
      }).fail(function (jqXHR, textStatus, errorThrown) {
        var compiled = _.template(SignTpl);
        var rendered = compiled(config);
        var $body = $('.viewport .body');
        $body.off();
        $body.html(rendered);
        if(localStorage.getItem('productUrl')) {
          localStorage.setItem('SignUrl',location.href);
        }
        if (config.sys === 'tyfq') {
          $('body').css({'background':'#fff'});
        }
        var events = {
          submit:function() {
            if (!ui.validate($('.sign form'))) {
              return false;
            }
            var data = {
              mobile: $('[name="mobile"]').val(),
              password: md5($('[name="password"]').val())
            };
            services.http.postJson('users/signup', data).then(function(resp) {
              localStorage.removeItem('userURl');
              localStorage.removeItem('SignURl');
              if (resp.code == 200) {
                localStorage.removeItem('progressflag');
                if(localStorage.getItem('productUrl') && localStorage.getItem('SignUrl')) {
                  fwd = '?'+localStorage.getItem('productUrl').split('?')[1];
                  console.log(fwd);
                  localStorage.removeItem('SignUrl');
                  localStorage.removeItem('productUrl');
                }
                fwd = fwd || '/?#home';
                history.replaceState({}, '', fwd);
                if(calback) {calback();}
                $(window).trigger('route');
              } else {
                ui.makeToast('登录失败：' + resp.message);
              }
            }).fail(function(jqXHR, textStatus, errorThrown) {
              ui.makeToast('登录异常，响应码为：' + jqXHR.status + '，原因：' + errorThrown);
            });
            return false;
          }
        };
        $('.viewport .sign').css({'height':'100%'});
        $('.viewport .body').css({'height':'100%'});
        $('.viewport >div').css({'height':'100%'});
        $body.on('click', '.sign .submint', events.submit);
        if (config.sys === 'tyfq') {
          $('.tyfq .sign').removeClass('signbg');
          $('.tyfq .sign').css({'background':'#fff !important'});
          $('.tyfq .sign .logoFxb').removeClass('hidden');
          $('.tyfq .sign .logo').addClass('hidden');
        }
      });
    },
    /* 找回密码*/
    findpass:function() {
      services.setTitle('找回密码');
      var compiled = _.template(findpasswordTpl);
      var $body = $('.viewport .body');
      var rendered = compiled();
      $body.off();
      $body.html(rendered);
      if (config.sys === 'tyfq') {
       $('.tyfq .sign').removeClass('signbg');
       $('.tyfq .sign').css({'background':'#f4f8fb!important'});
       $('.tyfq .logo').hide();
      }
      var step = 1;
      var events = {
        submit: function() {
          if (!ui.validate($('.sign form .section:not(.hidden)'))) {
            return false;
          }
          var mobile = $('[name="mobile"]').val();
          var verifyCode = $('[name="checkcode"]').val();
          var data = {
            mobile: mobile,
            verifyCode: verifyCode
          };
          if (!$('.sign form .auth').hasClass('hidden')) {
            services.http.postJson('users/auths', data).then(function(data) {
              if (data.code != 200) {
                ui.makeToast(data.message);
              } else {
                location.href='/#setpass='+mobile+'&'+verifyCode;
              }
            });
          }
          return false;
        },
        obtain:function() {
          var regut = /^[1][0-9]{10}$/;
          var mobile = $('.find [name="mobile"]').val().replace(/\s+/g, '');
          if(!mobile || !regut.test(mobile)) {
            alert('请输入正确的手机号');
            return false;
          }
          $(this).css({'background':'#ccc'});
          services.http.get('users/verify_code', {
            type: 'resetPassword',
            mobile: mobile
          }).then(function(resp) {
            console.log(resp.verifyCode);
          });
          var ppt=$(this);
          var text_time=60;
          $(this).addClass('state');
          $(this).attr({'disabled':'disabled'});
          var clearset = setInterval(function() {
            ppt.text(text_time+'秒后重发');
            text_time--;
            if(text_time <= 0) {
              clearTimeout(clearset);
              ppt.text('获取验证码');
              ppt.removeClass('state');
              ppt.removeAttr('disabled');
            }
          }, 1000);
        }
      };
      $('.viewport .sign').css({'height':'100%'});
      $('.viewport .body').css({'height':'100%'});
      $('.viewport >div').css({'height':'100%'});
      $body.on('click', '.sign .submint', events.submit);
      $body.on('click', '.find .obtain', events.obtain);
    },
    /* 找回密码Two */
    setpass:function() {
      services.setTitle('找回密码');
      var compiled = _.template(setpasswordTpl);
      var $body = $('.viewport .body');
      var rendered = compiled();
      $body.off();
      $body.html(rendered);
      if (config.sys === 'tyfq') {
        $('.tyfq .sign').removeClass('signbg');
        $('.tyfq .sign').css({'background':'#f4f8fb!important'});
        $('.tyfq .logo').hide();
      }
      var mobile  = location.hash.split('=')[1].split('&')[0];
      var verifyCode =  location.hash.split('=')[1].split('&')[1];
      if(!mobile || !verifyCode) {
        ui.makeToast('数据异常请稍后再试!');
      }
      var events = {
        submit:function() {
          var newPassword = $('[name="newPassword"]').val();
          var newPasswordRepeat = $('[name="newPasswordRepeat"]').val();
          localStorage.setItem('lasthash',''); //清空在登陆界面做的限制 解决不能在登陆界面返回问题
          if(newPassword != newPasswordRepeat) {
            ui.makeToast('两次密码不一致');
            return false;
          }
          newPassword =md5(newPassword);
          var data = {
            mobile:mobile,
            verifyCode:verifyCode,
            newPassword:newPassword
          };
          services.http.putJson('users/passwords', data).then(function(data) {
            if (data.code != 200) {
              ui.makeToast(data.message);
            } else {
              ui.makeToast('重置密码成功');
              setTimeout(function() {
                location.href = '#user';
              }, 2000);
            }
          });
          return false;
        }
      };
      $body.on('click', '.sign .submint', events.submit);
    },
    /* 意见反馈*/
    survey:function() {
      services.setTitle('意见反馈');
      var compiled = _.template(surveyTpl);
      var $body = $('.viewport .body');
      var rendered = compiled();
      $body.off();
      $body.html(rendered);
      var events = {
        button:function() {
          var content = $('textarea').val().replace(/\s+/g, '');
          if(!content) {
            alert('请输入意见反馈！');
            return;
          }
          services.http.postJson('leaveMessage',{content:content}).then(function(resp) {
            alert(resp.message);
            location.href='#user';
          });
        }
      };
      $body.on('click', '.survey button', events.button);
    },
    /*分享有礼*/
    share:function() {
      services.setTitle('分享有礼');
      var thisULR = location.href;
      services.http.get('/weixin/wxConfig?url='+thisULR).then(function(resp) {
        if(resp.code == 200) {
          services.http.get('users/share').then(function(resp1) {
            if(resp1.code == 200) {
              resp = resp.data;
              var nonceStr = resp.nonceStr;
              var signature = resp.signature;
              var timestamp = resp.timestamp;
              var appId = resp.appId;
              wx.config({
                debug: false,
                appId: appId,
                timestamp: timestamp,
                nonceStr: nonceStr,
                signature: signature,
                jsApiList: ['checkJsApi','onMenuShareTimeline','onMenuShareAppMessage']
              });
              wx.ready(function() {
                wx.onMenuShareAppMessage({
                  title: '分享有礼',
                  desc: '我在#e惠聚购#看到了邀请好友注册成功，最高得150元优惠券，简直太棒了！',
                  link: location.origin+'/#sharefriend/'+ resp1.shareCode ,
                  imgUrl: location.origin+'/img/sharelink.png',
                  trigger: function (res) {},
                  success: function (res) {},
                  cancel: function (res) {
                    ui.makeToast('已取消');
                  },
                  fail: function (res) {
                    ui.makeToast(JSON.stringify(res));
                  }
                });
                wx.onMenuShareTimeline({
                  title: '我在#e惠聚购#看到了邀请好友注册成功，最高得150元优惠券，简直太棒了！',
                  link: location.origin+'/#sharefriend/'+ resp1.shareCode,
                  imgUrl: location.origin+'/img/sharelink.png',
                  trigger: function (res) {},
                  success: function (res) {
                    ui.makeToast('已分享');
                  },
                  cancel: function (res) {
                    ui.makeToast('已取消');
                  },
                  fail: function (res) {
                    ui.makeToast(JSON.stringify(res));
                  }
                });
              });
              var compiled = _.template(shareTpl);
              var $body = $('.viewport .body');
              var rendered = compiled();
              $body.off();
              $body.html(rendered);
              $body.on('click','.share .shareto',events.guide);
              $body.on('click','.share .know',events.know);
            } else {
              alert(resp1.message);
            }
          });
        } else {
          alert(resp.message);
        }
      });
      var events = {
        guide : function() {
          $('.share .guide').show();
          $('.share .guidein').show();
        },
        know : function() {
          $('.share .guide').hide();
          $('.share .guidein').hide();
        }
      };
    },
    /*分享后注册*/
    sharefriend:function(userid) {
      services.setTitle('分享有礼');
      var code = location.href;
      code = code.substr(code.lastIndexOf('/')+1);
      services.http.get('/users/shareUser',{code:code}).then(function(resp) {
        var compiled = _.template(sharefriendTpl);
        var $body = $('.viewport .body');
        var rendered = compiled();
        $body.off();
        $body.html(rendered);
        $('.viewport >div').css({'height':'100%'});
        $('.body').css({'height':'100%'});
        $('.sharefriend .msg p i').html(resp.data);
        if(getCookie('registermobile')) {
          $('[name="mobile"]').val(getCookie('registermobile'));
        }
        if(getCookie('registercheckcode')) {
          $('[name="checkcode"]').val(getCookie('registercheckcode'));
        }
        if(getCookie('registerpassword')) {
          $('[name="password"]').val(getCookie('registerpassword'));
        }
        var events = {
          submit:function() {
            console.log(userid);
            localStorage.setItem('lasthash', ''); //清空在登陆界面做的限制 解决不能在登陆界面返回问题
            var data = {
              mobile: $('[name="mobile"]').val().replace(/\s+/g, ''),
              verifyCode: $('[name="checkcode"]').val().replace(/\s+/g, ''),
              password: md5($('[name="password"]').val()).replace(/\s+/g, ''),
              shareCode: userid
            };
            services.http.postJson('users', data).then(function (res) {
              localStorage.removeItem('progressflag');
              rendered = compiled(res);
              if (res.code == 200) {
                $('.sharefriend .draw').show();
                $('.draw .content .win').show();
                $('.draw .content .lose').hide();
                $('.draw .content .win i').html(res.data.couponName);
              } else {
                if(res.message=='手机号已被注册，无法参加此活动') {
                  $('.sharefriend .draw').show();
                  $('.draw .content .lose').show();
                  $('.draw .content .win').hide();
                } else {
                  ui.makeToast(res.message);
                }
              }
            }).fail(function (jqXHR, textStatus, errorThrown) {
              ui.makeToast('注册异常，响应码为：' + jqXHR.status + '，原因：' + errorThrown);
            });
            return false;
          },
          obtain:function() {
            services.http.get('users/verify_code', {
              type: 'reg',
              mobile: $('[name="mobile"]').val()
            }).then(function(resp) {
              $('').show();
            }).fail(function(jqXHR, textStatus, errorThrown) {
              ui.makeToast('获取短信验证码异常，响应码为：' + jqXHR.status + '，原因：' + errorThrown);
            });
            var ppt=$(this);
            var text_time=60;
            $(this).addClass('state');
            $(this).attr({'disabled':'disabled'});
            var clearset = setInterval(function() {
              ppt.text(text_time+'s');
              text_time--;
              if(text_time <= 0) {
                clearTimeout(clearset);
                ppt.text('获取');
                ppt.removeClass('state');
                ppt.removeAttr('disabled');
              }
            }, 1000);
            return false;
          },
          registermessage:function() {
            setCookie('registermobile', $('[name="mobile"]').val(),1);
            setCookie('registercheckcode', $('[name="checkcode"]').val(),1);
            setCookie('registerpassword', $('[name="password"]').val(),1);
          },
          sure:function() {
            $('.sharefriend .draw').hide();
          },
          goshop:function() {
            location.href='#home';
          }
        };
        $body.on('click', '.sharefriend .submint', events.submit);
        $body.on('click', '.sharefriend .obtain', events.obtain);
        $body.on('click','.sharefriend .content .sure', events.sure);
        $body.on('click','.sharefriend .content .goshop', events.goshop);
        $body.on('click','.sharefriend .draw .close', events.sure);
      });
    },
    /* 修改密码*/
    modify:function() {
      services.setTitle('修改密码');
      var compiled = _.template(modifyTpl);
      var $body = $('.viewport .body');
      var rendered = compiled();
      $body.off();
      $body.html(rendered);
      var events = {
        submit: function() {
          if (!ui.validate($('.sign form'))) {
            return false;
          }
          var password = $('[name="password"]').val();
          var newPassword = $('[name="newPassword"]').val();
          var newPasswordRepeat = $('[name="newPasswordRepeat"]').val();
          if (newPassword != newPasswordRepeat) {
            ui.makeToast('两次密码输入不一致');
            return false;
          }
          var data = {
            password: md5(password),
            newPassword: md5(newPassword)
          };
          services.http.putJson('users/passwords', data).then(function(data) {
            if (data.code != 200) {
              ui.makeToast(data.message);
            } else {
              ui.makeToast('修改密码成功');
              setTimeout(function() {
                location.href = '#user';
              }, 3000);
            }
          });
          return false;
        }
      };
      $('.viewport .sign').css({'height':'100%'});
      $('.viewport .body').css({'height':'100%'});
      $('.viewport >div').css({'height':'100%'});
      $body.on('click', '.sign .submint', events.submit);
      if (config.sys === 'tyfq') {
        $('.tyfq .sign').removeClass('signbg');
        $('.tyfq .sign').css({'background':'#f4f8fb!important'});
        $('.tyfq .logo').hide();
      }
    },
    /* 修改支付密码*/
    paymodify:function() {
      services.setTitle('修改支付密码');
      var compiled = _.template(paymodifyTpl);
      var $body = $('.viewport .body');
      if (config.sys === 'tyfq') {
        $('.tyfq .sign').css({'background':'#f4f8fb!important'});
        $('.tyfq .logo').hide();
      }
      services.http.get('users').then(function(resp) {
        if (resp.data.creditState == 2) {
          var rendered = compiled();
          $body.off();
          $body.html(rendered);
          if (config.sys === 'tyfq') {
            $('.tyfq .sign').css({'background':'#f4f8fb!important'});
            $('.tyfq .logo').hide();
          }
          $('[name="mobile"]').val(resp.data.mobile);
          var events = {
            codebut:function() { //短信
              var ppt=$(this);
              var text_time=60;
              $(this).addClass('state');
              $(this).attr({'disabled':'disabled'});
              var clearset = setInterval(function() {
                ppt.text(text_time+'秒后重发');
                text_time--;
                if(text_time <= 0) {
                  clearTimeout(clearset);
                  ppt.text('获取验证码');
                  ppt.removeClass('state');
                  ppt.removeAttr('disabled');
                }
              }, 1000);
              services.http.get('users/verify_code', {
                type: 'resetPayPassword',
                mobile: $('[name="mobile"]').val()
              }).then(function(resp) {
                console.log(resp.verifyCode);
              });
            },
            submit:function() { //第一步数据
              if (!ui.validate($('.sign .information'))) {
                return false;
              }
              var data = {
                mobile:$('[name="mobile"]').val(),
                verifyCode:$('[name="verifyCode"]').val(),
                idCard:$('[name="idCard"]').val()
              };
              services.http.postJson('/users/pay_auths',data).then(function(resp) {
                if(resp.code ==200) {
                  $('.payone').addClass('hidden');
                  $('.paytwo').removeClass('hidden');
                  $('.paytwo').css('paddingTop','50px');
                  var btn = document.getElementById('payPassword_rsainput');
                  btn.focus();
                } else {
                  alert(resp.message);
                }
              });
            },
            inputOnFocus:function() {
              document.getElementById('payPassword_container').getElementsByTagName('div')[0].className = 'sixDigitPassword active';
            },
            inputOnBlur:function() {
              document.getElementById('payPassword_container').getElementsByTagName('div')[0].className = 'sixDigitPassword';
            },
            onInputChange:function() {
              var length=$('.alieditContainer input').val().length;
              for (var i = 0; i < 6; i++) {
                if (i < length) {
                  document.getElementById('payPassword_container').getElementsByTagName('b')[i].style.visibility = 'visible';
                } else {
                  document.getElementById('payPassword_container').getElementsByTagName('b')[i].style.visibility = 'hidden';
                }
              }
              if(length ==6) {
                var  payword =$('.alieditContainer input').val();
                var  payfor =md5(payword);
                console.log(payword+';;'+payfor);
                //设置支付密码
                var data = {
                  mobile:$('[name="mobile"]').val(),
                  verifyCode:$('[name="verifyCode"]').val(),
                  idCard:$('[name="idCard"]').val(),
                  payPassword:payfor
                };
                services.http.putJson('/users/pay_passwords',data).then(function(resp) {
                  if(resp.code ==200) {
                    alert('修改支付密码成功');
                    location.href = '#user';
                  } else {
                    alert(resp.message);
                  }
                });
              }
            }
          };
          $body.on('focus', '.alieditContainer input', events.inputOnFocus);
          $body.on('blur', '.alieditContainer input', events.inputOnBlur);
          $body.on('keyup', '.alieditContainer input', events.onInputChange);
          $body.on('click', '.sixDigitPassword', events.zBoxfocus);
          $body.on('click', '.submint ', events.submit);
          $body.on('click', '.obtain ', events.codebut);
        } else {
          alert('您还未授信成功，暂时不能修改支付密码');
          location.href='#user';
        }
      });
    },
    /* 常见问题*/
    problem:function() {
      services.setTitle('常见问题');
      var compiled = _.template(problemTpl);
      var $body = $('.viewport .body');
      var fqa = services.http.get('fqa');
      fqa.then(function(data) {
        console.log(data);
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
        if (config.sys === 'tyfq') {
          $('.tyfq .sign').css({'background':'#f4f8fb!important'});
          $('.tyfq .logo').hide();
        }
      });
    },
    /* 注册*/
    register:function() {
      services.setTitle('注册');
      services.http.get('users').then(function(resp) {
        location.href = '#user';
      }).fail(function (jqXHR, textStatus, errorThrown) {
        var compiled = _.template(registerTpl);
        var $body = $('.viewport .body');
        var rendered = compiled();
        $body.off();
        $body.html(rendered);
        if(getCookie('registermobile')) {
          $('[name="mobile"]').val(getCookie('registermobile'));
        }
        if(getCookie('registercheckcode')) {
          $('[name="checkcode"]').val(getCookie('registercheckcode'));
        }
        if(getCookie('registerpassword')) {
          $('[name="password"]').val(getCookie('registerpassword'));
        }
        var events = {
          submit:function() {
            $('.sign .submint').attr('disabled','disabled');
            if (!ui.validate($('.sign form'))) {
              return false;
            }
            if (!$('.register .checkboxFive .checkboxFiveInput:checked').val()) {
              ui.makeToast('您需要勾选注册协议方可继续进行注册');
              return false;
            }
            localStorage.setItem('lasthash', ''); //清空在登陆界面做的限制 解决不能在登陆界面返回问题
            var data = {
              mobile: $('[name="mobile"]').val().replace(/\s+/g, ''),
              verifyCode: $('[name="checkcode"]').val().replace(/\s+/g, ''),
              password: md5($('[name="password"]').val()).replace(/\s+/g, '')
            };
            services.http.postJson('users', data).then(function (data) {
              setCookie('registermobile','',1);
              setCookie('registercheckcode','',1);
              setCookie('registerpassword','',1);
              localStorage.removeItem('progressflag');
              if (data.code != 200) {
                $('.sign .submint').attr('disabled','');
                ui.makeToast(data.message);
              } else {
                $('.sign .submint').attr('disabled','');
                location.href = '#user';
              }
            }).fail(function (jqXHR, textStatus, errorThrown) {
              ui.makeToast('注册异常，响应码为：' + jqXHR.status + '，原因：' + errorThrown);
            });
            return false;
          },
          obtain:function(){
            if (!ui.validate($('.sign form .account'))) {
              return false;
            }
            services.http.get('users/verify_code', {
              type: 'reg',
              mobile: $('[name="mobile"]').val()
            }).then(function(resp) {
              console.log(resp.verifyCode);
            }).fail(function(jqXHR, textStatus, errorThrown) {
              ui.makeToast('获取短信验证码异常，响应码为：' + jqXHR.status + '，原因：' + errorThrown);
            });
            var ppt=$(this);
            var text_time=60;
            $(this).addClass('state');
            $(this).attr({'disabled':'disabled'});
            var clearset = setInterval(function() {
              ppt.text(text_time+'秒后重发');
              text_time--;
              if(text_time <= 0) {
                clearTimeout(clearset);
                ppt.text('获取验证码');
                ppt.removeClass('state');
                ppt.removeAttr('disabled');
              }
            }, 1000);
            return false;
          },
          registermessage:function() {
            setCookie('registermobile', $('[name="mobile"]').val(),1);
            setCookie('registercheckcode', $('[name="checkcode"]').val(),1);
            setCookie('registerpassword', $('[name="password"]').val(),1);
          }
        };
        $('.viewport .sign').css({'height':'100%'});
        $('.viewport .body').css({'height':'100%'});
        $('.viewport >div').css({'height':'100%'});
        $body.on('click', '.sign .submint', events.submit);
        $body.on('click', '.register .obtain', events.obtain);
        $body.on('click', '.register .registermessage', events.registermessage);
        if (config.sys === 'tyfq') {
          $('.tyfq .sign').removeClass('signbg');
          $('.tyfq .sign').css({'background':'#fff!important'});
          $('body').css({'background':'#fff'});
          $('.tyfq .information form a ').text('我已阅读并接受分信宝服务条款');
          $('.hidden').removeClass('hidden');
          $('.tyfq .sign .logoFxb').removeClass('hidden');
          $('.tyfq .sign .logo').addClass('hidden');
        }
      });
    },
    /* 注册协议*/
    Agreement:function() {
      services.setTitle('注册协议');
      var compiled = _.template(AgreementTpl);
      var $body = $('.viewport .body');
      var rendered = compiled();
      $body.off();
      $body.html(rendered);
      if (config.sys === 'tyfq') {
        $('.tyfqservice').show();
        $('.thbservice').hide();
      }else if (config.sys === 'thb') {
        $('.tyfqservice').hide();
        $('.thbservice').show();
      }
    },
    /* 我的账单*/
    MyBill:function() {
      services.setTitle('我的账单');
      var repeatedly  = true;
      var compiled = _.template(MyBillTpl);
      var $body = $('.viewport .body');
      var thisid ='';
      var billprice = 0;
      // page 页码
      //pageSize 条数 默认查询10万条
      // flag null:全部账单 A1:全部账单 A2:近7日待还账单
      var bills =services.http.get('/bill?page=1&pageSize=100000&flag=');
      var events = {
        footercheckboxFiveInput:function() {
          thisid ='';
          billprice = 0;
          $('ul').each(function(index,item) {
            if($(item).find('.checkboxFiveInput').is(':checked')) {
              thisid = thisid +','+ ($(item).find('.checkboxFiveInput').data('id'));
              billprice = (parseFloat(billprice) + parseFloat($(item).find('.checkboxFiveInput').data('userlimit')) ).toFixed(2);
              $('.footer .Price span').text(billprice);
            } else {
              $('.footer .Price span').text('0');
            }
          });
        },
        parent_bodycheckboxFiveInput:function() {
          thisid ='';
          billprice = 0;
          var $this =$(this);
          $('ul').each(function(index,item) {
            $('.footer .Price span').text('0');
            if($(item).find('.checkboxFiveInput').is(':checked')) {
              thisid = thisid +','+ ($(item).find('.checkboxFiveInput').data('id'));
              billprice = (parseFloat(billprice) + parseFloat($(item).find('.checkboxFiveInput').data('userlimit')) ).toFixed(2);
            }
          });
          $('.footer .Price span').text(billprice);
        },
        footerbutton:function() {
          if(repeatedly) {
            repeatedly  = false;
          } else {
            return false;
          }
          setTimeout(function() {
            repeatedly  = true;
          },5000);
          if(thisid.substring(0,1) ==',') {
            thisid = thisid.substring(1);
          }
          console.log(thisid);
          console.log(billprice);
          var data = {
            orderId:thisid,
            type:1,
            grossAmount:billprice
          };
          weixinpay(data);
        }
      };
      bills.then(function(resp) {
        if(resp.code ==200) {
          var rendered = compiled(resp);
          $body.off();
          $body.html(rendered);
          $('.pj-action-add > a.credit').show();
          $('.pj-action-add > a').css({'width':'25%'});
          $('.footer input[type="checkbox"]').click(function() {
            if($('.footer input[type="checkbox"]').prop('checked')) {
              $('.liContent input[type="checkbox"]').prop('checked',true);
              $('.footer button').removeClass('disb');
              $('.footer button').removeAttr('disabled');
            } else {
              $('.liContent input[type="checkbox"]').prop('checked',false);
              $('.footer button').attr('disabled','disabled');
              $('.footer button').addClass('disb');
            }
          });
          $('.liContent input[type="checkbox"]').click(function() {
            var checkedindex=0;
            var disabledindex=0;
            if($(this).prop('checked')) {
              $('.footer button').removeClass('disb');
              $('.footer button').removeAttr('disabled');
              $.each($('.liContent input[type="checkbox"]'),function(index,item) {
                if($(item).prop('checked')) {
                  disabledindex = disabledindex + 1 ;
                }
              });
              if(disabledindex == $('.liContent input[type="checkbox"]').length) {
                $('.footer input[type="checkbox"]').prop('checked',true);
              }
            } else {
              $.each($('.liContent input[type="checkbox"]'),function(index,item) {
                if($(item).prop('checked')) {
                  checkedindex = checkedindex + 1 ;
                }
              });
              $('.footer input[type="checkbox"]').prop('checked',false);
              if(checkedindex === 0 ) {
                $('.footer button').attr('disabled','disabled');
                $('.footer button').addClass('disb');
              }
            }
          });
          $body.on('click', '.footer .checkboxFiveInput', events.footercheckboxFiveInput);
          $body.on('click', '.parent_body .checkboxFiveInput', events.parent_bodycheckboxFiveInput);
          $body.on('click', '.footer button', events.footerbutton);
        } else {
          alert(resp.messsge);
        }
      }).fail(function (jqXHR, textStatus, errorThrown) {
         if (jqXHR.status == 401) {
            if(localStorage.getItem('lasthash') == location.hash) {
                localStorage.setItem('lasthash','');
                location.href ='#home';
            } else {
              localStorage.setItem('lasthash',location.hash);
							services.http.get('/users/openId?state=sign?fwd=MyBill').then(function(resp) {
									if(resp.code == 302) {
										location.href = resp.url;
									}else if(resp.code == 200) {
										location.href = '#sign?fwd=/?#MyBill';
									}
								});
            }
        } else {
          ui.makeToast(errorThrown);
        }
      });
    },
    /* 账单详情*/
    BillingBetails:function(id) {
      services.setTitle('账单详情');
      var repeatedly  = true;
      var compiled = _.template(BillingBetailsTpl);
      var $body = $('.viewport .body');
      var thisid ='';
      var billprice = 0;
      var BillingBetails=services.http.get('/bill/'+id);
      var events = {
        footercheckboxFiveInput:function() {
          thisid ='';
          billprice = 0;
          $('ul li').each(function(index,item) {
            if($(item).find('.checkboxFiveInput').is(':checked')) {
              thisid = thisid +','+ ($(item).find('.checkboxFiveInput').data('id'));
              billprice = (parseFloat(billprice) + parseFloat($(item).find('.checkboxFiveInput').data('price')) ).toFixed(2);
            } else {
              $('.footer .Price span').text('0');
            }
          });
          $('.footer .Price span').text(billprice);
        },
        parent_bodycheckboxFiveInput:function() {
          thisid ='';
          billprice = 0;
          var $this =$(this);
          $('ul li').each(function(index,item) {
             if($(item).find('.checkboxFiveInput').is(':checked')) {
               thisid = thisid +','+ ($(item).find('.checkboxFiveInput').data('id'));
               billprice = (parseFloat(billprice) + parseFloat($(item).find('.checkboxFiveInput').data('price')) ).toFixed(2);
             } else {
              $('.footer .Price span').text('0');
            }
          });
          $('.footer .Price span').text(billprice);
        },
        footerbutton:function() {
          if(repeatedly ) {
             repeatedly  = false;
          } else {
            return false;
          }
          setTimeout(function() {
            repeatedly  = true;
          },5000);

          if(thisid.substring(0,1) ==',') {
            thisid = thisid.substring(1);
          }
          console.log(thisid);
          console.log(billprice);
          var data = {
            orderId:thisid,
            type:1,
            grossAmount:billprice
          };
          weixinpay(data);
        }
      };
      BillingBetails.then(function(resp) {
        _.each(resp.data.bills.aRefundBillList, function(item) {
          item.latestRepay=services.formatTime(item.latestRepay);
        });
        _.each(resp.data.bills.sRefundBillList, function(item) {
          item.latestRepay=services.formatTime(item.latestRepay);
        });
        console.log(resp);
        if(resp.code ==200) {
          var rendered = compiled(resp);
          $body.off();
          $body.html(rendered);
         $('.footer input[type="checkbox"]').click(function() {
            if($('.footer input[type="checkbox"]').prop('checked')) {
              $('.checkboxFive input[type="checkbox"]').prop('checked',true);
              $('.footer button').removeClass('disb');
              $('.footer button').removeAttr('disabled');
            } else {
              $('.checkboxFive input[type="checkbox"]').prop('checked',false);
              $('.footer button').attr('disabled','disabled');
              $('.footer button').addClass('disb');
            }
          });
          $('.checkboxFive input[type="checkbox"]').click(function() {
            var checkedindex=0;
            var disabledindex=0;
            if($(this).prop('checked')) {
              $('.footer button').removeClass('disb');
              $('.footer button').removeAttr('disabled');
              $.each($('.checkboxFive input[type="checkbox"]'),function(index,item) {
                if($(item).prop('checked')) {
                  disabledindex = disabledindex + 1 ;
                }
              });
              if(disabledindex + 1  == $('.checkboxFive input[type="checkbox"]').length) {
                $('.footer input[type="checkbox"]').prop('checked',true);
              }
            } else {
              $.each($('.checkboxFive input[type="checkbox"]'),function(index,item) {
                if($(item).prop('checked')) {
                  checkedindex = checkedindex + 1 ;
                }
              });
              $('.footer input[type="checkbox"]').prop('checked',false);
              if(checkedindex === 0 ) {
                $('.footer button').attr('disabled','disabled');
                $('.footer button').addClass('disb');
              }
            }
          });
          $body.on('click', '.footer .checkboxFiveInput', events.footercheckboxFiveInput);
          $body.on('click', '.parent_body .checkboxFiveInput', events.parent_bodycheckboxFiveInput);
          $body.on('click', '.footer button', events.footerbutton);
        } else {
          alert(resp.messsge);
        }
      });
    }
  };
  return homeController;
});
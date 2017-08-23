define([
  'underscore',
  'zepto',
  '../services',
  '../ui',
  'config',
  'weixinjs',
  'iscroll',
  'date',
  'text!../templates/user/user.html',
  'text!../templates/user/CorporateName.html',
  'text!../templates/user/creditcard.html',
  'text!../templates/user/JobInformation.html',
  'text!../templates/user/ContactPerson.html',
  'text!../templates/user/creditAddress.html',
  'text!../templates/user/selfmsg.html',
  'text!../templates/user/personmessage.html',
  'text!../templates/user/bankcard.html',
  'text!../templates/user/idcard.html',
  'text!../templates/user/creditprogress.html',
  'text!../templates/user/EmailVerification.html',
  'text!../templates/user/leaveMessage.html',
  'text!../templates/user/survey.html',
  'text!../templates/user/mymessage.html' , 
  'text!../templates/user/reload.html'
], function(_, $, services, ui, config, wx, iscroll, date, userTpl, CorporateNameTpl, creditcardTpl, JobInformationTpl,
 ContactPersonTpl,creditAddressTpl,selfmsgTpl,personmessageTpl,bankcardTpl,idcardTpl,creditprogressTpl,EmailVerificationTpl,leaveMessageTpl,surveyTpl,mymessageTpl,reloadTpl) {

  function getCookie(name) {
    var arr,reg=new RegExp('(^| )'+name+'=([^;]*)(;|$)');
    if(arr=document.cookie.match(reg)) {
      return (arr[2]);
    }else{
      return null;
    }
  }
  function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = 'expires='+d.toUTCString();
    document.cookie = cname + '=' + cvalue + '; ' + expires;
  }
  var emailSufflag = '';
  var userController = {
    /* 个人中心*/
    user:function() {
      services.http.get('users').then(function(data) {
        if (data.code == 200) {
            services.setTitle('个人中心');
            var $body = $('.viewport .body');
            var compiled = _.template(userTpl);
            var listOrder = services.orders.listOrder();
            var listreturn = services.orders.listreturn();
            var mobile = data.data.mobile;
            var userdata = data;
            var events = {
              signOut:function() {
                layer.open({
                    content: '确定要退出登录吗？',
                    btn: ['确认', '取消'],
                    shadeClose: false,
                    yes: function(){
                      layer.closeAll('loading');
                      services.http.post('users/signout').then(function(resp) {
                          if(resp.code == 200) {
                            location.href='#home';
                          } else {
                            ui.makeToast(resp.message);
                          }
                        });
                    }, no: function() {
                      layer.closeAll('loading');
                    }
                });
              },
              customerService:function() {
                if (config.sys == 'ceb') {
                  location.href='tel:400-6056-100';
                }else if (config.sys == 'thb') {
                  location.href='tel:400-660-3813';
                }else if (config.sys == 'tyfq') {
                  location.href='tel:400-668-6060';
                }
              },
              creditprogress:function() {
                var creditState=$(this).data('creditstate');
                var userType=$(this).data('usertype');
                var progressflag = localStorage.getItem('progressflag');
                if (creditState == 0) {
                  if (userType == 'A1') {
                    location.href='#idcard';
                  }else{
                    if (progressflag == 0 || progressflag == null) {
                      location.href='#idcard';
                    }else if (progressflag == 2) {
                      location.href='#JobInformation';
                    }else if (progressflag == 3) {
                      location.href='#EmailVerification';
                    }else if (progressflag == 4) {
                      location.href='#ContactPerson';
                    }else if (progressflag == 5) {
                      location.href='#creditcard';
                    }
                  }
                }else if (creditState == 1 || creditState == 2 || creditState == 3 || creditState == 4 || creditState == 5) {
                  location.href='#creditprogress';
                }
              }
            };
            function render (data) {
              var rendered = compiled(data);
              $body.off();
              $body.html(rendered);
              if (config.sys == 'ceb') {
	              if(data.count.data>0) {
			          	$('.user .count').css({'display':'block'});
			          }
	            }
              if (config.sys == 'tyfq') {
                $('body').css({'background':'#f4f8fb'});
                $('.pj-action-add > a.credit').show();
                $('.mobile').addClass('hidden');
                $('.quota').removeClass('hidden');
                $('.pj-action-add > a').css({'width':'25%'});
                $('.user .list .person').removeClass('hidden');
                $('.user .list .bill').removeClass('hidden');
                $('.user .list .paymodify').removeClass('hidden'); 
                $('.user .list .paymodify').addClass('borderbottom');
                $('.user .list .sign-out').addClass('borderbottom');
                $('.user .header .center-top').hide();
                $('.user .header>ul').hide();
                if (data.userdata.data.creditState == 0 || data.userdata.data.creditState == 1 || data.userdata.data.creditState == 3 || data.userdata.data.creditState == 4) {
                  $('.user .header').css({'height':'187px'});
                  $('.user .header .userFxb').removeClass('hidden');
                  $('.user .header .creditprogress').css({'background':''});
                }else if (data.userdata.data.creditState == 2 || data.userdata.data.creditState == 5 ) {
                  $('.user .header').css({'background':'url(../img/personcenter@2x.png) no-repeat #fff','background-size': '100% 168px','height':'189px','paddingBottom':'40px'});
                  $('.user .header .creditlimit').removeClass('hidden');
                }
                $('.user .header a.hidden').removeClass('hidden');
              } else if(config.sys == 'ceb') {
                $('.user .list .password').hide();
                $('.user .list .signOut').hide();
                $('.user .header').css('background','url(../img/bg@2x1.png)');
                $('.user .header div img').attr('src','./img/touxian@2x.png');
                $('.user .list .common-problem').css({'marginTop':'0'});
                $('.user .list .customer-service').addClass('borderbottom');
              }
	            if(data.userdata.data.nickName) {
                $('.user .mobile').text(data.userdata.data.nickName);
              }
              if(data.userdata.data.headImg) {
                $('.user .center-top img').attr('src',data.userdata.data.headImg);
              }
              if (config.sys == 'thb') {
                $('.list .password p').css({'borderBottom':'none'});
                $('.list .sign-out p').css({'borderBottom':'none'});
              }
              $body.on('click', '.user .sign-out', events.signOut);
              $body.on('click', '.user .customer-service', events.customerService);
              $body.on('click', '.user .creditprogress', events.creditprogress);
            }

        listOrder.then(function(orders) {
          listreturn.then(function(returns) {
          	if (config.sys == 'ceb') {
	          	services.http.get('/leaveMessage/count').then(function(count) {
	              render ({
	                orders:orders,
	                returns:returns,
	                mobile: mobile,
	                userdata:userdata,
	                config:config,
	                count:count
	              });
	           });
           	} else {
	           	render ({
	                orders:orders,
	                returns:returns,
	                mobile: mobile,
	                userdata:userdata,
	                config:config
	            });
           	}
          });
        });
        } else {
          //分信宝登录失败
          if (config.sys == 'tyfq') {
            if (data.code == 1013) {
              location.href = '#sign?fwd=/?#user';
            } else {
              ui.makeToast(data.message);
            }
          } else {
            ui.makeToast(data.message);
          }
        }
      }).fail(function(jqXHR, textStatus, errorThrown) {
        if (jqXHR.status == 401) {
          if (config.sys == 'tyfq' || config.sys == 'thb') {
						if(localStorage.getItem('userURl')) {
              localStorage.removeItem('userURl');
              localStorage.removeItem('SignURl'); 
              location.href = '#home';
            } else {
              localStorage.setItem('userURl',location.hash);
							services.http.get('/users/openId?state=sign?fwd=user').then(function(resp){
								if(resp.code == 302) {
									location.href = resp.url;
								}else if(resp.code == 200) {
									location.href = '#sign?fwd=/?#user';
								}
							});
            }
          } else {
            if(localStorage.getItem('lasthash') == location.hash) {
             localStorage.setItem('lasthash','');
             location.href ='#home';
            } else {
              localStorage.setItem('lasthash',location.hash);
              location.href = '#connects';
            }
          }
        } else {
          ui.makeToast(errorThrown);
        }
      });
    },
    /*个人信息设置*/
    selfmsg:function() {
        services.setTitle('个人信息');
        //微信api 数据注入接口
        if(config.sys!='ceb') {
          var thisULR = window.location.href;
          services.http.get('/weixin/wxConfig?url='+thisULR).then(function(resp) {
            if(resp.code == 200) {
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
                    jsApiList: ['chooseImage','uploadImage','downloadImage']
                });
            } else {
              alert(resp.message);
            }
          });
        }
        var compiled = _.template(selfmsgTpl);
        var $body = $('.viewport .body');
        var sys = '';
        services.http.get('users').then(function(resp) {
           resp.data.birthday = services.formatTime(resp.data.birthday);
           var rendered = compiled(resp);
           $body.off();
           $body.html(rendered);
           if(resp.code ==200) {
             if(resp.data.headImg) {
               $('.pto i img').attr('src',resp.data.headImg);
             } else {
               if(config.sys == 'ceb') {
                 sys = 'guangda_';
                 $('.pto p i img').attr('src','./img/touxian@2x.png');
                 $('.selfmsg .list li:nth-child(1)  p ').css('background','none');
                 $('.selfmsg .list li:nth-child(1)  p i ').css('right','0');
               }else if(config.sys == 'thb') {
                 sys ='';
                 $('.pto p i img').attr('src','./img/touxiang@2x.png');
               }else if(config.sys == 'tyfq') {
                //  $('.pto p img').attr('src','./img/touxiang@2x.png');
               }
             }
             $('.woman img').attr('src','img/'+sys+'woman.png');
             $body.on('click','.selfmsg .list .nick', events.nick);
             $body.on('focus','.nk .nki', events.focu);
             $body.on('click','.nk .clear', events.cleard);
             $body.on('click','.nk .sure', events.sure);
             $body.on('click','.selfmsg .list .sex', events.sex);
             $body.on('click','.sx .man', events.sexman);
             $body.on('click','.sx .woman', events.sexwoman);
             $body.on('click','.selfmsg .list .pto', events.pto);
             $body.on('click','#dateconfirm',events.birth);
             $('#beginTime').date();
           }
        });
        var events = {
          nick : function() {
            $('.changenick').show();
          },
          focu : function() {
            $('.nk .nki').css('border','1px solid #999999');
          },
          cleard : function() {
            $('.changenick').hide();
          },
          sure : function() {
            var nicki = $('.nk .nki').val();
            if(nicki) {
              var reg = /^([\u4e00-\u9fa5]|\w|\d){1,12}$/;
              if(reg.test(nicki)) {
                $('.changenick').hide();
                $('.nick p i').html(nicki);
                services.http.putJson('/users/settings',{type:2,parameter:nicki});
              } else {
                alert('最多可输入12个字');
              }
            } else {
              alert('请输入新昵称');
            }
          },
          sex : function() {
            $('.changesex').show();
            if($('.sex p i').text() =='男') {
              $('.sx .man img').attr('src','img/man_choice.png');
              $('.sx .woman img').attr('src','img/'+sys+'woman.png');
            }else if($('.sex p i').text() =='女') {
              $('.sx .woman img').attr('src','img/'+sys+'woman_choice.png');
              $('.sx .man img').attr('src','img/man.png');
            } else {
              $('.sx .man img').attr('src','img/man.png');
              $('.sx .woman img').attr('src','img/'+sys+'woman.png');
            }
          },
          sexman : function() {
            $('.sx .man img').attr('src','img/man_choice.png');
            $('.sex p i').text('男');
            $('.changesex').hide();
            $('.sx .woman img').attr('src','img/'+sys+'woman.png');
            services.http.putJson('/users/settings',{type:4,parameter:0});
          },
          sexwoman : function() {
             $('.sx .woman img').attr('src','img/'+sys+'woman_choice.png');
             $('.sex p i').text('女');
             $('.changesex').hide();
             $('.sx .man img').attr('src','img/man.png');
             services.http.putJson('/users/settings',{type:4,parameter:1});
          },
          pto : function() {
            if(config.sys!='ceb') {
              var $this =$(this);
              wx.chooseImage({
                count: 1,
                sizeType:  ['compressed'],
                sourceType: [],
                success: function (res) {
                  var localimg = res.localIds;
                  $('.selfmsg .list .pto p i').find('img').attr({'src':localimg});
                  wx.uploadImage({
                    localId : localimg[0],
                    isShowProgressTips : 1,
                    success: function(res) {
                      var serverId = res.serverId;
                      var src=$('.pto img').attr('src');
                      services.http.post('users/upload/img',{mediaId:serverId,type:'photo'}).then(function(resp){
                        var src =location.origin+'/photo/'+resp.imgUrl;
                        services.http.putJson('/users/settings',{type:1,parameter:src}).then(function(resp){
                          if(resp.code == 200) {
                            ui.makeToast('头像上传成功');
                          } else {
                            ui.makeToast('头像上传失败');
                            var touxiang = $('.user .center-top img').attr('src');
                            $($this).find('img').attr({'src':touxiang});
                          }
                        });
                      });
                    }
                  });
                }
              });
            }
          },
          birth : function() {
            var bothi = $('.birth i').text();
            services.http.putJson('/users/settings',{type:3,parameter:bothi});
          }
        };
    },
    /* 身份证信息*/
    idcard:function() {
      services.setTitle('填写身份证信息');
      var compiled = _.template(idcardTpl);
      var $body = $('.viewport .body');
      $('.viewport .sign').css({'height':'100%'});
      $('.viewport .body').css({'height':'100%'});
      $('.viewport >div').css({'height':'100%'});
      $('body').css({'background':'#fff'});
      services.http.get('users').then(function(data) {
        $('.idcardcredit .submint').attr('data-userType',data.data.userType);
        if (data.data.userType == 'A1') {
        	if(data.data.creditState == '0') {
        		var rendered = compiled(data);
	          $body.off();
	          $body.html(rendered);
	          $body.on('click', '.idcardcredit .submint', events.submit);
        	} else {
        		services.http.get('Credit/MemerInfo').then(function(resp) {
	            if (resp.code == 200) {
	            	resp.data.userType = data.data.userType;
	              var rendered = compiled(resp);
	              $body.off();
	              $body.html(rendered);
	              $body.on('click', '.idcardcredit .submint', events.submit);
	            } else {
	              ui.makeToast(resp.message);
	            }
	          });
        	}
        } else {
          services.http.get('Credit/MemerInfo').then(function(resp) {
            if (resp.code == 200) {
              var rendered = compiled(resp);
              $body.off();
              $body.html(rendered);
              $body.on('click', '.idcardcredit .submint', events.submit);
            } else {
              ui.makeToast(resp.message);
            }
          });
        }
      });
      var events = {
        submit:function() {
          $('.idcardcredit .submint').attr('disabled','disabled');
          $('.z_box100').css({'background':'rgba(0, 0, 0, 0.8)'});
          $('.z_box100').removeClass('hidden');
          var realname=$('.idcardcredit .realname input').val().replace(/\s+/g, '');
          var idcard=$('.idcardcredit .idcard input').val().replace(/\s+/g, '');
          var reidcard = /^[1-9]{1}[0-9]{14}$|^[1-9]{1}[0-9]{16}([0-9]|[xX])$/;
          var verifyState = $(this).data('state');
          var userType = $(this).data('usertype');
          if (!realname) {
            $('.z_box100').addClass('hidden');
            $('.idcardcredit .submint').removeAttr('disabled');
            ui.makeToast('请填写正确的姓名');
            return;
          }else if (!idcard) {
            $('.z_box100').addClass('hidden');
            $('.idcardcredit .submint').removeAttr('disabled');
            ui.makeToast('请填写正确的身份证号码');
            return;
          }else if (!reidcard.test(idcard)) {
            $('.z_box100').addClass('hidden');
            $('.idcardcredit .submint').removeAttr('disabled');
            ui.makeToast('请填写正确的身份证号码');
            return;
          }
          var data = {
            realName:realname,
            idCard:idcard
          };
          if (verifyState == 'A1' || verifyState == 'A2' || verifyState == '') {
            if (userType == 'A1') {
              var creditSubmit=services.http.postJson('users/creditSubmit',data);
              creditSubmit.then(function(resp) {
                if (resp.code == 200) {
                  location.href='#creditprogress';
                  $('.z_box100').addClass('hidden');
                  $('.idcardcredit .submint').removeAttr('disabled');
                } else {
                  $('.idcardcredit .prompt').text(resp.message);
                  $('.z_box100').addClass('hidden');
                  $('.idcardcredit .submint').removeAttr('disabled');
                }
              });
            } else {
              var data1 = {
                name:data.realName,
                cardNumber:data.idCard
              };
              var validIdentity=services.http.postJson('Credit/SaveMemerInfo',data1);
              validIdentity.then(function(resp) {
                if (resp.code == 200) {
                  localStorage.setItem('progressflag',2);
                  location.href='#JobInformation';
                  $('.z_box100').addClass('hidden');
                  $('.idcardcredit .submint').removeAttr('disabled');
                } else {
                  $('.idcardcredit .prompt').text(resp.message);
                  $('.z_box100').addClass('hidden');
                  $('.idcardcredit .submint').removeAttr('disabled');
                }
              });
            }
          }else if (verifyState == 'A3' || verifyState == 'A4') {
            if (userType == 'A1') {
              location.href='#creditprogress';
            } else {
              location.href='#JobInformation';
            }
          }
        }
      };
    },
    /* 工作信息*/
    JobInformation:function(id) {
      services.setTitle('填写公司信息');
      var $body = $('.viewport .body');
      var compiled = _.template(JobInformationTpl);
      var companyName;
      var state;
      var emailSuf;
      var provinces;
      if (location.href.indexOf('=') != -1) {
        var temp = location.href.split('=')[1];
        companyName = decodeURIComponent(temp.split('&')[0]);
        state = temp.split('&')[1];
        emailSuf = temp.split('&')[2];
      }
      services.http.get('Credit/JobInfo').then(function(resp) {
        if (resp.code == 200) {
            var rendered = compiled(resp);
            $body.off();
            $body.html(rendered);
            $body.on('click', '.idcardcredit .submint', events.button);
            if (!(resp.data.verifyState == 'A3' || resp.data.verifyState == 'A4')) {
              //解决回退之后重新选择公司会出现公司错乱的问题 外面挪到判断里面 author:Black
              if (companyName) {
                $('input[name=jobname]').val(companyName);
              }
              if (state) {
                $('.idcardcredit .submint').attr('data-state', state);
              }
              if (emailSuf) {
                $('.idcardcredit .submint').attr('data-emailSuf', emailSuf);
              }
              $body.on('click', '.idcardcredit .jobname', events.jobname);
              $body.on('click', '.idcardcredit .jobaddress', events.showRegionSelection);
              $body.on('click', '.addres-cont li', events.regionSelected);
              $body.on('click', '.addres li', events.changeRegionLevel);
            }
            if (config.sys == 'tyfq') {
              $('body').css({'background':'#fff'});
            }
        } else {
            ui.makeToast(resp.message);
        }
      });

      var events = {
        button:function() {
          var data = {
            companyName:$('input[name=jobname]').val(),
            province:$('.jobInformation .jobaddress input').data('province'),
            city:$('.jobInformation .jobaddress input').data('city'),
            county:$('.jobInformation .jobaddress input').data('county'),
            stateId:$('.jobInformation .jobaddress input').data('stateid'),
            cityId:$('.jobInformation .jobaddress input').data('cityid'),
            countyId:$('.jobInformation .jobaddress input').data('countyid'),
            companyAddress:$('input[name=detailaddress]').val(),
            state:$('.idcardcredit .submint').data('state'),
            emailSuf:$('.idcardcredit .submint').data('emailsuf')
          };
          var verifyState = $(this).data('verifystate');
          var state = $(this).data('state');
          if (!$('input[name=jobname]').val()) {
            ui.makeToast('请输入单位全称');
            return;
          }
          if (!$('input[name=jobaddress]').val()) {
            ui.makeToast('请选择所在区域');
            return;
          }
          if (!$('input[name=detailaddress]').val()) {
            ui.makeToast('请输入详细地址');
            return;
          }
          if (verifyState == 'A1' || verifyState == 'A2' || verifyState == '') {
            var SaveJobInfo=services.http.postJson('Credit/SaveJobInfo',data);
            SaveJobInfo.then(function(resp) {
              if (resp.code == 200) {
                if (state) {
                  localStorage.setItem('progressflag',3);
                  emailSufflag = emailSuf;
                  location.href='#EmailVerification';
                } else {
                  localStorage.setItem('progressflag',4);
                  location.href='#ContactPerson';
                }
                $('.z_box100').addClass('hidden');
                $('.idcardcredit .submint').removeAttr('disabled');
              } else {
                $('.idcardcredit .prompt').text(resp.message);
                $('.z_box100').addClass('hidden');
                $('.idcardcredit .submint').removeAttr('disabled');
              }
            });
          }else if (verifyState == 'A3' || verifyState == 'A4') {
            if (state) {
              location.href='#EmailVerification';
            } else {
              location.href='#ContactPerson';
            }
            
          } 
        },
        jobname:function() {
          var thisULR =location.href;
          location.href='/?#CorporateName/?='+thisULR;
        },
        /**
         * 开始三级联动
         */
        showRegionSelection: function() {
          // 重置
          $('.addres li').hide();
          $('.addres-cont ul').empty();
          // 初始化一级
          provinces = provinces || services.http.get('area/0&1');
          provinces.then(function(resp) {
            $('.jobInformation').hide();
            renderRegion(resp.data, 1);
            $('.select-region').show();
            $('.select-region').css({'height':window.screen.height});
            $('.addres-cont').css({'height':window.screen.height - 44});
          });
        },
        /**
         * 三级联动，选中某级行政区域
         */
        regionSelected: function() {
          var $this = $(this),
          id = $(this).data('id'),
          name = $(this).text(),
          level = $this.parent().data('level');
          $this.addClass('active');
          // 更新当前级别的标题为选中行政区的名字
          $('.addres [data-level="' + level + '"]').text(name).removeClass('active');
          $('.addres [data-level="' + level + '"]').attr('data-id',id);
          // 加载下级
          regions = [];
          for(var i=0;i<$('.addres ul li').length;i++) {
            regions.push({
              id:$('.addres ul li').eq(i).data('id'),
              name:$('.addres ul li').eq(i).text()
            });
          }
          if (level < 3) {
            services.http.get('area/'+id+'&'+(level + 1)).then(function(resp) {
              renderRegion(resp.data, level + 1);
            });
          // 结束
          } else {
            var city = regions[1].name == '市辖区' || regions[1].name == '市辖县' ? '' : regions[1].name;
            var fullText = regions[0].name + city + regions[2].name;
            $('.jobInformation .jobaddress input').val(fullText);
            $('.jobInformation .jobaddress input').attr('data-stateId',regions[0].id);
            $('.jobInformation .jobaddress input').attr('data-cityId',regions[1].id);
            $('.jobInformation .jobaddress input').attr('data-countyId',regions[2].id);
            $('.jobInformation .jobaddress input').attr('data-province',regions[0].name);
            $('.jobInformation .jobaddress input').attr('data-city',regions[1].name);
            $('.jobInformation .jobaddress input').attr('data-county',regions[2].name);
            if(localStorage.getItem('PersonalInformation')) {
               location.href='#PersonalInformation';
               localStorage.removeItem('PersonalInformation');
            }
            $('.select-region').hide();
            $('.jobInformation').show();

          }
        },
        /**
         * 三级联动，切换到地级或省级行政区域
         */
        changeRegionLevel: function() {
          var $this = $(this),
          level = $this.data('level');
          if (level == 1) {
            events.showRegionSelection();
          } else if (level < 3) {
            $('.addres li[data-level="3"]').hide();
            var id = regions[level - 2].id;
            services.http.get('area/'+id+'&'+level).then(function(resp) {
              renderRegion(resp.data, level);
            });
          }
        }
      };
      function renderRegion(data, level) {
        var html = '';
        _.each(data, function(region) {
          html += '<li data-id=' + region.id + '>' + region.name + '</li>';
        });
        $('.addres li').removeClass('active');
        $('.addres [data-level="' + level + '"]').text('请选择').addClass('active').show();
        $('.addres-cont > div').attr('data-active-level', level);
        $('.addres-cont div [data-level="' + level + '"]').html(html);
        $('.addres-cont').css({'height':window.screen.height - 44});
        $('.addres-cont > div').css({'height':window.screen.height - 44});
        $('.addres-cont div [data-level="' + level + '"]').css({'height':data.length * 35});
        console.log('计算地址列表高度:'+data.length * 35 +'px');
        if(data.length * 35  <= window.screen.height - 44) {
          $('.addres-cont > div').css({'overflow':'inherit'});
        } else {
         $('.addres-cont > div').css({'overflow':'auto'});
        }
        $('.addres-cont div').scrollTop(0);
      }
    },
    /* 公司名称*/
    CorporateName:function() {
      services.setTitle('公司名称');
      var $body = $('.viewport .body');
      var compiled = _.template(CorporateNameTpl);
      var name='ALL';
      var data = [];
      var retrieveCompanyName =services.http.get('Credit/retrieveCompanyName/'+name);
      var url = location.href.split('=')[1];
      var bflag=true;
      retrieveCompanyName.then(function(resp) {
          if (resp.code == 200) {
            $.each(resp.data,function(index,item) {
              if (item.prefix) {
                if (data !== '') {
                  $.each(data,function(index1,item1) {
                    if (item.prefix == item1.prefix) {
                      data[index1].data.push({
                        name:item.name,
                        state:item.state,
                        emailSuf:item.emailSuf
                      });
                      bflag=true;
                    } else {
                      bflag=false;
                    }
                  });
                  if (!bflag) {
                    var length = data.length;
                    data[length] = {};
                    data[length].prefix=item.prefix;
                    data[length].data=[];
                    data[length].data.push({
                      name:item.name,
                      state:item.state,
                      emailSuf:item.emailSuf
                    });
                  }
                } else {
                  data[index] = {};
                  data[index].prefix=item.prefix;
                  data[index].data=[];
                  data[index].data.push({
                    name:item.name,
                    state:item.state,
                    emailSuf:item.emailSuf
                  });
                }
              }
            });
            resp.data1=data;
            var rendered = compiled(resp);
            $body.off();
            $body.html(rendered);
            $('.viewport .sign').css({'height':'100%'});
            $('.viewport .body').css({'height':'100%'});
            $('.viewport >div').css({'height':'100%'});
            $('.viewport .CorporateName').css({'height':'100%'});
            $body.on('click', '.CorporateName dd', events.clickname);
            $body.on('click', '.CorporateName li', events.clickname);
            $body.on('click', '.CorporateName button', events.submit);
            $body.on('change', '.CorporateName .search input', events.search);
          } else {
            alert(resp.message);
            return;
          }
      });
      var events = {
        clickname:function() {
          var companyName = encodeURIComponent($(this).text());
          var state = $(this).data('state');
          var emailSuf = $(this).data('emailsuf');
          location.href = url + '/?=' + companyName+'&'+state +'&'+emailSuf;
        },
        submit:function() {
          var companyName = $('.CorporateName .search input').val();
          var state = 0;
          var emailSuf = '';
          location.href = url + '/?=' + companyName+'&'+state +'&'+emailSuf;
        },
        search:function() {
          if ($('.CorporateName .search input').val().length != length) {
            length = $('.CorporateName .search input').val().length;
            name = $('.CorporateName .search input').val();
            services.http.get('Credit/retrieveCompanyName/'+name).then(function(resp) {
              if (resp.code == 200) {
                resp.data1='';
                var rendered = compiled(resp);
                $body.off();
                $body.html(rendered);
                $('.CorporateName .search input').val(name);
                $('.viewport .sign').css({'height':'100%'});
                $('.viewport .body').css({'height':'100%'});
                $('.viewport >div').css({'height':'100%'});
                $('.viewport .CorporateName').css({'height':'100%'});
                $body.on('click', '.CorporateName dd', events.clickname);
                $body.on('click', '.CorporateName li', events.clickname);
                $body.on('click', '.CorporateName button', events.submit);
                $body.on('change', '.CorporateName .search input', events.search);
              } else {
                alert(resp.message);
                return;
              }
            });
          }
        }
      };
    },
    /* 邮箱验证*/
    EmailVerification:function() {
      services.setTitle('验证企业邮箱');
      var compiled = _.template(EmailVerificationTpl);
      var $body = $('.viewport .body');
      var emailInfo = services.http.get('Credit/emailInfo');
      emailInfo.then(function(resp) {
        if (resp.code == 200) {
          var rendered = compiled(resp);
          $body.off();
          $body.html(rendered);
          if (config.sys == 'tyfq') {
            $('body').css({'background':'#fff'});
          }
          $('.viewport .sign').css({'height':'100%'});
          $('.viewport .body').css({'height':'100%'});
          $('.viewport >div').css({'height':'100%'});
          $body.on('click', '.idcardcredit .submint', events.button);
          if (resp.data.verifyState == 'A1' || resp.data.verifyState == 'A2' || resp.data.verifyState == '') {
              $body.on('click', '.idcardcredit .obtain', events.obtain);
          }else if (resp.data.verifyState == 'A3' || resp.data.verifyState == 'A4') {
            $('.idcardcredit .obtain').css({'background':'#ccc'});
            $('.idcardcredit .checkcode input').attr('disabled','disabled');
          }
        } else {
          ui.makeToast(resp.message);
        } 
      });
      var events = {
        button:function() {
          var data = {
            email:$('input[name=email]').val()+$('.emailVerification .email span').text(),
            verifyCode:$('input[name=verifyCode]').val()
          };
          var verifyState = $(this).data('verifystate');
          if (!$('input[name=email]').val()) {
            ui.makeToast('请输入邮箱');
            return;
          }else if (!$('input[name=verifyCode]').val()) {
            if (verifyState == 'A1' || verifyState == 'A2' || verifyState == '') {
              ui.makeToast('请输入验证码');
              $('input[name=email]').removeAttr('disabled');
              return;
            }else if (verifyState == 'A3' || verifyState == 'A4') {}  
          }
          if (verifyState == 'A1' || verifyState == 'A2' || verifyState == '') {
            var saveEmailInfo=services.http.postJson('Credit/saveEmailInfo',data);
            saveEmailInfo.then(function(resp) {
              if (resp.code == 200) {
                localStorage.setItem('progressflag',4);
                location.href='#ContactPerson';
                $('.z_box100').addClass('hidden');
                $('.idcardcredit .submint').removeAttr('disabled');
                $('input[name=email]').removeAttr('disabled');
              } else {
                $('.idcardcredit .prompt').text(resp.message);
                $('.z_box100').addClass('hidden');
                $('.idcardcredit .submint').removeAttr('disabled');
                $('input[name=email]').removeAttr('disabled');
              }
            });
          }else if (verifyState == 'A3' || verifyState == 'A4') {
              location.href='#ContactPerson';
          } 
        },
        obtain:function() {
          $('input[name=email]').attr('disabled','disabled');
          var email = $('input[name=email]').val()+$('.emailVerification .email span').text();
          if (!email) {
            ui.makeToast('请输入邮箱');
            $('input[name=email]').removeAttr('disabled');
            return;
          }
          $(this).css({'background':'#ccc'});
          services.http.get('users/getEmailCode?mail='+email).then(function(resp){});
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
    },
    /* 联系人信息*/
    ContactPerson:function(id) {
      services.setTitle('填写联系人信息');
      var $body = $('.viewport .body');
      var compiled = _.template(ContactPersonTpl); 
      services.http.get('Credit/ContactInfo').then(function(resp) {
        if (resp.code == 200) {
          var rendered = compiled(resp);
          $body.off();
          $body.html(rendered);
          if (config.sys == 'tyfq') {
            $('body').css({'background':'#fff'});
          }
          if (resp.data[0]) {
            if (resp.data[0].verifyState == 'A1' || resp.data[0].verifyState == 'A2' || resp.data[0].verifyState == '' || resp.data[0].verifyState == undefined) {
              $body.on('click', '.idcardcredit .information .familyrelationship', events.select);
              $body.on('click', '.idcardcredit .information .familyrelationship li', events.selectli);
            }
          } else {
              $body.on('click', '.idcardcredit .information .familyrelationship', events.select);
              $body.on('click', '.idcardcredit .information .familyrelationship li', events.selectli);
          }
          $body.on('click', '.idcardcredit .submint', events.button);
        } else {
          ui.makeToast(resp.message);
        }
      });
      var events = {
        button:function() {
            if(!$('[name="familyname"]').val() || $('.idcardcredit .familyrelationship span').val() =='请选择亲属关系' || !$('input[name="familytel"]').val() || !$('[name="friendname"]').val() || !$('[name="friendtel"]').val()){
              ui.makeToast('请填写完整的联系人信息!');
              return;
            }
            var regut = /^[1][0-9]{10}$/;
            var verifyState = $(this).data('verifystate');
            var data=[{
                id:id,
                contactName:$('[name="familyname"]').val(),
                contactPhone:$('[name="familytel"]').val(),
                type:'A1',
                relationship:$('.idcardcredit .information .familyrelationship span').data('value'),
                verifyState:verifyState
            },
            {
                id:id,
                contactName:$('[name="friendname"]').val(),
                contactPhone:$('[name="friendtel"]').val(),
                type:'A2',
                relationship:'',
                verifyState:verifyState
            }];
            if (!data[0].contactName) {
              ui.makeToast('请输入姓名');
              return;
            }else if (!data[0].contactPhone) {
              ui.makeToast('请输入11位手机号码');
              return;
            }else if (!regut.test(data[0].contactPhone)) {
              ui.makeToast('请输入正确的11位手机号码');
              return;
            }else if (!data[0].relationship) {
              ui.makeToast('请选择亲属关系');
              return;
            }else if (!data[1].contactName) {
              ui.makeToast('请输入姓名');
              return;
            }else if (!data[1].contactPhone) {
              ui.makeToast('请输入11位手机号码');
              return;
            }else if (!regut.test(data[1].contactPhone)) {
              ui.makeToast('请输入正确的11位手机号码');
              return;
            }
            if (verifyState == 'A1' || verifyState == '' || verifyState == undefined) {
              services.http.postJson('Credit/SaveContacInfo',data).then(function(resp) {
                if(resp.code ==200) {
                  localStorage.setItem('progressflag',5);
                  location.href='#creditcard';
                  $('.z_box100').addClass('hidden');
                  $('.idcardcredit .submint').removeAttr('disabled');
                } else {
                  $('.idcardcredit .prompt').text(resp.message);
                  $('.z_box100').addClass('hidden');
                  $('.idcardcredit .submint').removeAttr('disabled');
                }
              });
            }else if(verifyState == 'A2') {
              services.http.postJson('Credit/SaveContacInfo',data).then(function(resp) {
                if(resp.code ==200) {
                  location.href='#creditprogress';
                  $('.z_box100').addClass('hidden');
                  $('.idcardcredit .submint').removeAttr('disabled');
                } else {
                  $('.idcardcredit .prompt').text(resp.message);
                  $('.z_box100').addClass('hidden');
                  $('.idcardcredit .submint').removeAttr('disabled');
                }
              });
            }else if (verifyState == 'A3' || verifyState == 'A4') {
              location.href='#creditcard';
            }
        },
        select:function() {
          $('.idcardcredit .information .familyrelationship ul').css({'display':'block'});
        },
        selectli:function() {
          $('.idcardcredit .information .familyrelationship span').text($(this).text());
          $('.idcardcredit .information .familyrelationship span').attr({'data-value':$(this).data('value')});
          $(this).parent().css({'display':'none'});
        }
      };
    },
    /*信用卡信息*/
    creditcard:function() {
      services.setTitle('信用授权');
      var compiled = _.template(creditcardTpl);
      var $body = $('.viewport .body');
      var bankCardInfo = services.http.get('Credit/bankCardInfo');
      bankCardInfo.then(function(resp) {
        if (resp.code == 200) {
          var rendered = compiled(resp);
          $body.off();
          $body.html(rendered);
          if (config.sys == 'tyfq') {
            $('body').css({'background':'#fff'});
          }
          $('.viewport .sign').css({'height':'100%'});
          $('.viewport .body').css({'height':'100%'});
          $('.viewport >div').css({'height':'100%'});
          $body.on('click', '.idcardcredit .submint', events.button);
          if (resp.data.verifyState == 'A1' || resp.data.verifyState == 'A2' || resp.data.verifyState == '') {
            $body.on('click', '.idcardcredit .obtain', events.obtain);
          }else if (resp.data.verifyState == 'A3' || resp.data.verifyState == 'A4') {
            $('.idcardcredit .obtain').css({'background':'#ccc'});
            $('.idcardcredit .checkcode input').attr('disabled','disabled');
          }
        } else {
          ui.makeToast(resp.message);
        } 
      });
      var events = {
        button:function() {
          var data = {
            cardNo:$('input[name=bankcard]').val(),
            mobile:$('input[name=mobile]').val(),
            verifyCode:$('input[name=checkcode]').val()
          };
          var recard = /^\d{16}|\d{19}$/;
          var regut = /^[1][0-9]{10}$/;
          var verifyState = $(this).data('verifystate');
          if (!$('input[name=bankcard]').val()) {
            ui.makeToast('请输入银行卡号');
            return;
          }
          if (!$('input[name=mobile]').val()) {
            ui.makeToast('请输入手机号');
            return;
          }
          if (!recard.test($('input[name=bankcard]').val())) {
            ui.makeToast('请输入正确的银行卡号');
            return;
          }
          if (!regut.test($('input[name=mobile]').val())) {
            ui.makeToast('请输入正确的手机号');
            return;
          }
          if (!$('input[name=checkcode]').val()) {
            if (verifyState == 'A1' || verifyState == 'A2' || verifyState == '') {
              ui.makeToast('请输入验证码');
              return;
            }
          }
          if (verifyState == 'A1' || verifyState == 'A2' || verifyState == '') {
            var savebankCardInfo=services.http.postJson('Credit/saveBankCardInfo',data);
            savebankCardInfo.then(function(resp) {
              if (resp.code == 200) {
               localStorage.setItem('progressflag',0);
                location.href='#creditprogress';
                $('.z_box100').addClass('hidden');
                $('.idcardcredit .submint').removeAttr('disabled');
              } else {

                $('.idcardcredit .prompt').text(resp.message);
                $('.z_box100').addClass('hidden');
                $('.idcardcredit .submint').removeAttr('disabled');
              }
            });
          }else if (verifyState == 'A3' || verifyState == 'A4') {
              location.href='#creditprogress';
          } 
        },
        obtain:function() {
          var mobile = $('input[name=mobile]').val();
          var bankCardNo = $('input[name=bankcard]').val();
          var regut = /^[1][0-9]{10}$/;
          if (!mobile) {
            ui.makeToast('请输入11位手机号码');
            return;
          }
          if (!regut.test($('input[name=mobile]').val())) {
            ui.makeToast('请输入正确的手机号');
            return;
          }
          $(this).css({'background':'#ccc'});
          services.http.get('users/getBankCardNoCode'+'/'+bankCardNo+'/'+mobile).then(function(resp) {});
          var ppt=$(this);
          var text_time=60;
          $(this).addClass('state');
          $(this).attr({'disabled':'disabled'});
          var clearset = setInterval(function(){
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
    },
    /* 个人信息*/
    personmessage:function() {
      services.setTitle('个人信息');
      var compiled = _.template(personmessageTpl);
      var $body = $('.viewport .body');
      var privateInfo = services.http.get('privateInfo');
      privateInfo.then(function(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
        $('.viewport .sign').css({'height':'100%'});
        $('.viewport .body').css({'height':'100%'});
        $('.viewport >div').css({'height':'100%'});
      });
    },
    /* 我的银行卡*/
    bankcard:function() {
      services.setTitle('我的银行卡');
      var compiled = _.template(bankcardTpl);
      var $body = $('.viewport .body');
      var privateInfo = services.http.get('privateInfo');
      privateInfo.then(function(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
        $('.viewport .sign').css({'height':'100%'});
        $('.viewport .body').css({'height':'100%'});
        $('.viewport >div').css({'height':'100%'});
      });
    },
    /* 授信进度*/
    creditprogress:function() {
      services.setTitle('授信进度');
      var compiled = _.template(creditprogressTpl);
      var $body = $('.viewport .body');
      var creditStep=services.http.get('users/creditStep');
      creditStep.then(function(resp) {
        if (resp.code == 200) {
          var rendered = compiled(resp);
          $body.off();
          $body.html(rendered);
          $('.viewport .sign').css({'height':'100%'});
          $('.viewport .body').css({'height':'100%'});
          $('.viewport >div').css({'height':'100%'});
          var events = {
            submit:function() {
              if (resp.data.userType == 'A1') {
                  location.href='#idcard';
              } else {
                  location.href='#ContactPerson';
              }
            },
            button:function() {
            	$('.z_box100').css({'background':'rgba(0, 0, 0, 0.8)'});
          		$('.z_box100').removeClass('hidden');
              location.href='#creditprogress';
            }
          };
          $body.on('click', '.progress .submit', events.submit);
          $body.on('click', '.progress .buttonprogress button', events.button);
        } else {
          ui.makeToast(resp.message);
        }
          
      });
    },
    //意见反馈
    leaveMessage:function() {
      services.setTitle('意见反馈');
      var $body = $('.viewport .body');
      var compiled = _.template(leaveMessageTpl);

      function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered); 
      }
      services.http.get('/leaveMessage/count').then(function(count) {
      	console.log(count);
        render ({
          count:count
        });
      });
   },
   survey:function() {
      services.setTitle('意见反馈');
	    var $body = $('.viewport .body');
	    var compiled = _.template(surveyTpl);
	    var events = {
        button:function() {
          var content = $('textarea').val().replace(/\s+/g, '');
          var reText=/^[\u4E00-\u9FA5\w\d\P]|[\uFE30-\uFFA0]+$/;
          var res = /[\uD800-\uDBFF][\uDC00-\uDFFF]/g;
		      if(!(reText.test(content) || content == '')|| res.test(content)) {
		        alert('请勿输入表情，否则提交失败！');
		        return;
		      }
          var typeId;
          $('.survey .troubles li:nth-child(odd)').each(function(index,item) {
          	if(item.children[0].checked == true) {
          		typeId = Number(item.children[0].value);
          		return false;
          	}
          });
          var params = {'content':content,'typeId':typeId};
          
          services.http.postJson('leaveMessage',params).then(function(resp) {
            ui.makeToast('感谢您宝贵的意见，我们会尽快给您答复');
            location.href='#user';
          });
        },
	      watch:function() {
					$('.watch').html($(this).val().length + ' /80');  
					var content = $('textarea').val().replace(/\s+/g, '');
	      	if(content.length>0) {
	      		$('.survey button.submit').css({'background':'#f63'});
	      		$('.survey button.submit').removeAttr('disabled');
	      	} else {
	      		$('.survey button.submit').css({'background':'#ccc'});
	      		$('.survey button.submit').attr('disabled','true');
	      	}
	      }
      };
      
	    function render(data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
        $('.survey .troubles li:first-child').find('input').attr('checked','true');
        $body.on('click', '.survey button.submit', events.button);
        $body.on('input', '.survey .intext textarea', events.watch);
      }
      services.http.get('/leaveMessageType').then(function(leaveMessageType) {
        render ({
          leaveMessageType:leaveMessageType
        });
      });
   },
   mymessage:function() {
      services.setTitle('我的消息');
	    var $body = $('.viewport .body');
	    var compiled = _.template(mymessageTpl);
	    var flag = true;
	    var deleting = false;
	    var events = {
	    	showmore:function() {
	    		if(flag) {
	    			$(this).siblings('dl').find('dd').css({'height':'auto'});
	    			$(this).css({'background':'url(../../../../img/fankuijiantouxia.png) no-repeat 95% 10px','background-size':'12px 12px'});
	    			flag = false;
	    		} else {
	    			$(this).siblings('dl').find('dd').css({'height':'80px'});
	    			$(this).css({'background':'url(../../../../img/fankuijiantou.png) no-repeat 95% 10px','background-size':'12px 12px'});
	    			flag = true;
	    		}
	    	},
	    	abortDeletigAddress: function(event) {
          var $lis = $('.mymessage li');
          if (event.target.innerText == '删除' || !deleting) {
            return true;
          }
          $lis.addClass('delderansform').removeClass('shantransform');
          deleting = true;
          return false;
        },
	    	itemDelete: function(event) {
          var $this = $(this);
          deleting = true;
          if (event.type == 'swipeLeft') {
          	for(var i=0;i<$('.mymessage li').length;i++) {
		           var height =  $('.mymessage li').eq(i).height();
		           $('.mymessage li').eq(i).find('dl .btn').css({'height':height,'line-height':height+'px'});
		        }
            $this.siblings().addClass('delderansform').removeClass('shantransform');
            $this.addClass('shantransform').removeClass('delderansform');
          } else {
            $this.addClass('delderansform').removeClass('shantransform');
          }
        },
        deleteItem: function() {
          services.http.delete('leaveMessage/'+$(this).data('id')).then(function(resp) {
            deleting = false;
            services.http.get('/leaveMessage').then(function(leaveMessage) {
			        render ({
			          leaveMessage:leaveMessage
			        });
			      });
          });
        }
	    };
      function render (data) {
        var rendered = compiled(data);
        $body.off();
        $body.html(rendered);
        if(data.leaveMessage.data.length <1 ) {
        	$('body').css({'background':'url("../../../../img/infos.png") no-repeat 50% 23%','background-size':'125px 82px'});
        }
        for(var i=0;i<$('.mymessage li').length;i++) {
           var height =  $('.mymessage li').eq(i).height();
           $('.mymessage li').eq(i).find('dl .btn').css({'height':height,'line-height':height+'px'});
        }
        $body.on('swipeLeft', '.list-li', events.itemDelete);
        $body.on('swipeRight', '.list-li', events.itemDelete);
        $body.on('change', '.list-li', events.itemDelete);
        $body.on('click', '.btn', events.deleteItem);
        $body.on('click', '.mymessage .showmore', events.showmore);
        $('.mymessage', $body)[0].addEventListener('click', events.abortDeletigAddress, true);
      }
      services.http.get('/leaveMessage').then(function(leaveMessage) {
        render ({
          leaveMessage:leaveMessage
        });
      });
   },
   reload:function() {
      services.setTitle('重新加载');
	    var $body = $('.viewport .body');
	    var compiled = _.template(reloadTpl);
	    var rendered = compiled();
      $body.off();
      $body.html(rendered);
      $('body').css({'background':'#fff'});
 			var match = location.hash.match(/state=([^&]+)/);
      var state = decodeURIComponent(match[1]).replace('=/?#','=');
      $body.on('click','.reloadBtn',function() {
      	services.http.get('/users/openId?state='+state).then(function(resp) {
					if(resp.code == 302) {
						location.href = resp.url;
					}else if(resp.code == 200) {
						location.href = '#sign?fwd=/?#user';
					}
				});
      });
    }
  };
  return userController;
});
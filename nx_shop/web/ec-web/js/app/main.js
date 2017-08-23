'use strict';

require.config({
  // baseUrl:
  paths: {
    text: '../lib/text',
    underscore: '../lib/underscore',
    zepto: '../lib/zepto',
    callbacks: '../lib/callbacks',
    deferred: '../lib/deferred',
    touch: '../lib/zepto-modules/touch',
    zTouch: '../lib/zTouch',
    base64: '../lib/base64',
    md5: '../lib/md5',
    scooch: '../lib/scooch',
    weixinjs:'../lib/jweixin-1.0.0',
    layer:'../lib/layer/layer',
    fx:'../lib/zepto-modules/fx',
    swiper:'../lib/swiper',
    iscroll:'../lib/iscroll',
    date:'../lib/date'
  },
  shim: {
    underscore: {
      exports: '_'
    },
    zepto: {
      exports: '$'
    },
    callbacks: {
      deps: ['zepto']
    },
    deferred: {
      deps: ['callbacks']
    },
    touch: {
      deps: ['zepto']
    },
    scooch: {
        deps: ['zepto']
    },
    fx: {
        deps: ['zepto']
    },
    swiper:{
    	deps:['zepto']
    },
    iscroll : {
        deps: ['zepto']
    },
    date : {
        deps: ['zepto']
    }
  }
});

require([
  'underscore',
  'zepto',
  'deferred',
  'touch',
  'base64',
  'md5',
  'config',
  './services',
  './controller/index',
  './controller/home',
  './controller/product',
  './controller/order',
  './controller/address',
  './controller/lottery',
  './controller/UniversalSpike',
  './controller/user',
  './controller/coupon',
  './controller/category',
  './controller/lotteryT',
  './controller/favorite',
  './controller/seckill',
  './controller/pickup',
  './controller/search',
  './controller/searchList'
], function (_, $,deferred, touch, base64, md5, config, services,
  indexController, homeController, productController, orderController,
  addressController, lotteryController, SpikeController,userController, couponController, categoryController,lotController,favoriteController,SeckillController,pickupController,searchController,searchListController) {


  $('html').addClass(config.sys);
  // 迷你路由
  if(localStorage.getItem('openId')){
    setCookie("openId",localStorage.getItem('openId'),365);
  }

  function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
  }

 var isRepeatProduct = false;  //记录不同商品切换
 var urlArray = JSON.parse(localStorage.getItem("urlArray"))||[];
 var urlObj= JSON.parse(localStorage.getItem("urlObj"))||{};
 //[{Page:"home",Hash:"home"}]
    function historyStateCalback(reHash){
        urlArray.pop();
        localStorage.setItem("urlArray",JSON.stringify(urlArray));
    }
  function route(event, data) {
      var hash = $.trim(location.hash);
      $("body").css({"background":"#f4f8fb"});

      if (hash.substr(0, 1) === '#') {
         hash = hash.substr(1);
      }

      // SPA应用初始化时，不要单独上报PV，否则重复
      if (event !== true && window._hmt) {
        _hmt.push(['_trackPageview', '/?#' + hash]);
      }

      isRepeatProduct = false;
      event = event||{};
      if(event.state){ //返回

          var hisObj = urlArray.pop();
          if(urlArray.length){
              hash = urlArray[urlArray.length-1].Hash;
              history.replaceState({target:hash}, "", "#"+hash);
          }else{
              hash = "home";
              history.replaceState({target:"home"}, "", "#home");
          }
          $(".viewport").siblings("div").remove();
          $("body").css({"overflow":""});
          //清空优惠券
          if(hisObj.Hash.indexOf("order/create")!=-1){
              couponController.clearCoupon();
          }
          var pagePath=localStorage.getItem("pagePath");
          if(pagePath=="couponAddPage"){
              location.href="#"+hisObj.Hash;
              localStorage.setItem("pagePath","");
              return false;
          }
      }else{ //新增
          var strHash = ["order/type","product"],lastArrObj,needPush =true;
          lastArrObj = urlArray[urlArray.length-1]||{};
          var pageType ="";
          if(lastArrObj.Hash==hash){
              needPush =false;
          }else{
              $.each(strHash,function(i,sh){
                  if(hash.indexOf(sh)=="0"){
                      pageType=sh;
                      if(lastArrObj.Page==sh){
                          needPush = true;      //修改订单列表页最后一个替换
                          if(sh=="product"){
                              isRepeatProduct = true;
                          }
                          return false;
                      }
                  }
              });
          }

          if(needPush){
              var payType = pageType||hash;
              if(urlObj[payType]){
                  $.each(urlArray,function(i,obj){
                      if(obj.Page==payType){
                          urlArray.splice(i,1);
                          return false;
                      }
                  });
              }else{
                  urlObj[payType] = hash;
              }
              urlArray.push({Page:pageType||hash,Hash:hash});
          }
          history.replaceState({target:hash}, "", "#"+hash);
      }
      localStorage.setItem("urlArray",JSON.stringify(urlArray));
      localStorage.setItem("urlObj",JSON.stringify(urlObj));
      var match;
      // 光大需要模拟一个 openId
      if ('index' === hash) {
          indexController.index();
      } else if ('home' === hash) {
          urlArray = [{Page:"home"||hash,Hash:"home"}];
          urlObj["home"]="home";
          localStorage.setItem("urlArray",JSON.stringify(urlArray));
          localStorage.setItem("urlObj",JSON.stringify(urlObj));
          homeController.homePage();
      // 商品详情
      } else if (match = hash.match(/product\/([^\/]+)/)) {
          productController.index(match[1],historyStateCalback);
      // 限购专题页
      } else if (match = hash.match(/purchase\/(\d+)/)) {
        productController.purchase(match[1]);
      }
      //新东方商品详情
      else if(match = hash.match(/orientalindex\/([^\/]+)/)){
          productController.orientalindex(match[1],historyStateCalback);
      //退课须知
      } else if ('notice' === hash) {
        productController.notice();
      }
      // 订单确认
      else if (match = hash.match(/order\/create\/([^\/?]+)/)){
          orderController.create(match[1],historyStateCalback);
      // 下单页的优惠券选择页
      } else if ( match = hash.match(/order\/coupon\?data=(.+)/) ) {
          orderController.coupon(JSON.parse(decodeURIComponent(match[1])));
      //绑定
      } else if ('connects' === hash) {
        homeController.connects();
      }else if(match = hash.match(/connects\?fwd=([^&]+)/)) {
        homeController.connects(decodeURIComponent(match[1]),historyStateCalback);
      //添加地址
      } else if ('address/edit' === hash) {
        addressController.edit(null,urlArray);
      //编辑地址
      } else if (match = hash.match(/address\/edit\/(\d+)/)) {
        addressController.edit(match[1],urlArray);
      //地址列表
      } else if ('address' === hash) {
        addressController.list(null,urlArray);
      // 地址列表，带默认选中
      } else if (match = hash.match(/address\?selectedId=(\d+)/)) {
        addressController.list(match[1],urlArray);
        //个人信息设置
      }else if('selfmsg'=== hash) {
        userController.selfmsg();
      //分享有礼
      }else if('share' === hash){
        homeController.share();
      //分享后页面
      } else if (match = hash.match(/sharefriend\/([^\/]+)/)) {
          homeController.sharefriend(match[1],historyStateCalback);
      // 订单列表
      } else if (match = hash.match(/order\/type\/([^\/]+)/)){
        orderController.list(match[1]);
      // 订单详情
      } else if (match = hash.match(/order\/(\d+)/)) {
        orderController.detail(match[1]);
      // 退货详情
      } else if (match = hash.match(/order\/return\/create\/([^\/]+)/)) {
        orderController.createReturn(match[1]);
      // 退货详情
      } else if (match = hash.match(/order\/return\/([^\/]+)/)) {
        orderController.returnDetail(match[1]);

      }else if(match = hash.match(/messsge\/([^\/]+)/)){

        orderController.messsge(match[1]);

      } else if('cebsucc'=== hash){
        orderController.cebsucc();
      } else if('cebfail'=== hash){
        orderController.cebfail();
      } else if('lottery'=== hash){
        lotteryController.lottery();
      }else if('lottery/succ'=== hash){
          lotteryController.succ();
      }else if('lottery/prizelist'=== hash){
          lotteryController.prizelist();
       //个人中心
      }else if('user'=== hash) {
        userController.user();
      //通用秒杀
      }else if('spike' === hash){
        SpikeController.spike();
      //优惠券
      }else if('coupon' === hash){
        couponController.coupon();
      //分类
      }else if('couponList' === hash){
          couponController.couponList();
      }else if('couponSelect' === hash){
          couponController.couponSelect();
      }
      else if('category' === hash){
        categoryController.category();
      }else if( match = hash.match(/category\/categorylist\/([^\/]+)/)){
        categoryController.categorylist(match[1]);
        //公司名称
      }else if('CorporateName' === hash){
        userController.CorporateName();
      }else if(match = hash.match(/CorporateName\/([^\/]+)/)){
        userController.CorporateName(match[1]);

        //收信地址
      }else if( match = hash.match(/creditAddress\/([^\/]+)/)){
        userController.creditAddress();
        //个人信息

      }else if(match = hash.match(/PersonalInformation\/([^\/?]+)/)){
        userController.PersonalInformation(match[1]);
        //个人信息
      }else if('PersonalInformation' === hash){
        userController.PersonalInformation();
        //意见反馈
      }else if('leaveMessage' === hash){
        userController.leaveMessage();
        //反馈问题
      }else if('mysurvey' === hash){
        userController.survey();
        //我的消息
      }else if('mymessage' === hash){
        userController.mymessage();
        //工作信息
      }else if('JobInformation' ===hash){
        userController.JobInformation();
      }else if(match = hash.match(/JobInformation\/([^\/]+)/)){
        userController.JobInformation(match[1]);
       //联系人
      }else if('ContactPerson' ===hash){
        userController.ContactPerson();
      }else if(match = hash.match(/ContactPerson\/([^\/]+)/)){
        userController.ContactPerson(match[1]);
      //邮箱验证
      }else if('EmailVerification' ===hash){
        userController.EmailVerification();
      }else if(match = hash.match(/EmailVerification\/([^\/]+)/)){
        userController.EmailVerification(match[1]);
      //信用卡信息
      }else if('creditcard' ===hash){
        userController.creditcard();
       //登录
      }else if('sign' === hash){
        homeController.Sign(null,historyStateCalback);
      }else if(match = hash.match(/sign\?fwd=([^&]+)/)){
        homeController.Sign(decodeURIComponent(match[1]),historyStateCalback);
      }
      //找回密码
      else if('findpass' === hash){
        homeController.findpass();
      }
      //找回密码TWo
      else if(match = hash.match(/setpass\=([^&]+)/)){
        homeController.setpass();
      }

      //意见反馈
      else if('survey' === hash){
        homeController.survey();
      }
      //常见问题
      else if('problem' === hash){
        homeController.problem();
      }
      //修改密码
      else if('modify' === hash){
        homeController.modify();
      }
      //修改支付密码
      else if('paymodify' === hash){
        homeController.paymodify();
      }
      //注册
      else if('register' === hash){
        homeController.register();
      }
      //注册协议
      else if('Agreement' === hash){
        homeController.Agreement();
      }
      //我的账单
      else if('MyBill' === hash ){
        homeController.MyBill();
      }
      //账单详情
      else if(match = hash.match(/MyBill\/([^\/]+)/)){
        homeController.BillingBetails(match[1]);
      }
      //发票
      else if ( match = hash.match(/bill\?data=(.+)/) ) {
        orderController.bill(JSON.parse(decodeURIComponent(match[1])));
      }
      //领取奖品
      else if(match = hash.match(/lot_prize\/([^\/]+)/)){
          lotController.prize(match[1]);
      }
      //六一活动页
      else if("lot_product"==hash){
          lotController.product();
      }
      //活动预告页
      else if("trailer"==hash){
          lotController.trailer();
      }
      //分期购买协议
      else if("installmentAgreement"==hash){
          orderController.installmentAgreement();
      }
      //我的收藏
      else if("favorite"==hash){
          favoriteController.favorite();
      //秒杀专题
      }
      else if("seckillList"==hash) {
          SeckillController.seckillList();
      }
      //个人信息
      else if("personmessage"==hash){
          userController.personmessage();
      }
      //我的银行卡
      else if("bankcard"==hash){
          userController.bankcard();
      }
      //填写身份证信息
      else if("idcard"==hash){
          userController.idcard();
      }
      //授信进度
      else if("creditprogress"==hash){
          userController.creditprogress();
      //自提网点
      }
      else if("pickupaddress"==hash){
          pickupController.pickupaddress();
      }
      //搜索
      else if("p_search"==hash){
          searchController.searchPage();
      }
      //列表页
      else if(match = hash.match(/searchlist\/([^\/]+)/)){
        searchListController .searchlist(match[1]);
      }
      //重新加载
      else if('reload' === hash){
        userController.reload(null,historyStateCalback);
      }else if(match = hash.match(/reload\?state=([^&]+)/)){
        userController.reload(decodeURIComponent(match[1]),historyStateCalback);
      }
      // 未匹配路径，到首页
      else {
         history.replaceState({}, '', '/?#home');
         route();
      }


  }


  //if( ('onhashchange' in window) && ((typeof document.documentMode==='undefined') || document.documentMode==8)) {
  //    // 浏览器支持onhashchange事件
  //   // window.onhashchange = hashChangeFire;  // TODO，对应新的hash执行的操作函数
  //} else {
  //    // 不支持则用定时器检测的办法
  //    setInterval(function() {
  //        // 检测hash值或其中某一段是否更改的函数， 在低版本的iE浏览器中通过window.location.hash取出的指和其它的浏览器不同，要注意
  //　　　　var ischanged = isHashChanged();
  //        if(ischanged) {
  //            hashChangeFire();  // TODO，对应新的hash执行的操作函数
  //        }
  //    }, 150);
  //}

  function hashChangeFire(){
    $(this).scrollTop(0);
  }
  var originalAjax = $.ajax;
  $.ajax = function(options) {
    options = options || {};
    options.crossDomain = true;
    options.xhrFields = {
      withCredentials: true
    };
    return originalAjax(options);
  };
    $.fn.renderHtml = $.fn.html;
    $.fn.html = function(content){
       this.renderHtml(content);
        var thisUrl = urlArray[urlArray.length-1];
        if(!isRepeatProduct){
            $(window).scrollTop(0);
        }
    };
  route(true);
  $(window).on('popstate', route);
  $(window).on('route', route);

});

var a = +new Date();

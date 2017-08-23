// 表单验证，基于 jQuery Validation Plugin v1.15.0
var cardnoRule = {
      required: true,
      minlength: 4,
      maxlength: 4
    };
var cardnoMsg = {
      required: '请输入信用卡卡号',
      minlength: '请输入16位信用卡卡号',
      maxlength: '请输入16位信用卡卡号'
    };
$('form').validate({
  onsubmit: true,
  onfocusout: false,
  onkeyup: false,
  onclick: false,
  rules: {
    part1: cardnoRule,
    part2: cardnoRule,
    part3: cardnoRule,
    part4: cardnoRule,
    month: {
      required: true
    },
    year: {
      required: true
    },
    cvv2: {
      required: true
    },
    verifyImage: {
      required: true
    },
    payPassword: {
      required: true,
      minlength: 6
    },
    verifyCode: {
      required: true,
      minlength: 6
    }
  },
  messages: {
    part1: cardnoMsg,
    part2: cardnoMsg,
    part3: cardnoMsg,
    part4: cardnoMsg,
    month: {
      required: '请输入卡片正面的有效期'
    },
    year: {
      required: '请输入卡片正面的有效期'
    },
    cvv2: {
      required: '请输入卡片背面7位数字的后三位'
    },
    verifyImage: {
      required: '请输入验证码'
    },
    payPassword: {
      required: '请输入交易密码',
      minlength: '交易密码至少为6位'
    },
    verifyCode: {
      required: '请输入动态密码',
      minlength: '动态密码必须为6位数字'
    }
  },
  showErrors: function(errorMap, errorList) {
    if (errorList.length > 0) {
      console.log(errorList);
      makeToast(errorList[0].message);
    }
  }
});

// 弹出一个浮层，提示表单验证失败项
function makeToast(msg) {
  $('#toast-wrapper').html('<div class="toast"><p>' + msg + '</p></div>');

  var $toast = $('#toast-wrapper .toast');
  var elementWidth = $toast.width();
  var elementHeight = $toast.height();
  var screenWidth = $(window).width();
  var screenHeight = $(window).height();
  var left = (screenWidth - elementWidth) >> 1;
  var top = (screenHeight - elementHeight) >> 1;
  $toast.css({
    left: left,
    top: top
  }).addClass('show');
}

// 卡号输入4位，自动跳到下一个4位输入框
var $cardnos = $('.cardno-wrapper input');
$cardnos.each(function(index) {
  if (index >= 3) {
    return;
  }

  var len = 0;
  $(this).on('focus', function() {
    len = $(this).val().length;
  });
  $(this).on('keyup', function() {
    var curLen = $(this).val().length;
    if (curLen === 4 && len !== 4) {
      $cardnos[index + 1].focus();
    }
    len = curLen;
  });
});

/*
// 有效期输入完月的2位，自动跳转到年的输入
// 注释，不需要此功能
var len = 0;
$('[name="month"]').on('focus', function() {
  len = $(this).val().length;
});
$('[name="month"]').on('keyup', function() {
  var curLen = $(this).val().length;
  if (curLen === 2 && len !== 2) {
    $('[name="year"]').focus();
  }
  len = curLen;
});
*/

// 动态密码按钮
var verifyCodeText = $('.get-verify-code').text();
$('.get-verify-code').on('click', function() {
  var $this = $(this);
  // 时间间隔未到，不可点击
  if ($this.text() !== verifyCodeText) {
    return;
  }

  var timeLeft = 60;
  $this.text('还剩' + timeLeft + '秒')
  var timer = setInterval(function() {
    if (--timeLeft <= 0) {
      clearInterval(timer);
      $this.text(verifyCodeText);
    }

    $this.text('还剩' + timeLeft + '秒');
  }, 1000);
  // TODO 发送 ajax 请求
});

// 点击图片，或者图片后面的刷新按钮，刷新图片验证码
$('.refresh-verify-image, .image-wrapper img').on('click', function() {
    $(this).attr('src', './img/verify.png?_=' + (+new Date()));
});

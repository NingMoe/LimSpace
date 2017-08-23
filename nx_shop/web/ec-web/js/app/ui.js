define([
  'underscore',
  'zepto',
  'layer',
  'config'
], function(_, $, layer, config) {
// 弹出一个浮层，提示表单验证失败项
  function makeToast(msg) {
    layer.open({
    content: msg,
    style: 'background:rgba(2, 2, 2, 0.58);color:#fff;border:none;',
    time:2,
    shade:false
   });
  }
  
  function Toastloading(msg) {
    if(msg){
       layer.open({
        type: 2,
        shadeClose:false,
        success:function(){
         var setmout = setTimeout(function(){
            layer.closeAll();
            clearInterval(setmout);
            makeToast("加载异常，请稍后再试！")
          },60000)
        }
      });
    }else{
      layer.closeAll();
    }
  }

  function validate($form) {
    var reMobile = /^[1][0-9]{10}$/;
    var reCode = /^[0-9]{6}$/;
    var reidcard = /^[1-9]{1}[0-9]{14}$|^[1-9]{1}[0-9]{16}([0-9]|[xX])$/;
    var $inputs;
    var $input;
    var title;
    var minlength;
    var maxlength;
    var val;
    var pattern;

    // 首先检查 required 规则
    $inputs = $form.find('input').not('input[optional]');
    for (var i = 0, len = $inputs.length; i < len; i++) {
      $input = $inputs.eq(i);
      if ($input.val() === '') {
        makeToast($input.attr('placeholder'));
        //$input.focus();
        return false;
      }
    }

    // 检查手机号规则
    $inputs = $form.find('input[data-validate-mobile]').not('input[optional]');
    for (var i = 0, len = $inputs.length; i < len; i++) {
      $input = $inputs.eq(i);
      val = $input.val();
      if (!reMobile.test(val)) {
        makeToast('请输入正确的手机号');
        //$input.focus();
        return false;
      }
    }


    // 检查 身份证 规则
    $inputs = $form.find('input[name="idCard"]').not('input[optional]');
    for (var i = 0, len = $inputs.length; i < len; i++) {
      $input = $inputs.eq(i);
      val = $input.val();
      if (!reidcard.test(val)) {
        makeToast('请输入正确的身份证');
        //$input.focus();
        return false;
      }
    }

    // 检查 验证码 规则
    $inputs = $form.find('input[name="checkcode"]').not('input[optional]');
    for (var i = 0, len = $inputs.length; i < len; i++) {
      $input = $inputs.eq(i);
      val = $input.val();
      if (!reCode.test(val)) {
        makeToast('请输入正确的验证码');
        //$input.focus();
        return false;
      }
    }

    // 检查 密码 规则
    $inputs = $form.find('input[type="password"]').not('input[optional]');
    for (var i = 0, len = $inputs.length; i < len; i++) {
      $input = $inputs.eq(i);
      val = $input.val();
      if (val.length < config.password.minlength) {
        makeToast('密码至少为' + config.password.minlength + '位');
        //$input.focus();
        return false;
      } else if (val.length > config.password.maxlength) {
        makeToast('密码至多为' + config.password.maxlength + '位');
        //$input.focus();
        return false;
      }
    }

    // 检查 minlength 规则
    $inputs = $form.find('input[minlength]').not('input[optional]');
    for (var i = 0, len = $inputs.length; i < len; i++) {
      $input = $inputs.eq(i);
      title = $input.data('title');
      minlength = $input.attr('minlength');
      if ($input.val().length < minlength) {
        makeToast(title + '至少为' + minlength + '位');
        //$input.focus();
        return false;
      }
    }

    // 检查 maxlength 规则
    $inputs = $form.find('input[maxlength]').not('input[optional]');
    for (var i = 0, len = $inputs.length; i < len; i++) {
      $input = $inputs.eq(i);
      title = $input.data('title');
      maxlength = $input.attr('maxlength');
      if ($input.val().length > maxlength) {
        makeToast(title + '至多为' + maxlength + '位');
        //$input.focus();
        return false;
      }
    }


    // 检查 pattern 规则
    $inputs = $form.find('input[pattern]').not('input[optional]');
    for (var i = 0, len = $inputs.length; i < len; i++) {
      $input = $inputs.eq(i);
      val = $input.val();
      pattern = $input.attr('pattern');
      title = $input.data('title');
      if (!new RegExp(pattern).test(val)) {
        makeToast('请输入正确' + title);
        //$input.focus();
        return false;
      }
    }

    return true;
  }

  return {
    makeToast: makeToast,
    validate: validate,
    Toastloading:Toastloading
  };
});



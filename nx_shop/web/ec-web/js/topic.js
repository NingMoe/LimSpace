// 覆盖默认的 ajax 实现，允许跨域
var originalAjax = $.ajax;
$.ajax = function(options) {
    options = options || {};
    options.crossDomain = true;
    options.xhrFields = {
        withCredentials: true
    };
    return originalAjax(options);
};

var URL = '/api/';

// 优惠券功能
$('.js-coupon').on('click', function(event) {
    var $this = $(this);
    var couponId = $this.data('couponid');
    $.ajax({
        url: URL + 'coupons/' + couponId + '/receive',
        success: function(data) {
            if (data.message == '领取成功') {
                alert('恭喜您，领取成功！');
            } else if (data.message == '领取失败') {
                alert(data.message + "，" + data.exception);
            }
        },
        // 未登录跳转到登录页面
        error: function(res){
            if (res.status == 401) {
                if (location.host.match(/cebshop/)) {
                    location.href = '/#connects?fwd='+ encodeURIComponent(location.href);
                } else {
                    location.href = '/#sign?fwd='+ encodeURIComponent(location.href);
                }
            }
        }
    });
    return false;
});

// 活动开始时间
var seq = 1;
var times = [];
var time;
while (time = $('body').data('time' + seq)) {
    times.push(parseTime(time));
    seq++;
}

var active = true;
if (times[0]) {
    var now = +new Date();
    if (now < times[0][0]) {
        active = false;
        alert($('body').data('before'));
    } else if (now > times[times.length - 1][1]) {
        active = false;
        alert($('body').data('after'));
    // 如果是活动期间，但不在每一个小的时间段内
    } else {
        var found = false;
        for (var i = times.length - 1; i > 0; i--) {
            if (now < times[i][0] && now > times[i - 1][1]) {
                found = true;
                active = false;
                break;
            }
        }
        if (found) {
            // 8月3日9:00
            var d = new Date(times[i][0]);
            var minutes = d.getMinutes();
            minutes = minutes < 10 ? '0' + minutes : minutes;
            var nextTime = (d.getMonth() + 1) + '月' + d.getDate() + '日' + d.getHours() + ':' + minutes;
            alert($('body').data('break').replace('%next%', nextTime));
        }
    }
}
if (!active) {
    $('.js-clickable-when-active').attr('href', 'javascript:;');
}

function parseTime(data) {
    var match = data.match(/(\d{4,4})-(\d{2,2})-(\d{2,2}) (\d{2,2}):(\d{2,2}):(\d{2,2}) - (\d{4,4})-(\d{2,2})-(\d{2,2}) (\d{2,2}):(\d{2,2}):(\d{2,2})/);
    if (match != null) {
        var t0 = new Date(match[1], parseInt(match[2], 10) - 1, parseInt(match[3], 10), parseInt(match[4], 10), parseInt(match[5], 10), parseInt(match[6], 10));
        var t1 = new Date(match[7], parseInt(match[8], 10) - 1, parseInt(match[9], 10), parseInt(match[10], 10), parseInt(match[11], 10), parseInt(match[12], 10));
        return [+t0, +t1];
    }
}


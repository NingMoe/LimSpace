define([], function() {
  var sys = 'ceb'; // ceb thb tyfq
  var installment = 1;
  var hasInstallmentInterest = 0; // 是否有分期利息
  var origin = '';
  var devOriginMap = {
    ceb: 'http://test.cebshop.tyiti.com',
    thb: 'http://dev.taohuibang.com',
    tyfq: 'http://dev2.fenxinbao.com',
    fxb: 'http://test.tianyaofenqi.com',
    pingan: 'http://dev3.pingan.tyiti.com'
  };
  if (location.host.split('.')[0].indexOf('localhost') === 0 || (location.port != '80' && location.port != '')) {
    origin = devOriginMap[sys];
    // 本地调试用
     // origin = 'http://localhost:8080'; // 王文明
    // origin = 'http://192.168.23.123'; // 王文明
//  origin = 'http://192.168.50.139:8080';
  } else {
    origin = location.origin;
  }
  // origin = 'http://dev.fenxinbao.com';

    //解决分信宝前端config
  if (sys == 'fxb') {
    sys = 'tyfq';
  }
  var config = {
    sys: sys,
    installment: installment,
    hasInstallmentInterest: hasInstallmentInterest,
    imgHost: 'http://tyiti.img-cn-beijing.aliyuncs.com',
    host: origin,
    napi: origin + '/api/',
    //napi: 'http://192.168.50.247/api/',
    password: {
      minlength: 6,
      maxlength: 16
    }
  };

  return config;
});

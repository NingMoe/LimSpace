-- -------------Start t_config 初始化数据---------------
INSERT INTO `t_config` VALUES ('1', 'weixin_token', '6C3693CDDA074687B626262CD3A4E46E', null);
INSERT INTO `t_config` VALUES ('2', 'weixin_appid', 'wx7d8243e6f9a5f870', null);
INSERT INTO `t_config` VALUES ('3', 'weixin_appsecret', '4c84550057d7fb293561f7097eceef3a', null);
INSERT INTO `t_config` VALUES ('4', 'weixin_redirect_url_prefix', 'http://test.cebshop.tyiti.com/?', null);
INSERT INTO `t_config` VALUES ('5', 'fee_amt_rate_1', '0.004', '不分期的商户佣金');
INSERT INTO `t_config` VALUES ('6', 'fee_amt_rate_3', '0.02', '3期的商户佣金');
INSERT INTO `t_config` VALUES ('7', 'fee_amt_rate_6', '0.035', '6期的商户佣金');
INSERT INTO `t_config` VALUES ('8', 'fee_amt_rate_12', '0.065', '12期的商户佣金');
INSERT INTO `t_config` VALUES ('9', 'origin', 'http://test.cebshop.tyiti.com/', 'origin');
INSERT INTO `t_config` VALUES ('10', 'koo_request_url', 'http://pay.neibu.koo.cn/tyorder/orderPush', null);
INSERT INTO `t_config` VALUES ('11', 'koo_request_key', 'hducJDOwfYsV2pWgB4mGnAXxkI8jKd3e5', null);
INSERT INTO `t_config` VALUES ('12', 'weixin_partner_key', 'https://api.mch.weixin.qq.com/pay/orderquery', null);
INSERT INTO `t_config` VALUES ('13', 'sku_warning_all', '45', '全部库存预警');
INSERT INTO `t_config` VALUES ('14', 'sms_url', 'http://cf.lmobile.cn/submitdata/service.asmx/g_Submit', '短信服务请求地址');
INSERT INTO `t_config` VALUES ('15', 'sms_sname', 'dlgogg00', '账户名');
INSERT INTO `t_config` VALUES ('16', 'sms_spwd', 'Lnk58Dgv', '密码');
INSERT INTO `t_config` VALUES ('17', 'sms_scorpid', null, 'SCORPID');
INSERT INTO `t_config` VALUES ('18', 'sms_sprdid', '1012818', '账户ID');
INSERT INTO `t_config` VALUES ('19', 'sms_tail', '【e惠聚购】', '短信签名');
INSERT INTO `t_config` VALUES ('20', 'score_ratio', '100', '兑换比率');
INSERT INTO `t_config` VALUES ('21', 'score_consume_quota', '100000', '设定商城积分总预算（元）');
INSERT INTO `t_config` VALUES ('22', 'score_quota', '100000000', '积分总量（元）');
-- -------------End t_config 初始化数据---------------
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `activity_type` tinyint(4) DEFAULT NULL COMMENT '活动类型  1：秒杀；2：限购；3：第二份半价；4：团购;',
  `activity_mode` tinyint(2) DEFAULT NULL COMMENT '活动支持方式，1：活动期间所有订单总和限购指定数量，2：活动期间每笔订单限购指定数量',
  `activity_name` varchar(255) DEFAULT NULL COMMENT '活动名称',
  `start_time` datetime DEFAULT NULL COMMENT '活动开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '活动结束时间',
  `activity_remarks` varchar(255) DEFAULT NULL COMMENT '活动备注',
  `activity_status` tinyint(2) DEFAULT '0' COMMENT '活动状态，0：未开始，1：进行中，2：已结束',
  `invalid` tinyint(2) DEFAULT '0' COMMENT '活动是否已删除 0：未删除，1：已删除',
  `coupon` varchar(11) DEFAULT NULL COMMENT '活动支持类型，1：优惠券，2：代金券',
  `succeed` tinyint(2) DEFAULT '0' COMMENT '活动是否创建成功：0 失败 1 成功',
  `display` tinyint(2) DEFAULT '0' COMMENT '活动是否显示在前端：0 显示 1 隐藏',
  `pic_url` varchar(1024) DEFAULT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='通用限购活动表';

-- ----------------------------
-- Table structure for t_activity_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_sku`;
CREATE TABLE `t_activity_sku` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) NOT NULL COMMENT '对应活动表t_activity中的id',
  `sku_id` int(11) NOT NULL COMMENT '对应sku表中的id',
  `sku_name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `price` decimal(14,2) DEFAULT '0.00' COMMENT '商品原价',
  `activity_price` decimal(14,2) DEFAULT '0.00' COMMENT '活动价格',
  `inventory` int(11) DEFAULT NULL COMMENT '预留总库存',
  `discounted_price` decimal(14,2) DEFAULT '0.00' COMMENT '优惠金额',
  `discount` decimal(14,2) DEFAULT NULL COMMENT '优惠折扣',
  `reserved_inventory` int(11) DEFAULT NULL COMMENT '剩余预留库存',
  `purchase_num` int(11) DEFAULT NULL COMMENT '限购个数',
  `sold_num` int(11) DEFAULT '0' COMMENT '已售个数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `top_rank` int(11) DEFAULT '0' COMMENT '活动商品排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='通用限购活动产品表';

-- ----------------------------
-- Table structure for t_address
-- ----------------------------
DROP TABLE IF EXISTS `t_address`;
CREATE TABLE `t_address` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `cust_id` int(11) NOT NULL COMMENT '客户ID',
  `username` char(32) DEFAULT NULL COMMENT '收货人姓名',
  `mobile` char(11) NOT NULL COMMENT '收货人手机号',
  `state_id` int(11) NOT NULL COMMENT '省ID',
  `city_id` int(11) NOT NULL COMMENT '市ID',
  `county_id` int(11) NOT NULL COMMENT '县ID',
  `street` varchar(128) DEFAULT NULL COMMENT '街道到门牌号',
  `zip` int(6) DEFAULT NULL COMMENT '邮政编码',
  `full_text` varchar(256) NOT NULL COMMENT '文字版的完整地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认地址',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除',
  `flag` tinyint(1) DEFAULT '0' COMMENT '0:手动添加，1：集团用户地址',
  PRIMARY KEY (`id`),
  KEY `idx_cust_id` (`cust_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_advert
-- ----------------------------
DROP TABLE IF EXISTS `t_advert`;
CREATE TABLE `t_advert` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `code` varchar(255) DEFAULT NULL COMMENT '编码',
  `group` varchar(255) DEFAULT NULL COMMENT '组编码',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '0不删除 1删除',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='广告位表';

-- ----------------------------
-- Table structure for t_appraise
-- ----------------------------
DROP TABLE IF EXISTS `t_appraise`;
CREATE TABLE `t_appraise` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `level` int(11) DEFAULT NULL COMMENT '评论星级 0-5',
  `content` varchar(1024) DEFAULT NULL COMMENT '评论内容',
  `pic_url` varchar(1024) DEFAULT NULL COMMENT '图片url，多张用空格分割',
  `sku_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户评价';

-- ----------------------------
-- Table structure for t_attribute
-- ----------------------------
DROP TABLE IF EXISTS `t_attribute`;
CREATE TABLE `t_attribute` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '属性名称',
  `value` varchar(2000) DEFAULT NULL COMMENT '所有值 多个用,分隔',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除。有关联 SPU 的不允许删除。',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_balance_statistics
-- ----------------------------
DROP TABLE IF EXISTS `t_balance_statistics`;
CREATE TABLE `t_balance_statistics` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `referrer_id` int(11) DEFAULT NULL COMMENT '推荐人ID',
  `balance_date` datetime DEFAULT NULL COMMENT '结算时间',
  `daybook` decimal(11,2) DEFAULT NULL COMMENT '当月流水。每月的下单商品总金额',
  `commission` decimal(11,2) DEFAULT NULL COMMENT '佣金。流水*反润比率',
  `state` char(2) DEFAULT NULL COMMENT '状态 A0:未出账 A1:已出账',
  `remarks` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='结算统计表';

-- ----------------------------
-- Table structure for t_bank
-- ----------------------------
DROP TABLE IF EXISTS `t_bank`;
CREATE TABLE `t_bank` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(1024) NOT NULL COMMENT '银行名称',
  `state_code` varchar(32) NOT NULL COMMENT '省code',
  `city_code` varchar(32) NOT NULL COMMENT '市code',
  `county_code` varchar(32) NOT NULL COMMENT '县code',
  `address_full_text` varchar(256) NOT NULL COMMENT '详细地址',
  `bank_account_no` varchar(32) DEFAULT NULL COMMENT '对公银行账户',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='银行表';

-- ----------------------------
-- Table structure for t_bank_card_info
-- ----------------------------
DROP TABLE IF EXISTS `t_bank_card_info`;
CREATE TABLE `t_bank_card_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `name` varchar(20) DEFAULT NULL COMMENT '持卡人',
  `card_no` varchar(20) DEFAULT NULL COMMENT '卡号',
  `card_name` varchar(64) DEFAULT NULL COMMENT '卡名',
  `tel` varchar(11) DEFAULT NULL COMMENT '预留手机号',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型: 1:储蓄卡 2:信用卡',
  `binding_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0: 已绑定，1: 已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='银行卡信息表';

-- ----------------------------
-- Table structure for t_bank_recommend_merchant
-- ----------------------------
DROP TABLE IF EXISTS `t_bank_recommend_merchant`;
CREATE TABLE `t_bank_recommend_merchant` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `bank_id` int(11) NOT NULL COMMENT '所属银行id',
  `merchant_id` int(11) NOT NULL COMMENT '商户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='银行商户推荐';

-- ----------------------------
-- Table structure for t_banner
-- ----------------------------
DROP TABLE IF EXISTS `t_banner`;
CREATE TABLE `t_banner` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `pic_url` varchar(1024) NOT NULL COMMENT '图片路径',
  `url` varchar(1024) NOT NULL COMMENT '跳转地址',
  `rank` int(11) DEFAULT '1' COMMENT '排序',
  `title` varchar(45) DEFAULT NULL COMMENT '标题',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `ad_id` int(11) DEFAULT NULL COMMENT '广告位id ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='首页轮播图表';

-- ----------------------------
-- Table structure for t_bill_record
-- ----------------------------
DROP TABLE IF EXISTS `t_bill_record`;
CREATE TABLE `t_bill_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `bill_id` varchar(64) DEFAULT NULL COMMENT '分期订单ID',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '订单流水号',
  `pay_state` varchar(10) DEFAULT NULL COMMENT '支付状态 A1: 未支付(默认) A2: 已支付 A3: 申请退款 A4: 已退款',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付完成时间，微信: 未支付成功: 无数据库记录，已支付: 支付完成时间(time_end)',
  `pay_no` varchar(64) DEFAULT NULL COMMENT '银行网上支付平台流水号，微信：微信支付订单号(transaction_id)',
  `acc_no` varchar(64) DEFAULT NULL COMMENT '支付银行，微信: 付款银行(bank_type)',
  `cst_name` varchar(128) DEFAULT NULL COMMENT '持卡人姓名，微信: NULL',
  `openid` varchar(255) DEFAULT NULL COMMENT '微信: 微信 openid',
  `is_subscribe` int(1) DEFAULT NULL COMMENT '微信: 是否关注公众账号(is_subscribe)',
  `ip` varchar(20) DEFAULT NULL COMMENT '客户支付ip地址，微信: NULL',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refund_time` timestamp NULL DEFAULT NULL COMMENT '账单退款时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分期还款账单流水表';

-- ----------------------------
-- Table structure for t_cart
-- ----------------------------
DROP TABLE IF EXISTS `t_cart`;
CREATE TABLE `t_cart` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `sku_id` int(11) NOT NULL COMMENT '商品 id ',
  `count` int(11) NOT NULL COMMENT '数量',
  `installment_months` int(11) DEFAULT NULL COMMENT '分期数',
  `status` int(1) NOT NULL COMMENT '状态。0 不结算(不选中) 1 待结算(选中) 2 已结算(转为订单)',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='购物车';

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '分类名',
  `parent_id` int(11) unsigned DEFAULT NULL COMMENT '父级分类ID，一级分类为NULL',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有效，默认有效',
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT '排序值，越大排序越靠后',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `meta_description` text COMMENT 'SEO description',
  `meta_keywords` text COMMENT 'SEO keywords',
  `meta_title` varchar(255) DEFAULT NULL COMMENT '标题',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态 1已启用 0未启用',
  `icon` varchar(128) DEFAULT NULL COMMENT '图标',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供货商ID',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_rank` (`rank`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_category_template
-- ----------------------------
DROP TABLE IF EXISTS `t_category_template`;
CREATE TABLE `t_category_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_id` int(11) DEFAULT NULL COMMENT '分类id',
  `type` tinyint(1) NOT NULL COMMENT '类别 1规格norm 2属性attribute',
  `key_id` int(11) DEFAULT NULL COMMENT '规格或属性id',
  `value` varchar(256) DEFAULT NULL COMMENT '规格或属性值，多个用,分隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='分类规格';

-- ----------------------------
-- Table structure for t_company_info
-- ----------------------------
DROP TABLE IF EXISTS `t_company_info`;
CREATE TABLE `t_company_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(32) DEFAULT NULL COMMENT '编号',
  `name` varchar(64) DEFAULT NULL COMMENT '公司名称',
  `company_rate` decimal(8,5) DEFAULT NULL COMMENT '公司总反润比率',
  `alias` varchar(64) DEFAULT NULL COMMENT '公司别名 比如：天尧',
  `prefix` char(2) DEFAULT NULL COMMENT '公司首字母',
  `filter` varchar(128) DEFAULT NULL COMMENT '公司筛选,用于like查询，输入名称过滤此字段即可',
  `address` varchar(128) DEFAULT NULL COMMENT '公司地址',
  `company_nature` char(2) DEFAULT NULL COMMENT '职业性质 A1:政府，公务员 A2:民营，集体企业 A3:国企，事务所 A4:机关事业单位 A5:学校，教育机构 A6:外资，合资企业 A7:个体工商户',
  `staff_number` int(11) DEFAULT NULL COMMENT '公司人数',
  `email_suf` varchar(32) DEFAULT NULL COMMENT '邮箱后缀',
  `tel_number` varchar(20) DEFAULT NULL COMMENT '公司电话',
  `state` int(2) DEFAULT NULL COMMENT '标识公司是否启用 0:启用 1:停用',
  `email_state` int(2) DEFAULT NULL COMMENT '是否启用邮箱验证 0:启用 1:停用',
  `abook_state` int(2) DEFAULT NULL COMMENT '是否启用通讯录验证 0:启用 1:停用',
  `create_userid` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_userid` int(11) DEFAULT NULL COMMENT '维护人ID',
  `modify_time` datetime DEFAULT NULL COMMENT '维护时间',
  `remarks` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公司信息表';

-- ----------------------------
-- Table structure for t_config
-- ----------------------------
DROP TABLE IF EXISTS `t_config`;
CREATE TABLE `t_config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `key` varchar(64) NOT NULL COMMENT '配置的key',
  `value` varchar(1024) DEFAULT NULL COMMENT '配置的value',
  `remark` varchar(1024) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_config_installment_rate
-- ----------------------------
DROP TABLE IF EXISTS `t_config_installment_rate`;
CREATE TABLE `t_config_installment_rate` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `months` int(11) NOT NULL COMMENT '期数',
  `rate` double NOT NULL COMMENT '费率',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='分期费率配置';

-- ----------------------------
-- Table structure for t_coupon
-- ----------------------------
DROP TABLE IF EXISTS `t_coupon`;
CREATE TABLE `t_coupon` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '优惠券名称',
  `count` int(11) NOT NULL DEFAULT '0' COMMENT '发放优惠券数量',
  `summary` varchar(2048) DEFAULT NULL COMMENT '优惠使用说明',
  `type` int(8) DEFAULT NULL COMMENT '1.优惠券 2 优惠码 3 代金券 4 代金码',
  `scope` int(1) DEFAULT NULL COMMENT '适用范围。1: 全场通用 2: SKU 3: Tag 标签',
  `ref_id` int(11) DEFAULT NULL COMMENT '标签/SKU id',
  `prefix` char(2) DEFAULT NULL COMMENT '优惠码前缀(两位)',
  `receive_start_time` datetime NOT NULL COMMENT '可以开始领取的时间',
  `receive_end_time` datetime NOT NULL COMMENT '领取终止时间',
  `time_type` int(1) NOT NULL COMMENT '有效期类型。1: 绝对时间(expire_time) 2: 相对时间(expire_in_days)',
  `start_time` datetime DEFAULT NULL COMMENT '有效期开始时间。如果是相对时间，此字段为 NULL',
  `expire_time` datetime DEFAULT NULL COMMENT 'time_type = 1 时有意义，有效期结束时间',
  `expire_in_days` int(11) DEFAULT NULL COMMENT 'time_type = 2 时有意义，有效天数（从领取时间开始算）',
  `threshold` double DEFAULT NULL COMMENT '优惠起始金额',
  `discount` double NOT NULL COMMENT '满减优惠金额',
  `receive_sms` varchar(256) DEFAULT NULL COMMENT '收到优惠券短信提醒内容',
  `due_sms` varchar(256) DEFAULT NULL COMMENT '优惠券即将过期短信提醒内容',
  `is_due` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0: 未过期 1: 过期(定时任务)',
  `received_num` int(11) NOT NULL DEFAULT '0' COMMENT '已领取数量(前端维护)',
  `used_num` int(11) NOT NULL DEFAULT '0' COMMENT '已使用数量(前端维护)',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除。 0: 未删除 1: 已删除',
  `stop` tinyint(1) DEFAULT '0' COMMENT '0:未结束 1：结束',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='优惠券规则表';

-- ----------------------------
-- Table structure for t_coupon_record
-- ----------------------------
DROP TABLE IF EXISTS `t_coupon_record`;
CREATE TABLE `t_coupon_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `coupon_id` int(11) NOT NULL COMMENT '优惠券ID',
  `cust_id` int(11) DEFAULT NULL COMMENT '用户ID，优惠码未分配时，cust_id为NULL',
  `coupon_code` varchar(256) DEFAULT NULL COMMENT '优惠码',
  `receive_time` datetime DEFAULT NULL COMMENT '用户收到优惠券时间',
  `is_used` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否使用 0：未使用；1：已使用',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `expire_time` datetime DEFAULT NULL COMMENT '到期时间(领取时维护、发放时维护)',
  `is_due` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0 未过期 1 已过期',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0：未删除；1：已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='优惠券记录表';

-- ----------------------------
-- Table structure for t_coupon_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_coupon_sku`;
CREATE TABLE `t_coupon_sku` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `coupon_id` int(11) NOT NULL COMMENT '优惠券id',
  `sku_id` int(11) NOT NULL COMMENT 'sku id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='优惠券sku中间表';

-- ----------------------------
-- Table structure for t_data_area
-- ----------------------------
DROP TABLE IF EXISTS `t_data_area`;
CREATE TABLE `t_data_area` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) DEFAULT NULL COMMENT '上级行政单位ID，省的为 NULL',
  `level` tinyint(1) NOT NULL COMMENT '1: 省、直辖市、自治区 2: 地级市 3: 县',
  `name` varchar(1024) NOT NULL COMMENT '行政区划名字',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_data_order_no_mapping
-- ----------------------------
DROP TABLE IF EXISTS `t_data_order_no_mapping`;
CREATE TABLE `t_data_order_no_mapping` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `source` char(3) NOT NULL COMMENT '转换前的数字',
  `target` char(3) NOT NULL COMMENT '转换后的数字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_express_info
-- ----------------------------
DROP TABLE IF EXISTS `t_express_info`;
CREATE TABLE `t_express_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `order_id` int(11) DEFAULT NULL COMMENT '订单id',
  `message` varchar(64) DEFAULT NULL COMMENT '消息体',
  `com` varchar(32) DEFAULT NULL COMMENT '快递公司(编码)',
  `nu` varchar(64) DEFAULT NULL COMMENT '运单号:物流公司的运单号',
  `is_check` tinyint(1) DEFAULT NULL COMMENT '是否签收: 0 否,1 是(快递100已弃用此字段)',
  `state` int(11) DEFAULT NULL COMMENT '快递单当前签收状态，包括0在途中、1已揽收、2疑难、3已签收、4退签、5同城派送中、6退回、7转单',
  `data` text COMMENT '路由信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='快递100返回信息';

-- ----------------------------
-- Table structure for t_express_log
-- ----------------------------
DROP TABLE IF EXISTS `t_express_log`;
CREATE TABLE `t_express_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `com` varchar(32) DEFAULT NULL COMMENT '物流公司',
  `num` varchar(128) DEFAULT NULL COMMENT '运单号:也就是物流单号',
  `is_data` tinyint(1) DEFAULT NULL COMMENT '是否有返回数据:0 没有返回, 1 有返回数据 ',
  `data` varchar(5000) DEFAULT NULL COMMENT '快递100返回回来的数据',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间 快递单号查询的时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='快递查询日志';

-- ----------------------------
-- Table structure for t_faq
-- ----------------------------
DROP TABLE IF EXISTS `t_faq`;
CREATE TABLE `t_faq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question` text COMMENT '问题',
  `answer` text COMMENT '答案',
  `manager_id` int(11) DEFAULT NULL COMMENT '管理员id',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='常见问题表';

-- ----------------------------
-- Table structure for t_favorite
-- ----------------------------
DROP TABLE IF EXISTS `t_favorite`;
CREATE TABLE `t_favorite` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sku_id` int(11) NOT NULL COMMENT '商品id（FK t_sku.id）',
  `user_id` int(11) NOT NULL COMMENT '用户id（FK t_user.id）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '用户是否取消收藏：1未取消，0取消，默认为1',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='商品收藏表';

-- ----------------------------
-- Table structure for t_headline
-- ----------------------------
DROP TABLE IF EXISTS `t_headline`;
CREATE TABLE `t_headline` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '头条标题',
  `link` varchar(255) DEFAULT NULL COMMENT '活动链接',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `rank` int(11) DEFAULT NULL COMMENT '排序(值越小越靠前)',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '0: 有效，1: 无效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='头条';

-- ----------------------------
-- Table structure for t_history_search
-- ----------------------------
DROP TABLE IF EXISTS `t_history_search`;
CREATE TABLE `t_history_search` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `query_text` varchar(45) NOT NULL COMMENT '用户搜索的内容',
  `query_times` int(11) NOT NULL DEFAULT '1' COMMENT '用户查询的次数',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID（如果未登录则为null）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否被用户删除：1否，0是，默认为1',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次的查询时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户历史搜索表';

-- ----------------------------
-- Table structure for t_koo_category_map
-- ----------------------------
DROP TABLE IF EXISTS `t_koo_category_map`;
CREATE TABLE `t_koo_category_map` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `koo_category_id` int(11) DEFAULT NULL COMMENT '新东方分类ID',
  `category_id` int(11) DEFAULT NULL COMMENT '分类ID',
  `tag_id` int(11) DEFAULT NULL COMMENT '标签ID',
  `creat_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='新东方分类对应表';

-- ----------------------------
-- Table structure for t_koo_course
-- ----------------------------
DROP TABLE IF EXISTS `t_koo_course`;
CREATE TABLE `t_koo_course` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `koo_id` int(11) NOT NULL COMMENT '新东方课程ID',
  `koo_product_id` varchar(11) NOT NULL COMMENT '新东方课程号',
  `koo_parent_id` int(11) DEFAULT NULL COMMENT '新东方课程父级ID',
  `name` varchar(200) DEFAULT NULL COMMENT '课程名称',
  `hour_length` varchar(50) DEFAULT NULL COMMENT '课时',
  `course_teacher` varchar(200) DEFAULT NULL COMMENT '授课老师',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除 0：未删除；1：已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='新东方课程';

-- ----------------------------
-- Table structure for t_koo_push_log
-- ----------------------------
DROP TABLE IF EXISTS `t_koo_push_log`;
CREATE TABLE `t_koo_push_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `user_id` int(11) NOT NULL COMMENT '当前登录用户的ID',
  `push_str` text COMMENT '推送用户字符串',
  `push_time` datetime DEFAULT NULL COMMENT '推送时间',
  `is_success` tinyint(1) DEFAULT '0' COMMENT '是否退送成功：0是不成功，1是成功',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='调用新东方下单接口记录';

-- ----------------------------
-- Table structure for t_leave_message
-- ----------------------------
DROP TABLE IF EXISTS `t_leave_message`;
CREATE TABLE `t_leave_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `content` text COMMENT '留言内容',
  `type_id` int(11) DEFAULT NULL COMMENT '反馈意见类型（fk：t_leave_message_type.id）',
  `cust_id` int(11) DEFAULT NULL COMMENT '用户id',
  `status` int(11) DEFAULT NULL COMMENT '留言状态 1：提交 2：已回复 0：已删除',
  `cust_status` int(11) DEFAULT '1' COMMENT '用户状态：1:Active，0:Deleted，默认为1',
  `manager_id` int(11) DEFAULT NULL COMMENT '管理员id',
  `leave_time` datetime DEFAULT NULL COMMENT '留言时间',
  `reply_content` text COMMENT '意见回复',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `is_read` int(11) DEFAULT '0' COMMENT '用户是否已读 0：未读 1：已读 默认0',
  `leave_ip` varchar(255) DEFAULT NULL COMMENT '留言ip',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户留言表';

-- ----------------------------
-- Table structure for t_leave_message_type
-- ----------------------------
DROP TABLE IF EXISTS `t_leave_message_type`;
CREATE TABLE `t_leave_message_type` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '类型名称',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：1:Active，0:Deleted，默认为1',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='反馈意见类型';

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `operator_id` int(11) NOT NULL COMMENT '操作人id',
  `operator_name` varchar(60) NOT NULL COMMENT '操作人用户名',
  `key_id` int(11) NOT NULL COMMENT '操作数据id',
  `create_time` datetime NOT NULL COMMENT '操作时间',
  `ip` varchar(32) NOT NULL COMMENT 'ip地址',
  `operate_model` int(11) DEFAULT NULL COMMENT '操作模块 1 ：订单 2：退货 3：退款',
  `action` int(11) NOT NULL COMMENT '操作类型1:提交订单:2.微信支付3：生成分期4：确认制单5：制单6：发货7：用户签收 8：取消 9：申请退货10：确定退货11：拒绝退货12：收货13：退款',
  `message` varchar(600) NOT NULL COMMENT '操作内容',
  `source` tinyint(1) NOT NULL COMMENT '1.商城服务 2：商城管理后台',
  `success_status` tinyint(1) NOT NULL COMMENT '成功状态 0 ：不成功 1 ：成功',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='系统操作日志（包括订单操作）';

-- ----------------------------
-- Table structure for t_member_level
-- ----------------------------
DROP TABLE IF EXISTS `t_member_level`;
CREATE TABLE `t_member_level` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(1024) NOT NULL COMMENT '名称',
  `min_score` int(11) NOT NULL COMMENT '最低积分(>=)',
  `max_score` int(11) NOT NULL COMMENT '最高积分(<)',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 {0: 正常, 1: 停用}',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除 0：未删除，1：已删除',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员等级';

-- ----------------------------
-- Table structure for t_merchant
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant`;
CREATE TABLE `t_merchant` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_category_id` int(11) NOT NULL COMMENT '所属分类id',
  `bank_id` int(11) NOT NULL COMMENT '所属商圈，即商户所属银行id',
  `name` varchar(1024) NOT NULL COMMENT '商铺名称',
  `contact_name` varchar(32) NOT NULL COMMENT '联系人',
  `contact_tel` varchar(32) NOT NULL COMMENT '联系电话',
  `address_full_text` varchar(256) NOT NULL COMMENT '详细地址',
  `pic_url` varchar(100) DEFAULT NULL COMMENT '封面图片',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '商铺评分',
  `details` text COMMENT '店铺详细',
  `is_wifi` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'wifi 0: 无 1: 有',
  `is_parking` tinyint(1) NOT NULL DEFAULT '0' COMMENT '停车 0: 无 1: 有',
  `business_start_time` datetime DEFAULT NULL COMMENT '营业开始时间',
  `business_end_time` datetime DEFAULT NULL COMMENT '营业结束时间',
  `other` varchar(256) DEFAULT NULL COMMENT '其他',
  `delivery_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '配送方式 0: 免费配送 1: 最低消费',
  `limit_price` decimal(14,2) DEFAULT NULL COMMENT '满免最低价格',
  `delivery_fee` decimal(14,2) DEFAULT NULL COMMENT '固定配送费',
  `account_num` varchar(128) DEFAULT NULL COMMENT '账号',
  `account_name` varchar(128) DEFAULT NULL COMMENT '账户名',
  `opening_bank` varchar(128) DEFAULT NULL COMMENT '开户行',
  `average_spend` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '人均消费',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 {0: 待审核, 1: 正常, 2: 停用}',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户表';

-- ----------------------------
-- Table structure for t_merchant_category
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_category`;
CREATE TABLE `t_merchant_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '分类名',
  `parent_id` int(11) unsigned DEFAULT NULL COMMENT '父级分类ID，一级分类为NULL',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否已删除 0：未删除，1：已删除',
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT '排序值，越大排序越靠后',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx_name` (`name`),
  KEY `idx_rank` (`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_merchant_recommend_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant_recommend_sku`;
CREATE TABLE `t_merchant_recommend_sku` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) NOT NULL COMMENT '商户id',
  `sku_id` int(11) NOT NULL COMMENT 'sku id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户推荐商品';

-- ----------------------------
-- Table structure for t_message_record
-- ----------------------------
DROP TABLE IF EXISTS `t_message_record`;
CREATE TABLE `t_message_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `message_template_id` int(11) DEFAULT NULL COMMENT '消息模版ID',
  `name` varchar(1024) NOT NULL COMMENT '消息名称',
  `content` varchar(1024) DEFAULT NULL COMMENT '消息内容',
  `pic_url` varchar(1024) DEFAULT NULL COMMENT '消息图片url',
  `link` varchar(1024) DEFAULT NULL COMMENT '消息链接',
  `is_read` tinyint(1) DEFAULT '0' COMMENT '是否已读 0: 未读 1:已读',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除 0：未删除，1：已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息记录';

-- ----------------------------
-- Table structure for t_message_template
-- ----------------------------
DROP TABLE IF EXISTS `t_message_template`;
CREATE TABLE `t_message_template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(32) NOT NULL COMMENT '消息code',
  `message_type` int(11) NOT NULL COMMENT '0：订单消息 1: 优惠券消息 2: 活动消息 3：系统通知',
  `name` varchar(1024) NOT NULL COMMENT '消息名称',
  `content` varchar(1024) DEFAULT NULL COMMENT '消息内容',
  `pic_url` varchar(1024) DEFAULT NULL COMMENT '消息图片url',
  `link` varchar(1024) DEFAULT NULL COMMENT '消息链接',
  `push_method` tinyint(1) DEFAULT NULL COMMENT '推送方式 {1: 弱推送, 2: 强推送 3: 弱推送和强推送}',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除 0：未删除，1：已删除',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='消息模版';

-- ----------------------------
-- Table structure for t_news
-- ----------------------------
DROP TABLE IF EXISTS `t_news`;
CREATE TABLE `t_news` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `news_category_id` int(11) DEFAULT NULL COMMENT '分类id',
  `title` varchar(1024) NOT NULL COMMENT '资讯标题',
  `content` text COMMENT '资讯内容',
  `pic_url` varchar(1024) DEFAULT NULL COMMENT '图片url',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 {0: 正常, 1: 停用}',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除 0：未删除，1：已删除',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资讯';

-- ----------------------------
-- Table structure for t_news_category
-- ----------------------------
DROP TABLE IF EXISTS `t_news_category`;
CREATE TABLE `t_news_category` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(1024) NOT NULL COMMENT '名称',
  `pic_url` varchar(1024) DEFAULT NULL COMMENT '图片url',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 {0: 正常, 1: 停用}',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除 0：未删除，1：已删除',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资讯分类';

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `merchant_id` int(11) NOT NULL COMMENT '所属商户id',
  `no` varchar(16) NOT NULL COMMENT '订单编号',
  `cust_id` int(11) NOT NULL COMMENT '客户ID',
  `amount` decimal(14,2) NOT NULL COMMENT '订单总金额',
  `third_party_amount` decimal(14,2) DEFAULT NULL COMMENT '第三方平台订单总金额',
  `down_payment` decimal(14,2) DEFAULT '0.00' COMMENT '首付款金额',
  `installment_amount` decimal(14,2) DEFAULT '0.00' COMMENT '分期金额',
  `installment_months` int(10) unsigned DEFAULT '0' COMMENT '分期数',
  `installment_rate` decimal(14,4) DEFAULT '0.0000' COMMENT '分期（每期）费率',
  `down_payment_payed` tinyint(1) DEFAULT '0' COMMENT '是否已支付首付，如果无首付，为 false',
  `installment_payed` tinyint(1) DEFAULT '0' COMMENT '是否已支付分期，如果无分期，为 false',
  `down_payment_time` datetime DEFAULT NULL COMMENT '首付成功的时间（支付时间）',
  `installment_time` datetime DEFAULT NULL COMMENT '分期成功的时间',
  `pay_method` tinyint(2) DEFAULT NULL COMMENT '订单支付类型，1: 全款 2: 全分期 3: 首付+分期',
  `pay_type` tinyint(2) DEFAULT '0' COMMENT '支付类型，0: 现金 1: 积分',
  `order_type` tinyint(1) DEFAULT '0' COMMENT '订单类型，0: 实物 1: 虚拟订单',
  `status` tinyint(2) NOT NULL COMMENT '订单状态，0: 草稿 1: 未付款 2: 已付款 3: 已确认 4: 已制单 5: 已发货 6: 已签收 7：已失效 9: 已取消',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单开始时间（下单时间）',
  `close_time` datetime DEFAULT NULL COMMENT '订单结束时间',
  `address_name` varchar(32) DEFAULT NULL COMMENT '收货人姓名',
  `address_mobile` char(11) DEFAULT NULL COMMENT '收货人手机号',
  `address_full_text` varchar(256) DEFAULT NULL COMMENT '收货人详细地址，从省市到门牌号',
  `address_zip` int(6) DEFAULT NULL COMMENT '收货人邮政编码',
  `logistics_no` varchar(32) DEFAULT NULL COMMENT '物流单号',
  `logistics_company` varchar(128) DEFAULT NULL COMMENT '物流公司',
  `receipt_time` datetime DEFAULT NULL COMMENT '订单签收时间',
  `return_time` datetime DEFAULT NULL COMMENT '订单退货时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `stage_id` varchar(12) DEFAULT NULL COMMENT '信分宝分期id',
  `invoice_title` varchar(255) DEFAULT NULL COMMENT '发票抬头',
  `invoice_type` int(11) NOT NULL DEFAULT '1' COMMENT '发票类型 1 普通发票',
  `invoice_status` int(11) DEFAULT NULL COMMENT '发票状态 0 未开 1 已开',
  `referee` varchar(32) DEFAULT NULL COMMENT '订单推荐人银行人员行号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_no` (`no`),
  KEY `idx_no` (`no`),
  KEY `idx_cust_id` (`cust_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='订单表';

-- ----------------------------
-- Table structure for t_order_cancellation
-- ----------------------------
DROP TABLE IF EXISTS `t_order_cancellation`;
CREATE TABLE `t_order_cancellation` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `no` varchar(16) NOT NULL COMMENT '申请单号',
  `type` tinyint(2) NOT NULL COMMENT '1: 申请取消 2: 申请退货',
  `reason_type` int(1) DEFAULT NULL COMMENT '1: 质量问题 2: 到货物流损坏 3: 商品与描述不符 4: 错发、漏发、缺件 5: 其他原因',
  `reason` varchar(1024) DEFAULT NULL COMMENT '申请理由，申请取消不填写此字段',
  `pics` varchar(1024) DEFAULT NULL COMMENT '图片，英文,分隔的URL（或URL除去域名前缀），申请取消不填写此字段',
  `status` int(2) NOT NULL COMMENT '状态，0: 初始(已提交) 1: 已确认 2: 已拒绝 3: 待收货 4: 已收货 5: 完成 (3、4、5只对退货)',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_no` (`no`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='退货表';

-- ----------------------------
-- Table structure for t_order_ext
-- ----------------------------
DROP TABLE IF EXISTS `t_order_ext`;
CREATE TABLE `t_order_ext` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT '订单id',
  `third_order_id` varchar(45) NOT NULL COMMENT '第三方订单Id',
  `third_order_type` tinyint(1) NOT NULL COMMENT '订单来源 2: 京东',
  `order_price` decimal(14,2) DEFAULT NULL COMMENT '价格',
  `order_naked_price` decimal(14,2) DEFAULT NULL COMMENT '订单裸价',
  `order_tac_price` decimal(14,2) DEFAULT NULL COMMENT '订单税额',
  `freight` decimal(14,2) DEFAULT NULL COMMENT '运费',
  `status` tinyint(1) NOT NULL COMMENT '订单状态 1: 拒收订单 2: 妥投订单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单扩展表';

-- ----------------------------
-- Table structure for t_order_payment
-- ----------------------------
DROP TABLE IF EXISTS `t_order_payment`;
CREATE TABLE `t_order_payment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `amount` decimal(14,2) NOT NULL COMMENT '金额',
  `category` tinyint(2) NOT NULL COMMENT '1: 首付 2: 分期',
  `type` tinyint(2) NOT NULL COMMENT '支付方式，10: 微信支付 20: 分期 11: 代金券',
  `source` int(11) DEFAULT NULL COMMENT '如代金券ID（如果使用代金券支付），每个支付方式都可存对应的属性',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0: 订单未完成 1: 订单完成',
  `create_time` datetime NOT NULL COMMENT '创建（支付成功）时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_order_return
-- ----------------------------
DROP TABLE IF EXISTS `t_order_return`;
CREATE TABLE `t_order_return` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_sku_id` int(11) NOT NULL COMMENT 'order_sku 主键',
  `no` varchar(32) NOT NULL COMMENT '售后单号',
  `count` int(11) NOT NULL COMMENT '退货数量',
  `avg_price` decimal(14,2) NOT NULL COMMENT '平均单价',
  `return_amount` decimal(14,2) NOT NULL COMMENT '退货总价',
  `reason_type` int(11) DEFAULT NULL COMMENT '1. 质量问题 2. 到货物流损坏 3. 商品与描述不符 4. 错发、漏发、缺件 5. 其他原因',
  `reason` varchar(1024) DEFAULT NULL COMMENT '申请理由，申请取消不填写此字段',
  `pics` varchar(1024) DEFAULT NULL COMMENT '图片，英文,分隔的URL（或URL除去域名前缀），申请取消不填写此字段',
  `status` int(11) NOT NULL COMMENT '状态，0: 初始(已提交) 1: 已确认 2: 已拒绝 3: 待收货 4: 已收货 5: 完成',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_sku_id` (`order_sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退货表';

-- ----------------------------
-- Table structure for t_order_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_order_sku`;
CREATE TABLE `t_order_sku` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `sku_id` int(11) NOT NULL COMMENT '商品ID',
  `sku_name` varchar(256) NOT NULL COMMENT '商品名称',
  `sku_description` text COMMENT '描述（副标题）',
  `sku_detail` text COMMENT '商品介绍',
  `sku_erp_code` varchar(128) NOT NULL COMMENT 'ERP CODE',
  `sku_head_thumbnail` varchar(256) DEFAULT NULL COMMENT '商品图片',
  `sku_count` int(11) NOT NULL COMMENT '商品数量',
  `return_count` int(11) DEFAULT NULL COMMENT '已申请(不包含退货失败)退货数量',
  `sku_price` decimal(14,2) NOT NULL COMMENT '单个商品价格',
  `avg_price` decimal(14,2) DEFAULT NULL COMMENT '实际单价',
  `fact_amount` decimal(14,2) DEFAULT NULL COMMENT '实际总价',
  `sku_original_price` decimal(14,2) DEFAULT NULL COMMENT '单个商品原价',
  `sku_attribute` varchar(255) DEFAULT NULL COMMENT '原商品规格',
  `activity_type` int(11) DEFAULT NULL COMMENT '参与活动分类(1：秒杀 2：优惠券)',
  `activity_id` int(11) DEFAULT NULL COMMENT '活动id(如果是秒杀活动等于秒杀活动id，如果是优惠券则等于优惠券id)',
  `third_party_id` varchar(100) DEFAULT NULL COMMENT '新东方: 第三方ID',
  `commission_rate` double DEFAULT '0' COMMENT '新东方: 佣金比例',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_order_sku_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_order_sku_activity`;
CREATE TABLE `t_order_sku_activity` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL COMMENT '订单ID',
  `activity_type` int(11) DEFAULT NULL COMMENT '活动类型（1：秒杀，2：限购，3：优惠卷）',
  `activity_id` int(11) DEFAULT NULL COMMENT '活动ID',
  `activity_skuId` int(11) DEFAULT NULL COMMENT '参加活动商品ID',
  `discounted_price` decimal(14,2) DEFAULT NULL COMMENT '优惠价格（优惠价格+商品当前价格=商品原价）',
  `activity_describe` varchar(500) DEFAULT NULL COMMENT '活动信息描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='SKU支付组成表';

-- ----------------------------
-- Table structure for t_order_sku_virtual
-- ----------------------------
DROP TABLE IF EXISTS `t_order_sku_virtual`;
CREATE TABLE `t_order_sku_virtual` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_sku_id` int(11) NOT NULL COMMENT '订单商品id',
  `serial_code` varchar(16) NOT NULL DEFAULT '1' COMMENT '序列号',
  `is_used` tinyint(1) DEFAULT '0' COMMENT '是否已使用 0：未使用，1：已使用',
  `expire_time` datetime DEFAULT NULL COMMENT '失效时间',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `creator_id` int(11) NOT NULL COMMENT '操作人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单虚拟商品';

-- ----------------------------
-- Table structure for t_pay_record
-- ----------------------------
DROP TABLE IF EXISTS `t_pay_record`;
CREATE TABLE `t_pay_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` varchar(64) DEFAULT NULL COMMENT '订单ID',
  `trade_no` varchar(64) DEFAULT NULL COMMENT '订单流水号',
  `pay_state` varchar(10) DEFAULT NULL COMMENT '支付状态\r\n            A1:未支付 默认\r\n            A2:已支付',
  `pay_time` timestamp NULL DEFAULT NULL COMMENT '支付完成时间，微信: 未支付成功: 无数据库记录，已支付: 支付完成时间(time_end)',
  `pay_no` varchar(64) DEFAULT NULL COMMENT '银行网上支付平台流水号，微信：微信支付订单号(transaction_id)',
  `acc_no` varchar(64) DEFAULT NULL COMMENT '支付银行，微信: 付款银行(bank_type)',
  `cst_name` varchar(128) DEFAULT NULL COMMENT '持卡人姓名，微信: NULL',
  `openid` varchar(255) DEFAULT NULL COMMENT '微信: 微信 openid',
  `is_subscribe` int(1) DEFAULT NULL COMMENT '微信: 是否关注公众账号(is_subscribe)',
  `ip` varchar(20) DEFAULT NULL COMMENT '客户支付ip地址',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_id` (`order_id`,`trade_no`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='订单流水表';

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL COMMENT '权限名',
  `code` varchar(128) NOT NULL COMMENT '权限代码,配合shiro用',
  `describ` varchar(128) DEFAULT NULL COMMENT '描述',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '上级',
  `url` varchar(30) DEFAULT NULL COMMENT '链接的地址',
  `glyphicon` varchar(50) DEFAULT NULL COMMENT '菜单英文名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='权限, 这个表数据只能开发来维护';

-- ----------------------------
-- Table structure for t_pickup_order
-- ----------------------------
DROP TABLE IF EXISTS `t_pickup_order`;
CREATE TABLE `t_pickup_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT '订单id',
  `pickup_point_id` int(11) NOT NULL COMMENT '自提点id',
  `code` varchar(10) DEFAULT NULL COMMENT '提货码',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态，0: 初始(订单生成) 1: 已付款 2: 已收货(货物已到自提点) 3: 已自提(已签收) 4: 退货中 5: 已拒绝 6: 已退货 7: 已取消 8: 已退款',
  `invalid` int(1) NOT NULL DEFAULT '0' COMMENT '0: 未删 1: 已删',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `arrive_time` datetime DEFAULT NULL COMMENT '到货时间',
  `take_time` datetime DEFAULT NULL COMMENT '提货时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提货单';

-- ----------------------------
-- Table structure for t_pickup_point
-- ----------------------------
DROP TABLE IF EXISTS `t_pickup_point`;
CREATE TABLE `t_pickup_point` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '自提点名称',
  `open_time` varchar(255) DEFAULT NULL COMMENT '营业开始时间',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '状态 0: 未开启 1: 开启',
  `rank` int(11) DEFAULT NULL COMMENT '排序',
  `invalid` int(1) NOT NULL DEFAULT '0' COMMENT '0: 未删 1: 已删',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自提点';

-- ----------------------------
-- Table structure for t_pickup_point_user
-- ----------------------------
DROP TABLE IF EXISTS `t_pickup_point_user`;
CREATE TABLE `t_pickup_point_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `login_name` varchar(64) NOT NULL COMMENT '登录名，例如手机号',
  `password` varchar(64) NOT NULL COMMENT '密码，两次MD5后的值',
  `type` int(1) NOT NULL COMMENT '类型 1: 营业厅人员 2: 配送员/验货员',
  `pickup_point_id` varchar(512) DEFAULT NULL COMMENT ',分隔的自提点id',
  `status` int(1) NOT NULL DEFAULT '0' COMMENT '是否启用 0: 未启用 1: 启用',
  `invalid` int(1) NOT NULL DEFAULT '0' COMMENT '0: 未删 1: 已删',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自提点用户';

-- ----------------------------
-- Table structure for t_popular_search
-- ----------------------------
DROP TABLE IF EXISTS `t_popular_search`;
CREATE TABLE `t_popular_search` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `query_text` varchar(45) NOT NULL COMMENT '搜索内容',
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT '排序值，越大排序越靠后',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效：1有效，0无效，默认为1',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='热门搜索表';

-- ----------------------------
-- Table structure for t_promotion
-- ----------------------------
DROP TABLE IF EXISTS `t_promotion`;
CREATE TABLE `t_promotion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL COMMENT '活动名称',
  `code` varchar(64) DEFAULT NULL COMMENT '代码',
  `rate` int(10) NOT NULL COMMENT '综合中奖率，如 30% 则存储 30',
  `description` varchar(256) NOT NULL COMMENT '说明',
  `begin_time` datetime DEFAULT NULL COMMENT '活动开始时间',
  `expire_time` datetime DEFAULT NULL COMMENT '活动截止时间',
  `status` tinyint(1) DEFAULT '0' COMMENT '0 不启用 1 启用 ',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `type` int(1) DEFAULT NULL COMMENT '0 按活动 1 按天',
  `newcomer` int(1) DEFAULT NULL COMMENT '1 老用户 2 新用户 3 所有用户',
  `times` int(11) DEFAULT NULL COMMENT '抽奖次数',
  `min_hit` int(11) DEFAULT NULL COMMENT '每个参与者，本次活动最少中奖次数',
  `max_hit` int(11) DEFAULT NULL COMMENT '每个参与者，本次活动最多中奖次数',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='抽奖活动配置表';

-- ----------------------------
-- Table structure for t_promotion_prize
-- ----------------------------
DROP TABLE IF EXISTS `t_promotion_prize`;
CREATE TABLE `t_promotion_prize` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '奖品表',
  `promotion_id` int(11) NOT NULL COMMENT 't_promotion.id',
  `name` varchar(128) DEFAULT NULL COMMENT '奖品名称',
  `description` varchar(4096) DEFAULT NULL COMMENT '奖品详细描述',
  `inventory` int(11) DEFAULT NULL COMMENT '库存数量，NULL 表示无上限',
  `awarded_amount` int(11) DEFAULT '0' COMMENT '中奖数量',
  `virtual_type` smallint(6) DEFAULT NULL COMMENT '1: 代金券; 2: 虚拟币 3: 积分。非虚拟奖品时，此字段为NULL',
  `virtual_value` decimal(18,2) DEFAULT NULL COMMENT '如果是虚拟物品，其价值',
  `virtual_ext1` int(11) DEFAULT NULL COMMENT '虚拟物品，扩展字段1。',
  `virtual_ext2` int(11) DEFAULT NULL COMMENT '虚拟物品，扩展字段2。',
  `rate` int(11) DEFAULT NULL COMMENT '中奖率，库存数量无上限，或者 > 0 时参与计算中奖率',
  `expire_time` datetime DEFAULT NULL COMMENT '奖品过期时间点',
  `expire_seconds` int(11) DEFAULT NULL COMMENT '奖品有效时长（秒，从中奖开始计算，expire_date 为 NULL 此字段才有效）。0 表示永久有效。',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '奖品创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '奖品最后更新时间',
  `image_url` varchar(128) DEFAULT NULL,
  `invalid` int(1) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_promotion_raffle
-- ----------------------------
DROP TABLE IF EXISTS `t_promotion_raffle`;
CREATE TABLE `t_promotion_raffle` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '中奖用户',
  `prize_id` int(11) DEFAULT NULL COMMENT '奖品，未中奖则为 NULL',
  `promotion_id` int(11) NOT NULL COMMENT 't_promotion.id。因为未中奖也要记录，所以这个字段是必须的',
  `code` int(11) NOT NULL COMMENT '抽奖结果。0: 中奖了; 3: 未中奖; 4: 没有可以抽的奖品; 5: 异常，必须中奖，但是未中奖; 6: 直接发奖',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '抽奖时间',
  `real_name` varchar(255) DEFAULT NULL COMMENT '领取人姓名',
  `mobile` char(11) DEFAULT NULL COMMENT '手机号',
  `is_receive` tinyint(1) DEFAULT '0' COMMENT '是否领取 默认未领取',
  `address` varchar(255) DEFAULT NULL COMMENT '收货地址',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `expire_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_rate_history
-- ----------------------------
DROP TABLE IF EXISTS `t_rate_history`;
CREATE TABLE `t_rate_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `referrer_id` int(11) DEFAULT NULL COMMENT '推荐人id',
  `company_info_id` int(11) DEFAULT NULL COMMENT '公司id',
  `date` datetime DEFAULT NULL COMMENT '日期（年月）',
  `company_rate` decimal(8,5) DEFAULT NULL COMMENT '比率（输入整数，如输入5实际为5%）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='往期佣金比率表';

-- ----------------------------
-- Table structure for t_referrer
-- ----------------------------
DROP TABLE IF EXISTS `t_referrer`;
CREATE TABLE `t_referrer` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `login_name` varchar(45) DEFAULT NULL COMMENT '登录名称',
  `pass_word` varchar(45) DEFAULT NULL COMMENT '登陆密码',
  `address` varchar(128) DEFAULT NULL COMMENT '详细地址',
  `real_name` varchar(64) DEFAULT NULL COMMENT '所在公司',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `create_userid` int(11) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_userid` int(11) DEFAULT NULL COMMENT '维护人ID',
  `modify_time` datetime DEFAULT NULL COMMENT '维护时间',
  `remarks` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='推荐人表';

-- ----------------------------
-- Table structure for t_referrer_company
-- ----------------------------
DROP TABLE IF EXISTS `t_referrer_company`;
CREATE TABLE `t_referrer_company` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `referrer_id` int(11) unsigned DEFAULT NULL COMMENT '推荐人ID',
  `company_info_id` int(11) unsigned DEFAULT NULL COMMENT '公司信息ID',
  `company_rate` decimal(8,5) DEFAULT NULL COMMENT '公司总反润比率',
  PRIMARY KEY (`id`),
  KEY `key_company_relevance_id` (`company_info_id`),
  KEY `key_referrer_relevance_id` (`referrer_id`),
  CONSTRAINT `t_referrer_company_ibfk_1` FOREIGN KEY (`company_info_id`) REFERENCES `t_company_info` (`id`),
  CONSTRAINT `t_referrer_company_ibfk_2` FOREIGN KEY (`referrer_id`) REFERENCES `t_referrer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='推荐人公司关联表';

-- ----------------------------
-- Table structure for t_refund
-- ----------------------------
DROP TABLE IF EXISTS `t_refund`;
CREATE TABLE `t_refund` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL COMMENT '订单ID',
  `cancellation_id` int(11) NOT NULL COMMENT '退货ID',
  `amount` decimal(14,2) NOT NULL COMMENT '退款金额',
  `type` tinyint(2) NOT NULL COMMENT '1: 首付款 2: 已还分期金额 3: 分期额度',
  `create_time` datetime NOT NULL COMMENT '生成时间',
  `refund_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` tinyint(2) NOT NULL COMMENT '状态，0: 未退款 1: 已退款',
  `refund_type` int(2) DEFAULT NULL COMMENT '退款类型 1 付款后取消 2 退货',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='退款表';

-- ----------------------------
-- Table structure for t_refund_plan_installment
-- ----------------------------
DROP TABLE IF EXISTS `t_refund_plan_installment`;
CREATE TABLE `t_refund_plan_installment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `refund_plan_id` int(11) NOT NULL COMMENT '退款计划ID',
  `installment_amount` decimal(14,2) DEFAULT '0.00' COMMENT '总分期金额',
  `installment_months` int(10) unsigned DEFAULT '0' COMMENT '总分期数',
  `installment_amount_repayed` decimal(14,2) DEFAULT '0.00' COMMENT '已还分期金额',
  `installment_months_repayed` int(10) unsigned DEFAULT '0' COMMENT '已还分期数',
  `create_time` datetime NOT NULL COMMENT '生成时间',
  PRIMARY KEY (`id`),
  KEY `idx_refund_plan_id` (`refund_plan_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_return_coupon
-- ----------------------------
DROP TABLE IF EXISTS `t_return_coupon`;
CREATE TABLE `t_return_coupon` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `activity_id` int(11) DEFAULT NULL COMMENT '活动id',
  `coupon_id` int(11) DEFAULT NULL COMMENT '优惠券id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='活动赠送优惠券';

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `code` varchar(16) DEFAULT NULL COMMENT '角色代码，配合shiro用',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否内置角色, 0:不是,1:是. 内置几个角色不可删除, 用户自己添加的角色可以删除',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态, 0:正常,1:禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Table structure for t_role_perm
-- ----------------------------
DROP TABLE IF EXISTS `t_role_perm`;
CREATE TABLE `t_role_perm` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `perm_id` int(11) NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='角色授权的权限';

-- ----------------------------
-- Table structure for t_rush_buy
-- ----------------------------
DROP TABLE IF EXISTS `t_rush_buy`;
CREATE TABLE `t_rush_buy` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(32) NOT NULL COMMENT '活动名称',
  `need_appoint` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否需要预约, 0不需要,1需要, 默认0',
  `appoint_start_time` datetime DEFAULT NULL COMMENT '开始接受预约时间, 当need_appoint=0的时候可空',
  `appoint_end_time` datetime DEFAULT NULL COMMENT '停止接受预约时间, 当need_appoint=0的时候可空',
  `start_time` datetime NOT NULL COMMENT '秒杀开始时间',
  `end_time` datetime NOT NULL COMMENT '秒杀结束时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '活动创建时间',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除,0:未删除,1:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='秒杀活动';

-- ----------------------------
-- Table structure for t_rush_buy_appoint
-- ----------------------------
DROP TABLE IF EXISTS `t_rush_buy_appoint`;
CREATE TABLE `t_rush_buy_appoint` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cust_id` int(10) unsigned NOT NULL COMMENT '预约用户',
  `rush_buy_id` int(10) unsigned NOT NULL COMMENT '所预约的秒杀活动id',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `mobile` varchar(11) NOT NULL COMMENT '手机号',
  `bank_card_no` varchar(32) DEFAULT NULL COMMENT '信合通银行卡号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预约时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='秒杀活动预约信息';

-- ----------------------------
-- Table structure for t_rush_buy_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_rush_buy_sku`;
CREATE TABLE `t_rush_buy_sku` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `rush_buy_id` int(10) unsigned NOT NULL COMMENT '秒杀活动id',
  `sku_id` int(10) unsigned NOT NULL COMMENT '活动商品id',
  `sku_name` varchar(255) NOT NULL COMMENT '商品名称',
  `price` decimal(14,2) NOT NULL COMMENT '秒杀活动价',
  `original_price` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '市场价',
  `available_times` int(10) unsigned NOT NULL COMMENT '单个用户可购买次数',
  `available_count` int(10) unsigned NOT NULL COMMENT '单个用户可购买个数',
  `left_sku` int(10) unsigned NOT NULL COMMENT '剩余库存量',
  `sold_sku` int(10) unsigned NOT NULL COMMENT '已售出数量',
  `return_sku` int(10) unsigned NOT NULL DEFAULT '0',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='秒杀活动所关联的商品';

-- ----------------------------
-- Table structure for t_score_order
-- ----------------------------
DROP TABLE IF EXISTS `t_score_order`;
CREATE TABLE `t_score_order` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `score_product_id` int(11) NOT NULL COMMENT '积分面值ID',
  `score` int(11) NOT NULL COMMENT '积分面值快照',
  `price` decimal(14,2) NOT NULL COMMENT '价格快照',
  `no` varchar(16) NOT NULL COMMENT '订单编号',
  `count` int(11) NOT NULL COMMENT '数量',
  `total_score` int(11) NOT NULL COMMENT '积分',
  `total_price` decimal(14,2) NOT NULL COMMENT '总价格',
  `pay_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 {0: 已支付, 1: 未支付}',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分订单';

-- ----------------------------
-- Table structure for t_score_product
-- ----------------------------
DROP TABLE IF EXISTS `t_score_product`;
CREATE TABLE `t_score_product` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `score` int(11) NOT NULL COMMENT '积分面值',
  `price` decimal(14,2) NOT NULL COMMENT '价格',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 {0: 正常, 1: 停用}',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除 0：未删除，1：已删除',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分面值';

-- ----------------------------
-- Table structure for t_score_record
-- ----------------------------
DROP TABLE IF EXISTS `t_score_record`;
CREATE TABLE `t_score_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '类型 1: 获取积分 2: 消耗积分',
  `method` tinyint(4) NOT NULL DEFAULT '1' COMMENT '获取/消费方式 1: 积分签到 2: 充值 3: 银行导入 4: 积分抽奖 5: 积分夺宝 6: 积分兑换',
  `score_ratio` int(11) NOT NULL COMMENT '兑换比率快照',
  `amount` decimal(14,2) NOT NULL COMMENT '消费/获取金额',
  `no` varchar(16) DEFAULT NULL COMMENT '订单编号',
  `score` int(11) NOT NULL COMMENT '积分',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='积分记录';

-- ----------------------------
-- Table structure for t_seq_no
-- ----------------------------
DROP TABLE IF EXISTS `t_seq_no`;
CREATE TABLE `t_seq_no` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `key` varchar(32) NOT NULL COMMENT '序列号Key',
  `seq_no` int(11) NOT NULL COMMENT '序列号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_share
-- ----------------------------
DROP TABLE IF EXISTS `t_share`;
CREATE TABLE `t_share` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '分享者id',
  `share_code` varchar(45) DEFAULT NULL COMMENT '分享码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_user_id` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='分享码表';

-- ----------------------------
-- Table structure for t_share_reward
-- ----------------------------
DROP TABLE IF EXISTS `t_share_reward`;
CREATE TABLE `t_share_reward` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id(邀请者或注册者)',
  `share_id` int(11) NOT NULL COMMENT 't_share.id',
  `share_rule_id` int(11) NOT NULL COMMENT 't_share_rule.id',
  `coupon_record_id` int(11) NOT NULL COMMENT '获取的优惠券记录id(t_coupon_record.id)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_coupon_record_id` (`coupon_record_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_share_id` (`share_id`),
  KEY `idx_share_rule_id` (`share_rule_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='分享注册奖励表';

-- ----------------------------
-- Table structure for t_share_rule
-- ----------------------------
DROP TABLE IF EXISTS `t_share_rule`;
CREATE TABLE `t_share_rule` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `coupon_id` int(11) NOT NULL COMMENT '优惠券Id',
  `type` int(11) NOT NULL COMMENT '类型 1、分享者获得 2、被分享者获得 3、被分享者获得额外',
  `extra_threshold` int(11) DEFAULT NULL COMMENT '达到此注册用户数时，可额外获得优惠券(type=3)',
  `status` int(11) NOT NULL COMMENT '状态 1 有效 0 无效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_coupon_id` (`coupon_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='分享优惠券赠送规则表';

-- ----------------------------
-- Table structure for t_share_user
-- ----------------------------
DROP TABLE IF EXISTS `t_share_user`;
CREATE TABLE `t_share_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '注册者用户Id',
  `share_id` int(11) NOT NULL COMMENT 't_share.id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_user_id` (`user_id`),
  KEY `idx_share_id` (`share_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='分享注册表';

-- ----------------------------
-- Table structure for t_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_sku`;
CREATE TABLE `t_sku` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `spu_id` int(11) unsigned NOT NULL COMMENT 'SPU ID',
  `name` varchar(255) NOT NULL COMMENT '商品名称（标题）',
  `description` text COMMENT '规格参数',
  `detail` text COMMENT '商品详情',
  `erp_code` varchar(128) DEFAULT NULL COMMENT 'ERP CODE',
  `status` tinyint(1) DEFAULT '0' COMMENT '0: 下架; 1: 上架',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有效，默认有效。可用于上架、下架。',
  `original_price` decimal(14,2) DEFAULT NULL COMMENT '原价格（或者市场价格）',
  `price` decimal(14,2) NOT NULL COMMENT '商品基础价格',
  `inventory` int(11) DEFAULT NULL COMMENT '库存',
  `score_exchange_count` int(11) DEFAULT '0' COMMENT '已兑换数量',
  `warning_inventory` int(11) DEFAULT NULL COMMENT '库存预警（等于这个值时预警）',
  `installment` varchar(255) NOT NULL COMMENT '可分期数（多个分期英文逗号隔开）',
  `head_thumbnail` varchar(255) DEFAULT NULL COMMENT 'SKU缩略图',
  `images_thumbnail` text COMMENT '缩略图，英文逗号分隔的图片URL',
  `images_original` text COMMENT '大图，英文逗号分隔的图片URL',
  `is_score` tinyint(1) DEFAULT '0' COMMENT '是否参与积分兑换 0: 否 1：是',
  `is_virtual` tinyint(1) DEFAULT '0' COMMENT '是否为虚拟商品 0: 否 1：是',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否 SPU 里默认显示的商品',
  `pickup` int(1) NOT NULL DEFAULT '0' COMMENT '0: 不自提 1: 自提',
  `time_type` int(1) DEFAULT NULL COMMENT '有效期类型。1: 绝对时间(expire_time) 2: 相对时间(expire_in_days)',
  `start_time` datetime DEFAULT NULL COMMENT '有效期开始时间。如果是相对时间，此字段为 NULL',
  `expire_time` datetime DEFAULT NULL COMMENT 'time_type = 1 时有意义，有效期结束时间',
  `expire_in_days` int(11) DEFAULT NULL COMMENT 'time_type = 2 时有意义，有效天数（从领取时间开始算）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sku_attribute
-- ----------------------------
DROP TABLE IF EXISTS `t_sku_attribute`;
CREATE TABLE `t_sku_attribute` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sku_id` int(11) unsigned NOT NULL COMMENT 'SkU ID',
  `spu_attribute_id` int(11) unsigned NOT NULL,
  `attribute_value` varchar(255) NOT NULL COMMENT '属性值',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sku_id_2_spu_attribute_id` (`sku_id`,`spu_attribute_id`) USING BTREE,
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_sku_audit
-- ----------------------------
DROP TABLE IF EXISTS `t_sku_audit`;
CREATE TABLE `t_sku_audit` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sku_id` int(11) NOT NULL,
  `creator_id` int(11) NOT NULL COMMENT '审核人id',
  `audit_result` varchar(1024) DEFAULT NULL COMMENT '审核结果',
  `description` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sku审核记录';

-- ----------------------------
-- Table structure for t_sku_ext
-- ----------------------------
DROP TABLE IF EXISTS `t_sku_ext`;
CREATE TABLE `t_sku_ext` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sku_id` int(11) NOT NULL COMMENT 'sku id',
  `type` int(11) NOT NULL COMMENT '类型，1: 新东方',
  `field1` varchar(128) DEFAULT NULL COMMENT '新东方: product_id; ',
  `field2` varchar(128) DEFAULT NULL COMMENT '新东方: 有效期; ',
  `field3` varchar(128) DEFAULT NULL COMMENT '新东方: 总课时; ',
  `field4` varchar(200) DEFAULT NULL COMMENT '新东方：授课老师',
  `field5` text COMMENT '新东方：课程简介',
  `field6` longtext COMMENT '新东方: 适用人群',
  `field7` text COMMENT '新东方: 学习目标',
  `field8` longtext COMMENT '新东方: 教材简介',
  `field9` varchar(100) DEFAULT NULL COMMENT '新东发：售价',
  `field10` varchar(100) DEFAULT NULL COMMENT '新东方：上次商品定价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='sku扩展表';

-- ----------------------------
-- Table structure for t_sku_shelves_schedule
-- ----------------------------
DROP TABLE IF EXISTS `t_sku_shelves_schedule`;
CREATE TABLE `t_sku_shelves_schedule` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sku_id` int(11) NOT NULL COMMENT 'sku id',
  `on_time` datetime DEFAULT NULL COMMENT '上架时间',
  `off_time` datetime DEFAULT NULL COMMENT '下架时间',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '0: 有效，1: 无效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='定时上下架计划';

-- ----------------------------
-- Table structure for t_sms_record
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_record`;
CREATE TABLE `t_sms_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `content` varchar(1024) DEFAULT NULL COMMENT '短信内容',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除 0：未删除，1：已删除',
  `result_code` varchar(32) DEFAULT NULL COMMENT '状态返回',
  `result_message` varchar(128) DEFAULT NULL COMMENT '返回信息',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信记录';

-- ----------------------------
-- Table structure for t_sms_template
-- ----------------------------
DROP TABLE IF EXISTS `t_sms_template`;
CREATE TABLE `t_sms_template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sms_type` int(11) NOT NULL COMMENT '短信类型 0: 发送验证码 1: 支付成功',
  `content` varchar(1024) DEFAULT NULL COMMENT '短信内容',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除 0：未删除，1：已删除',
  `creator_id` int(11) NOT NULL COMMENT '创建人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='短信模版';

-- ----------------------------
-- Table structure for t_spec
-- ----------------------------
DROP TABLE IF EXISTS `t_spec`;
CREATE TABLE `t_spec` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `value` varchar(2000) DEFAULT NULL COMMENT '所有值 多个用,分隔',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否无效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='规格';

-- ----------------------------
-- Table structure for t_spu
-- ----------------------------
DROP TABLE IF EXISTS `t_spu`;
CREATE TABLE `t_spu` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) NOT NULL COMMENT '所属商户id',
  `name` varchar(255) NOT NULL COMMENT '商品名称（标题）',
  `sub_name` varchar(255) DEFAULT NULL COMMENT '副名称',
  `category_id` int(11) unsigned NOT NULL COMMENT '所属分类ID',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有效，默认有效',
  `thumbnail` varchar(255) DEFAULT NULL COMMENT 'SPU缩略图',
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT '排序值，越大排序越靠后',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `description` text COMMENT '电脑版详情',
  `mobile_description` text COMMENT '手机版详情',
  `supplier_id` int(11) DEFAULT NULL COMMENT '供货商ID',
  PRIMARY KEY (`id`),
  KEY `idx_rank` (`rank`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_spu_attribute
-- ----------------------------
DROP TABLE IF EXISTS `t_spu_attribute`;
CREATE TABLE `t_spu_attribute` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `spu_id` int(11) unsigned NOT NULL COMMENT 'SPU ID',
  `attribute_id` int(11) unsigned NOT NULL COMMENT '属性ID',
  `value_constraint_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '0: 文本框，任意填写；1: 下拉',
  `value_constraint_expression` varchar(256) DEFAULT NULL COMMENT '如果是下拉，	分隔的值',
  `attribute_values` varchar(255) DEFAULT NULL COMMENT 'spu属性所有值，,分隔',
  `rank` int(11) NOT NULL DEFAULT '0' COMMENT '排序值，越大排序越靠后',
  `attr_values` varchar(1024) DEFAULT NULL COMMENT 'spu属性所有值，JSON格式，[{"value":"颜色","hasSku":1,"hasSkuOnShelves":0}]',
  PRIMARY KEY (`id`),
  KEY `idx_spu_id` (`spu_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_spu_spec
-- ----------------------------
DROP TABLE IF EXISTS `t_spu_spec`;
CREATE TABLE `t_spu_spec` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `spec_id` int(11) DEFAULT NULL COMMENT '规格id',
  `spu_id` int(11) DEFAULT NULL COMMENT 'spu ID',
  `spec_value` varchar(255) DEFAULT NULL COMMENT '规格值',
  `spec_values` varchar(255) DEFAULT NULL COMMENT 'spu品牌所有值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='spu规格';

-- ----------------------------
-- Table structure for t_statement
-- ----------------------------
DROP TABLE IF EXISTS `t_statement`;
CREATE TABLE `t_statement` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) NOT NULL COMMENT '所属商户id',
  `total_amount` decimal(14,2) NOT NULL COMMENT '订单总金额',
  `refund_amount` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '退款金额',
  `coupon_amount` decimal(14,2) NOT NULL DEFAULT '0.00' COMMENT '优惠总额',
  `pay_amount` decimal(14,2) NOT NULL COMMENT '实际支付金额',
  `pay_merchant_amount` decimal(14,2) NOT NULL COMMENT '实际支付商户金额',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态 {0: 未结算, 1: 已结算}',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `close_time` datetime DEFAULT NULL COMMENT '结算时间',
  `creator_id` int(11) NOT NULL COMMENT '结算人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='交易结算';

-- ----------------------------
-- Table structure for t_supplier
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier`;
CREATE TABLE `t_supplier` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(1000) NOT NULL COMMENT '供货商名称',
  `creat_time` date DEFAULT NULL COMMENT '创建时间',
  `update_time` date DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='供货商表';

-- ----------------------------
-- Table structure for t_sys_exception_record
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_exception_record`;
CREATE TABLE `t_sys_exception_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sysDomain` varchar(200) DEFAULT NULL COMMENT '系统域名',
  `optName` varchar(200) DEFAULT NULL COMMENT '操作名称：发生异常时，所执行方法的功能描述',
  `className` varchar(500) DEFAULT NULL COMMENT '异常发生的类名',
  `method` varchar(200) DEFAULT NULL COMMENT '异常发生的方法名',
  `params` varchar(2048) DEFAULT NULL,
  `exceptionContent` varchar(2048) DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '异常记录状态：0未处理，1 已处理',
  `treatment` int(11) DEFAULT '1' COMMENT '处理方式（暂时只定义了短信方式，留扩展其他方式）：1 短信方式，2 邮件方式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='系统异常记录表';

-- ----------------------------
-- Table structure for t_system_user
-- ----------------------------
DROP TABLE IF EXISTS `t_system_user`;
CREATE TABLE `t_system_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `login_name` varchar(128) NOT NULL COMMENT '登录名',
  `pwd` varchar(64) NOT NULL COMMENT '登录密码',
  `name` varchar(32) NOT NULL COMMENT '姓名',
  `tel` varchar(16) DEFAULT NULL COMMENT '联系人手机号码',
  `url` varchar(128) DEFAULT NULL COMMENT '登录之后默认跳转的路径',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户类型,0:平台,1:银行,2:商户',
  `parent_id` int(11) DEFAULT NULL COMMENT '所属银行/商户id',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态,0:正常,1:禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_login_name` (`login_name`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='系统后台用户';

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '标签名',
  `tree_depth` tinyint(1) DEFAULT NULL COMMENT '整棵树的深度，从1（这棵树只有一个节点）开始，只在根节点赋值',
  `code` varchar(255) DEFAULT NULL COMMENT '标签代号',
  `parent_id` int(11) DEFAULT NULL COMMENT '父标签id',
  `invalid` tinyint(1) DEFAULT NULL COMMENT '是否有效',
  `status` tinyint(1) DEFAULT '0' COMMENT '状态 1已启用 0未启用',
  `rank` int(11) DEFAULT NULL COMMENT '排序',
  `icon` varchar(128) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='标签';

-- ----------------------------
-- Table structure for t_tag_sku
-- ----------------------------
DROP TABLE IF EXISTS `t_tag_sku`;
CREATE TABLE `t_tag_sku` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_id` int(11) DEFAULT NULL COMMENT '标签id',
  `sku_id` int(11) DEFAULT NULL COMMENT '商品id',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '是否已删除',
  `rank` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='标签关联商品';

-- ----------------------------
-- Table structure for t_unifiedorder
-- ----------------------------
DROP TABLE IF EXISTS `t_unifiedorder`;
CREATE TABLE `t_unifiedorder` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `appid` varchar(255) DEFAULT NULL COMMENT '公众账号ID',
  `body` varchar(255) DEFAULT NULL COMMENT '商品描述',
  `device_info` varchar(255) DEFAULT NULL COMMENT '设备号',
  `fee_type` varchar(255) DEFAULT NULL COMMENT '货币类型 默认人民币：CNY',
  `mch_id` varchar(255) DEFAULT NULL COMMENT '商户号',
  `nonce_str` varchar(255) DEFAULT NULL COMMENT '随机字符串',
  `notify_url` varchar(255) DEFAULT NULL COMMENT '通知地址',
  `openid` varchar(255) DEFAULT NULL COMMENT '用户标识',
  `out_trade_no` varchar(255) DEFAULT NULL COMMENT '商户订单号',
  `spbill_create_ip` varchar(255) DEFAULT NULL COMMENT '终端IP',
  `total_fee` int(11) DEFAULT NULL COMMENT '订单总金额，单位为分',
  `trade_type` varchar(255) DEFAULT NULL COMMENT '交易类型',
  `sign` varchar(255) DEFAULT NULL COMMENT '签名',
  `attach` varchar(255) DEFAULT NULL COMMENT '附加数据',
  `pay_sync_count` tinyint(2) DEFAULT '0' COMMENT '微信订单查询次数，用于统计微信支付主动查询支付结果的次数，暂定不超过3次',
  `pay_state` tinyint(2) DEFAULT '0' COMMENT '支付状态，0: 未支付 1: 已关闭 2: 支付成功 3: 支付失败（其他原因）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信预支付信息表';

-- ----------------------------
-- Table structure for t_upload_log
-- ----------------------------
DROP TABLE IF EXISTS `t_upload_log`;
CREATE TABLE `t_upload_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `ip_host` varchar(16) DEFAULT NULL COMMENT 'ip 地址',
  `filename` varchar(128) DEFAULT NULL COMMENT '文件名 路径',
  `size` varchar(16) DEFAULT NULL COMMENT '大小',
  `mimeType` varchar(16) DEFAULT NULL COMMENT '类型',
  `height` int(8) DEFAULT NULL COMMENT '高度',
  `width` int(8) DEFAULT NULL COMMENT '宽度',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `mobile` char(11) DEFAULT NULL COMMENT '手机号',
  `real_name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `id_card` char(18) DEFAULT NULL COMMENT '身份证号',
  `password` varchar(32) DEFAULT NULL COMMENT '密码 MD5',
  `pay_password` varchar(32) DEFAULT NULL COMMENT '支付密码 MD5',
  `invalid` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否有效，默认有效',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0: 初始化，1: 已提交授信申请',
  `rec_by_code` varchar(32) DEFAULT NULL COMMENT '通过这个推荐码注册的',
  `rec_code` varchar(32) DEFAULT NULL COMMENT '推荐码',
  `third_party_type` tinyint(2) DEFAULT '0' COMMENT '第三方类型，0: 无 1: 信分宝 2：微信openID',
  `third_party_id` varchar(255) DEFAULT NULL COMMENT '第三方身份标识',
  `open_id` varchar(64) DEFAULT NULL COMMENT '微信OpenId',
  `company_info_id` int(11) DEFAULT NULL,
  `head_img` varchar(255) DEFAULT NULL COMMENT '头像',
  `nick_name` varchar(45) DEFAULT NULL COMMENT '昵称',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `gender` tinyint(4) DEFAULT NULL COMMENT '性别 0: 男 1: 女',
  `score` int(11) NOT NULL DEFAULT '0' COMMENT '用户总积分',
  `auth_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0: 未认证，1: 已认证',
  `auth_time` datetime DEFAULT NULL COMMENT '认证时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_third_party_id` (`third_party_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_user_location_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_location_info`;
CREATE TABLE `t_user_location_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cust_id` int(11) DEFAULT NULL,
  `to_username` varchar(64) DEFAULT NULL COMMENT '开发者微信号',
  `from_username` varchar(64) DEFAULT NULL COMMENT '发送方帐号（一个openid）',
  `msg_type` varchar(32) DEFAULT NULL COMMENT '消息类型，event',
  `event` varchar(32) DEFAULT NULL COMMENT '事件类型，location',
  `longitude` decimal(15,10) DEFAULT NULL COMMENT '地理位置经度',
  `precision` decimal(15,10) DEFAULT NULL COMMENT '地理位置精度',
  `latitude` decimal(15,10) DEFAULT NULL COMMENT '地理位置纬度',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(128) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户地理位置信息表';

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='用户授权角色';

-- ----------------------------
-- Table structure for t_verify_code
-- ----------------------------
DROP TABLE IF EXISTS `t_verify_code`;
CREATE TABLE `t_verify_code` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `mobile` char(11) DEFAULT NULL COMMENT '手机号',
  `verify_code` char(6) NOT NULL COMMENT '短信验证码，6位数字',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `email` varchar(64) DEFAULT NULL COMMENT '用户邮箱',
  `flag` tinyint(1) DEFAULT '0' COMMENT '0:短信验证码，1：邮箱验证码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_warning_group
-- ----------------------------
DROP TABLE IF EXISTS `t_warning_group`;
CREATE TABLE `t_warning_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '组名称',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '0: 有效，1: 无效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='报警组';

-- ----------------------------
-- Table structure for t_warning_item
-- ----------------------------
DROP TABLE IF EXISTS `t_warning_item`;
CREATE TABLE `t_warning_item` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '报警项名称',
  `send_sms` tinyint(1) NOT NULL COMMENT '是否发短信（0：否  1：是）',
  `send_email` tinyint(1) NOT NULL COMMENT '是否发邮件（0：否  1：是）',
  `sms_tpl` varchar(255) DEFAULT NULL COMMENT '短信模板',
  `email_title` varchar(255) DEFAULT NULL COMMENT '邮件标题',
  `email_tpl` varchar(255) DEFAULT NULL COMMENT '邮件模板',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '0: 有效，1: 无效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_name` (`name`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='报警项';

-- ----------------------------
-- Table structure for t_warning_item_group
-- ----------------------------
DROP TABLE IF EXISTS `t_warning_item_group`;
CREATE TABLE `t_warning_item_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `warning_item_id` int(11) NOT NULL COMMENT '预警项ID',
  `warning_group_id` int(11) NOT NULL COMMENT '预警组ID',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '0: 有效，1: 无效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key_item_id_group_id` (`warning_item_id`,`warning_group_id`),
  KEY `idx_warning_item_id` (`warning_item_id`),
  KEY `idx_warning_group_id` (`warning_group_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='报警项发送给哪些报警组';

-- ----------------------------
-- Table structure for t_warning_user
-- ----------------------------
DROP TABLE IF EXISTS `t_warning_user`;
CREATE TABLE `t_warning_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '用户姓名',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号，用于短信报警',
  `email` varchar(40) DEFAULT NULL COMMENT '邮箱，用户邮件报警',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '0: 有效，1: 无效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='报警用户';

-- ----------------------------
-- Table structure for t_warning_user_group
-- ----------------------------
DROP TABLE IF EXISTS `t_warning_user_group`;
CREATE TABLE `t_warning_user_group` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `warning_user_id` int(11) NOT NULL COMMENT '用户ID',
  `warning_group_id` int(11) NOT NULL COMMENT '组ID',
  `invalid` tinyint(1) DEFAULT '0' COMMENT '0: 有效，1: 无效',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_warning_user_id` (`warning_user_id`),
  KEY `idx_warning_group_id` (`warning_group_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='报警用户和组的关联表';


CREATE TABLE `t_merchant_audit` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `merchant_id` int(11) NOT NULL,
  `creator_id` int(11) NOT NULL COMMENT '审核人id',
  `audit_result` varchar(1024) DEFAULT NULL COMMENT '审核结果',
  `description` varchar(1024) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商户审核记录';


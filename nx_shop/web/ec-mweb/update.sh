#!/bin/bash

#str='ALTER TABLE t_tag ADD COLUMN `code` varchar(255) DEFAULT NULL COMMENT "标签代号" AFTER `name`;'
str='ALTER TABLE `t_tag_sku` ADD COLUMN `invalid` tinyint(1) DEFAULT "0" COMMENT "是否已删除" AFTER `sku_id`; ALTER TABLE `t_tag_sku` ADD COLUMN `update_time` datetime DEFAULT NULL COMMENT "更新时间" AFTER `create_time`;'
str='ALTER TABLE `t_tag` ADD COLUMN `level` tinyint(1) DEFAULT NULL COMMENT "末级节点的层级，从1（这棵树只有一个节点）开始" AFTER `name`;'
str='ALTER TABLE t_spu_attribute ADD COLUMN `attribute_values` varchar(255) DEFAULT NULL COMMENT "spu属性所有值" after `value_constraint_expression`;'
str='ALTER TABLE `t_spu_spec` ADD COLUMN `spec_values` varchar(255) DEFAULT NULL COMMENT "spu品牌所有值" AFTER `spec_value`;'
str='ALTER TABLE `t_tag` CHANGE COLUMN `depth` `tree_depth` tinyint(1) DEFAULT NULL COMMENT "整棵树的深度，从1（这棵树只有一个节点）开始，只在根节点赋值";'
str='ALTER TABLE `t_tag` ADD COLUMN `status` tinyint(1) DEFAULT 0 COMMENT "状态 1已启用 0未启用" after invalid;'
str='CREATE TABLE if not exists `t_unifiedorder` (`id` int(11) NOT NULL AUTO_INCREMENT COMMENT "自增主键",`appid` varchar(255) DEFAULT NULL COMMENT "公众账号ID",`body` varchar(255) DEFAULT NULL COMMENT "商品描述",`device_info` varchar(255) DEFAULT NULL COMMENT "设备号", `fee_type` varchar(255) DEFAULT NULL COMMENT "货币类型 默认人民币：CNY",`mch_id` varchar(255) DEFAULT NULL COMMENT "商户号",`nonce_str` varchar(255) DEFAULT NULL COMMENT "随机字符串",`notify_url` varchar(255) DEFAULT NULL COMMENT "通知地址",`openid` varchar(255) DEFAULT NULL COMMENT "用户标识",`out_trade_no` varchar(255) DEFAULT NULL COMMENT "商户订单号",`spbill_create_ip` varchar(255) DEFAULT NULL COMMENT "终端IP",`total_fee` int(11) DEFAULT NULL COMMENT "订单总金额，单位为分",`trade_type` varchar(255) DEFAULT NULL COMMENT "交易类型",`sign` varchar(255) DEFAULT NULL COMMENT "签名",`attach` varchar(255) DEFAULT NULL COMMENT "附加数据", PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT="微信预支付信息表";'
str='truncate t_spec; truncate t_tag;truncate t_spu; truncate t_sku; truncate t_sku_attribute; truncate t_attribute; truncate t_spu_attribute; truncate t_category; truncate t_category_template; truncate t_order; truncate t_order_payment; truncate t_order_cancellation; truncate t_order_sku; truncate t_spu_spec; truncate t_tag_sku;'
str='ALTER TABLE `t_spec` CHANGE COLUMN `value` `value` varchar(2000) DEFAULT NULL COMMENT "所有值 多个用,分隔";'
str='ALTER TABLE `t_category_template` CHANGE COLUMN `value` `value` varchar(256) DEFAULT NULL COMMENT "规格或属性值，多个用,分隔";'
str='ALTER TABLE `t_attribute` CHANGE COLUMN `value` `value` varchar(2000) DEFAULT NULL COMMENT "所有值 多个用,分隔";'
str='ALTER TABLE `t_spu_attribute` CHANGE COLUMN `attribute_values` `attribute_values` varchar(255) DEFAULT NULL COMMENT "spu属性所有值，,分隔";'
str='UPDATE t_spu_attribute SET value_constraint_expression = REPLACE(value_constraint_expression, "\t", ","), attribute_values = REPLACE(attribute_values, "\\t", ",");UPDATE t_category_template SET `value` = REPLACE(`value`, "\\t", ",");'

# dev
echo $str | mysql -h192.168.17.128 -uec -pec ec_thb
echo $str | mysql -h192.168.17.128 -uec -pec ec_ceb
echo $str | mysql -h192.168.17.128 -uec -pec ec_tyfq

# test
echo $str | mysql -h192.168.17.226 -uec -pec ec_thb
echo $str | mysql -h192.168.17.226 -uec -pec ec_ceb
echo $str | mysql -h192.168.17.226 -uec -pec ec_tyfq

# stage
#echo $str | mysql -h192.168.17.227 -uec -pec ec_thb
#echo $str | mysql -h192.168.17.227 -uec -pec ec_ceb
#echo $str | mysql -h192.168.17.227 -uec -pec ec_tyfq


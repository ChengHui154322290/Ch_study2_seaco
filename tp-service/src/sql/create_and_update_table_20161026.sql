ALTER TABLE `sup_supplier_shop`
ADD COLUMN `business_time`  varchar(64) NULL COMMENT '营业时间' ,
ADD COLUMN `longitude`  float NULL COMMENT '经度' AFTER `business_time`,
ADD COLUMN `latitude`  float(255,0) NULL COMMENT '纬度' AFTER `longitude`,
ADD COLUMN `shop_tel`  varchar(64) NULL COMMENT '店铺电话' AFTER `latitude`,
ADD COLUMN `shop_address`  varchar(255) NULL COMMENT '店铺地址' AFTER `shop_tel`;




ALTER TABLE `sup_supplier_shop`
ADD COLUMN `business_time`  varchar(64) NULL COMMENT '营业时间' ,
ADD COLUMN `longitude`  float NULL COMMENT '经度' AFTER `business_time`,
ADD COLUMN `latitude`  float(255,0) NULL COMMENT '纬度' AFTER `longitude`,
ADD COLUMN `shop_tel`  varchar(64) NULL COMMENT '店铺电话' AFTER `latitude`,
ADD COLUMN `shop_address`  varchar(255) NULL COMMENT '店铺地址' AFTER `shop_tel`;



CREATE TABLE `ord_order_redeem_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `parent_order_id` bigint(20) DEFAULT NULL COMMENT '父订单ID',
  `parent_order_code` varchar(32) DEFAULT NULL COMMENT '父订单代码',
  `order_id` bigint(20) NOT NULL COMMENT '子订单ID',
  `order_code` bigint(20) NOT NULL COMMENT '订单编码',
  `sku_code` varchar(32) NOT NULL COMMENT '商品sku编码',
  `redeem_code` varchar(32) DEFAULT NULL COMMENT '兑换码',
  `supplier_id` bigint(11) NOT NULL COMMENT '供应商id',
  `warehouse_id` bigint(32) DEFAULT NULL COMMENT '仓库ID',
  `shop_name` varchar(255) DEFAULT NULL COMMENT '店铺名称',
  `sales_price` double(10,2) NOT NULL COMMENT '兑换码金额',
  `redeem_code_state` int(1) DEFAULT NULL COMMENT '兑换码状态  1：未使用 0：已使用 3：已到期  4：已退款',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `effect_time_start` datetime DEFAULT NULL COMMENT '兑换码有效开始日期',
  `effect_time_end` datetime DEFAULT NULL COMMENT '兑换码有效截止日期',
  `redeem_name` varchar(255) DEFAULT NULL COMMENT '兑换码名称',
  `update_user` varchar(32) DEFAULT NULL,
  
  PRIMARY KEY (`id`),
  KEY `idx_order_promotion_order_id` (`parent_order_id`) USING BTREE,
  KEY `idx_order_promotion_sub_order_id` (`order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=248 DEFAULT CHARSET=utf8 COMMENT='商家线下购买兑换码信息';










ALTER TABLE `ord_sub_order`
ADD COLUMN `receive_tel`  varchar(32) NULL COMMENT '接收手机号码';








ALTER TABLE `ord_order_info`
ADD COLUMN `receive_tel`  varchar(32) NULL COMMENT '接收手机号码' AFTER `update_user`;



ALTER TABLE `ord_order_redeem_item`
ADD COLUMN `spu_name`  varchar(100) NULL AFTER `warehouse_id`;


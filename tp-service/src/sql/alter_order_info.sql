ALTER TABLE `xg`.`ord_order_info` CHANGE `remark` `remark` VARCHAR(255) NULL COMMENT '订单备注', CHANGE `deleted` `deleted` TINYINT(1) DEFAULT 0 NOT NULL COMMENT '是否删除（1.删除 2. 不删除）';
ALTER TABLE `xg`.`ord_sub_order` CHANGE `discount` `discount` DOUBLE(10,2) DEFAULT 0 NOT NULL COMMENT '优惠金额', CHANGE `freight` `freight` DOUBLE(10,2) DEFAULT 0 NOT NULL COMMENT '子单运费', CHANGE `balance` `balance` DOUBLE(10,2) DEFAULT 0 NULL COMMENT '使用余额', CHANGE `points` `points` INT(11) DEFAULT 0 NULL COMMENT '使用积分', CHANGE `supplier_name` `supplier_name` VARCHAR(30) NULL COMMENT '供应商名称', CHANGE `supplier_alias` `supplier_alias` VARCHAR(30) NULL COMMENT '供应商别名', CHANGE `warehouse_name` `warehouse_name` VARCHAR(20) NULL COMMENT '仓库名称', CHANGE `deleted` `deleted` TINYINT(1) DEFAULT 2 NOT NULL COMMENT '是否删除（1.删除 2. 不删除）', CHANGE `remark` `remark` VARCHAR(255) NULL COMMENT '订单备注', CHANGE `tax_fee` `tax_fee` DOUBLE(5,2) DEFAULT 0 NULL COMMENT '税费';
ALTER TABLE `xg`.`ord_order_item` CHANGE `type` `type` TINYINT(3) DEFAULT 1 NULL COMMENT '类型（1：商品行。2：赠品行）', CHANGE `topic_id` `topic_id` BIGINT(20) NOT NULL COMMENT '主题ID', CHANGE `supplier_id` `supplier_id` BIGINT(20) NOT NULL COMMENT '供应商ID'; 
ALTER TABLE `ord_order_item`
MODIFY COLUMN `sku_id`  bigint(20) NULL COMMENT 'SKU ID' AFTER `brand_Name`;
ALTER TABLE `ord_mem_realinfo`
MODIFY COLUMN `address`  varchar(255) NULL COMMENT '用户常住地址' AFTER `identity_back_img`;
ALTER TABLE `ord_order_promotion`
MODIFY COLUMN `type`  tinyint(3) NOT NULL DEFAULT 0 COMMENT '类型（整单优惠、赠品、包邮等等）' AFTER `id`;
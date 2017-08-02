-- 新建海外直邮订单操作日志表
CREATE TABLE `ord_derict_operat_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_code` bigint(20) NOT NULL COMMENT '子订单编号',
  `operat_type` tinyint(4) NOT NULL COMMENT '操作类型 推送-1，查询-2',
  `is_success` tinyint(4) NOT NULL COMMENT '是否成功 成功-1，失败-0',
  `operat_message` varchar(255) DEFAULT NULL COMMENT '操作信息记录',
  `create_user` varchar(255) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='海外直邮订单操作日志表';


-- 子订单表新增字段
ALTER TABLE ord_sub_order ADD COLUMN direct_order_status tinyint(3)  NOT NULL DEFAULT 0 COMMENT '海外直邮订单推送状态 未推送-0，推送成功-1，推送失败-2';


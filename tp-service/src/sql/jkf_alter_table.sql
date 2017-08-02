/**
 * 增加海关推送状态和数据
 */
alter TABLE ord_sub_order ADD clearance_status int(20) DEFAULT 0 COMMENT '清关状态';
/* alter TABLE ord_sub_order ADD put_customs_status int(20) DEFAULT 0 COMMENT '订单推送海关状态'; */
alter TABLE ord_sub_order ADD put_order_status int(11) DEFAULT 0 COMMENT '订单推送海关状态' after clearance_status;
alter TABLE ord_sub_order ADD put_personalgoods_status int(11) DEFAULT 0 COMMENT '清关单推送海关状态' after put_order_status;
alter TABLE ord_sub_order ADD put_waybill_status int(11) DEFAULT 0 COMMENT '运单推送海关状态' after put_personalgoods_status;
alter TABLE ord_sub_order ADD put_pay_status int(11) DEFAULT 0 COMMENT '支付单推送海关状态' after put_waybill_status;
alter TABLE ord_sub_order ADD put_customs_times int(11) DEFAULT 0 COMMENT '订单推送海关次数' after put_pay_status;
/**
 *	修改商品行数据
 */
alter TABLE ord_order_item  ADD unit_id BIGINT(20) DEFAULT 0 COMMENT '计量单位' after unit;
alter TABLE ord_order_item  ADD country_id BIGINT(20) DEFAULT 0 COMMENT '原产地'; 
alter TABLE ord_order_item  ADD weight_net double(10,2) DEFAULT 0.0 COMMENT '商品净重' after weight;
alter TABLE ord_order_item  ADD unit_quantity int(20) DEFAULT 1 COMMENT '商品净数量-多件装商品数量' after quantity;
alter TABLE ord_order_item  ADD wrap_quantity int(20) DEFAULT 1 COMMENT '商品独立包装数' after unit_quantity;
/**
 * 修改商品详情数据
 */
alter TABLE prd_item_detail ADD unit_quantity int(20) DEFAULT 1 COMMENT '商品净数量-组合商品中计量单位对应的数量' after weight_net;
alter TABLE prd_item_detail ADD wrap_quantity int(20) DEFAULT 1 COMMENT '商品内独立包装数' after unit_quantity;
/**
 * 修改海关备案商品信息 
 */
alter TABLE prd_item_sku_art ADD item_first_unit_code varchar(32) DEFAULT NULL COMMENT '第一单位编号' after hs_code;
alter TABLE prd_item_sku_art ADD item_first_unit varchar(32) DEFAULT NULL COMMENT '海关备案的商品计量第一申报单位' after item_first_unit_code;
alter TABLE prd_item_sku_art ADD item_first_unit_count varchar(32) DEFAULT NULL COMMENT '第一单位对应数量' after item_first_unit;
alter TABLE prd_item_sku_art ADD item_second_unit_code varchar(32) DEFAULT NULL COMMENT '第二单位编号' after item_first_unit_count;
alter TABLE prd_item_sku_art ADD item_second_unit varchar(32) DEFAULT NULL COMMENT '海关备案的商品计量第二申报单位' after item_second_unit_code;
alter TABLE prd_item_sku_art ADD item_second_unit_count varchar(32) DEFAULT NULL COMMENT '第二单位对应数量' after item_second_unit;
alter TABLE prd_item_sku_art ADD item_price Double(10,2) DEFAULT 0.0 COMMENT '海关备案的商品价' after item_second_unit_count;
alter TABLE prd_item_sku_art ADD item_record_no varchar(50) DEFAULT NULL COMMENT '国检备案号' after item_price;

/**
 * 修改支付表信息 
 */
ALTER TABLE pay_payment_info ADD COLUMN payment_customs_no varchar(32) DEFAULT NULL COMMENT '支付报关流水号';

INSERT INTO pay_customs_info VALUES
(1,6,'91330103MA27W0K5XR','杭州西客商城贸易有限公司','HANGZHOU',1,7,'ZF14021901'),
(2,6,'91330103MA27W0K5XR','杭州西客商城贸易有限公司','HANGZHOU',1,8,'ZF14120401');


CREATE TABLE IF NOT EXISTS `ord_personalgoods_declare_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_code` bigint(20) NOT NULL COMMENT '订单编号',
  `declare_customs` varchar(20) NOT NULL COMMENT '申报单申报平台如浙江杭州电子口岸对应JKF',
  `declare_no` varchar(30) DEFAULT NULL COMMENT '个人物品申报单单号(订单号)',
  `pre_entry_no` varchar(30) NOT NULL COMMENT '个人物品申报单预录入号码(入库时生成)',
  `personalgoods_no` varchar(50) DEFAULT NULL COMMENT '个人物品申报单编号(海关生成)', 
  `company_no` varchar(20) DEFAULT NULL COMMENT '物流公司编号',
  `company_name` varchar(255) DEFAULT NULL COMMENT '物流公司名称',
  `express_no` varchar(255) NOT NULL COMMENT '快递单号(海淘订单只有一个包裹)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人物品申报数据表';

CREATE TABLE IF NOT EXISTS `ord_order_declare_receipt_log`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_code` bigint(20) NOT NULL COMMENT '订单编号(子单)',
  `declare_no` varchar(30) NOT NULL COMMENT '申报单号',
  `customs_code` varchar(30) NOT NULL COMMENT '海关编号',
  `result` tinyint(1) DEFAULT NULL COMMENT '结果（1成功2处理失败）',
  `result_detail` varchar(512) DEFAULT NULL COMMENT '结果详情',
  `res_msg` varchar(2048) DEFAULT NULL COMMENT '回执原始数据',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单申报回执流水日志';

CREATE TABLE IF NOT EXISTS `ord_personalgoods_status_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_code` bigint(20) NOT NULL COMMENT '订单号',
  `declare_no` varchar(20) DEFAULT NULL COMMENT '申报单号',
  `customs_declare_no` varchar(30) DEFAULT NULL COMMENT '海关生成申报单编号',
  `customs_code` varchar(30) NOT NULL COMMENT '海关编号',
  `type` tinyint(1) NOT NULL COMMENT '类型：0回调1查询',
  `result` varchar(20) DEFAULT NULL COMMENT '结果',
  `result_desc` varchar(20) DEFAULT NULL COMMENT '结果描述',
  `result_detail` varchar(512) DEFAULT NULL COMMENT '详情',
  `res_data` varchar(2048) DEFAULT NULL COMMENT '回执原始数据',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人物品申报单审单状态日志(包括回执与主动查询)';


CREATE TABLE IF NOT EXISTS `ord_personalgoods_tax_receipt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_code` bigint(20) DEFAULT NULL COMMENT '订单编号',
  `personalgoods_no` varchar(30) DEFAULT NULL COMMENT '申报单号(海关生成的编号)',
  `express_no` varchar(255) NOT NULL COMMENT '运单编号',
  `is_tax` tinyint(1) DEFAULT NULL COMMENT '税费是否应征，0免征1应征',
  `tax_amount` double(10,2) DEFAULT NULL COMMENT '税额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人物品申报税费回执';

CREATE TABLE IF NOT EXISTS `ord_cancel_customs_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `cancel_id` bigint(20) DEFAULT NULL COMMENT '取消单的ID',
  `order_code` bigint(20) NOT NULL COMMENT '订单编号(子单)',
  `customs_code` varchar(30) NOT NULL COMMENT '海关编号',
  `express_no` varchar(50) NOT NULL COMMENT '运单号',
  `express_company_code` varchar(30) DEFAULT NULL COMMENT '物流公司编号',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `supplier_name` varchar(50) DEFAULT NULL COMMENT '供应商名称',
  `cancel_status` tinyint(11) NOT NULL COMMENT '取消单状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='取消海淘单海关申报';

CREATE TABLE IF NOT EXISTS `ord_cancel_customs_receipt_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_code` bigint(20) NOT NULL COMMENT '订单编号(子单)',
  `customs_code` varchar(30) NOT NULL COMMENT '海关编号',
  `approve_result` int(1) DEFAULT NULL COMMENT '审批结果（21撤销成功22撤销失败）',
  `approve_comment` varchar(512) DEFAULT NULL COMMENT '海关审批意见',
  `res_msg` varchar(2048) DEFAULT NULL COMMENT '回执原始数据',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='取消海淘单海关申报回执流水日志';

CREATE TABLE IF NOT EXISTS `ord_customs_clearance_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_code` bigint(20) NOT NULL COMMENT '订单编号(子单)',
  `customs_code` varchar(30) NOT NULL COMMENT '海关编号',
  `type` tinyint(1) NOT NULL COMMENT '类型1订单申报2运单申报3个人物品申报4清关状态',
  `result` tinyint(1) NOT NULL COMMENT '结果0失败1成功',
  `result_desc` varchar(500) DEFAULT NULL COMMENT '结果简述',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单相关申报海关全日志';

/*--------------------------------------系统日志--------------------------------------------*/
CREATE TABLE IF NOT EXISTS `sys_apicall_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uri` varchar(60) NOT NULL COMMENT '地址',
  `ip` varchar(32) NOT NULL COMMENT 'ip地址',
  `content_type` varchar(50) DEFAULT NULL COMMENT '内容类型',
  `content_len` bigint(20) DEFAULT NULL COMMENT '内容长度',
  `method_name` varchar(60) DEFAULT NULL COMMENT '接口名',
  `request_time` datetime DEFAULT NULL COMMENT '请求时间',
  `return_time` datetime DEFAULT NULL COMMENT '返回时间',
  `param` text DEFAULT NULL COMMENT '请求参数:json',
  `header` text DEFAULT NULL COMMENT '请求头信息',
  `content` text DEFAULT NULL COMMENT '请求内容',
  `method` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `result` text DEFAULT NULL COMMENT '返回参数',
  `timelapse` bigint(20) DEFAULT NULL COMMENT '耗时',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='API请求日志表';

COMMIT;

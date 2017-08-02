drop table wms_export_config;
drop table wms_stock_asn_receive;
drop table wms_stock_asn_receive_item;
drop table wms_stock_out;
drop table wms_stock_out_audit;
drop table wms_stock_out_cancel;
drop table wms_stock_out_decl;
drop table wms_stock_out_decl_item;
drop table wms_stock_out_item;

/**
 * 公共仓对接
 *  
 */

/* -----------------WMS出库相关表---------------------- */
CREATE TABLE IF NOT EXISTS `wms_stockout` (
   id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   order_code varchar(30) NOT NULL COMMENT '订单编号',
   order_type varchar(20) DEFAULT NULL COMMENT '订单类型',
   warehouse_id bigint(20) NOT NULL COMMENT '仓库id',
   warehouse_code varchar(20) NOT NULL COMMENT '仓库编号',
   warehouse_name varchar(50) DEFAULT NULL COMMENT '仓库名称',
   order_create_time datetime NOT NULL COMMENT '订单创建时间',
   pay_time datetime NOT NULL COMMENT '订单支付时间',
   total_amount double(10,2) DEFAULT NULL COMMENT '订单总金额',
   pay_amount double(10, 2) DEFAULT NULL COMMENT '订单实付金额',
   discount double(10, 2) DEFAULT NULL COMMENT '订单优惠金额',
   postage double(10,2) DEFAULT NULL COMMENT '邮费',
   is_postage_pay tinyint(1) DEFAULT NULL COMMENT '邮费是否到付:0是1否',
   is_delivery_pay tinyint(1) DEFAULT NULL COMMENT '是否货到付款:0是1否',
   is_urgency tinyint(1) DEFAULT NULL COMMENT '是否紧急：0普通1紧急',
   logistics_company_code varchar(32) DEFAULT NULL COMMENT '物流公司编码',
   logistics_company_name varchar(50) DEFAULT NULL COMMENT '物流公司名称',
   express_no varchar(50) DEFAULT NULL COMMENT '快递单号',
   member_id bigint(20) DEFAULT NULL COMMENT '会员ID',
   member_name varchar(50) DEFAULT NULL COMMENT '会员昵称',
   consignee varchar(50) NOT NULL COMMENT '收货人姓名',
   post_code varchar(20) DEFAULT NULL COMMENT '邮编',
   province varchar(50) NOT NULL COMMENT '省名称',
   city varchar(50) NOT NULL COMMENT '市名称',
   area varchar(50) NOT NULL COMMENT '区名称',
   address varchar(500) NOT NULL COMMENT '收件地址',
   mobile varchar(20) NOT NULL COMMENT '移动电话',
   tel varchar(20) DEFAULT NULL COMMENT '固定电话',
   is_invoice tinyint(1) DEFAULT NULL COMMENT '是否需要开发票：0是1否',
   remark varchar(500) DEFAULT NULL COMMENT '备注',
   status tinyint(1) DEFAULT NULL COMMENT '状态 0 失败 1 成功',
   fail_times int(11) DEFAULT NULL COMMENT '失败次数',
   error_msg varchar(500) DEFAULT NULL COMMENT '失败原因',
   create_time datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_code` (`order_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出库订单';

CREATE TABLE IF NOT EXISTS `wms_stockout_detail` (
   id bigint(20) NOT NULL AUTO_INCREMENT,
   stockout_id bigint(20) NOT NULL COMMENT '出库单ID', 
   item_sku varchar(50) NOT NULL COMMENT 'SKU编码',
   item_name varchar(200) NOT NULL COMMENT 'SKU名称',
   item_barcode varchar(50) NOT NULL COMMENT '商品条形码',
   quantity int(11) NOT NULL COMMENT '数量',
   actual_price double(12, 2) NOT NULL COMMENT '实际价格',  
   sales_price double(12,2) DEFAULT NULL COMMENT '销售价格',
   discount_amount double(12,2) DEFAULT NULL COMMENT '优惠金额',
   create_time datetime NOT NULL,
   PRIMARY KEY (`id`),
   KEY `stockout_id` (`stockout_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出库订单明细';

CREATE TABLE IF NOT EXISTS `wms_stockout_invoice` (
   id bigint(20) NOT NULL AUTO_INCREMENT,
   order_code varchar(30) NOT NULL COMMENT '订单编号',
   invoice_code varchar(50) DEFAULT NULL COMMENT '发票代码',
   invoice_no varchar(50) DEFAULT NULL COMMENT '发票号码',
   invoice_type tinyint(1) NOT NULL COMMENT '发票类型：1增值税发票2普通发票',
   invoice_title varchar(100) NOT NULL COMMENT '发票抬头',
   invoice_amount varchar(20) NOT NULL COMMENT '发票金额',
   invoice_content varchar(200) DEFAULT NULL COMMENT '发票内容',
   create_time datetime NOT NULL,
   PRIMARY KEY (`id`),
   KEY `order_code` (`order_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单发票';

/* ---------- 取消配货相关表 -------------*/
CREATE TABLE IF NOT EXISTS `wms_stockout_cancel` (
   id bigint(20) NOT NULL AUTO_INCREMENT,
   order_code varchar(50) NOT NULL COMMENT '配货单号',
   req_msg varchar(1000) DEFAULT NULL COMMENT '取消配货数据',
   cancel_time datetime NOT NULL COMMENT '取消时间',
   success tinyint(1) NOT NULL COMMENT '取消成功：0失败1成功',
   error varchar(500) DEFAULT NULL COMMENT '错误信息',
   res_msg varchar(1000) DEFAULT NULL COMMENT '返回信息',
   create_time datetime NOT NULL COMMENT '记录创建时间',
   PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='取消配货表';


/*-----------入库订单相关表------------- */
CREATE TABLE IF NOT EXISTS `wms_stockasn` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_code` varchar(50) NOT NULL COMMENT '采购单号',
  `order_type` varchar(10) NOT NULL COMMENT '业务类型',
  `contract_code` varchar(50) DEFAULT NULL COMMENT '合同号',
  `supplier_code` varchar(50) DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(50) DEFAULT NULL COMMENT '供应商名称',
  `order_create_time` datetime NOT NULL COMMENT '订单生成时间',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库id',
  `warehouse_code` varchar(50) NOT NULL COMMENT '仓库编号',
  `warehouse_name` varchar(50) DEFAULT NULL COMMENT '仓库名称', 
  `plan_start_time` datetime NOT NULL COMMENT '预计到货开始日期',
  `plan_over_time` datetime NOT NULL COMMENT '预计到货截止日期',
  `gross_weight` double(12, 2) NOT NULL COMMENT '毛重',
  `net_weight` double(12,2) NOT NULL COMMENT '净重',
  `amount` int(11) NOT NULL COMMENT '件数',
  `batch_no` varchar(30) DEFAULT NULL COMMENT '批次号',
  `logistics_code` varchar(30) DEFAULT NULL COMMENT '物流公司编码',
  `express_code` varchar(50) DEFAULT NULL COMMENT '快递单号',
  `pre_order_code` varchar(30) DEFAULT NULL COMMENT '前物流订单号',
  `consignee` varchar(50) DEFAULT NULL COMMENT '收货人姓名',
  `post_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `province` varchar(50) DEFAULT NULL COMMENT '省名称',
  `city` varchar(50) DEFAULT NULL COMMENT '市名称',
  `area` varchar(50) DEFAULT NULL COMMENT '区名称',
  `address` varchar(500) DEFAULT NULL COMMENT '收件地址',
  `mobile` varchar(20) DEFAULT NULL COMMENT '移动电话',
  `tel` varchar(20) DEFAULT NULL COMMENT '固定电话',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态 0 失败 1 成功',
  `fail_times` int(11) DEFAULT NULL COMMENT '失败次数',
  `error_msg` varchar(500) DEFAULT NULL COMMENT '失败原因',
  `create_time` datetime DEFAULT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `create_user` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_code` (`order_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='入库订单';

CREATE TABLE IF NOT EXISTS `wms_stockasn_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stockasn_id` bigint(20) NOT NULL COMMENT '入库订单id',
  `item_sku` varchar(50) NOT NULL COMMENT 'sku',
  `item_barcode` varchar(50) NOT NULL COMMENT '商品条形码',
  `inventory_type` varchar(10) DEFAULT NULL COMMENT '库存类型',
  `quantity` int(11) NOT NULL COMMENT '申报数量',
  `actual_price` double(10,2) DEFAULT 0 COMMENT '商品实际价格',
  `price` double(10,2) DEFAULT NULL COMMENT '销售价格',
  `country_code` varchar(10) DEFAULT NULL COMMENT '原产国国别代码',
  `country_name` varchar(20) DEFAULT NULL COMMENT '原产国名字',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `stockasn_id` (`stockasn_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公共仓入库订单明细';

/*---------------出库单反馈 --------------------*/
CREATE TABLE IF NOT EXISTS `wms_stockout_back` (
   id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   order_code varchar(30) NOT NULL COMMENT '订单号',
   warehouse_code varchar(20) NOT NULL COMMENT '仓库编号',
   express_no varchar(30) DEFAULT NULL COMMENT '运单号',
   logistics_company_code varchar(50) NOT NULL COMMENT '物流企业编码',
   logistics_company_name varchar(50) NOT NULL COMMENT '物流企业名称',
   weight double(10,2) DEFAULT NULL COMMENT '包裹重量',
   auditor varchar(50) DEFAULT NULL COMMENT '审核人',
   audit_time datetime DEFAULT NULL COMMENT '审核时间',
   create_time datetime NOT NULL,
   status tinyint(4) NOT NULL DEFAULT 1 COMMENT '发货处理状态:0失败1成功',
   message varchar(200) DEFAULT NULL COMMENT '发货处理描述', 
  PRIMARY KEY (`id`),
  KEY `order_code` (`order_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出库单回执';

CREATE TABLE IF NOT EXISTS `wms_stockout_back_detail` (
   id bigint(20) NOT NULL AUTO_INCREMENT,
   stockout_back_id bigint(20) NOT NULL COMMENT '出库单回执ID', 
   item_sku varchar(50) NOT NULL COMMENT 'SKU编码(平台方SKU)',
   stock_sku varchar(50) NOT NULL COMMENT '仓库方SKU',
   product_no varchar(50) DEFAULT NULL COMMENT '商品编码',	
   quantity int(11) NOT NULL COMMENT '数量',
   weight double(10,2) DEFAULT NULL COMMENT '单个商品重量 单位g', 
   create_time datetime NOT NULL,
   PRIMARY KEY (`id`),
   KEY `stockout_back_id` (`stockout_back_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='出库单回执明细';

/*--------------- 库存同步 --------------------*/
CREATE TABLE IF NOT EXISTS `wms_stocksync_info` (
   id bigint(20) NOT NULL AUTO_INCREMENT,
   wh_id bigint(20) NOT NULL COMMENT '仓库ID',
   wms_code varchar(50) DEFAULT NULL COMMENT '仓库WMS编号',
   sku_name varchar(50) DEFAULT NULL COMMENT '商品名称', 
   stock_sku varchar(50) NOT NULL COMMENT '仓库SKU',
   sku varchar(50) NOT NULL COMMENT '商品SKU',
   stock_inventory bigint(20) NOT NULL COMMENT '仓库返回实际库存',
   inventory bigint(20) NOT NULL COMMENT '系统库存',
   sync_time datetime NOT NULL COMMENT '同步时间',
   create_time datetime NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='库存同步信息表';

/*--------------------入库回执--------------------------*/
CREATE TABLE IF NOT EXISTS `wms_stockasn_detail_fact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `stockasn_fact_id` bigint(20) NOT NULL,
  `sku` varchar(45) NOT NULL COMMENT '我方sku',
  `sku_tp` varchar(45) DEFAULT NULL COMMENT '第三方sku',
  `quantity` int(11) NOT NULL COMMENT '实际入库数量',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `wms_stockasn_fact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `stockasn_id` bigint(20) NOT NULL COMMENT '入库订单号',
  `order_code` varchar(45) NOT NULL COMMENT '采购单号',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库Id',
  `warehouse_code` varchar(45) NOT NULL COMMENT '仓库CODE',
  `auditor` varchar(45) DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `provider_code` varchar(45) DEFAULT NULL COMMENT '电商编码',
  `goods_owner` varchar(100) DEFAULT NULL COMMENT '货主',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*-------------------------导表日志---------------------------*/
CREATE TABLE IF NOT EXISTS `wms_stockin_import_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `real_file_name` varchar(60) NOT NULL COMMENT '上传文件名称',
  `file_name` varchar(60) DEFAULT NULL COMMENT '文件名(存储在文件服务器上的名称)',
  `total_amount` int(8) DEFAULT NULL COMMENT 'excel总行数',
  `success_count` int(8) DEFAULT NULL COMMENT '成功的行数',
  `fail_count` int(8) DEFAULT NULL COMMENT '失败条数',
  `file_key` varchar(60) DEFAULT NULL COMMENT '文件密钥(MD5文件数据)',
  `upload_token` varchar(60) NOT NULL COMMENT '上传token',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '导入时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='入库单导入日志表';


CREATE TABLE IF NOT EXISTS `wms_stockin_import_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `log_id` bigint(10) NOT NULL COMMENT '导入记录主键 ',
  `upload_token` varchar(50) NOT NULL COMMENT 'token标识',
  `purchase_code` varchar(50) NOT NULL COMMENT '*采购单号',
  `warehouse_code` varchar(50) NOT NULL COMMENT '*仓库编号',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '仓库ID',
  `barcode` varchar(50) NOT NULL COMMENT '*条形码（商品码）',
  `sku_code` varchar(50) NOT NULL COMMENT '*商品SKU',
  `article_number` varchar(50) NOT NULL COMMENT '*备案料号',
  `fact_amount` bigint(20) NOT NULL COMMENT '*实际入库数量',
  `plan_amount` bigint(20) DEFAULT 0 COMMENT '计划入库数量',
  `stockin_time` datetime NOT NULL COMMENT '*实际入库时间',
  `operator` varchar(50) DEFAULT NULL COMMENT '操作人',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `excel_index` bigint(8) NOT NULL COMMENT '导入的excel行号',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '状态:1-导入成功，2-导入失败，默认1',
  `op_message` text COMMENT '操作描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(100) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='入库单导入明细表';

COMMIT;


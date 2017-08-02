/**
 * 申通对接
 *  
 */

CREATE TABLE IF NOT EXISTS wms_waybill_detail (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  waybill_no bigint(20) NOT NULL COMMENT '运单号',
  application_id bigint(20) NOT NULL COMMENT '运单号申请单号',
  logistics_code varchar(30) NOT NULL COMMENT '快递企业编码',
  logistics_name varchar(30) NOT NULL COMMENT '快递企业名称',
  order_code varchar(50) DEFAULT NULL COMMENT '订单号',
  status tinyint(1) NOT NULL COMMENT '运单号使用状态',  
  create_time datetime NOT NULL COMMENT '创建时间',
  update_time datetime NOT NULL COMMENT '更新时间',
  remark varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  KEY waybill_no (waybill_no) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运单号使用详情表';


CREATE TABLE IF NOT EXISTS wms_waybill_application (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  logistics_code varchar(30) NOT NULL COMMENT '快递企业编码',
  logistics_name varchar(30) NOT NULL COMMENT '快递企业名称',
  amount bigint(20) NOT NULL COMMENT '申请总数',
  waybill_low bigint(20) DEFAULT NULL COMMENT '申请的运单号区间下限',
  waybill_up bigint(20) DEFAULT NULL COMMENT '申请的运单号区间上限',
  actual_amount bigint(20) DEFAULT NULL COMMENT '实际数量',
  status tinyint(1) NOT NULL COMMENT '申请状态：0成功1失败',
  result_msg varchar(300) DEFAULT NULL COMMENT '申请返回结果',
  apply_time datetime NOT NULL COMMENT '申请时间',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_time datetime NOT NULL COMMENT '更新时间',
  remark varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  KEY logistics_code (logistics_code) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运单号申请表';

CREATE TABLE IF NOT EXISTS wms_waybill_info (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  logistics_code varchar(30) NOT NULL COMMENT '快递企业编码',
  logistics_name varchar(30) NOT NULL COMMENT '快递企业名称',
  type tinyint(1) NOT NULL COMMENT '类型:0报关1发运',
  order_code varchar(50) NOT NULL COMMENT '订单编号',
  waybill_no varchar(20) NOT NULL COMMENT '运单号',
  main_goods_name varchar(300) NOT NULL COMMENT '主要商品名称：多种商品填写一种即可',
  gross_weight varchar(50) NOT  NULL COMMENT '包裹重量',
  net_weight varchar(50) NOT NULL COMMENT '包裹净重',
  pack_amount varchar(50) NOT NULL COMMENT '商品发货数量',
  worth varchar(50) NOT NULL COMMENT '包裹价值',  
  is_postage_pay tinyint(1) DEFAULT NULL COMMENT '邮费是否到付:0是1否 默认1',
  is_delivery_pay tinyint(1) DEFAULT NULL COMMENT '是否货到付款:0是1否 默认1',
  send_province varchar(50) DEFAULT NULL COMMENT '发货方省份',
  send_city varchar(50) DEFAULT NULL COMMENT '发货方城市',
  send_area varchar(50) DEFAULT NULL COMMENT '发货方区域',
  send_address varchar(200) DEFAULT NULL COMMENT '发货方地址',
  send_rough_area  varchar(100) DEFAULT NULL COMMENT '发货方粗略地址,例如：浙江杭州',
  send_name varchar(100) DEFAULT NULL COMMENT '发件人姓名',
  send_tel varchar(100) DEFAULT NULL COMMENT '发件人电话',
  send_company varchar(100) DEFAULT NULL COMMENT '发件人单位',
  consignee varchar(50) NOT NULL COMMENT '收货人姓名',
  post_code varchar(20) DEFAULT NULL COMMENT '邮编',
  province varchar(50) NOT NULL COMMENT '省名称',
  city varchar(50) NOT NULL COMMENT '市名称',
  area varchar(50) NOT NULL COMMENT '区名称',
  address varchar(500) NOT NULL COMMENT '收件地址',
  mobile varchar(20) NOT NULL COMMENT '移动电话',
  tel varchar(20) DEFAULT NULL COMMENT '固定电话',
  status tinyint(1) NOT NULL COMMENT '状态0成功1失败',
  fail_times int(11) NOT NULL COMMENT '失败次数',
  error_msg varchar(500) DEFAULT NULL COMMENT '失败原因',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_time datetime NOT NULL COMMENT '更新时间',
  remark varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  KEY logistics_code (logistics_code) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运单报关及发运';

CREATE TABLE IF NOT EXISTS wms_waybill_detail_log(
  id bigint(20) NOT NULL AUTO_INCREMENT,
  waybill_no bigint(20) NOT NULL COMMENT '运单号',
  order_code varchar(50) DEFAULT NULL COMMENT '订单号',
  logistics_code varchar(30) NOT NULL COMMENT '快递企业编码',
  pre_status tinyint(1) NOT NULL COMMENT '运单号之前状态',  
  cur_status tinyint(1) NOT NULL COMMENT '运单号最新状态',  
  content varchar(100) DEFAULT NULL COMMENT '描述',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_time datetime NOT NULL COMMENT '更新时间',
  remark varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (id),
  KEY waybill_no (waybill_no) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='运单号详情日志';
COMMIT;
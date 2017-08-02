CREATE TABLE mmp_point_detail (
  point_detail_id bigint(11) NOT NULL AUTO_INCREMENT COMMENT '详情日志ID',
  title varchar(32) NOT NULL COMMENT '详情标题',
  member_id bigint(11) NOT NULL COMMENT '会员ID',
  point int(8) NOT NULL COMMENT '使用积分',
  org_total_point int(10) NOT NULL COMMENT '原有总的积分数',
  point_type tinyint(1) NOT NULL COMMENT '积分入账类型(1：入账-获取,2：出帐-使用)',
  biz_type tinyint(2) NOT NULL COMMENT '业务类型',
  biz_id varchar(32) NOT NULL COMMENT '业务编码',
  create_time datetime NOT NULL COMMENT '创建时间',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  PRIMARY KEY (point_detail_id)
)  COMMENT='积分日志详情表';
CREATE TABLE mmp_point_member (
  point_mem_id bigint(11) NOT NULL AUTO_INCREMENT COMMENT '会员积分记录ID',
  member_id bigint(11) NOT NULL COMMENT '会员ID',
  accumulate_point bigint(14) NOT NULL COMMENT '累计积分',
  total_point int(11) NOT NULL COMMENT '会员总积分',
  create_time datetime NOT NULL COMMENT '创建时间',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  update_time datetime NOT NULL COMMENT '更新时间',
  update_user varchar(32) NOT NULL COMMENT '更新人',
  PRIMARY KEY (point_mem_id)
)  COMMENT='会员积分记录表';
CREATE TABLE mmp_point_package (
  point_package_id bigint(11) NOT NULL AUTO_INCREMENT COMMENT '会员积分打包ID',
  member_id bigint(11) NOT NULL COMMENT '会员ID',
  package_time int(8) NOT NULL COMMENT '打包时间（按年打包）',
  package_status tinyint(1) NOT NULL COMMENT '积分包状态 0为未过期，1为已过期',
  sub_total_point int(8) NOT NULL COMMENT '积分总数',
  create_time datetime NOT NULL COMMENT '创建时间',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  update_time datetime NOT NULL COMMENT '更新时间',
  update_user varchar(32) NOT NULL COMMENT '更新人',
  PRIMARY KEY (point_package_id)
)  COMMENT='会员积分打包记录';

CREATE TABLE mmp_point_package_detail (
  point_package_detail_id bigint(14) NOT NULL AUTO_INCREMENT COMMENT '积分包详情ID',
  point_package_id bigint(11) NOT NULL COMMENT '积分包ID',
  point_detail_id bigint(11) NOT NULL COMMENT '积分详情ID',
  point int(8) NOT NULL COMMENT '积分',
  create_time datetime NOT NULL COMMENT '创建时间',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  PRIMARY KEY (point_package_detail_id)
)  COMMENT='会员积分包使用记录';
CREATE TABLE ord_order_point (
  order_point_id bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  order_item_id bigint(11) NOT NULL COMMENT '订单项ID',
  order_id bigint(11) NOT NULL COMMENT '订单id',
  order_code bigint(16) NOT NULL COMMENT '订单编码',
  parent_order_id bigint(11) NOT NULL COMMENT '父订单ID',
  parent_order_code bigint(16) NOT NULL COMMENT '父订单编码',
  point_package_id bigint(11) NULL COMMENT '积分包ID',
  point int(8) NOT NULL COMMENT '使用积分',
  refunded_point int(8) NOT NULL COMMENT '已返还积分',
  create_time datetime NOT NULL COMMENT '创建时间',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  PRIMARY KEY (order_point_id)
)  COMMENT='订单积分使用表';

ALTER TABLE ord_order_info
ADD COLUMN total_point  int(8) NULL DEFAULT 0 COMMENT '使用总的积分数' AFTER tpin;
ALTER TABLE ord_order_item
ADD COLUMN points  int(8) NULL COMMENT '订单项使用积分数 ' AFTER freight;
ALTER TABLE ord_reject_info
ADD COLUMN points  int(8) NULL DEFAULT 0 COMMENT '返还的积分' AFTER offset_amount;
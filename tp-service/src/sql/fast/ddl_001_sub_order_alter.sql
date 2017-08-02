ALTER TABLE ord_sub_order
ADD COLUMN fast_user_id  bigint(10) NULL COMMENT '速递人员ID' AFTER done_time;
CREATE TABLE dss_fast_user_info (
  fast_user_id bigint(10) NOT NULL AUTO_INCREMENT COMMENT '速递用户编号',
  user_name varchar(16) NOT NULL COMMENT '用户姓名',
  mobile varchar(16) NOT NULL COMMENT '用户手机',
  warehouse_id bigint(14) NOT NULL COMMENT '关联仓库（店铺）',
  user_type tinyint(1) NOT NULL COMMENT '用户类型:1-店铺管理人员，2-店铺员工，3-关联的速递人员',
  enabled tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可使用：1-可使用，0-不可使用',
  remark varchar(128) DEFAULT NULL COMMENT '备注',
  create_time datetime NOT NULL COMMENT '创建时间',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  update_time datetime NOT NULL COMMENT '更新时间',
  update_user varchar(32) NOT NULL COMMENT '更新人',
  PRIMARY KEY (fast_user_id)
) COMMENT='速递人员信息表';
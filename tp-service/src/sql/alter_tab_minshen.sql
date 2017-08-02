ALTER TABLE mem_member_info
ADD COLUMN channel_code  varchar(16) NULL COMMENT '渠道代码' AFTER user_name;
ALTER TABLE ord_order_info
ADD COLUMN channel_code  varchar(16) NULL COMMENT '渠道代码' AFTER shop_promoter_id;
ALTER TABLE ord_order_info
ADD COLUMN uuid  varchar(32) NULL COMMENT '民生uuid' AFTER channel_code;
ALTER TABLE ord_order_info ADD COLUMN tpin  varchar(50) NULL COMMENT '民生 推荐用户OPENID' AFTER uuid;
CREATE TABLE dss_channel_info (
  channel_id int(8) NOT NULL AUTO_INCREMENT COMMENT '营销渠道ID',
  channel_code varchar(16) NOT NULL COMMENT '渠道代码',
  channel_name varchar(32) NOT NULL COMMENT '渠道名称',
  remark varchar(128) DEFAULT NULL COMMENT '渠道备注',
  parent_channel_id int(8) DEFAULT '0' COMMENT '父级渠道ID',
  disabled tinyint(1) DEFAULT '0' COMMENT '是否已禁用0:否，1是，默认0',
  create_time datetime NOT NULL COMMENT '创建时间',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  update_time datetime NOT NULL COMMENT '更新时间',
  update_user varchar(32) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (channel_id)
)  COMMENT='营销渠道信息表';

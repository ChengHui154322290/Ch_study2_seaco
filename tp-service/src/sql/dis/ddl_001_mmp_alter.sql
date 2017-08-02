ALTER TABLE prd_item_sku
ADD COLUMN commision_type  tinyint(2) NULL DEFAULT 1 COMMENT '分销提佣类型，1:普通商品,2:进口红酒，默认为1' AFTER commision_rate;
CREATE TABLE ord_order_channel_track (
  track_id bigint(11) NOT NULL AUTO_INCREMENT COMMENT '渠道跟踪ID',
  member_id bigint(14) NOT NULL COMMENT '会员ID',
  parent_order_id bigint(14) NOT NULL COMMENT '父订单ID',
  parent_order_code bigint(16) NOT NULL COMMENT '父订单编号',
  channel_code varchar(32) NOT NULL COMMENT '渠道代码',
  source varchar(16) NOT NULL COMMENT '订单来源或渠道',
  client_code varchar(24) NOT NULL COMMENT '渠道对应平台代码',
  distribute_code varchar(200) NOT NULL COMMENT '渠道分发信息',
  session_id varchar(64) NOT NULL COMMENT '会话编码',
  create_time datetime NOT NULL COMMENT '创建时间',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  PRIMARY KEY (track_id)
) COMMENT='订单推广渠道信息跟踪表';

ALTER TABLE dss_channel_info
ADD COLUMN token  varchar(64) NULL COMMENT '渠道TOKEN' AFTER parent_channel_id;
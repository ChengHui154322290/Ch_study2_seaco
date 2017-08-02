CREATE TABLE mmp_topic_promoter (
  topic_channel_id bigint(11) NOT NULL AUTO_INCREMENT COMMENT '专题渠道ID',
  topic_id bigint(11) NOT NULL COMMENT '专题ID',
  promoter_id bigint(11) NOT NULL COMMENT '店铺ID',
  promoter_name varchar(32) NOT NULL COMMENT '店铺名称（冗余）',
  channel_code varchar(16) NOT NULL COMMENT '分销渠道代码',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (topic_channel_id)
) COMMENT='专题对应分销渠道';
CREATE TABLE mmp_topic_promoter_change (
  topic_channel_id bigint(11) NOT NULL AUTO_INCREMENT COMMENT '专题渠道ID',
  topic_change_id bigint(11) NOT NULL COMMENT '变更ID',
  topic_id bigint(11) NOT NULL COMMENT '专题ID',
  promoter_id bigint(11) NOT NULL COMMENT '店铺ID',
  promoter_name varchar(32) NOT NULL COMMENT '店铺名称（冗余）',
  channel_code varchar(16) NOT NULL COMMENT '分销渠道代码',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (topic_channel_id)
) COMMENT='专题对应分销渠道';
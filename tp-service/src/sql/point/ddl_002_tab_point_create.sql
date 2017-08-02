CREATE TABLE mmp_point_sign_config (
  id int(11) NOT NULL AUTO_INCREMENT COMMENT '连续签到ID',
  sequence_day int(11) NOT NULL COMMENT '连续签到天数',
  point int(11) NOT NULL COMMENT '奖励积分',
  used tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可用:0不可用，1 可用，默认为1 ',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签到积分奖励配置表';
CREATE TABLE mem_member_sign_point (
  id bigint(11) NOT NULL AUTO_INCREMENT COMMENT '会员签到ID',
  member_id bigint(11) NOT NULL COMMENT '会员ID',
  sign_date datetime NOT NULL COMMENT '签到日期',
  point int(4) NOT NULL COMMENT '获得到的积分',
  create_time datetime NOT NULL COMMENT '创建时间',
  create_user varchar(32) NOT NULL COMMENT '创建人',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员签到获取积分日志表';
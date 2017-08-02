drop table if exists dss_promoter_info;

/*==============================================================*/
/* Table: dss_promoter_info                                     */
/*==============================================================*/
CREATE TABLE dss_promoter_info (
  promoter_id bigint(12) NOT NULL AUTO_INCREMENT COMMENT '分销员编号',
  member_id bigint(14) DEFAULT NULL COMMENT '关联会员',
  promoter_name varchar(16) NOT NULL COMMENT '真实姓名',
  pass_word varchar(32) NOT NULL COMMENT '密码',
  salt varchar(64) NOT NULL COMMENT '密码盐',
  promoter_status tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态(0:禁用，1：使用，默认1)',
  promoter_type tinyint(1) NOT NULL COMMENT '类型(0:卡券推广，1：店铺分销)',
  gender tinyint(1) NOT NULL COMMENT '性别(0:女，1：男)',
  birthday datetime DEFAULT NULL COMMENT '年龄',
  mobile varchar(64) NOT NULL COMMENT '手机号',
  qq varchar(12) DEFAULT NULL COMMENT 'QQ',
  weixin varchar(32) DEFAULT NULL COMMENT '微信',
  invite_code varchar(32) DEFAULT NULL COMMENT '邀请码',
  parent_promoter_id bigint(14) NOT NULL DEFAULT '0' COMMENT '上级分销编号',
  credential_type tinyint(1) NOT NULL DEFAULT '1' COMMENT '证件类型(1:身份证，2：居住证，默认为1)',
  credential_code varchar(64) NOT NULL COMMENT '证件号码',
  bank_name varchar(32) NOT NULL COMMENT '开户银行',
  bank_account varchar(64) NOT NULL COMMENT '银行卡号',
  accumulated_amount double(10,2) NOT NULL DEFAULT '0.00' COMMENT '累计佣金',
  surplus_amount double(8,2) NOT NULL DEFAULT '0.00' COMMENT '剩余佣金',
  referral_fees double(8,2) NOT NULL DEFAULT '0.00' COMMENT '拉新佣金总额',
  page_show tinyint(1) DEFAULT '0' COMMENT '是否页面显示名称',
  commision_rate float(4,2) DEFAULT '0.00' COMMENT '佣金比率设置',
  pass_time datetime DEFAULT NULL COMMENT '开通时间',
  create_user varchar(32) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_user varchar(32) NOT NULL COMMENT '更新者',
  update_time datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (promoter_id)
);

alter table dss_promoter_info comment '分销员信息';
drop table if exists dss_commision_detail;

/*==============================================================*/
/* Table: dss_commision_detail                                  */
/*==============================================================*/
CREATE TABLE dss_commision_detail (
  commision_detail_id bigint(14) NOT NULL AUTO_INCREMENT COMMENT '佣金明细编号',
  operate_type tinyint(1) NOT NULL DEFAULT '0' COMMENT '佣金操作类型：0:入，1：冲，默认0',
  biz_code bigint(16) NOT NULL COMMENT '业务编号',
  biz_type tinyint(2) NOT NULL COMMENT '业务类型',
  biz_amount double(8,2) NOT NULL COMMENT '提佣金额',
  order_amount double(8,2) DEFAULT NULL COMMENT '订单总金额',
  order_receipt_time datetime DEFAULT NULL COMMENT '订单收货日期',
  coupon_amount double(8,2) DEFAULT NULL COMMENT '卡券金额',
  commision double(8,2) NOT NULL COMMENT '佣金',
  commision_rate float(4,2) NOT NULL COMMENT '佣金比率',
  order_code bigint(16) NOT NULL COMMENT '订单编码',
  order_status tinyint(1) NOT NULL COMMENT '订单状态(关联到订单的状态)',
  collect_status tinyint(1) NOT NULL DEFAULT '0' COMMENT '汇总状态(0：未汇总，1：已汇总，默认为0)',
  indirect tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否为间接提佣(0：否，1：是，默认为 0)',
  promoter_id bigint(12) NOT NULL COMMENT '订单提佣者',
  promoter_type tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型(0:卡券推广，1：店铺分销)',
  member_id bigint(14) NOT NULL COMMENT '购买者账号',
  create_user varchar(32) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_user varchar(32) NOT NULL COMMENT '更新者',
  update_time datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (commision_detail_id)
);

alter table dss_commision_detail comment '佣金明细';
drop table if exists dss_withdraw_detail;

/*==============================================================*/
/* Table: dss_withdraw_detail                                   */
/*==============================================================*/
CREATE TABLE dss_withdraw_detail (
  withdraw_detail_id bigint(14) NOT NULL AUTO_INCREMENT COMMENT '提现明细编号',
  withdraw_detail_code bigint(16) NOT NULL COMMENT '提现明细编码',
  withdraw_time datetime NOT NULL COMMENT '提现日期',
  withdraw_amount double(8,2) NOT NULL COMMENT '提现金额',
  withdraw_status tinyint(4) NOT NULL DEFAULT '0' COMMENT '提现状态(0:申请，1：审核中，2：审核通过，3：审核未通过，4：财务打款成功，5：财务打款失败)',
  withdrawor bigint(14) NOT NULL COMMENT '提现者',
  user_type tinyint(1) NOT NULL COMMENT '提现者类型',
  withdraw_bank varchar(32) NOT NULL COMMENT '提现银行',
  withdraw_bank_account varchar(32) NOT NULL COMMENT '提现银行卡号',
  old_surplus_amount double(8,2) NOT NULL COMMENT '提现银行卡号提现前金额',
  payed_time datetime DEFAULT NULL COMMENT '打款日期',
  paymentor varchar(32) DEFAULT NULL COMMENT '打款人',
  create_user varchar(32) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_user varchar(32) NOT NULL COMMENT '更新者',
  update_time datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (withdraw_detail_id)
);

alter table dss_withdraw_detail comment '提现明细表';
drop table if exists dss_withdraw_log;

/*==============================================================*/
/* Table: dss_withdraw_log                                      */
/*==============================================================*/
CREATE TABLE dss_withdraw_log (
  withdraw_log_id bigint(14) NOT NULL AUTO_INCREMENT COMMENT '提现日志编码',
  withdraw_detail_id bigint(14) NOT NULL COMMENT '提现明细编号',
  withdraw_detail_code bigint(16) NOT NULL COMMENT '提现明细编码',
  active_type tinyint(2) NOT NULL COMMENT '操作类型',
  old_status tinyint(4) NOT NULL COMMENT '操作时旧状态',
  current_status tinyint(4) NOT NULL COMMENT '操作后状态',
  remark varchar(512) NOT NULL COMMENT '备注',
  create_user varchar(32) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (withdraw_log_id)
);

alter table dss_withdraw_log comment '提现日志表';

drop table if exists dss_referee_detail;

/*==============================================================*/
/* Table: dss_referee_detail                                    */
/*==============================================================*/
CREATE TABLE dss_referee_detail (
  referee_detail_id bigint(14) NOT NULL AUTO_INCREMENT COMMENT '拉新明细编号',
  referee_detail_code bigint(16) NOT NULL COMMENT '拉新提佣编码',
  member_id bigint(14) NOT NULL COMMENT '拉新用户编号',
  promoter_id bigint(14) NOT NULL COMMENT '分销员编号',
  commision double(8,2) NOT NULL COMMENT '佣金',
  create_user varchar(32) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (referee_detail_id)
) ;

alter table dss_referee_detail comment '推荐新人佣金明细表';

drop table if exists dss_global_commision;

/*==============================================================*/
/* Table: dss_global_commision                                  */
/*==============================================================*/
CREATE TABLE dss_global_commision (
  id int(3) NOT NULL AUTO_INCREMENT COMMENT '设置编码',
  first_commision_rate float(4,2) NOT NULL COMMENT '一级提佣比率',
  second_commision_rate float(4,2) NOT NULL COMMENT '二级提佣比率',
  create_user varchar(32) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
);

alter table dss_global_commision comment '全局佣金设置表';

drop table if exists dss_account_detail;

/*==============================================================*/
/* Table: dss_account_detail                                    */
/*==============================================================*/
CREATE TABLE dss_account_detail (
  account_detail_id bigint(14) NOT NULL AUTO_INCREMENT COMMENT '流水编号',
  user_id bigint(14) NOT NULL COMMENT '用户账号',
  user_type tinyint(4) NOT NULL COMMENT '用户类型(0:卡券推广，1：店铺分销)',
  surplus_amount double(8,2) NOT NULL COMMENT '账户余额',
  amount double(6,2) NOT NULL COMMENT '金额',
  account_time datetime NOT NULL COMMENT '日期',
  account_type tinyint(1) NOT NULL COMMENT '账务类型(1：订单入账，2：退款出账，3：提现出账)',
  bussiness_type tinyint(2) NOT NULL COMMENT '业务类型(1：订单 2：退款 3：提现)',
  bussiness_code bigint(16) NOT NULL COMMENT '业务编码',
  create_user varchar(32) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (account_detail_id)
);

alter table dss_account_detail comment '账户流水表';
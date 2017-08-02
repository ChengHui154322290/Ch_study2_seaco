ALTER TABLE `xg`.`sup_contract` CHANGE `is_sea` `is_sea` TINYINT(1) DEFAULT 1 NOT NULL COMMENT '是否海淘'; 
ALTER TABLE `xg`.`sup_contract` CHANGE `contract_desc` `contract_desc` VARCHAR(255) NULL COMMENT '合同备注', CHANGE `signing_date` `signing_date` DATETIME NULL COMMENT '签约日期', CHANGE `is_agreement_contract` `is_agreement_contract` TINYINT(1) DEFAULT 0 NULL COMMENT '是否协议合同（1：是 0：否）', CHANGE `agreement_contract_name` `agreement_contract_name` VARCHAR(80) NULL COMMENT '协议合同名称', CHANGE `agreement_contract_url` `agreement_contract_url` VARCHAR(100) NULL COMMENT '协议合同地址', CHANGE `contractor_name` `contractor_name` VARCHAR(100) NULL COMMENT '签约人姓名', CHANGE `contractor_dept_id` `contractor_dept_id` VARCHAR(80) NULL COMMENT '签约人部门id', CHANGE `contractor_dept_name` `contractor_dept_name` VARCHAR(100) NULL COMMENT '签约人部门名称', CHANGE `contractor_email` `contractor_email` VARCHAR(80) NULL COMMENT '签约人邮箱地址', CHANGE `contractor_phone` `contractor_phone` VARCHAR(20) NULL COMMENT '签约人联系电话', CHANGE `create_user` `create_user` VARCHAR(32) NOT NULL COMMENT '创建操作者', CHANGE `update_user` `update_user` VARCHAR(32) NOT NULL COMMENT '更新操作者';
ALTER TABLE `xg`.`sup_contract_product` CHANGE `mid_id` `mid_id` BIGINT(11) NULL COMMENT '中类', CHANGE `mid_name` `mid_name` VARCHAR(80) NULL COMMENT '中类', CHANGE `small_id` `small_id` BIGINT(11) NULL COMMENT '小类', CHANGE `small_name` `small_name` VARCHAR(80) NULL COMMENT '小类'; 
ALTER TABLE `xg`.`sup_contract_properties` CHANGE `sp_mobile_phone` `sp_mobile_phone` VARCHAR(20) NULL COMMENT '(乙方)移动电话', CHANGE `sp_telephone` `sp_telephone` VARCHAR(20) NULL COMMENT '(乙方)固定电话', CHANGE `sp_link_address` `sp_link_address` VARCHAR(100) NULL COMMENT '(乙方)联系人地址', CHANGE `sp_email` `sp_email` VARCHAR(60) NULL COMMENT '(乙方)电子邮箱', CHANGE `sp_fax` `sp_fax` VARCHAR(20) NULL COMMENT '(乙方)传真', CHANGE `sp_qq` `sp_qq` VARCHAR(20) NULL COMMENT '(乙方)QQ', CHANGE `xg_mobile_phone` `xg_mobile_phone` VARCHAR(20) NULL COMMENT '(乙方)移动电话', CHANGE `xg_telephone` `xg_telephone` VARCHAR(20) NULL COMMENT '(乙方)固定电话', CHANGE `xg_link_address` `xg_link_address` VARCHAR(100) NULL COMMENT '(乙方)联系人地址', CHANGE `xg_email` `xg_email` VARCHAR(60) NULL COMMENT '(乙方)电子邮箱', CHANGE `xg_fax` `xg_fax` VARCHAR(20) NULL COMMENT '(乙方)传真', CHANGE `xg_qq` `xg_qq` VARCHAR(20) NULL COMMENT '(乙方)QQ', CHANGE `bank_account` `bank_account` VARCHAR(100) NULL COMMENT '银行账户', CHANGE `bank_currency` `bank_currency` VARCHAR(20) NULL COMMENT '银行币种（人民币、美元之类的code）', CHANGE `bank_acc_name` `bank_acc_name` VARCHAR(80) NULL COMMENT '银行开户人姓名', CHANGE `sp_invoice_name` `sp_invoice_name` VARCHAR(80) NULL COMMENT '开票名称', CHANGE `sp_bank_name` `sp_bank_name` VARCHAR(80) NULL COMMENT '开户银行', CHANGE `sp_bank_account` `sp_bank_account` VARCHAR(60) NULL COMMENT '开户银行帐号', CHANGE `sp_invoice_link_address` `sp_invoice_link_address` VARCHAR(100) NULL COMMENT '开票信息-联系地址', CHANGE `sp_taxpayer_code` `sp_taxpayer_code` VARCHAR(60) NULL COMMENT '纳税人识别码', CHANGE `base_legal_person` `base_legal_person` VARCHAR(60) NULL COMMENT '公司法人', CHANGE `base_link_name` `base_link_name` VARCHAR(60) NULL COMMENT '联系人', CHANGE `base_link_address` `base_link_address` VARCHAR(100) NULL COMMENT '联系地址', CHANGE `status` `status` TINYINT(1) DEFAULT 1 NULL COMMENT '状体（1：启用 0：禁用）', CHANGE `create_user` `create_user` VARCHAR(32) NOT NULL COMMENT '创建操作者', CHANGE `update_user` `update_user` VARCHAR(32) NOT NULL COMMENT '更新操作者';
ALTER TABLE `xg`.`sup_contract_attach`   
  CHANGE `name` `name` VARCHAR(80) NULL  COMMENT '合同附件名称',
  CHANGE `url` `url` VARCHAR(100)   NULL  COMMENT '合同附件地址';
ALTER TABLE `sup_contract_properties`
MODIFY COLUMN `sp_link_name`  varchar(80) NULL COMMENT '(乙方)联系人姓名' AFTER `contract_id`,
MODIFY COLUMN `sp_link_type`  varchar(20) NULL COMMENT '(乙方)联系人类型' AFTER `sp_link_name`,
MODIFY COLUMN `sp_telephone`  varchar(20) NULL COMMENT '(乙方)固定电话' AFTER `sp_mobile_phone`,
MODIFY COLUMN `sp_link_address`  varchar(100) NULL COMMENT '(乙方)联系人地址' AFTER `sp_telephone`,
MODIFY COLUMN `sp_email`  varchar(60) NULL COMMENT '(乙方)电子邮箱' AFTER `sp_link_address`,
MODIFY COLUMN `sp_fax`  varchar(20) NULL COMMENT '(乙方)传真' AFTER `sp_email`,
MODIFY COLUMN `sp_qq`  varchar(20) NULL COMMENT '(乙方)QQ' AFTER `sp_fax`,
MODIFY COLUMN `xg_dept_id`  varchar(45) NULL COMMENT '(甲方)部门id' AFTER `sp_qq`,
MODIFY COLUMN `xg_dept_name`  varchar(45) NULL COMMENT '(甲方)部门名称' AFTER `xg_dept_id`,
MODIFY COLUMN `xg_user_name`  varchar(80) NULL COMMENT '(甲方)姓名' AFTER `xg_user_id`,
MODIFY COLUMN `xg_mobile_phone`  varchar(20) NULL COMMENT '(乙方)移动电话' AFTER `xg_user_name`,
MODIFY COLUMN `xg_telephone`  varchar(20) NULL COMMENT '(乙方)固定电话' AFTER `xg_mobile_phone`,
MODIFY COLUMN `xg_link_address`  varchar(100) NULL COMMENT '(乙方)联系人地址' AFTER `xg_telephone`,
MODIFY COLUMN `xg_email`  varchar(60) NULL COMMENT '(乙方)电子邮箱' AFTER `xg_link_address`,
MODIFY COLUMN `xg_fax`  varchar(20) NULL COMMENT '(乙方)传真' AFTER `xg_email`,
MODIFY COLUMN `xg_qq`  varchar(20) NULL COMMENT '(乙方)QQ' AFTER `xg_fax`,
MODIFY COLUMN `bank_name`  varchar(80) NULL COMMENT '开户银行名称' AFTER `xg_qq`,
MODIFY COLUMN `bank_account`  varchar(100) NULL COMMENT '银行账户' AFTER `bank_name`,
MODIFY COLUMN `bank_currency`  varchar(20) NULL COMMENT '银行币种（人民币、美元之类的code）' AFTER `bank_account`,
MODIFY COLUMN `bank_acc_name`  varchar(80) NULL COMMENT '银行开户人姓名' AFTER `bank_currency`,
MODIFY COLUMN `sp_invoice_name`  varchar(80) NULL COMMENT '开票名称' AFTER `bank_acc_name`,
MODIFY COLUMN `sp_bank_name`  varchar(80) NULL COMMENT '开户银行' AFTER `sp_invoice_name`,
MODIFY COLUMN `sp_bank_account`  varchar(60) NULL COMMENT '开户银行帐号' AFTER `sp_bank_name`,
MODIFY COLUMN `sp_invoice_link_address`  varchar(100) NULL COMMENT '开票信息-联系地址' AFTER `sp_bank_account`,
MODIFY COLUMN `sp_taxpayer_code`  varchar(60) NULL COMMENT '纳税人识别码' AFTER `sp_invoice_link_address`,
MODIFY COLUMN `sp_bank_acc_name`  varchar(80) NULL COMMENT '开户名称' AFTER `sp_taxpayer_code`,
MODIFY COLUMN `base_legal_person`  varchar(60) NULL COMMENT '公司法人' AFTER `sp_link_phone`,
MODIFY COLUMN `base_link_name`  varchar(60) NULL COMMENT '联系人' AFTER `base_legal_person`,
MODIFY COLUMN `base_link_address`  varchar(100) NULL COMMENT '联系地址' AFTER `base_link_name`;


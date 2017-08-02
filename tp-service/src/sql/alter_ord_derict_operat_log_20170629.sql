
-- 海外直邮订单操作日志表新增字段
ALTER TABLE ord_derict_operat_log ADD COLUMN original_result text COMMENT '接口返回的原始数据';
ALTER TABLE ord_derict_operat_log ADD COLUMN modify_user varchar(255) DEFAULT NULL ;
ALTER TABLE ord_derict_operat_log ADD COLUMN modify_time datetime DEFAULT NULL;
ALTER TABLE prd_item_detail
ADD COLUMN added_value_rate  bigint(8) NULL COMMENT '增值税率' AFTER pur_rate,
ADD COLUMN excise_rate  bigint(8) NULL COMMENT '费消税率' AFTER added_value_rate,
ADD COLUMN customs_rate  bigint(8) NULL COMMENT '海关税率' AFTER excise_rate;
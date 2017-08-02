ALTER TABLE prd_item_import_list
ADD COLUMN customs_rate_name  varchar(50) NULL COMMENT '关税名称' AFTER income_tax_tate_id,
ADD COLUMN customs_rate_id  int(8) NULL COMMENT '关税税率ID' AFTER customs_rate_name,
ADD COLUMN excise_rate_name  varchar(50) NULL COMMENT '消费税税率名' AFTER customs_rate_id,
ADD COLUMN excise_rate_id  int(8) NULL COMMENT '消费税税率ID' AFTER excise_rate_name,
ADD COLUMN addedvalue_rate_name  varchar(50) NULL COMMENT '增殖税税率名称' AFTER excise_rate_id,
ADD COLUMN addedvalue_rate_id  int(8) NULL COMMENT '增殖税税率ID' AFTER addedvalue_rate_name;
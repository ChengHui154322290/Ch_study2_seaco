ALTER TABLE `prd_item_info`
ADD COLUMN `supplier_id`  bigint(10) NULL COMMENT '供应商ID' AFTER `id`,
ADD COLUMN `bind_level`  varchar(10) NULL COMMENT '绑定层级' AFTER `unit_id`;
ALTER TABLE `prd_item_detail`
ADD COLUMN `supplier_id`  bigint(10) NULL COMMENT '供应商ID' AFTER `id`,
ADD COLUMN `bind_level`  varchar(10) NULL COMMENT '绑定层级' AFTER `unit_id`;
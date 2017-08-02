
-- sku表新增字段  商品编码、货号
ALTER TABLE prd_item_sku ADD COLUMN goods_code varchar(50) DEFAULT NULL COMMENT '商品编码（第三方推送）';

ALTER TABLE prd_item_sku ADD COLUMN article_code varchar(50) DEFAULT NULL COMMENT '货号（第三方推送）';
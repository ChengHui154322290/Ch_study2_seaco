ALTER TABLE prd_item_sku
ADD COLUMN topic_price  double(11,2) NULL DEFAULT NULL COMMENT '西客商城促销价' AFTER basic_price;
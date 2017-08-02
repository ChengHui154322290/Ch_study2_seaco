ALTER TABLE ord_order_item
ADD COLUMN commision_rate  double(5,2) NULL DEFAULT 0 COMMENT '分销提佣比率' AFTER refund_days;
ALTER TABLE prd_item_sku
ADD COLUMN commision_rate  double(5,2) NULL DEFAULT 0 COMMENT '分销提佣比率' AFTER waves_sign;
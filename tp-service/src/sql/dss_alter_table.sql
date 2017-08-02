ALTER TABLE mem_member_info
ADD COLUMN promoter_id  bigint(14) NULL COMMENT '关联推广员ID' AFTER state;

ALTER TABLE ord_order_info
ADD COLUMN promoter_id  bigint(14) NULL COMMENT '推广员ID' AFTER account_name,
ADD COLUMN shop_promoter_id  bigint(14) NULL COMMENT '分销员ID' AFTER promoter_id;

ALTER TABLE ord_sub_order
ADD COLUMN promoter_id  bigint(14) NULL COMMENT '推广员ID' AFTER source,
ADD COLUMN shop_promoter_id  bigint(14) NULL COMMENT '分销员ID' AFTER promoter_id;

ALTER TABLE ord_order_promotion
ADD COLUMN promoter_id  bigint(14) NULL COMMENT '关联推广员ID' AFTER coupon_face_amount;
ALTER TABLE ord_cart_item
ADD COLUMN shop_id  bigint(14) NOT NULL DEFAULT 0 COMMENT '店铺ID，如果为0或空，则是西客' AFTER member_id;
ALTER TABLE ord_sub_order
ADD COLUMN channel_code  varchar(16) NULL COMMENT '分销渠道代码' AFTER track_source;
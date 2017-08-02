ALTER TABLE dss_fast_user_info
ADD COLUMN shop_type  tinyint(1) NOT NULL DEFAULT 1 COMMENT '店铺类型' AFTER user_type;
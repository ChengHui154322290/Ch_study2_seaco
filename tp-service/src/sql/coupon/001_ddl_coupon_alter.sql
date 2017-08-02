ALTER TABLE mmp_coupon
ADD COLUMN active_status  tinyint(1) NULL COMMENT '激活状态:0-未激活,1-已激活' AFTER status;
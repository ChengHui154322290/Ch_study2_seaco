ALTER TABLE mmp_exchange_coupon_channel_code
ADD COLUMN promoter_id  bigint(11) NULL COMMENT '卡券推广员ID' AFTER member_name,
ADD COLUMN code_seq  int(8) NULL COMMENT '卡券在此批次中的序列号' AFTER id,
MODIFY COLUMN create_time  datetime NOT NULL COMMENT '创建时间' AFTER coupon_user_id;
ALTER TABLE mmp_exchange_coupon_channel_code
ADD COLUMN bind_time  datetime NULL COMMENT '绑卡时间' AFTER create_time,
ADD COLUMN bind_user  varchar(32) NULL COMMENT '绑卡操作人' AFTER bind_time;
ALTER TABLE mmp_coupon_user
ADD COLUMN promoter_id  bigint(11) NULL COMMENT '推广员ID(冗余)' AFTER coupon_use_etime;
ALTER TABLE mmp_coupon
DROP COLUMN promoter_id;
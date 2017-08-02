ALTER TABLE dss_promoter_info
ADD COLUMN channel_code  varchar(16) NULL COMMENT '分销渠道代码，供分销公司使用' AFTER promoter_level;
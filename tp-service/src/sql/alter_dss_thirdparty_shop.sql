ALTER TABLE dss_promoter_info ADD COLUMN channel_code varchar(16) DEFAULT NULL COMMENT '渠道代码(作为二级域名标示)';


ALTER TABLE dss_channel_info ADD COLUMN eshop_name varchar(50) DEFAULT NULL COMMENT '渠道对应商城名称';
ALTER TABLE dss_channel_info ADD COLUMN share_title varchar(50) DEFAULT NULL COMMENT '商城分享标题';
ALTER TABLE dss_channel_info ADD COLUMN share_content varchar(500) DEFAULT NULL COMMENT '商城分享内容';


INSERT INTO cms_temple (page_id, page_name,temple_name,temple_code,status, seq, element_type, element_num) VALUES(2,"轮播图", "店铺轮播", "HAITAO_APP_DSS_CAROUSE_ADVERT", 0,1,2, 3);


ALTER TABLE dss_channel_info ADD COLUMN company_dss_type tinyint(4) DEFAULT 0 COMMENT '商城分销类型：0无分销1店铺分销';

INSERT INTO cms_temple (page_id, page_name,temple_name,temple_code,status, seq, element_type, element_num) VALUES(4,"店铺首页", "店铺功能标签", "HAITAO_APP_DSS_FUNLAB", 0,4,2, 4);

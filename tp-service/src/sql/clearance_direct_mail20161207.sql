ALTER TABLE ord_personalgoods_declare_info ADD COLUMN import_type tinyint(2) NOT NULL DEFAULT 1 COMMENT '进口类型：0直邮1保税';
ALTER TABLE ord_personalgoods_declare_info ADD COLUMN voyage_no varchar(50) DEFAULT NULL COMMENT '直邮进口航班班次号';
ALTER TABLE ord_personalgoods_declare_info ADD COLUMN traf_no varchar(50) DEFAULT NULL COMMENT '直邮进口运输工具编号（可以填写航班号）';
ALTER TABLE ord_personalgoods_declare_info ADD COLUMN bill_no varchar(50) DEFAULT NULL COMMENT '总提运单号（直邮参数）';


ALTER TABLE wms_waybill_info ADD COLUMN import_type tinyint(2) DEFAULT 1 COMMENT '进口类型：0直邮1保税';
ALTER TABLE wms_waybill_info ADD COLUMN voyage_no varchar(50) DEFAULT  NULL COMMENT '航班号';
ALTER TABLE wms_waybill_info ADD COLUMN delivery_no varchar(50) DEFAULT NULL COMMENT '总提运单号';
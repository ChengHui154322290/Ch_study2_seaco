ALTER TABLE stg_warehouse
ADD COLUMN put_sign  smallint(3) NULL COMMENT '推送标识' AFTER bonded_area;
ALTER TABLE ord_sub_order
ADD COLUMN put_sign  smallint(3) NULL COMMENT '推送标识(转成二进制）' AFTER warehouse_name;
ALTER TABLE mmp_topic_item
ADD COLUMN put_sign  smallint(3) NULL COMMENT '推送标识' AFTER stock_location_id;
ALTER TABLE mmp_topic_item_change
ADD COLUMN put_sign  smallint(3) NULL COMMENT '推送标识' AFTER stock_location_id;
/*
 * 保税区仓库参数 
 */
ALTER TABLE stg_warehouse
ADD COLUMN import_type tinyint(2) DEFAULT 1 COMMENT '进口类型：0直邮进口1保税进口' AFTER put_sign;
ALTER TABLE stg_warehouse
ADD COLUMN wms_name varchar(32) NULL COMMENT '对接WMS仓库名称仓库' AFTER import_type;
ALTER TABLE stg_warehouse
ADD COLUMN wms_code varchar(32) NULL COMMENT '对接WMS仓库编码' AFTER wms_name;
ALTER TABLE stg_warehouse
ADD COLUMN io_seaport varchar(32) NULL COMMENT '进出口岸(关区)' AFTER wms_code;
ALTER TABLE stg_warehouse
ADD COLUMN decl_seaport varchar(32) NULL COMMENT '申报口岸(关区)' AFTER io_seaport;
ALTER TABLE stg_warehouse
ADD COLUMN customs_field varchar(32) NULL COMMENT '码头货场' AFTER decl_seaport;
ALTER TABLE stg_warehouse
ADD COLUMN logistics varchar(32) NULL COMMENT '快递企业' AFTER customs_field;
ALTER TABLE stg_warehouse
ADD COLUMN logistics_code varchar(32) NULL COMMENT '快递企业编码（报关参数）' AFTER logistics;
ALTER TABLE stg_warehouse
ADD COLUMN logistics_name varchar(32) NULL COMMENT '快递企业名称（报关参数）' AFTER logistics_code;
ALTER TABLE stg_warehouse
ADD COLUMN goods_owner varchar(32) NULL COMMENT '货主名(仓库参数)' AFTER logistics_name;
ALTER TABLE stg_warehouse
ADD COLUMN account_book_no varchar(32) NULL COMMENT '账册编号(每个仓库只有一个)' AFTER goods_owner;
/*
 * 报关参数 
 */
ALTER TABLE stg_warehouse
ADD COLUMN storage_name varchar(32) NULL COMMENT '仓储企业名称(报关)' AFTER account_book_no;
ALTER TABLE stg_warehouse
ADD COLUMN storage_code varchar(32) NULL COMMENT '仓储企业代码(报关)' AFTER storage_name;
ALTER TABLE stg_warehouse
ADD COLUMN declare_type varchar(64) NULL COMMENT '报关类型' AFTER storage_code;
ALTER TABLE stg_warehouse
ADD COLUMN declare_company_name varchar(32) NULL COMMENT '报关企业名称' AFTER declare_type;
ALTER TABLE stg_warehouse
ADD COLUMN declare_company_code varchar(32) NULL COMMENT '报关企业编号' AFTER declare_company_name;

/*
 * 报关参数 
 */
ALTER TABLE stg_warehouse
ADD COLUMN post_mode varchar(32) NULL COMMENT '发货方式(报关)' AFTER declare_company_code;
ALTER TABLE stg_warehouse
ADD COLUMN sender_name varchar(32) NULL COMMENT '发件人' AFTER post_mode;
ALTER TABLE stg_warehouse
ADD COLUMN sender_country_code varchar(64) NULL COMMENT '发件人国别' AFTER sender_name;
ALTER TABLE stg_warehouse
ADD COLUMN sender_city varchar(32) NULL COMMENT '发件人城市' AFTER sender_country_code;
ALTER TABLE stg_warehouse
ADD COLUMN application_form_no varchar(32) NULL COMMENT '仓库申请单编号' AFTER sender_city;

COMMIT;

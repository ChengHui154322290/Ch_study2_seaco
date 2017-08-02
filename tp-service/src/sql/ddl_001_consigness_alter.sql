ALTER TABLE mem_consignee_address
ADD COLUMN longitude  varchar(16) NULL COMMENT '地理经度' AFTER state,
ADD COLUMN latitude  varchar(16) NULL COMMENT '地理纬度' AFTER longitude;
ALTER TABLE stg_warehouse
ADD COLUMN lng_lat  varchar(32) NULL COMMENT '经纬度，以,分割' AFTER main_warehouse_id;
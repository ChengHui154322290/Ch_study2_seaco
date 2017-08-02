ALTER TABLE pay_customs_info ADD COLUMN payplat_name varchar(80) NOT NULL COMMENT '支付平台海关备案名称';



UPDATE pay_customs_info SET payplat_name = '支付宝(中国)网络技术有限公司' where id = 1;
UPDATE pay_customs_info SET payplat_name = '财付通支付科技有限公司' where id = 2;
INSERT INTO bse_clearance_channels (code, name, real_name, status, remark, create_time, modify_time)
	VALUES('ZONGSHU', '海关总署', '海关总署', '1', '海关总署统一版', '2016-12-30 15:00:00', '2016-12-30 15:00:00');
	
--总署版支付平台海关信息
INSERT INTO pay_customs_info VALUES(3, 10, '3301960H93', '杭州西客商城贸易有限公司', 'ZONGSHU', 1, 7, '312226T001', '支付宝(中国)网络技术有限公司');
INSERT INTO pay_customs_info VALUES(4, 10, '3301960H93', '杭州西客商城贸易有限公司', 'HANGZHOU_ZS', 1, 8, '440316T004', '财付通支付科技有限公司');
COMMIT;


INSERT INTO bse_clearance_channels (code, name, real_name, status, remark, create_time, modify_time)
	VALUES('SZBSQ', '深圳保税仓', '深圳保税仓', '1', '深圳保税仓', '2016-12-30 15:00:00', '2016-12-30 15:00:00');
	
ALTER TABLE stg_warehouse ADD COLUMN traf_mode varchar(20) DEFAULT NULL COMMENT '运输模式：保税区仓库应该选择保税区，直邮模式按实际填写，一般为航空';
ALTER TABLE stg_warehouse ADD COLUMN trade_country varchar(20) DEFAULT NULL COMMENT '贸易国（起运地）一般为应填写实际贸易国,总署版系统要求保税仓填写中国';
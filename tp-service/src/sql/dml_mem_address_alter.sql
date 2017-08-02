ALTER TABLE mem_consignee_address
ADD COLUMN front_img  varchar(256) NULL COMMENT '证件正面图地址' AFTER identity_card,
ADD COLUMN back_img  varchar(256) NULL COMMENT '证件反面图' AFTER front_img;
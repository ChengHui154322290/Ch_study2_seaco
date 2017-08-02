alter TABLE ord_order_item  ADD unit_id BIGINT(20) DEFAULT 0 COMMENT '计量单位';
alter TABLE ord_order_item  ADD country_id BIGINT(20) DEFAULT 0 COMMENT '原产地'; 
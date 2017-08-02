ALTER TABLE ord_order_item
ADD COLUMN orig_freight  double(8,2) NULL COMMENT '原始运费' AFTER original_sub_total,
ADD COLUMN orig_tax_fee  double(8,2) NULL COMMENT '原始税费' AFTER orig_freight;
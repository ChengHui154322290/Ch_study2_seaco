ALTER TABLE ord_order_item
ADD COLUMN added_value_rate  float(5,2) NULL COMMENT '增值税率' AFTER tax_rate,
ADD COLUMN excise_rate  float(5,2)  NULL COMMENT '费消税率' AFTER added_value_rate,
ADD COLUMN customs_rate  float(5,2)  NULL COMMENT '海关税率' AFTER excise_rate,
ADD COLUMN item_amount  double(10,2) NULL COMMENT '商品总金额' AFTER original_sub_total,
ADD COLUMN sales_price  double(10,2) NULL COMMENT '销售价' AFTER list_price;
ALTER TABLE ord_sub_order
MODIFY COLUMN tax_fee  double(7,2) NULL DEFAULT 0.00 COMMENT '税费' AFTER delivered_time;
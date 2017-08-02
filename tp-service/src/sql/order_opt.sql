CREATE TABLE ord_cart_item (
  cart_item_id bigint(14) NOT NULL AUTO_INCREMENT COMMENT '购买车商品编号',
  member_id bigint(14) NOT NULL COMMENT '会员ID',
  sku_code varchar(20) NOT NULL COMMENT '商品SKU',
  topic_id bigint(14) NOT NULL COMMENT '活动编号',
  quantity int(5) NOT NULL COMMENT '购买商品数量',
  selected tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否选中0:未选中，1选中，默认0',
  create_time datetime NOT NULL COMMENT '添加时间',
  update_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (cart_item_id),
  KEY idx_cart_item_member (member_id,sku_code)
)  COMMENT='购物车商品信息表';
ALTER TABLE  mem_consignee_address ADD COLUMN identity_card VARCHAR(32) NULL  COMMENT '用户身份编码' AFTER address;
ALTER TABLE  ord_order_consignee   ADD COLUMN identity_card VARCHAR(32) NULL  COMMENT '用户身份编码' AFTER address;
ALTER TABLE  ord_sub_order  ADD COLUMN item_total DOUBLE(10,2) NULL  COMMENT '商品实际总价' AFTER total;
ALTER TABLE ord_sub_order
MODIFY COLUMN total  double(10,2) NOT NULL COMMENT '子单实付总价' AFTER quantity,
MODIFY COLUMN original_total  double(10,2) NOT NULL COMMENT '子单应付总价' AFTER item_total;
ALTER TABLE ord_sub_order
MODIFY COLUMN supplier_alias  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商别名' AFTER supplier_name;
ALTER TABLE ord_order_info
MODIFY COLUMN total  double(10,2) NOT NULL DEFAULT 0 COMMENT '实付总价' AFTER member_id,
MODIFY COLUMN original_total  double(10,2) NOT NULL DEFAULT 0 COMMENT '应付总价' AFTER total,
ADD COLUMN item_total  double(10,2) NOT NULL DEFAULT 0 COMMENT '商品总金额' AFTER original_total,
ADD COLUMN tax_total  double(10,2) NOT NULL DEFAULT 0 COMMENT '税费总金额' AFTER item_total,
ADD COLUMN discount_total  double(10,2) NULL DEFAULT 0 COMMENT '优惠总金额' AFTER tax_total,
ADD COLUMN merge_pay  tinyint(1) NULL DEFAULT 0 COMMENT '是否可以进行合并支付(0：否,1：是)默认值0' AFTER pay_code;
ALTER TABLE pay_payment_gateway
ADD COLUMN merge_pay  tinyint(1) NULL DEFAULT 0 COMMENT '是否可以进行合并支付(0：否,1：是)默认值0' AFTER parent_id;
ALTER TABLE ord_sub_order
MODIFY COLUMN balance  double(10,2) NULL DEFAULT 0.00 COMMENT '使用余额' AFTER freight,
MODIFY COLUMN points  int(11) NULL DEFAULT 0 COMMENT '使用积分' AFTER balance;
ALTER TABLE ord_sub_order
MODIFY COLUMN remark  varchar(255)   NULL DEFAULT '' COMMENT '订单备注' AFTER account_name;
ALTER TABLE ord_sub_order
ADD COLUMN cancel_reason  varchar(128) NULL COMMENT '取消原因' AFTER remark;

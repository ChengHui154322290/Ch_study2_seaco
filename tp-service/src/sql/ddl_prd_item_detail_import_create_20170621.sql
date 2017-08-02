CREATE TABLE prd_item_detail_import (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  log_id bigint(20) NOT NULL,
  detail_id bigint(20) NOT NULL,
  sku varchar(50) DEFAULT NULL,
  brand_name varchar(64) DEFAULT NULL COMMENT '品牌名称',
  brand_story text COMMENT '品牌故事',
  item_title varchar(60) DEFAULT NULL COMMENT '商品名称',
  item_detail_decs text COMMENT '商品详情描述',
  material varchar(50) DEFAULT NULL COMMENT '材质',
  special_instructions varchar(255) DEFAULT NULL COMMENT '特殊说明',
  create_user varchar(255) NOT NULL,
  create_time datetime NOT NULL,
  status int(2) NOT NULL DEFAULT '0' COMMENT '导入状态：成功-0，失败-1',
  op_message varchar(255) DEFAULT NULL COMMENT '导入失败原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT='商品详情导入日志表';
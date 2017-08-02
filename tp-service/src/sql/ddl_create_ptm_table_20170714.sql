CREATE TABLE `ptm_platform_item_log` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL COMMENT '接口类型',
  `content` varchar(2000) DEFAULT NULL COMMENT '请求数据',
  `response` varchar(2000) DEFAULT NULL COMMENT '返回数据',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='平台请求日志表';


CREATE TABLE `prd_item_push_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sku` varchar(50) NOT NULL,
  `goods_code` varchar(50) DEFAULT NULL COMMENT '第三方商品编号',
  `type` int(2) NOT NULL COMMENT '日志类型  1-设置库存接口 ， 2-设置成本价接口',
  `inventory` int(11) DEFAULT NULL COMMENT '第三方商品库存数量',
  `cost_price` double(11,2) DEFAULT NULL COMMENT '成本价',
  `create_user` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='第三方推送库存和成本价接口日志表';

--新增字段成本价
ALTER TABLE prd_item_sku ADD COLUMN cost_price double(11,2) DEFAULT NULL COMMENT '第三方推送成本价';

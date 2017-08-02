ALTER TABLE wms_stockout_back ADD wms_code varchar(50) NOT NULL DEFAULT '' COMMENT '仓库WMS编号';

ALTER TABLE wms_stockout ADD wms_code varchar(50) NOT NULL DEFAULT '' COMMENT '仓库WMS编号';
ALTER TABLE wms_stockout ADD wms_name varchar(50) DEFAULT NULL COMMENT '仓库WMS名称';


CREATE TABLE `sys_rest_log` (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(30) DEFAULT NULL COMMENT '请求类型',
  `method` varchar(20) DEFAULT NULL COMMENT '请求方法',
  `url` varchar(500) DEFAULT NULL COMMENT '请求地址',
  `content` text DEFAULT NULL COMMENT '请求数据',
  `result` text DEFAULT NULL COMMENT '返回结果',
  `request_time` datetime DEFAULT NULL COMMENT '请求时间',
  `remain1` varchar(200) DEFAULT NULL COMMENT '保留字段',
  `remain2` varchar(200) DEFAULT NULL COMMENT '保留字段',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='对外REST请求日志表';

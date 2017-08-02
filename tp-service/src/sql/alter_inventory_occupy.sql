ALTER TABLE stg_inventory_occupy ADD COLUMN occupy_distribute tinyint(2) NOT NULL DEFAULT 1 COMMENT '是否冻结预占库存：0否1是';
ALTER TABLE stg_inventory_occupy ADD COLUMN inventory_id bigint(20) DEFAULT NULL COMMENT '总库存ID';
ALTER TABLE `stg_inventory`
	ADD COLUMN `reserve_inventory`  int(11) DEFAULT 0 COMMENT '商品预留库存数' AFTER `occupy`,
	ADD COLUMN `warn_inventory`  int(11) DEFAULT 0 COMMENT '商品预警库存数' AFTER `reserve_inventory`;
ALTER TABLE stg_inventory_distribute ADD COLUMN biz_inventory int(11) DEFAULT 0 COMMENT '业务分配库存';

ALTER TABLE stg_inventory_occupy MODIFY COLUMN inventory_dist_id bigint(20) DEFAULT NULL COMMENT '分配库存ID';
ALTER TABLE stg_inventory_occupy MODIFY COLUMN biz_id varchar(20) DEFAULT NULL COMMENT '活动ID';

ALTER TABLE mmp_topic ADD COLUMN reserve_inventory_flag tinyint(2) NOT NULL DEFAULT 0 COMMENT '活动是否预占库存：0否1是';
UPDATE mmp_topic SET reserve_inventory_flag = 1 where sales_partten = 6 or sales_partten = 7;COMMIT;
ALTER TABLE mmp_topic_item ADD COLUMN reserve_inventory_flag tinyint(2) NOT NULL DEFAULT 0 COMMENT '活动是否预占库存：0否1是';
UPDATE mmp_topic_item a, mmp_topic b set a.reserve_inventory_flag = b.reserve_inventory_flag where a.topic_id = b.id;COMMIT;
ALTER TABLE mmp_topic_change ADD COLUMN reserve_inventory_flag tinyint(2) NOT NULL DEFAULT 0 COMMENT '活动是否预占库存：0否1是';
UPDATE mmp_topic_change SET reserve_inventory_flag = 1 where sales_partten = 6 or sales_partten = 7;COMMIT;

ALTER TABLE ord_order_item ADD COLUMN topic_inventory_flag tinyint(2) NOT NULL DEFAULT 0 COMMENT '活动是否预占库存：0否1是';
UPDATE ord_order_item a, mmp_topic b SET a.topic_inventory_flag = b.reserve_inventory_flag where a.topic_id = b.id;COMMIT;


update stg_inventory_distribute a, mmp_topic_item b set a.biz_inventory = b.limit_total where a.biz_id = b.topic_id and a.backed = 0 and a.sku = b.sku and b.deletion = 0;COMMIT;

update stg_inventory_occupy a, mmp_topic b set a.occupy_distribute = 0 where a.biz_id = b.id and b.sales_partten <> 6 and b.sales_partten <> 7;COMMIT;

update stg_inventory_occupy a, mmp_topic b set a.occupy_distribute = 1 where a.biz_id = b.id and (b.sales_partten = 6 or b.sales_partten = 7);COMMIT;

update stg_inventory_occupy a, stg_inventory_distribute b set a.inventory_id = b.inventory_id where a.inventory_dist_id = b.id;COMMIT;

delete a from stg_inventory_distribute a, mmp_topic b where a.biz_id = b.id and b.sales_partten <> 6 and b.sales_partten <> 7;
delete from stg_inventory_distribute where backed = 1 and occupy = 0;commit;

update stg_inventory_occupy a set a.inventory_dist_id = null where a.inventory_dist_id not in (select id from stg_inventory_distribute);

update stg_inventory a, (select inventory_id,SUM(occupy) occupy from stg_inventory_distribute GROUP BY inventory_id) b 
	set a.occupy = a.occupy - b.occupy where a.id = b.inventory_id;COMMIT;
	
update stg_inventory set warn_inventory = 1; COMMIT;
	
CREATE TABLE `stg_inventory_manage_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `inventory_id` bigint(20) NOT NULL,
  `inventory` int(11) DEFAULT '0' COMMENT '现货库存总数量（包括坏品等）',
  `occupy` int(11) DEFAULT '0' COMMENT '占用库存总数量（即冻结库存）',
  `reserve_inventory` int(11) DEFAULT '0' COMMENT '商品预留库存数',
  `warn_inventory` int(11) DEFAULT '0' COMMENT '商品预警库存数',
  `reject` int(11) DEFAULT '0' COMMENT '残次数量',
  `sample` int(11) DEFAULT '0' COMMENT '样品',
  `freeze` int(11) DEFAULT '0' COMMENT '冻结状态',
  `sku` varchar(50) NOT NULL COMMENT 'sku编号',
  `main_title` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库id',
  `warehouse_name` varchar(100) DEFAULT NULL COMMENT '仓库名称',
  `sp_id` bigint(20) NOT NULL COMMENT '供应商id',
  `sp_name` varchar(100) DEFAULT NULL COMMENT '供应商名称',
  `change_type` int(2) NOT NULL COMMENT '更改标志：1：新增，2：修改；3：删除',
  `district_id` bigint(20) DEFAULT NULL COMMENT '地区id',
  `create_user` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='库存数据变更日志';

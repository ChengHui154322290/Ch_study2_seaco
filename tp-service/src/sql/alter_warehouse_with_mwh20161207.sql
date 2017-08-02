ALTER TABLE stg_warehouse 
	ADD COLUMN main_warehouse_id bigint(20) DEFAULT 0 NOT NULL 
	COMMENT '主仓库ID（同一主仓库的商品，及时供应商不同，也不会拆单）：默认为0，不存在主仓库';
	
ALTER TABLE stg_warehouse 
	ADD COLUMN main_warehouse_name varchar(100) DEFAULT NULL COMMENT '主仓库名称';
	
ALTER TABLE stg_warehouse 
	ADD COLUMN main_sp_id bigint(20) DEFAULT NULL COMMENT '主仓库所属供应商Id';
	
ALTER TABLE stg_warehouse 
	ADD COLUMN main_sp_name varchar(100) DEFAULT NULL COMMENT '主仓库所属供应商名称';
	
ALTER TABLE stg_warehouse 
	ADD COLUMN main_type tinyint(3) DEFAULT NULL COMMENT '主仓库类型：0不是主仓库1与物理仓对应仓';
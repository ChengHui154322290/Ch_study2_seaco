package com.tp.dao.prd;

import java.util.List;

import com.tp.common.dao.BaseDao;
import com.tp.dto.prd.ExcelSkuDto;
import com.tp.model.prd.ItemImportList;

public interface ItemImportListDao extends BaseDao<ItemImportList> {

	void updateByLogId(ItemImportList itemImportList);

	void batchUpdateDynamic(List<ExcelSkuDto> validFailIndexList);

}

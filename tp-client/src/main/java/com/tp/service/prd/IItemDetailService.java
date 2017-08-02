package com.tp.service.prd;

import java.util.List;

import com.tp.common.vo.PageInfo;
import com.tp.dto.prd.ItemDetailImportLogDto;
import com.tp.dto.prd.ItemDetailOpenDto;
import com.tp.dto.prd.ItemResultDto;
import com.tp.dto.prd.excel.ExcelItemDetailForTransDto;
import com.tp.model.prd.ItemDetail;
import com.tp.query.prd.DetailQuery;
import com.tp.service.IBaseService;
/**
  * @author szy
  * 商品基础信息prdid维度接口
  */
public interface IItemDetailService extends IBaseService<ItemDetail>{
	
	/**
	 * 根据detailId 的集合查询商品基础信息prdid维度信息
	 * @param detailIds
	 * @return
	 */
	public List<ItemDetail> selectByDetailIds(List<Long> detailIds);
	
	/**
	 * 强制主库查询
	 * @param id
	 * @return
	 */
	ItemDetail selectByIdFromMaster(Long id);
	
	/**
	 * 校验条形码（强制主库校验）
	 * @param barcode
	 * @return
	 */
	ItemDetail checkBarcodeFromMaster(String barcode);

	public PageInfo<ItemResultDto> queryPageByQuery(DetailQuery detailQuery);

	public ItemDetailOpenDto getItemDetailOpenDtoByDetailId(Long detailId);

	void saveItemDetailForTrans(List<ExcelItemDetailForTransDto> excelList);

	ItemDetailImportLogDto saveImportLogDto(ItemDetailImportLogDto itemDetailImportLogDto);

}

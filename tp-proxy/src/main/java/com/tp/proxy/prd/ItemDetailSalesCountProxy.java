package com.tp.proxy.prd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.PageInfo;
import com.tp.common.vo.prd.ItemConstant;
import com.tp.dto.cms.result.SubmitResultInfo;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.prd.DetailMainInfoDto;
import com.tp.dto.prd.ItemDetailSalesCountDto;
import com.tp.model.prd.ItemDetail;
import com.tp.model.prd.ItemDetailSalesCount;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.prd.IItemDetailSalesCountService;
import com.tp.service.prd.IItemRemoteService;
/**
 * 商品prdid 销售数量表代理层
 * @author szy
 *
 */
@Service
public class ItemDetailSalesCountProxy extends BaseProxy<ItemDetailSalesCount>{

	@Autowired
	private IItemDetailSalesCountService itemDetailSalesCountService;
	@Autowired
	private IItemRemoteService itemRemoteService;

	@Override
	public IBaseService<ItemDetailSalesCount> getService() {
		return itemDetailSalesCountService;
	}
	
	public int updateItemSalesCount(Map<String, Integer> skuCountMap){
		return itemRemoteService.updateItemSalesCount(skuCountMap);
	}
	
	public int getSalesCountBySku(String sku) {
		return itemRemoteService.getSalesCountBySku(sku);
	}
	public Map<String, Integer> getSalesCountBySkuList(List<String> skuList) {
		return itemRemoteService.getSalesCountBySkuList(skuList);
	}
	
	public PageInfo<ItemDetailSalesCountDto> queryListOfItemSalesCount(ItemDetailSalesCountDto dto,int startPage,int pageSize){
		if(dto == null){
			dto=new ItemDetailSalesCountDto();
		}
		
		if (dto != null && startPage>0 && pageSize>0) {
			dto.setStartPage(startPage);
			dto.setPageSize(pageSize);
			
			try {
				PageInfo<ItemDetailSalesCountDto> page = itemRemoteService.queryDetailSalesPageListByStartPageSize(dto, startPage, pageSize);
				//获取商品名称
				if(page != null && CollectionUtils.isNotEmpty(page.getRows())){
					
					List<Long> detailIds = new ArrayList<>();
					for(ItemDetailSalesCountDto dcDto:page.getRows()){
						detailIds.add(dcDto.getDetailId());
					}
					//获取商品名称
					if(CollectionUtils.isNotEmpty(detailIds)){
						List<DetailMainInfoDto> listMainInfo = itemRemoteService.getMaintitlesByDetailIds(detailIds);
						if(CollectionUtils.isNotEmpty(listMainInfo)){
							for(DetailMainInfoDto mianDto:listMainInfo){
								for(int i=0;i<page.getRows().size();i++){
									if(mianDto.getDetailId().equals(page.getRows().get(i).getDetailId())){
										page.getRows().get(i).setMainTitle(mianDto.getMainTitle());
									}
								}
							}
						}
					}
				}
				
				
			return 	page;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return new PageInfo<ItemDetailSalesCountDto>(); 
	}

	/**
	 * @param id
	 * @return
	 */
	public ItemDetailSalesCountDto getDetailSalesCountDtoInfoById(Long id) {
		ItemDetailSalesCount detail = itemDetailSalesCountService.queryById(id);
		if(detail != null) {
			ItemDetailSalesCountDto dto = new ItemDetailSalesCountDto();
			BeanUtils.copyProperties(detail, dto);
			return dto;
		}
		return null;
	}

	/**
	 * @param prdidList
	 * @param userId
	 * @return
	 */
	public ResultInfo insertWithPRDIDS(String prdList, String userId) {
		if(StringUtils.isNotBlank(prdList)){
			String prdidArry[] =  prdList.split("\n");
			if(prdidArry != null && prdidArry.length > 0){
				
				List<ItemDetailSalesCount> insertList = new ArrayList<>();
				
				if(prdidArry.length > 200){
					return  new ResultInfo(new FailInfo("数据最多为200行，超过不予受理", 100));
				}
				for(int i=0;i<prdidArry.length;i++){
					String[] eachLine = prdidArry[i].split(" ");
					ItemDetailSalesCount newValidate = new ItemDetailSalesCount();
					newValidate.setPrdid(eachLine[0].trim());
					if(!org.apache.commons.lang3.math.NumberUtils.isDigits(eachLine[1].trim().toString())){
						return  new ResultInfo(new FailInfo("第"+i+eachLine[1].trim().toString()+"不是有效数字",100));
					}
					newValidate.setDefaultSalesCount(Long.valueOf(eachLine[1].trim().toString()));
					newValidate.setUpdateDefaultCountTime(new Date());
					newValidate.setUpdateUser(userId);
					newValidate.setCreateUser(userId);
					insertList.add(newValidate);
				}
				if(CollectionUtils.isNotEmpty(insertList)){
					try {
						String retrunInf =	itemRemoteService.insertWithPRDIDS(insertList);
						
						if(retrunInf.indexOf(ItemConstant.CAN_NOT_GET) != -1){
							return  new ResultInfo(new ResultInfo(new FailInfo("商品库中不存在PRDID".concat(retrunInf.replaceAll(ItemConstant.CAN_NOT_GET, "")),100)));
						}
						if(!ItemConstant.ITEM_DETAIL_SALES_SUCCESS.equals(retrunInf) && !ItemConstant.ITEM_DETAIL_SALES_NOT_MATCH.equals(retrunInf)){
							return  new ResultInfo(new ResultInfo(new FailInfo(retrunInf.concat("已经存在"),100)));
						}
					} catch (Exception e) {
						return new ResultInfo(new FailInfo(e.getMessage(), 100));
					}
					return new ResultInfo(new FailInfo("保存成功", 100));
				}
			}else{
				return  new ResultInfo(new FailInfo("无法解析数据", 100));
			}
		}
		 return  new ResultInfo(new FailInfo("未能成功保存数据", 100));
	}

	/**
	 * @param barcodeList
	 * @param userId
	 * @return
	 */
	public ResultInfo insertWithBarcodes(String barcodeList, String userId) {
		if(StringUtils.isNotBlank(barcodeList)){
			String barCodeArry[] =  barcodeList.split("\n");
			if(barCodeArry != null && barCodeArry.length > 0){
				
				List<ItemDetailSalesCount> insertList = new ArrayList<>();
				
				if(barCodeArry.length > 200){
					return  new ResultInfo(new FailInfo("数据最多为200行，超过不予受理",100));
				}
				for(int i=0;i<barCodeArry.length;i++){
					String[] eachLine = barCodeArry[i].split(" ");
					ItemDetailSalesCount newValidate = new ItemDetailSalesCount();
					newValidate.setBarcode(eachLine[0].trim());
					if(!org.apache.commons.lang3.math.NumberUtils.isDigits(eachLine[1].trim().toString())){
						return new ResultInfo(new FailInfo("第"+i+eachLine[1].trim().toString()+"不是有效数字", 100));
					}
					newValidate.setDefaultSalesCount(Long.valueOf(eachLine[1].trim().toString()));
					newValidate.setUpdateDefaultCountTime(new Date());
					newValidate.setUpdateUser(userId);
					newValidate.setCreateUser(userId);
					insertList.add(newValidate);
				}
				if(CollectionUtils.isNotEmpty(insertList)){
					try {
						String retrunInf =	itemRemoteService.insertWithBarcodes(insertList);
						if(retrunInf.indexOf(ItemConstant.CAN_NOT_GET) != -1){
							return  new ResultInfo(new FailInfo("商品库中不存在上架状态的条形码".concat(retrunInf.replaceAll(ItemConstant.CAN_NOT_GET, "")),100));
						}
						if(!ItemConstant.ITEM_DETAIL_SALES_SUCCESS.equals(retrunInf) && !ItemConstant.ITEM_DETAIL_SALES_NOT_MATCH.equals(retrunInf)){
							return new ResultInfo(new FailInfo(retrunInf.concat("已经存在"), 100));
						}
					} catch (Exception e) {
						return new ResultInfo(new FailInfo(e.getMessage(), 100));
					}
					return  new ResultInfo("保存成功");
				}
			}else{
				return  new ResultInfo(new FailInfo("无法解析数据", 100));
			}
		}
		return  new ResultInfo(new FailInfo("未能成功保存数据", 100));
	}

	/**
	 * @param dto
	 * @return
	 */
	public ResultInfo adjustDetailSales(ItemDetailSalesCountDto dto) {
		try {
			 dto.setUpdateDefaultCountTime(new Date());
			 int count =itemRemoteService.updateDetailSalesCountDefaultCount(dto);
			 if(count > 0){
				 return new ResultInfo("修改成功");
			 }else{
				 return new ResultInfo(new FailInfo("未能成功修改数据", 100));
			 }
			} catch (Exception e) {
				return new ResultInfo(new FailInfo(e.getMessage(), 100));
		}	
	}
}

package com.tp.proxy.wms;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.model.wms.Stockout;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.wms.IStockoutService;
import com.tp.util.DateUtil;
/**
 * 出库订单代理层
 * @author szy
 *
 */
@Service
public class StockoutProxy extends BaseProxy<Stockout>{

	@Autowired
	private IStockoutService stockoutService;

	@Override
	public IBaseService<Stockout> getService() {
		return stockoutService;
	}
	
	public HSSFWorkbook formatStockExcel(Stockout stockout){
		HSSFWorkbook workbook = new HSSFWorkbook();// 创建一个Excel文件   
	    HSSFSheet sheet = workbook.createSheet();// 创建一个Excel的Sheet   
	    HSSFRow row0 = sheet.createRow(0);   // 创建第一行   
	    HSSFCell cell = row0.createCell(0);   
	    cell.setCellValue(new HSSFRichTextString("订单编号")); 
	    cell = row0.createCell(1);   
	    cell.setCellValue(new HSSFRichTextString("仓库编号"));  
	    cell = row0.createCell(2);   
	    cell.setCellValue(new HSSFRichTextString("仓库名称"));
	    cell = row0.createCell(3);   
	    cell.setCellValue(new HSSFRichTextString("承运人"));
	    cell = row0.createCell(4);   
	    cell.setCellValue(new HSSFRichTextString("快递单号"));
	    cell = row0.createCell(5);   
	    cell.setCellValue(new HSSFRichTextString("收货人"));
	    cell = row0.createCell(6);   
	    cell.setCellValue(new HSSFRichTextString("收货人手机"));
	    cell = row0.createCell(7);   
	    cell.setCellValue(new HSSFRichTextString("状态"));
	    cell = row0.createCell(8);   
	    cell.setCellValue(new HSSFRichTextString("创建时间"));
	    List<Stockout> stockoutList = stockoutService.queryByObject(stockout);
	    Stockout out = null;
	    for(int i=1;i<=stockoutList.size();i++){
	    	out = stockoutList.get(i-1);
	    	HSSFRow row = sheet.createRow(i);   
	    	cell = row.createCell(0);
	    	cell.setCellValue(new HSSFRichTextString(out.getOrderCode()));
	    	cell = row.createCell(1);
	    	cell.setCellValue(new HSSFRichTextString(out.getWarehouseCode()));
	    	cell = row.createCell(2);
	    	cell.setCellValue(new HSSFRichTextString(out.getWarehouseName()));
	    	cell = row.createCell(3);
	    	cell.setCellValue(new HSSFRichTextString(out.getLogisticsCompanyName()));
	    	cell = row.createCell(4);
	    	cell.setCellValue(new HSSFRichTextString(out.getExpressNo()));
	    	cell = row.createCell(5);
	    	cell.setCellValue(new HSSFRichTextString(out.getConsignee()));
	    	cell = row.createCell(6);
	    	cell.setCellValue(new HSSFRichTextString(out.getMobile()));
	    	cell = row.createCell(7);
	    	if(0==out.getStatus()){
	    		cell.setCellValue(new HSSFRichTextString("失败"));
	    	}else if(1==out.getStatus()){
	    		cell.setCellValue(new HSSFRichTextString("成功"));
	    	}
	    	cell = row.createCell(8);
	    	cell.setCellValue(new HSSFRichTextString(DateUtil.format(out.getCreateTime(),DateUtil.NEW_FORMAT)));
	    }
		return workbook;
	}
}

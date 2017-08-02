package com.tp.proxy.mmp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mmp.ExchangeCodeConstant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mmp.Coupon;
import com.tp.model.mmp.CouponUser;
import com.tp.model.mmp.ExchangeCouponChannel;
import com.tp.model.mmp.ExchangeCouponChannelCode;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.mmp.callBack.Callback;
import com.tp.service.IBaseService;
import com.tp.service.mmp.ICouponService;
import com.tp.service.mmp.ICouponUserService;
import com.tp.service.mmp.IExchangeCouponChannelCodeService;
import com.tp.service.mmp.IExchangeCouponChannelService;
import com.tp.util.BeanUtil;
import com.tp.util.DateUtil;
import com.tp.util.StringUtil;

/**
 * 活动兑换码明细表代理层
 *
 * @author szy
 */
@Service
public class ExchangeCouponChannelCodeProxy extends BaseProxy<ExchangeCouponChannelCode> {
	
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final static String exportPath = "excel/export_exchangeCode.xls";
    private final static int CMS_EXCEPT_EXCEL_LIMIT = 50000;
    @Autowired
    private IExchangeCouponChannelCodeService exchangeCouponChannelCodeService;
    @Autowired
    IExchangeCouponChannelService exchangeCouponChannelService;
    @Autowired
    private ICouponUserService couponUserService;
    @Autowired
    private ICouponService couponService;
    @Autowired
    private PromoterInfoProxy promoterInfoProxy;
    
    @Override
    public IBaseService<ExchangeCouponChannelCode> getService() {
        return exchangeCouponChannelCodeService;
    }

    public ResultInfo insertExcode(final ExchangeCouponChannelCode mode, final Integer num) {
        ResultInfo result = new ResultInfo();
        this.execute(result, new Callback() {
            @Override
            public void process() throws Exception {
                List<ExchangeCouponChannelCode> list = exchangeCouponChannelCodeService.generateCode(mode, num);
                exchangeCouponChannelCodeService.batchInsert(list);
            }
        });
        return result;
    }

    public List<ExchangeCouponChannelCode> getExchangeCodeByid(Long actId) {
        ExchangeCouponChannelCode mode = new ExchangeCouponChannelCode();
        mode.setActId(actId);
        //mode.setStatus(0);
        return exchangeCouponChannelCodeService.queryByObject(mode);
    }

    public ResultInfo<PageInfo<ExchangeCouponChannelCode>> queryExchangeCouponByParam(ExchangeCouponChannelCode query){
    	try{
    		PageInfo<ExchangeCouponChannelCode> pageInfo = exchangeCouponChannelCodeService.queryExchangeCouponByParam(query);
    		if(CollectionUtils.isNotEmpty(pageInfo.getRows())){
    			Map<String,Object> params = new HashMap<String,Object>();
    			List<Long> couponUserIdList = new ArrayList<Long>();
    			List<Long> couponIdList = new ArrayList<Long>();
    			List<Long> promoterIdList = new ArrayList<Long>();
    			pageInfo.getRows().forEach(new Consumer<ExchangeCouponChannelCode>(){
					public void accept(ExchangeCouponChannelCode t) {
						if(t.getCouponUserId()!=null){
							couponUserIdList.add(t.getCouponUserId());
						}
						couponIdList.add(t.getCouponId());
						if(t.getPromoterId()!=null){
							promoterIdList.add(t.getPromoterId());
						}
					}
    			});
    			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(couponIdList,SPLIT_SIGN.COMMA)+")");
    			List<Coupon> couponList = couponService.queryByParam(params);
    			List<CouponUser> couponUserList = null;
    			if(CollectionUtils.isNotEmpty(couponUserIdList)){
        			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " id in ("+StringUtil.join(couponUserIdList,SPLIT_SIGN.COMMA)+")");
    				couponUserList = couponUserService.queryByParam(params);
    			}
    			List<PromoterInfo> promoterInfoList = null;
    			if(CollectionUtils.isNotEmpty(promoterIdList)){
    				params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " promoter_id in ("+StringUtil.join(promoterIdList,SPLIT_SIGN.COMMA)+")");
    				promoterInfoList = promoterInfoProxy.queryByParam(params).getData();
    			}
    			for(ExchangeCouponChannelCode t:pageInfo.getRows()){
    				if(CollectionUtils.isNotEmpty(couponUserList)){
    					for(CouponUser couponUser:couponUserList){
    						if(couponUser.getId().equals(t.getCouponUserId())){
    							t.setCouponUser(couponUser);
    						}
    					}
					}
    				for(Coupon coupon:couponList){
    					if(coupon.getId().equals(t.getCouponId())){
    						t.setCouponName(coupon.getCouponName());
    					}
    				}
    				if(CollectionUtils.isNotEmpty(promoterInfoList)){
    					for(PromoterInfo promoterInfo:promoterInfoList){
    						if(promoterInfo.getPromoterId().equals(t.getPromoterId())){
    							t.setPromoterName(promoterInfo.getPromoterName());
    						}
    					}
					}
    			}
    		}
			return new ResultInfo<>(pageInfo);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,query);
			return new ResultInfo<>(failInfo);
		}
    }
    public List<ExchangeCouponChannelCode> getExchangeCodeByTimeAndStatus(Map<String, Object> params) {
        return exchangeCouponChannelCodeService.queryByTimeAndStatus(params);
    }
    public List<ExchangeCouponChannelCode> queryExchangeCouponByParams(Map<String, Object> params){
    	 return exchangeCouponChannelCodeService.queryExchangeCouponByParams(params);
    }

    public PageInfo<ExchangeCouponChannel> queryExchangeCodeList(ExchangeCouponChannel query) {
        Map<String, Object> param = BeanUtil.beanMap(query);
        param.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
        return exchangeCouponChannelService.queryPageByParam(param, new PageInfo<ExchangeCouponChannel>(query.getStartPage(), query.getPageSize()));
    }

    public Integer stopActExchangeCode(Long id) {
        ExchangeCouponChannel mode = new ExchangeCouponChannel();
        mode.setId(id);
        mode.setStatus(ExchangeCodeConstant.STATUS_ACT_EXCHANGE_END);
        return exchangeCouponChannelService.updateNotNullById(mode);
    }

    public ExchangeCouponChannel getActExchangeCodeById(Long id) {
        return exchangeCouponChannelService.queryById(id);
    }

    public void insertExcode(ExchangeCouponChannel mode) {
        exchangeCouponChannelService.insert(mode);
    }

    public Integer updateExcode(ExchangeCouponChannel mode) {
        return exchangeCouponChannelService.updateNotNullById(mode);
    }

    public ResultInfo<Integer> updatePromoterIdBind(Map<String,Object> params){
    	try{
    		return new ResultInfo<>(exchangeCouponChannelCodeService.updatePromoterIdBind(params));
    	}catch(Throwable throwable){
    		FailInfo failInfo = ExceptionUtils.print(new FailInfo(throwable), logger,params);
			return new ResultInfo<>(failInfo);
    	}
    }
    
    public ResultInfo<Integer> disabledPromoterId(Map<String,Object> params){
    	try{
    		return new ResultInfo<>(exchangeCouponChannelCodeService.updateCouponStatusEnabled(params));
    	}catch(Throwable throwable){
    		FailInfo failInfo = ExceptionUtils.print(new FailInfo(throwable), logger,params);
			return new ResultInfo<>(failInfo);
    	}
    }
    /**
     * 
     * cancleddPromoterId:(卡券作废). <br/>  
     *  
     * @author zhouguofeng  
     * @param params
     * @return  
     * @sinceJDK 1.8
     */
    public ResultInfo<Integer> cancledPromoterId(Map<String,Object> params){
    	try{
    		return new ResultInfo<>(exchangeCouponChannelCodeService.updateCouponStatusCancled(params));
    	}catch(Throwable throwable){
    		FailInfo failInfo = ExceptionUtils.print(new FailInfo(throwable), logger,params);
			return new ResultInfo<>(failInfo);
    	}
    }
    /**
     * <pre>
     * 下载导入excel的文件
     * </pre>
     *
     * @param request
     * @param response
     * @param fileId
     * @throws IOException
     */
    public void export(HttpServletRequest request, HttpServletResponse response, String fileId, List<ExchangeCouponChannelCode> importLists)
            throws InvalidFormatException, IOException {
        /*List<MasterOrderDTO> importLists = pgMasterOrderList.getList();*/
        List<String[][]> datasList = new ArrayList<>();
        int j = 0;
        if (CollectionUtils.isNotEmpty(importLists)) {
            String[][] datas = null;
            for (ExchangeCouponChannelCode i : importLists) {
                if (j % CMS_EXCEPT_EXCEL_LIMIT == 0) {
                    if (j == CMS_EXCEPT_EXCEL_LIMIT) {
                        datasList.add(datas);
                    }
                    datas = new String[CMS_EXCEPT_EXCEL_LIMIT][2];
                    j = 0;
                }
                String[] oneRow = {
                        i.getCodeSeq()==null?String.valueOf(j + 1):String.format("%06d", i.getCodeSeq()),
                        i.getExchangeCode() == null ? "" : i.getExchangeCode().toString(),
                        i.getCouponId() == null ? "" : i.getCouponId().toString(),
                        i.getCreateTime() == null ? "" : DateUtil.formatDate(i.getCreateTime()),
                        i.getStatus() > 0 ? "已使用" : "未使用"};
                datas[j] = oneRow;
                j++;
            }
            datasList.add(datas);
        }


        String newPath = request.getSession().getServletContext().getRealPath("");

        String fileName = "exceptExcel.xlsx";
        File destFile = new File(newPath);
        if (!destFile.exists()) {
            destFile.mkdirs();
        }

        //Workbook wb = WorkbookFactory.create(in);
        Workbook wb = new HSSFWorkbook();
        if (CollectionUtils.isNotEmpty(datasList)) {
            for (int i = 0; i < datasList.size(); i++) {
                creatSheet2(datasList.get(i), wb, i);
            }
        }
//		FileOutputStream fileoutputstream = new FileOutputStream(file);
//		wb.write(fileoutputstream);
//		fileoutputstream.close();
//		response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setContentType("application/x-download");
        ServletOutputStream out = null;
        try {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
            response.addHeader("Content-Disposition", "attachment; filename=export_exchangeCode.xls");
            out = response.getOutputStream();
            /*byte[] buff = FileUtils.readFileToByteArray(file);
            if (null == buff) {
				throw new FileNotFoundException("dfs未找到：" + fileId);
			}
			int len = buff.length;
			out.write(buff, 0, len);*/
            wb.write(out);

            response.setStatus(HttpServletResponse.SC_OK);
            response.flushBuffer();
            out.flush();
            out.close();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private void creatSheet2(String[][] datas, Workbook wb, int t) {
        //Sheet sheet = wb.getSheetAt(t);
        Sheet sheet = wb.createSheet(String.valueOf("sheet" + String.valueOf(t)));
        //填充数据
        int i;
        for (i = 0; i < datas.length; i++) {
            if (i == 0) {
                Row row = sheet.createRow(0);
                for (int j = 0; j < 5; j++) {
                    Cell cell = row.createCell(j);
                    switch (j) {
                        case 0:
                            cell.setCellValue("序号");
                            break;
                        case 1:
                            cell.setCellValue("兑换码");
                            break;
                        case 2:
                            cell.setCellValue("批次号");
                            break;
                        case 3:
                            cell.setCellValue("生成时间");
                            break;
                        case 4:
                            cell.setCellValue("使用状态");
                            break;
                    }
                }
            }
            String[] selectRow = datas[i];
            //Row row = sheet.getRow(i+1);
            Row row = sheet.createRow((i + 1));
            for (int k = 0; k < selectRow.length; k++) {
                Cell cell = row.createCell(k);
                cell.setCellValue(selectRow[k]);
            }
        }
    }

    private void creatSheet(String[][] datas, Workbook wb, int t) {
        Sheet sheet = wb.getSheetAt(t);
        boolean bool = false;
        int u = 1;
        //填充数据
        for (int i = t * CMS_EXCEPT_EXCEL_LIMIT; i < datas.length; i++) {
            String[] selectRow = datas[i];
            //Row row = sheet.getRow(i+1);
            System.out.println(i);
            Row row = sheet.createRow((i + 1) - (t * CMS_EXCEPT_EXCEL_LIMIT));
            for (int k = 0; k < selectRow.length; k++) {
                Cell cell = row.createCell(k);
                cell.setCellValue(selectRow[k]);
            }
            u++;
            if (u == ((CMS_EXCEPT_EXCEL_LIMIT) * (t + 1) + 1)) {
                bool = true;
                break;
            }
        }
        if (bool) {
            creatSheet(datas, wb, t + 1);
        }
    }

    public void copyFile(File oldPathFile, File newPathFile) {
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
	           /*int bytesum = 0; */
            int byteread = 0;
            if (oldPathFile.exists()) { //文件存在时
                inStream = new FileInputStream(oldPathFile); //读入原文件
                fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
	                   /*bytesum += byteread; //字节数 文件大小 */
                    fs.write(buffer, 0, byteread);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
            try {
                if (fs != null) {
                    fs.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }
  

    public List<Map<String, String>> queryExchangeCountDetails(Long actId) {

        if (actId == null) return Collections.emptyList();
        return  exchangeCouponChannelCodeService.queryExchangeCountDetails(actId);
    }


}

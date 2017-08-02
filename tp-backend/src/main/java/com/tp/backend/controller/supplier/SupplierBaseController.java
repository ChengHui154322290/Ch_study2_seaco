package com.tp.backend.controller.supplier;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.supplier.CommonUtil;
import com.tp.common.vo.supplier.SupplierConstant;

public class SupplierBaseController extends AbstractBaseController {
    
    /**
     * 默认时间格式
     */
    public final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * 
     * <pre>
     *  获取分页首页
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public int getPageIndex(HttpServletRequest request,String name){
        Integer index = 1;
        String indexStr = request.getParameter(name);
        index = CommonUtil.getIntVal(indexStr);
        if(null == index){
            index = 1;
        }
        return index;
    }
    
    /**
     * 
     * <pre>
     *   获取整数
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public Integer getIntegerVal(HttpServletRequest request,String name) {
        Integer intVal = null;
        String indexStr = request.getParameter(name);
        intVal = CommonUtil.getIntVal(indexStr);
        return intVal;
    }
    
    /**
     * 
     * <pre>
     *   获取long类型
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public Long getLongVal(HttpServletRequest request,String name){
        String indexStr = request.getParameter(name);
        if (StringUtils.isNotBlank(indexStr)) {
            return CommonUtil.getLongVal(indexStr);
        }
        return null;
    }
    
    /**
     * 
     * <pre>
     *   获取string
     * </pre>
     *
     * @param request
     * @param name
     * @return
     */
    public String getStringVal(HttpServletRequest request,String name) {
        String indexStr = request.getParameter(name);
        if (StringUtils.isNotBlank(indexStr)) {
            return indexStr;
        }
        return null;
    }
    
    /**
     * 
     * <pre>
     *   获取分页大小
     * </pre>
     *
     * @return
     */
    public int getPageSize(HttpServletRequest request,String name){
        Integer pageSize = 50;
        String pSize = request.getParameter(name);
        pageSize = CommonUtil.getIntVal(pSize);
        if(null == pageSize){
            pageSize = 50;
        }
        return pageSize;
    }
    
    /**
     * 
     * <pre>
     * 校验结果
     * </pre>
     *
     * @param resultMap
     * @return
     */
    public boolean checkResult(Map<String,Object> resultMap){
        try {
            return null != resultMap && null != resultMap.get(SupplierConstant.SUCCESS_KEY)
                    && (Boolean)resultMap.get(SupplierConstant.SUCCESS_KEY);
        } catch(Exception e){
            return false;
        }
    }
    
    /**
     * 
     * <pre>
     * 从返回结果中获取message
     * </pre>
     *
     * @param resultMap
     * @return
     */
    public String getMessage(Map<String,Object> resultMap){
        String result = "";
        if(null != resultMap && null != resultMap.get(SupplierConstant.SUCCESS_KEY)){
            return (String)resultMap.get(SupplierConstant.SUCCESS_KEY);
        }
        return result;
    }
    
    /**
     * 
     * <pre>
     * 获取返回的result
     * </pre>
     *
     * @return
     */
    public Map<String,Object> setResult(Map<String,Object> resultMap){
        Map<String,Object> retMap = new HashMap<String,Object>();
        retMap.put(SupplierConstant.SUCCESS_KEY, false);
        retMap.put(SupplierConstant.MESSAGE_KEY, getMessage(resultMap));
        return retMap;
    }
    
    /**
     * 
     * <pre>
     * 获取时间
     * </pre>
     *
     * @param request
     * @param name
     * @param format
     * @return
     */
    public Date getDate(HttpServletRequest request,String name,final String format){
        Date date = null;
        String dateFormat = DEFAULT_TIME_FORMAT;
        if(null != format){
            dateFormat = format;
        }
        String dateVal = request.getParameter(name);
        if(null == dateVal || "".equals(dateVal.trim())){
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            date = sdf.parse(dateVal);
        } catch(Exception e){
        }
        return date;
    }

}

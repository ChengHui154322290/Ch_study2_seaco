package com.tp.backend.controller.supplier.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.supplier.SupplierBaseController;
import com.tp.common.vo.Constant;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.model.bse.Category;
import com.tp.model.bse.DictionaryInfo;
import com.tp.model.mmp.FreightTemplate;
import com.tp.model.stg.Warehouse;
import com.tp.model.sup.Contract;
import com.tp.model.usr.UserDetail;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.bse.CategoryProxy;
import com.tp.proxy.prd.ItemInfoProxy;
import com.tp.proxy.sup.SupplierInfoProxy;
import com.tp.proxy.sup.SupplierItemProxy;
import com.tp.proxy.usr.UserInfoProxy;
import com.tp.result.prd.SkuInfoResult;
import com.tp.service.mmp.IFreightTemplateService;

/**
 * 
 * <pre>
 * 供应商调用外部接口的controller
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
@RequestMapping("/supplier/client/")
@Controller
public class SupplierClientController extends SupplierBaseController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SupplierInfoProxy supplierInfoProxy;

    @Autowired
    private IFreightTemplateService freightTemplateService;
    @Autowired
    private ItemInfoProxy itemInfoProxy;
    
    @Autowired
    private CategoryProxy categoryProxy;
    @Autowired
    private UserInfoProxy userInfoProxy;
    @Autowired
    private SupplierItemProxy supplierItemProxy;
    
    
    /**
     * 
     * <pre>
     * 获取品牌信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/getBrands")
    public ResultInfo<?> getBrandInfo(){
        ModelMap map = new ModelMap();
        
        return new ResultInfo<>(map);
    }
    
    /***
     * 
     * <pre>
     * 获取品类列表
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/getCategorys")
    @ResponseBody
    public ResultInfo<List<Category>> getCategoryInfo(Long cid){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("status",Constant.ENABLED.YES);
        params.put("parentId",cid);
        return categoryProxy.queryByParam(params);
    }
    
    /**
     * 
     * <pre>
     * 获取后台的用户信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/getXgUsers")
    @ResponseBody
    public ResultInfo<List<UserInfo>> getXgUsersInfo(Long bmId){
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("departmentId", bmId);
    	return userInfoProxy.queryByParam(params);
    }
    
    /**
     * 
     * <pre>
     *   获取运费模板信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/getExpressTemplate")
    public String getExpressTemplates(Model model,Long expressTemplateId,String expressTemplateName){
        List<FreightTemplate> freightTemplateList = supplierInfoProxy.getFreightTemplate(expressTemplateId,expressTemplateName);
        model.addAttribute("expressTemplates", freightTemplateList);
        model.addAttribute("expressTemplateId", expressTemplateId);
        model.addAttribute("expressTemplateName", expressTemplateName);
        return "supplier/pop_table/express_pop";
    }
    
    /**
     * 
     * <pre>
     *   获取仓库信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/getStorages")
    public String getStorages(Model model,Long spAddStorageId,String spAddStorageName){
        List<Warehouse> warehouseDOs = supplierInfoProxy.getStorages(spAddStorageId,spAddStorageName);
        model.addAttribute("spAddStorages", warehouseDOs);
        return "supplier/pop_table/storage_pop";
    }
    
    /**
     * 
     * <pre>
     *   获取仓库信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/getStoragesRadio")
    public String getStoragesRadio(Model model,@RequestParam("supplierId")Long supplierId,Long spAddStorageId,String spAddStorageName){
        List<Warehouse> warehouseDOs = supplierInfoProxy.getStorageWithCondition(supplierId,spAddStorageId,spAddStorageName);
        model.addAttribute("spAddStorages", warehouseDOs);
        model.addAttribute("supplierId", supplierId);
        return "supplier/pop_table/storage_pop_radio";
    }
    
    /**
     * 
     * <pre>
     *   获取仓库基本信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/getStorageInfo")
    @ResponseBody
    public /*List<Warehouse>*/ ResultInfo<Warehouse> getStorageInfo(HttpServletRequest request,Long supplierId,Long warehouseId,String warehouseName){    	
//    	List<Warehouse> supplierWarehouses = supplierInfoProxy.getStorageWithCondition(supplierId,warehouseId,warehouseName);
//        return supplierWarehouses;
        // by zhs 01152100 将返回值修改为ResultInfo
    	List<Warehouse> supplierWarehouses = supplierInfoProxy.getStorageWithCondition(supplierId,warehouseId,warehouseName);
    	ResultInfo<List<Warehouse>>  RetListWarehouse = new ResultInfo<>(supplierWarehouses);
    	if(RetListWarehouse.success && CollectionUtils.isNotEmpty(RetListWarehouse.getData()) && RetListWarehouse.getData().size()==1){
    		return new ResultInfo<Warehouse>( RetListWarehouse.getData().get(0) );
    	}
    	return new ResultInfo<Warehouse>(new FailInfo("仓库不存在"));
    }
    
    /**
     * 获取用户详情
     * 
     * @return
     */
    @RequestMapping("/getUserDetail")
    @ResponseBody
    public ResultInfo<UserDetail> getUserDetail(Long userId){
    	return supplierInfoProxy.getDetailByUserId(userId);
    }
    
    /**
     * 
     * <pre>
     *   获取物流基本信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/checkExpressInfo")
    @ResponseBody
    public List<FreightTemplate> checkExpressInfo(Long expressId){
        return supplierInfoProxy.getFreightTemplate(expressId,null);
    }
    
    /**
     * 获取资质证明信息
     * 
     * @param request
     * @return
     */
    @RequestMapping("/getQuatation")
    @ResponseBody
    public ResultInfo<List<DictionaryInfo>> getQuatation(Long cid){
    	return supplierInfoProxy.getLicensByCategoryId(cid);
    }
    
    /**
     * 
     * <pre>
     *   获取商品基本信息
     * </pre>
     *
     * @param request
     * @return
     */
    @RequestMapping("/getProductInfo")
    @ResponseBody
    public SkuInfoResult getProductInfo(String barCode,String skuCode,Long supplierId) {
        if(null == supplierId) {
            return null;
        }
        return supplierItemProxy.getSkuInfoByBarCodeOrSku(skuCode, barCode, supplierId);
    }

}

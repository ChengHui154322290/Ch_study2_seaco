package com.tp.backend.controller.mmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.AreaConstant;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.common.vo.supplier.entry.AuditStatus;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicDetailDTO;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.dto.mmp.TopicQueryDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.model.bse.Brand;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicCoupon;
import com.tp.model.sup.SupplierInfo;
import com.tp.model.sup.SupplierShop;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.mmp.AreaProxy;
import com.tp.proxy.mmp.TopicItemProxy;
import com.tp.proxy.mmp.TopicProxy;
import com.tp.proxy.sup.SupplierInfoProxy;
import com.tp.proxy.sup.SupplierShopProxy;

@Controller
public class TopicController extends AbstractBaseController {

    Logger log = LoggerFactory.getLogger(TopicController.class);


    @Autowired
    private TopicProxy topicProxy;

    @Autowired
    private TopicItemProxy topicItemProxy;

    @Autowired
    private AreaProxy areaProxy;

    @Autowired
    private SupplierInfoProxy supplierInfoProxy;

    @Autowired
    private SupplierShopProxy supplierShopProxy;
    @Autowired
    private PromoterInfoProxy promoterInfoProxy;
    
    /**
     * 复制指定主题
     *
     * @param topicId
     * @return
     */
    @RequestMapping(value = "/topic/copy", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo copyTopic(@RequestParam("topicId") Long topicId) {

        if (null == topicId) {
            return new ResultInfo(new FailInfo("指定专题无效!"));
        }
        UserInfo user = getUserInfo();
        return topicProxy.copyTopic(topicId, user.getId(), user.getUserName());
    }

    /**
     * 创建新主题
     *
     * @param topic
     * @return
     */
    @RequestMapping(value = "/topic/create", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo createTopic(Topic topic) {
        topic.setCreateUser(getUserName());
        topic.setUpdateUser(getUserName());
        return topicProxy.createTopic(topic);
    }

    /**
     * 保存专题信息
     *
     * @param topicDetail
     * @param itemInfos
     * @param removeItemIds
     * @param relateIds
     * @param removeRelateIds
     * @param request
     * @return
     */
    @RequestMapping(value = "/topic/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveTopic(TopicDetailDTO topicDetail, String itemInfos,
                                String removeItemIds, String relateIds, String removeRelateIds,
                                String topicCoupons, String removeCouponIds,
                                HttpServletRequest request,Integer reserveInventoryFlag) {
    	Long id = topicDetail.getTopic().getId();
    	
    	if(reserveInventoryFlag == null){
    		return new ResultInfo<>("/topic/" + String.valueOf(id) + "/edit");
    	}
        
        UserInfo user = getUserInfo();
//        TopicItemInfoDTO[] itemInfoList = (TopicItemInfoDTO[]) JSONArray.toArray(JSONArray.fromObject(itemInfos), TopicItemInfoDTO.class);

        JSONArray ja = JSONArray.fromObject(itemInfos);
        TopicItemInfoDTO[] itemInfoList = new TopicItemInfoDTO[ja.size()];//(TopicItemInfoDTO[]) JSONArray.toArray(JSONArray.fromObject(itemInfos), TopicItemInfoDTO.class);
        for (int i = 0; i < ja.size(); i++) {
            JSONObject jo = (JSONObject) ja.get(i);
            Object itemTags = jo.get("itemTags");
            jo.remove("itemTags");
            TopicItemInfoDTO dto = (TopicItemInfoDTO) JSONObject.toBean(jo, TopicItemInfoDTO.class);
            dto.setItemTags(itemTags == null ? null : itemTags.toString());
            itemInfoList[i] = dto;
        }


        TopicCoupon[] couponList = null;
        if (!StringUtils.isBlank(topicCoupons)) {
            couponList = (TopicCoupon[]) JSONArray.toArray(JSONArray.fromObject(topicCoupons), TopicCoupon.class);
        }
        ResultInfo result = topicProxy.saveTopicInfo(topicDetail, itemInfoList, removeItemIds, relateIds, removeRelateIds, couponList, removeCouponIds, user.getId(), user.getUserName(),reserveInventoryFlag);
        if (!result.isSuccess()) {
            return result;
        }
        return new ResultInfo("/topic/" + String.valueOf(id) + "/edit");
    }

    /**
     * 提交专题信息
     *
     * @param topicDetail
     * @param itemInfos
     * @param removeItemIds
     * @param relateIds
     * @param removeRelateIds
     * @param request
     * @return
     */
    @RequestMapping(value = "/topic/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo submitTopic(TopicDetailDTO topicDetail, String itemInfos,
                                  String removeItemIds, String relateIds, String removeRelateIds,
                                  String topicCoupons, String removeCouponIds,
                                  HttpServletRequest request,Integer reserveInventoryFlag) {

        UserInfo user = getUserInfo();

        JSONArray ja = JSONArray.fromObject(itemInfos);
        TopicItemInfoDTO[] itemInfoList = new TopicItemInfoDTO[ja.size()];//(TopicItemInfoDTO[]) JSONArray.toArray(JSONArray.fromObject(itemInfos), TopicItemInfoDTO.class);
        for (int i = 0; i < ja.size(); i++) {
            JSONObject jo = (JSONObject) ja.get(i);
            Object itemTags = jo.get("itemTags");
            jo.remove("itemTags");
            TopicItemInfoDTO dto = (TopicItemInfoDTO) JSONObject.toBean(jo, TopicItemInfoDTO.class);
            dto.setItemTags(itemTags == null ? null : itemTags.toString());
            itemInfoList[i] = dto;
        }
        TopicCoupon[] couponList = null;
        if (!StringUtils.isBlank(topicCoupons)) {
            couponList = (TopicCoupon[]) JSONArray.toArray(JSONArray.fromObject(topicCoupons), TopicCoupon.class);
        }
        ResultInfo result = topicProxy.submitTopicInfo(topicDetail, itemInfoList, removeItemIds, relateIds, removeRelateIds, couponList, removeCouponIds, user.getId(), user.getUserName(),reserveInventoryFlag);
        if (result.isSuccess()) {
            result.setData("/topic/load.htm");
        }
        return result;
    }

    /**
     * 批准专题信息
     *
     * @param topicId
     * @param request
     * @return
     */
    @RequestMapping(value = "/topic/approve", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo approveTopic(@RequestParam("topicId") Long topicId, HttpServletRequest request) {
        UserInfo user = getUserInfo();
        return topicProxy.approveTopic(topicId, user.getId(), user.getUserName());

    }

    /**
     * 驳回专题信息
     *
     * @param topicId
     * @param request
     * @return
     */
    @RequestMapping(value = "/topic/refuse", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo refuseTopic(@RequestParam("topicId") Long topicId, HttpServletRequest request) {

        UserInfo user = getUserInfo();
        return topicProxy.refuseTopic(topicId, user.getId(),
                user.getUserName());

    }

    /**
     * 终止专题信息
     *
     * @param topicId
     * @param request
     * @return
     */
    @RequestMapping(value = "/topic/terminate", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo terminateTopic(@RequestParam("topicId") Long topicId, HttpServletRequest request) {
        UserInfo user = getUserInfo();
        return topicProxy.terminateTopic(topicId, user.getId(), user.getUserName());

    }

    /**
     * 取消专题信息
     *
     * @param topicId
     * @param request
     * @return
     */
    @RequestMapping(value = "/topic/cancel", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo cancelTopic(@RequestParam("topicId") Long topicId, HttpServletRequest request) {

        UserInfo user = getUserInfo();
        return topicProxy.cancelTopic(topicId, user.getId(), user.getUserName());

    }

    /**
     * 查看专题活动详细信息
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/topic/{id}/detail", method = RequestMethod.GET)
    public String viewTopic(@PathVariable Long id, ModelMap model) {
        ResultInfo<TopicDetailDTO> result = topicProxy.getTopicDetailById(id);
        model.addAttribute("topicDetail", result.getData());
        model.addAttribute("result", result);
        model.addAttribute("mode", "view");
        model.addAttribute("platformEnum", PlatformEnum.values());
        model.addAttribute("promoterInfoList",queryCompanyPromoterList(result.getData().getPromoterIdList()));
        return "promotion/topicEdit";
    }

    /**
     * 查看专题活动详细信息
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/topic/upload/Image/{controlName}", method = RequestMethod.GET)
    public String uploadTopicImg(@PathVariable String controlName, ModelMap model, HttpServletRequest request) {

        if (null == controlName) {
            return "";
        }
        model.addAttribute("sid", request.getSession().getId());
        model.addAttribute("controlName", controlName);
        model.addAttribute("fullControlName", controlName + "Img");
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.cmsimg.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.cmsimg.name());
        return "promotion/topicUploadPic";
    }

    /**
     * 增加活动库存
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/topic/{id}/increaseStock", method = RequestMethod.GET)
    public String increaseTopicStock(@PathVariable Long id, ModelMap model) {
        ResultInfo<TopicDetailDTO> resultInfo = topicProxy.getTopicDetailById(id);
        model.addAttribute("topicDetail", resultInfo.getData());
        model.addAttribute("result", resultInfo);
        model.addAttribute("mode", "view");
        return "promotion/topicEdit";
    }

    /**
     * 编辑指定专题活动
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/topic/{id}/edit", method = RequestMethod.GET)
    public String editTopic(@PathVariable Long id, ModelMap model, HttpServletRequest request) {
    	System.out.println(topicProxy);
        ResultInfo<TopicDetailDTO> resultInfo = topicProxy.getTopicDetailById(id);
        TopicDetailDTO topicDetail = resultInfo.getData();
        // 编辑时，如果区域和平台为空，默认赋为全部
        if (null != topicDetail.getTopic()) {
            String area = topicDetail.getTopic().getAreaStr();
            String platform = topicDetail.getTopic().getPlatformStr();
            if (StringUtils.isBlank(area)) {
                topicDetail.getTopic().setAreaStr(String.valueOf(AreaConstant.AREA_ALL));
            }
            if (StringUtils.isBlank(platform)) {
                topicDetail.getTopic().setPlatformStr(String.valueOf(PlatformEnum.ALL.getCode()));
            }
            // 编辑时，如果编号为空，将序号 乘以10作为编号内容
            if (StringUtils.isBlank(topicDetail.getTopic().getNumber()) && null != topicDetail.getTopic().getId()) {
                Long number = topicDetail.getTopic().getId() * 10;
                topicDetail.getTopic().setNumber(String.valueOf(number));
            }
        }
        model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.cmsimg.url);
        model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.cmsimg.name());
        model.addAttribute("topicDetail", topicDetail);
        model.addAttribute("sid", request.getSession().getId());
        model.addAttribute("mode", "edit");
        model.addAttribute("platformEnum", PlatformEnum.values());
        model.addAttribute("promoterInfoList",queryCompanyPromoterList(topicDetail.getPromoterIdList()));
        return "promotion/topicEdit";
    }

    /**
     * 加载新增专题活动页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/topic/add")
    public String addTopic(ModelMap model) {

        return "promotion/topicAdd";
    }

    /**
     * 加载专题活动查询页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/topic/load")
    public String load(ModelMap model) {

        return "promotion/topicSearch";
    }

    /**
     * 查询专题活动
     *
     * @param id
     * @param type
     * @param salesPartten
     * @param number
     * @param name
     * @param status
     * @param process
     * @param perCount
     * @param pageNo
     * @param model
     * @return
     */
    @RequestMapping(value = "/topic/search", method = RequestMethod.POST)
    public String searchTopics(
            @RequestParam("id") Long id,
            @RequestParam("type") Integer type,
            @RequestParam(value = "salesPartten", defaultValue = "0") Integer salesPartten,
            @RequestParam("number") String number,
            @RequestParam("name") String name,
            @RequestParam("status") Integer status,
            @RequestParam("process") Integer process,
            @RequestParam("perCount") Integer perCount,
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam(value = "sort",required = false) Integer sort,

            ModelMap model) {

        if (null == pageNo || 0 == pageNo) {
            pageNo = TopicConstant.DEFAULT_PAGE_NO;
        }
        if (null == perCount || 0 == perCount) {
            perCount = TopicConstant.DEFAULT_PER_PAGE_SIZE;
        }
        if (pageNo > 0) {
            // 拼装查询条件对象
            TopicQueryDTO topic = new TopicQueryDTO();
            if (StringUtils.isNotBlank(number)) {
                topic.setNumber(number);

            }
            if (!StringUtils.isBlank(name)) {
                topic.setName(name);
            }
            if (null != type && 0 != type) {
                topic.setType(type);
            }
            if (null != status && -1 != status) {
                topic.setStatus(status);
            }
            if (null != process && -1 != process) {
                topic.setProgress(process);
            }
            if (null != salesPartten && 0 != salesPartten) {
                topic.setSalesPartten(salesPartten);
            }
            if (null != id && 0 < id) {
                topic.setId(id);
            }
            topic.setSortField(sort);

            // 查询专题数据
            ResultInfo<PageInfo<Topic>> topics = topicProxy.getTopicInfosByPagedWithLike(topic, pageNo, perCount);

            if (topics.isSuccess() && !CollectionUtils.isEmpty(topics.getData().getRows())) {
                ResultInfo<List<TopicDetailDTO>> topicDetailResult = topicProxy.getSingleProductSkuInfo(topics.getData().getRows());
                // 反馈专题查询结果
                model.addAttribute("topics", topicDetailResult.getData());
                model.addAttribute("topicCount", topics.getData().getRecords());
                model.addAttribute("perCount", topics.getData().getSize());
                model.addAttribute("currPage", topics.getData().getPage());
                model.addAttribute("totalPage", topics.getData().getTotal());
                model.addAttribute("sort",sort);
            }
        }

        return "promotion/subpages/topicList";
    }

    @RequestMapping(value = "/topic/scan")
    @ResponseBody
    public String taskTopic() {
        if (topicProxy.scanTopicStatus().isSuccess()) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequestMapping(value = "/topic/brand/query")
    public String showSearchBrand(ModelMap model) {
        model.addAttribute("pageSize", "10");
        model.addAttribute("currPage", "1");
        model.addAttribute("totalPage", "1");
        model.addAttribute("brandCount", "0");
        return "promotion/topicBrandSearch";
    }

    @RequestMapping(value = "/topic/supplier/query")
    public String showSearchSupplier(ModelMap model) {
        Map<String, Object> params = new HashMap<>();
        params.put("status", Constant.ENABLED.YES);
        params.put("auditStatus", AuditStatus.THROUGH.getStatus());
        params.put(DAOConstant.MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc,supplier_type asc");
        ResultInfo<List<SupplierInfo>> resultInfo = supplierInfoProxy.queryByParam(params);
        model.put("result", resultInfo);

        return "promotion/topicSupplierSearch";
    }

    @ResponseBody
    @RequestMapping(value = "/topic/supplier/querySupplierShop")
    public ResultInfo<SupplierShop> querySupplierShop(@RequestParam("supplierId") Long supplierId) {
        SupplierShop supplierShop = new SupplierShop();
        supplierShop.setSupplierId(supplierId);
        supplierShop.setStatus(1);
        ResultInfo<List<SupplierShop>> resultInfo = supplierShopProxy.queryByObject(supplierShop);
        if (resultInfo.isSuccess() && !CollectionUtils.isEmpty(resultInfo.getData())) {
            return new ResultInfo<>(resultInfo.getData().get(0));
        } else if (resultInfo.isSuccess()) {
            return new ResultInfo<>();
        } else {
            return new ResultInfo<>(resultInfo.getMsg());
        }
    }

    @RequestMapping(value = "/topic/setbrand/search", method = RequestMethod.POST)
    public String searchBrand(@RequestParam("nameEn") String nameEn,
                              @RequestParam("name") String name,
                              @RequestParam("startPage") Integer startPage,
                              @RequestParam("pageSize") Integer pageSize, ModelMap model,
                              HttpServletRequest request) {
        Brand brand = new Brand();
        brand.setName(name);
        brand.setNameEn(nameEn);
        brand.setPageSize(pageSize);
        brand.setStartPage(startPage);
        ResultInfo<PageInfo<Brand>> brandsResult = topicProxy.searchTopicBrand(brand);
        model.addAttribute("brands", brandsResult.getData().getRows());
        model.addAttribute("brandCount", brandsResult.getData().getRecords());
        model.addAttribute("totalPage", brandsResult.getData().getTotal());
        model.addAttribute("currPage", brandsResult.getData().getPage());
        model.addAttribute("pageSize", brandsResult.getData().getSize());
        return "promotion/subpages/topicBrandList";
    }

    @RequestMapping(value = "topic/brand/confirm", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo searchBrandInfo(@RequestParam("brandId") Long brandId, ModelMap model) {
        return topicProxy.searchTopicBrandInfoById(brandId);
    }

    @RequestMapping(value = "topic/supplier/confirm", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo searchSupplierInfo(@RequestParam("supplierId") Long supplierId, ModelMap model) {
        return supplierInfoProxy.queryById(supplierId);
    }
    
    private List<PromoterInfo> queryCompanyPromoterList(final List<Long> promoterIdList){
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("promoterType", DssConstant.PROMOTER_TYPE.COMPANY.code);
    	params.put("promoterStatus", DssConstant.PROMOTER_STATUS.EN_PASS.code);
    	List<PromoterInfo> list = promoterInfoProxy.queryByParam(params).getData();
    	if(!CollectionUtils.isEmpty(promoterIdList) && !CollectionUtils.isEmpty(list)){
    		for(Long promoterId:promoterIdList){
    			for(PromoterInfo promoterInfo:list){
    				if(promoterId.equals(promoterInfo.getPromoterId())){
    					promoterInfo.setUserAgreed(Boolean.TRUE);
    				}
    			}
    		}
    	}
    	return list;
    }
    /**
     * 批量查询商品
     * @param model
     * @param sku
     * @param spu
     * @param supplierId 供应商id
     * @param brandId 品牌id
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/topic/batchAddItemList", method = RequestMethod.GET)
    public String searchBrand(ModelMap model,String sku,String spu,Long supplierId,Long brandId,Integer sortIndex,Long topicId,String oldSkus,Integer page,Integer pageSize) {
    	Map<String,Object> params = new HashMap<String,Object>();
    	
    	if(StringUtils.isNotBlank(oldSkus)){
    		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sku not in ("+oldSkus+")");
    	}
    	if(StringUtils.isNotBlank(sku)){
    		params.put(MYBATIS_SPECIAL_STRING.LIKE.name(), " sku like concat('%','"+sku+"','%')");
    	}
    	if(null != spu && !"0".equals(spu)){
    		params.put("spu", spu);
    	}
    	if(null != supplierId && -1 != supplierId){
    		params.put("sp_id", supplierId);
    	}
    	if(null != brandId && 0 != brandId){
    		params.put("brand_id", brandId);
    	}
    
    	ResultInfo<PageInfo<TopicItemInfoDTO>> result = topicItemProxy.getTopicItemInfoBySkus(params, page, pageSize);
        model.put("result", result);
        model.put("topicId", topicId);
        model.put("sortIndex", sortIndex);
        model.put("sku", sku);
        model.put("oldSkus", oldSkus);
        model.put("params", params);
        
        return "promotion/subpages/chooseItemList";
    }
}

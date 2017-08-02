package com.tp.backend.controller.mmp;

import com.tp.backend.controller.AbstractBaseController;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.TopicChangeDetailDTO;
import com.tp.dto.mmp.TopicItemInfoDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mmp.TopicChange;
import com.tp.model.mmp.TopicCouponChange;
import com.tp.model.usr.UserInfo;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.mmp.TopicChangeProxy;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
@Scope(BeanDefinition.SCOPE_SINGLETON)
@RequestMapping(value = "/topicChange")
public class TopicChangeController extends AbstractBaseController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TopicChangeProxy topicChangeProxy;
    @Autowired
    private PromoterInfoProxy promoterInfoProxy;

    @RequestMapping(value = "/search")
    public String searchTopics(@RequestParam("changeId") Long changeId,
                               @RequestParam("type") Integer type,
                               @RequestParam("number") String number,
                               @RequestParam(value = "salesPartten", defaultValue = "0") Integer salesPartten,
                               @RequestParam("name") String name,
                               @RequestParam("status") Integer status,
                               @RequestParam("process") Integer process,
                               @RequestParam("perCount") Integer perCount,
                               @RequestParam("pageNo") Integer pageNo, ModelMap model) {

        if (null == pageNo || 0 == pageNo) {
            pageNo = TopicConstant.DEFAULT_PAGE_NO;
        }
        if (null == perCount || 0 == perCount) {
            perCount = TopicConstant.DEFAULT_PER_PAGE_SIZE;
        }
        try {
            if (pageNo > 0) {
                // 拼装查询条件对象
                TopicChange topicChange = new TopicChange();
                if (!StringUtils.isBlank(number)) {
                    topicChange.setNumber(String.valueOf(number));
                }
                if (!StringUtils.isBlank(name)) {
                    topicChange.setName(name);
                }
                if (null != type && 0 != type) {
                    topicChange.setType(type);
                }
                if (null != status && -1 != status) {
                    topicChange.setStatus(status);
                }
                if (null != process && -1 != process) {
                    topicChange.setProgress(process);
                }
                if (null != salesPartten && 0 != salesPartten) {
                    topicChange.setSalesPartten(salesPartten);
                }
                if (null != changeId && 0 < changeId) {
                    topicChange.setChangeTopicId(changeId);
                }
                // 查询专题数据
                PageInfo<TopicChange> topics = topicChangeProxy.getTopicInfosByPagedWithLike(topicChange, pageNo, perCount);
                // 反馈专题查询结果
                model.addAttribute("topicChanges", topics.getRows());
                model.addAttribute("topicCount", topics.getRecords());
                    model.addAttribute("perCount", topics.getSize());
                model.addAttribute("currPage", topics.getPage());
                model.addAttribute("totalPage", topics.getTotal());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "promotion/subpages/topicChangeList";
    }

    /**
     * 加载专题活动变更单查询页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/load")
    public String load(ModelMap model) {

        return "promotion/topicChangeSearch";
    }

    /**
     * 新增专题活动变更单查询页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/add")
    public String add(ModelMap model) {

        return "promotion/topicChangeAdd";
    }

    /**
     * 编辑专题活动变更单查询页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/{id}/edit")
    public String edit(@PathVariable("id") Long id, ModelMap model,
                       HttpServletRequest request) {
        try {

            TopicChangeDetailDTO detailDTO = topicChangeProxy.getTopicChange(id);
            model.put("topicDetail", detailDTO);
            model.put("mode", "edit");
            model.put("order", "edit");
            model.put("topicChangeId", id);
            model.addAttribute("platformEnum", PlatformEnum.values());
            model.addAttribute("promoterInfoList",queryCompanyPromoterList(detailDTO.getPromoterIdList()));
            model.put("changeTopicId", detailDTO.getTopic().getChangeTopicId());
            model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.cmsimg.url);
            model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.cmsimg.name());
            model.put("sid", request.getSession().getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "promotion/topicChangeEdit";
    }

    /**
     * 查看专题活动变更单
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/{id}/detail")
    public String detail(@PathVariable("id") Long id, ModelMap model) {
        try {
            TopicChangeDetailDTO detailDTO = topicChangeProxy.getTopicChange(id);
            model.put("topicDetail", detailDTO);
            model.put("order", "view");
            model.put("mode", "view");
            model.put("topicChangeId", id);
            model.addAttribute("platformEnum", PlatformEnum.values());
            model.addAttribute("promoterInfoList",queryCompanyPromoterList(detailDTO.getPromoterIdList()));
            model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.cmsimg.url);
            model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.cmsimg.name());
            model.put("changeTopicId", detailDTO.getTopic().getChangeTopicId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "promotion/topicChangeEdit";
    }

    /**
     * 查询专题活动变更单
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/{id}/search")
    public String search(@PathVariable("id") Long id, ModelMap model, HttpServletRequest request) {
        try {
            TopicChangeDetailDTO detailDTO = topicChangeProxy.getTopicChange(id);
            model.put("topicDetail", detailDTO);
            model.put("changeTopicId", detailDTO.getTopic().getChangeTopicId());
            model.put("mode", "edit");
            model.put("order", "edit");
            model.put("sid", request.getSession().getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "promotion/subpages/topicChangeOrder";
    }

    /**
     * 查询专题活动变更单(只读)
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/{id}/searchReadonly")
    public String searchReadonly(@PathVariable("id") Long id, ModelMap model,
                                 HttpServletRequest request) {
        try {
            TopicChangeDetailDTO detailDTO = topicChangeProxy.getTopicChange(id);
            model.put("mode", "view");
            model.put("order", "view");
            model.put("topicDetail", detailDTO);
            model.put("changeTopicId", detailDTO.getTopic().getChangeTopicId());
            model.addAttribute("bucketURL", Constant.IMAGE_URL_TYPE.cmsimg.url);
            model.addAttribute("bucketname", Constant.IMAGE_URL_TYPE.cmsimg.name());
            model.put("sid", request.getSession().getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "promotion/subpages/topicChangeOrder";
    }

    /**
     * 编辑专题活动变更单查询页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/importTopic")
    @ResponseBody
    public String importTopic(@RequestParam("id") Long id, ModelMap model) {

        UserInfo user = getUserInfo();
        ResultInfo<Long> result = topicChangeProxy.getNewTopicChange(id, user.getId(), user.getUserName());
        if (result.getData() == null) {
            return "";
        }
        if (!result.isSuccess()) {
            model.put("errorInfo", result.getMsg().getMessage());
        }
        return "/topicChange/" + String.valueOf(result.getData()) + "/edit";

    }
    
    /**
     * 生成变更单
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/generateTopic", method = RequestMethod.GET)
    @ResponseBody
    public ResultInfo generateTopicChange(@RequestParam("id") Long id, ModelMap model, HttpServletRequest request) {

        UserInfo user = getUserInfo();
        ResultInfo<Long> resultInfo = topicChangeProxy.generateTopicChange(id, user.getId(), user.getUserName());
        return resultInfo;
    }

    /**
     * 审批专题活动变更单查询页面
     *
     * @return
     */
    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo approve(@RequestParam("topicChangeId") Long topicChangeId, HttpServletRequest request) {
        UserInfo user = getUserInfo();
        return topicChangeProxy.approveTopicChange(topicChangeId, user.getId(), user.getUserName());
    }

    /**
     * 取消专题变更单
     *
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo cancelTopic(@RequestParam("topicChangeId") Long topicChangeId, HttpServletRequest request) {
        UserInfo user = getUserInfo();
        return topicChangeProxy.cancelTopicChange(topicChangeId, user.getId(), user.getUserName());

    }

    /**
     * 驳回活动变更单
     *
     * @param topicId
     * @param request
     * @return
     */
    @RequestMapping(value = "/refuse", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo refuseTopicChange(@RequestParam("topicChangeId") Long topicChangeId, HttpServletRequest request) {
        UserInfo user = getUserInfo();
        return topicChangeProxy.refuseTopicChange(topicChangeId, user.getId(), user.getUserName());

    }

    /**
     * 保存活动变更单
     *
     * @param topicChangeDetail
     * @param itemInfos
     * @param removeItemIds
     * @param relateIds
     * @param removeRelateIds
     * @param request
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo saveTopicChange(TopicChangeDetailDTO topicChangeDetail,
                                      String itemInfos, String removeItemIds, String relateIds,
                                      String removeRelateIds, String topicCoupons,
                                      String removeCouponIds, HttpServletRequest request,String reserveInventoryFlag) {
        Long id = topicChangeDetail.getTopic().getId();
        try {
        	topicChangeDetail.getTopic().setReserveInventoryFlag(Integer.parseInt(reserveInventoryFlag));
		} catch (Exception e) {
			return new ResultInfo("数据转换错误");
		}
        
        UserInfo user = getUserInfo();
       // TopicItemInfoDTO[] itemInfoList = (TopicItemInfoDTO[]) JSONArray.toArray(JSONArray.fromObject(itemInfos), TopicItemInfoDTO.class);
        JSONArray ja = JSONArray.fromObject(itemInfos);
        TopicItemInfoDTO[] itemInfoList = new TopicItemInfoDTO[ja.size()];//(TopicItemInfoDTO[]) JSONArray.toArray(JSONArray.fromObject(itemInfos), TopicItemInfoDTO.class);
        for(int i=0; i<ja.size(); i++){
            JSONObject jo = (JSONObject) ja.get(i);
            Object itemTags = jo.get("itemTags");
            jo.remove("itemTags");
            TopicItemInfoDTO dto = (TopicItemInfoDTO) JSONObject.toBean(jo, TopicItemInfoDTO.class);
            dto.setItemTags(itemTags == null ? null : itemTags.toString());
            itemInfoList[i] = dto;
        }
        TopicCouponChange[] couponList = null;
        if (!StringUtils.isBlank(topicCoupons)) {
            couponList = (TopicCouponChange[]) JSONArray.toArray(JSONArray.fromObject(topicCoupons), TopicCouponChange.class);
        }
        ResultInfo result = topicChangeProxy.saveTopicChangeInfo(topicChangeDetail, itemInfoList, removeItemIds, relateIds,
                removeRelateIds, couponList, removeCouponIds, user.getId(), user.getUserName());
        if (!result.isSuccess()) {
            //return new ResultInfo(result.getMsg());
            return new ResultInfo<Boolean>(result.getMsg());
        }
        return new ResultInfo("/topicChange/" + String.valueOf(id) + "/edit");
    }

    /**
     * 提交活动变更单
     *
     * @param topicChangeDetail
     * @param itemInfos
     * @param removeItemIds
     * @param relateIds
     * @param removeRelateIds
     * @param request
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @ResponseBody
    public ResultInfo submitTopicChange(TopicChangeDetailDTO topicChangeDetail,
                                        String itemInfos, String removeItemIds, String relateIds,
                                        String removeRelateIds, String topicCoupons,
                                        String removeCouponIds, HttpServletRequest request,Integer reserveInventoryFlag) {

      		topicChangeDetail.getTopic().setReserveInventoryFlag(reserveInventoryFlag);

            UserInfo user = getUserInfo();
            JSONArray ja = JSONArray.fromObject(itemInfos);
            TopicItemInfoDTO[] itemInfoList = new TopicItemInfoDTO[ja.size()];//(TopicItemInfoDTO[]) JSONArray.toArray(JSONArray.fromObject(itemInfos), TopicItemInfoDTO.class);
            for(int i=0; i<ja.size(); i++){
            	JSONObject jo = (JSONObject) ja.get(i);
            	Object itemTags = jo.get("itemTags");
            	jo.remove("itemTags");
            	TopicItemInfoDTO dto = (TopicItemInfoDTO) JSONObject.toBean(jo, TopicItemInfoDTO.class);
            	dto.setItemTags(itemTags == null ? null : itemTags.toString());
            	itemInfoList[i] = dto;
            }
            TopicCouponChange[] couponList = null;
            if (!StringUtils.isBlank(topicCoupons)) {
                couponList = (TopicCouponChange[]) JSONArray.toArray( JSONArray.fromObject(topicCoupons), TopicCouponChange.class);
            }
            ResultInfo result = topicChangeProxy.submitTopicInfo(
                    topicChangeDetail, itemInfoList, removeItemIds, relateIds,
                    removeRelateIds, couponList, removeCouponIds, user.getId(),
                    user.getUserName());
            if (!result.isSuccess()) {
//                return new ResultInfo(result.getMsg());
            	result.getMsg().setMessage(result.getMsg().getModelType());
                return new ResultInfo<Boolean>(result.getMsg());
            }

        return new ResultInfo( "/topicChange/load.htm");
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
}

package com.tp.proxy.cmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DAOConstant.WHERE_ENTRY;
import com.tp.dto.common.ResultInfo;
import com.tp.model.cmt.ItemReview;
import com.tp.model.mem.MemberInfo;
import com.tp.model.prd.ItemInfo;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.cmt.IItemReviewService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.prd.IItemInfoService;
import com.tp.service.prd.IItemService;
import com.tp.util.BeanUtil;
import com.tp.util.StringUtil;

/**
 * 商品评论信息表代理层
 * @author szy
 *
 */
@Service
public class ItemReviewProxy extends BaseProxy<ItemReview>{

	@Autowired
	private IItemReviewService itemReviewService;

	@Autowired
	private IItemService itemService;
	
	@Autowired
	private IItemInfoService itemInfoService;
	
	@Autowired
	private ItemReviewValidationProxy itemReviewValidationProxy;
	
	@Autowired
	private IMemberInfoService memberInfoService;
	
	@Override
	public IBaseService<ItemReview> getService() {
		return itemReviewService;
	}
	
	public ResultInfo<ItemReview> save(ItemReview review,Long uid, String username){		
		review.setPid(itemService.getPrdidCode(review.getSkuCode()));
		review.setUid(uid);
		review.setUserName(username);
		return itemReviewService.save(review);
	}
		
	
	public Map<String,ItemReview> findItemReview(String orderCode,Long userId){
		Map<String, Object> params = new HashMap<>();
		params.put("orderCode", orderCode);
		params.put("uid", userId);
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), "create_time desc");
		List<ItemReview> list =  this.itemReviewService.queryByParamNotEmpty(params);
		
		if(null == list) return null;
		Map<String,ItemReview> map = new HashMap<String,ItemReview>();
		for (ItemReview item : list) {
			map.put(item.getSkuCode(), item);
		}
		return map;
	}
	
	public ItemReview getItemReview(Long id){
		if (null == id) return null;
		ResultInfo<ItemReview> resultInfo =	queryById(id);
		if (Boolean.FALSE == resultInfo.isSuccess()) {
			return null;
		}
		ItemReview itemReview = resultInfo.getData();
		Map<String, Object> params = new HashMap<>();
		if (StringUtil.isNullOrEmpty(itemReview.getSpu())) {
			return itemReview;
		}
    	params.put("spu", itemReview.getSpu());
    	List<ItemInfo> itemInfos = itemInfoService.queryByParam(params);
		if (CollectionUtils.isNotEmpty(itemInfos)) {
			itemReview.setItemTitle(itemInfos.get(0).getMainTitle());
		}
		return itemReview;
	}
	
	/** 后台调用接口*/
    public PageInfo<ItemReview> getItemReview(ItemReview itemReview, PageInfo<ItemReview> pageInfo){
    	Map<String, Object> params = BeanUtil.beanMap(itemReview);
    	params.remove("createBeginTime");params.remove("createEndTime");    	
    	params.remove("prds");params.remove("count");
    	List<WHERE_ENTRY> whEntries = new ArrayList<>();
    	if (null != itemReview.getCreateBeginTime()) {
			whEntries.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.GT, itemReview.getCreateBeginTime()));
		}
    	if (null != itemReview.getCreateEndTime()) {
			whEntries.add(new WHERE_ENTRY("create_time", MYBATIS_SPECIAL_STRING.LT, itemReview.getCreateEndTime()));
		}
    	params.put(MYBATIS_SPECIAL_STRING.WHERE.name(), whEntries);
    	ResultInfo<PageInfo<ItemReview>> result = queryPageByParamNotEmpty(params, pageInfo);
    	if (CollectionUtils.isEmpty(result.getData().getRows())) {
			return result.getData();
		}
    	
    	List<String> pids = new ArrayList<>();
    	for (ItemReview itemE : result.getData().getRows()) {
    		if (StringUtil.isNotEmpty(itemE.getSpu())) {
    			pids.add(itemE.getSpu());
			}    		
		}
    	//查询商品的名称
    	params.clear();
    	params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "spu in(" + StringUtil.join(pids, Constant.SPLIT_SIGN.COMMA) + ")");
    	List<ItemInfo> itemInfos = itemInfoService.queryByParam(params);
    	Map<String, String> mapSpu2Name = new HashMap<>();
    	for (ItemInfo itemInfoE : itemInfos) {
    		mapSpu2Name.put(itemInfoE.getSpu(),itemInfoE.getMainTitle());
		}
    	
    	List<ItemReview> itemReviewObjs = result.getData().getRows();
    	for (ItemReview itemE : itemReviewObjs) {
			String tmpName = mapSpu2Name.get(itemE.getSpu());
			if (null != tmpName) {
				itemE.setItemTitle(tmpName);
			}
		}
    	
    	//违禁词变**(test)(backend不需禁用)
    	Pattern pattern = itemReviewValidationProxy.getForbiddenWordsPattern();
    	//过滤js字符串
    	for (ItemReview itemReview2 : itemReviewObjs) {
			String content = Pattern.compile("<script.*?>.*?</script>", Pattern.CASE_INSENSITIVE).matcher(itemReview2.getContent()).replaceAll(" ");
			itemReview2.setContent(itemReviewValidationProxy.filterItemReviewContent(pattern, content, "**"));
		}
    	
    	return result.getData();
    }
    
	public MemberInfo getmemberInfoByLoginName(String loginName){
		//获取用户信息
		MemberInfo memberInfo = null;
		if(StringUtils.isBlank(loginName)){
			return null;
		}
		memberInfo = new MemberInfo();
		memberInfo.setMobile(loginName);
		List<MemberInfo> memberInfos = memberInfoService.queryByObject(memberInfo);
		if(CollectionUtils.isNotEmpty(memberInfos)){
			return memberInfos.get(0);
		}
		return null;
	}
}

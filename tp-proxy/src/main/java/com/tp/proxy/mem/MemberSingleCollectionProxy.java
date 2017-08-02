package com.tp.proxy.mem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.vo.Constant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.mem.ErrorCode;
import com.tp.common.vo.mem.MemberInfoConstant.State;
import com.tp.dto.mem.ItemCollection;
import com.tp.exception.UserServiceException;
import com.tp.model.mem.MemberSingleCollection;
import com.tp.model.prd.ItemPictures;
import com.tp.model.prd.ItemSku;
import com.tp.proxy.BaseProxy;
import com.tp.service.IBaseService;
import com.tp.service.mem.IMemberSingleCollectionService;
import com.tp.service.mmp.ITopicService;
import com.tp.service.prd.IItemPicturesService;
import com.tp.service.prd.IItemSkuService;
import com.tp.util.StringUtil;
/**
 * 单品收藏代理层
 * @author szy
 *
 */
@Service
public class MemberSingleCollectionProxy extends BaseProxy<MemberSingleCollection>{

	@Autowired
	private IMemberSingleCollectionService memberSingleCollectionService;
	@Autowired
	private IItemPicturesService itemPicturesService;
	@Autowired
	private IItemSkuService itemSkuService;
	@Autowired
	private ITopicService topicService;

	
	public IBaseService<MemberSingleCollection> getService() {
		return memberSingleCollectionService;
	}
	
	/**
	 * 
	 * <pre>
	 * 校验商品是否被收藏了
	 * </pre>
	 *
	 * @return 返回0 表示未收藏 4046 表示该商品就已经被收藏 -1表示系统异常
	 */
	
	public Integer isCollection(String skuCode, Long userId) {
		Integer result = State.True;
		try {
			logger.info("【BEGIN】>>>>>>>>>>>>>>>>>>>>>>>>>>>>>校验产品是否被收藏");
			MemberSingleCollection sc = new MemberSingleCollection();
			sc.setSkuCode(skuCode);
			sc.setUserId(userId);
			sc.setState(Boolean.TRUE);
			Integer count = this.memberSingleCollectionService.queryByObjectCount(sc);
			
			if (null != count && count>0){ 
				logger.info("【INFO】用户已经收藏过[skuCode:"+skuCode+"]");
				result = ErrorCode.PRODUCT_HASH_COLLECTION.code;
			}
			
			logger.info("【END】校验产品是否被收藏");
		} catch (Exception e) {
			logger.error("【ERROR】校验产品异常:" + e.getMessage());
			result = ErrorCode.SYSTEM_ERROR.code;
		}
		return result;
	}
	
	
	public List<ItemCollection> checkItemsIsCollect(List<String> skuCodes,Long userId) throws UserServiceException{
		if(null == skuCodes || skuCodes.isEmpty()) return null;
		Map<String,ItemCollection> map = null;
		List<ItemCollection> icList = null;
		try {
			List<MemberSingleCollection> list = selectBySkuCodes(skuCodes, userId, 1);
			if(null == list) return null;
			map =  new HashMap<String, ItemCollection>();
			icList = new ArrayList<ItemCollection>();
			for (String skuCode : skuCodes) {
				map.put(skuCode, new ItemCollection(skuCode,Boolean.FALSE));
			}
			
			
			for (MemberSingleCollection coll : list) {
				ItemCollection ic = map.get(coll.getSkuCode());
				ic.setIsCollection(Boolean.TRUE);
				ic.setTopicId(coll.getTopicId());
				map.put(coll.getSkuCode(), ic);
			}
			
			
			for (ItemCollection itemCollection : map.values()) {
				icList.add(itemCollection);
			}
			
		} catch (Exception e) {
			logger.error("【ERROR】校验产品异常:" + e.getMessage());
			throw new UserServiceException(e.getMessage());
		}
		return icList;
	}

	/**
	 * 
	 * <pre>
	 * 收藏单品
	 * </pre>
	 *
	 */
	
	public void collectionSingleProduct(String skuCode, Long topicId, Long userId) throws UserServiceException{
		logger.info("【BEGIN】开始收藏单品");
		try {
			Integer result = isCollection(skuCode, userId);// 校验单品是否被收藏

			if (State.True.intValue() != result.intValue()) throw new UserServiceException(ErrorCode.PRODUCT_HASH_COLLECTION.code,ErrorCode.PRODUCT_HASH_COLLECTION.value);
			ItemSku skuDO = new ItemSku();
			skuDO.setSku(skuCode);

			List<ItemSku> list = itemSkuService.queryByObject(skuDO);
			if (null == list || list.isEmpty())throw new UserServiceException(ErrorCode.PRODUCT_IS_NOT_EXIST.code,ErrorCode.PRODUCT_IS_NOT_EXIST.value);

			topicService.checkCollectTopicStatus(topicId);//校验活动是否有效
			
			skuDO = list.get(0);
			
			ItemPictures picturesDO = new ItemPictures();
			picturesDO.setDetailId(skuDO.getDetailId());
			List<ItemPictures> pic = itemPicturesService.queryByObject(picturesDO);
			if(null != pic && !pic.isEmpty()) picturesDO = pic.get(0);

			MemberSingleCollection single = new MemberSingleCollection();
			single.setBrandId(skuDO.getBrandId());
			single.setCreateTime(new Date());
			single.setItemTitle(skuDO.getDetailName());
			single.setMainTitle(skuDO.getDetailName());
			single.setPrdId(skuDO.getPrdid());
			single.setTopicId(topicId);
			single.setSkuCode(skuDO.getSku());
			single.setState(Boolean.TRUE);
			single.setUpdateTime(new Date());
			single.setUserId(userId);
			single.setPrdLogo(picturesDO.getPicture());
			this.memberSingleCollectionService.insert(single);
			
			logger.info("【SUCCESS】收藏单品成功");
		} catch (Exception e) {
			logger.error("【ERROR】收藏商品异常:"+e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}

	}
	
	
	public List<MemberSingleCollection> selectUserSingleCollectionForSkuCodeAndUserId(List<String> skuCodes,Long userId) throws UserServiceException{
		try {
			return selectBySkuCodes(skuCodes, userId, 1);
		} catch (Exception e) {
			logger.error("【ERROR】查询用户收藏集合异常:"+e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
	}
	
	private List<MemberSingleCollection> selectBySkuCodes(List<String> skuCodes, Long userId, int state){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " sku_code in ("+StringUtil.join(skuCodes, Constant.SPLIT_SIGN.COMMA)+")");
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " state ="+state + " ");
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " user_id = " + userId+ " ");
		return this.memberSingleCollectionService.queryByParam(params);
	}
	
	public PageInfo<MemberSingleCollection> queryPageListForDefault(
			MemberSingleCollection userSingleCollectionDO) throws UserServiceException{
		try {
			if (null != userSingleCollectionDO) {
				PageInfo<MemberSingleCollection> page = new PageInfo<MemberSingleCollection>();
				page.setPage(userSingleCollectionDO.getStartPage());
				page.setSize(userSingleCollectionDO.getPageSize());
				page = memberSingleCollectionService.queryPageByObject(userSingleCollectionDO, page);
				return page;
			}
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: " + ex.getMessage(), ex);
			throw new UserServiceException(ex.getMessage());
		}
		return new PageInfo<MemberSingleCollection>();
	}

	
	public PageInfo<MemberSingleCollection> queryPageList(
			MemberSingleCollection userSingleCollectionDO, int startPage, int pageSize) throws UserServiceException{
		try {
			if (userSingleCollectionDO != null && startPage > 0 && pageSize > 0) {
				userSingleCollectionDO.setStartPage(startPage);
				userSingleCollectionDO.setPageSize(pageSize);
				return this.queryPageListForDefault(userSingleCollectionDO);
			}
		} catch (Exception ex) {
			logger.error(">>>>>[ERROR!]: " + ex.getMessage(), ex);
			throw new UserServiceException(ex.getMessage());
		}
		return new PageInfo<MemberSingleCollection>();
	}
	
	public void removeSingleCollect(Long id) throws UserServiceException{
		try {
			logger.info("【BEGIN】删除收藏商品");
			this.memberSingleCollectionService.deleteById(id);
			logger.info("【END】删除收藏商品成功");
		} catch (Exception e) {
			logger.error("【ERROR】删除收藏商品失败:"+e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
	}
	
	
	public void logicRemoveSingleCollect(Long id,Long userId) throws UserServiceException{
		try {
			logger.info("【BEGIN】删除收藏商品");
			MemberSingleCollection single = new MemberSingleCollection();
			if(null == id) throw new UserServiceException(ErrorCode.USERSINGLE_ID_IS_NULL.code,ErrorCode.USERSINGLE_ID_IS_NULL.value);
			single.setId(id);
			single.setUserId(userId);
			single.setState(Boolean.FALSE);
			single.setUpdateTime(new Date());
			
			this.memberSingleCollectionService.updateNotNullById(single);
			logger.info("【END】删除收藏商品成功");
		} catch (Exception e) {
			logger.error("【ERROR】删除收藏商品失败:"+e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
	}
}

package com.tp.shop.convert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tp.common.util.ImageDownUtil;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dto.ord.remote.OrderList4UserDTO;
import com.tp.dto.promoter.PromoterInfoMobileDTO;
import com.tp.dto.promoter.PromoterTopicItemDTO;
import com.tp.m.base.Page;
import com.tp.m.constant.PageConstant;
import com.tp.m.enums.ImgEnum;
import com.tp.m.query.promoter.QueryPromoter;
import com.tp.m.to.order.OrderLineTO;
import com.tp.m.util.DateUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.NumberUtil;
import com.tp.m.vo.promoter.AccountDetailVO;
import com.tp.m.vo.promoter.PromoterInfoMobileVO;
import com.tp.m.vo.promoter.PromoterInfoVO;
import com.tp.m.vo.promoter.PromoterOrderVO;
import com.tp.m.vo.promoter.PromoterTopicItemVO;
import com.tp.m.vo.promoter.TopicInfoVO;
import com.tp.m.vo.promoter.WithdrawDetailVO;
import com.tp.model.dss.AccountDetail;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.WithdrawDetail;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicInfo;
import com.tp.model.ord.OrderItem;
import com.tp.model.ord.SubOrder;
import com.tp.query.ord.SubOrderQO;
import com.tp.shop.helper.ImgHelper;
import com.tp.util.StringUtil;

/**
 * @author Administrator
 * 推广员封装类
 */
public class PromoterConvert {
	
	private static final Logger log = LoggerFactory.getLogger(PromoterConvert.class);
		
	
	public static PromoterInfoMobileVO convertPromoterMoblieDTO2VO(PromoterInfoMobileDTO dto){
	
		PromoterInfoMobileVO vo = new PromoterInfoMobileVO();
		vo.setNickname(dto.getNickname());
		vo.setWeixin(dto.getWeixin());
		vo.setQq(dto.getQq());
		vo.setMobile(dto.getMobile());
		vo.setEmail( dto.getEmail() );
		vo.setName( dto.getName() );
		vo.setCredentialtype( dto.getCredentialtype() );
		vo.setCredentialcode( dto.getCredentialcode() );
		vo.setBankname( dto.getBankname());
		vo.setBankaccount( dto.getBankaccount() );
		vo.setAlipay( dto.getAlipay() );
		vo.setPageshow( dto.getPageshow() );
		vo.setIscoupondss( dto.getIscoupondss() );
		vo.setIsshopdss( dto.getIsshopdss() );
		vo.setIsscandss( dto.getIsscandss() );
		vo.setShopmobile( dto.getShopmobile() );
		vo.setShopnickname( dto.getShopnickname() );
		return vo;
	}
	
	
	
	public static PromoterInfo convertPromoterInfo(QueryPromoter promoter){
		PromoterInfo promoterInfo = new PromoterInfo();
		promoterInfo.setPromoterId(promoter.getPromoterid());
		promoterInfo.setMemberId(promoter.getUserid());
		promoterInfo.setPromoterType(StringUtil.isEmpty(promoter.getType())?null:Integer.valueOf(promoter.getType()));
		log.info("[调用推广员接口    入参] = {}",JsonUtil.convertObjToStr(promoterInfo));
		return promoterInfo;
	}
	
	/**
	 * 封装订单列表入参
	 */
	public static SubOrderQO convertPageOrderQuery(QueryPromoter promoter){
		SubOrderQO  sq = new SubOrderQO();		
		Integer status = StringUtil.isEmpty(promoter.getOrderstatus()) ? null:Integer.valueOf(promoter.getOrderstatus());
		sq.setOrderStatus(status);
		sq.setPageSize(PageConstant.DEFAULT_PAGESIZE);
		sq.setStartPage(com.tp.m.util.StringUtil.getCurpageDefault(promoter.getCurpage()));
		Integer type = StringUtil.isEmpty(promoter.getType()) ? null:Integer.valueOf(promoter.getType());
		switch (type) {
		case 1:	//店铺
			sq.setShopPromoterId(Long.valueOf(promoter.getPromoterid()));
			break;
		case 0: //卡券
			sq.setPromoterId(Long.valueOf(promoter.getPromoterid()));
			break;
		case 2: //扫码
			sq.setScanPromoterId(Long.valueOf(promoter.getPromoterid()));
			break;
		default:
			break;
		}
		return sq;
	}
	
	/**
	 * 封装客户总数入参
	 */
	public static SubOrderQO convertSubOrderQuery(QueryPromoter promoter){
		SubOrderQO  sq = new SubOrderQO();		
//		Integer status = StringUtil.isEmpty(promoter.getOrderstatus()) ? null:Integer.valueOf(promoter.getOrderstatus());
//		sq.setOrderStatus(status);
//		sq.setPageSize(PageConstant.DEFAULT_PAGESIZE);
//		sq.setStartPage(com.tp.shop.util.StringUtil.getCurpageDefault(promoter.getCurpage()));
		Integer type = StringUtil.isEmpty(promoter.getType()) ? null:Integer.valueOf(promoter.getType());
		switch (type) {
		case 1:	//店铺
			sq.setShopPromoterId(Long.valueOf(promoter.getPromoterid()));
			break;
		case 0: //卡券
			sq.setPromoterId(Long.valueOf(promoter.getPromoterid()));
			break;
		case 2: //扫码
			sq.setScanPromoterId(Long.valueOf(promoter.getPromoterid()));
			break;
		default:
			break;
		}
		return sq;
	}	
	
	/**账户简略信息**/
	public static PromoterInfoVO convertBriefPromoterInfoVO(PromoterInfo promoterInfo){
		PromoterInfoVO vo = new PromoterInfoVO();
		vo.setPromoterid(promoterInfo.getPromoterId());
		vo.setUserid(promoterInfo.getMemberId());
		vo.setName(promoterInfo.getPromoterName());
		vo.setName(promoterInfo.getPromoterName());
		if (StringUtil.isEmpty(promoterInfo.getNickName())) {
			vo.setNickname(promoterInfo.getPromoterName());
		}else {
			vo.setNickname(promoterInfo.getNickName());
		}
		vo.setType(promoterInfo.getPromoterType());
		if(StringUtil.isNotEmpty(promoterInfo.getMobile())){
			vo.setMobile(promoterInfo.getMobile());	
		}else{
			vo.setMobile("");
		}
		
		//收入情况
		String val = "0.00";
		
		vo.setOrderamount(((val = NumberUtil.sfwr(promoterInfo.getOrderAmount())) == null) ? "0.00":val);
		vo.setTotalfees(((val = NumberUtil.sfwr(promoterInfo.getAccumulatedAmount())) == null) ? "0.00":val);
		
		return vo;
	}
	
	/**账户简略信息**/
	public static PromoterInfoVO convertDealerPromoterInfoVO(PromoterInfo promoterInfo){
		PromoterInfoVO vo = new PromoterInfoVO();
		vo.setPromoterid(promoterInfo.getPromoterId());
		if(StringUtil.isNotEmpty(promoterInfo.getNickName())){
			vo.setNickname(promoterInfo.getNickName() );
		}else{
			vo.setNickname("");
		}		
		if(StringUtil.isNotEmpty(promoterInfo.getPromoterName())){
//			vo.setName(promoterInfo.getPromoterName().substring(0, 1) + "**");
			vo.setName(promoterInfo.getPromoterName() );
		}else{
			vo.setName("");
		}
		vo.setType(promoterInfo.getPromoterType());
		if(StringUtil.isNotEmpty(promoterInfo.getMobile())){
//			vo.setMobile(promoterInfo.getMobile().substring(0, 3) + "****" + promoterInfo.getMobile().substring(7, promoterInfo.getMobile().length()));	
			vo.setMobile(promoterInfo.getMobile() );	
		}else{
			vo.setMobile("");
		}
		
		//收入情况
		String val = "0.00";
		
		vo.setOrderamount(((val = NumberUtil.sfwr(promoterInfo.getOrderAmount())) == null) ? "0.00":val);
		vo.setTotalfees(((val = NumberUtil.sfwr(promoterInfo.getAccumulatedAmount())) == null) ? "0.00":val);
		
		return vo;
	}
	
	/**账户详情**/
	public static PromoterInfoVO convertPromoterInfoVO(PromoterInfo promoterInfo){
		PromoterInfoVO vo = new PromoterInfoVO();
		vo.setPromoterid(promoterInfo.getPromoterId());
		vo.setUserid(promoterInfo.getMemberId());
//		vo.setName(promoterInfo.getPromoterName());
		if( StringUtil.isNotBlank( promoterInfo.getPromoterName())){
			int len = promoterInfo.getPromoterName().length();
			vo.setName("*"+promoterInfo.getPromoterName().substring(1, len));			
		}
				
		if (StringUtil.isEmpty(promoterInfo.getNickName())) {
//			vo.setNickname(promoterInfo.getPromoterName());
			vo.setNickname(vo.getName());
		}else {
			vo.setNickname(promoterInfo.getNickName());
		}
		vo.setType(promoterInfo.getPromoterType());
		
		if( promoterInfo.getPromoterStatus()==null || promoterInfo.getPromoterStatus()==0){	// 未开通
			vo.setStatus( DssConstant.VO_STATUS.UN_PASS.code );
		}else if( promoterInfo.getPromoterStatus() ==1 ) {  			// 已开通，未认证
			vo.setStatus(  DssConstant.VO_STATUS.EN_PASS_UN_IDEN.code ) ;				
		}else if( promoterInfo.getPromoterStatus() > 1 ){	// 店铺被禁用
			vo.setStatus( DssConstant.VO_STATUS.FORBIDDEN.code);			
		}
				
		if(promoterInfo.getPromoterStatus() ==1 	// 开通店铺已认证
				&& !StringUtil.isBlank(promoterInfo.getPromoterName())
				&& !StringUtil.isBlank(promoterInfo.getCredentialCode()) 
				&& ( ( !StringUtil.isBlank( promoterInfo.getBankName()) && !StringUtil.isBlank(promoterInfo.getBankAccount()) ) || !StringUtil.isBlank(promoterInfo.getAlipay()) ) ){
			vo.setStatus( DssConstant.VO_STATUS.IN_PASS_IDEN.code);
		}

		vo.setShareImpagePath(promoterInfo.getShareImagePath());
		vo.setGender(promoterInfo.getGenderCn());
		//计算年龄
		int difMonth = 0;
		if (null != promoterInfo.getBirthday() && (difMonth = DateUtil.getDiffMonths(promoterInfo.getBirthday(), new Date())) > 0) {
			vo.setAge(Long.valueOf((difMonth % 12) == 0?(difMonth/12):(difMonth/12+1)));
		}
		
		//基本信息
//		vo.setMobile(promoterInfo.getMobile());
		if(StringUtil.isNotBlank(promoterInfo.getMobile())){
			vo.setMobile(promoterInfo.getMobile().substring(0, 3) + "****" + promoterInfo.getMobile().substring(7, promoterInfo.getMobile().length()));	
		}
				
		vo.setQq(promoterInfo.getQq());
		vo.setWeixin(promoterInfo.getWeixin());
		vo.setEmail(promoterInfo.getEmail());
		vo.setCredential(promoterInfo.getCredentialTypeCn());
//		vo.setCredentialcode(promoterInfo.getCredentialCode());
		if( StringUtil.isNotBlank(promoterInfo.getCredentialCode()) && promoterInfo.getCredentialCode().length()>4){
			int len = promoterInfo.getCredentialCode().length();
			if(len>14){
				vo.setCredentialcode( promoterInfo.getCredentialCode().substring(0, 6) +"********" +promoterInfo.getCredentialCode().substring(14, len));					
			}else{
				vo.setCredentialcode( promoterInfo.getCredentialCode() );									
			}
		}
				
		vo.setBank(promoterInfo.getBankName());
//		vo.setBankaccount(promoterInfo.getBankAccount());
		if( StringUtil.isNotBlank(promoterInfo.getBankAccount()) && promoterInfo.getBankAccount().length()>4){
			int len = promoterInfo.getBankAccount().length();
			if(len > 4){
				vo.setBankaccount( promoterInfo.getBankAccount().substring(0, len-4) + "****" );					
			}else{
				vo.setBankaccount( promoterInfo.getBankAccount() );									
			}
		}
		
//		vo.setAlipay(promoterInfo.getAlipay());
		String alipay = promoterInfo.getAlipay();
		if(StringUtil.isNotBlank(alipay)){
			if( alipay.contains("@") ){
				int index = -1;
				index=alipay.indexOf('@');
				if(index >=3){
					vo.setAlipay(alipay.substring(0, 3) + "***" + alipay.substring(index, alipay.length()));					
				}else{
					vo.setAlipay(alipay.substring(0, index) + "***" + alipay.substring(index, alipay.length()));										
				}
			}else{
				if(alipay.length()>7 ){
					vo.setAlipay(alipay.substring(0, 3) + "****" + alipay.substring(7, alipay.length()));						
				}else if(alipay.length()>3){
					vo.setAlipay(alipay.substring(0, 3)  + "****"  );											
				}else{
					vo.setAlipay(alipay );																
				}
			}			
		}
			
		
		// 扫码图片
		vo.setImgurl(promoterInfo.getScanAttentionImage());
		//收入情况
		String val = "0.00";
		vo.setReferralfees(((val = NumberUtil.sfwr(promoterInfo.getReferralFees())) == null) ? "0.00":val);
		vo.setWithdrawedfees(((val = NumberUtil.sfwr(promoterInfo.getWithdrawAmount())) == null) ? "0.00":val);
		vo.setSurplusamount(((val = NumberUtil.sfwr(promoterInfo.getSurplusAmount())) == null) ? "0.00":val);
		
		vo.setOrderamount(((val = NumberUtil.sfwr(promoterInfo.getOrderAmount())) == null) ? "0.00":val);
		vo.setTotalfees(((val = NumberUtil.sfwr(promoterInfo.getAccumulatedAmount())) == null) ? "0.00":val);
		
		vo.setOrdercount(promoterInfo.getOrderCount()==null?"0":promoterInfo.getOrderCount().toString());	
		
		vo.setPasstime(promoterInfo.getPassTime() == null?"":com.tp.util.DateUtil.format(promoterInfo.getPassTime(), "yyyy-MM-dd"));
		
		if(DssConstant.PROMOTER_STATUS.UN_PASS.code.equals(promoterInfo.getPromoterStatus()))
			vo.setPaycode(promoterInfo.getBizCode().toString());
		return vo;
	}
	
	/**获取分享地址信息**/
	public static PromoterInfoVO convertPromoterShareShopInfoVO(PromoterInfo promoterInfo){
		PromoterInfoVO vo = new PromoterInfoVO();
		vo.setShareImpagePath(promoterInfo.getShareImagePath());
		return vo;
	}
		
	/**账单详情**/
	public static Page<AccountDetailVO> convertPageAccountDetail(PageInfo<AccountDetail> page){
		Page<AccountDetailVO> voPage = new Page<AccountDetailVO>();
		if (null != page) {
			List<AccountDetailVO> l = new ArrayList<>();
			List<AccountDetail> rows = page.getRows();
			if (CollectionUtils.isNotEmpty(rows)) {
				for (AccountDetail accountDetail : rows) {
					l.add(convertAccountDetailVO(accountDetail));
				}
				voPage.setFieldTCount(l, page.getPage(), page.getRecords());
			}
			voPage.setCurpage(page.getPage());
		}
		return voPage;
	}	
	
	public static Page<PromoterInfoVO> convertPageDealers(PageInfo<PromoterInfo> page){
		Page<PromoterInfoVO> voPage = new Page<PromoterInfoVO>();
		if (null != page) {
			List<PromoterInfoVO> l = new ArrayList<>();
			List<PromoterInfo> rows = page.getRows();
			if (CollectionUtils.isNotEmpty(rows)) {
				for (PromoterInfo p : rows) {
					l.add(convertDealerPromoterInfoVO(p));
				}
				voPage.setFieldTCount(l, page.getPage(), page.getRecords());
			}
			voPage.setCurpage(page.getPage());
		}
		return voPage;
	}
	
	private static AccountDetailVO convertAccountDetailVO(AccountDetail detail){
		AccountDetailVO vo = new AccountDetailVO();
		vo.setId(detail.getAccountDetailId());
		vo.setType(detail.getUserTypeCn());
		
		String val = "0.00";
		vo.setSurplusamount(((val = NumberUtil.sfwr(detail.getSurplusAmount())) == null) ? "0.00":val);
		
		if (null != detail.getAmount() && detail.getAmount() > 0) {
			vo.setAmount(((val = NumberUtil.sfwr(detail.getAmount())) == null) ? "0.00":"+" + val);
		}else{
			vo.setAmount(((val = NumberUtil.sfwr(detail.getAmount())) == null) ? "0.00":val);
		}
		
		vo.setAccounttime(detail.getAccountTime() == null?"":com.tp.util.DateUtil.format(detail.getAccountTime(), "yyyy-MM-dd HH:mm:ss"));
//		vo.setAccounttype(detail.getAccountTypeCn());
//		vo.setBussinesstype(detail.getBussinessTypeCn());
		vo.setAccounttype( detail.getAccountType().toString() );
		vo.setBussinesstype( detail.getBussinessType().toString() );
		vo.setRefereeName( detail.getRefereeName());
		vo.setRefereeNickName( detail.getRefereeNickName());		
		if( detail.getBussinessType() != null ){						
			vo.setBussinesstypedesc( DssConstant.BUSSINESS_TYPE.getCnName( detail.getBussinessType() ) );						
		}		
		return vo;
	}
	
	
	public static WithdrawDetailVO convertWithdrawDetailVO(WithdrawDetail detail){
		WithdrawDetailVO vo = new WithdrawDetailVO();
		vo.setWithdrawTime( detail.getWithdrawTime() == null?"":com.tp.util.DateUtil.format(detail.getWithdrawTime(), "yyyy-MM-dd HH:mm:ss"));
		vo.setWithdrawBank(detail.getWithdrawBank());
//		vo.setWithdrawBankAccount(detail.getWithdrawBankAccount());			
		vo.setWithdrawAmount(detail.getWithdrawAmount());		
		//vo.setWithdrawStatus(detail.getWithdrawStatus());		
		if( DssConstant.PAYMENT_MODE.ALIPAY.getCnName().equals(detail.getWithdrawBank() )){
			vo.setWithdrawType(DssConstant.PAYMENT_MODE.ALIPAY.code);
			//vo.setWithdrawBankAccount( detail.getWithdrawBankAccount() );			
			String alipay = detail.getWithdrawBankAccount();
			if(StringUtil.isNotBlank(alipay)){
				if( alipay.contains("@") ){
					int index = -1;
					index=alipay.indexOf('@');
					if(index >=3){
						vo.setWithdrawBankAccount(alipay.substring(0, 3) + "***" + alipay.substring(index, alipay.length()));					
					}else{
						vo.setWithdrawBankAccount(alipay.substring(0, index) + "***" + alipay.substring(index, alipay.length()));										
					}
				}else{
					if(alipay.length()>7 ){
						vo.setWithdrawBankAccount(alipay.substring(0, 3) + "****" + alipay.substring(7, alipay.length()));						
					}else if(alipay.length()>3){
						vo.setWithdrawBankAccount(alipay.substring(0, 3)  + "****"  );											
					}else{
						vo.setWithdrawBankAccount(alipay );																
					}
				}			
			}			
		}else{
			vo.setWithdrawType(DssConstant.PAYMENT_MODE.UNPAY.code);
			vo.setWithdrawBankAccount( detail.getWithdrawBankAccount().substring(detail.getWithdrawBankAccount().length()-4, detail.getWithdrawBankAccount().length()) );
		}			
		return vo;
	}
	
	/**订单信息**/
	public static Page<PromoterOrderVO> convertPagePromoterOrderVO(PageInfo<OrderList4UserDTO> page, Integer promoterType){
		Page<PromoterOrderVO> voPage = new Page<PromoterOrderVO>();
		if (null != page) {
			List<PromoterOrderVO> l = new ArrayList<>();
			List<OrderList4UserDTO> rows = page.getRows();
			if (CollectionUtils.isNotEmpty(rows)) {
				for (OrderList4UserDTO dto : rows) {
					PromoterOrderVO vo = convertOrderVO(dto, promoterType);
					if (null != vo) {
						l.add(vo);
					}
				}
				voPage.setFieldTCount(l, page.getPage(), page.getRecords());
			}
			voPage.setCurpage(page.getPage());
		}
		return voPage;
	}
	
	private static PromoterOrderVO convertOrderVO(OrderList4UserDTO od, Integer promoterType){
		PromoterOrderVO vo = null;
		SubOrder order = od.getSubOrder();
		if(null != order){
			vo = new PromoterOrderVO();
			vo.setOrdercode(order.getOrderCode() == null ? "":order.getOrderCode().toString());
			vo.setOrdercount(order.getQuantity() == null ? "":order.getQuantity().toString());
			vo.setOrderprice(NumberUtil.sfwr(order.getPayTotal()));
			vo.setOrdertime(DateUtil.formatDateTime(order.getCreateTime()));
			vo.setStatus(order.getOrderStatus() == null ? "":order.getOrderStatus().toString());
			vo.setStatusdesc(order.getStatusStr());
			List<OrderItem> orderItemList = od.getOrderItemList();
			List<OrderLineTO> lines = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(orderItemList)){
				for(OrderItem oi : orderItemList){
					lines.add(OrderConvert.convertOrderLine(oi, order.getOrderStatus()));
				}
				vo.setLines(lines);
			}
			vo.setLinecount(com.tp.m.util.StringUtil.getStrByObj(lines.size()));
//			if(0 == promoterType)
//				vo.setCommision(NumberUtil.sfwr(od.getSubOrder().getCommisionAmoutCoupon()));
//			else 
//				vo.setCommision(NumberUtil.sfwr(od.getSubOrder().getCommisionAmoutShop()));					
			if( DssConstant.PROMOTER_TYPE.COUPON.code.equals(promoterType) )
				vo.setCommision(NumberUtil.sfwr(od.getSubOrder().getCommisionAmoutCoupon()));
			else if(DssConstant.PROMOTER_TYPE.DISTRIBUTE.code.equals(promoterType) )
				vo.setCommision(NumberUtil.sfwr(od.getSubOrder().getCommisionAmoutShop()));					
			else if( DssConstant.PROMOTER_TYPE.SCANATTENTION.code.equals(promoterType))
				vo.setCommision(NumberUtil.sfwr(od.getSubOrder().getCommisionAmoutScan()));								
		}
		return vo;
	}
	
	
	public static List<TopicInfoVO> convertTopicInfoList2TopicInfoVOList(List<TopicInfo> listTopic, Integer topicType){
		List<TopicInfoVO> listVO = new ArrayList<TopicInfoVO>();
		for(TopicInfo topic : listTopic ){
			TopicInfoVO vo = new TopicInfoVO();			
			if(topicType.equals(TopicConstant.TOPIC_TYPE_TOPIC)){
				vo.setTopicid(topic.getId());
				vo.setName(topic.getName());
				vo.setType(topic.getType());
			}else if(topicType.equals(TopicConstant.TOPIC_TYPE_BRAND) ){
				vo.setTopicid(topic.getId());
				vo.setName(topic.getName());
				vo.setType(topic.getType());
				vo.setImage( ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.cmsimg.url, ImgHelper.getImgUrl(topic.getImage(), ImgEnum.Width.WIDTH_750)) );					
				vo.setOnshelves(topic.getOnShelves());	
			}			
			listVO.add(vo);
		}
		return listVO;
	}

	
	public static List<TopicInfoVO> convertTopicList2TopicInfoVOList(List<Topic> listTopic){
		List<TopicInfoVO> listVO = new ArrayList<TopicInfoVO>();
		for(Topic topic : listTopic ){
			TopicInfoVO vo = new TopicInfoVO();
			vo.setTopicid(topic.getId());
			vo.setName(topic.getName());
			vo.setType(topic.getType());
			listVO.add(vo);
		}
		return listVO;
	}

	
//	/** 主题ID */
//	private  Long topicId;			
//	/** 商品名称 */
//	private String name;			
//	/** 商品 */
//	private String sku;			
//	/** 销售价格 */	
//	private Double salePrice;			
//	/** 活动价格 */
//	private Double topicPrice;
//	/** 商品上下架情况 */
//	private Integer onShelves;			
//	/** 商品活动图片 */
//	private String imgUrl;			
//	/** 佣金 */
//	private Double commission;
	
	public static List<PromoterTopicItemVO> convertListPromoterTopicItemVO(List<PromoterTopicItemDTO> listItems){
		List<PromoterTopicItemVO> listVO = new ArrayList<PromoterTopicItemVO>();
		for(PromoterTopicItemDTO topic : listItems ){
			PromoterTopicItemVO vo = new PromoterTopicItemVO();

		
			listVO.add(vo);
		}
		return listVO;
	}
	
}

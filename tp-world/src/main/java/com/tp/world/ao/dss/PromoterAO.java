/**
 * 
 */
package com.tp.world.ao.dss;
import static com.tp.util.BigDecimalUtil.multiply;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tp.common.util.ImageDownUtil;
import com.tp.common.util.mem.PasswordHelper;
import com.tp.common.vo.*;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DssConstant.BUSSINESS_TYPE;
import com.tp.common.vo.DssConstant.PROMOTER_TYPE;
import com.tp.common.vo.cms.AppTaiHaoTempletConstant;
import com.tp.common.vo.cms.ElementEnum;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mmp.enums.DeletionStatus;
import com.tp.dto.mmp.enums.SalesPartten;
import com.tp.dto.ord.remote.OrderList4UserDTO;
import com.tp.dto.promoter.PromoterTopicItemDTO;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.enums.ImgEnum;
import com.tp.m.enums.MResultInfo;
import com.tp.m.exception.MobileException;
import com.tp.m.query.dss.QueryPromoterTopic;
import com.tp.m.query.promoter.QueryPromoter;
import com.tp.m.util.NumberUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.promoter.AccountDetailVO;
import com.tp.m.vo.promoter.DssLoginVO;
import com.tp.m.vo.promoter.PromoterInfoVO;
import com.tp.m.vo.promoter.PromoterOrderVO;
import com.tp.m.vo.promoter.PromoterTopicItemVO;
import com.tp.m.vo.promoter.ShelvesVO;
import com.tp.m.vo.promoter.TopicInfoVO;
import com.tp.m.vo.promoter.WithdrawDetailVO;
import com.tp.model.dss.AccountDetail;
import com.tp.model.dss.GlobalCommision;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.PromoterTopic;
import com.tp.model.dss.RefereeDetail;
import com.tp.model.dss.WithdrawDetail;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mmp.Topic;
import com.tp.model.mmp.TopicInfo;
import com.tp.model.mmp.TopicItem;
import com.tp.model.pay.PaymentInfo;
import com.tp.proxy.dss.AccountDetailProxy;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.proxy.dss.PromoterTopicProxy;
import com.tp.proxy.dss.RefereeDetailProxy;
import com.tp.proxy.dss.WithdrawDetailProxy;
import com.tp.proxy.mem.MemberInfoProxy;
import com.tp.proxy.mkt.ChannelPromoteProxy;
import com.tp.proxy.mmp.CouponProxy;
import com.tp.proxy.mmp.TopicItemProxy;
import com.tp.proxy.mmp.TopicProxy;
import com.tp.proxy.ord.OrderInfoProxy;
import com.tp.proxy.ord.SubOrderProxy;
import com.tp.proxy.pay.PaymentInfoProxy;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.dss.IGlobalCommisionService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.util.BigDecimalUtil;
import com.tp.util.DateUtil;
import com.tp.util.ErWeiMaUtil;
import com.tp.world.convert.PromoterConvert;
import com.tp.world.helper.ImgHelper;
import com.tp.world.helper.PropertiesHelper;

import sun.misc.BASE64Encoder;
/**
 * @author Administrator
 *	卡券推广员业务层
 */
@Service
public class PromoterAO {

	private static final Logger log = LoggerFactory.getLogger(PromoterAO.class);
	private static String SHOP_TOKEN_PREFIX = "dss_shop_";
	private static String COUPON_TOKEN_PREFIX = "dss_coupon_";
	private static String SCAN_TOKEN_PREFIX = "dss_scan_";	
	private static Integer TOKEN_LIVE = 365*86400;
	@Autowired
	private PromoterInfoProxy promoterInfoProxy;

	@Autowired
	private PromoterTopicProxy promoterTopicProxy;

	@Autowired
	private WithdrawDetailProxy withdrawDetailProxy;
	
	@Autowired
	private AccountDetailProxy accountDetailProxy;
	
	@Autowired
	private OrderInfoProxy orderInfoProxy;
	
	@Autowired
	private CouponProxy couponProxy;
	
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	
	@Autowired
	private SubOrderProxy subOrderProxy;

	
	@Autowired 
	private RefereeDetailProxy refereeDetailProxy;
	
		
	@Autowired
	private IGlobalCommisionService globalCommisionService;
	
	@Autowired
	private TopicItemProxy topicItemProxy;
	
	@Autowired
	private IPromoterInfoService promoterInfoService;
	
	@Autowired
	private TopicProxy topicProxy;
	
	@Autowired
	private MemberInfoProxy memberInfoProxy;
	
	@Autowired
	private ChannelPromoteProxy channelPromoteProxy;
	
	@Autowired
	PropertiesHelper propertiesHelper;


	@Autowired
	private PaymentInfoProxy paymentInfoProxy;

	/**logo存放路径*/
    public String  logoImagePath="/shareImg/logo.png";
	/**
	 * 设置上下架专题或商品
	 */
	@Transactional
	public MResultVO<ShelvesVO> setPromoterTopic(QueryPromoterTopic queryTopic){
		try {
			ResultInfo<PromoterInfo> resultPromoterInfo = promoterInfoProxy.queryById(queryTopic.getPromoterid());
			if (!resultPromoterInfo.isSuccess()) {
				return new MResultVO<>(MResultInfo.FAILED);
			}
			
			// 检测活动专题
			if( !existTopicInfo(queryTopic.getPromoterid(), queryTopic.getTopictype(), queryTopic.getTopicid()) ){
				return new MResultVO<>(MResultInfo.NOT_HAVE_TOPIC);				
			}
			
			// 检测活动专题商品
			if( queryTopic.getShelftype().equals( DssConstant.PROMOTERTOPIC_TYPE.TOPICITEM.code ) ){
				TopicItem qitem = new TopicItem();
				qitem.setTopicId(queryTopic.getTopicid());
				qitem.setSku(queryTopic.getSku());
				qitem.setDeletion( DeletionStatus.NORMAL.ordinal() );
				ResultInfo<List<TopicItem>> reltList = topicItemProxy.queryByObject(qitem);
				if( !reltList.isSuccess()  || reltList.getData().isEmpty() ){
					return new MResultVO<>(MResultInfo.NOT_HAVE_TOPICITEM);					
				}								
			}
			
			// 主题团不能进行上下架
			if(queryTopic.getShelftype().equals( DssConstant.PROMOTERTOPIC_TYPE.TOPIC.code ) 
					&& queryTopic.getTopictype().equals(TopicConstant.TOPIC_TYPE_TOPIC)){
				return new MResultVO<>(MResultInfo.TOPIC_CAN_NOT_SET_ON_SHELF);									
			}
						
			PromoterTopic pTopic = new PromoterTopic();
			if( queryTopic.getShelftype().equals( DssConstant.PROMOTERTOPIC_TYPE.TOPIC.code) ){	// 专题
				pTopic.setType(DssConstant.PROMOTERTOPIC_TYPE.TOPIC.code);								
			}
			else if( queryTopic.getShelftype().equals( DssConstant.PROMOTERTOPIC_TYPE.TOPICITEM.code)  ){		// 专题商品
				pTopic.setSku(queryTopic.getSku() );			
				pTopic.setType(DssConstant.PROMOTERTOPIC_TYPE.TOPICITEM.code);
			}								
			pTopic.setPromoterId(queryTopic.getPromoterid());
			pTopic.setTopicId(queryTopic.getTopicid());
			ResultInfo<List<PromoterTopic>> reltTopic  = promoterTopicProxy.queryByObject( pTopic );			
			if( !reltTopic.isSuccess()){
				return new MResultVO<>(MResultInfo.FAILED);				
			}
			
			if( reltTopic.getData().isEmpty() ){
//				if( queryTopic.getOnshelf().equals( DssConstant.PROMOTERTOPIC_STATUS.OFFSHELF.code )  ){	// 下架， 新增加一条记录
//					pTopic.setStatus(DssConstant.PROMOTERTOPIC_STATUS.OFFSHELF.code);
//					promoterTopicProxy.insert(pTopic);
//				}			
				pTopic.setStatus(queryTopic.getOnshelf() );
				promoterTopicProxy.insert(pTopic);
			}
			else{
				PromoterTopic oldTopic = reltTopic.getData().get(0);				
				oldTopic.setStatus( queryTopic.getOnshelf() );
				promoterTopicProxy.updateNotNullById(oldTopic);						
			}
						
			ShelvesVO vo = new ShelvesVO();
			vo.setShelvestype(queryTopic.getShelftype() );
			vo.setTopicid(queryTopic.getTopicid());
			vo.setSku(queryTopic.getSku());
			vo.setOnshelves(queryTopic.getOnshelf());
									
			return new MResultVO<ShelvesVO>(MResultInfo.SUCCESS, vo);	
		} catch (MobileException e) {
			log.error("[API接口 - 设置上下架商品  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		} catch (Exception e) {
			log.error("[API接口 - 设置上下架商品  Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}				
	}
	

	
	private boolean existTopicInfo(Long promoterid, Integer topictype, Long topicid){
		List<TopicInfo> listchktop = selectTopicsAO( promoterid,  topictype, topicid);
		if(listchktop==null || listchktop.isEmpty() ){
			return false;
		}
		return true;		
	}


	/**
	 * 查询当前活动主题商品
	 **/
	public MResultVO< Page<PromoterTopicItemVO> > getTopicItems(PromoterTopic pTopic){		
		try {
			Page<PromoterTopicItemVO> pages = new Page<>();
			ResultInfo<PromoterInfo> resultPromoterInfo = promoterInfoProxy.queryById(pTopic.getPromoterId());  
			if (!resultPromoterInfo.isSuccess()) {
				return new MResultVO<>(MResultInfo.FAILED);
			}				
			
			// 检测活动专题
			if( !existTopicInfo(pTopic.getPromoterId(),  pTopic.getTopicType(), pTopic.getTopicId()) ){
				return new MResultVO<>(MResultInfo.NOT_HAVE_TOPIC);				
			}

			// 如果没有传topicId，读取所有topicId
			if(pTopic.getTopicId() == null){
				List<TopicInfo> topiclist = selectTopicsAO(pTopic.getPromoterId(), pTopic.getTopicType(), null);				
				for(TopicInfo tmp : topiclist){
					pTopic.getListTopic().add(tmp.getId());
				}
			}else{
				pTopic.getListTopic().add(pTopic.getTopicId());
			}
			
			List<PromoterTopicItemDTO> listTopicItems = promoterInfoProxy.selectTopicItems(pTopic);	
			
			GlobalCommision globalCommision = globalCommisionService.queryLastGlobalCommision();
			List<PromoterTopicItemVO> listVO = new  ArrayList<PromoterTopicItemVO>();
			for(PromoterTopicItemDTO dto : listTopicItems){
				Double rate = globalCommision.getCurrentCommisionRate(resultPromoterInfo.getData(), dto.getCommissionRate());								
				PromoterTopicItemVO vo = new PromoterTopicItemVO();
				vo.setTopicid(dto.getTopicId());
				vo.setName(dto.getName());
				vo.setSku(dto.getSku());
				vo.setSaleprice(dto.getSalePrice());
				vo.setTopicprice(dto.getTopicPrice());
				vo.setOnshelves(dto.getOnShelves());
				vo.setCommission( BigDecimalUtil.toPrice(multiply( dto.getTopicPrice(), rate)) );								
				vo.setImgurl( ImageDownUtil.getOriginalImage(Constant.IMAGE_URL_TYPE.item.url, ImgHelper.getImgUrl(dto.getImgUrl(), ImgEnum.Width.WIDTH_360))       );
				
				if( vo.getTopicid() != null  && vo.getSku()!=null ){
					String shareUrl = propertiesHelper.shareProductUrl.trim().replace("TID", vo.getTopicid().toString()).replace("SKU", vo.getSku());
					shareUrl = shareUrl + "&shop="+resultPromoterInfo.getData().getMobile().trim();
					vo.setShareurl(shareUrl);	
				}
				
				listVO.add(vo);				
			}			
			Long total = promoterInfoProxy.countTopicItems(pTopic);			
			pages.setFieldTCount(listVO,pTopic.getStartPage(), total.intValue() );
			return new MResultVO<>(MResultInfo.SUCCESS,   pages); 
		} catch (MobileException e) {
			log.error("[API接口 - 查询当前活动主题商品  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 查询当前活动主题商品  Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	
	/**
	 * 查询当前活动专题
	 **/
	public MResultVO< List<TopicInfoVO> > getTopics(QueryPromoter promoter){
		
		try {
			ResultInfo<PromoterInfo> resultPromoterInfo = promoterInfoProxy.queryById(promoter.getPromoterid());  
			if (!resultPromoterInfo.isSuccess()) {
				return new MResultVO<>(MResultInfo.FAILED);
			}							
			List<TopicInfo> topicinfolist = selectTopicsAO(promoter.getPromoterid(), promoter.getTopictype(), null);						
			if(topicinfolist==null){
				throw new MobileException( MResultInfo.QUERY_TOPIC_FAILED) ;
			}
			if(topicinfolist.isEmpty()){
				throw new MobileException( MResultInfo.NOT_HAVE_TOPIC) ;					
			}
			
			
			List<TopicInfoVO> topicvolist = PromoterConvert.convertTopicInfoList2TopicInfoVOList( topicinfolist, promoter.getTopictype());
			// 分享地址
			for(TopicInfoVO topicvo : topicvolist){
				if( topicvo.getTopicid() != null  && topicvo.getType()!=null && topicvo.getType().equals(TopicConstant.TOPIC_TYPE_BRAND) ){
					String shareUrl = propertiesHelper.shareTopicUrl.trim().replace("TID", topicvo.getTopicid().toString() );
					shareUrl = shareUrl + "&shop="+resultPromoterInfo.getData().getMobile().trim();
					topicvo.setShareurl(shareUrl);	
				}
			}
			return new MResultVO<>(MResultInfo.SUCCESS,   topicvolist ); 								

		} catch (MobileException e) {
			log.error("[API接口 - 查询当前活动主题  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 查询当前活动主题  Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	
	/**
	 * 查询活动标题
	 **/
	public MResultVO< List<TopicInfoVO> > getTitles(QueryPromoter promoter){		
		try {			
			List<TopicInfoVO> retlist = new ArrayList<TopicInfoVO>();
			// 全部标题
			TopicInfoVO vo1 = new TopicInfoVO();
			vo1.setName("全部");
			vo1.setType( TopicConstant.TOPIC_TYPE_TOPIC );
			vo1.setCallmode(DssConstant.PROMOTERTOPIC_CALLMODE.CALLMODE_TOPICITEM.code);
			retlist.add(vo1);			
			
			ResultInfo<PromoterInfo> resultPromoterInfo = promoterInfoProxy.queryById(promoter.getPromoterid());  
			if (!resultPromoterInfo.isSuccess()) {
				return new MResultVO<>(MResultInfo.FAILED);
			}							
			List<TopicInfo> topicinfolist = selectTopicsAO(promoter.getPromoterid(),  TopicConstant.TOPIC_TYPE_TOPIC  , null);						
			if(topicinfolist==null){
				throw new MobileException( MResultInfo.QUERY_TOPIC_FAILED) ;
			}
			if(topicinfolist.isEmpty()){
				throw new MobileException( MResultInfo.NOT_HAVE_TOPIC) ;					
			}						
			List<TopicInfoVO> tmpList = PromoterConvert.convertTopicInfoList2TopicInfoVOList( topicinfolist, TopicConstant.TOPIC_TYPE_TOPIC);		
			for(TopicInfoVO info : tmpList){
				info.setCallmode( DssConstant.PROMOTERTOPIC_CALLMODE.CALLMODE_TOPICITEM.code );
			}
			retlist.addAll(tmpList);

			// 品牌团标题
			TopicInfoVO vo2 = new TopicInfoVO();
			vo2.setName("品牌团");
			vo2.setType( TopicConstant.TOPIC_TYPE_BRAND );
			vo2.setCallmode(DssConstant.PROMOTERTOPIC_CALLMODE.CALLMODE_TOPIC.code);
			retlist.add(vo2);			
			return new MResultVO<>(MResultInfo.SUCCESS,   retlist); 								

		} catch (MobileException e) {
			log.error("[API接口 - 查询当前活动标题  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 查询当前活动标题  Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	
	private List<TopicInfo> selectTopicsAO(Long promoterid, Integer topicType, Long topicid){		
		try {
			List<String> list = new ArrayList<String>();
			list.add(AppTaiHaoTempletConstant.HAITAO_APP_DSS_TOPICLIST);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("templecodelist", list);
			params.put("elementype", ElementEnum.ACTIVITY.getValue());
			params.put("promoterid", promoterid);
			params.put("topictype", topicType);
			if (topicid != null) {
				params.put("topicid", topicid);
			}
			return promoterInfoProxy.selectTopics(params);
		} catch (MobileException e) {
			log.error("[API接口 private - 查询当前活动主题  MobileException]={}", e.getMessage());
			return null;
		} catch (Exception e) {
			log.error("[API接口 private - 查询当前活动主题  Exception]={}", e);
			return null;
		}
	}
	
	
	
	private List<TopicInfoVO> selectTopicsAO2(Long promoterid, Integer topicType, Long topicid){		
		try {
			Topic topic = new Topic();
			topic.setDeletion( TopicConstant.TOPIC_DELETION_NORMAL);
			topic.setSalesPartten(SalesPartten.DISTRIBUTION.getValue());
			topic.setProgress(TopicConstant.TOPIC_PROCESS_PROCESSING);
			topic.setStatus(TopicConstant.TOPIC_STATUS_AUDITED);
			topic.setType(topicType);			
			if(topicType.equals(TopicConstant.TOPIC_TYPE_TOPIC)){
				ResultInfo<List<Topic>> reltlist = topicProxy.queryByObject(topic);	
				if(reltlist==null ||!reltlist.isSuccess()){
					throw new MobileException( MResultInfo.QUERY_TOPIC_FAILED) ;
				}
				if(reltlist.getData()==null || reltlist.getData().isEmpty()){
					throw new MobileException( MResultInfo.NOT_HAVE_TOPIC) ;					
				}				
				return PromoterConvert.convertTopicList2TopicInfoVOList( reltlist.getData() );				
				
			}else if(topicType.equals(TopicConstant.TOPIC_TYPE_BRAND) ){
			
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("promoterid", promoterid);
				if (topicid != null) {
					params.put("topicid", topicid);
				}
				List<TopicInfo> topicinfolist = promoterInfoProxy.selectBrandTopics(params);
				if(topicinfolist == null || topicinfolist.isEmpty()){
					throw new MobileException( MResultInfo.NOT_HAVE_TOPIC) ;						
				}				
				return PromoterConvert.convertTopicInfoList2TopicInfoVOList(topicinfolist, topicType);
			}else{
				throw new MobileException( MResultInfo.WRONG_TOPIC_TYPE) ;					
			}
		
		} catch (MobileException e) {
			log.error("[API接口 private - 查询当前活动主题  MobileException]={}", e.getMessage());
			return null;
		} catch (Exception e) {
			log.error("[API接口 private - 查询当前活动主题  Exception]={}", e);
			return null;
		}
	}
	
	/**
	 * 查询推广员账户信息 
	 **/
	public MResultVO<PromoterInfoVO> getPromoterInfo(QueryPromoter promoter){
		try {
			ResultInfo<PromoterInfo> resultPromoterInfo = promoterInfoProxy.queryById(promoter.getPromoterid());  
			if (!resultPromoterInfo.isSuccess()) {
				return new MResultVO<>(MResultInfo.FAILED);
			}	
			PromoterInfoVO vo = PromoterConvert.convertPromoterInfoVO(resultPromoterInfo.getData());


			Map<String,Object> param = new HashMap<>();
			param.put("bizType",PaymentConstant.BIZ_TYPE.DSS.code);
			param.put("createUser",resultPromoterInfo.getData().getMemberId());
			param.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name()," create_time desc");
			ResultInfo<List<PaymentInfo>> paymentInfoResult = paymentInfoProxy.queryByParamNotEmpty(param);
			if(paymentInfoResult.isSuccess() && !org.springframework.util.CollectionUtils.isEmpty(paymentInfoResult.getData())){
				vo.setPaycode(paymentInfoResult.getData().get(0).getPaymentTradeNo());
				vo.setPayamount(String.valueOf(paymentInfoResult.getData().get(0).getAmount()));
			}


			// 已提现金额
			AccountDetail detail = new AccountDetail();
			detail.setUserId(promoter.getPromoterid());
			Double withdrawFees = accountDetailProxy.GetWithdrawedfees(detail);
			
			if(withdrawFees != null ){
				if(withdrawFees < 0){
					withdrawFees = withdrawFees * (-1.00);
				}				
				vo.setWithdrawedfees(withdrawFees.toString());
			}else{
				vo.setWithdrawedfees("0.0");				
			}
			// 店铺总销售额
//			Double total = subOrderProxy.GetShopTotalSales(promoter.getPromoterid());
			Double total = subOrderProxy.GetShopTotalSales_coupons_shop_scan(promoter.getPromoterid(), Integer.valueOf(promoter.getType()) );
			if(total != null){
				vo.setOrderamount(total.toString());
			}			

			//提现状态
			if (1 == resultPromoterInfo.getData().getPromoterStatus()) { //已开通
				ResultInfo<List<WithdrawDetail>> result = 
						withdrawDetailProxy.queryWithdrawStatus(promoter.getPromoterid());
				if (!result.isSuccess()) {
					return new MResultVO<>(MResultInfo.FAILED);
				}
				if (null == result.getData() || result.getData().size() <= 0) {
					if( vo.getSurplusamount()==null || "0.00".equals(vo.getSurplusamount() ) ){
						vo.setWithdrawstatus(2);	// 不可提现												
					}else{
						vo.setWithdrawstatus(0);	//可提现						
					}					
				}else{
					vo.setWithdrawstatus(1);	//不允许提现,提现中
					vo.setWithdrawingamount(NumberUtil.sfwr(result.getData().get(0).getWithdrawAmount()));
				}
			}else{
				vo.setWithdrawstatus(0);		//可提现
			}	
			if ("0".equals(promoter.getType())) { //卡券推广员
				ResultInfo<Map<String, Integer>> resultcoupon = 						
						couponProxy.queryPromoterCouponCount(promoter.getPromoterid());
				if (resultcoupon.isSuccess() && null != resultcoupon.getData()) {
					vo.setCouponcount(resultcoupon.getData().get("totalCount"));
					vo.setCpusedcount(resultcoupon.getData().get("usedCount"));
					vo.setCpexchangedcount(resultcoupon.getData().get("exchangedCount"));
					vo.setCpunexchangecount(resultcoupon.getData().get("unexchangeCount"));
				}
			}
			if("1".equals(promoter.getType())){ // 店铺	
				vo.setImg( getImageData(propertiesHelper.shareErweimaUrl + resultPromoterInfo.getData().getMobile() ) );
				log.info("[API接口 -获取分销人员店铺网址 ] = {}", propertiesHelper.shareErweimaUrl + resultPromoterInfo.getData().getMobile() );
			}
			if ("2".equals(promoter.getType())) { //扫码推广员
//				MemberInfo mem = new MemberInfo();
//				mem.setScanPromoterId( promoter.getPromoterid() );
//				ResultInfo<Integer> resInt = memberInfoProxy.queryByObjectCount(mem);
				Map<String, Object> params = new HashMap<String, Object>();
		        params.put("scan_promoter_id",  promoter.getPromoterid());
		       // params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), "mobile is not null and LENGTH(trim(mobile))>=1");
				ResultInfo<Integer> resInt = memberInfoProxy.queryByParamNotEmptyCount(params);
				if( resInt.isSuccess() && resInt.getData() != null){
					vo.setScancount(resInt.getData() );
				}else{
					vo.setScancount( 0 );
				}
				// 获取扫码图片
//				ChannelPromote cp = new ChannelPromote();
//				cp.setChannel(resultPromoterInfo.getData().getMobile());
//				cp.setUniqueId("");
//				cp = channelPromoteProxy.queryUniqueByObject(cp).getData();
//				if(cp != null){
//					vo.setImgurl( cp.getQrcode() );													
//				}
			}
			vo.setPromoterShareImagePath(resultPromoterInfo.getData().getPromoterShareImagePath());			
			ResultInfo<Integer> reltTotalCustCount = promoterInfoProxy.getTotalCustomersForPromoterByQO( PromoterConvert.convertSubOrderQuery(promoter));
			if(reltTotalCustCount.isSuccess()){
				vo.setTotalCustomers(reltTotalCustCount.getData());
			}
			
			return new MResultVO<>(MResultInfo.SUCCESS, vo); 								
		} catch (MobileException e) {
			log.error("[API接口 - 推广员账户信息  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 推广员账户信息  Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	
	private String getImageData(String codeUrl) {
		if(codeUrl == null)
			return null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ErWeiMaUtil.encoderQRCodeWidthLogo(codeUrl, output,logoImagePath);
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(output.toByteArray());
	}

	
	/**
	 * 查询推广员账户流水
	 **/
	public MResultVO<Page<AccountDetailVO>> getBillinfo(QueryPromoter promoter){
		try {
			AccountDetail query = new AccountDetail();
			query.setUserId(promoter.getPromoterid());
			ResultInfo<PageInfo<AccountDetail>> result = accountDetailProxy.queryPageByObject(query, 
					new PageInfo<AccountDetail>(Integer.valueOf(promoter.getCurpage()), promoter.getPagesize()));
			if (result.isSuccess()) {
				fillPromoterInAccountDetail(result.getData());				
				return new MResultVO<>(MResultInfo.SUCCESS, PromoterConvert.convertPageAccountDetail(result.getData()));
			}
			log.error("[调用Proxy接口 - 获取账户流水(findOrderList4UserPage) FAILED] ={}",result.getMsg().toString());
			return new MResultVO<>(MResultInfo.FAILED);
		}catch (MobileException e) {
			log.error("[API接口 - 推广员账户流水  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 推广员账户流水   Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	
	/**在AccountDetail里填充nickname和prmotername**/
	public void fillPromoterInAccountDetail(PageInfo<AccountDetail> page){
		Page<AccountDetailVO> voPage = new Page<AccountDetailVO>();
		if (null != page) {
			List<AccountDetail> rows = page.getRows();
			List<Long> detailCodeList = new ArrayList<Long>();
			if (CollectionUtils.isNotEmpty(rows)) {
				for (AccountDetail accountDetail : rows) {
					if(accountDetail.getBussinessType().equals( BUSSINESS_TYPE.REFERRAL_FEES.code) ){
						detailCodeList.add(accountDetail.getBussinessCode());						
					}
				}
			}			
			List<RefereeDetail> refereeDetailList = new ArrayList<>();
			if( !detailCodeList.isEmpty() ){
				refereeDetailList = refereeDetailProxy.queryRefereeByDetailCode(detailCodeList);
			}
			
			if( !refereeDetailList.isEmpty()){
				for (AccountDetail accountDetail : rows) {
					for(RefereeDetail refereeDetail : refereeDetailList){
						if(accountDetail.getBussinessCode().equals( refereeDetail.getRefereeDetailCode() )){
							accountDetail.setRefereeNickName( refereeDetail.getNickName());
							accountDetail.setRefereeName(refereeDetail.getPromoterName());
						}
					}
				}
			}						
		}		
	}	
	
	/**
	 * 分销员查询 
	 */
	public MResultVO<Page<PromoterInfoVO>> queryDealers(QueryPromoter promoter){
		Map<String,Object> params = new HashMap<String,Object>();
		try {
			ResultInfo<PageInfo<PromoterInfo>> result;
			if(promoter.getLevel()==null || !DssConstant.INDIRECT_TYPE.GRANDSON.code.equals(Integer.valueOf(promoter.getLevel()) )){
				params.put("parentPromoterId", promoter.getPromoterid());
				result= promoterInfoProxy.queryPageByParamAndParent(params, new PageInfo<PromoterInfo>(Integer.valueOf(promoter.getCurpage()), promoter.getPagesize()));
			}else{			
				if(  promoter.getSonpromoterid() != null ){
					PromoterInfo sonPromoter = promoterInfoService.queryById(promoter.getSonpromoterid());
					if( !promoter.getPromoterid().equals( sonPromoter.getParentPromoterId() ) ){
						return new MResultVO<>(MResultInfo.NO_SONPROMOTER);						
					}
					params.put("parentPromoterId", promoter.getSonpromoterid());
					result= promoterInfoProxy.queryPageByParamAndTop(params, new PageInfo<PromoterInfo>(Integer.valueOf(promoter.getCurpage()), promoter.getPagesize()));
				}else{
					return new MResultVO<>(MResultInfo.SONPROMOTER_ID_IS_NULL);					
				}
			}
			if (result.isSuccess()) {
				return new MResultVO<Page<PromoterInfoVO>>(MResultInfo.SUCCESS, PromoterConvert.convertPageDealers(result.getData()));
			}
			log.error("[调用Proxy接口 - 分销员查询 FAILED] ={}",result.getMsg().toString());
			return new MResultVO<>(MResultInfo.FAILED);
		}catch (MobileException e) {
			log.error("[API接口 - 分销员查询  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 分销员查询   Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 查询推广员订单列表
	 **/
	public MResultVO<Page<PromoterOrderVO>> queryOrders(QueryPromoter promoter){
		try {
			ResultInfo<PageInfo<OrderList4UserDTO>> orders = orderInfoProxy.queryOrderPageByPromoter(PromoterConvert.convertPageOrderQuery(promoter));
			if(orders.isSuccess()){
				return new MResultVO<>(MResultInfo.SUCCESS,PromoterConvert.convertPagePromoterOrderVO(orders.getData(), Integer.valueOf(promoter.getType())));
			}
			log.error("[调用Service接口 - 获取订单列表(findOrderList4UserPage) FAILED] ={}",orders.getMsg().toString());
			return new MResultVO<>(orders.getMsg().getMessage());
		}catch (MobileException e) {
			log.error("[API接口 - 查询推广员卡券列表信息 MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 查询推广员卡券列表信息  Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}


	
	
	
	
	
	/**
	 * 提现请求 
	 */
	public MResultVO<MResultInfo> withdrawApply(QueryPromoter promoter){
		ResultInfo<PromoterInfo> resultPromoterInfo = promoterInfoProxy.queryById(promoter.getPromoterid());  
		if (!resultPromoterInfo.isSuccess()) {
			return new MResultVO<>(MResultInfo.FAILED);
		}
		if (resultPromoterInfo.getData().getSurplusAmount() > 0) {
			ResultInfo<WithdrawDetail> applyResult = 
					withdrawDetailProxy.applyByPromoter(promoter.getPromoterid(), resultPromoterInfo.getData().getSurplusAmount(), DssConstant.PAYMENT_MODE.UNPAY.code);
			if (!applyResult.isSuccess()) {
				return new MResultVO<>(MResultInfo.FAILED);
			}
		}
		return new MResultVO<>(MResultInfo.SUCCESS);
	}
	
	/**
	 * 提现请求2
	 */
	public MResultVO<MResultInfo> withdrawApply2(QueryPromoter promoter){
		PromoterInfo qurProm = new PromoterInfo();
		
		if( promoter.getPromoterid() == null ){
			return new MResultVO<>(MResultInfo.PROMOTER_ID_IS_NULL);
		}
		if( StringUtil.isBlank(promoter.getPromoterName()) ){
			return new MResultVO<>(MResultInfo.PROMOTERNAME_IS_BLANK);			
		}
		if(promoter.getCredentialType() == null){
			return new MResultVO<>(MResultInfo.CREDENTIALTYPE_IS_NULL);						
		}
		if( StringUtil.isBlank( promoter.getCredentialCode()) ){
			return new MResultVO<>(MResultInfo.CREDENTIALCODE_IS_BLANK);									
		}
		
//		if( (StringUtil.isBlank( promoter.getBankAccount()) || StringUtil.isBlank( promoter.getBankName()))
//				&& StringUtil.isBlank(promoter.getAlipay()) ){
//			return new MResultVO<>(MResultInfo.BANK_OR_ALIPAY_IS_BLANK);				
//		}
//		if( StringUtil.isBlank( promoter.getBankName()) ){
//			return new MResultVO<>(MResultInfo.BANKNAME_IS_BLANK);							
//		}			
//		if( StringUtil.isBlank( promoter.getBankAccount()) ){
//			return new MResultVO<>(MResultInfo.BANKACCOUNT_IS_BLANK);
//		}			
		if( promoter.getCurWithdraw()  == null){
			return new MResultVO<>(MResultInfo.CURWITHDRAW_IS_NULL);									
		}	
		if( promoter.getCurWithdraw().equals(0.0d)){
			return new MResultVO<>(MResultInfo.CURWITHDRAW_IS_ZERO);	
		}	
		
//		if( DssConstant.PAYMENT_MODE.UNPAY.code.equals( promoter.getWithdrawType()) && StringUtil.isBlank( promoter.getBankName()) ) {
//			return new MResultVO<>(MResultInfo.BANKNAME_IS_BLANK);				
//		}
		
//		if( DssConstant.PAYMENT_MODE.UNPAY.code.equals( promoter.getWithdrawType()) 
//				&&  DssConstant.PAYMENT_MODE.ALIPAY.getCnName().trim().equals( promoter.getBankName().trim() ) ) {
//			return new MResultVO<>(MResultInfo.BANKNAME_IS_WRONG);				
//		}
		
		qurProm.setPromoterId(promoter.getPromoterid());				
		ResultInfo< List<PromoterInfo> > resultListPromoterInfo = promoterInfoProxy.queryByObject(qurProm);  				
		if (!resultListPromoterInfo.isSuccess() || resultListPromoterInfo.getData()==null || resultListPromoterInfo.getData().isEmpty() ) {
			return new MResultVO<>(MResultInfo.PROMOTER_NOT_EXIST);
		}
		
		Integer paymode = null;
//		if( DssConstant.PAYMENT_MODE.ALIPAY.code.equals( promoter.getWithdrawType() )  ){	// 如果支付宝不为空			
//			promoter.setAlipay( promoter.getBankAccount() );
//			promoter.setBankName(null);
//			promoter.setBankAccount(null);
//			paymode = DssConstant.PAYMENT_MODE.ALIPAY.code;
//		}else{
//			paymode = DssConstant.PAYMENT_MODE.UNPAY.code;			
//		}		
		if( DssConstant.PAYMENT_MODE.UNPAY.code.equals( promoter.getWithdrawType() )  ){	// 银行卡
			paymode = DssConstant.PAYMENT_MODE.UNPAY.code;			
		}else{			
			promoter.setAlipay( promoter.getBankAccount() );
			promoter.setBankName(null);
			promoter.setBankAccount(null);
			paymode = DssConstant.PAYMENT_MODE.ALIPAY.code;
		}		
		
		PromoterInfo promInfo = resultListPromoterInfo.getData().get(0);
		if( StringUtil.isBlank(promInfo.getPromoterName()) 
			|| promInfo.getCredentialType() == null 
			|| StringUtil.isBlank( promInfo.getCredentialCode())
			|| StringUtil.isBlank( promInfo.getBankAccount()) 
			|| StringUtil.isBlank( promInfo.getBankName()) 
			|| StringUtil.isBlank( promInfo.getAlipay()) ){
			updatePromoter(promoter);	 				
		}
													
		if( promoter.getCurWithdraw() > promInfo.getSurplusAmount() )	{
			// 账户余额不足
			return new MResultVO<>(MResultInfo.NOT_ENOUGH_SURPLUS);
		}
		
		if (promInfo.getSurplusAmount() > 0 &&  promoter.getCurWithdraw() > 0) {
			ResultInfo<WithdrawDetail> applyResult = 
					withdrawDetailProxy.applyByPromoter(promoter.getPromoterid(), promoter.getCurWithdraw(), paymode);
			if (!applyResult.isSuccess()) {
				return new MResultVO<>(MResultInfo.FAILED.code, applyResult.getMsg().getDetailMessage() );
			}
		}else{
			return new MResultVO<>(MResultInfo.FAILED);			
		}
		return new MResultVO<>(MResultInfo.SUCCESS);
	}

	
	public MResultVO<WithdrawDetailVO> withdrawDetail(QueryPromoter promoter){
		try {
			ResultInfo<PromoterInfo> resultPromoterInfo = promoterInfoProxy.queryById(promoter.getPromoterid());  
			if (!resultPromoterInfo.isSuccess() || resultPromoterInfo.getData()==null) {
				return new MResultVO<>(MResultInfo.FAILED);
			}										
			ResultInfo<WithdrawDetail> rltDetail = withdrawDetailProxy.getDetailForWithdrawing(promoter.getPromoterid());
			if( !rltDetail.isSuccess() ){
				throw new MobileException(MResultInfo.DO_NOT_HAVE_WITHDRAWING);
			}			
			WithdrawDetailVO detailVO = new WithdrawDetailVO();
			detailVO = PromoterConvert.convertWithdrawDetailVO(rltDetail.getData());
			return new MResultVO<>(MResultInfo.SUCCESS, detailVO); 								
		} catch (MobileException e) {
			log.error("[API接口 - 推广员账户信息  MobileException]={}", e.getMessage());
			return new MResultVO<>(e);
		}catch (Exception e) {
			log.error("[API接口 - 推广员账户信息  Exception]={}", e);
			return new MResultVO<>(MResultInfo.CONN_ERROR);
		}
	}
	
	/**
	 * 登录
	 */
	public MResultVO<DssLoginVO> promoterLogin(Long memberId, String token){
		if (null == memberId || null == token) {
			return new MResultVO<>(MResultInfo.FAILED);
		}
		PromoterInfo promoterInfo = new PromoterInfo();
		promoterInfo.setMemberId(memberId);
		ResultInfo<List<PromoterInfo>> result = promoterInfoProxy.queryByObject(promoterInfo); 
		if (!result.isSuccess()) {
			return new MResultVO<>(MResultInfo.FAILED);
		}
		DssLoginVO vo = new DssLoginVO();
		if (CollectionUtils.isEmpty(result.getData())) {
			return new MResultVO<>(MResultInfo.SUCCESS, vo);
		}		
		for (PromoterInfo p : result.getData()) {
			if (0 == p.getPromoterType()) {				//卡券推广员
				vo.setIscoupondss(1);
				setCache(COUPON_TOKEN_PREFIX + token, p.getPromoterId());
			}else if(1 == p.getPromoterType()){			//店铺推广员
				vo.setIsshopdss(1);
				setCache(SHOP_TOKEN_PREFIX + token, p.getPromoterId());
			}else if(2 == p.getPromoterType()){			//扫码推广员
				vo.setIsscandss(1);
				setCache(SCAN_TOKEN_PREFIX + token, p.getPromoterId());
			}
		}
		return new MResultVO<>(MResultInfo.SUCCESS, vo);
	}
	
	/**验证token**/
	public Long authPromoter(String token, Integer type){
		if(StringUtils.isBlank(token)) {
			log.error("[验证Token error] = {}",MResultInfo.TOKEN_NULL.message);
			throw new MobileException(MResultInfo.NO_PROMOTER);	
		}
		Long promoterId = null;
		switch (type) {
		case 0:		//卡券
			promoterId = (Long)getCache(COUPON_TOKEN_PREFIX + token);
			break;
		case 1:
			promoterId = (Long)getCache(SHOP_TOKEN_PREFIX + token);
			break;
		case 2:
			promoterId = (Long)getCache(SCAN_TOKEN_PREFIX + token);
			break;
		}
		if(null == promoterId){
			log.error("[验证Token error] = {}",MResultInfo.TOKEN_NO_EXIST.message);
			throw new MobileException(MResultInfo.NO_PROMOTER);
		}
		return promoterId;
	}
	
	
	/**
	 * 更新认证信息
	 */
	public MResultVO<MResultInfo> updatePromoter(QueryPromoter promoter){
		ResultInfo<PromoterInfo> resultPromoterInfo = promoterInfoProxy.queryById(promoter.getPromoterid());  
		if (!resultPromoterInfo.isSuccess() || resultPromoterInfo.getData() == null) {
			return new MResultVO<>(MResultInfo.PROMOTER_NOT_EXIST);
		}
		PromoterInfo updatePromoter = resultPromoterInfo.getData();		
		Boolean bUpdate = false;
		if( StringUtil.isNotBlank(promoter.getPromoterName() )	// 更新姓名，身份证号等
			&& ( promoter.getCredentialType() == DssConstant.CARD_TYPE.IDENTITY_CARD.code) 
			&& StringUtil.isNotBlank(promoter.getCredentialCode()) 
			&& (StringUtil.isBlank(updatePromoter.getPromoterName()) || StringUtil.isBlank(updatePromoter.getCredentialCode()))
			){
			bUpdate = true;
			updatePromoter.setPromoterName(promoter.getPromoterName());
			updatePromoter.setCredentialType(promoter.getCredentialType());
			updatePromoter.setCredentialCode(promoter.getCredentialCode());
			Date birthday = DateUtil.parse(updatePromoter.getCredentialCode().substring(6, 14), DateUtil.SHORT_FORMAT);
			updatePromoter.setBirthday(birthday);
		}
		if( StringUtil.isNotBlank(promoter.getBankName()) && StringUtil.isNotBlank(promoter.getBankAccount()) 
				&& (StringUtil.isBlank(updatePromoter.getBankName()) || StringUtil.isBlank(updatePromoter.getBankAccount()))
				){		// 更新银行卡信息
			bUpdate = true;
			updatePromoter.setBankName(promoter.getBankName());
			updatePromoter.setBankAccount(promoter.getBankAccount());
		}
		if( StringUtil.isNotBlank( promoter.getAlipay() ) && StringUtil.isBlank( updatePromoter.getAlipay() ) ){	// 更新支付宝信息
			bUpdate = true;
			updatePromoter.setAlipay(promoter.getAlipay());
		}
		if(StringUtil.isNotBlank( promoter.getQq() ) || StringUtil.isNotBlank(promoter.getWeixin()) || StringUtil.isNotBlank(promoter.getEmail() )){	//更新QQ, 微信，邮箱
			bUpdate = true;
			updatePromoter.setQq( promoter.getQq() );
			updatePromoter.setWeixin( promoter.getWeixin() );
			updatePromoter.setEmail( promoter.getEmail() );			
		}
		if(StringUtil.isNotBlank( promoter.getPromoterShareImagePath() )){	//更新扫码推广路径
			bUpdate = true;
			updatePromoter.setPromoterShareImagePath(promoter.getPromoterShareImagePath());
		}
		if(StringUtil.isNotBlank( promoter.getUserMobile() )){	//更新手机号码
			bUpdate = true;
			updatePromoter.setMobile(promoter.getUserMobile());
		}
		if(StringUtil.isNotBlank( promoter.getPassword() )){	//更新手机号码
			bUpdate = true;
			updatePromoter.setPassWord(promoter.getPassword());
		}
		
		if(bUpdate == false){
			return new MResultVO<>(MResultInfo.DO_NOT_HAVE_PROMOTERINFO_FOR_UPDATE);			
		}
		if(StringUtils.isNotBlank(promoter.getInviteCode())){//如果邀请码不为空  则通过邀请码注册
        	//如果是扫码推广的话 默认生成二维码图片 扫码推广员
    		if(PROMOTER_TYPE.SCANATTENTION.code.equals(updatePromoter.getPromoterType()) && StringUtil.isNotBlank(promoter.getUserMobile())){
    			//渠道表插入企业渠道
    			String qrcode = channelPromoteProxy.saveChannel(promoter.getUserMobile(),2,QrcodeConstant.SCAN_TYPE.QR_LIMIT_STR_SCENE.name(),QrcodeConstant.QRCODE_PROMOTER_USER_CODE);
    			if(StringUtil.isNotBlank(qrcode))updatePromoter.setScanAttentionImage(qrcode);
    			
    			MemberInfo memberInfo = promoterInfoProxy.queryMemberInfoByMobile(promoter.getUserMobile());
    			if(memberInfo!=null){
    				updatePromoter.setMemberId(memberInfo.getId());
    				memberInfo.setScanPromoterId(updatePromoter.getPromoterId());
    				memberInfoProxy.updateById(memberInfo);
    			}else{
    				promoterInfoProxy.register(updatePromoter);
    			}
    			updatePromoter.setPromoterStatus(DssConstant.PROMOTER_STATUS.EN_PASS.code);//默认开通认证
    			updatePromoter.setPassWord(promoter.getPassword());
    			updatePromoter.setSalt(PasswordHelper.getSalt());
			
    		}
		}
		
		ResultInfo<Integer> rlt = promoterInfoProxy.updateNotNullById(updatePromoter);
		
        
		
		if(rlt.isSuccess()){
			return new MResultVO<>(MResultInfo.SUCCESS);			
		}else{
			return new MResultVO<>(MResultInfo.UPDATE_PROMOTERINFO_FAILED);
		}	
		
	}

	
	
	private void setCache(String key , Object value){
		boolean result =  jedisCacheUtil.setCache(key, value, TOKEN_LIVE);
		if(!result){
			log.error("[缓存工具-设置Token 失败] = {}",MResultInfo.CACHE_SET_FAILED.message);
			throw new MobileException(MResultInfo.CACHE_SET_FAILED);
		}
	}
	
	private Object getCache(String key){
		return jedisCacheUtil.getCache(key);
	}
	/**
	 * 
	 * getPromoterInfoByInviteCode:(根据邀请码查询分销人员信息). <br/>  
	 * TODO(根据邀请码查询分销人员信息).<br/>   
	 *  
	 * @author zhouguofeng  
	 * @param inviteCode
	 * @return  
	 * @sinceJDK 1.8
	 */
	public MResultVO<PromoterInfoVO>  getPromoterInfoByInviteCode(String inviteCode){
		PromoterInfo  promoterInfoPram=new PromoterInfo();
		promoterInfoPram.setInviteCode(inviteCode);
		ResultInfo<PromoterInfo>  resultPromoterInfo=promoterInfoProxy.queryUniqueByObject(promoterInfoPram);
		PromoterInfoVO vo=null;
		if(resultPromoterInfo!=null){
			vo = PromoterConvert.convertPromoterInfoVO(resultPromoterInfo.getData());
		}
	
		return new MResultVO<>(MResultInfo.SUCCESS, vo); 	
	}
	/**
	 * 
	 * getUnUsedInviteCode:(根据邀请码查询是否使用). <br/>  
	 * TODO(根据邀请码查询是否使用).<br/>   
	 *  
	 * @author zhouguofeng  
	 * @param inviteCode
	 * @return  
	 * @sinceJDK 1.8
	 */
	public MResultVO<PromoterInfoVO>  getUnUsedInviteCode(String inviteCode){
		PromoterInfo  promoterInfoPram=new PromoterInfo();
		promoterInfoPram.setInviteCode(inviteCode);
		promoterInfoPram.setScanAttentionImage(null);
		ResultInfo<PromoterInfo>  resultPromoterInfo=promoterInfoProxy.queryUniqueByObject(promoterInfoPram);
		PromoterInfoVO vo=null;
		if(resultPromoterInfo.getData()!=null){
			 vo = PromoterConvert.convertPromoterInfoVO(resultPromoterInfo.getData());
		}
		
		return new MResultVO<>(MResultInfo.SUCCESS, vo); 	
	}
	/**
	 * 
	 * getPromoterInfoByTelAndType:根据手机号码和分销类型查询分销员账号信息). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>   
	 *  
	 * @author zhouguofeng  
	 * @param mobile
	 * @param promoterType
	 * @return  
	 * @sinceJDK 1.8
	 */
	public MResultVO<PromoterInfoVO>  getPromoterInfoByTelAndType(String mobile,Integer promoterType){
		PromoterInfo  promoterInfoPram=new PromoterInfo();
		promoterInfoPram.setMobile(mobile);
		promoterInfoPram.setPromoterType(promoterType);
		ResultInfo<PromoterInfo>  resultPromoterInfo=promoterInfoProxy.queryUniqueByObject(promoterInfoPram);
		PromoterInfoVO vo=null;
		if(resultPromoterInfo.getData()!=null){
			 vo = PromoterConvert.convertPromoterInfoVO(resultPromoterInfo.getData());
		}
		
		return new MResultVO<>(MResultInfo.SUCCESS, vo); 	
	}
	
}

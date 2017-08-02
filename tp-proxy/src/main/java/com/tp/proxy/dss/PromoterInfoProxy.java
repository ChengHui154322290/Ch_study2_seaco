package com.tp.proxy.dss;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.xml.rpc.ServiceException;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.util.ExceptionUtils;
import com.tp.common.util.mem.PasswordHelper;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.common.vo.PaymentConstant;
import com.tp.common.vo.QrcodeConstant;
import com.tp.common.vo.Constant.SPLIT_SIGN;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DssConstant.PROMOTER_TYPE;
import com.tp.common.vo.ord.OrderCodeType;
import com.tp.dto.common.FailInfo;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.mem.MemCallDto;
import com.tp.dto.mem.MemberInfoDto;
import com.tp.dto.pay.PayPaymentSimpleDTO;
import com.tp.dto.promoter.PromoterTopicItemDTO;
import com.tp.enums.common.PlatformEnum;
import com.tp.enums.common.SourceEnum;
import com.tp.model.dss.ChannelInfo;
import com.tp.model.dss.CommisionDetail;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.PromoterTopic;
import com.tp.model.mem.MemberInfo;
import com.tp.model.mmp.TopicInfo;
import com.tp.proxy.BaseProxy;
import com.tp.proxy.mkt.ChannelPromoteProxy;
import com.tp.proxy.mmp.ExchangeCouponChannelCodeProxy;
import com.tp.query.ord.SubOrderQO;
import com.tp.service.IBaseService;
import com.tp.service.dss.ICommisionDetailService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.dss.IPromoterTopicService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.service.mem.ISendSmsService;
import com.tp.service.ord.IOrderCodeGeneratorService;
import com.tp.service.ord.remote.ISalesOrderRemoteService;
import com.tp.service.pay.IPaymentInfoService;
import com.tp.util.StringUtil;
/**
 * 分销员信息代理层
 * @author szy
 *
 */
@Service
public class PromoterInfoProxy extends BaseProxy<PromoterInfo>{

	@Autowired
	private IPromoterInfoService promoterInfoService;
	@Autowired
	private IMemberInfoService memberInfoService;
	@Autowired
	private IPaymentInfoService paymentInfoService;
	@Autowired
	private ICommisionDetailService commisionDetailService;
	@Autowired
	private ExchangeCouponChannelCodeProxy exchangeCouponChannelCodeProxy;
	@Autowired
	ISendSmsService sendSmsService;
	@Autowired
	private IPromoterTopicService promoterTopicService;
	@Autowired
	private ISalesOrderRemoteService salesOrderRemoteService;
	
	@Autowired
	private ChannelPromoteProxy channelPromoteProxy;
	@Autowired
	private ChannelInfoProxy channelInfoProxy;

	@Autowired
	private IOrderCodeGeneratorService orderCodeGeneratorService;
	

	@Override
	public IBaseService<PromoterInfo> getService() {
		return promoterInfoService;
	}
	

	public boolean existPromoter(Number id) {
		PromoterInfo promoter = promoterInfoService.queryById(id);	
		if(promoter != null){
			return true;
		}		
		return false;
	}

	public ResultInfo<PageInfo<PromoterInfo>> queryPageByInviteCode(PromoterInfo promoterInfo,PageInfo<PromoterInfo> pageInfo){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("promoterName", promoterInfo.getPromoterName());
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(),"invite_code is not null");
		if(!"".equals(promoterInfo.getInviteCodeUsed())){
			String subsql = " and scan_attention_image is "+("1".equals(promoterInfo.getInviteCodeUsed())?"not null":"null");
			params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), params.get(MYBATIS_SPECIAL_STRING.COLUMNS.name())+subsql);
		}
		
		params.put("promoterType",DssConstant.PROMOTER_TYPE.SCANATTENTION.code);   
		try{
			PageInfo<PromoterInfo> result = promoterInfoService.queryPageByParamNotEmpty(params, pageInfo);
			if(result!=null && CollectionUtils.isNotEmpty(result.getRows())){
				List<Long> promoterIdList = new ArrayList<Long>();
				for(PromoterInfo promoter:result.getRows()){
					if(promoter.getScanAttentionImage()!=null){
						promoter.setInviteCodeUsed( DssConstant.PROMOTER_SCAN_USE.USED.getCode());
					}else{
						promoter.setInviteCodeUsed( DssConstant.PROMOTER_SCAN_USE.UNUSE.getCode());
					}
				}
				
			}
			return new ResultInfo<PageInfo<PromoterInfo>>(result);
		}catch(Throwable throwable){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(throwable), logger,promoterInfo);
			return new ResultInfo<>(failInfo);
		}
	}
	
	
	public ResultInfo<PageInfo<PromoterInfo>> queryPageByParamAndParent(Map<String,Object> params,PageInfo<PromoterInfo> pageInfo){
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
		ResultInfo<PageInfo<PromoterInfo>> result = super.queryPageByParam(params, pageInfo);
		result = initOrderInfoByParent(result,DssConstant.INDIRECT_TYPE.YES.code);
		return result;
	}
	public ResultInfo<PageInfo<PromoterInfo>> queryPageByParamAndTop(Map<String,Object> params,PageInfo<PromoterInfo> pageInfo){
		params.put(MYBATIS_SPECIAL_STRING.ORDER_BY.name(), " create_time desc");
		ResultInfo<PageInfo<PromoterInfo>> result = super.queryPageByParam(params, pageInfo);
		result = initOrderInfoByParent(result,DssConstant.INDIRECT_TYPE.GRANDSON.code);
		return result;
	}
	/**
	 * 注册促销人员（推广、分销）
	 */
	@Override
	public ResultInfo<PromoterInfo> insert(PromoterInfo obj) {
		MemberInfo memberInfo = queryMemberInfoByMobile(obj.getMobile());
		if(!DssConstant.PROMOTER_TYPE.COMPANY.code.equals(obj.getPromoterType())){
			Boolean exists = valiatePromoterInfo(obj);
			if(exists){
				return new ResultInfo<PromoterInfo>(new FailInfo("手机号已存在，不能重复申请"));
			}
			if(memberInfo!=null){
				obj.setMemberId(memberInfo.getId());
			}else{
				register(obj);
			}
		}else{
			if(StringUtil.isBlank(obj.getChannelCode())){
				return new ResultInfo<>(new FailInfo("渠道代码不能为空"));
			}
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("channelCode", obj.getChannelCode());
			Integer count = channelInfoProxy.queryByParamCount(params).getData();
			if(count!=null && obj.getPromoterId()==null && count>0){
				return new ResultInfo<>(new FailInfo("渠道代码已被使用，请输入其它代码"));
			}
			if(count!=null && obj.getPromoterId()!=null && count>1){
				return new ResultInfo<>(new FailInfo("渠道代码已被使用，请输入其它代码"));
			}
		}
		obj.setSalt(PasswordHelper.getSalt());
		if(obj.getPromoterStatus()==null){
			obj.setPromoterStatus(DssConstant.PROMOTER_STATUS.UN_PASS.code);
		}
		if(StringUtil.isNotBlank(obj.getInviter()) && PROMOTER_TYPE.DISTRIBUTE.code.equals(obj.getPromoterType())){
			PromoterInfo query = new PromoterInfo();
			query.setMobile(obj.getInviter());
			query.setPromoterType(PROMOTER_TYPE.DISTRIBUTE.code);
			PromoterInfo inviter = promoterInfoService.queryUniqueByObject(query);
			if(inviter != null) {
				obj.setParentPromoterId(inviter.getPromoterId());
			}
		}
		//如果是扫码推广的话 默认生成二维码图片 扫码推广员
		if(PROMOTER_TYPE.SCANATTENTION.code.equals(obj.getPromoterType()) && StringUtil.isNotBlank(obj.getMobile())){
			//渠道表插入企业渠道
			String qrcode = channelPromoteProxy.saveChannel(obj.getMobile(),2,QrcodeConstant.SCAN_TYPE.QR_LIMIT_STR_SCENE.name(),QrcodeConstant.QRCODE_PROMOTER_USER_CODE);
			if(StringUtil.isNotBlank(qrcode))obj.setScanAttentionImage(qrcode);
		}
		try{
			PromoterInfo promoterInfo = promoterInfoService.insert(obj);
			if(PROMOTER_TYPE.DISTRIBUTE.code.equals(obj.getPromoterType())){
				memberInfo = new MemberInfo();
				memberInfo.setId(promoterInfo.getMemberId());
				memberInfo.setShopPromoterId(promoterInfo.getPromoterId());
				memberInfoService.updateNotNullById(memberInfo);
				insertPaymentInfo(promoterInfo,obj.getDssRegisterAmount()); //生成店铺分销员开通费订单
			}
			if(DssConstant.PROMOTER_TYPE.COUPON.code.equals(obj.getPromoterType())){
				memberInfo = new MemberInfo();
				memberInfo.setId(promoterInfo.getMemberId());
				memberInfo.setPromoterId(promoterInfo.getPromoterId());
				memberInfoService.updateNotNullById(memberInfo);
			}else if (DssConstant.PROMOTER_TYPE.SCANATTENTION.code.equals(obj.getPromoterType())){
				memberInfo = new MemberInfo();
				memberInfo.setId(promoterInfo.getMemberId());
				memberInfo.setScanPromoterId(promoterInfo.getPromoterId());
				memberInfoService.updateNotNullById(memberInfo);
			}else if(DssConstant.PROMOTER_TYPE.COMPANY.code.equals(obj.getPromoterType())){
				ChannelInfo channelInfo = new ChannelInfo();
				channelInfo.setChannelCode(obj.getChannelCode());
				channelInfo.setChannelName(obj.getNickName());
				channelInfo.setCreateUser(obj.getCreateUser());
				channelInfo.setUpdateUser(obj.getCreateUser());
				channelInfo.setEshopName(obj.getNickName());
				channelInfo.setShareContent(obj.getShareContent());
				channelInfo.setShareTitle(obj.getShareTitle());
				channelInfo.setCompanyDssType(obj.getCompanyDssType());
				channelInfoProxy.insert(channelInfo);
			}
			return new ResultInfo<>(promoterInfo);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj);
			return new ResultInfo<>(failInfo);
		}
	}
	
	
	public ResultInfo<PromoterInfo> insertScan(PromoterInfo obj) {
		//查看名称是否重复
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("promoterName",obj.getPromoterName());
		param.put("promoterType",DssConstant.PROMOTER_TYPE.SCANATTENTION.code);
		Integer count = promoterInfoService.queryByParamCount(param);
		if(count>0){
			return new ResultInfo<PromoterInfo>(new FailInfo("名称已存在，不能重复申请"));
		}
		obj.setPromoterType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code);
		obj.setPromoterStatus(DssConstant.PROMOTER_STATUS.UN_PASS.code);
		try{
			return new ResultInfo<PromoterInfo>(promoterInfoService.insertScan(obj));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj);
			return new ResultInfo<>(failInfo);
		}
	}
	
	public ResultInfo<Integer> getTotalCustomersForPromoterByQO( SubOrderQO qo ) {
		return salesOrderRemoteService.getTotalCustomersForPromoterByQO(qo);
	}
		
	@Override
	public ResultInfo<PromoterInfo> queryById(Number id) {
		try{
			PromoterInfo promoterInfo = promoterInfoService.queryById(id);
			if(promoterInfo!=null){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("orderCount", "orderCount");
				params.put("orderAmount", "orderAmount");
				params.put("promoterId", promoterInfo.getPromoterId());
//				Map<String,Number> count = commisionDetailService.queryStatisticByOrderPromoterId(params);

				Map<String,Number> count_in = commisionDetailService.queryStatisticByOrderPromoterId_In(params);
				Map<String,Number> count_out = commisionDetailService.queryStatisticByOrderPromoterId_Out(params);				

				Integer orderCount = count_in.get("orderCount").intValue() - count_out.get("orderCount").intValue();
				Double orderAmount = count_in.get("orderAmount").doubleValue() - count_out.get("orderAmount").doubleValue();								
				promoterInfo.setOrderCount( orderCount );
				promoterInfo.setOrderAmount( orderAmount );
				params.clear();
				params.put("promoterId", promoterInfo.getPromoterId());
				Integer couponCount = exchangeCouponChannelCodeProxy.queryByParamCount(params).getData();
				promoterInfo.setCouponCount(couponCount);
				
			}
			return new ResultInfo<>(promoterInfo);
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,id);
			return new ResultInfo<>(failInfo);
		}
	}
	/**
	 * 根据promoterId 查询该用户的顶级分销员
	 * @param promoterId
	 * @return
	 */
    public  long  getTopPromoterById(Long promoterId){
    	PromoterInfo promoterInfo=promoterInfoService.queryById(promoterId);
    	if(promoterInfo.getParentPromoterId()==0){//如果是顶级用户  返回该用户ID
        	return promoterInfo.getPromoterId();
    	}else{
    		return promoterInfo.getTopPromoterId();//返回该用户ID
    	}
    }
	
	public ResultInfo<List<PromoterInfo>> queryPromoterInfoByIdList(List<Long> idList){
		if(CollectionUtils.isEmpty(idList)){
			return new ResultInfo<>(new FailInfo("没有参数"));
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " promoter_id in ("+StringUtil.join(idList,SPLIT_SIGN.COMMA)+")");
		try{
			return new ResultInfo<>(promoterInfoService.queryByParam(params));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,idList);
			return new ResultInfo<>(failInfo);
		}
	}
	
	public MemberInfo queryMemberInfoByMobile(String mobile){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mobile", mobile);
		MemberInfo memberInfo = memberInfoService.queryUniqueByParams(params);
		return memberInfo;
	}
	
	public ResultInfo<Integer> updatePassword(PromoterInfo promoterInfo) {
		PromoterInfo updateInfo = new PromoterInfo();
		updateInfo.setSalt(promoterInfo.getSalt());
		updateInfo.setPassWord(promoterInfo.getPassWord());
		updateInfo.setPromoterId(promoterInfo.getPromoterId());
		return super.updateNotNullById(updateInfo);
	}
	
	public PromoterInfo register(PromoterInfo promoterInfo){
		MemCallDto memberCall = new MemCallDto();
		memberCall.setIp("127.0.0.1");
		memberCall.setMobile(promoterInfo.getMobile());
		memberCall.setNickName(promoterInfo.getMobile());
		memberCall.setPassword(promoterInfo.getPassWord());
		memberCall.setSource(SourceEnum.XG);
		memberCall.setPlatform(PlatformEnum.PC);
		MemberInfoDto memberInfo = memberInfoService.register(memberCall);
		promoterInfo.setMemberId(memberInfo.getUid());
		return promoterInfo;
	}
	
	public Boolean valiatePromoterInfo(PromoterInfo promoterInfo){
		return promoterInfoService.valiatePromoterInfo(promoterInfo)>0?Boolean.TRUE:Boolean.FALSE;
	}

	private void insertPaymentInfo(PromoterInfo promoterInfo,Double dssRegisterAmount) throws ServiceException {
		PayPaymentSimpleDTO paymentDto = new PayPaymentSimpleDTO();
		paymentDto.setActionIP("127.0.0.1");
		paymentDto.setAmount(dssRegisterAmount);
		paymentDto.setBizCode(orderCodeGeneratorService.generate(OrderCodeType.DSS_PAY));
		paymentDto.setBizCreateTime(new Date());
		paymentDto.setBizType(PaymentConstant.BIZ_TYPE.DSS.code);
		paymentDto.setChannelId(0L);
		paymentDto.setGatewayId(1L);
		paymentDto.setOrderType(0L);
		paymentDto.setIdentityType(promoterInfo.getCredentialTypeCn());
		paymentDto.setIdentityCode(promoterInfo.getCredentialCode());
		paymentDto.setTaxFee(0d);
		paymentDto.setFreight(0d);
		paymentDto.setRealName(promoterInfo.getPromoterName());
		paymentDto.setUserId(promoterInfo.getMemberId());
		
		paymentInfoService.insertPaymentInfo(paymentDto);
	}
	
	private ResultInfo<PageInfo<PromoterInfo>> initOrderInfoByParent(ResultInfo<PageInfo<PromoterInfo>> resultInfo,Integer indirect){
		if(resultInfo.success){
			List<PromoterInfo> promoterInfoList = resultInfo.getData().getRows();
			if(CollectionUtils.isNotEmpty(promoterInfoList)){
				List<Long> idChildren = new ArrayList<Long>();
				promoterInfoList.forEach(new Consumer<PromoterInfo>(){
					public void accept(PromoterInfo t) {
						t.setOrderAmount(0d);
						t.setAccumulatedAmount(0d);
						idChildren.add(t.getPromoterId());
					}
				});
				List<CommisionDetail> commisionDetailList =null;
				if(DssConstant.INDIRECT_TYPE.GRANDSON.code.equals(indirect)){
					commisionDetailList = commisionDetailService.queryStatisticByBizAmountCommisionSum(idChildren, indirect, null);
				}else{
					commisionDetailList = commisionDetailService.queryStatisticByBizAmountSum(idChildren,indirect);
				}
				if(CollectionUtils.isNotEmpty(commisionDetailList)){
					for(PromoterInfo promoterInfo:promoterInfoList){
						commisionDetailList.forEach(new Consumer<CommisionDetail>(){
							public void accept(CommisionDetail commisionDetail) {
								if(promoterInfo.getPromoterId().equals(commisionDetail.getPromoterId())){
									promoterInfo.setOrderAmount(commisionDetail.getBizAmount());
									promoterInfo.setAccumulatedAmount(commisionDetail.getCommision());
								}
							}
						});
					};
				}
			}
		}
		return resultInfo;
	}
	
	public ResultInfo<Integer> updateNotNullById(PromoterInfo obj) {
		try{
			PromoterInfo promoterInfo = promoterInfoService.queryById(obj.getPromoterId());
			if(DssConstant.PROMOTER_TYPE.COMPANY.code.equals(promoterInfo.getPromoterType())){
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("channelCode", promoterInfo.getChannelCode());
				ChannelInfo channelInfo = channelInfoProxy.queryUniqueByParams(params).getData();
				if(channelInfo!=null){
					channelInfo.setEshopName(obj.getNickName());
					channelInfo.setShareContent(obj.getShareContent());
					channelInfo.setShareTitle(obj.getShareTitle());
					channelInfo.setCompanyDssType(obj.getCompanyDssType());
					channelInfo.setChannelName(obj.getNickName());
					channelInfo.setUpdateUser(obj.getUpdateUser());
					channelInfoProxy.updateNotNullById(channelInfo);
				}
			}
			return new ResultInfo<>(promoterInfoService.updateNotNullById(obj));
		}catch(Throwable exception){
			FailInfo failInfo = ExceptionUtils.print(new FailInfo(exception), logger,obj);
			return new ResultInfo<>(failInfo);
		}
	}
	
	public List<TopicInfo> selectTopics( Map<String,Object> params){
		return promoterTopicService.selectTopics(params);
	}

	public List<TopicInfo> queryTopicListByChannelCode(Long promoterId,String channelCode){
		return promoterTopicService.queryTopicListByChannelCode(promoterId,channelCode);
	}
	
	public List<PromoterTopicItemDTO> selectTopicItems( PromoterTopic pTopic){
		return promoterTopicService.selectTopicItems(pTopic);
	}
	

	public Long countTopicItems( PromoterTopic pTopic){
		return promoterTopicService.countTopicItems(pTopic);
	}	

	
	public List<TopicInfo> selectBrandTopics( Map<String,Object> params){
		return promoterTopicService.selectBrandTopics(params);
	}	

	
}

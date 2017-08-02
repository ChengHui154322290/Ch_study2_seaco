	/**
 * 
 */
package com.tp.shop.controller.dss;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tp.common.vo.DssConstant;
import com.tp.common.vo.DssConstant.PROMOTERTOPIC_STATUS;
import com.tp.common.vo.DssConstant.PROMOTER_TYPE;
import com.tp.common.vo.mem.SessionKey;
import com.tp.common.vo.mmp.TopicConstant;
import com.tp.dto.common.ResultInfo;
import com.tp.dto.promoter.PromoterInfoMobileDTO;
import com.tp.m.base.MResultVO;
import com.tp.m.base.Page;
import com.tp.m.constant.PageConstant;
import com.tp.m.constant.UserConstant;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ValidFieldType;
import com.tp.m.exception.MobileException;
import com.tp.m.query.dss.QueryPromoterTopic;
import com.tp.m.query.promoter.QueryPromoter;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.promoter.AccountDetailVO;
import com.tp.m.vo.promoter.DssLoginVO;
import com.tp.m.vo.promoter.PromoterInfoVO;
import com.tp.m.vo.promoter.PromoterOrderVO;
import com.tp.m.vo.promoter.PromoterTopicItemVO;
import com.tp.m.vo.promoter.ShelvesVO;
import com.tp.m.vo.promoter.TopicInfoVO;
import com.tp.m.vo.promoter.WithdrawDetailVO;
import com.tp.m.vo.system.UploadVO;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.dss.PromoterTopic;
import com.tp.proxy.dss.PromoterInfoProxy;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.dss.IGlobalCommisionService;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.wx.IAccountManagerService;
import com.tp.shop.ao.dss.DSSUserAO;
import com.tp.shop.ao.dss.PromoterAO;
import com.tp.shop.ao.system.FileAO;
import com.tp.shop.convert.PromoterConvert;
import com.tp.shop.helper.AuthHelper;
import com.tp.shop.helper.PropertiesHelper;
import com.tp.shop.helper.RequestHelper;
import com.tp.util.ErWeiMaUtil;
import com.tp.util.FileUtil;

import sun.misc.BASE64Encoder;

/**
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/promoter")
public class PromoterController {
	
	private static final Logger log = LoggerFactory.getLogger(PromoterController.class);
	
	@Autowired
	private PromoterAO promoterAO;
	
	@Autowired
	private AuthHelper authHelper;
	@Autowired
	private DSSUserAO dSSUserAO;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;
	@Autowired
	private IGlobalCommisionService globalCommisionService;
	@Autowired
	private IAccountManagerService accountManagerService;
	@Autowired
	IPromoterInfoService  promoterInfoService;
	/**logo存放路径*/
	@Value("#{meta['xg.logo.image.path']}")
    public String  logoImagePath;
	@Value("#{meta['xg.logo.image.back.path']}")
    public String  backGroudPath;
	@Value("#{meta['xg.scan.image.back.path']}")
    public String  scanbackGroudPath;
	@Autowired
	private FileAO fileAO;
	
	@Autowired
	private PromoterInfoProxy promoterInfoProxy;
	
	@Autowired
	private PropertiesHelper propertiesHelper;
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoter promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员登录 入参] = {}",JsonUtil.convertObjToStr(promoter));
			}
			TokenCacheTO usr = authHelper.authToken(promoter.getToken());
			MResultVO<DssLoginVO> result = promoterAO.promoterLogin(usr,promoter.getChannelcode(), promoter.getToken());
			MResultVO<DssLoginVO> rst =	promoterAO.promoterLogin(usr.getUid(), promoter.getToken());
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员登录 返回值] = {}",JsonUtil.convertObjToStr(rst));
			}
			if("hhb".equalsIgnoreCase(promoter.getChannelcode())){
				result.getData().setIsshopdss(0);
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 推广员登录  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 推广员登录  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	/**
	 * 查询推广员账户详情
	 * **/
	@RequestMapping(value = "/account",method = RequestMethod.POST)
	@ResponseBody
	public String promoterinfo(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoter promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员账户详情 入参] = {}",JsonUtil.convertObjToStr(promoter));
			}
			AssertUtil.notBlank(promoter.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoter.getType(), MResultInfo.PROMOTER_TYPE_NULL);
			Long promoterId = promoterAO.authPromoter(promoter.getToken(),promoter.getChannelcode(),DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			promoter.setPromoterid(promoterId);
			String channelCode=RequestHelper.getChannelCode(request);
			MResultVO<PromoterInfoVO> result = promoterAO.getPromoterInfo(promoter,channelCode);
			String imageUrl=result.getData().getImgurl();//分享二维码URL
			if(StringUtils.isNotBlank(imageUrl)){
				if(imageUrl == null)
					return null;

		        InputStream inStream=null;
				try {
					URL url = new URL(imageUrl); 
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5 * 1000);
					inStream = conn.getInputStream();
				} catch (MalformedURLException e) {
					  
					// TODO Auto-generated catch block  
					e.printStackTrace();  
					
				} catch (ProtocolException e) {
					  
					// TODO Auto-generated catch block  
					e.printStackTrace();  
					
				} catch (IOException e) {
					  
					// TODO Auto-generated catch block  
					e.printStackTrace();  
					
				}
				String path=ErWeiMaUtil.decoderQRCode(inStream);
				String img=getImageData(path,channelCode);
				result.getData().setImg(img);//放置带logo的二维码
			}
			if("2".equals( promoter.getType())){//扫码推广
				if( StringUtils.isBlank(result.getData().getPromoterShareImagePath() )){
					String scanshareImagePath=getShareScanImageData(imageUrl);
					result.getData().setPromoterShareImagePath(scanshareImagePath);//带背景的二维码图片
					promoter.setPromoterShareImagePath(scanshareImagePath);
					promoterAO.updatePromoter(promoter);//更新带背景的二维码图片
				}else{
					result.getData().setPromoterShareImagePath(result.getData().getPromoterShareImagePath());//带背景的二维码图片
				}
			}
//			
			
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员账户详情 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 推广员账户详情  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 推广员账户详情  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
 
	private String getImageData(String codeUrl,String channelCode) {
		if(codeUrl == null)
			return null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		String logoPath="/shareImg/"+channelCode+"Logo.png";
		if(DSSUserController.class.getResource(logoPath)==null){
			logoPath="/shareImg/defaultLogo.png";
		}
		ErWeiMaUtil.encoderQRCodeWidthLogo(codeUrl, output,logoPath);
		BASE64Encoder encoder = new BASE64Encoder();
//		MResultVO<UploadVO> result = fileAO.uploadImg(encoder.encode(output.toByteArray()));
		return encoder.encode(output.toByteArray());
	}
	
	/**
	 * 根据手机号码获取店铺人员分享信息
	 * @return
	 */
	@RequestMapping(value="/shareShop",method = RequestMethod.POST)
	@ResponseBody
	public String shareShop(HttpServletRequest request, HttpSession session){
		ResultInfo<PromoterInfo> invitePromoterInfo = null;
		try {
			String mobile = null;
			PromoterInfo promoterInfo = null;
			QueryPromoter promoter = null;
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			if (!jsonStr.isEmpty()) {
				log.info("[API接口 - 获取分销人员信息  入参] = {}", jsonStr);
			}
			if (StringUtil.isNotBlank(jsonStr)) {
				promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
				if (StringUtils.isNotBlank(promoter.getUserMobile())) {
					if (promoter.getUserMobile() != null) {
						mobile=promoter.getUserMobile();
						invitePromoterInfo = dSSUserAO.getPromoterInfo(mobile);
						if (invitePromoterInfo != null && StringUtils.isBlank(invitePromoterInfo.getData().getShareImagePath())) {
							promoterInfo = invitePromoterInfo.getData();
							 String channelCode=RequestHelper.getChannelCode(request);
							String shareImagePath=getShareImageData(propertiesHelper.getShareErweimaUrl(channelCode) + mobile );
							promoterInfo.setShareImagePath(shareImagePath);
//							dSSUserAO.updateAccountInfo(promoterInfo);//更新二维码信息
							invitePromoterInfo.getData().setShareImagePath(shareImagePath);
							log.info("[API接口 -获取分销人员店铺网址 ] = {}", propertiesHelper.getShareErweimaUrl(channelCode) + mobile );
						}
					}
				}
			}
			PromoterInfoVO vo = PromoterConvert.convertPromoterShareShopInfoVO(invitePromoterInfo.getData());
			MResultVO<PromoterInfoVO> result= new MResultVO<>(MResultInfo.SUCCESS, vo); 			
			return JsonUtil.convertObjToStr(result);		
			
		} catch (Exception e) {
			log.error(e.getMessage(), e.getMessage());
			return JsonUtil.convertObjToStr( new MResultVO<>( e.getMessage() ) );
		}
	
	}
	
	
	@RequestMapping(value = "/updatepromoter",method = RequestMethod.POST)
	@ResponseBody
	public String updatepromoter(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoter promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员认证信息更新 入参] = {}",JsonUtil.convertObjToStr(promoter));
			}
			AssertUtil.notBlank(promoter.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoter.getType(), MResultInfo.PROMOTER_TYPE_NULL);			
			if( StringUtil.isNotBlank(promoter.getCredentialCode()) ){
				AssertUtil.notValid(promoter.getCredentialCode(), ValidFieldType.ID);				
			}
			Long promoterId = promoterAO.authPromoter(promoter.getToken(),promoter.getChannelcode(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			promoter.setPromoterid(promoterId);
			MResultVO<MResultInfo> result = promoterAO.updatePromoter(promoter);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员认证信息更新 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 推广员认证信息更新  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 推广员认证信息更新  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}

	}
	
	
	
	/**
	 * 提现详情
	 * **/
	@RequestMapping(value = "/withdrawdetail",method = RequestMethod.POST)
	@ResponseBody
	public String withdrawDetail(HttpServletRequest request){		
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoter promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员提现详情请求 入参] = {}",JsonUtil.convertObjToStr(promoter));
			}
			AssertUtil.notBlank(promoter.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoter.getType(), MResultInfo.PROMOTER_TYPE_NULL);
			Long promoterId = promoterAO.authPromoter(promoter.getToken(),promoter.getChannelcode(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			promoter.setPromoterid(promoterId);
			MResultVO<WithdrawDetailVO> result = promoterAO.withdrawDetail(promoter);			
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员提现详情请求 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 推广员提现详情请求  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 推广员提现详情请求  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}		
	}

	/**
	 * 提现
	 * **/
	@RequestMapping(value = "/withdraw",method = RequestMethod.POST)
	@ResponseBody
	public String withdraw(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoter promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员提现请求 入参] = {}",JsonUtil.convertObjToStr(promoter));
			}
			AssertUtil.notBlank(promoter.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoter.getType(), MResultInfo.PROMOTER_TYPE_NULL);
			Long promoterId = promoterAO.authPromoter(promoter.getToken(),promoter.getChannelcode(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			promoter.setPromoterid(promoterId);
//			MResultVO<MResultInfo> result = promoterAO.withdrawApply(promoter);
			MResultVO<MResultInfo> result = promoterAO.withdrawApply2(promoter);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员提现请求 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 推广员提现请求  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 推广员提现请求  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 查询推广员账户流水
	 * **/
	@RequestMapping(value = "/billinfo",method = RequestMethod.POST)
	@ResponseBody
	public String accountdetail(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoter promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员账户流水 入参] = {}",JsonUtil.convertObjToStr(promoter));
			}
			AssertUtil.notBlank(promoter.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoter.getType(), MResultInfo.PROMOTER_TYPE_NULL);
			Long promoterId = promoterAO.authPromoter(promoter.getToken(),promoter.getChannelcode(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			promoter.setPromoterid(promoterId);
			MResultVO<Page<AccountDetailVO>> result = promoterAO.getBillinfo(promoter);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员账户流水 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 推广员账户流水  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 推广员账户流水  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 分销员查询
	 */
	@RequestMapping(value = "/dealers",method = RequestMethod.POST)
	@ResponseBody
	public String queryDealers(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoter promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员分销员查询 入参] = {}",JsonUtil.convertObjToStr(promoter));
			}
			AssertUtil.notBlank(promoter.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoter.getType(), MResultInfo.PROMOTER_TYPE_NULL);
			AssertUtil.notBlank(promoter.getLevel(), MResultInfo.PROMOTER_QUERY_LEVEL_CODE_NULL);
			Long promoterId = promoterAO.authPromoter(promoter.getToken(),promoter.getChannelcode(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			promoter.setPromoterid(promoterId);					
			MResultVO<Page<PromoterInfoVO>> result = promoterAO.queryDealers(promoter);
			if(log.isInfoEnabled()){
				log.info("[API接口 -推广员分销员查询 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 推广员分销员查询  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 推广员分销员查询  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	/**
	 * 查询订单数据
	 */
	@RequestMapping(value = "/orders",method = RequestMethod.POST)
	@ResponseBody
	public String queryOrders(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoter promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -查询订单数据 入参] = {}",JsonUtil.convertObjToStr(promoter));
			}
			AssertUtil.notBlank(promoter.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoter.getType(), MResultInfo.PROMOTER_TYPE_NULL);
			Long promoterId = promoterAO.authPromoter(promoter.getToken(),promoter.getChannelcode(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			promoter.setPromoterid(promoterId);
			MResultVO<Page<PromoterOrderVO>> result = promoterAO.queryOrders(promoter);
			if(log.isInfoEnabled()){
				log.info("[API接口 -查询订单数据 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 查询订单数据  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 查询订单数据  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	
	/**
	 * 设计商品上下架
	 */
	@RequestMapping(value = "/setshelves",method = RequestMethod.POST)
	@ResponseBody
	public String setShelves(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoterTopic promoterTopic = (QueryPromoterTopic) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoterTopic.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 -专题或商品上下架] = {}",JsonUtil.convertObjToStr(promoterTopic));
			}
			AssertUtil.notBlank(promoterTopic.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoterTopic.getType(), MResultInfo.PROMOTER_TYPE_NULL);
			AssertUtil.notNull(promoterTopic.getTopicid(), MResultInfo.TOPICID_IS_NULL);
			AssertUtil.notNull(promoterTopic.getShelftype(), MResultInfo.SHELF_TYPE_IS_NULL);
			AssertUtil.notNull(promoterTopic.getOnshelf(), MResultInfo.ON_SHELF_IS_NULL);			
			AssertUtil.notNull(promoterTopic.getTopictype(), MResultInfo.TOPIC_TYPE_IS_NULL);
			if( promoterTopic.getShelftype().equals( DssConstant.PROMOTERTOPIC_TYPE.TOPICITEM.code ) ){
				AssertUtil.notBlank(promoterTopic.getSku(), MResultInfo.ITEM_SKU_NULL);
			}
			if( !promoterTopic.getShelftype().equals( DssConstant.PROMOTERTOPIC_TYPE.TOPIC.code ) && 
				 !promoterTopic.getShelftype().equals( DssConstant.PROMOTERTOPIC_TYPE.TOPICITEM.code ) ){				
	            throw new MobileException(MResultInfo.WRONG_SHELF_TYPE);
			}
			if( !promoterTopic.getOnshelf().equals( DssConstant.PROMOTERTOPIC_STATUS.OFFSHELF.code ) && 
					 !promoterTopic.getOnshelf().equals( DssConstant.PROMOTERTOPIC_STATUS.ONSHELF.code ) ){				
	            throw new MobileException(MResultInfo.WRONG_SHELF_STATUS);
			}			
			if( !promoterTopic.getTopictype().equals(TopicConstant.TOPIC_TYPE_TOPIC ) 
					&& !promoterTopic.getTopictype().equals(TopicConstant.TOPIC_TYPE_BRAND ) ){
				throw new MobileException(MResultInfo.WRONG_TOPIC_TYPE);
			}

			Long promoterId = promoterAO.authPromoter(promoterTopic.getToken(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			if(DssConstant.PROMOTER_TYPE.COMPANY.code.toString().equals(promoterTopic.getType())){
				TokenCacheTO userTO = authHelper.authToken(promoterTopic.getToken());
				Boolean isManager = promoterAO.getIsManager(promoterTopic.getChannelcode(),userTO.getTel());
				if(isManager){
					promoterId =  promoterAO.authPromoter(promoterTopic.getToken(), promoterTopic.getChannelcode());
				}
			}
			promoterTopic.setPromoterid(promoterId);			
			MResultVO<ShelvesVO>  result = promoterAO.setPromoterTopic(promoterTopic);
						
			if(log.isInfoEnabled()){
				log.info("[API接口 -专题或商品上下架  返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 专题或商品上下架 MobileException] = {}",me.getMessage());
			log.error("[API接口 - 专题或商品上下架  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}

	
	/**
	 * 获取标题
	 */
	@RequestMapping(value = "/getitles",method = RequestMethod.POST)
	@ResponseBody
	public String getTitles(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			QueryPromoterTopic promoterTopic = (QueryPromoterTopic) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoterTopic.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取活动标题 入参] = {}",JsonUtil.convertObjToStr(promoterTopic));
			}
			AssertUtil.notBlank(promoterTopic.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoterTopic.getType(), MResultInfo.PROMOTER_TYPE_NULL);			
			Long promoterId = promoterAO.authPromoter(promoterTopic.getToken(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			if(DssConstant.PROMOTER_TYPE.COMPANY.code.toString().equals(promoterTopic.getType())){
				TokenCacheTO userTO = authHelper.authToken(promoterTopic.getToken());
				Boolean isManager = promoterAO.getIsManager(promoterTopic.getChannelcode(),userTO.getTel());
				if(isManager){
					promoterId =  promoterAO.authPromoter(promoterTopic.getToken(), promoterTopic.getChannelcode());
				}
			}	
			
			QueryPromoter promoter = new QueryPromoter();
			promoter.setPromoterid(promoterId);
			promoter.setTopictype( promoterTopic.getTopictype());
			MResultVO< List<TopicInfoVO> > result  = promoterAO.getTitles(promoter);
			if(log.isInfoEnabled()){
				log.info("[API接口 -  获取活动标题  返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 获取活动标题 MobileException] = {}",me.getMessage());
			log.error("[API接口 - 获取活动标题  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}
	
	
	/**
	 * 获取活动主题
	 */
	@RequestMapping(value = "/getopics",method = RequestMethod.POST)
	@ResponseBody
	public String getTopics(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			String channelCode = RequestHelper.getChannelCode(request);
			QueryPromoterTopic promoterTopic = (QueryPromoterTopic) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoterTopic.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取活动主题 入参] = {}",JsonUtil.convertObjToStr(promoterTopic));
			}
			AssertUtil.notBlank(promoterTopic.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoterTopic.getType(), MResultInfo.PROMOTER_TYPE_NULL);			
			AssertUtil.notNull(promoterTopic.getTopictype(), MResultInfo.TOPIC_TYPE_IS_NULL);
			if( !promoterTopic.getTopictype().equals(TopicConstant.TOPIC_TYPE_TOPIC ) 
					&& !promoterTopic.getTopictype().equals(TopicConstant.TOPIC_TYPE_BRAND ) ){
				throw new MobileException(MResultInfo.WRONG_TOPIC_TYPE);
			}
			
			Long promoterId = promoterAO.authPromoter(promoterTopic.getToken(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			if(DssConstant.PROMOTER_TYPE.COMPANY.code.toString().equals(promoterTopic.getType())){
				TokenCacheTO userTO = authHelper.authToken(promoterTopic.getToken());
				Boolean isManager = promoterAO.getIsManager(promoterTopic.getChannelcode(),userTO.getTel());
				if(isManager){
					promoterId =  promoterAO.authPromoter(promoterTopic.getToken(), promoterTopic.getChannelcode());
				}
			}
			QueryPromoter promoter = new QueryPromoter();
			promoter.setPromoterid(promoterId);
			promoter.setTopictype( promoterTopic.getTopictype());
			MResultVO< List<TopicInfoVO> > result  = promoterAO.getTopics(promoter,channelCode);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取活动主题 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 获取活动主题 MobileException] = {}",me.getMessage());
			log.error("[API接口 - 获取活动主题  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}

	
	/**
	 * 获取活动主题商品
	 */
	@RequestMapping(value = "/getopicitems",method = RequestMethod.POST)
	@ResponseBody
	public String getTopicItems(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			String channelCode = RequestHelper.getChannelCode(request);
			QueryPromoterTopic promoterTopic = (QueryPromoterTopic) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoterTopic.class);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取活动主题商品 入参] = {}",JsonUtil.convertObjToStr(promoterTopic));
			}
			AssertUtil.notBlank(promoterTopic.getToken(), MResultInfo.TOKEN_NULL);
			AssertUtil.notBlank(promoterTopic.getType(), MResultInfo.PROMOTER_TYPE_NULL);
			AssertUtil.notNull(promoterTopic.getTopictype(), MResultInfo.TOPIC_TYPE_IS_NULL);	
			if( !promoterTopic.getTopictype().equals(TopicConstant.TOPIC_TYPE_TOPIC ) 
					&& !promoterTopic.getTopictype().equals(TopicConstant.TOPIC_TYPE_BRAND ) ){
				throw new MobileException(MResultInfo.WRONG_TOPIC_TYPE);
			}
			
			Long promoterId = promoterAO.authPromoter(promoterTopic.getToken(), DssConstant.PROMOTER_TYPE.DISTRIBUTE.code);
			if(DssConstant.PROMOTER_TYPE.COMPANY.code.toString().equals(promoterTopic.getType())){
				TokenCacheTO userTO = authHelper.authToken(promoterTopic.getToken());
				Boolean isManager = promoterAO.getIsManager(promoterTopic.getChannelcode(),userTO.getTel());
				if(isManager){
					promoterId =  promoterAO.authPromoter(promoterTopic.getToken(), channelCode);
				}
			}						
			PromoterTopic pTopic = new PromoterTopic();
			pTopic.setPromoterId(promoterId);
			pTopic.setStatus(PROMOTERTOPIC_STATUS.OFFSHELF.code);
			pTopic.setTopicId(promoterTopic.getTopicid());			
			pTopic.setStartPage( Integer.parseInt(promoterTopic.getCurpage()) );		
			pTopic.setPageSize(PageConstant.DEFAULT_PAGESIZE);			 
			pTopic.setTopicType(promoterTopic.getTopictype());
			
			MResultVO< Page<PromoterTopicItemVO> > result  = promoterAO.getTopicItems(pTopic,channelCode);
			if(log.isInfoEnabled()){
				log.info("[API接口 - 获取活动主题商品 返回值] = {}",JsonUtil.convertObjToStr(result));
			}
			return JsonUtil.convertObjToStr(result);
		}catch(MobileException me){
			log.error("[API接口 - 获取活动主题商品 MobileException] = {}",me.getMessage());
			log.error("[API接口 - 获取活动主题商品  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me)));
			return JsonUtil.convertObjToStr(new MResultVO<>(me));
		}
	}	
	
	/**
	 * 
	 * doCheckInviteCode:(这里用一句话描述这个方法的作用). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>   
	 *  
	 * @author zhouguofeng  
	 * @param request  
	 * @return  
	 * @sinceJDK 1.8
	 */
	@RequestMapping(value = "/getInviteCode",method = RequestMethod.POST)
	@ResponseBody
	public String getInviteCode(HttpServletRequest request){
		String jsonStr = RequestHelper.getJsonStrByIO(request);
		QueryPromoter queryPromoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
		if(log.isInfoEnabled()){
			log.info("[API接口 -获取邀请码传 入参] = {}",JsonUtil.convertObjToStr(queryPromoter));
		}
		MResultVO<PromoterInfoVO> promoterInfo= promoterAO.getUnUsedInviteCode(queryPromoter.getInviteCode());
		return JsonUtil.convertObjToStr(promoterInfo);
	}
	/**
	 * 
	 * saveScanPromoter:(保存扫码员信息). <br/>  
	 * TODO(保存扫码员信息).<br/>   
	 *  
	 * @author zhouguofeng  
	 * @param request
	 * @return  
	 * @sinceJDK 1.8
	 */
	@RequestMapping(value = "/saveInviteUserInfo",method = RequestMethod.POST)
	@ResponseBody
	public String saveScanPromoter(HttpServletRequest request){
		String jsonStr = RequestHelper.getJsonStrByIO(request);
		QueryPromoter promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
		if(StringUtils.isBlank(promoter.getInviteCode())){
			String inviteCode=promoterInfoService.initInviteCode();
			PromoterInfo  promoterInfo=new PromoterInfo();
			promoterInfo.setPromoterName(promoter.getPromoterName());
			promoterInfo.setInviteCode(inviteCode);
			promoterInfo.setPromoterLevel(1);
			promoterInfo.setCommisionRate((float) 50);
			promoterInfo.setCreateUser("admin");
			promoterInfo.setUpdateUser("admin");
			ResultInfo<PromoterInfo> result = promoterInfoProxy.insertScan(promoterInfo);
			if(result.success==false){
				 return JsonUtil.convertObjToStr(new MResultVO<MResultInfo>(MResultInfo.PROMOTER_NAME_SCAN_IS_EXIST));
			}else{
				promoter.setInviteCode(result.getData().getInviteCode());
			}
			System.out.println(result.getData().getPromoterId());
		}
		
		//查询该手机号码是否已经是扫码推广员
		MResultVO<PromoterInfoVO> promoterInfo =promoterAO.getPromoterInfoByTelAndType(promoter.getUserMobile(),PROMOTER_TYPE.SCANATTENTION.code);
		if(promoterInfo.getData()!=null){//扫码员已存在
			 return JsonUtil.convertObjToStr(new MResultVO<MResultInfo>(MResultInfo.PROMOTER_SCAN_IS_EXIST));
		}
		
		MResultVO<PromoterInfoVO> inviteCodeInfo= promoterAO.getPromoterInfoByInviteCode(promoter.getInviteCode());
		promoter.setPromoterid(inviteCodeInfo.getData().getPromoterid());//设置
		if(log.isInfoEnabled()){
			log.info("[API接口 -推广员账户详情 入参] = {}",JsonUtil.convertObjToStr(promoter));
		}
		promoter.setCredentialType(DssConstant.CARD_TYPE.IDENTITY_CARD.code);
		StringBuffer smsCodeKey = new StringBuffer(promoter.getUserMobile()).append(":")
				.append(SessionKey.REGISTER_DSS.value);
		Object o = jedisCacheUtil.getCache(smsCodeKey.toString());
		log.info("smsCodeKey:"+smsCodeKey);
		log.info("smsCode Object:"+o);
		log.info("user input smsCode:"+promoter.getUserMobile());
		Object  realCode=this.jedisCacheUtil.getCache(smsCodeKey.toString());
		if(realCode==null){
		   return JsonUtil.convertObjToStr(new MResultVO<MResultInfo>(MResultInfo.CAPTCHA_ERROR));
		}else{
			Integer realSmsCode = Integer.parseInt(realCode.toString());
			// 校验验证吗
			if (promoter.getRealSmsCode().intValue() != realSmsCode.intValue())
				 return JsonUtil.convertObjToStr(new MResultVO<MResultInfo>(MResultInfo.CAPTCHA_ERROR));
		}
		
		MResultVO<MResultInfo> result=promoterAO.updatePromoter(promoter);//更新带背景的二维码图片
		if(log.isInfoEnabled()){
			log.info("[API接口 -推广员认证信息更新 返回值] = {}",JsonUtil.convertObjToStr(result));
		}
		
		return JsonUtil.convertObjToStr(result);
	}
	/**
	 * 返回分享的二进制分享图片
	 * getShareImageData:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param codeUrl
	 * @return  
	 * @sinceJDK 1.8
	 */
	private String getShareImageData(String codeUrl) {
		if(codeUrl == null)
			return null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ErWeiMaUtil.encoderQRCodeWidthLogo(codeUrl, output,logoImagePath,backGroudPath,8,330);
		BASE64Encoder encoder = new BASE64Encoder();
		MResultVO<UploadVO> result = fileAO.uploadImg(encoder.encode(output.toByteArray()));
		return result.getData().getPath();
		
	}
	
	/**
	 * 返回分享扫码的二进制分享图片
	 * getShareImageData:(这里用一句话描述这个方法的作用). <br/>  
	 *  
	 * @author zhouguofeng  
	 * @param codeUrl
	 * @return  
	 * @sinceJDK 1.8
	 */
	private String getShareScanImageData(String codeUrl) {
		if(codeUrl == null)
			return null;

        InputStream inStream=null;
		try {
			URL url = new URL(codeUrl); 
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5 * 1000);
			inStream = conn.getInputStream();
		} catch (MalformedURLException e) {
			  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
			
		} catch (ProtocolException e) {
			  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
			
		} catch (IOException e) {
			  
			// TODO Auto-generated catch block  
			e.printStackTrace();  
			
		}
		String path=ErWeiMaUtil.decoderQRCode(inStream);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ErWeiMaUtil.encoderQRCodeWidthLogo(path, output,logoImagePath,scanbackGroudPath,8,330);
		BASE64Encoder encoder = new BASE64Encoder();
		MResultVO<UploadVO> result = fileAO.uploadImg(encoder.encode(output.toByteArray()));
		return result.getData().getPath();
		
	}
}

package com.tp.m.controller.dss;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.DssConstant.GETINFO_SOURCE;
import com.tp.common.vo.DssConstant.PROMOTER_TYPE;
import com.tp.dto.common.ResultInfo;
import com.tp.m.ao.dss.DSSUserAO;
import com.tp.m.ao.dss.PromoterAO;
import com.tp.m.ao.system.FileAO;
import com.tp.m.base.BaseQuery;
import com.tp.m.base.MResultVO;
import com.tp.m.enums.MResultInfo;
import com.tp.m.enums.ValidFieldType;
import com.tp.m.exception.MobileException;
import com.tp.m.helper.AuthHelper;
import com.tp.m.helper.PropertiesHelper;
import com.tp.m.helper.RequestHelper;
import com.tp.m.query.promoter.QueryPromoter;
import com.tp.m.to.cache.TokenCacheTO;
import com.tp.m.util.AssertUtil;
import com.tp.m.util.JsonUtil;
import com.tp.m.util.StringUtil;
import com.tp.m.vo.promoter.PromoterInfoVO;
import com.tp.m.vo.system.UploadVO;
import com.tp.m.vo.user.AccountVO;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.mem.MemberInfo;
import com.tp.proxy.mkt.ChannelPromoteProxy;
import com.tp.service.dss.IPromoterInfoService;
import com.tp.service.mem.IMemberInfoService;
import com.tp.util.ErWeiMaUtil;

import sun.misc.BASE64Encoder;

@Controller
public class DSSUserController {
	private static final Logger log = LoggerFactory.getLogger(DSSUserController.class);
	
	@Autowired
	private DSSUserAO dSSUserAO;
	@Autowired
	private AuthHelper authHelper;	
	@Autowired
	IMemberInfoService 	memberInfoService;
	@Autowired
	private PromoterAO promoterAO;
	
	
	@Autowired
	IPromoterInfoService promoterInfoService;
	
	@Autowired
	private ChannelPromoteProxy channelPromoteProxy;
	
	@Value("#{meta['dss.register.amount']}")
	private Double dssRegisterAmount = 168d;
	/**logo存放路径*/
	@Value("#{meta['xg.logo.image.path']}")
    public String  logoImagePath;
	@Value("#{meta['xg.logo.image.back.path']}")
    public String  backGroudPath;
	
	@Value("#{meta['xg.scan.image.back.path']}")
    public String  scanbackGroudPath;
	
	@Autowired
	private FileAO fileAO;
	/**
	 * 用户-注册
	 * @return
	 */
	@RequestMapping(value="/dss/regist",method = RequestMethod.POST)
	@ResponseBody
	public String regist(HttpServletRequest request){
		try{
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			Gson gson = new Gson();
			PromoterInfo promoterInfo = gson.fromJson(jsonStr, PromoterInfo.class);			
			if(log.isInfoEnabled()){
				log.info("[[API接口 - 用户注册 入参] = {}",JsonUtil.convertObjToStr(promoterInfo));
			}

			promoterInfo.setPromoterType(PROMOTER_TYPE.DISTRIBUTE.code);
			promoterInfo.setCreateTime(new Date());
			promoterInfo.setCreateUser(promoterInfo.getMobile());
			promoterInfo.setUpdateTime(new Date());
			promoterInfo.setUpdateUser(promoterInfo.getMobile());
			
			if(promoterInfo.getUserAgreed()==null || promoterInfo.getUserAgreed()==false){
				throw new MobileException(MResultInfo.USERAGREEMENT_NOT_AGREED);
			}						
			AssertUtil.notValid(promoterInfo.getMobile(), ValidFieldType.TELEPHONE);
			AssertUtil.notValid(promoterInfo.getPassWord(), ValidFieldType.PASSWORD);
//			AssertUtil.notValid(promoterInfo.getCredentialCode(), ValidFieldType.ID);
//			AssertUtil.notEmpty(promoterInfo.getPromoterName(), MResultInfo.NAME_NULL);
			AssertUtil.notEmpty(promoterInfo.getCaptcha(), MResultInfo.CAPTCHA_NO_NULL);
			AssertUtil.notEmpty(promoterInfo.getNickName(), MResultInfo.NICKNAME_NO_NULL);
						
//			try {
//				Date birthday = DateUtil.parse(promoterInfo.getCredentialCode().substring(6, 14), DateUtil.SHORT_FORMAT);
//				promoterInfo.setBirthday(birthday);
//			} catch (Exception e) {
//				log.error(e.getMessage(), e);
//				return JsonUtil.convertObjToStr(new MResultVO<>(MResultInfo.ID_NO_VALID));
//			}			
//			if(StringUtils.isEmpty(promoterInfo.getBankName()) || promoterInfo.getBankName().length() > 32){
//				return JsonUtil.convertObjToStr(new MResultVO<>("银行名称为空或者太长"));
//			}
//			if(!VerifyUtil.isNumeric(promoterInfo.getBankAccount()) || promoterInfo.getBankAccount().length() > 64){
//				return JsonUtil.convertObjToStr(new MResultVO<>("银行卡号必须是长度小于64个字符的数字"));
//			}
						
			if(promoterInfo.getPromoterName() == null){
				promoterInfo.setPromoterName("");
			}
			if(promoterInfo.getCredentialCode() == null){
				promoterInfo.setCredentialCode("");
			}
			if(promoterInfo.getBankName() == null){
				promoterInfo.setBankName("");
			}
			if(promoterInfo.getBankAccount() == null){
				promoterInfo.setBankAccount("");
			}
			promoterInfo.setDssRegisterAmount(dssRegisterAmount);			
			MResultVO<AccountVO> result = dSSUserAO.insert(promoterInfo);
			return JsonUtil.convertObjToStr(result);
		}catch(Exception me){
			log.error("[API接口 - 用户注册  MobileException] = {}",me.getMessage());
			log.error("[API接口 - 用户注册  返回值] = {}",JsonUtil.convertObjToStr(new MResultVO<>(me.getMessage())));
			return JsonUtil.convertObjToStr(new MResultVO<>(me.getMessage()));
		}
	}
	
	/**
	 * 店铺入口
	 * @return
	 */
	@RequestMapping(value="/shop/{mobile}",method = RequestMethod.GET)
	public String index(BaseQuery baseQuery, HttpServletRequest request, @PathVariable(value = "mobile") String mobile){
		ResultInfo<PromoterInfo> promoterInfo = dSSUserAO.getPromoterInfo(mobile);
		if(promoterInfo != null && promoterInfo.data != null) {
			HttpSession session = request.getSession();
			session.setAttribute("dssUser", mobile);
		}
		return "redirect:/index.html?shop="+mobile;
	}
	
	@Autowired
	private PropertiesHelper propertiesHelper;
	
	
	
	/**
	 * 获取分销人员信息
	 * @return
	 */
	@RequestMapping(value="/dss/shareShop",method = RequestMethod.POST)
	@ResponseBody
	public String shareShop(HttpServletRequest request, HttpSession session){
		try {
			String mobile = null;
			Long uid = null;
			Map<String, String> result = new HashMap<String, String>();
			PromoterInfo promoterInfo = null;
			QueryPromoter promoter = null;
			GETINFO_SOURCE enumsource = null;
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			if (!jsonStr.isEmpty()) {
				log.info("[API接口 - 获取分销人员信息  入参] = {}", jsonStr);
			}
			if (StringUtil.isNotBlank(jsonStr)) {
				promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
				if (promoter != null && (StringUtil.isNotBlank(promoter.getToken()))) {
					TokenCacheTO usr = authHelper.authToken(promoter.getToken());
					mobile = usr.getTel();
					uid = usr.getUid();
					PromoterInfo invitePromoterInfo = null;
					if(mobile != null){
						invitePromoterInfo = dSSUserAO.getPromoterInfo(mobile).getData();
					}
					enumsource = DssConstant.GETINFO_SOURCE.TOKEN;
					if (invitePromoterInfo != null ) {//全部实时生成分享图片
						promoterInfo = invitePromoterInfo;
						String shareImagePath=getShareImageData(propertiesHelper.shareErweimaUrl + mobile );
						result.put("img", shareImagePath);
						promoterInfo.setShareImagePath(shareImagePath);
//						dSSUserAO.updateAccountInfo(promoterInfo);//更新二维码信息
						log.info("[API接口 -获取分销人员店铺网址 ] = {}", propertiesHelper.shareErweimaUrl + mobile );
					}else{
						result.put("img", invitePromoterInfo.getShareImagePath());
					}
				}else if (StringUtils.isNotBlank(promoter.getUserMobile())){
					if(promoter.getUserMobile() != null){
						PromoterInfo invitePromoterInfo = null;
						invitePromoterInfo = dSSUserAO.getPromoterInfo(mobile).getData();
						if (invitePromoterInfo != null) {
							promoterInfo = invitePromoterInfo;
							result.put("img", getShareImageData(propertiesHelper.shareErweimaUrl + promoter.getUserMobile() ));
							log.info("[API接口 -获取分销人员店铺网址 ] = {}", propertiesHelper.shareErweimaUrl + promoter.getUserMobile() );
						}
					}
				}
			}
			
			String strResult= JsonUtil.convertObjToStr(result);		
			return strResult;
			
		} catch (Exception e) {
			log.error(e.getMessage(), e.getMessage());
			return JsonUtil.convertObjToStr( new MResultVO<>( e.getMessage() ) );
		}
	
	}
	
	
	/**
	 * 获取分销人员信息
	 * @return
	 */
	@RequestMapping(value="/dss/shareScanPromoter",method = RequestMethod.POST)
	@ResponseBody
	public String shareScanPromoter(HttpServletRequest request, HttpSession session){
		PromoterInfo invitePromoterInfo = null;
		try {
			String mobile = null;
			Long uid = null;
			Map<String, String> result = new HashMap<String, String>();
			QueryPromoter promoter = null;
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			if (!jsonStr.isEmpty()) {
				log.info("[API接口 - 获取分销人员信息  入参] = {}", jsonStr);
			}
			if (StringUtil.isNotBlank(jsonStr)) {
				promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
				if (promoter != null && (StringUtil.isNotBlank(promoter.getToken()))) {
					TokenCacheTO usr = authHelper.authToken(promoter.getToken());
					mobile = usr.getTel();
					uid = usr.getUid();
					if(mobile != null){
						invitePromoterInfo = dSSUserAO.getScanPromoterInfo(mobile).getData();
					}
					
				}else if (StringUtils.isNotBlank(promoter.getUserMobile())){
					if(promoter.getUserMobile() != null){
						invitePromoterInfo = dSSUserAO.getScanPromoterInfo(promoter.getUserMobile()).getData();
					}
				}
			}
			if (invitePromoterInfo != null) {
				promoter.setType(DssConstant.PROMOTER_TYPE.SCANATTENTION.code.toString());
				promoter.setPromoterid(invitePromoterInfo.getPromoterId());
				MResultVO<PromoterInfoVO> promoterResult = promoterAO.getPromoterInfo(promoter);
				String imageUrl=promoterResult.getData().getImgurl();//分享二维码URL
				String scanshareImagePath=getShareScanImageData(imageUrl);
				promoter.setPromoterShareImagePath(scanshareImagePath);
				result.put("img", scanshareImagePath);
				promoterAO.updatePromoter(promoter);//更新带背景的二维码图片
			
			}
			String strResult= JsonUtil.convertObjToStr(result);		
			return strResult;
			
		} catch (Exception e) {
			log.error(e.getMessage(), e.getMessage());
			return JsonUtil.convertObjToStr( new MResultVO<>( e.getMessage() ) );
		}
	
	}
	
	
	
	/**
	 * 获取分销人员信息
	 * @return
	 */
	@RequestMapping(value="/dss/getinfo",method = RequestMethod.POST)
	@ResponseBody
	public String getInfo(HttpServletRequest request, HttpSession session){
		try {
			String mobile = null;
			Long uid = null;
			Map<String, String> result = new HashMap<String, String>();
			PromoterInfo promoterInfo = null;
			QueryPromoter promoter = null;
			GETINFO_SOURCE enumsource = null;
			String jsonStr = RequestHelper.getJsonStrByIO(request);
			if (!jsonStr.isEmpty()) {
				log.info("[API接口 - 获取分销人员信息  入参] = {}", jsonStr);
			}
			if (StringUtil.isNotBlank(jsonStr)) {
				promoter = (QueryPromoter) JsonUtil.getObjectByJsonStr(jsonStr, QueryPromoter.class);
				if (promoter != null && StringUtil.isNotBlank(promoter.getToken()) ) {
					TokenCacheTO usr = authHelper.authToken(promoter.getToken());
					mobile = usr.getTel();
					uid = usr.getUid();
					PromoterInfo invitePromoterInfo = null;
					if(mobile != null){
						invitePromoterInfo = dSSUserAO.getPromoterInfo(mobile).getData();
					}
					enumsource = DssConstant.GETINFO_SOURCE.TOKEN;
					if (invitePromoterInfo != null ) {
						promoterInfo = invitePromoterInfo;
						String img=getImageData(propertiesHelper.shareErweimaUrl + mobile );
						result.put("img", img);
						log.info("[API接口 -获取分销人员店铺网址 ] = {}", propertiesHelper.shareErweimaUrl + mobile );
					}
				}
			}
			
			if(promoter == null){
				return JsonUtil.convertObjToStr( new MResultVO< >(new MobileException(MResultInfo.PARAM_ERROR)) );		
			}
			
			if (promoter != null &&  StringUtil.isNotBlank( promoter.getInviter() ) ) {
				PromoterInfo invitePromoterInfo = dSSUserAO.getPromoterInfo( promoter.getInviter() ).getData();
				if (invitePromoterInfo != null) {
					promoterInfo = invitePromoterInfo;
					mobile = promoter.getInviter();
				}
			}
			if (promoter != null  &&  StringUtil.isNotBlank( promoter.getShop() ) && 
				( (promoterInfo == null) ||  ( promoter.getPriority()!=null && promoter.getPriority().equals(DssConstant.GETINFO_PRIORITY.SHOP.code)) )  ) {	// 店铺信息
				PromoterInfo shopPromoterInfo = dSSUserAO.getPromoterInfo( promoter.getShop() ).getData();
				if (shopPromoterInfo != null) {
					promoterInfo = shopPromoterInfo;
					mobile = promoter.getShop();
					enumsource = DssConstant.GETINFO_SOURCE.SHOP;
					session.setAttribute("dssUser", mobile);
				}
			}
			
			// 关联店铺
			if(  promoterInfo ==null && uid != null){
				MemberInfo member = memberInfoService.queryById(uid);
				Long shopid = member.getShopPromoterId();							
				PromoterInfo shopinfo= promoterInfoService.queryById(shopid);
				if(shopinfo != null){
					promoterInfo = shopinfo;
					enumsource = DssConstant.GETINFO_SOURCE.RELATEDSHOP;	
				}
			}
					
			/////////////////////////
			if (StringUtil.isNotBlank(mobile) && promoterInfo == null) {
				promoterInfo = dSSUserAO.getPromoterInfo(mobile).getData();
			}
			if (promoterInfo != null) {
				result.put("nickname", (promoterInfo.getNickName() == null ? promoterInfo.getPromoterName()
						: promoterInfo.getNickName()));
				result.put("weixin", promoterInfo.getWeixin());
				result.put("qq", promoterInfo.getQq());
				result.put("mobile", promoterInfo.getMobile());
				result.put("email", promoterInfo.getEmail());
				result.put("name", promoterInfo.getPromoterName());
				result.put("credentialType", promoterInfo.getCredentialTypeCn());
				result.put("credentialCode", promoterInfo.getCredentialCode());
				result.put("bankName", promoterInfo.getBankName());
				result.put("bankAccount", promoterInfo.getBankAccount());
				result.put("alipay", promoterInfo.getAlipay());
				result.put("pageShow", promoterInfo.getPageShow().toString());
				result.put("passTime", promoterInfo.getPassTime() == null ? ""
						: com.tp.util.DateUtil.format(promoterInfo.getPassTime(), "yyyy-MM-dd"));
				result.put("pageShow", promoterInfo.getPageShow().toString());
				if(enumsource != null){
					result.put("source",  String.valueOf(enumsource.code));									
				}
			}
				
			MResultVO<Map<String, String>> mresult = new MResultVO<Map<String, String>>(MResultInfo.SUCCESS, result);
			if (promoter.getApptype().equalsIgnoreCase("ios") || promoter.getApptype().equalsIgnoreCase("android")) {
				if (log.isInfoEnabled()) {
					log.info("[API接口 -获取分销人员信息 返回值] = {}", JsonUtil.convertObjToStr(mresult));
				}
				return JsonUtil.convertObjToStr(mresult);
			} else {
				if (log.isInfoEnabled()) {
					log.info("[API接口 -获取分销人员信息 返回值] = {}", JsonUtil.convertObjToStr(result));
				}
				return JsonUtil.convertObjToStr(result);
			}
			
		} catch (Exception e) {
			log.error(e.getMessage(), e.getMessage());
			return JsonUtil.convertObjToStr( new MResultVO<>( e.getMessage() ) );
		}
	}
	
	private String getImageData(String codeUrl) {
		if(codeUrl == null)
			return null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ErWeiMaUtil.encoderQRCodeWidthLogo(codeUrl, output,logoImagePath);
		BASE64Encoder encoder = new BASE64Encoder();
//		MResultVO<UploadVO> result = fileAO.uploadImg(encoder.encode(output.toByteArray()));
		return encoder.encode(output.toByteArray());
	}
	/**
	 * 返回分享的二进制分享图片
	 * getShareImageData:(这里用一句话描述这个方法的作用). <br/>  
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>  
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>  
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>  
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>  
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
		ErWeiMaUtil.encoderQRCodeWidthLogo(codeUrl, output,logoImagePath,backGroudPath,7,330);
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
		ErWeiMaUtil.encoderQRCodeWidthLogoXgShop(path, output,logoImagePath,scanbackGroudPath,8,230);
		BASE64Encoder encoder = new BASE64Encoder();
		MResultVO<UploadVO> result = fileAO.uploadImg(encoder.encode(output.toByteArray()));
		return result.getData().getPath();
		
	}
}

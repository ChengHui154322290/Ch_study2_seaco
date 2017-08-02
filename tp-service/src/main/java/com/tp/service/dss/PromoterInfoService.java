package com.tp.service.dss;

import static com.tp.util.BigDecimalUtil.multiply;
import static com.tp.util.BigDecimalUtil.toPrice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp.common.dao.BaseDao;
import com.tp.common.util.mem.PasswordHelper;
import com.tp.common.vo.Constant;
import com.tp.common.vo.DAOConstant.MYBATIS_SPECIAL_STRING;
import com.tp.common.vo.DssConstant;
import com.tp.common.vo.PageInfo;
import com.tp.dao.dss.PromoterInfoDao;
import com.tp.model.dss.GlobalCommision;
import com.tp.model.dss.PromoterInfo;
import com.tp.model.prd.ItemSku;
import com.tp.redis.util.DBJedisList;
import com.tp.redis.util.JedisCacheUtil;
import com.tp.service.BaseService;
import com.tp.service.prd.IItemSkuService;
import com.tp.util.Base64;
import com.tp.util.StringUtil;

@Service
public class PromoterInfoService extends BaseService<PromoterInfo> implements IPromoterInfoService {

	@Autowired
	private IItemSkuService  itemSkuService;
	@Autowired
	private IGlobalCommisionService  globalCommisionService;
	@Autowired
	private JedisCacheUtil jedisCacheUtil;

	@Autowired
	private PromoterInfoDao promoterInfoDao;
	
	private DBJedisList<String> invatecodeList;
	
	@Override
	public BaseDao<PromoterInfo> getDao() {
		return promoterInfoDao;
	}
	
	public PromoterInfo insertScan(PromoterInfo obj) {
		obj.setInviteCode(initInviteCode());
		return super.insert(obj);
	}
	
	@Override
	public Integer valiatePromoterInfo(PromoterInfo promoterInfo) {
		encode(promoterInfo);
		Map<String,Object> params = new HashMap<String,Object>();
		params.put(MYBATIS_SPECIAL_STRING.COLUMNS.name(), " promoter_type="+promoterInfo.getPromoterType()+
				" and mobile='"+promoterInfo.getMobile()+"' ");
		return promoterInfoDao.queryByParamCount(params);
	}
	

	@Override
	public Double getCurrentCommision(Long promoterid, String sku, Double price){		
		ItemSku qursku = new ItemSku();
		qursku.setSku( sku );
		List<ItemSku> skulist = itemSkuService.queryByObject(qursku);
		Double commisionRate = null;
		if(skulist == null || skulist.isEmpty()){
			return Constant.ZERO;
		}
		commisionRate = skulist.get(0).getCommisionRate();			
		PromoterInfo promoterinfo = queryById(promoterid);
		if(promoterinfo == null){
			return Constant.ZERO;
		}
		GlobalCommision globalCommision = globalCommisionService.queryLastGlobalCommision();						
		if(globalCommision ==null){
			return Constant.ZERO;
		}
		Double rate = globalCommision.getCurrentCommisionRate(promoterinfo, commisionRate);	
		return toPrice(multiply( price, rate)).doubleValue();										
	}
	
	
	@Override
	public Double getCurrentCommision2(PromoterInfo promoterinfo, Double price, Double commisionRate){
		
		if(promoterinfo == null){
			return Constant.ZERO;
		}
		GlobalCommision globalCommision = globalCommisionService.queryLastGlobalCommision();						
		if(globalCommision ==null){
			return Constant.ZERO;
		}
		Double rate = globalCommision.getCurrentCommisionRate(promoterinfo, commisionRate);	
		return toPrice(multiply( price, rate)).doubleValue();										
	}

	
	public PromoterInfo insert(PromoterInfo obj) {
		return super.insert(encode(obj));
	}

	public int updateNotNullById(PromoterInfo obj) {
		return super.updateNotNullById(encode(obj));
	}

	public int updateById(PromoterInfo obj) {
		return super.updateById(encode(obj));
	}

	public PromoterInfo queryById(Number id) {
		return decode(super.queryById(id));
	}
	

	public List<PromoterInfo> queryByObject(PromoterInfo obj) {
		return decode(super.queryByObject(encode(obj)));
	}

	public List<PromoterInfo> queryByParamNotEmpty(Map<String, Object> params) {
		return decode(super.queryByParamNotEmpty(encode(params)));
	}

	public List<PromoterInfo> queryByParam(Map<String, Object> params) {
		return decode(super.queryByParam(encode(params)));
	}
	
	public PageInfo<PromoterInfo> queryPageByParamNotEmpty(Map<String, Object> params,
			PageInfo<PromoterInfo> info) {
		return decode(super.queryPageByParamNotEmpty(encode(params),info));
	}

	public PageInfo<PromoterInfo> queryPageByParam(Map<String, Object> params, PageInfo<PromoterInfo> info) {
		return decode(super.queryPageByParam(encode(params),info));
	}
	
	public Integer queryByParamCount(Map<String, Object> params) {
		return super.queryByParamCount(encode(params));
	}
	
	
	public PageInfo<PromoterInfo> queryPageByParams(Map<String, Object> params, PageInfo<PromoterInfo> info) {
		return decode(super.queryPageByParams(encode(params),info));
	}
	
	private String getPassword(String password, String salt){
		if(password == null) return null;
		return Hex.encodeHexString(DigestUtils.md5(password+salt));
	}
	private PromoterInfo encode(PromoterInfo promoterInfo){
		if(StringUtil.isNotBlank(promoterInfo.getPassWord())){
			promoterInfo.setSalt(PasswordHelper.getSalt());
			promoterInfo.setPassWord(getPassword(promoterInfo.getPassWord(),promoterInfo.getSalt()));
		}
		if(StringUtil.isNotBlank(promoterInfo.getMobile())){
			promoterInfo.setMobile(Base64.encode(promoterInfo.getMobile().getBytes()));
		}
		if(StringUtil.isNotBlank(promoterInfo.getCredentialCode())){
			promoterInfo.setCredentialCode(Base64.encode(promoterInfo.getCredentialCode().getBytes()));
		}
		if(StringUtil.isNotBlank(promoterInfo.getBankAccount())){
			promoterInfo.setBankAccount(Base64.encode(promoterInfo.getBankAccount().getBytes()));
		}
		return promoterInfo;
	}
	
	private Map<String,Object> encode(Map<String,Object> param){
		if(StringUtil.isNotBlank(param.get("mobile"))){
			param.put("mobile", Base64.encode(param.get("mobile").toString().getBytes()));
		}
		if(StringUtil.isNotBlank(param.get("credentialCode"))){
			param.put("credentialCode", Base64.encode(param.get("credentialCode").toString().getBytes()));
		}
		if(StringUtil.isNotBlank(param.get("bankAccount"))){
			param.put("bankAccount", Base64.encode(param.get("bankAccount").toString().getBytes()));
		}
		return param;
	}
	
	private PageInfo<PromoterInfo> decode(PageInfo<PromoterInfo> promoterInfoPage){
		decode(promoterInfoPage.getRows());
		return promoterInfoPage;
	}
	
	private List<PromoterInfo> decode(List<PromoterInfo> promoterInfoList){
		if(CollectionUtils.isNotEmpty(promoterInfoList)){
			promoterInfoList.forEach(new Consumer<PromoterInfo>(){
				@Override
				public void accept(PromoterInfo promoterInfo) {
					decode(promoterInfo);
				}
			});
		}
		return promoterInfoList;
	}
	private PromoterInfo decode(PromoterInfo promoterInfo){
		if(promoterInfo == null) return null;
		if(StringUtil.isNotBlank(promoterInfo.getMobile())){
			promoterInfo.setMobile(new String(Base64.decode(promoterInfo.getMobile())));
		}
		if(StringUtil.isNotBlank(promoterInfo.getCredentialCode())){
			promoterInfo.setCredentialCode(new String(Base64.decode(promoterInfo.getCredentialCode())));
		}
		if(StringUtil.isNotBlank(promoterInfo.getBankAccount())){
			promoterInfo.setBankAccount(new String(Base64.decode(promoterInfo.getBankAccount())));
		}
		return promoterInfo;
	}

	

	public String initInviteCode(){
		String code = getRandomString(6);
		if(invatecodeList==null){
			invatecodeList = new DBJedisList<String>("promoter:invatecode",1);
		}
		if(invatecodeList.contains(code)){
			initInviteCode();
		}else{
			invatecodeList.add(code);
		}
		return code;
	}

	public static String getRandomString(int length){
	     String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	     Random random=new Random();
	     StringBuffer sb=new StringBuffer();
	     do{
	    	 int number=random.nextInt(62);
	    	 char chr = str.charAt(number);
	    	 if(!"lIoO".contains(""+chr)){
			     sb.append(str.charAt(number));
	    	 }
	     }while(sb.length()<length);
	     return sb.toString();
	 }

	@Override
	public String queryShortNameByChannelCode(String channelCode) {
		if (StringUtil.isEmpty(channelCode)) return null;
		String shortName = jedisCacheUtil.getCacheString("promoter:channelcode:shortname:"+channelCode);
		if(StringUtil.isNotBlank(shortName)){
			return shortName;
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("channelCode",channelCode);
		params.put("promoterType", DssConstant.PROMOTER_TYPE.COMPANY.code);
		PromoterInfo promoterInfo = queryUniqueByParams(params);
		if(promoterInfo!=null){
			jedisCacheUtil.setCacheString("promoter:channelcode:shortname:"+channelCode, promoterInfo.getNickName());
			return promoterInfo.getNickName();
		}
		return null;
	}

	@Override
	public List<PromoterInfo> getAllChildPromoterInfo(PromoterInfo promoterInfo) {
		List<PromoterInfo>   allChildPromoterInfoList=new ArrayList<PromoterInfo>();
		List<PromoterInfo> firstChildPromoterInfoList=promoterInfoDao.queryByObject(promoterInfo);
		allChildPromoterInfoList.addAll(firstChildPromoterInfoList);
		for(PromoterInfo firstpromoterInfo:firstChildPromoterInfoList){
			PromoterInfo promoterInfoForSearch=new PromoterInfo();
			promoterInfoForSearch.setPromoterType(promoterInfo.getPromoterType());
			promoterInfoForSearch.setParentPromoterId(firstpromoterInfo.getPromoterId());//设置父分销员ID
			List<PromoterInfo>  childList=getAllChildPromoterInfo(promoterInfoForSearch);
			if(childList!=null&&childList.size()>0){
				allChildPromoterInfoList.addAll(childList);
			}
			
		}
		
		return allChildPromoterInfoList;
	}
}

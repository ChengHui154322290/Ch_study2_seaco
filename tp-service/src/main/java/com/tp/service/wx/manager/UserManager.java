package com.tp.service.wx.manager;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.util.RequestUtil;
import com.tp.common.vo.wx.RequestUrlConstant;
import com.tp.dto.wx.Oauth2AccessTokenDto;
import com.tp.dto.wx.Oauth2UserInfoDto;
import com.tp.dto.wx.UserListDto;

/**
 * 用户管理
 * @author zhuss
 * @2016年4月27日 下午4:49:28
 */
public class UserManager extends BaseManager{
	
	//拉取用户列表
	public static List<String> queryUserList(String token) {
		String url = RequestUrlConstant.USER_LIST_URL.replace("ACCESS_TOKEN",token).trim();
		List<String> result = new ArrayList<>();
		JSONObject jsonObject = RequestUtil.httpsRequest(url.trim(), "GET", null);
		handleError(jsonObject,"拉取关注用户列表");
		if (jsonObject.containsKey("data")) {
			UserListDto userList = JSONObject.parseObject(jsonObject.toJSONString(), UserListDto.class);
			if(null != userList.getData()){
				if(userList.getCount() == userList.getTotal()){ //所有用户已拉取
					return userList.getData().getOpenid();
				}
				result.addAll(userList.getData().getOpenid());
				if (url.contains("next_openid")) {
					String nextOpenIdVal = url.substring(url.indexOf("=")+1);
					String[] nextOpenIdVals = nextOpenIdVal.split("&next_openid=");
//					url.replace(nextOpenIdVals[1], userList.getNext_openid());
					url=nextOpenIdVals[0]+"&next_openid=" + userList.getNext_openid();
				} else {
//					url += "&next_openid=" + userList.getNext_openid();
					url = token+"&next_openid=" + userList.getNext_openid();
				}
				queryUserList(url);
			}
		}
		return result;
	}
	
	/**网页授权相关API**/
	//根据CODE获取网页授权的openid和access_token，此access_token和基础的access_token不同，没有次数限制
	public static Oauth2AccessTokenDto getOauthAccessToken(String code, String appId, String appSecret) {
		String url = RequestUrlConstant.OAUTH2_ACCESS_TOKEN_URL.replace("APPID", appId).replace("SECRET", appSecret)
				.replace("CODE", code).trim();
		JSONObject jsonObject = RequestUtil.httpsRequest(url, "GET", null);
		handleError(jsonObject, "获取网页授权的ACCESSTOKEN");
		return JSONObject.parseObject(jsonObject.toJSONString(), Oauth2AccessTokenDto.class);
	}
	
	//网页授权同意后根据code获取微信用户信息
	public static Oauth2UserInfoDto getOauthUserInfo(String accessToken,String openId){
		String url = RequestUrlConstant.OAUTH2_GET_USERINFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId).trim();
		JSONObject jsonObject = RequestUtil.httpsRequest(url, "GET", null);
		handleError(jsonObject,"获取网页授权的用户信息");
		return JSONObject.parseObject(jsonObject.toJSONString(), Oauth2UserInfoDto.class);
	}
}

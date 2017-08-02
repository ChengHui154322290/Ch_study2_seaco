package com.tp.ptm.interceptor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.tp.common.util.ptm.EncryptionUtil;
import com.tp.common.vo.ptm.ErrorCodes.AuthError;
import com.tp.dto.mmp.ReturnData;
import com.tp.ptm.annotation.Authority;
import com.tp.ptm.ao.TokenServiceAO;

/**
 * 权限拦截器
 * 
 * @author 项硕
 * @version 2015年5月16日 上午12:37:59
 */
public class AuthorityInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AuthorityInterceptor.class);

	private static Integer EXPIRE_HOURS = 3; // 时间戳3小时过期
	private static String DATE_PATTERN = "yyyyMMddHHmmss";

	@Autowired
	private TokenServiceAO tokenServiceAO;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {

			HandlerMethod method = (HandlerMethod) handler;
			if (null != method.getMethodAnnotation(Authority.class) || null != method.getMethod().getDeclaringClass().getAnnotation(Authority.class)) { // 需要权限验证

				String appKey = request.getParameter("appkey");
				String timestamp = request.getParameter("timestamp");
				String version = request.getParameter("version");
				String sign = request.getParameter("sign"); // MD5（APPKEY+token+时间戳+版本号）

				if (StringUtils.isNotBlank(appKey) && StringUtils.isNotBlank(timestamp) && StringUtils.isNotBlank(version) && StringUtils.isNotBlank(sign)) {
					try {
						Date clientDate = new SimpleDateFormat(DATE_PATTERN).parse(timestamp);
						if (DateUtils.addHours(new Date(), -EXPIRE_HOURS).after(clientDate) || DateUtils.addHours(new Date(), EXPIRE_HOURS).before(clientDate)) {
							return error(AuthError.TIME_STAMP_EXPIRE, response);
						}
					} catch (ParseException e) {
						return error(AuthError.TIME_STAMP_PATTERN, response);
					}

					String token = tokenServiceAO.getTokenByAppKey(appKey);
					String encryptionSource = appKey + token + timestamp + version;
					String encryptionSign = EncryptionUtil.encrptMD5(encryptionSource);
					if (logger.isDebugEnabled()) {
						logger.debug("encryptionSource:{}_________encryptionSign:{}", encryptionSource, encryptionSign);
					}
					if (sign.equalsIgnoreCase(encryptionSign)) {
						return true; // 验证通过
					}
				}

				/* 验证失败 */
				return error(AuthError.UNPASS_SIGN, response);
			}
		}
		return true;
	}

	// 校验错误
	private boolean error(AuthError error, HttpServletResponse response) {
		try {
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(JSONObject.toJSONString(new ReturnData(false, error.code, error.cnName)));
		} catch (Exception e) {
			logger.error("返回响应结果时IO异常", e);
		}
		return false;
	}

//	public static void main(String[] args) {
//
//		Date clientDate = null;
//		try {
//			clientDate = new SimpleDateFormat(DATE_PATTERN).parse("20150602075600");
//		} catch (ParseException e) {
//			logger.error(e.getMessage(), e);
//		}
//		if (clientDate.before(DateUtils.addHours(new Date(), -EXPIRE_HOURS)) || clientDate.after(DateUtils.addHours(new Date(), EXPIRE_HOURS))) {
//			System.out.println("yes");
//		} else {
//			System.out.println("no");
//		}
//	}
}

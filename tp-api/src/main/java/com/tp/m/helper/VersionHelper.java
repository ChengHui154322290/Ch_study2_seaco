package com.tp.m.helper;

import com.tp.enums.common.PlatformEnum;
import com.tp.m.base.BaseQuery;
import com.tp.m.util.StringUtil;

/**
 * 版本管理
 * @author zhuss
 *
 */
public class VersionHelper {
	
	public static final int VERSION_120 = 120;
	private static final int VERSION_130 =130 ;
	
	public static final int VERSION_ANDRIOID_1301 = 1301;

	public static final int VERSION_150 = 1500;

	/**
	 * 获取版本号并转为整型
	 * @param name
	 * @return
	 */
	public static int convertAppVersion(String appversion) {
		if(StringUtil.isBlank(appversion)) return 0;
		String appv = appversion.replace(".", "").trim();
		return StringUtil.getIntegerByStr(appv);
	}

	public static int getAppVersion4(String appversion){
		if(StringUtil.isBlank(appversion)) return 0;
		String appv = appversion;
		if(appv.indexOf(",")>0 ){
			if(appv.lastIndexOf(",")+1< appv.length()){
				appv = appv.substring(appv.lastIndexOf(",")+1);
			}else {
				appv = appv.substring(0,appv.indexOf(","));
			}
		}

		String appvFordot = appv;
		int dotCount = 0;
		while (appvFordot.indexOf(".")>0){
			appvFordot = appvFordot.substring(appvFordot.indexOf(".")+1);
			dotCount++;
		}
		appv = appv.replace(".", "").trim();

		if(dotCount<3){
			appv= appv+"0";
		}
		return StringUtil.getIntegerByStr(appv);
	}
	
	/**
	 * 比较两个版本
	 * @param dbV:数据库最新版本
	 * @param appV:前台传入的版本
	 * @return 0最新 1不是最新
	 */
	public static String compareVersion(String dbV,String appV){
		int dbVe = convertAppVersion(dbV);
		if(dbVe < 1000) dbVe = dbVe*10;
		int appVe = convertAppVersion(appV);
		if(appVe < 1000) appVe = appVe*10;
		if(appVe >= dbVe) return StringUtil.ONE;
		return StringUtil.ZERO;
	}

	/**
	 * 处理1.2.0版本
	 * @param apptype
	 * @param appversion
	 * @return
	 */
	public static boolean before120Version(BaseQuery base){
		boolean isApp = RequestHelper.isAPP(base.getApptype());
		if(isApp){
			int currtv = convertAppVersion(base.getAppversion());
			if(currtv<VERSION_120) return true;
			return false;
		}
		return false;
	}

	/**
	 * 处理1.3.0版本
	 * @return
	 */
	public static boolean before130Version(BaseQuery base){
		boolean isApp = RequestHelper.isAPP(base.getApptype());
		if(isApp){
			int currtv = convertAppVersion(base.getAppversion());
			if(currtv<VERSION_130) return true;
			return false;
		}
		return false;
	}

	/**
	 * 处理1.3.0.1临时android版本
	 * @return
	 */
	public static boolean before1301AndroidVersion(String appType,String appversion){
		if(StringUtil.equals(appType, PlatformEnum.ANDROID.name())){
			int currtv = convertAppVersion(appversion);
			if(currtv < 1000) currtv = currtv*10;
			if(currtv<VERSION_ANDRIOID_1301) return true;
			return false;
		}
		return false;
	}
}

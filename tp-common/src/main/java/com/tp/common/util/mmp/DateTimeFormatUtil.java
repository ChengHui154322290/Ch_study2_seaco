package com.tp.common.util.mmp;


import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;


public class DateTimeFormatUtil {

	private static final SimpleDateFormat yyyMMddSDF = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat yyyMMSDF = new SimpleDateFormat("yyyy-MM");
	private static final SimpleDateFormat yyyMMddSDFAsInteger = new SimpleDateFormat("yyyyMMdd");
	private static final SimpleDateFormat yyyMMddHHmmssSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat yyyMMddHHmmssForTitle = new SimpleDateFormat("yyyyMMddHHmmss");
	private static final SimpleDateFormat yyyMMForAttribute = new SimpleDateFormat("yyyyMM");
	private static final SimpleDateFormat HHmmSDF = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat HHSDF = new SimpleDateFormat("HH");
	private static final SimpleDateFormat mmSDF = new SimpleDateFormat("mm");
	private static final SimpleDateFormat yyyyMMddHHmmSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat yyyyMMddHHmmSDFLocal=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	private static final SimpleDateFormat MMddHHmmSDF = new SimpleDateFormat("MM-dd HH:mm");
	private static final SimpleDateFormat fmtGMT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
	private static final DateFormat MMddHHmmSDFLocal = new SimpleDateFormat("MM月dd日 HH:mm");
	
	static {
		//
		fmtGMT.setTimeZone(new SimpleTimeZone(0, "GMT"));
	}

	private static Calendar cal= Calendar.getInstance();

	public static Date parseyyyyMMddHHmmDate(String str) throws Exception, ParseException {
		if (StringUtils.isEmpty(str))
			throw new Exception("输入时间不能为空!");
		return yyyyMMddHHmmSDF.parse(str);
	}
	public static Date parseyyyyMMdd(String str) throws Exception, ParseException {
		if (StringUtils.isEmpty(str))
			throw new Exception("输入时间不能为空!");
		//	str = str.replace("年", "-").replace("月", "-").replace("日", "").replace("/", "-").replace(".", "-");
		return yyyMMddSDF.parse(str);
	}
	public static Date parseyyyyMM(String str) throws Exception, ParseException {
		if (StringUtils.isEmpty(str))
			throw new Exception("输入时间不能为空!");
		return yyyMMSDF.parse(str);
	}
	
	public static Date parseyyyyMMddHHmmssDate(String str) throws Exception, ParseException {
		if (StringUtils.isEmpty(str))
			throw new Exception("输入时间不能为空!");
		return yyyMMddHHmmssSDF.parse(str);
	}

	/**
	 * 格式化时间:形如2011-04-11
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDatemm(Date date) {
		if (date == null)
			return "";
		return mmSDF.format(date);
	}

	// 毫秒数
	public static long getMillisecond() {

		return System.currentTimeMillis();
	}

	/**
	 * 格式化时间:形如2011-04-11
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateHH(Date date) {
		if (date == null)
			return "";
		return HHSDF.format(date);
	}

	/**
	 * 格式化时间:形如2011-04-11
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateyyyyMMdd(Date date) {
		if (date == null)
			return "";
		return yyyMMddSDF.format(date);
	}

	/**
	 * 格式化时间:形如20110411
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateyyyyMMddAsInteger(Date date) {
		if (date == null)
			return "";
		return yyyMMddSDFAsInteger.format(date);
	}

	/**
	 * 格式化时间:形如2011-04-11 18:15:12
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateyyyyMMddHHmmss(Date date) {
		if (date == null)
			return "";
		return yyyMMddHHmmssSDF.format(date);
	}

	
	public static String formatDateyyyyMMddHHmm(Date date) {
		if (date == null)
			return "";
		return yyyyMMddHHmmSDF.format(date);
	}
	
	public static String formatDateyyyyMMddHHmmLocal(Date date) {
		if (date == null)
			return "";
		return yyyyMMddHHmmSDFLocal.format(date);
	}
	
	public static String formatDateMMddHHmmLocal(Date date){
		if (date == null)
			return "";
		return MMddHHmmSDFLocal.format(date);
	}
	/**
	 * 格式化时间:形如18:15
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateHHmm(Date date) {
		if (date == null)
			return "";
		return HHmmSDF.format(date);
	}

	/**
	 * 格式化时间:形如20110411181512
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateyyyyMMddHHmmssForTitle(Date date) {
		if (date == null)
			return "";
		return yyyMMddHHmmssForTitle.format(date);
	}

	/**
	 * 格式化时间:形如201104
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateyyyyMM(Date date) {
		if (date == null)
			return "";
		return yyyMMForAttribute.format(date);
	}

	/**
	 * long --> Date
	 * 
	 * @param millis
	 * @return
	 * @throws Exception
	 */
	public static Date parseDate(long millis) {
		cal.setTimeInMillis(millis);
		return cal.getTime();
	}

	/**
	 * date --> long
	 * 
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static long fromDate(Date date) throws Exception {
		if (date == null)
			throw new Exception("input date cannot be null!!!");
		return date.getTime();
	}

	public static boolean isGreaterSystemDate(Date d1) {
		Date now = new Date();
		long difference = now.getTime() - d1.getTime();
		System.out.println(now.getTime() +"________"+ d1.getTime());
		System.out.println(formatDateyyyyMMddHHmmss(now));
		System.out.println(formatDateyyyyMMddHHmmss(d1));
		return difference < 0 ? true : false;
	}
	
	public static String toGMTString(Date d) {
		//
		return fmtGMT.format(d);
	}
	
	public static Date fromGMTString(String s) {
		try {
			return fmtGMT.parse(s);
		}
		catch (Exception e) {
			return new Date(0L);
		}
	}
	
	public static void main(String[] args) {
		long t = 1407643255000l;
		Date date = parseDate(t);
		System.out.println(date.toLocaleString());
	}
     
     /*
      * 一分鐘內：x秒前
		一小時內：x分鐘前
		一天內：今天 下午6：15
		兩天內：昨天 上午11:56
		超過兩天，顯示完整年月日：12/5/8 下午11:56
      */
	public static String timeTransform(Date noteDate) {
		return timeTransform(noteDate, Locale.CHINA);
	}

	/**
	 * 英文版时间转换
	 * 
	 * @param date
	 * @param en
	 * @return
	 */
	public static String timeTransform(Date date, Locale loc) {
		String result = "";
		Date now = new Date();
		long millionSecondsNow = now.getTime();
		long millionSeconds = Math.abs(now.getTime() - date.getTime());
		long minutes = millionSeconds / (1000 * 60);
		long seconds = millionSeconds % (1000 * 60) / 1000;
		String ymd_now = DateTimeFormatUtil.formatDateyyyyMMddAsInteger(now);
		String note_ymd = DateTimeFormatUtil.formatDateyyyyMMddAsInteger(date);
		long millionSecondsYesterday = millionSecondsNow - (1000 * 60 * 60 * 24);
		try {
			Date yesterday = parseDate(millionSecondsYesterday);
			String ymd_yesterday = DateTimeFormatUtil.formatDateyyyyMMddAsInteger(yesterday);

			if (minutes == 0) {
				String profix = "秒前";
				if (loc != null && Locale.US.equals(loc))
					profix = " sec ago";
				result = seconds + profix;
			} else if (minutes < 60) {
				String profix = "分钟前";
				if (loc != null && Locale.US.equals(loc))
					profix = " min ago";
				result = minutes + profix;
			} else if (ymd_now.equals(note_ymd)) {
				String HHmm = DateTimeFormatUtil.formatDateHHmm(date);
				result = "今天  " + HHmm;
				if (loc != null && Locale.US.equals(loc))
					result = HHmm + " today";
			} else if (ymd_yesterday.equals(note_ymd)) {
				String HHmm = DateTimeFormatUtil.formatDateHHmm(date);
				result = "昨天  " + HHmm;
				if (loc != null && Locale.US.equals(loc))
					result = HHmm + " yesterday";
			} else {
				result = DateTimeFormatUtil.formatDateyyyyMMddHHmm(date);
			}

		} catch (Exception e) {
			result = DateTimeFormatUtil.formatDateyyyyMMddHHmm(date);
		}
		return result;
	}
	
	public static String formatDateMMddHHmm(Date date) {
		if (date == null)
			return "";
		return MMddHHmmSDF.format(date);
	}
	
	public static boolean isAfter(Date d1,Date d2){
		return d1.after(d2);
	}
	
	public static Date getNextMonday(){
		Calendar c=Calendar.getInstance();
		int between;
		if(c.get(Calendar.DAY_OF_WEEK)==1){
			between=1;
		}else{
			between=9-c.get(Calendar.DAY_OF_WEEK);
		}
		c.add(Calendar.DATE, between);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static Date getDayBetweenToday(int beteen){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, beteen);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
}

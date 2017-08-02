/**
 * 
 */
package com.tp.common.vo.customs;

/**
 * @author Administrator
 *
 */
public class CustomsConstant {
	
	//申报单位类型
	public enum DeclareCompanyType{
		P2EC(1 ,"个人委托电商企业申报"),
		P2LC(2 ,"个人委托物流企业申报"),
		P2TC(3 ,"个人委托第三方申报");

		public Integer code;
		public String desc;
		
		public static String getDeclareCompanyTypeDescByCode(Integer code){
			for (DeclareCompanyType company : DeclareCompanyType.values()) {
				if (company.getCode().equals(code)) {
					return company.getDesc();
				}
			}
			return null;
		}
		
		private DeclareCompanyType(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	//码头货场（暂时只有杭州，之后增加其他或者写入 数据库）
	public enum CustomsField{
		HZ_XIACHENG("292801", "杭州-下城园区"),
		HZ_XIASHA("299102", "杭州-下沙园区"),
		HZ_XIAOSHAN("299201", "杭州-萧山园区");
		
		public String code;
		public String desc;
		
		private CustomsField(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	
	//发货方式
	public enum DeliveryType{
		POST(1, "邮政小包"),
		EXPRESS(2, "快件"),
		EMS(3, "EMS");
		
		public Integer code;
		public String desc;
		
		private DeliveryType(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
	
	//包装类型
	public enum WrapType{
		WOOD(1, "木箱"),
		PAPER(2, "纸箱"),
		BUCKET(3, "桶装"),
		BULK(4, "散装"),
		TRAY(5, "托盘"),
		BAG(6, "包"),
		OTHER(7, "其它");
		
		public Integer code;
		public String name;
		
		private WrapType(Integer code, String name) {
			this.code = code;
			this.name = name;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	//币种
	public enum Currency{
		HKD("110", "港元"),
		IRR("113", "伊朗里亚尔"),
		JPY("116", "日本元"),
		KWD("118", "科威特第纳尔"),
		MOP("121", "澳门元"),
		MYR("122", "马来西亚林吉特"),
		PKR("127", "巴基斯坦卢比"),
		PHP("129", "菲律宾比索"),
		SGD("132", "新加坡元"),
		THB("136", "泰国铢"),
		CNY("142", "人民币"),
		TWD("143", "台湾元"),
		ECU("300", "欧元"),
		DKK("302", "丹麦克朗"),
		GBP("303", "英镑"),
		NOK("326", "挪威克朗"),
		SEK("330", "瑞典克朗"),
		CHF("331", "瑞士法郎"),
		JCHF("398", "记帐瑞士法郎"),
		CAD("501", "加拿大元"),
		USD("502", "美元"),
		AUD("601", "澳大利亚元"),
		NZD("609", "新西兰元");
		
		public String code;
		public String name;
		
		private Currency(String code, String name) {
			this.code = code;
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	//运输方式代码
	public enum TransF{
		UN_BA				("0", 		"非保税区"),
		MONITOR_WH			("1",		"监管仓库"),
		JH_TRANS			("2",		"江海运输"),
		RAIL_TRANS			("3",		"铁路运输"),
		AM_TRANS			("4",		"汽车运输"),
		AIR_TRANS			("5",		"航空运输"),
		MAIL_TRANS			("6",		"邮件运输"),
		BONDED_AREA		("7",		"保税区"),
		BA_WH				("8",		"保税仓库"),
		OTHER_TRANS		("9",		"其它运输"),
		EXPORT				("Z",		"出口加工"),
		ALL_TRANS			("A",		"全部运输"),
		LOGISTICS_CENTER	("W",		"物流中心"),
		LOGISTICS_ZONE		("X",		"物流园区"),
		BONDED_PORT		("Y",		"保税港区");

		public String code;
		public String desc;
		private TransF(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getDesc() {
			return desc;
		}
		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
}

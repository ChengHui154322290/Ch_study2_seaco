package com.tp.common.vo.customs;

import com.tp.common.vo.OrderConstant.ClearanceStatus;
import com.tp.common.vo.bse.ClearanceChannelsEnum;

public class JKFConstant {
	
	/** 支付多久后订单或者个人物品申报单推送海关 **/
	public static final int UNDECLARED_AFTER_PAYED_MINUTE_DEFAULT = 60;
	
	/** 推送单单页数量 **/
	public static final int JKF_PAGE_SIZE = 50;
	
	/** 电子口岸代码 **/
	public static final String JKF_CUSTOMS_CODE = "JKF";
	
	/** 通关渠道Code（杭州保税区） **/
	public static final String BONDED_AREA_CODE = ClearanceChannelsEnum.HANGZHOU.name();	
	
	/**
	 *	约定错误类型及说明 
	 */
	public enum JKFResultError{		
		INVALID_XML					("S01", 	"非法的XML格式"),
		INVALID_SIGN					("S02", 	"非法的数字签名"),
		INVALID_EXPRESS_ENTERPRISE	("S03", 	"非法的物流公司"),
		INVALID_BUSINESS_TYPE			("S04", 	"非法的通知类型"),
		INVALID_CONTENT				("S05", 	"非法的通知内空"),
		SERVER_REQUEST_TIMEOUT		("S06", 	"服务器请求超时，目标主机不可达请重试"),
		SERVER_RESPONSE_TIMEOUT		("S17", 	"服务器响应超时，请重试"),
		SYSTEM_EXCEPTION				("S07", 	"系统异常，请重试"),
		HTTP_ERROR_CODE				("S08", 	"HTTP状态异常(非200)"),
		RESPONSE_CONTENT_NULL			("S09", 	"返回报文为空"),
		ERROR_GATEWAY_INFO			("S10", 	"找不到对应的网关信息"),
		INVALID_GATEWAY_INFO			("S11", 	"非法的网关信息"),
		INVALID_REQUEST_PARAM			("S12", 	"非法的请求参数"),
		BUSINESS_SERVER_EXCEPTION	("S13", 	"业务服务异常"),
		SYSTEM_TRAFFIC_CONTROL		("S14", 	"系统流控"),
		CALLBACK_RESPONSE_UNSERIABLE	("S15", 	"回调响应业务层返回值无法序列化"),
		GATEWAY_EXCEPTION				("S16", 	"网关业务通道层系统异常"),
		BUSINESS_SYSTEM_EXCEPTION	("S18", 	"业务系统异常"),
		B_UNKNOWN_BUSINESS_TYPE		("B0001",	"未知业务错误"),
		B_PARAM_NULL					("B0002",	"必选参数为空"),
		B_MAX_BATCH_COUNT				("B3100",	"批量操作不能超过20条"),
		B_INVALID_TIME_FORMAT			("B3101", 	"时间格式错误");

		
		public String code;
		public String name;
		private JKFResultError(String code, String name) {
			this.code = code;
			this.name = name;
		}
		
		public static JKFResultError getJKFErrorbyCode(String code){
			for (JKFResultError error : JKFResultError.values()) {
				if (error.code.equals(code)) {
					return error;
				}
			}
			return null;
		}
		
		public static String getCnName(String code){
			for (JKFResultError error : JKFResultError.values()) {
				if (error.code.equals(code)) {
					return error.name;
				}
			}
			return null;
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
	
	/**
	 * 回执类型 
	 */
	public enum JKFFeedbackType{
		CUSTOMS_DECLARE_RESULT_CALLBACK	("CUSTOMS_DECLARE_RESULT_CALLBACK","跨境电商平台回执报文"),
		CUSTOMS_DECLARE_GOODS_CALLBACK	("CUSTOMS_DECLARE_GOODS_CALLBACK", "个人物品申报单审单结果"),
		CUSTOMS_BILL_CALLBACK				("CUSTOMS_BILL_CALLBACK", "进口运单出区回执"),
		CUSTOMS_TAX_CALLBACK				("CUSTOMS_TAX_CALLBACK", "税款回传"),
		PRODUCT_RECORD						("PRODUCT_RECORD", "产品备案信息"),
		CUSTOMS_CEB_CALLBACK				("CUSTOMS_CEB_CALLBACK","总署版回执"),
		CUSTOMS_CEB_CALLBACK_ORDER		("CUSTOMS_CEB_CALLBACK_ORDER","总署版订单回执"),
		CUSTOMS_CEB_CALLBACK_GOODS		("CUSTOMS_CEB_CALLBACK_GOODS","总署版清单回执");
		
		public String type;
		public String name;
		private JKFFeedbackType(String type, String name){
			this.type = type;
			this.name = name;
		}
		
		public static JKFFeedbackType getTypeByType(String type){
			for(JKFFeedbackType feedbackType : JKFFeedbackType.values()){
				if(feedbackType.type.equals(type)){
					return feedbackType;
				}
			}
			return null;
		}
		
		public static String getCnName(String type){
			for (JKFFeedbackType feedback : JKFFeedbackType.values()) {
				if (feedback.type.equals(type)) {
					return feedback.name;
				}
			}
			return null;
		}
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
	/**
	 *	业务类型 
	 */
	public enum JKFBusinessType{
		
		IMPORTORDER				("IMPORTORDER", 			"申报商品订单"),
		IMPORT_COMPANY				("IMPORT_COMPANY", 			"进口企业备案"),
		PERSONAL_GOODS_DECLAR		("PERSONAL_GOODS_DECLAR", 	"个人物品申报单"),
		RESULT						("RESULT", 					"回执"),
		CHECKRESULT				("CHECKRESULT", 			"进口企业备案回执"),
		IMPORTBILLRESULT			("IMPORTBILLRESULT", 		"进口运单出区回执"),
		TAXISNEED					("TAXISNEED", 				"税款是否应征信息发送企业"),
		IMPORT_ORDER_RETURN		("IMPORT_ORDER_RETURN", 	"退单"),
		MODIFY_CANCEL				("MODIFY_CANCEL", 			"企业删单");
		
		public String type;
		public String name;
		private JKFBusinessType(String type, String name){
			this.type = type;
			this.name = name;
		}
		
		public static JKFBusinessType  getBusinessTypeByType(String type){
			for(JKFBusinessType businessType : JKFBusinessType.values()){
				if(businessType.type.equalsIgnoreCase(type)){
					return businessType;
				}
			}
			return null;
		}
		
		public static String getCnName(String type){
			for (JKFBusinessType business : JKFBusinessType.values()) {
				if (business.type.equals(type)) {
					return business.name;
				}
			}
			return null;
		}
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}		
	}
	
	/**
	 *	回执结果 
	 */
	public enum CheckResult{
		SUCCESS	(1, "处理成功"),
		FAILED(2, "处理失败");
		
		public Integer result;
		public String desc;
		
		private CheckResult(Integer result, String desc) {
			this.result = result;
			this.desc = desc;
		}

		public static boolean isSuccess(Integer result){
			if(SUCCESS.result.equals(result)) {
				return true;
			}
			return false;
		}
		
		public static String getCnDesc(Integer result){
			for (CheckResult cr : CheckResult.values()) {
				if (cr.result.equals(result)) {
					return cr.desc;
				}
			}
			return null;
		}
		
		public Integer getResult() {
			return result;
		}

		public void setResult(Integer result) {
			this.result = result;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}	
	}
	
	public enum ImportType{
		
		IMPORT_ZY(0, "直邮进口"),
		IMPORT_BA(1, "保税区进口");
		
		public Integer type;
		public String desc;
		
		private ImportType(Integer type, String desc) {
			this.type = type;
			this.desc = desc;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
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
		public String name;
		private TransF(String code, String name) {
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
	
	/** 个人物品申报海关审核状态 **/
	public enum GoodsDeclareStatus{
		DECL_UNACCEPT			("32",	"不接受申报"),		//退单,可以重复申报
		ELEC_DECL_ACCEPT		("99",	"电子审单通过"),
		ELEC_DECL_RED			("33",	"电子审单进入红通道"),
		ELEC_DECL_GREEN		("34",	"电子审单进入绿通道"),
		ORDER_REFUSE			("42",	"接单拒绝"),
		DECL_CONCEL			("80",	"申报单撤销"),
		DECL_AUTO_PASS			("51",	"流水线自动放行"),
		DECL_DIRECT_PASS		("53",	"直接放行"),
		DECL_MANUAL_PASS		("52",	"手工放行"),
		DECL_WAIT_M4			("M4",	"待过机"),
		DECL_WAIT_M5			("M5",	"待过机"),
		DECL_WAIT_M6			("M6",	"待过机"),
		DECL_NI_ACCEPT			("N1",	"国检接收申报"),	//国检暂时没有
		DECL_NI_PASS			("N2",	"国检放行"),
		DECL_NI_UNPASS			("N3",	"国检审批不通过");
		
		public String code;
		public String name;
		
		private GoodsDeclareStatus(String code, String name) {
			this.code = code;
			this.name = name;
		}
		
		public static ClearanceStatus checkResult(String code){
			if(isReject(code)) return ClearanceStatus.AUDIT_FAILED;
			if(isPass(code)) return ClearanceStatus.AUDIT_SUCCESS;
			return null;
		}
		
		//是否拒单
		public static boolean isReject(String code){
			if (DECL_UNACCEPT.code.equals(code)) return true;
			return false;
		}
		
		//是否放行
		public static boolean isPass(String code){
			if(DECL_AUTO_PASS.code.equals(code) || 
					DECL_DIRECT_PASS.code.equals(code) || 
					DECL_MANUAL_PASS.code.equals(code)){
				return true;
			}
			return false;
		}
		
		public static String getNameByCode(String code){
			for (GoodsDeclareStatus status : GoodsDeclareStatus.values()) {
				if (status.code.equals(code)) {
					return status.name;
				}
			}
			return null;
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
	
	/**
	 * 回执数据类型
	 */
	public enum ApprovalType{
		PLATFORM	(0, 	"跨境电商通关平台回执数据"),
		CUSTOMS		(1,		"海关回执数据");
		
		public Integer type;
		public String 	name;
		
		private ApprovalType(Integer type, String name) {
			this.type = type;
			this.name = name;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}		
	}
	
	/**
	 *	日志类型 
	 */
	public enum LogType{
		DECLARE				(1, 	"申报"),
		CALLBACK			(2,		"回执"),
		STATUS_MODIFY		(3,		"状态修改");
		
		public Integer type;
		public String name;
		
		private LogType(Integer type, String name) {
			this.type = type;
			this.name = name;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	
	//海关审批删单状态
	public enum CancelOrderAuditStatus{
		SUCCESS("21", "成功"),
		FAILED("22", "失败");
		
		public String code;
		public String desc;
		
		private CancelOrderAuditStatus(String code, String desc) {
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
	
	public enum CebFlag{
		DECLARE_HZ("01", "申报杭州"),
		DECLARE_ZS_SELF("02", "企业自行对接"),
		DECLARE_ZS_ZJPORT("03", "企业应用浙江电子口岸接口对接总署");
		
		public String flag;
		public String desc;
		
		private CebFlag(String flag, String desc) {
			this.flag = flag;
			this.desc = desc;
		}

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
		
	}
	
	public enum CebClearanceStatus{
		CEB_TEMPSAVE(1, "电子口岸已暂存"),
		CEB_DECLARING(2, "电子口岸申报中"),
		CEB_SENDSUCCESS(3, "发送海关成功"),
		CEB_SENDFAIL(4, "发送海关失败"),
		CEB_UNDECLARE(100, "海关退单"),
		CEB_SAVE(120, "海关入库"),
		CEB_MANUALAUDIT(300, "人工审核"),
		CEB_CUSTOMSCONCLUSION(399, "海关审结"),
		CEB_ACCEPT(800, "海关放行"),
		CEB_CONCLUSION(899, "结关"),
		CEB_CHECK(500, "查验"),
		CEB_DETAIN_1(501, "扣留移送通关"),
		CEB_DETAIN_2(502, "扣留移送缉私"),
		CEB_DETAIN_3(503, "扣留移送法规"),
		CEB_DETAIN_OTHER(509, "其他扣留");
		
		public Integer code;
		public String desc;
		
		public static ClearanceStatus checkClearanceAudit(Integer code){
			if (isPushFail(code)) return ClearanceStatus.PUT_FAILED;
			if (isReject(code)) return ClearanceStatus.AUDIT_FAILED;
			if (isAccept(code)) return ClearanceStatus.AUDIT_SUCCESS;
			return null;
		}
		
		public static boolean isPushSuccess(Integer code){
			if (CEB_SAVE.code.equals(code)) return true;
			return false;
		}
		
		public static boolean isPushFail(Integer code){
			if (code < 0) return true;
			if (CEB_SENDFAIL.code.equals(code)) return true;
			return false;
		}
		
		public static boolean isReject(Integer code){
			if (CEB_UNDECLARE.code.equals(code)) return true;
			return false;
		}
		
		public static boolean isAccept(Integer code){
			if (CEB_ACCEPT.code.equals(code)) return true;
			return false;
		}
		
		
		public static String getDescByCode(Integer code){
			for(CebClearanceStatus status : CebClearanceStatus.values()){
				if (status.getCode().equals(code)) return status.getDesc();
			}
			return null;
		}
		private CebClearanceStatus(Integer code, String desc){
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
}

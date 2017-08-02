package com.tp.common.vo.prd;

/**
 * 
 * <pre>
 *   商品相关的一些参数
 * </pre>
 *
 * @author szy
 * @version 0.0.1
 */
public class ItemConstant {
	
	/**小类编码的长度 */
	public final static int SMALL_CODE_LENGTH=6;
	
	/**SPU编码的长度 */
	public final static int SPU_CODE_LENGTH=10;
	
	/**prdid编码的长度 */
	public final static int PRDID_CODE_LENGTH = 12;

	/**SKU编码的长度 */
	public final static int SKU_CODE_LENGTH = 14;
	
	/**生产spu，prdid，sku编码 初始化的值1 */
	public final static int CODE_INIT_VALUE=1;
	
	/**基础数据是否有效 :0-失效 ,1-有效,2-全部*/
	public final static int INVALID_DATAS =0;
	public final static int EFFECTIVE_DATAS =1;
	public final static int ALL_DATAS =2;
	
	/**运费模板，1为单品团*/
	public final static int SINGLE_FREIGHTTEMPLATE_STATUS = 1;
	
	/**PRDID 有西客商城商家**/
	public final static int HAS_XIGOU_SELLER = 1;
	
	/** 规格均码处理 */
	public final static Long FREE_SIZE_ID = -1l;
	public final static String FREE_SIZE_NAME = "*";
	
	/** prdid 显示是否上架 */
	public final static String ON_SALES_STR = "已上架";
	public final static String OFF_SALES_STR = "未上架";
	
	/** 商品属性来源 ，0-来自基础数据里面的设定，1-商品部门自定义 2 服务商品属性*/
	public final static int ATTR_FROM_BASEDATA=0;
	public final static int ATTR_CUSTOM = 1;
	public final static int ATTR_DUMMY=2;//服务商品
	public final static int ATTR_SELECTED = 1;
	
	/** 默认分割符合 */
	public final static String DEFAULT_SEPARATOR = ",";
	public final static String DEFAULT_JOIN = "-";
	
	/***自营西客商城商家*/
	public final static long SUPPLIER_ID = 0;
	public final static String SUPPLIER_NAME = "西客商城";
	
	/**分类的level*/
	public final static int  CATEGORY_LARGE_LEVEL = 1;
	public final static int  CATEGORY_MEDIUM_LEVEL = 2;
	public final static int  CATEGORY_SMALL_LEVEL = 3;
	
	/**商品服务消息队列名称 **/
	public final static String ITEM_INFO_PUB_MSG = "item_info_queue_topic";
	public final static String ITEM_DETAIL_PUB_MSG = "item_detail_queue_topic";
	public final static String ITEM_SKU_PUB_MSG = "item_sku_queue_topic";
	public final static String ITEM_PROMOTION_PUB_MSG = "item_promotion_sku_queue_p2p";
	
	/***海淘商品标示符**/
	public final static int  HAI_TAO = 2;
	
	/***无法获取**/
	public final static String  CAN_NOT_GET = "CAN_NOT_GET";
	
	/***更具prd批量插入销售信息 返回标示**/
	public final static String  ITEM_DETAIL_SALES_SUCCESS = "success";

	/***更具prd批量插入销售信息 返回标示 数据校验不匹配**/
	public final static String  ITEM_DETAIL_SALES_NOT_MATCH = "notmatch";
	
	/**
	 * 佣金类型 普通商品 传A  进口红酒 传B
	 * @author szy
	 *
	 */
	public enum COMMISION_TYPE{
		COMMON(1,"A","普通商品"),
		IMPORT_WINES(2,"B","进口红酒"),
		TOPIC(3,"C","活动商品");
		public Integer type;
		public String code;
		public String cnName;
		
		COMMISION_TYPE(Integer type,String code,String cnName){
			this.type = type;
			this.code = code;
			this.cnName = cnName;
		}
		
		public static String getCnName(Integer type){
			for(COMMISION_TYPE entry:COMMISION_TYPE.values()){
				if(entry.type.equals(type)){
					return entry.cnName;
				}
			}
			return null;
		}
		
		public static String getCode(Integer type){
			for(COMMISION_TYPE entry:COMMISION_TYPE.values()){
				if(entry.type.equals(type)){
					return entry.code;
				}
			}
			return null;
		}

		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getCnName() {
			return cnName;
		}

		public void setCnName(String cnName) {
			this.cnName = cnName;
		}
	}
}

package com.tp.common.vo.mem;

public enum MemberUnionType {
	// 微信H5
	WEIXIN(1),
	// 微信APP
	WEIXIN_UN(2),
	// QQ
	QQ(3),
	/**民生银行*/
	CMBC(4);
	
	public Integer code;
	
	private MemberUnionType(Integer code) {
		this.code = code;
	}
	
	public static MemberUnionType getByCode(Integer code){
		for(MemberUnionType c : MemberUnionType.values()){
			if(code == c.code) return c;
		}
		return null;
	}
}

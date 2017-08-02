package com.tp.common.vo.mem;

public interface UriConstant {
	
	public interface USER {
		String SPACE = "/user";
	}
	
	public interface CONSIGNEEADDRESS {
		String SPACE = USER.SPACE + "/consigneeAddress";
	}
	
	public interface MAIL {
		String SPACE = USER.SPACE + "/mail";
	}
	
	public interface MY_ATTENTION {
		String SPACE = USER.SPACE + "/myAttention";
	}
	
	public interface COUPON {
		String SPACE = USER.SPACE + "/coupon";
	}
	
	public interface MY_RETURNGOODS {
		String SPACE = USER.SPACE + "/myReturnGoods";
	}
	
	public interface MY_INFO {
		String SPACE = USER.SPACE + "/myInfo";
	}
	
	public interface MY_ORDER {
		String SPACE = USER.SPACE + "/myOrder";
	}
	
	public interface USER_AUTHENTICATION {
		String SPACE = USER.SPACE + "/userAuthentication";
	}
	
	public interface MY_COUPON {
		String SPACE = USER.SPACE + "/myCoupon";
	}
	
	public interface MY_REDPACK {
		String SPACE = USER.SPACE + "/myRedPack";
	}
	
	public interface USER_CENTER {
		String SPACE = USER.SPACE + "/userCenter";
	}
	
	public interface FILE {
		String SPACE = USER.SPACE + "/file";
	}
	
	public interface DA_REN {
		String SPACE = USER.SPACE + "/daren";
	}
	
	public interface REMOTE {
		String SPACE = USER.SPACE + "/remote";
	}
	
	public interface MYCOLLECT {
		String SPACE = USER.SPACE + "/myCollect";
	}
	
	public interface COMMONS {
		String SPACE =  "/commons";
	}
	
	public interface VALIDATE {
		String SPACE =  "/validate";
	}
	
	public interface TEST {
		String SPACE = "/test";
	}
	
	public interface SSO {
		String SPACE = USER.SPACE + "/sso";
	}
}

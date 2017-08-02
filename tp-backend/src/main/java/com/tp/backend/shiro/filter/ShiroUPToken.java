package com.tp.backend.shiro.filter;


import org.apache.shiro.authc.UsernamePasswordToken;

/**
 */
public class ShiroUPToken extends UsernamePasswordToken {
	
    private static final long serialVersionUID = 1L;

    private String captcha;
    public String getCaptcha() {
        return captcha;
    }
    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String userType;
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public ShiroUPToken() {
        super();
    }
    
    public ShiroUPToken(String loginName, String password) {
        super(loginName, password);
    }

    public ShiroUPToken(String loginName, char[] password,boolean rememberMe, String host, String captcha,String userType) {
        super(loginName, password, rememberMe, host);
        this.captcha = captcha;
        this.userType = userType;
    }

}

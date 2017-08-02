package com.tp.proxy.usr;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;

import com.tp.model.usr.UserInfo;

/**
 * <p>User: szy
 */
@Service
public class PasswordHelper {

    private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();
    private static String algorithmName = "md5";
    private static Integer hashIterations = 2;
    static{
    	/**
    	PropertiesFactoryBean settings = (PropertiesFactoryBean)SpringBeanProxy.getBean("settings");
    	if(settings!=null){
    		try {
				Object algorithmNameObject = settings.getObject().getProperty("password.algorithmName");
				if(algorithmNameObject!=null){
					algorithmName = algorithmNameObject.toString();
				}
				Object hashIterationsObject =  settings.getObject().getProperty("password.hashIterations");
				if(hashIterationsObject!=null){
					hashIterations = Integer.valueOf(hashIterationsObject.toString());
				}
			} catch (IOException e) {
			}
    	}
    	*/
    }

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    public void encryptPassword(UserInfo user) {

        user.setSalt(randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(
                algorithmName,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                hashIterations).toHex();

        user.setPassword(newPassword);
    }
    
    public String getPass(String pass,UserInfo user){
    	 String newPassword = new SimpleHash(
                 algorithmName,
                 pass,
                 ByteSource.Util.bytes(user.getCredentialsSalt()),
                 hashIterations).toHex();
    	 
    	return newPassword;
    	
    }
    
    public static boolean checkPassWord(String password){
    	String regex = "(?=.*[a-zA-Z])(?=.*[0-9])(?=.*?[-!\\(\\)\\~@#$%\\^\\&\\*_\\+\\-\\=])[a-zA-Z0-9-!\\(\\)\\~@#$%\\^\\&\\*_\\+\\-\\=]{10,20}";
    	return password.matches(regex);
    }
}

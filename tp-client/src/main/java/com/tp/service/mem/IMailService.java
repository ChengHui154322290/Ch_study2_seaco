package com.tp.service.mem;

import com.tp.exception.UserServiceException;

/**
 * 
 * @author szy
 *
 */
public interface IMailService {

	String WRAP = "<br>";
	String SENDER_NAME = "Seagoor";//"西客商城";
	/**
	 * Send mail.
	 * @param receiver
	 * @param toAddr
	 * @param title
	 * @param body
	 * @return
	 * @throws Exception
	 */
	boolean send(String receiver, String toAddr, 
			String title, String body) throws UserServiceException;
	
	/**
	 * Generate url for validation email.
	 * @param uid
	 * @param token
	 * @return
	 */
	String getBindEmailUrl(String uid, String email, String token) throws UserServiceException;

	Boolean batchSend(String[] emails, String mailNotifyTitleFg, String string);
	
}

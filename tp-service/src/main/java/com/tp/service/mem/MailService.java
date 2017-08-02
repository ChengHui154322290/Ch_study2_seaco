package com.tp.service.mem;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.tp.exception.UserServiceException;
import com.tp.service.mem.IMailService;


/**
 * 
 * @author szy
 *
 */
@Service
public class MailService implements IMailService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JavaMailSenderImpl mailSender;
	
	
	public boolean send(String nickName, String toAddr, String title,
			String body) throws UserServiceException{
		try {
			validateEmail(toAddr);
			String subject = getEmailHead(nickName, title);
			send(subject, body, SENDER_NAME, nickName, toAddr);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}

		return true;
	}

	private boolean validateEmail(String email) throws UserServiceException {
		boolean isValid = false;
		try {
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			isValid = true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
		return isValid;
	}

	private void send(String subject, String body, String senderName,
			String nickName, String toMail) throws UserServiceException{
		try {
			MimeMessage mailMessage = mailSender.createMimeMessage();
			mailMessage.setContent(body, "text/html;charset=utf-8");
			String sender = javax.mail.internet.MimeUtility.encodeText(senderName, "utf-8", "B");
			MimeMessageHelper messageHelper = new MimeMessageHelper(
					mailMessage, true, "utf-8");
			messageHelper.setFrom(mailSender.getUsername(), sender);
			messageHelper.setTo(toMail);
			messageHelper
					.setFrom(new InternetAddress(sender + "<" + mailSender.getUsername() + ">"));
			messageHelper.setSubject(subject);
			messageHelper.setText(body, true);
			// send
			mailSender.send(mailMessage);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
	}

	private String getEmailHead(String userName, String content) throws UserServiceException{
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(userName).append(content);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
		return sb.toString();
	}

	public String getBindEmailUrl(String uid, String email, String token) throws UserServiceException{
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("/user/bindEmail").append("?").append("uid=")
					.append(uid).append("&email=").append(email)
					.append("&token=").append(token);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new UserServiceException(e.getMessage());
		}
		return sb.toString();
	}

	@Override
	public Boolean batchSend(String[] emails, String title,
			String body) {
		if (null == emails || emails.length == 0) {
			return false;
		}
		String subject = title;
		for (String mailAddr : emails) {
			validateEmail(mailAddr);
			try{
				send(SENDER_NAME,mailAddr,subject, body);
			}catch(Exception e){
				
			}
		}
		return false;
	}
}

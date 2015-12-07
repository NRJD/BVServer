/**
 * 
 */
package org.nrjd.bv.server.util;

import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_FROM_ID;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

;
/**
 * @author Sathya
 * 
 */
public class EmailUtil {

	/**
	 * 
	 * @return
	 */
	private static Session getGMailSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.socketFactory.port", "465"); // SSL Port
		props.put("mail.smtp.socketFactory.class",
		        "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
		props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
		props.put("mail.smtp.port", "465"); // SMTP Port

		Session session = Session.getInstance(props,
		        new javax.mail.Authenticator() {
			        @Override
			        protected PasswordAuthentication getPasswordAuthentication() {
				        return new PasswordAuthentication(
				                "bvtestsathya@gmail.com", "jpsgurudev");
			        }
		        });

		return session;
	}

	public static void sendEmail(String emailId) {

		Session session = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");

			session = getGMailSession();
			if (session == null) {
				session = (Session) envCtx.lookup("mail/Session");
			}
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(EMAIL_FROM_ID));
			InternetAddress to[] = new InternetAddress[1];
			to[0] = new InternetAddress(emailId);
			message.setRecipients(Message.RecipientType.TO, to);
			message.setSubject(EMAIL_SUBJECT);
			message.setContent(
			        "Please Verify your email ID by clicking the below link",
			        "text/html;charset=UTF-8");
			Transport.send(message);

		}
		catch (AddressException e) {
			e.printStackTrace();
		}
		catch (MessagingException e) {
			e.printStackTrace();
		}
		catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

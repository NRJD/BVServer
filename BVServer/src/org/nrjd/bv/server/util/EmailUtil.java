/**
 * 
 */
package org.nrjd.bv.server.util;

import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_ACCT_ID;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_ACCT_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_CONTEXT;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_GMAIL_HOST;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_GMAIL_PORT;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SESSION;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_VER_EMAIL;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_EMAIL_ID;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_VERIF_CODE;

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

import org.nrjd.bv.server.dto.ServerRequest;

;
/**
 * @author Sathya
 * 
 */
public class EmailUtil {

	private static Session	session;

	/**
	 * Gets the HTML basic tags for Header
	 * 
	 * @return
	 */
	private static String getBodyHeader() {
		StringBuffer buf = new StringBuffer();
		buf.append("<html><head><meta charset=\"ISO-8859-1\"></head><body><h4>Hare Krishna Devotee,</h4><br>");
		buf.append("All Glories To Srila Prabhupada.</br>All Glories To Sri Guru and Gauranga.</br></br>");
		return buf.toString();
	}

	/**
	 * Gets the HTML basic tags for Trailer
	 * 
	 * @return
	 */
	private static String getBodyTrailer() {
		StringBuffer buf = new StringBuffer();
		buf.append("<b>Please Chant Hare Krsna Maha Mantra and Be Happy,</b><br>Bhakti Vriksha Team</body></html>");
		return buf.toString();
	}

	/**
	 * 
	 * @return
	 */
	private static Session getGMailSession() {

		if (session == null) {

			Properties props = new Properties();
			props.put("mail.smtp.host", EMAIL_GMAIL_HOST); // SMTP Host
			props.put("mail.smtp.socketFactory.port", EMAIL_GMAIL_PORT); // SSL
			                                                             // Port
			props.put("mail.smtp.socketFactory.class",
			        "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
			props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
			props.put("mail.smtp.port", EMAIL_GMAIL_PORT); // SMTP Port

			session = Session.getInstance(props,
			        new javax.mail.Authenticator() {
				        @Override
				        protected PasswordAuthentication getPasswordAuthentication() {
					        return new PasswordAuthentication(EMAIL_ACCT_ID,
					                EMAIL_ACCT_PWD);
				        }
			        });
		}
		return session;
	}

	/**
	 * 
	 * @param email
	 * @param emailVerifCode
	 * @param mobileVerifCode
	 * @return
	 */
	public static String getVerifEmailBody(String toEmailId,
	        String emailVerifCode, String mobileVerifCode) {

		StringBuffer buf = new StringBuffer();
		buf.append(getBodyHeader());
		buf.append("Thanks for showing interest to experience spiritual bliss and gain the <b>Ultimate Knowledge.</b></br></br>");
		buf.append("<p>Please ");
		buf.append("<a href=\"http://localhost:8011/BVServer/VerifyEmail.jsp?");
		buf.append(KEY_VERIF_CODE).append("=").append(emailVerifCode);
		buf.append("&").append(KEY_EMAIL_ID).append("=").append(toEmailId);
		buf.append("\">click Here</a>");
		buf.append(" to activate your account.</p>");
		buf.append("<h5>OR</h5><p>You can also verify your email address thru Bhakti Vriksha application in your mobile by entering the 6 digit code: ");
		buf.append("<b>").append(mobileVerifCode).append("</b></p>");
		buf.append(getBodyTrailer());

		return buf.toString();
	}

	/**
	 * 
	 * @param email
	 * @param emailVerifCode
	 * @param mobileVerifCode
	 * @return
	 */
	public static String getWelcomeEmailBody() {

		StringBuffer buf = new StringBuffer();
		buf.append(getBodyHeader());
		buf.append("Welcome to BV App to experience spiritual bliss and gain the <b>Ultimate Knowledge.</b></br></br>");
		buf.append("<p>Please use the BV app from your smart phone to enjoy reading the BV manual at your ease</p>");
		buf.append(
		        "<p>If you have any questions or feedback please email to <b>")
		        .append(EMAIL_ACCT_ID).append("</b> </p></br></br>");
		buf.append(getBodyTrailer());

		return buf.toString();
	}

	/**
	 * 
	 * @param emailId
	 */
	public static void sendEmail(ServerRequest srvrReq, String subject) {

		Session session = null;
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup(EMAIL_CONTEXT);

			session = getGMailSession();
			if (session == null) {
				session = (Session) envCtx.lookup(EMAIL_SESSION);
			}
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(EMAIL_ACCT_ID));
			InternetAddress to[] = new InternetAddress[1];
			to[0] = new InternetAddress(srvrReq.getEmailId());
			message.setRecipients(Message.RecipientType.TO, to);
			message.setSubject(subject);

			String body = subject == EMAIL_SUBJECT_VER_EMAIL ? getVerifEmailBody(
			        srvrReq.getEmailId(), srvrReq.getEmailVerifCode(),
			        srvrReq.getMobileVerifCode()) : getWelcomeEmailBody();

			message.setContent(body, "text/html;charset=UTF-8");

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

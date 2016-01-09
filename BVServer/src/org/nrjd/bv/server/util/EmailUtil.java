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
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_PWD_RESET;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_VER_EMAIL;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_WELCOME;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUPPORT;
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

	private Session	session;

	/**
	 * Gets the HTML basic tags for Header
	 * 
	 * @return
	 */
	private String getBodyHeader(String name) {
		StringBuffer buf = new StringBuffer();
		buf.append("<html><head><meta charset=\"ISO-8859-1\">");
		buf.append("</head><body><h4>Dear ").append(
		        name != null ? name : "Devotee");
		buf.append("</h4> Hare Krishna,");
		buf.append("<p>Please accept our respectful obeisances.<br></br>");
		buf.append("All Glories To Srila Prabhupada.<br></br>All Glories To Sri Guru and Gauranga.</p><br></br>");
		return buf.toString();
	}

	/**
	 * Gets the HTML basic tags for Trailer
	 * 
	 * @return
	 */
	private String getBodyTrailer() {
		StringBuffer buf = new StringBuffer();
		buf.append("<br></br><p><b>Please Chant Hare Krsna Maha Mantra and Be Happy,</b><br>Your Humble Servant,<br></br>Bhakti Vriksha Team</p></body></html>");
		return buf.toString();
	}

	/**
	 * 
	 * @return
	 */
	private Session getGMailSession() {

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
	 * Email body for the Pasword Reset Email
	 * 
	 * @param email
	 * @param emailVerifCode
	 * @param mobileVerifCode
	 * @return
	 */
	private String getPwdResetEmailBody(ServerRequest srvrReq) {

		StringBuffer buf = new StringBuffer();
		buf.append(getBodyHeader(srvrReq.getName()));
		buf.append("<p>This email has been sent to you as per your request to reset your Bhakthi Vriksha Mobile App User Account password.");
		buf.append("In order to complete the password reset process, you must enter the temporary password :");
		buf.append("<b>").append(srvrReq.getTempPwd()).append("</b>");
		buf.append(" from your Bhakthi Vriksha Mobile App.</p><br></br><br></br>");
		buf.append(
		        "<b>Note:</b><br></br>If you did not request a reset password, please email to:<b>")
		        .append(EMAIL_SUPPORT).append("</b>");
		buf.append(getBodyTrailer());

		return buf.toString();
	}

	/**
	 * Email body for the Subscription Email
	 * 
	 * @param email
	 * @param emailVerifCode
	 * @param mobileVerifCode
	 * @return
	 */
	private String getVerifEmailBody(ServerRequest srvrReq) {

		StringBuffer buf = new StringBuffer();
		buf.append(getBodyHeader(srvrReq.getName()));
		buf.append("<br>Thanks for showing interest to experience spiritual bliss and gain the <b>Ultimate Knowledge.</b></br>");
		buf.append("<p>Please ");
		buf.append("<a href=\"http://localhost:8011/BVServer/VerifyEmail.jsp?");
		buf.append(KEY_VERIF_CODE).append("=")
		        .append(srvrReq.getEmailVerifCode());
		buf.append("&").append(KEY_EMAIL_ID).append("=")
		        .append(srvrReq.getEmailId());
		buf.append("\">click Here</a>");
		buf.append(" to activate your account.</p>");
		buf.append("<h5>OR</h5><p>You can also verify your email address thru Bhakti Vriksha application in your mobile by entering the 6 digit code: ");
		buf.append("<b>").append(srvrReq.getMobileVerifCode())
		        .append("</b></p>");
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
	public String getWelcomeEmailBody(ServerRequest srverReq) {

		StringBuffer buf = new StringBuffer();
		buf.append(getBodyHeader(srverReq.getName()));
		buf.append("<br></br><p>Welcome to BV App to experience spiritual bliss and gain the <b>Ultimate Knowledge.</b></p>");
		buf.append("<br></br<p>Please use the BV app from your smart phone to enjoy reading the BV manual at your ease</p><br></br>");
		buf.append(
		        "<p>If you have any questions or feedback please email to <b>")
		        .append(EMAIL_SUPPORT).append("</b> </p><br></br>");
		buf.append(getBodyTrailer());

		return buf.toString();
	}

	/**
	 * 
	 * @param emailId
	 */
	public void sendEmail(ServerRequest srvrReq, String subject) {

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

			String body = null;

			if (subject == EMAIL_SUBJECT_VER_EMAIL) {
				body = getVerifEmailBody(srvrReq);
			}
			else if (subject == EMAIL_SUBJECT_WELCOME) {
				body = getWelcomeEmailBody(srvrReq);
			}
			else if (subject == EMAIL_SUBJECT_PWD_RESET) {
				body = getPwdResetEmailBody(srvrReq);
			}

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

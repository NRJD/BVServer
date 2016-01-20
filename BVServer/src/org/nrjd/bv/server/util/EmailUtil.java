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
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_ACC_ACTIVATION;
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

import org.nrjd.bv.server.dto.ServerConstant;
import org.nrjd.bv.server.dto.ServerRequest;

;
/**
 * @author Sathya
 * 
 */
public class EmailUtil {
	private static final String NEW_LINE = "<br/>";
	private static final String INSERT_ONE_BLANK_LINE = NEW_LINE + NEW_LINE;
	private static final String INSERT_TWO_BLANK_LINES = NEW_LINE + NEW_LINE + NEW_LINE;
	
	private Session	session;

	/**
	 * Gets the HTML basic tags for Header
	 * 
	 * @return
	 */
	private String getBodyHeader(String name) {
		StringBuffer buf = new StringBuffer();
		buf.append("<html><head><meta charset=\"ISO-8859-1\"></head><body>");
		if(name != null) {
			buf.append("Dear ").append(name).append(",").append(INSERT_ONE_BLANK_LINE);
		}
		buf.append("Hare Krishna!");
		buf.append(NEW_LINE).append("Please accept our respectful obeisances.");
		buf.append(NEW_LINE).append("All glories to Srila Prabhupada!");
		buf.append(NEW_LINE).append("All glories to Sri Sri Guru and Gauranga!");
		return buf.toString();
	}

	/**
	 * Gets the HTML basic tags for Trailer
	 * 
	 * @return
	 */
	private String getBodyTrailer() {
		StringBuffer buf = new StringBuffer();
		buf.append("For any technical support, questions or feedback, please write to " + ServerConstant.EMAIL_SUPPORT);		
		buf.append(INSERT_ONE_BLANK_LINE).append("Please Chant <b>Hare Krishna Maha Mantra</b> and Be Happy!");
		buf.append(INSERT_ONE_BLANK_LINE).append("Your Humble Servant,");
		buf.append(NEW_LINE).append("Bhakti Vriksha Team");
		buf.append("</body></html>");
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
		buf.append(INSERT_ONE_BLANK_LINE);
		buf.append("This email has been sent to you as per your request to reset your Bhakthi Vriksha app user account password.");
		buf.append(INSERT_ONE_BLANK_LINE);
		buf.append("In order to complete the password reset process, you must enter the temporary password: ");
		buf.append("<b>").append(srvrReq.getTempPwd()).append("</b>");
		buf.append(" from your Bhakthi Vriksha app.");
		buf.append(INSERT_ONE_BLANK_LINE);
		buf.append("In case if you did not request a reset password, please send your concern to: <b>")
		        .append(EMAIL_SUPPORT).append("</b>");
		buf.append(INSERT_TWO_BLANK_LINES);
		buf.append(getBodyTrailer());
		return buf.toString();
	}

	/**
	 * Email body for the account activation email.
	 * 
	 * @param email
	 * @param emailVerifCode
	 * @param mobileVerifCode
	 * @return
	 */
	private String getAccountActivationEmailBody(ServerRequest srvrReq) {

		StringBuffer buf = new StringBuffer();
		buf.append(getBodyHeader(srvrReq.getName()));
		buf.append(INSERT_ONE_BLANK_LINE);
		buf.append("Thanks for showing the interest in Bhakti Vriksha app to experience the spiritual bliss and gain the ultimate knowledge.");
		buf.append(NEW_LINE).append("<p>Please ");
		buf.append("<a href=\"http://localhost:8011/BVServer/VerifyEmail.jsp?");
		buf.append(KEY_VERIF_CODE).append("=")
		        .append(srvrReq.getEmailVerifCode());
		buf.append("&").append(KEY_EMAIL_ID).append("=")
		        .append(srvrReq.getEmailId());
		buf.append("\">click here</a>");
		buf.append(" to activate your account.");
		buf.append(NEW_LINE).append("<span>&nbsp;&nbsp;&nbsp;&nbsp;</span><b>OR</b>");
		buf.append(NEW_LINE).append("You can also activate your account through Bhakti Vriksha app in your mobile by entering the 6 digit email address verification code: ");
		buf.append("<b>").append(srvrReq.getMobileVerifCode()).append("</b>");
		buf.append(INSERT_TWO_BLANK_LINES);
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
		buf.append(INSERT_ONE_BLANK_LINE);
		buf.append("Welcome to Bhakti Vriksha app to experience the spiritual bliss and gain the ultimate knowledge.");
		buf.append(INSERT_ONE_BLANK_LINE);
		buf.append("<font color=\"green\">Your account has been activated! Now you can login into the Bhakti Vriksha app.</font>");
		buf.append(INSERT_ONE_BLANK_LINE);
		buf.append("Please use the Bhakti Vriksha app from your smart phone to enjoy reading the Bhakti Vriksha User Guide at your ease.");
		buf.append(INSERT_TWO_BLANK_LINES);
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

			if (subject == EMAIL_SUBJECT_ACC_ACTIVATION) {
				body = getAccountActivationEmailBody(srvrReq);
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

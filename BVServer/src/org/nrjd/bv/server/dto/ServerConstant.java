/**
 * 
 */
package org.nrjd.bv.server.dto;

/**
 * @author Sathya
 * 
 */
public final class ServerConstant {

	public static String	ACTION_USER_REG	        = "register";
	public static String	ACTION_VERIFY_EMAIL	    = "verifyEmail";
	public static String	EMAIL_SUBJECT_VER_EMAIL	= "Bhakthi Vriksha App - Please Verify Your Subscription";
	public static String	EMAIL_SUBJECT_WELCOME	= "Welcome to Bhakti Vrishna";
	public static String	KEY_CODE	            = "code";
	public static String	KEY_MSG	                = "msg";
	public static String	KEY_EMAIL_ID	        = "email";
	public static String	KEY_NAME	            = "name";
	public static String	KEY_PHONE	            = "phoneNum";
	public static String	KEY_PWD	                = "password";
	public static String	KEY_LANG	            = "language";
	public static String	KEY_VERIF_CODE	        = "vCode";
	public static String	CMD_REGISTER	        = "register";
	public static String	CMD_LOGIN	            = "login";
	public static String	CMD_VERIFY_EMAIL	    = "verifyEmail";
	public static String	CMD_CHG_PWD	            = "changePassword";
	public static String	CMD_UPDATE_PROF	        = "updateProfile";
	public static String	EMAIL_GMAIL_HOST	    = "smtp.gmail.com";
	public static String	EMAIL_GMAIL_PORT	    = "465";
	public static String	EMAIL_ACCT_ID	        = "bvtestsathya@gmail.com";
	public static String	EMAIL_ACCT_PWD	        = "jpsgurudev";
	public static String	EMAIL_SESSION	        = "mail/Session";
	public static String	EMAIL_CONTEXT	        = "java:comp/env";
	public static String	QRY_PERSIST_USER	    = "insert into user_login values(?,?,?,?,?,?,?,?,?)";
	public static String	QRY_VERIFY_EMAIL	    = "UPDATE user_login SET EMAIL_VERIFIED=1 WHERE EMAIL_VERIF_CODE=?";

	private ServerConstant() {

	}

}

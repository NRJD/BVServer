/**
 * 
 */
package org.nrjd.bv.server.dto;

/**
 * @author Sathya
 * 
 */
public final class ServerConstant {

	public static final String	PWD_ALGO_MD5	           = "MD5";
	public static final String	PWD_ALGO_PBKD	           = "PBKDF2WithHmacSHA1";
	public static final String	PWD_ALGO_SHA1	           = "SHA1PRNG";
	public static String	   EMAIL_SUBJECT_VER_EMAIL	   = "Bhakthi Vriksha App - Please Verify Your Subscription";
	public static String	   EMAIL_SUBJECT_WELCOME	   = "Welcome to Bhakti Vrishna";
	public static String	   EMAIL_SUBJECT_PWD_RESET	   = "Bhakthi Vriksha App - Reset Password Requested";
	public static String	   EMAIL_SUPPORT	           = "iskcon.nrjd.support@gmail.com";
	public static String	   KEY_CODE	                   = "code";
	public static String	   KEY_STATUS_ID	           = "statusId";
	public static String	   KEY_MSG	                   = "msg";
	public static String	   KEY_EMAIL_ID	               = "email";
	public static String	   KEY_NAME	                   = "name";
	public static String	   KEY_PHONE	               = "phoneNum";
	public static String	   KEY_COUNTRY_CODE	           = "countryCode";
	public static String	   KEY_PWD	                   = "password";
	public static String	   KEY_TEMP_PWD	               = "tempPassword";
	public static String	   KEY_PWD_RESET_ENABLED	   = "resetPwdEnabled";
	public static String	   KEY_LANG	                   = "language";
	public static String	   KEY_VERIF_CODE	           = "vCode";
	public static String	   KEY_FLOW	                   = "cmd";
	public static String	   KEY_FILE_NAME	           = "fileName";
	public static String	   OUT_PARAM_STATUS_FROM_DB	   = "STATUS_FROM_DB";
	public static String	   OUT_PARAM_EMAIL	           = "EMAIL_ID";
	public static String	   OUT_PARAM_PWD	           = "PASSWORD";
	public static String	   OUT_PARAM_ACCT_VERIFIED	   = "ACCT_VERIFIED";
	public static String	   OUT_PARAM_PWD_RESET_ENABLED	= "PWD_RESET_ENABLED";
	public static String	   CMD_REGISTER	               = "register";
	public static String	   CMD_LOGIN	               = "login";
	public static String	   CMD_LOGOFF	               = "logoff";
	public static String	   CMD_RESEND_SUBSCRP_EMAIL	   = "resendSubscriptionEmail";
	public static String	   CMD_ACCT_VERIFY_MOBILE	   = "verifyFromMobile";
	public static String	   CMD_UPDATE_PWD	           = "updatePassword";
	public static String	   CMD_RESET_PWD	           = "resetPassword";
	public static String	   CMD_UPDATE_PROF	           = "updateProfile";
	public static String	   CMD_DOWNLOAD	               = "download";
	public static String	   EMAIL_GMAIL_HOST	           = "smtp.gmail.com";
	public static String	   EMAIL_GMAIL_PORT	           = "465";
	public static String	   EMAIL_ACCT_ID	           = "bvtestsathya@gmail.com";
	public static String	   DB_DRIVER	               = "jdbc:mysql://localhost:3306/";
	public static String	   DB_SCHEMA	               = "bv";
	public static String	   DB_USER_NAME	               = "root";
	public static String	   DB_PWD	                   = "gurudev";
	public static String	   EMAIL_ACCT_PWD	           = "jpsgurudev";
	public static String	   EMAIL_SESSION	           = "mail/Session";
	public static String	   EMAIL_CONTEXT	           = "java:comp/env";
	public static String	   SP_PERSIST_USER	           = "{CALL usp_user_register(?,?,?,?,?,?,?,?)}";
	public static String	   QRY_UPDATE_VERIFY_EMAIL	   = "UPDATE user_login SET ACCT_VERIFIED=1 WHERE ACCT_VERIFIED = 0 AND EMAIL_VERIF_CODE=? AND EMAIL_ID=?";
	public static String	   SP_UPDATE_VERIFY_CODES	   = "{CALL usp_updateVerifCodes(?,?,?)}";
	public static String	   QRY_RETRIEVE_ACCT_VERIFIED	= "SELECT ACCT_VERIFIED FROM user_login WHERE EMAIL_ID = ?";
	public static String	   QRY_UPDATE_VERIFY_MOBILE	   = "UPDATE user_login SET ACCT_VERIFIED=1 WHERE ACCT_VERIFIED = 0 AND MOBILE_VERIF_CODE=? AND EMAIL_ID=?";
	public static String	   QRY_UPDATE_PROFILE	       = "UPDATE user_login SET NAME = ?, MOBILE_NUMBER = ?, LANGUAGE = ?, COUNTRY_CODE = ? WHERE EMAIL_ID = ?";
	public static String	   QRY_VERIFY_LOGIN	           = "SELECT PASSWORD, ACCT_VERIFIED, PWD_RESET_ENABLED FROM user_login WHERE EMAIL_ID = ?";
	public static String	   QRY_UPDATE_PWD	           = "UPDATE user_login SET PASSWORD = ?, PWD_RESET_ENABLED = ? WHERE EMAIL_ID = ?";
	public static String	   QRY_IS_ACCT_EMAIL_VERIFIED	= "SELECT ACCT_VERIFIED FROM user_login WHERE EMAIL_VERIF_CODE=? AND EMAIL_ID=?";
	public static String	   QRY_IS_ACCT_MOBILE_VERIFIED	= "SELECT ACCT_VERIFIED FROM user_login WHERE MOBILE_VERIF_CODE=? AND EMAIL_ID=?";
	public static int	       VAL_EMAIL_LEN	           = 50;
	public static int	       VAL_NAME_LEN	               = 50;
	public static int	       VAL_PWD_LEN	               = 15;
	public static int	       VAL_MOBNUM_LEN	           = 12;

	private ServerConstant() {

	}

}

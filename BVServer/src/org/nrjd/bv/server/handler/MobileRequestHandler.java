/**
 * 
 */
package org.nrjd.bv.server.handler;

import static org.nrjd.bv.server.dto.ServerConstant.CMD_ACCT_VERIFY_MOBILE;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_LOGIN;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_LOGOFF;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_REGISTER;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_RESEND_SUBSCRP_EMAIL;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_RESET_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_UPDATE_PROF;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_UPDATE_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_PWD_RESET;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_ACC_ACTIVATION;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_WELCOME;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_EMAIL_ID;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_FLOW;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_PWD_RESET_ENABLED;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_TEMP_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_VERIF_CODE;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.nrjd.bv.server.ds.BVServerDBException;
import org.nrjd.bv.server.ds.DataAccessServiceImpl;
import org.nrjd.bv.server.dto.BVServerException;
import org.nrjd.bv.server.dto.ServerRequest;
import org.nrjd.bv.server.dto.ServerResponse;
import org.nrjd.bv.server.dto.StatusCode;
import org.nrjd.bv.server.dto.UserData;
import org.nrjd.bv.server.util.CommonUtility;
import org.nrjd.bv.server.util.EmailUtil;
import org.nrjd.bv.server.util.JSONHelper;
import org.nrjd.bv.server.util.PasswordHandler;

/**
 * @author Sathya
 * 
 */
public class MobileRequestHandler {

	/**
	 * This method updates the Name, Mobile Number, Country Code, Language
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	private String loginLogOff(ServerRequest srvrReq, HttpServletRequest request) {

		System.out.println(">>> loginLogOff");
		StatusCode status = null;
		String jsonResponse = null;
		boolean isPwdResetEnabled = false;
		try {

			if (CMD_LOGIN.equals(srvrReq.getCommandFlow())) {

				ServerResponse srvrResponse = verifyLoginCredentials(srvrReq, false);
				status = srvrResponse.getCode();
				isPwdResetEnabled = srvrResponse.isResetPwdEnabled();
				if(status == StatusCode.LOGIN_SUCCESS) {
					HttpSession session = request.getSession();
					session.setAttribute(KEY_EMAIL_ID, srvrReq.getEmailId());
				}
			}
			else {

				HttpSession session = request.getSession();
				session.invalidate();
				status = StatusCode.LOGOFF_SUCCESS;
			}

		}
		catch (BVServerDBException e) {
			e.printStackTrace();
			status = StatusCode.ERROR_DB;
		}
		catch (Throwable e) {
			e.printStackTrace();
			status = StatusCode.ERROR_SERVER;
		}
		Map<String, String> resMap = new TreeMap<String, String>();
		resMap.put(KEY_EMAIL_ID, srvrReq.getEmailId());
		resMap.put(KEY_PWD_RESET_ENABLED, String.valueOf(isPwdResetEnabled));
		jsonResponse = JSONHelper.getJSonResponse(resMap, status);

		System.out.println("<<< loginLogOff " + jsonResponse);
		return jsonResponse;
	}
	
	/**
	 * This method verifies user login credentials.
	 * 
	 * @param request
	 * @return ServerResponse
	 * @throws BVServerDBException
	 */
	private ServerResponse verifyLoginCredentials(ServerRequest srvrReq, boolean verifyTempPassword) throws BVServerDBException {
		
			System.out.println(">>> processLogin");
			ServerResponse srvrResponse = new DataAccessServiceImpl().verifyLogin(srvrReq);
			
			// If no error code, then proceed with verifying the user password.
			if (srvrResponse.getCode() == null) {
				boolean isMatched = false;
				try {
					String inputPassword = (verifyTempPassword? srvrReq.getTempPwd() : srvrReq.getPassword());
					isMatched = PasswordHandler.validatePassword(
							inputPassword, srvrResponse.getDbPassword());
					System.out.println(isMatched);
				}
				catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (!isMatched) {
					srvrResponse.setCode(StatusCode.LOGIN_FAILED_INVALID_CREDENTIALS);
				}
				else {
					if (!srvrResponse.isAcctVerified()) {
						srvrResponse.setCode(StatusCode.ACCT_NOT_VERIFIED);
					}
					else {
						srvrResponse.setCode(StatusCode.LOGIN_SUCCESS);
					}
				}
			} 
			
			// If for some reason, if status code is still not set, then set it to default login failed code.
			if (srvrResponse.getCode() == null) {
				srvrResponse.setCode(StatusCode.LOGIN_FAILED_INVALID_CREDENTIALS);
			}
			return srvrResponse;
	}

	/**
	 * This method reset and updates the password requests.
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	private String processPasswordUpdate(ServerRequest srvrReq) {

		System.out.println(">>> processPasswordUpdate");
		StatusCode status = null;
		String jsonResponse = null;
		String email = null;
		String tempPassword = null;
		String encryptedTempPassword = null;
		String password = null;
		String encryptedPassword = null;
		boolean pwdResetEnabled = false;
		try {
			String commandFlow = srvrReq.getCommandFlow();
			email = srvrReq.getEmailId();
			if (CMD_RESET_PWD.equals(commandFlow)) {
				tempPassword = CommonUtility.generateTempPassword(); // This is new temporary password to be updated.
				encryptedTempPassword = PasswordHandler.encryptPassword(tempPassword); // Encrypt the new temporary password to be updated.
			}
			else {
				ServerResponse srvrResponse = verifyLoginCredentials(srvrReq, true);
				status = srvrResponse.getCode();
				pwdResetEnabled = srvrResponse.isResetPwdEnabled();
				if(status == StatusCode.LOGIN_SUCCESS) {
					tempPassword = srvrReq.getTempPwd(); // This is existing password to be matched.
					password = srvrReq.getPassword(); // This is new password to be updated.
					encryptedTempPassword = srvrResponse.getDbPassword(); // Assign the existing encrypted password value.
					encryptedPassword = PasswordHandler.encryptPassword(password); // Encrypt the new password to be updated.
				} else {
					Map<String, String> resMap = new TreeMap<String, String>();
					resMap.put(KEY_EMAIL_ID, email);
					resMap.put(KEY_PWD_RESET_ENABLED, Boolean.toString(pwdResetEnabled));
					jsonResponse = JSONHelper.getJSonResponse(resMap, status);
					System.out.println("<<< processPasswordUpdate " + jsonResponse);
					return jsonResponse;
				}
			}
			srvrReq.setEmailId(email);
			srvrReq.setCommandFlow(commandFlow);
			// Use encrypted passwords for DB operations.
			srvrReq.setTempPwd(encryptedTempPassword);
			srvrReq.setPassword(encryptedPassword);
			// Update in DB
			try{
				status = new DataAccessServiceImpl().resetPassword(srvrReq);
			} finally {
				// Use normal passwords after DB operation, so that we can use the
				// normal passwords in email notifications etc.
				srvrReq.setTempPwd(tempPassword);
				srvrReq.setPassword(password);
			}
			
			if (status == StatusCode.PWD_RESET_ENABLED) {
				new EmailUtil().sendEmail(srvrReq, EMAIL_SUBJECT_PWD_RESET);
				pwdResetEnabled = true;
			}
			else if (status == StatusCode.PWD_UPDATED_SUCCESS) {
				pwdResetEnabled = false;
			}
			else if ((status == StatusCode.PWD_RESET_FAILED) || (status == StatusCode.PWD_UPDATE_FAILED)) {
				UserData userData = new DataAccessServiceImpl().getUserByEmail(email);
				if(userData == null) {
					status = StatusCode.EMAIL_NOT_REGISTERED;
					// If user doesn't exists, then pwdResetEnabled flag doesn't make sense, so set it to not enabled.
					pwdResetEnabled = false; 
				} else {
					pwdResetEnabled = userData.isPwdResetEnabled();
				}
			}
		}
		catch (BVServerDBException e) {
			e.printStackTrace();
			status = StatusCode.ERROR_DB;
		}
		catch (Throwable e) {
			e.printStackTrace();
			status = StatusCode.ERROR_SERVER;
		}
		Map<String, String> resMap = new TreeMap<String, String>();
		resMap.put(KEY_EMAIL_ID, email);
		resMap.put(KEY_TEMP_PWD, tempPassword);
		resMap.put(KEY_PWD_RESET_ENABLED, Boolean.toString(pwdResetEnabled));
		jsonResponse = JSONHelper.getJSonResponse(resMap, status);

		System.out.println("<<< processPasswordUpdate " + jsonResponse);
		return jsonResponse;
	}

	/**
	 * This process the request and based on the Request dispatch Path delegates
	 * to the respective flow like User REgistry, Email Verification, Password
	 * Reset, Update profile ,download books, etc
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws BVServerDBException
	 */
	public String processRequest(HttpServletRequest request,
	        HttpServletResponse response) {

		System.out.println(">>> processRequest");
		long start = System.currentTimeMillis();
		String jsonResponse = null;

		try {
			JSONObject json = CommonUtility.readData(request);
			ServerRequest srvrReq = CommonUtility.populateRequestFromJson(json);

			String flowCommand = (String) json.get(KEY_FLOW);
			System.out.println("Read Data : " + json);
			if (flowCommand != null && flowCommand.length() > 0) {

				if (CMD_REGISTER.equals(flowCommand)) {

					// TODO validate the input
					jsonResponse = registerUser(srvrReq);
				}

				else if (CMD_ACCT_VERIFY_MOBILE.equals(flowCommand)) {

					jsonResponse = verifyAcctFromMobile(srvrReq);
				}
				else if (CMD_UPDATE_PWD.equals(flowCommand)
				        || CMD_RESET_PWD.equals(flowCommand)) {

					jsonResponse = processPasswordUpdate(srvrReq);
				}
				else if (CMD_UPDATE_PROF.equals(flowCommand)) {

					jsonResponse = updateProfile(srvrReq);
				}
				else if (CMD_LOGIN.equals(flowCommand)
				        || CMD_LOGOFF.equals(flowCommand)) {

					jsonResponse = loginLogOff(srvrReq, request);
				}
				else if (CMD_RESEND_SUBSCRP_EMAIL.equals(flowCommand)) {

					jsonResponse = resendVerificationEmail(srvrReq);
				}
			}
		}
		catch (BVServerException e) {

			e.printStackTrace();
			jsonResponse = JSONHelper.getJSonResponse(null,
			        StatusCode.ERROR_SERVER);
		}
		long end = System.currentTimeMillis();
		System.out.println("Total Time " + (end - start) + " Ms");
		System.out.println("<<< processRequest " + jsonResponse);
		return jsonResponse;
	}

	/**
	 * This method registers the User and sends an EMail for Verification.
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	private String registerUser(ServerRequest srvrReq) {

		System.out.println(">>> registerUser");
		StatusCode status = null;
		String jsonResponse = null;
		String email = null;
		try {
			UUID uuid = UUID.randomUUID();
			String emailVerifCode = uuid.toString().replaceAll("-", "")
			        .toUpperCase();
			int mobVerifCode = (100000 + new Random().nextInt(899999));

			srvrReq.setEmailVerifCode(emailVerifCode);
			srvrReq.setMobileVerifCode(String.valueOf(mobVerifCode));
			email = srvrReq.getEmailId();

			String encryptedPwd = PasswordHandler.encryptPassword(srvrReq
			        .getPassword());
			srvrReq.setPassword(encryptedPwd);

			status = new DataAccessServiceImpl().registerNewUser(srvrReq);

			if (status != null && status == StatusCode.USER_ADDED) {

				new EmailUtil().sendEmail(srvrReq, EMAIL_SUBJECT_ACC_ACTIVATION);
			}
		}
		catch (BVServerDBException | BVServerException e) {
			e.printStackTrace();
			status = StatusCode.ERROR_DB;
		}
		Map<String, String> resMap = new TreeMap<String, String>();
		resMap.put(KEY_EMAIL_ID, email);
		jsonResponse = JSONHelper.getJSonResponse(resMap, status);

		System.out.println("<<< registerUser " + jsonResponse);
		return jsonResponse;
	}

	/**
	 * This method resends the subscription Verification email if the user has
	 * not yet verified his subscription. A new email and mobile verification
	 * code will be generated.
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	private String resendVerificationEmail(ServerRequest srvrReq) {

		System.out.println(">>> registerUser");
		StatusCode status = null;
		String jsonResponse = null;
		String email = null;
		try {
			UUID uuid = UUID.randomUUID();
			String emailVerifCode = uuid.toString().replaceAll("-", "")
			        .toUpperCase();
			int mobVerifCode = (100000 + new Random().nextInt(899999));

			srvrReq.setEmailVerifCode(emailVerifCode);
			srvrReq.setMobileVerifCode(String.valueOf(mobVerifCode));
			email = srvrReq.getEmailId();

			status = new DataAccessServiceImpl()
			        .updateVerificationCodes(srvrReq);

			if (status != null && status == StatusCode.RESEND_VERIF_SUCCESS) {

				new EmailUtil().sendEmail(srvrReq, EMAIL_SUBJECT_ACC_ACTIVATION);
			}
			else if (status == null) {
				status = StatusCode.RESEND_VERIF_FAILED;
			}
		}
		catch (BVServerDBException e) {
			e.printStackTrace();
			status = StatusCode.ERROR_DB;
		}
		Map<String, String> resMap = new TreeMap<String, String>();
		resMap.put(KEY_EMAIL_ID, email);
		jsonResponse = JSONHelper.getJSonResponse(resMap, status);

		System.out.println("<<< registerUser " + jsonResponse);
		return jsonResponse;
	}

	/**
	 * This method updates the Name, Mobile Number, Country Code, Language
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	private String updateProfile(ServerRequest srvrReq) {

		System.out.println(">>> updateProfile");
		StatusCode status = null;
		String jsonResponse = null;
		try {

			status = new DataAccessServiceImpl().updateProfile(srvrReq);
		}
		catch (BVServerDBException e) {
			e.printStackTrace();
			status = StatusCode.ERROR_DB;
		}
		catch (Throwable e) {
			e.printStackTrace();
			status = StatusCode.ERROR_SERVER;
		}
		Map<String, String> resMap = new TreeMap<String, String>();
		resMap.put(KEY_EMAIL_ID, srvrReq.getEmailId());
		jsonResponse = JSONHelper.getJSonResponse(resMap, status);

		System.out.println("<<< updateProfile " + jsonResponse);
		return jsonResponse;
	}

	/**
	 * This method verifies the Account subscription request receievd from
	 * Mobile using the Email ID and Mobile Verification Code.
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	private String verifyAcctFromMobile(ServerRequest srvrRequest) {

		System.out.println(">>> verifyAcctFromMobile");
		StatusCode status = null;
		String jsonResponse = null;
		String email = null;
		String mobVerifCode = null;
		try {

			srvrRequest.setEmailId(srvrRequest.getEmailId());
			srvrRequest.setMobileVerifCode(srvrRequest.getMobileVerifCode());
			srvrRequest.setMobileVerification(true);

			status = new DataAccessServiceImpl()
			        .verifySubscription(srvrRequest);

			if (status == StatusCode.ACCT_VERIFIED) {

				new EmailUtil().sendEmail(srvrRequest, EMAIL_SUBJECT_WELCOME);
			} else {
				if(!(new DataAccessServiceImpl().isEmailExists(srvrRequest.getEmailId()))) {
					status = StatusCode.EMAIL_NOT_REGISTERED;
				}
			}
		}
		catch (BVServerDBException e) {
			e.printStackTrace();
			status = StatusCode.ERROR_DB;
		}
		Map<String, String> resMap = new TreeMap<String, String>();
		resMap.put(KEY_EMAIL_ID, email);
		resMap.put(KEY_VERIF_CODE, mobVerifCode);
		jsonResponse = JSONHelper.getJSonResponse(resMap, status);

		System.out.println("<<< verifyAcctFromMobile " + jsonResponse);
		return jsonResponse;
	}

}

/**
 * 
 */
package org.nrjd.bv.server.handler;

import static org.nrjd.bv.server.dto.ServerConstant.CMD_ACCT_VERIFY_MOBILE;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_REGISTER;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_RESET_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_UPDATE_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_PWD_RESET;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_VER_EMAIL;
import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_WELCOME;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_COUNTRY_CODE;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_EMAIL_ID;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_FLOW;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_LANG;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_NAME;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_PHONE;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_PWD_RESET_ENABLED;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_TEMP_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_VERIF_CODE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.nrjd.bv.server.ds.BVServerDBException;
import org.nrjd.bv.server.ds.DataAccessServiceImpl;
import org.nrjd.bv.server.dto.BVServerException;
import org.nrjd.bv.server.dto.ServerRequest;
import org.nrjd.bv.server.dto.StatusCode;
import org.nrjd.bv.server.util.CommonUtility;
import org.nrjd.bv.server.util.EmailUtil;
import org.nrjd.bv.server.util.JSONHelper;

/**
 * @author Sathya
 * 
 */
public class MobileRequestHandler {

	/**
	 * 
	 * @param inputData
	 * @return
	 * @throws IOException
	 */
	public static String getRequestData(InputStream inputData)
	        throws BVServerException {

		InputStreamReader isr = new InputStreamReader(inputData);
		BufferedReader br = new BufferedReader(isr);
		try {
			StringBuffer xmlBuffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				xmlBuffer.append(line);
			}
			String xmlData = xmlBuffer.toString();
			System.out.println("XML Request Data: " + xmlData);
			return xmlData;
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new BVServerException(e.getMessage());
		}
		finally {
			try {
				br.close();
			}
			catch (Exception e) { /* Ignore Exception */
			}
			try {
				isr.close();
			}
			catch (Exception e) { /* Ignore Exception */
			}
		}
	}

	private ServerRequest populateRequestFromJson(JSONObject json) {

		ServerRequest srvrReq = new ServerRequest();

		String name = (String) json.get(KEY_NAME);
		String pwd = (String) json.get(KEY_PWD);
		String email = (String) json.get(KEY_EMAIL_ID);
		String lang = (String) json.get(KEY_LANG);
		String mobile = (String) json.get(KEY_PHONE);
		String countryCode = (String) json.get(KEY_COUNTRY_CODE);
		String mobVerifCode = (String) json.get(KEY_VERIF_CODE);
		String tempPassword = (String) json.get(KEY_TEMP_PWD);
		String commandFlow = (String) json.get(KEY_FLOW);

		srvrReq.setCommandFlow(commandFlow);
		srvrReq.setCountryCode(countryCode);
		srvrReq.setEmailId(email);
		srvrReq.setLanguage(lang);
		srvrReq.setMobileVerifCode(mobVerifCode);
		srvrReq.setName(name);
		srvrReq.setPassword(pwd);
		srvrReq.setPhoneNumber(mobile);
		srvrReq.setTempPwd(tempPassword);

		return srvrReq;
	}

	/**
	 * This method reset and updates the password requests.
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	private String processPasswordUpdate(JSONObject json) {

		System.out.println(">>> processPasswordUpdate");
		StatusCode status = null;
		String jsonResponse = null;
		String email = null;
		String tempPassword = null;
		String password = null;
		boolean pwdResetEnabled = false;
		try {
			email = (String) json.get(KEY_EMAIL_ID);
			String commandFlow = (String) json.get(KEY_FLOW);

			if (CMD_RESET_PWD.equals(commandFlow)) {
				tempPassword = CommonUtility.generateTempPassword();
			}
			else {
				tempPassword = (String) json.get(KEY_TEMP_PWD);
				password = (String) json.get(KEY_PWD);// this is new password
			}
			ServerRequest srvrReq = new ServerRequest();
			srvrReq.setEmailId(email);
			srvrReq.setCommandFlow(commandFlow);
			srvrReq.setTempPwd(tempPassword);
			srvrReq.setPassword(password);

			status = new DataAccessServiceImpl().resetPassword(srvrReq);

			if (status == StatusCode.PWD_RESET_ENABLED) {

				EmailUtil.sendEmail(srvrReq, EMAIL_SUBJECT_PWD_RESET);
				pwdResetEnabled = true;
			}
			else if (status == StatusCode.PWD_UPDATED_SUCCESS) {

				pwdResetEnabled = false;
			}
			else if (status == StatusCode.PWD_UPDATE_FAILED) {

				pwdResetEnabled = true;
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
		String jsonResponse = null;

		try {
			JSONObject json = readData(request);

			String flowCommand = (String) json.get(KEY_FLOW);
			System.out.println("Read Data : " + json);
			if (flowCommand != null && flowCommand.length() > 0) {

				if (CMD_REGISTER.equals(flowCommand)) {

					// TODO validate the input
					jsonResponse = registerUser(json);
				}

				else if (CMD_ACCT_VERIFY_MOBILE.equals(flowCommand)) {

					jsonResponse = verifyAcctFromMobile(json);
				}
				else if (CMD_UPDATE_PWD.equals(flowCommand)
				        || CMD_RESET_PWD.equals(flowCommand)) {

					jsonResponse = processPasswordUpdate(json);
				}
				else if (CMD_UPDATE_PWD.equals(flowCommand)) {

					jsonResponse = processPasswordUpdate(json);
				}
			}
		}
		catch (BVServerException e) {
			e.printStackTrace();
			jsonResponse = JSONHelper.getJSonResponse(null,
			        StatusCode.ERROR_SERVER);
		}

		System.out.println("<<< processRequest " + jsonResponse);
		return jsonResponse;
	}

	/**
	 * To Read The JSON data
	 * 
	 * @param request
	 * @return
	 * @throws BVServerException
	 */
	private JSONObject readData(HttpServletRequest request)
	        throws BVServerException {
		InputStream is = null;
		try {
			is = request.getInputStream();
			String requestXmlData = getRequestData(is);

			JSONObject json = JSONHelper.parseJSONRequest(requestXmlData);
			return json;
		}
		catch (Exception e) {

			throw new BVServerException(
			        "Error while retriving the request data", e);
		}
		finally {
			try {
				is.close();
			}
			catch (Exception e) {

				// Ignore the Exception
			}
		}
	}

	/**
	 * This method registers the User and sends an EMail for Verification.
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	private String registerUser(JSONObject json) {

		System.out.println(">>> registerUser");
		StatusCode status = null;
		String jsonResponse = null;
		String email = null;
		try {
			String name = (String) json.get(KEY_NAME);
			String pwd = (String) json.get(KEY_PWD);
			email = (String) json.get(KEY_EMAIL_ID);
			String lang = (String) json.get(KEY_LANG);
			String mobile = (String) json.get(KEY_PHONE);
			String countryCode = (String) json.get(KEY_COUNTRY_CODE);

			UUID uuid = UUID.randomUUID();
			String emailVerifCode = uuid.toString().replaceAll("-", "")
			        .toUpperCase();
			int mobVerifCode = (100000 + new Random().nextInt(899999));

			ServerRequest srvrReq = new ServerRequest();
			srvrReq.setEmailId(email);
			srvrReq.setLanguage(lang);
			srvrReq.setName(name);
			srvrReq.setPassword(pwd);
			srvrReq.setPhoneNumber(mobile);
			srvrReq.setEmailVerifCode(emailVerifCode);
			srvrReq.setMobileVerifCode(String.valueOf(mobVerifCode));
			srvrReq.setCountryCode(countryCode);

			status = new DataAccessServiceImpl().registerNewUser(srvrReq);

			if (status != null && status == StatusCode.USER_ADDED) {

				EmailUtil.sendEmail(srvrReq, EMAIL_SUBJECT_VER_EMAIL);
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
	private String updateProfile(JSONObject json) {

		System.out.println(">>> updateProfile");
		StatusCode status = null;
		String jsonResponse = null;
		ServerRequest srvrReq = null;
		try {
			srvrReq = populateRequestFromJson(json);

			status = new DataAccessServiceImpl().resetPassword(srvrReq);
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
	private String verifyAcctFromMobile(JSONObject json) {

		System.out.println(">>> verifyAcctFromMobile");
		StatusCode status = null;
		String jsonResponse = null;
		String email = null;
		String mobVerifCode = null;
		try {
			mobVerifCode = (String) json.get(KEY_VERIF_CODE);
			email = (String) json.get(KEY_EMAIL_ID);

			ServerRequest srvrReq = new ServerRequest();
			srvrReq.setEmailId(email);
			srvrReq.setMobileVerifCode(String.valueOf(mobVerifCode));
			srvrReq.setMobileVerification(true);

			status = new DataAccessServiceImpl().verifySubscription(srvrReq);

			if (status == StatusCode.ACCT_VERIFIED) {

				EmailUtil.sendEmail(srvrReq, EMAIL_SUBJECT_WELCOME);
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

/**
 * 
 */
package org.nrjd.bv.server.util;

import static org.nrjd.bv.server.dto.ServerConstant.KEY_COUNTRY_CODE;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_EMAIL_ID;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_FILE_NAME;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_FLOW;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_LANG;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_NAME;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_PHONE;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_TEMP_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_VERIF_CODE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.nrjd.bv.server.dto.BVServerException;
import org.nrjd.bv.server.dto.ServerConstant;
import org.nrjd.bv.server.dto.ServerRequest;
import org.nrjd.bv.server.dto.StatusCode;

/**
 * @author Sathya
 * 
 */
public class CommonUtility {

	private static final String	ALPHA_CAPS	= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String	ALPHA	   = "abcdefghijklmnopqrstuvwxyz";
	private static final String	NUM	       = "0123456789";
	private static final String	SPL_CHARS	= "!@#$%^&*_=+-/";

	/**
	 * 
	 * @param minLen
	 * @param maxLen
	 * @param noOfCAPSAlpha
	 * @param noOfDigits
	 * @param noOfSplChars
	 * @return
	 */
	public static char[] generatePswd(int noOfCAPSAlpha, int noOfDigits,
	        int noOfSplChars) {

		int maxLen = 8;
		int minLen = 8;

		Random random = new Random();
		int len = random.nextInt(maxLen - minLen + 1) + minLen;
		char[] pswd = new char[len];
		int index = 0;
		for (int i = 0; i < noOfCAPSAlpha; i++) {
			index = getNextIndex(random, len, pswd);
			pswd[index] = ALPHA_CAPS
			        .charAt(random.nextInt(ALPHA_CAPS.length()));
		}
		for (int i = 0; i < noOfDigits; i++) {
			index = getNextIndex(random, len, pswd);
			pswd[index] = NUM.charAt(random.nextInt(NUM.length()));
		}
		// for (int i = 0; i < noOfSplChars; i++) {
		// index = getNextIndex(random, len, pswd);
		// pswd[index] = SPL_CHARS.charAt(random.nextInt(SPL_CHARS.length()));
		// }
		for (int i = 0; i < len; i++) {
			if (pswd[i] == 0) {
				pswd[i] = ALPHA.charAt(random.nextInt(ALPHA.length()));
			}
		}
		return pswd;
	}

	/**
	 * This method generates the temp Password. The algo is :
	 * 
	 * @1 Generate UUID
	 * @2 Remove All hyphens
	 * @3 Starting bound : generate a random number less than 32 (UUID length
	 *    after removing hyphens)
	 * @4 Ending bound : If the starting bound is less than 23 then substract 8
	 *    else add 8 ( to avoid arrayIndexoutofboud exception if the ending
	 *    crosses 32)
	 * @5 substring the UUID and convert to upper case
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateTempPassword() {

		UUID uuid = UUID.randomUUID();
		String emailVerifCode = uuid.toString().replaceAll("-", "");
		Random ran = new Random();
		int s1 = ran.nextInt(32);

		int s2 = s1 > 23 ? (s1 - 8) : (s1 + 8);

		int start = s1 < s2 ? s1 : s2;
		int end = s1 > s2 ? s1 : s2;

		String tempPwd = emailVerifCode.substring(start, end).toUpperCase();

		return tempPwd;
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	public static String getMessage(StatusCode code) {

		int status = code.getStatusCode();
		String desc = null;
		switch (status) {

		case 3000:
			desc = "User Registered Successfully. verify the Email";
			break;
		case 3001:
			desc = "Email ID already Registered. verify the Email";
			break;
		case 3003:
			desc = "Invalid Password.";
			break;
		case 3005:
			desc = "Account Verified Successfully.";
			break;
		case 3006:
			desc = "Account Not Activated. Please activate the account by following the directions in the subscription email";
			break;
		case 3007:
			desc = "Failed to process the Request due to Database Error.Contact System Admin";
			break;
		case 3008:
			desc = "Failed to process the Request due to Server Technical Error.Contact System Admin";
			break;
		case 3011:
			desc = "Account activation failed as the Account was already activated.";
			break;
		case 3012:
			desc = "Email Address not provided.";
			break;
		case 3013:
			desc = "Email Address cannot exceed than 50 chars.";
			break;
		case 3014:
			desc = "Email Address format is invalid.";
			break;
		case 3015:
			desc = "Name not provided.";
			break;
		case 3016:
			desc = "Name cannot exceed than 50 chars.";
			break;
		case 3017:
			desc = "Name format is invalid.";
			break;
		case 3018:
			desc = "Password not provided.";
			break;
		case 3019:
			desc = "Password cannot exceed than 15 chars.";
			break;
		case 3020:
			desc = "Phone Number not provided.";
			break;
		case 3021:
			desc = "Phone Number cannot exceed than 12 chars.";
			break;
		case 3022:
			desc = "Phone Number format is invalid.";
			break;
		case 3023:
			desc = "Validation Failed.User is not registered.Verify the data you have submitted";
			break;
		case 3024:
			desc = "Request for Password Reset is Successful. Please check your email.";
			break;
		case 3025:
			desc = "Update Password is Successful.";
			break;
		case 3026:
			desc = "Request for Password Reset is Failed. Contact support team";
			break;
		case 3027:
			desc = "Update Password is Failed. Contact support team";
			break;
		case 3028:
			desc = "User Profile updated successfully.";
			break;
		case 3029:
			desc = "User Profile update Failed. Contact support team";
			break;
		case 3030:
			desc = "Successfully Logged In.";
			break;
		case 3031:
			desc = "Invalid Email Id or Password.";
			break;
		case 3032:
			desc = "Logged Off successfully";
			break;
		case 3033:
			desc = "Resend Subscription Code successful.";
			break;
		case 3034:
			desc = "Resend Subscription Code failed due to technical reason.";
			break;
		case 3035:
			desc = "Email is not registered.";
			break;
		}

		return desc;
	}

	/**
	 * Get next Index Value
	 * 
	 * @param rnd
	 * @param len
	 * @param pswd
	 * @return
	 */
	private static int getNextIndex(Random rnd, int len, char[] pswd) {
		int index = rnd.nextInt(len);
		while (pswd[index = rnd.nextInt(len)] != 0)
			;
		return index;
	}

	/**
	 * 
	 * @param inputData
	 * @return
	 * @throws IOException
	 */
	private static String getRequestData(InputStream inputData)
	        throws BVServerException {

		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(inputData, ServerConstant.ENCODING_FORMAT);
			br = new BufferedReader(isr);
			StringBuffer xmlBuffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				xmlBuffer.append(line);
			}
			String xmlData = xmlBuffer.toString();
			return xmlData;
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new BVServerException(e.getMessage());
		}
		finally {
			try {
				if(br != null) {
					br.close();
				}
			}
			catch (Exception e) { /* Ignore Exception */
			}
			try {
				if(isr != null) {
					isr.close();
				}
			}
			catch (Exception e) { /* Ignore Exception */
			}
		}
	}

	/**
	 * This method will populate the ServerRequest with the data retrieved from
	 * JSON onject
	 * 
	 * @param json
	 * @return
	 */
	public static ServerRequest populateRequestFromJson(JSONObject json) {

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
		String fileToDownload = (String) json.get(KEY_FILE_NAME);

		srvrReq.setCommandFlow(commandFlow);
		srvrReq.setCountryCode(countryCode);
		srvrReq.setEmailId(email);
		srvrReq.setLanguage(lang);
		srvrReq.setMobileVerifCode(mobVerifCode);
		srvrReq.setName(name);
		srvrReq.setPassword(pwd);
		srvrReq.setPhoneNumber(mobile);
		srvrReq.setTempPwd(tempPassword);
		srvrReq.setFileName(fileToDownload);

		return srvrReq;
	}

	/**
	 * To Read The JSON data
	 * 
	 * @param request
	 * @return
	 * @throws BVServerException
	 */
	public static JSONObject readData(HttpServletRequest request)
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
}

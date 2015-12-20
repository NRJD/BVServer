/**
 * 
 */
package org.nrjd.bv.server.util;

import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

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
			desc = "Your Account has been already activated.";
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
}

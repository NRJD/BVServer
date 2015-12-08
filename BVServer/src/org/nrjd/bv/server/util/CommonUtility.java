/**
 * 
 */
package org.nrjd.bv.server.util;

import org.nrjd.bv.server.dto.StatusCode;

/**
 * @author Sathya
 * 
 */
public class CommonUtility {

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
			desc = "Email Verified Successfully.";
			break;
		case 3006:
			desc = "Email Not Verified. Please activate the account by following the directions in the subscription email";
			break;
		case 3007:
			desc = "Failed to process the Request due to Database Error.Contact System Admin";
			break;
		case 3008:
			desc = "Failed to process the Request due to Server Technical Error.Contact System Admin";
			break;
		}

		return desc;
	}
}

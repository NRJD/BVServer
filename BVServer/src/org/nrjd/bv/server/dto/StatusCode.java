/**
 * 
 */
package org.nrjd.bv.server.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sathya
 * 
 */
public enum StatusCode {
	USER_ADDED(3000), DUPL_EMAILID(3001), EMAIL_UNREGISTERED(3002), INVALID_PWD(
	        3003), UPDATE_SUCCESS(3004), ACCT_VERIFIED(3005), ACCT_NOT_VERIFIED(
	        3006), ERROR_DB(3007), ERROR_SERVER(3008), ERROR_EMAIL(3009), ERROR_JSON(
	        3010), ACCT_ALREADY_VERIFIED(3011), EMAIL_NULL(3012), EMAIL_TOO_LONG(
	        3013), EMAIL_INVALID(3014), NAME_NULL(3015), NAME_TOO_LONG(3016), NAME_INVALID(
	        3017), PWD_NULL(3018), PWD_TOO_LONG(3019), MOBNUM_NULL(3020), MOBNUM_TOO_LONG(
	        3021), MOBNUM_INVALID(3022), VALIDATION_FAILED(3023), PWD_RESET_ENABLED(
	        3024), PWD_UPDATED_SUCCESS(3025), PWD_RESET_FAILED(3026), PWD_UPDATE_FAILED(
	        3027), PROFILE_UPDATE_SUCCESS(3028), PROFILE_UPDATE_FAILED(3029), LOGIN_SUCCESS(
	        3030), LOGIN_FAILED_INVALID_CREDENTIALS(3031), LOGOFF_SUCCESS(3032), RESEND_VERIF_SUCCESS(
	        3033), RESEND_VERIF_FAILED(3034), EMAIL_NOT_REGISTERED(3035);

	private int	                                 statusCode;

	public static final Map<Integer, StatusCode>	lookup	= new HashMap<Integer, StatusCode>();
	static {
		for (StatusCode d : StatusCode.values()) {
			lookup.put(d.getStatusCode(), d);
		}
	}

	/**
	 * Gets the Enum for the given int value
	 * 
	 * @param stat
	 * @return
	 */
	public static StatusCode reverseLookup(int stat) {

		return lookup.get(stat);

	}

	StatusCode(int code) {
		this.statusCode = code;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return this.statusCode;
	}

	/**
	 * @return the statusCode
	 */
	public String getStatusId() {
		return this.name();
	}
}

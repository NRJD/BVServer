/**
 * 
 */
package org.nrjd.bv.server.dto;

/**
 * @author Sathya
 * 
 */
public enum StatusCode {

	STATUS_USER_ADDED(3000), STATUS_DUPL_EMAILID(3001), STATUS_EMAIL_UNREGISTERED(
	        3002), STATUS_INVALID_PWD(3003), STATUS_UPDATE_SUCCESS(3004), STATUS_ACCT_VERIFIED(
	        3005), STATUS_ACCT_NOT_VERIFIED(3006), STATUS_ERROR_DB(3007), STATUS_ERROR_SERVER(
	        3008), STATUS_ERROR_EMAIL(3009), STATUS_ERROR_JSON(3010), STATUS_ACCT_ALREADY_VERIFIED(
	        3011);

	private int	statusCode;

	StatusCode(int code) {

		statusCode = code;
	}

	/**
	 * @return the statusCode
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

}

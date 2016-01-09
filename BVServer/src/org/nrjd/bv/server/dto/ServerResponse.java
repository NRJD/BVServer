/**
 * 
 */
package org.nrjd.bv.server.dto;

import java.io.Serializable;

/**
 * @author Sathya
 * 
 */
public class ServerResponse implements Serializable {

	private static final long	serialVersionUID	= -7245469735442158414L;
	private boolean	          isAcctVerified;
	private boolean	          isResetPwdEnabled;
	private String	          emailId;
	private StatusCode	      code;
	private String	          dbPassword;

	/**
	 * @return the code
	 */
	public StatusCode getCode() {
		return code;
	}

	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @return the isAcctVerified
	 */
	public boolean isAcctVerified() {
		return isAcctVerified;
	}

	/**
	 * @return the isResetPwdEnabled
	 */
	public boolean isResetPwdEnabled() {
		return isResetPwdEnabled;
	}

	/**
	 * @param isAcctVerified
	 *            the isAcctVerified to set
	 */
	public void setAcctVerified(boolean isAcctVerified) {
		this.isAcctVerified = isAcctVerified;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(StatusCode code) {
		this.code = code;
	}

	/**
	 * @param dbPassword
	 *            the dbPassword to set
	 */
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @param isResetPwdEnabled
	 *            the isResetPwdEnabled to set
	 */
	public void setResetPwdEnabled(boolean isResetPwdEnabled) {
		this.isResetPwdEnabled = isResetPwdEnabled;
	}

}

/**
 * 
 */
package org.nrjd.bv.server.dto;

import java.io.Serializable;

/**
 * @author Sathya
 * 
 */
public class ServerRequest implements Serializable {

	private static final long	serialVersionUID	= -2929704671638148115L;
	private String	          name;
	private String	          emailId;
	private String	          password;
	private String	          phoneNumber;
	private String	          language;
	private String	          emailVerifCode;
	private String	          mobileVerifCode;
	private boolean	          mobileVerification;
	private boolean	          emailVerification;

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @return the emailVerifCode
	 */
	public String getEmailVerifCode() {
		return emailVerifCode;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return the mobileVerifCode
	 */
	public String getMobileVerifCode() {
		return mobileVerifCode;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @return the emailVerification
	 */
	public boolean isEmailVerification() {
		return emailVerification;
	}

	/**
	 * @return the mobileVerification
	 */
	public boolean isMobileVerification() {
		return mobileVerification;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @param emailVerifCode
	 *            the emailVerifCode to set
	 */
	public void setEmailVerifCode(String emailVerifCode) {
		this.emailVerifCode = emailVerifCode;
	}

	/**
	 * @param emailVerification
	 *            the emailVerification to set
	 */
	public void setEmailVerification(boolean emailVerification) {
		this.emailVerification = emailVerification;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @param mobileVerifCode
	 *            the mobileVerifCode to set
	 */
	public void setMobileVerifCode(String mobileVerifCode) {
		this.mobileVerifCode = mobileVerifCode;
	}

	/**
	 * @param mobileVerification
	 *            the mobileVerification to set
	 */
	public void setMobileVerification(boolean mobileVerification) {
		this.mobileVerification = mobileVerification;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}

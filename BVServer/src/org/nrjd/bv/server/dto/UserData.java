/**
 * 
 */
package org.nrjd.bv.server.dto;

import java.io.Serializable;

/**
 * @author Satya
 */
public class UserData implements Serializable {
	private static final long serialVersionUID = -3549035897954605489L;
	private long id;
	private String name;
	private String emailId;
	private String password;
	private String phoneNumber;
	private int countryCode;
	private String language;
	private boolean isAccountVerified;
	private boolean isPwdResetEnabled;

	public UserData(long id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return this.emailId;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	 * @param phoneNumber
	 *            the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the countryCode
	 */
	public int getCountryCode() {
		return this.countryCode;
	}

	/**
	 * @param countryCode
	 *            the countryCode to set
	 */
	public void setCountryCode(int countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return this.language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return the isAccountVerified
	 */
	public boolean isAccountVerified() {
		return this.isAccountVerified;
	}

	/**
	 * @param isAccountVerified
	 *            the isAccountVerified to set
	 */
	public void setIsAccountVerified(boolean isAccountVerified) {
		this.isAccountVerified = isAccountVerified;
	}

	/**
	 * @return the isPwdResetEnabled
	 */
	public boolean isPwdResetEnabled() {
		return this.isPwdResetEnabled;
	}

	/**
	 * @param isPwdResetEnabled
	 *            the isPwdResetEnabled to set
	 */
	public void setIsPwdResetEnabled(boolean isPwdResetEnabled) {
		this.isPwdResetEnabled = isPwdResetEnabled;
	}
}

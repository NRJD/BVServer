/**
 * 
 */
package org.nrjd.bv.server.dto;

import java.io.Serializable;

/**
 * @author Sathya
 * 
 */
public class DataAccessRequest implements Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4646276734320207790L;

	private String	          name;
	private String	          emailId;
	private String	          password;
	private String	          phoneNumber;
	private String	          language;
	private boolean	          emailVerified;

	/**
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
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
	 * @return the emailVerified
	 */
	public boolean isEmailVerified() {
		return emailVerified;
	}

	/**
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @param emailVerified
	 *            the emailVerified to set
	 */
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
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

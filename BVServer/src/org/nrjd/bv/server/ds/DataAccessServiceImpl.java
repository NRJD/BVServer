/**
 * 
 */
package org.nrjd.bv.server.ds;

import static org.nrjd.bv.server.dto.ServerConstant.CMD_RESET_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_UPDATE_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.DB_DRIVER;
import static org.nrjd.bv.server.dto.ServerConstant.DB_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.DB_SCHEMA;
import static org.nrjd.bv.server.dto.ServerConstant.DB_USER_NAME;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_ACCT_VERIFIED;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_PWD_RESET_ENABLED;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_STATUS_FROM_DB;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_USER_LOGIN_ID;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_EMAIL;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_NAME;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_MOBILE_NUMBER;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_COUNTRY_CODE;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_LANGUAGE;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_GET_USER;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_IS_ACCT_EMAIL_VERIFIED;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_IS_ACCT_MOBILE_VERIFIED;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_UPDATE_PROFILE;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_UPDATE_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_UPDATE_VERIFY_EMAIL;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_UPDATE_VERIFY_MOBILE;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_VERIFY_LOGIN;
import static org.nrjd.bv.server.dto.ServerConstant.SP_PERSIST_USER;
import static org.nrjd.bv.server.dto.ServerConstant.SP_UPDATE_VERIFY_CODES;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nrjd.bv.server.dto.ServerRequest;
import org.nrjd.bv.server.dto.ServerResponse;
import org.nrjd.bv.server.dto.StatusCode;
import org.nrjd.bv.server.dto.UserData;

/**
 * @author Sathya
 * 
 */
public class DataAccessServiceImpl {

	private static Connection	connection;

	/**
	 * Closes the connection
	 * 
	 * @param ps
	 * @throws BVServerDBException
	 */
	private void closeConnection(PreparedStatement ps)
	        throws BVServerDBException {

		if (connection != null) {

			try {
				if (!connection.isClosed() && ps != null) {

					connection.close();
				}
			}
			catch (SQLException e) {
				// Ignore the Exception
			}
		}
	}

	private void getConnection() throws BVServerDBException {

		try {
			Class.forName("com.mysql.jdbc.Driver");

			if (connection == null || connection.isClosed()) {

				connection = DriverManager.getConnection(DB_DRIVER + DB_SCHEMA,
				        DB_USER_NAME, DB_PWD);

				System.out.println("Connection Established ! ");

			}
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new BVServerDBException(e.getMessage());
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new BVServerDBException(e.getMessage());
		}

	}

	/**
	 * 
	 * @param e
	 * @throws BVServerDBException
	 */
	private StatusCode handleException(SQLException e)
	        throws BVServerDBException {

		StatusCode code = StatusCode.ERROR_DB;
		try {
			connection.rollback();

		}
		catch (SQLException e1) {
			e1.printStackTrace();
		}
		finally {

			if (e.getMessage() != null
			        && e.getMessage().contains("EMAIL_ID_UNIQUE")) {
				code = StatusCode.DUPL_EMAILID;
			}
			else {
				e.printStackTrace();
				throw new BVServerDBException(e.getMessage());
			}
		}
		return code;
	}

	private boolean isAlreadyVerified(ServerRequest request)
	        throws BVServerDBException {

		getConnection();
		PreparedStatement ps = null;
		boolean emailVerified = false;
		try {
			String qry = request.isEmailVerification() ? QRY_IS_ACCT_EMAIL_VERIFIED
			        : QRY_IS_ACCT_MOBILE_VERIFIED;
			ps = connection.prepareStatement(qry);
			String verifCode = request.isEmailVerification() ? request
			        .getEmailVerifCode() : request.getMobileVerifCode();
			ps.setString(1, verifCode);
			ps.setString(2, request.getEmailId());

			ResultSet rs = ps.executeQuery();

			if (rs != null) {

				while (rs.next()) {

					emailVerified = rs.getInt(1) == 1 ? true : false;
				}
			}
		}
		catch (SQLException e) {
			// ignore
		}
		finally {
			closeConnection(ps);
		}

		return emailVerified;
	}
	
	/**
	 * This method calls the stored procedure to persist the user. The stored
	 * procedure checks if the Email address already exist in the system, if yes
	 * then checks whether it has been verified . If verified returns status
	 * code 3001 (Duplicate Email ID), if the email Address is found but Not
	 * verified then return 3006 (Email ID not verified). If email is not found
	 * in database then adds the user to the system and return success status
	 * 3000.
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	public StatusCode registerNewUser(ServerRequest request)
	        throws BVServerDBException {

		StatusCode code = null;
		getConnection();
		PreparedStatement ps = null;

		try {
			ps = connection.prepareCall(SP_PERSIST_USER);

			ps.setString(1, request.getName());
			ps.setString(2, request.getEmailId());
			ps.setString(3, request.getPassword());
			ps.setString(4, request.getPhoneNumber());
			ps.setString(5, request.getLanguage());
			ps.setString(6, request.getEmailVerifCode());
			ps.setString(7, request.getMobileVerifCode());
			ps.setString(8, request.getCountryCode());

			ps.execute();

			ResultSet rs = ps.getResultSet();
			int dbStatus = 0;
			while (rs.next()) {

				dbStatus = rs.getInt(OUT_PARAM_STATUS_FROM_DB);
				System.out.println("OUT_PARAM_STATUS_FROM_DB : " + dbStatus);
				code = StatusCode.reverseLookup(dbStatus);
			}

		}
		catch (SQLException e) {
			code = handleException(e);
		}
		finally {
			closeConnection(ps);
		}
		return code;
	}

	/**
	 * This method resets the password to temp and updates the password to user
	 * defined one
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	public StatusCode resetPassword(ServerRequest request)
	        throws BVServerDBException {

		StatusCode code = null;
		getConnection();
		PreparedStatement ps = null;

		try {
			StringBuffer qry = new StringBuffer();
			qry.append(QRY_UPDATE_PWD);
			int resetEnabled = 0;
			if (CMD_RESET_PWD.equals(request.getCommandFlow())) {

				resetEnabled = 1;
			}
			else if (CMD_UPDATE_PWD.equals(request.getCommandFlow())) {

				qry.append(" AND PASSWORD = ?");
			}
			ps = connection.prepareStatement(qry.toString());

			String param1 = CMD_RESET_PWD.equals(request.getCommandFlow()) ? request
			        .getTempPwd() : request.getPassword();

			ps.setString(1, param1);
			ps.setInt(2, resetEnabled);
			ps.setString(3, request.getEmailId());

			if (CMD_UPDATE_PWD.equals(request.getCommandFlow())) {

				ps.setString(4, request.getTempPwd());
			}

			int result = ps.executeUpdate();

			if (CMD_RESET_PWD.equals(request.getCommandFlow())) {

				code = result == 1 ? StatusCode.PWD_RESET_ENABLED
				        : StatusCode.PWD_RESET_FAILED;
			}
			else {

				code = result == 1 ? StatusCode.PWD_UPDATED_SUCCESS
				        : StatusCode.PWD_UPDATE_FAILED;
			}

		}
		catch (SQLException e) {
			code = handleException(e);
		}
		finally {
			closeConnection(ps);
		}
		return code;
	}

	/**
	 * This method updates the Name, Mobile, Country Code and Language
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	public StatusCode updateProfile(ServerRequest request)
	        throws BVServerDBException {

		StatusCode code = null;
		getConnection();
		PreparedStatement ps = null;

		try {
			ps = connection.prepareStatement(QRY_UPDATE_PROFILE);

			ps.setString(1, request.getName());
			ps.setString(2, request.getPhoneNumber());
			ps.setString(3, request.getLanguage());
			ps.setString(4, request.getCountryCode());
			ps.setString(5, request.getEmailId());
			int i = ps.executeUpdate();
			code = i > 0 ? StatusCode.PROFILE_UPDATE_SUCCESS
			        : StatusCode.PROFILE_UPDATE_FAILED;
		}
		catch (SQLException e) {
			code = handleException(e);
		}
		finally {
			closeConnection(ps);
		}
		return code;
	}

	/**
	 * This method updates the new email and mobile Verification code when the
	 * user requests to resends the subscription Verification email
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	public StatusCode updateVerificationCodes(ServerRequest request)
	        throws BVServerDBException {

		StatusCode code = null;
		getConnection();
		PreparedStatement ps = null;

		try {
			ps = connection.prepareCall(SP_UPDATE_VERIFY_CODES);
			ps.setString(1, request.getEmailId());
			ps.setString(2, request.getEmailVerifCode());
			ps.setString(3, request.getMobileVerifCode());

			ps.execute();

			ResultSet rs = ps.getResultSet();
			int dbStatus = 0;
			while (rs.next()) {

				dbStatus = rs.getInt(OUT_PARAM_STATUS_FROM_DB);
				System.out.println("OUT_PARAM_STATUS_FROM_DB : " + dbStatus);
				code = StatusCode.reverseLookup(dbStatus);
			}

		}
		catch (SQLException e) {
			code = handleException(e);
		}
		finally {
			closeConnection(ps);
		}
		return code;
	}

	/**
	 * This method updates the Name, Mobile, Country Code and Language
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	public ServerResponse verifyLogin(ServerRequest request)
	        throws BVServerDBException {

		getConnection();
		PreparedStatement ps = null;
		ServerResponse srvrResponse = null;
		try {

			ps = connection.prepareStatement(QRY_VERIFY_LOGIN);

			ps.setString(1, request.getEmailId());
			ps.execute();

			ResultSet rs = ps.getResultSet();

			while (rs.next()) {
				if (srvrResponse == null) {
					srvrResponse = new ServerResponse();
				}
				int booValue = rs.getInt(OUT_PARAM_ACCT_VERIFIED);
				srvrResponse.setAcctVerified(booValue == 0 ? false : true);

				booValue = rs.getInt(OUT_PARAM_PWD_RESET_ENABLED);
				srvrResponse.setResetPwdEnabled(booValue == 0 ? false : true);
				srvrResponse.setEmailId(request.getEmailId());

				srvrResponse.setDbPassword(rs.getString(OUT_PARAM_PWD));
			}

		}
		catch (SQLException e) {

			if (srvrResponse == null) {

				srvrResponse = new ServerResponse();
			}
			srvrResponse.setCode(handleException(e));
		}
		finally {

			// That means the emailId did not match
			if (srvrResponse == null) {
				srvrResponse = new ServerResponse();
				srvrResponse
				        .setCode(StatusCode.EMAIL_NOT_REGISTERED);
			}
			closeConnection(ps);
		}
		return srvrResponse;
	}

	/**
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	public StatusCode verifySubscription(ServerRequest request)
	        throws BVServerDBException {

		boolean isEmailVerified = isAlreadyVerified(request);
		StatusCode code = null;
		getConnection();
		PreparedStatement ps = null;

		try {

			if (!isEmailVerified) {
				String qry = request.isEmailVerification() ? QRY_UPDATE_VERIFY_EMAIL
				        : QRY_UPDATE_VERIFY_MOBILE;

				ps = connection.prepareStatement(qry);

				String verifCode = request.isEmailVerification() ? request
				        .getEmailVerifCode() : request.getMobileVerifCode();

				ps.setString(1, verifCode);
				ps.setString(2, request.getEmailId());

				int i = ps.executeUpdate();

				if (i > 0) {

					code = StatusCode.ACCT_VERIFIED;
				}
				else {
					code = StatusCode.ACCT_NOT_VERIFIED;
				}
			}
			else {
				code = StatusCode.ACCT_ALREADY_VERIFIED;
			}

		}
		catch (SQLException e) {
			code = handleException(e);
		}
		finally {
			closeConnection(ps);
		}
		return code;
	}

	/**
	 * @param request
	 * @throws BVServerDBException
	 */
	public boolean isEmailExists(String emailaddress) throws BVServerDBException {
		return (getUserByEmail(emailaddress) != null);
	}
	
	/**
	 * @param request
	 * @throws BVServerDBException
	 */
	public UserData getUserByEmail(String emailaddress)
	        throws BVServerDBException {
		getConnection();
		PreparedStatement ps = null;
		try {
			ps = connection.prepareStatement(QRY_GET_USER);
			ps.setString(1, emailaddress);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					int isAccountVerified = rs.getInt(OUT_PARAM_ACCT_VERIFIED);
					int isPwdResetEnabled = rs.getInt(OUT_PARAM_PWD_RESET_ENABLED);
					UserData userData = new UserData(rs.getInt(OUT_PARAM_USER_LOGIN_ID));
					userData.setName(OUT_PARAM_NAME);
					userData.setEmailId(OUT_PARAM_EMAIL);
					userData.setPassword(OUT_PARAM_PWD);
					userData.setPassword(OUT_PARAM_MOBILE_NUMBER);
					userData.setCountryCode(rs.getInt(OUT_PARAM_COUNTRY_CODE));
					userData.setLanguage(OUT_PARAM_LANGUAGE);
					userData.setIsAccountVerified(isAccountVerified == 0 ? false : true);
					userData.setIsPwdResetEnabled(isPwdResetEnabled == 0 ? false : true);
					return userData;
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			throw new BVServerDBException(e.getMessage());
		}
		finally {
			closeConnection(ps);
		}

		return null;
	}
}

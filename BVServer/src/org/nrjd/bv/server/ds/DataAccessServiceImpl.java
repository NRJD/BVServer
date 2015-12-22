/**
 * 
 */
package org.nrjd.bv.server.ds;

import static org.nrjd.bv.server.dto.ServerConstant.CMD_RESET_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.CMD_UPDATE_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.OUT_PARAM_STATUS_FROM_DB;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_IS_ACCT_EMAIL_VERIFIED;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_IS_ACCT_MOBILE_VERIFIED;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_UPDATE_PROFILE;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_UPDATE_PWD;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_UPDATE_VERIFY_EMAIL;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_UPDATE_VERIFY_MOBILE;
import static org.nrjd.bv.server.dto.ServerConstant.SP_PERSIST_USER;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.nrjd.bv.server.dto.ServerRequest;
import org.nrjd.bv.server.dto.StatusCode;

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

				System.out.println("Connection Established !!!! ");
				connection = DriverManager.getConnection(
				        "jdbc:mysql://localhost:3306/bv", "root", "gurudev");

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
}

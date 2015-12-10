/**
 * 
 */
package org.nrjd.bv.server.ds;

import static org.nrjd.bv.server.dto.ServerConstant.QRY_IS_ACCT_EMAIL_VERIFIED;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_IS_ACCT_MOBILE_VERIFIED;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_PERSIST_USER;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_UPDATE_VERIFY_EMAIL;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_UPDATE_VERIFY_MOBILE;

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

		StatusCode code = null;
		try {
			connection.rollback();

		}
		catch (SQLException e1) {
			// Ignore the Exception
		}
		finally {

			if (e.getMessage() != null
			        && e.getMessage().contains("EMAIL_ID_UNIQUE")) {
				code = StatusCode.STATUS_DUPL_EMAILID;
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
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	public StatusCode persistUser(ServerRequest request)
	        throws BVServerDBException {

		StatusCode code = null;
		getConnection();
		PreparedStatement ps = null;

		try {
			ps = connection.prepareStatement(QRY_PERSIST_USER);

			ps.setInt(1, 0);
			ps.setString(2, request.getName());
			ps.setString(3, request.getEmailId());
			ps.setString(4, request.getPassword());
			ps.setString(5, request.getPhoneNumber());
			ps.setString(6, request.getLanguage());
			ps.setInt(7, 0);
			ps.setString(8, request.getEmailVerifCode());
			ps.setString(9, request.getMobileVerifCode());

			int i = ps.executeUpdate();

			if (i > 0) {

				code = StatusCode.STATUS_USER_ADDED;
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

					code = StatusCode.STATUS_ACCT_VERIFIED;
				}
				else {
					code = StatusCode.STATUS_ACCT_NOT_VERIFIED;
				}
			}
			else {
				code = StatusCode.STATUS_ACCT_ALREADY_VERIFIED;
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

/**
 * 
 */
package org.nrjd.bv.server.ds;

import static org.nrjd.bv.server.dto.ServerConstant.QRY_PERSIST_USER;
import static org.nrjd.bv.server.dto.ServerConstant.QRY_VERIFY_EMAIL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
				if (!connection.isClosed()) {

					ps.close();
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
	public StatusCode verifyEmail(ServerRequest request)
	        throws BVServerDBException {

		StatusCode code = null;
		getConnection();
		PreparedStatement ps = null;

		try {
			ps = connection.prepareStatement(QRY_VERIFY_EMAIL);

			ps.setString(1, request.getEmailVerifCode());

			int i = ps.executeUpdate();

			if (i > 0) {

				code = StatusCode.STATUS_EMAIL_VERIFIED;
			}
			else {
				code = StatusCode.STATUS_EMAIL_NOT_VERIFIED;
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

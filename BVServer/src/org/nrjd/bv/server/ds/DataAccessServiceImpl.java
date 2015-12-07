/**
 * 
 */
package org.nrjd.bv.server.ds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.nrjd.bv.server.dto.DataAccessRequest;
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

			if (connection == null) {

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
	public StatusCode persistUser(DataAccessRequest request)
	        throws BVServerDBException {

		StatusCode code = null;
		getConnection();
		PreparedStatement ps = null;

		try {
			ps = connection
			        .prepareStatement("insert into user_login values(?,?,?,?,?,?,?)");

			ps.setInt(1, 0);
			ps.setString(2, request.getName());
			ps.setString(3, request.getEmailId());
			ps.setString(4, request.getPassword());
			ps.setString(5, request.getPhoneNumber());
			ps.setString(6, request.getLanguage());
			ps.setInt(7, 0);

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
}

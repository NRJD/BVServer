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
	 * @param request
	 * @throws BVServerDBException
	 */
	public StatusCode persistUser(DataAccessRequest request)
	        throws BVServerDBException {

		StatusCode code = null;
		getConnection();

		try {
			PreparedStatement ps = connection
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
			e.printStackTrace();
			throw new BVServerDBException(e.getMessage());
		}
		return code;
	}
}

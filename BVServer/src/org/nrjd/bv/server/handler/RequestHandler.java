/**
 * 
 */
package org.nrjd.bv.server.handler;

import static org.nrjd.bv.server.dto.ServerConstant.ACTION_USER_REG;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nrjd.bv.server.ds.BVServerDBException;
import org.nrjd.bv.server.ds.DataAccessServiceImpl;
import org.nrjd.bv.server.dto.DataAccessRequest;
import org.nrjd.bv.server.dto.StatusCode;
import org.nrjd.bv.server.util.EmailUtil;

/**
 * @author Sathya
 * 
 */
public class RequestHandler {

	/**
	 * This process the request and based on the Request dispatch Path delegates
	 * to the respective flow like User REgistry, Email Verification, Password
	 * Reset, Update profile ,download books, etc
	 * 
	 * @param request
	 * @param response
	 * @throws BVServerDBException
	 */
	public StatusCode processRequest(HttpServletRequest request,
	        HttpServletResponse response) throws BVServerDBException {

		System.out.println(">>> processRequest");
		StatusCode status = null;
		String requestURI = request.getRequestURI();

		if (requestURI != null && requestURI.length() > 0) {

			if (requestURI.contains(ACTION_USER_REG)) {

				status = registerUser(request);
			}
		}

		System.out.println("<<< processRequest " + status);
		return status;
	}

	/**
	 * This method registers the User and sends an EMail for Verification.
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	private StatusCode registerUser(HttpServletRequest request)
	        throws BVServerDBException {

		System.out.println(">>> registerUser");

		String name = request.getParameter("name");
		String pwd = request.getParameter("password");
		String email = request.getParameter("email");
		String lang = request.getParameter("language");
		String mobile = request.getParameter("phoneNum");

		DataAccessRequest dbReq = new DataAccessRequest();
		dbReq.setEmailId(email);
		dbReq.setLanguage(lang);
		dbReq.setName(name);
		dbReq.setPassword(pwd);
		dbReq.setPhoneNumber(mobile);

		StatusCode status = new DataAccessServiceImpl().persistUser(dbReq);

		if (status != null && status == StatusCode.STATUS_USER_ADDED) {

			EmailUtil.sendEmail(email);
		}

		System.out.println("<<< registerUser " + status);
		return status;
	}
}

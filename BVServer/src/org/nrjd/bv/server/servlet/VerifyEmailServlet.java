package org.nrjd.bv.server.servlet;

import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_WELCOME;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_EMAIL_ID;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_VERIF_CODE;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nrjd.bv.server.ds.BVServerDBException;
import org.nrjd.bv.server.ds.DataAccessServiceImpl;
import org.nrjd.bv.server.dto.ServerRequest;
import org.nrjd.bv.server.dto.StatusCode;
import org.nrjd.bv.server.util.EmailUtil;

/**
 * Servlet implementation class HandleRequestServlet
 */
public class VerifyEmailServlet extends HttpServlet {

	private static final long	serialVersionUID	= -9182901800695800141L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public VerifyEmailServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException, IOException {

		StatusCode code = null;
		String verifCode = request.getParameter(KEY_VERIF_CODE);
		String emailId = request.getParameter(KEY_EMAIL_ID);

		System.out.println(">>> doGet verifCode : " + verifCode
		        + "\n emailId : " + emailId);

		ServerRequest dbReq = new ServerRequest();
		dbReq.setEmailVerifCode(verifCode);
		dbReq.setEmailId(emailId);
		dbReq.setEmailVerification(true);

		boolean isSuccess = false;
		try {
			if (verifCode != null && emailId != null && verifCode.length() > 0
			        && emailId.length() > 0) {

				code = new DataAccessServiceImpl().verifySubscription(dbReq);

				if (code == StatusCode.ACCT_VERIFIED) {

					EmailUtil.sendEmail(dbReq, EMAIL_SUBJECT_WELCOME);
					isSuccess = true;
				}
			}
		}
		catch (BVServerDBException e) {
			e.printStackTrace();
		}
		if (isSuccess) {
			response.sendRedirect("VerifyEmailSuccess.jsp");
		}
		else {
			String reDirectPage = code == StatusCode.ACCT_ALREADY_VERIFIED ? "DupVerifyEmail.jsp"
			        : "VerifyEmailFailed.jsp";
			response.sendRedirect(reDirectPage);
		}
		System.out.println("<<< doGet ");
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

}

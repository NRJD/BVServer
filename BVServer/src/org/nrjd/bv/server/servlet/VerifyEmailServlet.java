package org.nrjd.bv.server.servlet;

import static org.nrjd.bv.server.dto.ServerConstant.EMAIL_SUBJECT_WELCOME;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_EMAIL_ID;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_VERIF_CODE;

import java.io.IOException;
import java.io.PrintWriter;

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
import org.nrjd.bv.server.util.JSONHelper;

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

		System.out.println(">>> doGet verifCode : " + verifCode);

		ServerRequest dbReq = new ServerRequest();
		dbReq.setEmailVerifCode(verifCode);
		dbReq.setEmailId(emailId);
		String jsonResponse = null;
		try {
			code = new DataAccessServiceImpl().verifyEmail(dbReq);

			EmailUtil.sendEmail(dbReq, EMAIL_SUBJECT_WELCOME);
			jsonResponse = JSONHelper.getJSonResponse(null, code);

			response.sendRedirect("EmailVerified.jsp");
		}
		catch (BVServerDBException e) {
			e.printStackTrace();
			jsonResponse = JSONHelper.getJSonResponse(null,
			        StatusCode.STATUS_ERROR_SERVER);
		}
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.print(jsonResponse);
		out.close();
		System.out.println("<<< doGet " + jsonResponse);
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

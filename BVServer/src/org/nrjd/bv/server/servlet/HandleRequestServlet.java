package org.nrjd.bv.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nrjd.bv.server.ds.BVServerDBException;
import org.nrjd.bv.server.dto.StatusCode;
import org.nrjd.bv.server.handler.RequestHandler;
import org.nrjd.bv.server.util.CommonUtility;

/**
 * Servlet implementation class HandleRequestServlet
 */
@WebServlet("/HandleRequestServlet")
public class HandleRequestServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	ServletContext	          context;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HandleRequestServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		StatusCode status = null;
		try {
			status = new RequestHandler().processRequest(request, response);
		}
		catch (BVServerDBException e) {
			e.printStackTrace();
			status = StatusCode.STATUS_ERROR_DB;
		}

		out.print(CommonUtility.getMessage(status));
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

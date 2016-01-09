package org.nrjd.bv.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nrjd.bv.server.handler.MobileRequestHandler;

/**
 * Servlet implementation class HandleRequestServlet
 */
public class MobileRequestServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5586492332079327778L;
	ServletContext	          context;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MobileRequestServlet() {
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

		System.out.println(">>> doPost ");

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String jsonResponse = new MobileRequestHandler().processRequest(
		        request, response);
		out.print(jsonResponse);
		out.close();
		System.out.println("<<< doPost ");
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

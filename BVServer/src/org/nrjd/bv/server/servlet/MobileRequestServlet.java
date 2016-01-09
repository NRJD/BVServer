package org.nrjd.bv.server.servlet;

import static org.nrjd.bv.server.dto.ServerConstant.ENCODING_FORMAT;
import static org.nrjd.bv.server.dto.ServerConstant.JSON_CONTENT_TYPE;

import java.io.IOException;

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

		String jsonResponse = new MobileRequestHandler().processRequest(
		        request, response);

		response.setContentType(JSON_CONTENT_TYPE);
		response.setCharacterEncoding(ENCODING_FORMAT);
		response.getWriter().write(jsonResponse);
		response.getWriter().close();
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

package org.nrjd.bv.server.servlet;

import static org.nrjd.bv.server.dto.ServerConstant.ENCODING_FORMAT;
import static org.nrjd.bv.server.dto.ServerConstant.JSON_CONTENT_TYPE;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_FILE_NAME;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_LANG;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class DownloadServlet
 */
public class DownloadServlet extends HttpServlet {
	private static final long	serialVersionUID	= 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException {

		ServletContext context = getServletContext();
		String location = context.getInitParameter("BVManualLocation");
		String lang = request.getParameter(KEY_LANG);
		String fileName = request.getParameter(KEY_FILE_NAME);
		StringBuffer buf = new StringBuffer();
		buf.append(location).append(lang).append(File.separator)
		        .append(fileName);

		File file = new File(buf.toString());

		FileInputStream fis;
		try {
			fis = new FileInputStream(file.getAbsolutePath());
			// // if you want to use a relative path to context root:
			// String relativePath = getServletContext().getRealPath("");
			// System.out.println("relativePath = " + relativePath);

			String mimeType = context.getMimeType(fileName);

			if (mimeType == null) {

				mimeType = "application/octet-stream";
			}
			int len = (int) file.length();
			response.setContentType(mimeType);
			response.setContentLength(len);
			response.setHeader("Content-Disposition", "attachment; filename="
			        + file.getName());

			OutputStream os = response.getOutputStream();
			byte[] buffer = new byte[len];
			int bytesRead = -1;

			while ((bytesRead = fis.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}

			fis.close();
			os.close();
		}
		catch (FileNotFoundException e) {
			handleException(fileName, response);
		}
		catch (IOException e) {
			handleException(fileName, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
	        HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	private void handleException(String msg, HttpServletResponse response) {

		try {
			response.setContentType(JSON_CONTENT_TYPE);
			response.setCharacterEncoding(ENCODING_FORMAT);
			response.getWriter().write(msg + " Not found");
			response.getWriter().close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

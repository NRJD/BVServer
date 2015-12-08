/**
 * 
 */
package org.nrjd.bv.server.handler;

import static org.nrjd.bv.server.dto.ServerConstant.ACTION_USER_REG;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_EMAIL_ID;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_LANG;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_NAME;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_PHONE;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_PWD;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.nrjd.bv.server.ds.BVServerDBException;
import org.nrjd.bv.server.ds.DataAccessServiceImpl;
import org.nrjd.bv.server.dto.BVServerException;
import org.nrjd.bv.server.dto.ServerRequest;
import org.nrjd.bv.server.dto.StatusCode;
import org.nrjd.bv.server.util.EmailUtil;
import org.nrjd.bv.server.util.JSONHelper;

/**
 * @author Sathya
 * 
 */
public class RequestHandler {

	/**
	 * 
	 * @param inputData
	 * @return
	 * @throws IOException
	 */
	public static String getRequestData(InputStream inputData)
	        throws BVServerException {

		InputStreamReader isr = new InputStreamReader(inputData);
		BufferedReader br = new BufferedReader(isr);
		try {
			StringBuffer xmlBuffer = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				xmlBuffer.append(line);
			}
			String xmlData = xmlBuffer.toString();
			System.out.println("XML Request Data: " + xmlData);
			return xmlData;
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new BVServerException(e.getMessage());
		}
		finally {
			try {
				br.close();
			}
			catch (Exception e) { /* Ignore Exception */
			}
			try {
				isr.close();
			}
			catch (Exception e) { /* Ignore Exception */
			}
		}
	}

	/**
	 * This process the request and based on the Request dispatch Path delegates
	 * to the respective flow like User REgistry, Email Verification, Password
	 * Reset, Update profile ,download books, etc
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @throws BVServerDBException
	 */
	public String processRequest(HttpServletRequest request,
	        HttpServletResponse response) {

		System.out.println(">>> processRequest");
		String jsonResponse = null;
		String requestURI = request.getRequestURI();

		try {
			JSONObject json = readData(request);

			System.out.println("Read Data : " + json);
			if (requestURI != null && requestURI.length() > 0) {

				if (requestURI.contains(ACTION_USER_REG)) {

					jsonResponse = registerUser(json);
				}
			}
		}
		catch (BVServerException e) {
			e.printStackTrace();
			jsonResponse = JSONHelper.getJSonResponse(null,
			        StatusCode.STATUS_ERROR_SERVER);
		}

		System.out.println("<<< processRequest " + jsonResponse);
		return jsonResponse;
	}

	/**
	 * To Read The JSON data
	 * 
	 * @param request
	 * @return
	 * @throws BVServerException
	 */
	private JSONObject readData(HttpServletRequest request)
	        throws BVServerException {
		InputStream is = null;
		try {
			is = request.getInputStream();
			String requestXmlData = getRequestData(is);

			JSONObject json = JSONHelper.parseJSONRequest(requestXmlData);
			return json;
		}
		catch (Exception e) {

			throw new BVServerException(
			        "Error while retriving the request data", e);
		}
		finally {
			try {
				is.close();
			}
			catch (Exception e) {

				// Ignore the Exception
			}
		}
	}

	/**
	 * This method registers the User and sends an EMail for Verification.
	 * 
	 * @param request
	 * @throws BVServerDBException
	 */
	private String registerUser(JSONObject json) {

		System.out.println(">>> registerUser");
		StatusCode status = null;
		String jsonResponse = null;
		String email = null;
		try {
			String name = (String) json.get(KEY_NAME);
			String pwd = (String) json.get(KEY_PWD);
			email = (String) json.get(KEY_EMAIL_ID);
			String lang = (String) json.get(KEY_LANG);
			String mobile = (String) json.get(KEY_PHONE);

			UUID uuid = UUID.randomUUID();
			String emailVerifCode = uuid.toString().replaceAll("-", "")
			        .toUpperCase();
			int mobVerifCode = (100000 + new Random().nextInt(899999));

			ServerRequest srvrReq = new ServerRequest();
			srvrReq.setEmailId(email);
			srvrReq.setLanguage(lang);
			srvrReq.setName(name);
			srvrReq.setPassword(pwd);
			srvrReq.setPhoneNumber(mobile);
			srvrReq.setEmailVerifCode(emailVerifCode);
			srvrReq.setMobileVerifCode(String.valueOf(mobVerifCode));

			status = new DataAccessServiceImpl().persistUser(srvrReq);

			if (status != null && status == StatusCode.STATUS_USER_ADDED) {

				EmailUtil.sendEmail(srvrReq);
			}
		}
		catch (BVServerDBException e) {
			e.printStackTrace();
			status = StatusCode.STATUS_ERROR_DB;
		}
		Map<String, String> resMap = new TreeMap<String, String>();
		resMap.put(KEY_EMAIL_ID, email);
		jsonResponse = JSONHelper.getJSonResponse(resMap, status);

		System.out.println("<<< registerUser " + jsonResponse);
		return jsonResponse;
	}
}

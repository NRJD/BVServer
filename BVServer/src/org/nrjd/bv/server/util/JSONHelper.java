/**
 * 
 */
package org.nrjd.bv.server.util;

import static org.nrjd.bv.server.dto.ServerConstant.KEY_CODE;
import static org.nrjd.bv.server.dto.ServerConstant.KEY_MSG;

import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.nrjd.bv.server.dto.StatusCode;

/**
 * @author Sathya
 * 
 */
public final class JSONHelper {

	private static boolean	verbose	= true;

	/**
	 * This method generates a JSON Response
	 * 
	 * @param emailId
	 * @param code
	 * @return
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public static String getJSonResponse(Map<String, String> values,
	        StatusCode code) {

		System.out.print(">>> getJSonResponse ");
		String jsonText = null;
		if (values != null && !values.isEmpty()) {

			Set<String> keySet = values.keySet();
			JSONObject json = new JSONObject();
			json.put(KEY_CODE, code.getStatusCode());

			// Set the Status Literal only when the Verbose is true. TODO : how
			// to set this up. Satya Pr will send a seperate command to enable
			// the config.Future enhancement
			if (verbose) {

				json.put(KEY_MSG, CommonUtility.getMessage(code));
			}
			for (String key : keySet) {

				json.put(key, values.get(key));

			}
			jsonText = json.toString();
			System.out.print("<<< getJSonResponse ");
		}

		return jsonText;
	}

	/**
	 * 
	 * @param jsonReq
	 * @return
	 */
	public static JSONObject parseJSONRequest(String jsonReq) {

		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(jsonReq);

		}
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;

	}

	private JSONHelper() {

	}
}

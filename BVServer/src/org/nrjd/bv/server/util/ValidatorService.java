/**
 * 
 */
package org.nrjd.bv.server.util;

import static org.nrjd.bv.server.dto.ServerConstant.KEY_EMAIL_ID;

import java.util.Map;
import java.util.TreeMap;

import org.nrjd.bv.server.dto.ServerRequest;
import org.nrjd.bv.server.dto.StatusCode;

/**
 * @author Sathya
 * 
 */
public final class ValidatorService {

	public static String validate(ServerRequest request) {

		StatusCode status = null;
		String jsonResponse = null;
		Map<String, String> resMap = new TreeMap<String, String>();

		jsonResponse = JSONHelper.getJSonResponse(resMap, status);

		if (request != null) {

			if (request.getEmailId() == null) {

				resMap.put(KEY_EMAIL_ID, "");
			}
		}
		return jsonResponse;
	}

	private ValidatorService() {

	}
}

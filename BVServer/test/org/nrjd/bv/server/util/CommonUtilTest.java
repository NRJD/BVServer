/**
 * 
 */
package org.nrjd.bv.server.util;

import org.nrjd.bv.server.ds.DataAccessServiceImpl;
import org.nrjd.bv.server.dto.ServerRequest;
import org.nrjd.bv.server.dto.ServerResponse;

/**
 * @author Sathya
 * 
 */
public class CommonUtilTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		// CommonUtility.generateTempPassword();

		ServerRequest srvrReq = new ServerRequest();
		srvrReq.setEmailId("kinnu_sathya@yahoo.com");
		srvrReq.setPassword("sathya");
		ServerResponse srvrResponse = new DataAccessServiceImpl()
		        .verifyLogin(srvrReq);

		String encryptedPwd = PasswordHandler.encryptPassword(srvrReq
		        .getPassword());
		boolean isMatched = PasswordHandler.validatePassword(encryptedPwd,
		        srvrResponse.getDbPassword());

		System.out.println(isMatched);

	}
}

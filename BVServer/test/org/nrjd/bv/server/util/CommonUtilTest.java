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

		validateMD5Pwd(srvrReq);

	}

	private static void validateMD5Pwd(ServerRequest srvrReq) throws Exception {

		ServerResponse srvrResponse = new DataAccessServiceImpl()
		        .verifyLogin(srvrReq);

		boolean validateMD5Password = PasswordHandler.validateMD5Password(
		        srvrReq.getPassword(), srvrResponse.getDbPassword());
		System.out.println(validateMD5Password);
	}

	private static void validateStringPwd(String loginPwd, String dbPwd) {

	}
}

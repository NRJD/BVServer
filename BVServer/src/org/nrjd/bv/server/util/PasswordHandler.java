/**
 * 
 */
package org.nrjd.bv.server.util;

import static org.nrjd.bv.server.dto.ServerConstant.PWD_ALGO_MD5;
import static org.nrjd.bv.server.dto.ServerConstant.PWD_ALGO_PBKD;
import static org.nrjd.bv.server.dto.ServerConstant.PWD_ALGO_SHA1;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.nrjd.bv.server.dto.BVServerException;

/**
 * @author Sathya
 * 
 */
public class PasswordHandler {

	/**
	 * 
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static String encryptPassword(String userProvidedPwd)
	        throws BVServerException {

		String generatedSecuredPasswordHash = null;
		long start = System.currentTimeMillis();
		try {
			generatedSecuredPasswordHash = generateStorngPasswordHash(userProvidedPwd);

			// generatedSecuredPasswordHash =
			// getMD5SecurePassword(userProvidedPwd);
			System.out.println(generatedSecuredPasswordHash);
		}
		catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		System.out.println("Total Time for PWD  " + (end - start) + " Ms");
		return generatedSecuredPasswordHash;
	}

	/**
	 * 
	 * @param hex
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2),
			        16);
		}
		return bytes;
	}

	/**
	 * 
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static String generateStorngPasswordHash(String password)
	        throws NoSuchAlgorithmException, InvalidKeySpecException {

		int iterations = 1971;
		char[] chars = password.toCharArray();
		byte[] salt = getSalt().getBytes();

		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(PWD_ALGO_PBKD);
		byte[] hash = skf.generateSecret(spec).getEncoded();
		// return iterations + ":" + toHex(salt) + ":" + toHex(hash);

		return toHex(salt) + ":" + toHex(hash);
	}

	/**
	 * 
	 * @param passwordToHash
	 * @param salt
	 * @return
	 */
	public static String getMD5SecurePassword(String passwordToHash) {
		String generatedPassword = null;
		String salt = null;

		try {

			salt = getSalt();
			// Create MessageDigest instance for MD5
			MessageDigest md = MessageDigest.getInstance(PWD_ALGO_MD5);
			// Add password bytes to digest
			md.update(salt.getBytes());
			// Get the hash's bytes
			byte[] bytes = md.digest(passwordToHash.getBytes());
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
				        .substring(1));
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * Generates the Random Salt based on SHA1PRNG algorithm
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static String getSalt() throws NoSuchAlgorithmException {

		SecureRandom sr = SecureRandom.getInstance(PWD_ALGO_SHA1);
		byte[] salt = new byte[16];
		sr.nextBytes(salt);
		return salt.toString();
	}

	/**
	 * 
	 * @param array
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static String toHex(byte[] array) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		}
		else {
			return hex;
		}
	}

	/**
	 * 
	 * @param originalPassword
	 * @param storedPassword
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static boolean validatePassword(String originalPassword,
	        String storedPassword) throws NoSuchAlgorithmException,
	        InvalidKeySpecException {

		String[] parts = storedPassword.split(":");
		int iterations = 1971;
		byte[] salt = fromHex(parts[0]);
		byte[] hash = fromHex(parts[1]);

		PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt,
		        iterations, hash.length * 8);
		SecretKeyFactory skf = SecretKeyFactory
		        .getInstance("PBKDF2WithHmacSHA1");
		byte[] testHash = skf.generateSecret(spec).getEncoded();

		int diff = hash.length ^ testHash.length;
		for (int i = 0; i < hash.length && i < testHash.length; i++) {
			diff |= hash[i] ^ testHash[i];
		}
		return diff == 0;
	}
}

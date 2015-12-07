/**
 * 
 */
package org.nrjd.bv.server.dto;

/**
 * @author Sathya
 * 
 */
public class BVServerException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2560386052026142210L;

	public BVServerException() {
		super();
	}

	public BVServerException(String msg) {

		super(msg);
	}

	public BVServerException(String msg, Throwable t) {
		super(msg, t);
	}

	public BVServerException(Throwable t) {
		super(t);
	}

}

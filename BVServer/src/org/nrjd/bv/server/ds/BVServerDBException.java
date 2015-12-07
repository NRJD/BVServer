/**
 * 
 */
package org.nrjd.bv.server.ds;

/**
 * @author Sathya
 * 
 */
public class BVServerDBException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -5857576827971323080L;

	public BVServerDBException() {
		super();
	}

	public BVServerDBException(String msg) {

		super(msg);
	}

	public BVServerDBException(String msg, Throwable t) {
		super(msg, t);
	}

	public BVServerDBException(Throwable t) {
		super(t);
	}

}

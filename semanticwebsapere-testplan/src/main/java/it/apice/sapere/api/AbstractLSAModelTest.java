package it.apice.sapere.api;

/**
 * <p>
 * This is a base class for API test. It does not provide any test case, just a
 * set of elements required to run tests.
 * </p>
 * <p>
 * THIS CLASS SHOULD NOT BE EXTENDED.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractLSAModelTest {

	/**
	 * <p>
	 * Creates a new LSAFactory.
	 * </p>
	 * 
	 * @return A reference to a LSAFactory
	 */
	protected abstract LSAFactory createFactory();

	/**
	 * <p>
	 * Creates a new Privileged LSAFactory.
	 * </p>
	 * 
	 * @return A reference to an PrivilegedLSAFactory
	 */
	protected abstract PrivilegedLSAFactory createPrivilegedFactory();
}

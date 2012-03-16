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
public abstract class AbstractModelTest {

	/**
	 * <p>
	 * Creates a new SAPEREFactory.
	 * </p>
	 * 
	 * @return A reference to a SAPEREFactory
	 */
	protected abstract SAPEREFactory createFactory();

	/**
	 * <p>
	 * Creates a new Extended SAPEREFactory.
	 * </p>
	 * 
	 * @return A reference to an ExtSAPEREFactory
	 */
	protected abstract ExtSAPEREFactory createExtFactory();
}

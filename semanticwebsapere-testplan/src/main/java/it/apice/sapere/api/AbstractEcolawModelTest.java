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
public abstract class AbstractEcolawModelTest extends AbstractLSAModelTest {

	/**
	 * <p>
	 * Creates a new EcolawFactory.
	 * </p>
	 * 
	 * @return A reference to a EcolawFactory
	 */
	protected abstract EcolawFactory createEcolawFactory();

}

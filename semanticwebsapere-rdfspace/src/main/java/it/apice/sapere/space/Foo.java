package it.apice.sapere.space;

import com.hp.hpl.jena.rdf.model.InfModel;

/**
 * <p>
 * This is a stupid class created in order to run some dependencies test.
 * </p>
 *
 * @author conteit
 *
 */
public interface Foo {

	/**
	 * <p>
	 * Stupid method for test.
	 * </p>
	 *
	 * @return Something from Jena (dependency)
	 */
	InfModel foo();
}

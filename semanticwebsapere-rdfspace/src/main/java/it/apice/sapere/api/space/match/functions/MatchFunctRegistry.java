package it.apice.sapere.api.space.match.functions;

import java.net.URI;

import com.hp.hpl.jena.sparql.function.Function;
import com.hp.hpl.jena.sparql.function.FunctionFactory;

/**
 * <p>
 * This interface models a service which is capable of registering custom
 * functions to be used by SPARQL and SPARUL queries.
 * </p>
 * <p>
 * Further information on how to deal with ARQ Custom Functions can be found at
 * {@link http://jena.sourceforge.net/ARQ/library-function.html}.
 * 
 * @author Paolo Contessi
 * 
 */
public interface MatchFunctRegistry {

	/**
	 * <p>
	 * Registers a new Custom Function.
	 * </p>
	 * 
	 * @param uri
	 *            The identifier of the function (that should be used for
	 *            invocation)
	 * @param function
	 *            The function to be used
	 */
	void register(URI uri, Class<? extends Function> function);

	/**
	 * <p>
	 * Registers a new Custom Function, via its factory.
	 * </p>
	 * <p>
	 * NB: if the inserted URI is already known then the new provided factory
	 * substitutes the old one.
	 * </p>
	 * 
	 * @param uri
	 *            The identifier of the function (that should be used for
	 *            invocation)
	 * @param fFactory
	 *            The factory capable of constructing the function to be used
	 */
	void register(URI uri, FunctionFactory fFactory);

	/**
	 * <p>
	 * Cancels the registration of the specified function.
	 * </p>
	 * 
	 * @param uri
	 *            The identifier of the function
	 */
	void cancel(URI uri);
}

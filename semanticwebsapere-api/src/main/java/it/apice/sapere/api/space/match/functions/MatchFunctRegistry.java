package it.apice.sapere.api.space.match.functions;

import java.net.URI;

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
 * @param <FunctionType>
 *            The basic type for a custom function usable by the engine
 */
public interface MatchFunctRegistry<FunctionType> {

	/**
	 * <p>
	 * Creates a new URI which will be used to identified a registered function.
	 * </p>
	 * <p>
	 * This is an utility method meant to be used to provide a standard
	 * namespace for all SAPERE custom functions.
	 * </p>
	 * 
	 * @param fname
	 *            The name of the function to be registered
	 * @return The corresponding URI
	 */
	URI buildFunctionURI(String fname);

	/**
	 * <p>
	 * Creates a new URI which will be used to identified a registered function.
	 * </p>
	 * <p>
	 * This is an utility method meant to be used to provide a standard
	 * namespace for all SAPERE custom functions.
	 * </p>
	 * 
	 * @param fname
	 *            The name of the function to be registered
	 * @param folder
	 *            A sub-folder to be used to organize some function in
	 *            sub-namespaces
	 * @return The corresponding URI
	 */
	URI buildFunctionURI(String folder, String fname);

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
	void register(URI uri, Class<? extends FunctionType> function);

	/**
	 * <p>
	 * Cancels the registration of the specified function.
	 * </p>
	 * 
	 * @param uri
	 *            The identifier of the function
	 */
	void cancel(URI uri);

	/**
	 * <p>
	 * Retrieves a list of all custom functions that has been registered.
	 * </p>
	 * 
	 * @return A list of all registered custom functions' identifiers
	 */
	URI[] getKnownFunctionsURIs();
}

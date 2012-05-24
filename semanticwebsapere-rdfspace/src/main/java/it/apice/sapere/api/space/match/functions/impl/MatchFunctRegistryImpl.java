package it.apice.sapere.api.space.match.functions.impl;

import it.apice.sapere.api.space.match.functions.MatchFunctRegistry;

import java.net.URI;

import com.hp.hpl.jena.sparql.function.Function;
import com.hp.hpl.jena.sparql.function.FunctionFactory;
import com.hp.hpl.jena.sparql.function.FunctionRegistry;

/**
 * <p>
 * This class implements the {@link MatchFunctRegistry} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class MatchFunctRegistryImpl implements MatchFunctRegistry {

	/** Singleton instance. */
	private static transient MatchFunctRegistry instance;

	@Override
	public final void register(final URI uri,
			final Class<? extends Function> function) {
		FunctionRegistry.get().put(uri.toString(), function);
	}

	@Override
	public final void register(final URI uri, final FunctionFactory fFactory) {
		FunctionRegistry.get().put(uri.toString(), fFactory);

	}

	@Override
	public final void cancel(final URI uri) {
		FunctionRegistry.get().remove(uri.toString());
	}

	/**
	 * <p>
	 * Singleton method.
	 * </p>
	 * 
	 * @return The singleton instance
	 */
	public static MatchFunctRegistry getInstance() {
		if (instance == null) {
			instance = new MatchFunctRegistryImpl();
		}

		return instance;
	}
}

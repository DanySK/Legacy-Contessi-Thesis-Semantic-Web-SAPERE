package it.apice.sapere.api.space.match.functions.impl;

import it.apice.sapere.api.space.match.functions.MatchFunctRegistry;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.sparql.function.Function;
import com.hp.hpl.jena.sparql.function.FunctionRegistry;

/**
 * <p>
 * This class implements the {@link MatchFunctRegistry} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class MatchFunctRegistryImpl implements MatchFunctRegistry<Function> {

	/** SAPERE custom functions' standard namespace. */
	private static final transient String FUNCT_NS = "http://"
			+ "www.sapere-project.eu/functions";

	/** Singleton instance. */
	private static transient MatchFunctRegistry<Function> instance;

	@Override
	public final void register(final URI uri,
			final Class<? extends Function> function) {
		FunctionRegistry.get().put(uri.toString(), function);
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
	public static MatchFunctRegistry<Function> getInstance() {
		if (instance == null) {
			instance = new MatchFunctRegistryImpl();
		}

		return instance;
	}

	@Override
	public URI buildFunctionURI(final String fname) {
		return buildFunctionURI(null, fname);
	}

	@Override
	public URI buildFunctionURI(final String folder, final String fname) {
		if (folder == null) {
			return URI.create(String.format("%s/%s", FUNCT_NS, escape(fname)));
		}

		return URI.create(String.format("%s/%s/%s", FUNCT_NS, escape(folder),
				escape(fname)));
	}

	/**
	 * <p>
	 * Utility function that encodes the provided string a URL/URI compatible
	 * manner.
	 * </p>
	 * 
	 * @param src
	 *            A String to be escaped
	 * @return The escaped String
	 */
	private String escape(final String src) {
		try {
			return URLEncoder.encode(src, "ASCII");
		} catch (UnsupportedEncodingException e) {
			assert e == null;
			return src;
		}
	}

	@Override
	public URI[] getKnownFunctionsURIs() {
		final Iterator<String> iter = FunctionRegistry.get().keys();
		final List<URI> keys = new LinkedList<URI>();

		while (iter.hasNext()) {
			keys.add(URI.create(iter.next()));
		}

		return keys.toArray(new URI[keys.size()]);
	}
}

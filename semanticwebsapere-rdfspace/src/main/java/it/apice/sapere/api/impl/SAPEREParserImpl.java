package it.apice.sapere.api.impl;

import it.apice.sapere.api.ExtSAPEREFactory;
import it.apice.sapere.api.SAPEREParser;
import it.apice.sapere.api.internal.JenaSAPEREConverter;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.nodes.SAPEREException;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * <p>
 * Implementation of SAPEREParser.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see SAPEREParser
 * 
 */
public class SAPEREParserImpl implements SAPEREParser {

	/** Base used in relative URI resolution. */
	private static final transient String BASE_URI = null;

	/** Converter. */
	private final transient JenaSAPEREConverter processor;

	/**
	 * <p>
	 * Builds new SAPEREParserImpl.
	 * </p>
	 * 
	 * @param aFactory
	 *            SAPERE Model Factory
	 */
	public SAPEREParserImpl(final ExtSAPEREFactory aFactory) {
		if (aFactory == null) {
			throw new IllegalArgumentException("Invalid factory");
		}

		processor = new JenaSAPEREConverter(aFactory);
	}

	@Override
	public final Set<LSA> parseLSAs(final String input) throws SAPEREException {
		return parseLSAs(new StringReader(input));
	}

	@Override
	public final Set<LSA> parseLSAs(final InputStream input)
			throws SAPEREException {
		final Model model = ModelFactory.createDefaultModel();
		model.read(input, BASE_URI);

		try {
			return processor.parseJenaModel(model);
		} catch (Exception e) {
			throw new SAPEREException("Cannot parse LSAs", e);
		}
	}

	@Override
	public final Set<LSA> parseLSAs(final Reader input) throws SAPEREException {
		final Model model = ModelFactory.createDefaultModel();
		model.read(input, BASE_URI);

		try {
			return processor.parseJenaModel(model);
		} catch (Exception e) {
			throw new SAPEREException("Cannot parse LSAs", e);
		}
	}
}

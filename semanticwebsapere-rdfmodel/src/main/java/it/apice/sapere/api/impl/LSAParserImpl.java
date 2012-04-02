package it.apice.sapere.api.impl;

import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.internal.Jena2SAPEREConverter;
import it.apice.sapere.api.lsas.LSA;

import java.io.IOException;
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
 * @see LSAParser
 * 
 */
public class LSAParserImpl implements LSAParser {

	/** Base used in relative URI resolution. */
	private static final transient String BASE_URI = null;

	/** Converter. */
	private final transient Jena2SAPEREConverter processor;

	/**
	 * <p>
	 * Builds new SAPEREParserImpl.
	 * </p>
	 * 
	 * @param aFactory
	 *            SAPERE Model Factory
	 */
	public LSAParserImpl(final PrivilegedLSAFactory aFactory) {
		if (aFactory == null) {
			throw new IllegalArgumentException("Invalid factory");
		}

		processor = new Jena2SAPEREConverter(aFactory);
	}

	@Override
	public final Set<LSA> parseLSAs(final String input) throws SAPEREException {
		return parseLSAs(input, RDFFormat.TURTLE);
	}

	@Override
	public final Set<LSA> parseLSAs(final InputStream input)
			throws SAPEREException {
		return parseLSAs(input, RDFFormat.TURTLE);
	}

	@Override
	public final Set<LSA> parseLSAs(final Reader input) throws SAPEREException {
		return parseLSAs(input, RDFFormat.TURTLE);
	}

	@Override
	public final Set<LSA> parseLSAs(final String input, final RDFFormat type)
			throws SAPEREException {
		return parseLSAs(new StringReader(input), type);
	}

	@Override
	public final Set<LSA> parseLSAs(final InputStream input,
			final RDFFormat type) throws SAPEREException {
		final Model model = ModelFactory.createDefaultModel();
		model.read(input, BASE_URI, type.toString());

		try {
			return processor.parseJenaModel(model);
		} catch (Exception e) {
			throw new SAPEREException("Cannot parse LSAs", e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public final Set<LSA> parseLSAs(final Reader input, final RDFFormat type)
			throws SAPEREException {
		final Model model = ModelFactory.createDefaultModel();
		model.read(input, BASE_URI, type.toString());

		try {
			return processor.parseJenaModel(model);
		} catch (Exception e) {
			throw new SAPEREException("Cannot parse LSAs", e);
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

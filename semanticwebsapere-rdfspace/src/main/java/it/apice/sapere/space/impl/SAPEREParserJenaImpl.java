package it.apice.sapere.space.impl;

import it.apice.sapere.api.SAPEREParser;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.nodes.SAPEREException;

import java.io.InputStream;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Resource;

/**
 * <p>
 * This class provides an implementation of the SAPEREParser which relies on
 * Jena Framework.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SAPEREParserJenaImpl implements SAPEREParser {

	@Override
	public final Set<LSA> parseLSAs(final String input) throws SAPEREException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Set<LSA> parseLSAs(final InputStream input)
			throws SAPEREException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Set<LSA> parseLSAs(final Reader input) throws SAPEREException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 * Parses a set of Resources retrieved from the RDF Store.
	 * </p>
	 * 
	 * @param lsas
	 *            The set of LSA resources
	 * @return The set of LSA that has been parsed
	 */
	public final Set<LSA> parseLSAs(final Set<Resource> lsas) {
		final Set<LSA> res = new HashSet<LSA>();
		for (Resource lsa : lsas) {
			res.add(parseLSA(lsa));
		}

		return res;
	}

	/**
	 * <p>
	 * Parses the provided resource in order to extract the LSA.
	 * </p>
	 * 
	 * @param lsa
	 *            The resource to be parsed
	 * @return The parsed LSA
	 */
	public final LSA parseLSA(final Resource lsa) {
		// TODO Implement it
		return null;
	}
}

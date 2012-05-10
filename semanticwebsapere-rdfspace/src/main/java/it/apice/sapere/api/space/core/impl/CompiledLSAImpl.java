package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.core.CompiledLSA;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <p>
 * This class implements {@link CompiledLSA} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class CompiledLSAImpl implements CompiledLSA<StmtIterator> {

	/** LSA-id. */
	private final LSAid lsaId;

	/** Iterator over LSA's statements. */
	private final Model model;

	/**
	 * <p>
	 * Builds a new {@link CompiledLSAImpl}.
	 * </p>
	 * 
	 * @param id
	 *            The LSA-id of related LSA
	 * @param compModel
	 *            The model which contains the compiled LSA statements
	 */
	public CompiledLSAImpl(final LSAid id, final Model compModel) {
		if (id == null) {
			throw new IllegalArgumentException("Invalid LSA-id provided");
		}

		if (compModel == null) {
			throw new IllegalArgumentException("Invalid Model provided");
		}

		lsaId = id;
		model = compModel;
	}

	/**
	 * <p>
	 * Builds a new {@link CompiledLSAImpl}.
	 * </p>
	 * 
	 * @param id
	 *            The LSA-id of related LSA
	 * @param iter
	 *            An iterator over all compiled LSA statements
	 */
	public CompiledLSAImpl(final LSAid id, final StmtIterator iter) {
		if (id == null) {
			throw new IllegalArgumentException("Invalid LSA-id provided");
		}

		if (iter == null) {
			throw new IllegalArgumentException(
					"Invalid Statement Iterator provided");
		}

		lsaId = id;
		model = ModelFactory.createDefaultModel();
		model.add(iter);
	}

	@Override
	public final LSAid getLSAid() {
		return lsaId;
	}

	@Override
	public final StmtIterator getStatements() {
		return model.listStatements();
	}

	@Override
	public final String toString() {
		return toString(RDFFormat.TURTLE);
	}

	@Override
	public final String toString(final RDFFormat format) {
		final StringWriter out = new StringWriter();
		try {
			model.write(out, format.toString());
			return out.toString();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public final void assertProperty(final URI propURI, 
			final String propValue) {
		model.addLiteral(model.createResource(lsaId.getId().toString()),
				model.createProperty(propURI.toString()), propValue);
	}

	@Override
	public final void assertProperty(final URI propURI, final URI propValue) {
		model.add(model.createResource(lsaId.getId().toString()),
				model.createProperty(propURI.toString()),
				model.createResource(propValue.toString()));
	}

	@Override
	public final void clearProperty(final URI propURI) {
		model.remove(model.getResource(lsaId.getId().toString())
				.listProperties(model.createProperty(propURI.toString())));
	}

	@Override
	public final String[] readLiteralProperty(final URI propURI) {
		final List<String> res = new LinkedList<String>();
		final StmtIterator iter = model.getResource(lsaId.getId().toString())
				.listProperties(model.createProperty(propURI.toString()));
		while (iter.hasNext()) {
			final RDFNode val = ((Statement) iter.next()).getObject();
			if (val.isLiteral()) {
				res.add(val.asNode().getLiteralValue().toString());
			}
		}

		return res.toArray(new String[res.size()]);
	}

	@Override
	public final URI[] readURIProperty(final URI propURI) {
		final List<URI> res = new LinkedList<URI>();
		final StmtIterator iter = model.getResource(lsaId.getId().toString())
				.listProperties(model.createProperty(propURI.toString()));
		while (iter.hasNext()) {
			final RDFNode val = ((Statement) iter.next()).getObject();
			if (!val.isLiteral()) {
				res.add(URI.create(val.asNode().getURI()));
			}
		}

		return res.toArray(new URI[res.size()]);
	}

}

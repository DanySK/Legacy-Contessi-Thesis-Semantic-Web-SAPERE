package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.lsas.visitors.impl.ToJenaVisitorImpl;

import java.io.StringReader;
import java.net.URI;
import java.util.List;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <p>
 * This class provides an implementation of the {@link LSACompiler} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class LSACompilerImpl implements LSACompiler<StmtIterator> {

	/** The sapere:LSA type. */
	private static final transient String LSA_TYPE = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#LSA";

	/** The rdf:type. */
	private static final transient String RDF_TYPE = "http://"
			+ "www.w3.org/1999/02/22-rdf-syntax-ns#type";

	/** Basic URI for RDF Model parsing. */
	private static final transient String BASE_URI = null;

	/** Reference to PrivilegedLSAFactory. */
	private final transient PrivilegedLSAFactory factory;

	/**
	 * <p>
	 * Builds a new {@link LSACompilerImpl}.
	 * </p>
	 * 
	 * @param aFactory
	 *            The LSAFactory to be used
	 */
	public LSACompilerImpl(final PrivilegedLSAFactory aFactory) {
		if (aFactory == null) {
			throw new IllegalArgumentException(
					"Invalid privileged LSAFactory provided");
		}

		factory = aFactory;
	}

	@Override
	public final CompiledLSA<StmtIterator> compile(final LSA lsa) {
		if (lsa == null) {
			throw new IllegalArgumentException("Invalid LSA provided");
		}

		final Model tmp = ModelFactory.createDefaultModel();
		lsa.accept(new ToJenaVisitorImpl(tmp));

		return new CompiledLSAImpl(lsa.getLSAId(), tmp);
	}

	@Override
	public final CompiledLSA<StmtIterator> parse(final String rdf,
			final RDFFormat format) {
		return parse(rdf, format, false);
	}

	@Override
	public final CompiledLSA<StmtIterator> parse(final String rdf,
			final RDFFormat format, final boolean makeCopy) {
		if (rdf == null) {
			throw new IllegalArgumentException("Invalid rdf-string provided");
		}

		if (format == null) {
			throw new IllegalArgumentException("Invalid RDFFormat provided");
		}

		final Model tmp = ModelFactory.createDefaultModel();
		final StringReader reader = new StringReader(rdf);
		try {
			tmp.read(reader, BASE_URI, format.toString());
		} finally {
			reader.close();
		}

		LSAid id = extractLSAid(tmp);
		if (makeCopy) {
			final LSAid newId = factory.createLSAid();
			substituteId(id, newId, tmp);
			id = newId;
		}

		return new CompiledLSAImpl(id, tmp);
	}

	/**
	 * <p>
	 * Changes the LSA-id resource, in the model, with the new one.
	 * </p>
	 * 
	 * @param id
	 *            The old LSA-id
	 * @param newId
	 *            The new LSA-id
	 * @param tmp
	 *            The model to be altered
	 */
	private void substituteId(final LSAid id, final LSAid newId, 
			final Model tmp) {
		final Resource newRes = tmp.createResource(newId.getId().toString());
		final List<Statement> iter = tmp.createResource(id.getId().toString())
				.listProperties().toList();
		tmp.removeAll();
		for (Statement stmt : iter) {
			newRes.addProperty(stmt.getPredicate(), stmt.getObject());
		}
	}

	/**
	 * <p>
	 * Extracts the LSA-id from the provided model.
	 * </p>
	 * 
	 * @param model
	 *            The model which contains the LSA specification
	 * @return The desired LSA-id
	 */
	private LSAid extractLSAid(final Model model) {
		final ResIterator iter = model.listSubjectsWithProperty(
				model.createProperty(RDF_TYPE), model.createResource(LSA_TYPE));
		try {
			while (iter.hasNext()) {
				final Resource lsa = (Resource) iter.next();
				if (iter.hasNext()) {
					throw new IllegalArgumentException(
							"Only one LSA should be parsed");
				}

				return factory.createLSAid(new URI(lsa.getURI()));
			}
		} catch (Exception ex) {
			throw new IllegalArgumentException(
					"Invalid LSA representation as an RDF String", ex);
		}

		throw new IllegalArgumentException(
				"Invalid LSA representation as an RDF String");
	}

	@Override
	public final CompiledLSA<StmtIterator> create() {
		return compile(factory.createLSA());
	}

}

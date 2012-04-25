package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.core.CompiledLSA;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
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
	private final transient Model model;

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

}

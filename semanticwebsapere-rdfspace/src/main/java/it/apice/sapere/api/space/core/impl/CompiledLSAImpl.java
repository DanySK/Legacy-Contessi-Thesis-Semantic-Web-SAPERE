package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.space.core.CompiledLSA;

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
	private final transient StmtIterator iter;

	/**
	 * <p>
	 * Builds a new {@link CompiledLSAImpl}.
	 * </p>
	 * 
	 * @param id
	 *            The LSA-id of related LSA
	 * @param stmtIter
	 *            An iterator over LSA's statements
	 */
	public CompiledLSAImpl(final LSAid id, final StmtIterator stmtIter) {
		if (id == null) {
			throw new IllegalArgumentException("Invalid LSA-id provided");
		}

		if (stmtIter == null) {
			throw new IllegalArgumentException(
					"Invalid Statement Iterator provided");
		}

		lsaId = id;
		iter = stmtIter;
	}

	@Override
	public final LSAid getLSAid() {
		return lsaId;
	}

	@Override
	public final StmtIterator getStatements() {
		return iter;
	}

}

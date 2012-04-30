package it.apice.sapere.space.tdb.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl;
import it.apice.sapere.api.space.core.impl.ReasoningLevel;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <p>
 * Realization of an High Performance LSA-space.
 * </p>
 * 
 * @author Paolo Contessi
 */
public class HPLSAspaceImpl extends AbstractLSAspaceCoreImpl {

	/**
	 * <p>
	 * Builds a new {@link HPLSAspaceImpl}.
	 * </p>
	 * 
	 * @param lsaCompiler
	 *            Reference to a {@link LSACompiler}
	 * @param lsaFactory
	 *            Reference to a {@link PrivilegedLSAFactory}
	 * @param level
	 *            The {@link ReasoningLevel}
	 */
	public HPLSAspaceImpl(final LSACompiler<StmtIterator> lsaCompiler,
			final PrivilegedLSAFactory lsaFactory, final ReasoningLevel level) {
		super(lsaCompiler, lsaFactory, level);
	}

	/**
	 * <p>
	 * Builds a new {@link HPLSAspaceImpl}.
	 * </p>
	 * 
	 * @param lsaCompiler
	 *            Reference to a {@link LSACompiler}
	 * @param lsaFactory
	 *            Reference to a {@link PrivilegedLSAFactory}
	 */
	public HPLSAspaceImpl(final LSACompiler<StmtIterator> lsaCompiler,
			final PrivilegedLSAFactory lsaFactory) {
		super(lsaCompiler, lsaFactory);
	}

	@Override
	protected final Model initRDFGraphModel(final ReasoningLevel level) {
		throw new UnsupportedOperationException("Not implemented yet");
	}
}

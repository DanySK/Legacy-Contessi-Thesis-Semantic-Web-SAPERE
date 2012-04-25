package it.apice.sapere.space.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <p>
 * Realization of an LSA-space.
 * </p>
 * 
 * @author Paolo Contessi
 */
public class LSAspaceImpl extends AbstractLSAspaceCoreImpl {

	/**
	 * <p>
	 * Builds a new LSAspaceImpl.
	 * </p>
	 * 
	 * @param lsaCompiler
	 *            Reference to a {@link LSACompiler}
	 * @param lsaFactory
	 *            Reference to a {@link PrivilegedLSAFactory}
	 */
	public LSAspaceImpl(final LSACompiler<StmtIterator> lsaCompiler,
			final PrivilegedLSAFactory lsaFactory) {
		super(lsaCompiler, lsaFactory);
	}

	@Override
	protected final Model initRDFGraphModel() {
		return ModelFactory.createDefaultModel();
	}

}

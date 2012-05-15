package it.apice.sapere.space.impl;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.space.core.impl.AbstractLSAspaceCoreImpl;
import it.apice.sapere.api.space.core.impl.ReasoningLevel;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

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
	 * Builds a new {@link LSAspaceImpl}.
	 * </p>
	 * 
	 * @param lsaFactory
	 *            Reference to a {@link PrivilegedLSAFactory}
	 * @param level
	 *            The {@link ReasoningLevel}
	 */
	public LSAspaceImpl(final PrivilegedLSAFactory lsaFactory,
			final ReasoningLevel level) {
		super(lsaFactory, level);
	}

	/**
	 * <p>
	 * Builds a new {@link LSAspaceImpl}.
	 * </p>
	 * 
	 * @param lsaFactory
	 *            Reference to a {@link PrivilegedLSAFactory}
	 */
	public LSAspaceImpl(final PrivilegedLSAFactory lsaFactory) {
		super(lsaFactory);
	}

	@Override
	protected final Model initRDFGraphModel(final ReasoningLevel level) {
		if (level.equals(ReasoningLevel.OWL_DL)) {
			return ModelFactory
					.createOntologyModel(PelletReasonerFactory.THE_SPEC);
		} else if (level.equals(ReasoningLevel.RDFS_INF)) {
			return ModelFactory
					.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);
		} else if (level.equals(ReasoningLevel.NONE)) {
			return ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		} else {
			return ModelFactory.createDefaultModel();
		}
	}

}

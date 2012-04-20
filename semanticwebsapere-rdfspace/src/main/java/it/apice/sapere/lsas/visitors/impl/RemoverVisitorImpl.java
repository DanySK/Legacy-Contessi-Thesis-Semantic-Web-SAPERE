package it.apice.sapere.lsas.visitors.impl;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.lsas.visitor.LSAVisitor;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * <p>
 * Implementation of an LSAVisitor which injects the LSA in the RDF Model.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class RemoverVisitorImpl implements LSAVisitor {

	/** RDF Model. */
	private final transient Model model;

	/**
	 * <p>
	 * Builds a new InjectorVisitorImpl.
	 * </p>
	 * 
	 * @param aModel
	 *            RDF Model instance
	 */
	public RemoverVisitorImpl(final Model aModel) {
		model = aModel;
	}

	@Override
	public final void visit(final LSAid lsaId) {
		model.createResource(lsaId.getId().toString()).removeProperties();
	}

	@Override
	public final void visit(final Property prop) {
		prop.getName().accept(this);
		for (PropertyValue<?, ?> val : prop.values()) {
			val.accept(this);
		}
	}

	@Override
	public final void visit(final PropertyName pname) {

	}

	@Override
	public final void visit(final SemanticDescription desc) {
		for (Property prop : desc.properties()) {
			prop.accept(this);
		}
	}

	@Override
	public final void visit(final PropertyValue<?, ?> val) {
		
	}

	@Override
	public final void visit(final LSA lsa) {
		lsa.getLSAId().accept(this);
		lsa.getSemanticDescription().accept(this);
	}

}

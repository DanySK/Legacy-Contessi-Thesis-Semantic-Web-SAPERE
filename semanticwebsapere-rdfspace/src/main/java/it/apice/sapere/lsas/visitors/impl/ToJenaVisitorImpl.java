package it.apice.sapere.lsas.visitors.impl;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.lsas.values.SDescValue;
import it.apice.sapere.api.lsas.visitor.LSAVisitor;

import java.util.Calendar;
import java.util.Date;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * <p>
 * Implementation of an LSAVisitor which injects the LSA in the RDF Model.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class ToJenaVisitorImpl implements LSAVisitor {

	/** The sapere:LSA type. */
	private static final transient String LSA_TYPE = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#LSA";

	/** The rdf:type. */
	private static final transient String RDF_TYPE = "http://"
			+ "www.w3.org/1999/02/22-rdf-syntax-ns#type";

	/** RDF Model. */
	private final transient Model model;

	/** Resource representing the LSA. */
	private transient Resource lsaResource;

	/** rdf:type property. */
	private final transient com.hp.hpl.jena.rdf.model.Property rdfTypeProp;

	/** Resource representing the LSA owl:Class. */
	private final transient Resource lsaClass;

	/** The property actually under translation. */
	private transient com.hp.hpl.jena.rdf.model.Property actualProp;

	/**
	 * <p>
	 * Builds a new ToJenaVisitorImpl.
	 * </p>
	 * 
	 * @param aModel
	 *            RDF Model instance
	 */
	public ToJenaVisitorImpl(final Model aModel) {
		model = aModel;

		rdfTypeProp = model.createProperty(RDF_TYPE);
		lsaClass = model.createResource(LSA_TYPE);
	}

	@Override
	public final void visit(final LSAid lsaId) {
		lsaResource = model.createResource(lsaId.getId().toString());
		model.add(lsaResource, rdfTypeProp, lsaClass);
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
		actualProp = model.createProperty(pname.getValue().toString());
	}

	@Override
	public final void visit(final SemanticDescription desc) {
		for (Property prop : desc.properties()) {
			prop.accept(this);
		}
	}

	@Override
	public final void visit(final PropertyValue<?, ?> val) {
		Object obj = val.getValue();
		if (obj instanceof Date) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime((Date) obj);
			obj = cal;
		}

		// Handle nested Semantic Descriptions:
		if (val instanceof SDescValue) {
			// Create a Blank Node
			final AnonId bId = new AnonId();
			final Resource bNode = model.createResource(bId);

			// Associate it to the actualProp
			model.add(lsaResource, actualProp, bNode);

			// Push actual (root) lsaResource and property [stack]
			final Resource tmpRes = lsaResource;
			final com.hp.hpl.jena.rdf.model.Property tmpProp = actualProp;

			// Set the Blank node as current resource and visit the nested
			// Semantic Description
			lsaResource = bNode;
			((SDescValue) val).getValue().accept(this);

			// Pop root lsaResource and property [unstack]
			lsaResource = tmpRes;
			actualProp = tmpProp;
		} else { // Standard (flat) behaviour
			Literal lit;
			if (val.hasLocale()) {
				lit = model
						.createLiteral(obj.toString(), val.getLanguageCode());
			} else {
				lit = model.createTypedLiteral(obj);
			}

			model.addLiteral(lsaResource, actualProp, lit);
		}
	}

	@Override
	public final void visit(final LSA lsa) {
		lsa.getLSAId().accept(this);
		lsa.getSemanticDescription().accept(this);
	}

}

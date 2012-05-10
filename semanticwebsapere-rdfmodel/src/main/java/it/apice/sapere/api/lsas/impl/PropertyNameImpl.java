package it.apice.sapere.api.lsas.impl;

import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.visitor.LSAVisitor;

import java.net.URI;

/**
 * <p>
 * Implementation of PropertyName.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see PropertyName
 * 
 */
public class PropertyNameImpl implements PropertyName {

	/** List of namespaces reserved to synthetic properties. */
	public static final transient String[] SYNTHETIC_NS = new String[] {
			"http://www.w3.org/1999/02/22-rdf-syntax-ns#",
			"http://www.w3.org/2000/01/rdf-schema#",
			"http://www.w3.org/2001/XMLSchema#",
			"http://www.w3.org/2002/07/owl#",
			"http://www.sapere-project.eu/ontologies/2012/0/sapere-model.owl"};

	/** Property name. */
	private final URI id;

	/**
	 * <p>
	 * Builds a new PropertyNameImpl.
	 * </p>
	 * 
	 * @param value
	 *            The PropertyName value
	 */
	public PropertyNameImpl(final URI value) {
		if (value == null) {
			throw new IllegalArgumentException("Invalid value");
		}

		id = value;
	}

	@Override
	public final URI getValue() {
		return id;
	}

	@Override
	public final String toString() {
		return id.toString();
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (id != null) {
			result += id.hashCode();
		}

		return result;
	}

	@Override
	public final boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		PropertyNameImpl other = (PropertyNameImpl) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public final void accept(final LSAVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public final boolean isSynthetic() {
		for (String ns : SYNTHETIC_NS) {
			if (id.toString().toLowerCase().startsWith(ns.toLowerCase())) {
				return true;
			}
		}

		return false;
	}

}

package it.apice.sapere.api.lsas.impl;

import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.SemanticDescription;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Implementation of SemanticDescription.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see SemanticDescription
 * 
 */
public class SemanticDescriptionImpl implements SemanticDescription {

	/** Properties of this SemanticDescription. */
	private final Map<PropertyName, Property> properties;

	/**
	 * <p>
	 * Builds a new SemanticDescriptionImpl.
	 * </p>
	 */
	public SemanticDescriptionImpl() {
		properties = new HashMap<PropertyName, Property>();
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param desc
	 *            Semantic description to be cloned
	 */
	public SemanticDescriptionImpl(final SemanticDescription desc) {
		this();

		for (Property prop : desc.properties()) {
			properties.put(prop.getName(), prop.clone());
		}
	}

	@Override
	public final SemanticDescription addProperty(final Property prop) {
		if (properties.containsKey(prop.getName())) {
			throw new IllegalArgumentException(String.format(
					"Property already added (%s)", prop));
		}

		properties.put(prop.getName(), prop);
		return this;
	}

	@Override
	public final SemanticDescription removeProperty(final Property prop) {
		if (!properties.containsKey(prop.getName())) {
			throw new IllegalArgumentException(String.format(
					"Cannot remove property (%s): "
							+ "the specified property does not "
							+ "belong to this semantic description", prop));
		}

		properties.remove(prop.getName());
		return this;
	}

	@Override
	public final Property[] properties() {
		return properties.values().toArray(new Property[properties.size()]);
	}

	@Override
	public final boolean contains(final PropertyName name) {
		return properties.containsKey(name);
	}

	@Override
	public final Property get(final PropertyName name) {
		return properties.get(name);
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;

		if (properties != null) {
			result += properties.hashCode();
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

		SemanticDescriptionImpl other = (SemanticDescriptionImpl) obj;
		if (properties == null) {
			if (other.properties != null) {
				return false;
			}
		} else if (!properties.equals(other.properties)) {
			return false;
		}
		return true;
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();
		for (Property prop : properties.values()) {
			builder.append(prop.toString()).append("; ");
		}

		return builder.toString();
	}

	@Override
	public final boolean isCloneOf(final SemanticDescription other) {
		return equals(other)
				|| (properties.isEmpty() && other.properties().length == 0);
	}

	@Override
	public final boolean isExtensionOf(final SemanticDescription other) {
		for (Property prop : other.properties()) {
			final Property tProp = properties.get(prop.getName());
			if (tProp == null || !tProp.isExtensionOf(prop)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public final SemanticDescription clone() {
		return new SemanticDescriptionImpl(this);
	}
}

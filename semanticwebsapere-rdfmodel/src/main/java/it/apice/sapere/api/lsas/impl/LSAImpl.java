package it.apice.sapere.api.lsas.impl;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.lsas.visitor.LSAVisitor;

/**
 * <p>
 * Implementation of LSA.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see LSA
 * 
 */
public class LSAImpl implements LSA {

	/** LSA-id. */
	private final LSAid id;

	/** LSA's semantic description (a.k.a. content). */
	private final SemanticDescription content;

	/**
	 * <p>
	 * Builds a new LSAImpl object.
	 * </p>
	 * 
	 * @param anId
	 *            LSA-id
	 */
	public LSAImpl(final LSAid anId) {
		if (anId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		id = anId;
		content = new SemanticDescriptionImpl();
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param lsa
	 *            LSA to be cloned
	 */
	protected LSAImpl(final LSAImpl lsa) {
		id = lsa.id;
		content = lsa.content.clone();
	}

	@Override
	public final LSAid getLSAId() {
		return id;
	}

	@Override
	public final SemanticDescription getSemanticDescription() {
		return content;
	}

	@Override
	public final LSA clone() {
		return new LSAImpl(this);
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (content != null) {
			result += content.hashCode();
		}

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

		final LSAImpl other = (LSAImpl) obj;
		if (content == null) {
			if (other.content != null) {
				return false;
			}
		} else if (!content.equals(other.content)) {
			return false;
		}
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
	public final String toString() {
		final StringBuilder builder = new StringBuilder(id.toString());
		builder.append(" {\n").append(content.toString()).append("}");

		return builder.toString();
	}

	@Override
	public final void accept(final LSAVisitor visitor) {
		visitor.visit(this);
	}
}

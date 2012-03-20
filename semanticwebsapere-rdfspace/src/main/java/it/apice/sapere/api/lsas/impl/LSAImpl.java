package it.apice.sapere.api.lsas.impl;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.nodes.SAPEREAgent;

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

	/** LSA's owner (agent). */
	private final SAPEREAgent owner;

	/** LSA's semantic description (a.k.a. content). */
	private final SemanticDescription content;

	/**
	 * <p>
	 * Builds a new LSAImpl object.
	 * </p>
	 * 
	 * @param anId
	 *            LSA-id
	 * @param anAgent
	 *            Owner agent
	 */
	public LSAImpl(final LSAid anId, final SAPEREAgent anAgent) {
		if (anId == null) {
			throw new IllegalArgumentException("Invalid LSA-id");
		}

		id = anId;
		owner = anAgent;
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
		owner = lsa.owner;
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
	public final boolean isOwnedBy(final SAPEREAgent agent) {
		if (agent == null) {
			throw new IllegalArgumentException("isOwnedBy(null) has no sense: "
					+ "each LSA must have an owner");
		}

		return owner != null && owner.equals(agent);
	}

	@Override
	public final boolean isSynthetic() {
		return owner == null;
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

		result *= prime;
		if (owner != null) {
			result += owner.hashCode();
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

		LSAImpl other = (LSAImpl) obj;
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
		if (owner == null) {
			if (other.owner != null) {
				return false;
			}
		} else if (!owner.equals(other.owner)) {
			return false;
		}
		return true;
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder(id.toString());
		builder.append(" { #owner: ");
		if (owner != null) {
			builder.append(owner.toString());
		} else {
			builder.append("SYSTEM");
		}

		builder.append("; ").append(content.toString()).append("}");

		return builder.toString();
	}
}

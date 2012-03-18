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
	private final transient SAPEREAgent owner;

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
		return owner.equals(agent);
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
	public final String toString() {
		final StringBuilder builder = new StringBuilder(id.toString());
		builder.append(" { #owner: ").append(owner.toString()).append("; ")
				.append(content.toString()).append("}");

		return builder.toString();
	}
}

package it.apice.sapere.api.lsas.impl;

import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.visitor.LSAVisitor;

import java.net.URI;

/**
 * <p>
 * Implementation of LSAid.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see LSAid
 */
public class LSAidImpl implements LSAid {

	/** LSA-id. */
	private final URI id;

	/**
	 * <p>
	 * Builds a new LSAidImpl.
	 * </p>
	 * 
	 * @param value
	 *            The LSA-id value
	 */
	public LSAidImpl(final URI value) {
		id = value;
	}

	@Override
	public final URI getId() {
		return id;
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
		LSAidImpl other = (LSAidImpl) obj;
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
		return id.toString();
	}

	@Override
	public final void accept(final LSAVisitor visitor) {
		visitor.visit(this);
	}
}

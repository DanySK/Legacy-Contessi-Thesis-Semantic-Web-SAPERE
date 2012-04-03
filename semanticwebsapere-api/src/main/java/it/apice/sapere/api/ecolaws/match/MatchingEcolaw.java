package it.apice.sapere.api.ecolaws.match;

import it.apice.sapere.api.ecolaws.Ecolaw;

// TODO define...
public interface MatchingEcolaw extends Ecolaw {

	void bind(Object... objs);

	void unbind();

	void bindings();

	double score();

	/**
	 * <p>
	 * Applies this ecolaw to the LSA-space.
	 * </p>
	 */
	void apply();
}

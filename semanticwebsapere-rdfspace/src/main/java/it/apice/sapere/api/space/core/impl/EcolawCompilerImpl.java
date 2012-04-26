package it.apice.sapere.api.space.core.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.compilers.EcolawToSPARQL;
import it.apice.sapere.api.ecolaws.compilers.EcolawToUpdTemplate;
import it.apice.sapere.api.space.core.CompiledEcolaw;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.match.MatchingEcolawTemplate;
import it.apice.sapere.api.space.match.impl.MatchingEcolawTemplateImpl;

/**
 * <p>
 * This class implements the {@link EcolawCompiler} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class EcolawCompilerImpl implements EcolawCompiler {

	@Override
	public final CompiledEcolaw compile(final Ecolaw law) {
		return create(EcolawToSPARQL.convert(law),
				EcolawToUpdTemplate.convert(law));
	}

	@Override
	public final CompiledEcolaw create(final String mQuery, 
			final String uQuery) {
		return new CompiledEcolawImpl(mQuery, new MatchingEcolawTemplateImpl(
				uQuery));
	}

	/**
	 * <p>
	 * Creates a {@link CompiledEcolaw}.
	 * </p>
	 *
	 * @param mQuery
	 *            The query for matching phase
	 * @param uQuery
	 *            The query template for update phase
	 * @return The compiled eco-law
	 */
	private CompiledEcolaw create(final String mQuery,
			final MatchingEcolawTemplate uQuery) {
		return new CompiledEcolawImpl(mQuery, uQuery);
	}
}

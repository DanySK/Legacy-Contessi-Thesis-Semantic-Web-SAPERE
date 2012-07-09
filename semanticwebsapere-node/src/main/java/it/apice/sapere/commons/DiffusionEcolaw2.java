package it.apice.sapere.commons;

import it.apice.sapere.api.space.core.EcolawCompiler;

/**
 * <p>
 * Defines a DIFFUSE eco-law based on {@link AbstractDiffusionEcolawFactory}.
 * </p>
 * 
 * @author Paolo Contessi
 *
 */
public class DiffusionEcolaw2 extends AbstractDiffusionEcolawFactory {

	/** Type of LSA to be relocated. */
	private final transient String _type;

	/** Name of the neighbour. */
	private final transient String _toName;

	/**
	 * <p>
	 * Builds a new {@link DiffusionEcolaw2}.
	 * </p>
	 * 
	 * @param compiler
	 *            Reference to {@link EcolawCompiler}
	 * @param type
	 *            Type of LSA to be relocated
	 * @param toName
	 *            Name of the neighbour
	 */
	protected DiffusionEcolaw2(final EcolawCompiler compiler,
			final String type, final String toName) {
		super(compiler);

		_type = type;
		_toName = toName;
	}

	@Override
	protected String getSourceLSASelector(final String sourceLsaVar) {
		return sourceLsaVar + " rdf:type " + _type + ".";
	}

	@Override
	protected String[] getDestinationsNames() {
		return new String[] { _toName };
	}

	@Override
	protected String getRateValue(final String rateVar) {
		return "BIND (\"1.0\" AS " + rateVar + ").";
	}

}

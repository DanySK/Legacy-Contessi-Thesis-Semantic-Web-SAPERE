package it.apice.sapere.api.ecolaws.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.match.MatchingEcolaw;
import it.apice.sapere.api.ecolaws.match.ScoredMatchResult;
import it.apice.sapere.api.ecolaws.match.impl.MatchingEcolawImpl;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;
import it.apice.sapere.api.ecolaws.visitors.impl.VarRetrieverVisitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * This class implements an Ecolaw as described in SAPERE model.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 * @see Ecolaw
 */
public class EcolawImpl implements Ecolaw {

	/** Eco-law's label. */
	private final transient String label;

	/** Eco-law's scheduling rate. */
	private final transient Rate<?> rate;

	/** List of all reactants. */
	private final transient Set<Reactant> reactants = new HashSet<Reactant>();

	/** List of all products. */
	private final transient Set<Product> products = new HashSet<Product>();

	/**
	 * <p>
	 * Default Constructor.
	 * </p>
	 */
	public EcolawImpl() {
		label = "";
		rate = new ASAPRateImpl();
	}

	/**
	 * <p>
	 * Builds a new EcolawImpl.
	 * </p>
	 * 
	 * @param aLabel
	 *            A label for the ecolaw
	 */
	public EcolawImpl(final String aLabel) {
		this(aLabel, new ASAPRateImpl());
	}

	/**
	 * <p>
	 * Builds a new EcolawImpl.
	 * </p>
	 * 
	 * @param aLabel
	 *            A label for the ecolaw
	 * @param aRate
	 *            Ecolaw's scheduling rate
	 */
	public EcolawImpl(final String aLabel, final Rate<?> aRate) {
		if (aLabel == null || aLabel.equals("")) {
			throw new IllegalArgumentException("Invalid label provided");
		}

		if (aRate == null) {
			throw new IllegalArgumentException("Invalid rate provided");
		}

		rate = aRate;
		label = aLabel;
	}

	@Override
	public final String getLabel() {
		return label;
	}

	@Override
	public final void accept(final EcolawVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public final Rate<?> getRate() {
		return rate;
	}

	@Override
	public final Reactant[] reactants() {
		return reactants.toArray(new Reactant[reactants.size()]);
	}

	@Override
	public final Product[] products() {
		return products.toArray(new Product[products.size()]);
	}

	@Override
	public final VarTerm<?>[] variables() {
		final VarRetrieverVisitor vis = new VarRetrieverVisitor(this);

		final List<VarTerm<?>> vars = vis.getAllVariables();
		final VarTerm<?>[] res = new VarTerm<?>[vars.size()];

		return vars.toArray(res);
	}

	@Override
	public final Ecolaw setRate(final Rate<?> aRate) {
		if (aRate == null) {
			throw new IllegalArgumentException("Invalid rate provided");
		}

		return null;
	}

	@Override
	public final Ecolaw addReactant(final Reactant react) {
		if (!reactants.add(react)) {
			throw new IllegalArgumentException("Already added.");
		}

		return this;
	}

	@Override
	public final Ecolaw addProduct(final Product prod) {
		if (!products.add(prod)) {
			throw new IllegalArgumentException("Already added.");
		}

		return this;
	}

	@Override
	public final MatchingEcolaw bind(final ScoredMatchResult match) {
		return new MatchingEcolawImpl(this, match);
	}

}

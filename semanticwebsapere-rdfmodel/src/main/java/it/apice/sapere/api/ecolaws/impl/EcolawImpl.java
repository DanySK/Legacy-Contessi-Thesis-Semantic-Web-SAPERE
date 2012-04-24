package it.apice.sapere.api.ecolaws.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;
import it.apice.sapere.api.ecolaws.visitors.impl.VarRetrieverVisitor;

import java.util.LinkedList;
import java.util.List;

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
	private final String label;

	/** Eco-law's scheduling rate. */
	private final Rate<?> rate;

	/** List of all reactants. */
	private final List<Reactant> reactants = new LinkedList<Reactant>();

	/** List of all products. */
	private final List<Product> products = new LinkedList<Product>();

	/** Extra info. */
	private transient String extras = "";

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

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            Eco-law to be cloned
	 */
	public EcolawImpl(final Ecolaw src) {
		if (src == null) {
			throw new IllegalArgumentException("Cannot clone nothing");
		}

		label = src.getLabel();
		final Object rateVal = src.getRate().getRateValue();
		if (rateVal instanceof Term<?> && ((Term<?>) rateVal).isVar()) {
			rate = src.getRate();
		} else {
			rate = src.getRate();
		}

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
		if (reactants.contains(react)) {
			throw new IllegalArgumentException("Already added.");
		}

		reactants.add(react);
		return this;
	}

	@Override
	public final Ecolaw addProduct(final Product prod) {
		if (products.contains(prod)) {
			throw new IllegalArgumentException("Already added.");
		}

		products.add(prod);
		return this;
	}

	@Override
	public final String toString() {
		final StringBuilder builder = new StringBuilder();

		if (!label.equals("")) {
			builder.append("<").append(label).append(extras).append("> ");
		}

		boolean notFirst = false;
		for (Reactant react : reactants) {
			if (notFirst) {
				builder.append(" + ");
			}

			builder.append(react.toString());
			notFirst = true;
		}

		builder.append(" --").append(rate.toString()).append("--> ");

		notFirst = false;
		for (Product prod : products) {
			if (notFirst) {
				builder.append(" + ");
			}

			builder.append(prod.toString());
			notFirst = true;
		}

		return builder.toString();
	}

	/**
	 * <p>
	 * Provides extra info to be printed out.
	 * </p>
	 * 
	 * @param info
	 *            Extra info
	 */
	protected final void setExtraInfo(final String info) {
		if (info == null) {
			throw new IllegalArgumentException("Invalid extra info");
		}
		extras = info;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (label != null) {
			result += label.hashCode();
		}

		result *= prime;
		if (products != null) {
			result += products.hashCode();
		}

		result *= prime;
		if (rate != null) {
			result += rate.hashCode();
		}

		result *= prime;
		if (reactants != null) {
			result += reactants.hashCode();
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
		EcolawImpl other = (EcolawImpl) obj;
		if (label == null) {
			if (other.label != null) {
				return false;
			}
		} else if (!label.equals(other.label)) {
			return false;
		} else if (label.equals("")) {
			// The two laws are unlabelled
			return false;
		}
		if (products == null) {
			if (other.products != null) {
				return false;
			}
		} else if (!products.equals(other.products)) {
			return false;
		}
		if (rate == null) {
			if (other.rate != null) {
				return false;
			}
		} else if (!rate.equals(other.rate)) {
			return false;
		}
		if (reactants == null) {
			if (other.reactants != null) {
				return false;
			}
		} else if (!reactants.equals(other.reactants)) {
			return false;
		}
		return true;
	}

}

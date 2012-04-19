package it.apice.sapere.api.ecolaws.visitors.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.formulas.IsFormula;
import it.apice.sapere.api.ecolaws.terms.AnnotatedVarTerm;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;

/**
 * <p>
 * This class represents an {@link EcolawVisitor}, capable of translating an
 * eco-law into a SPARQL Query.
 * </p>
 * <p>
 * The right part (Products) is not considered by this converter, because not
 * useful for the desired query.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class EcolawToSPARQL implements EcolawVisitor {

	/** Eco-law to be translated in SPARQL. */
	private final transient Ecolaw law;

	/** The query builder. */
	private final transient StringBuilder query;

	/**
	 * <p>
	 * Builds a new {@link EcolawToSPARQL}.
	 * </p>
	 * 
	 * @param anEcolaw
	 *            The ecolaw to be translated
	 */
	private EcolawToSPARQL(final Ecolaw anEcolaw) {
		if (anEcolaw == null) {
			throw new IllegalArgumentException("Cannot translate null");
		}

		law = anEcolaw;
		query = new StringBuilder("SELECT DISTINCT * WHERE ");
	}
	
	@Override
	public void visit(final Ecolaw elaw) {
		elaw.getRate().accept(this);
		for (Reactant react : elaw.reactants()) {
			react.accept(this);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void visit(final Rate<?> rate) {
		final Object rVal = rate.getRateValue();
		if (rVal instanceof VarTerm<?> && ((VarTerm) rVal).isVar()) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	public void visit(final Term<?> term) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(final Filter filter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(final Product product) {
		// Ignored because producing a SPARQL query for matching phase
	}

	@Override
	public void visit(final Reactant reactant) {
		
	}
	
	private void ruleW(final AnnotatedVarTerm<?> var) {
		if (var.getFormula() instanceof IsFormula) {
			// BIND rule
		} else {
			// FILTER rule
		}
	}

	/**
	 * <p>
	 * Creates a SPARQL query.
	 * </p>
	 * 
	 * @return The query
	 */
	private String createQuery() {
		query.append("{ ");
		law.accept(this);

		return query.append(" }").toString();
	}

	/**
	 * <p>
	 * Converts the law in a SPARQL query.
	 * </p>
	 * 
	 * @param law
	 *            The ecolaw to be converted
	 * @return The resulting SPARQL query
	 */
	public static String convert(final Ecolaw law) {
		return new EcolawToSPARQL(law).createQuery();
	}
}

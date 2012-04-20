package it.apice.sapere.api.ecolaws.visitors.impl;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.filter.impl.ExtendsFilterImpl;
import it.apice.sapere.api.ecolaws.filter.impl.HasFilterImpl;
import it.apice.sapere.api.ecolaws.filters.AssignFilter;
import it.apice.sapere.api.ecolaws.filters.ClonesFilter;
import it.apice.sapere.api.ecolaws.filters.ExtendsFilter;
import it.apice.sapere.api.ecolaws.filters.HasFilter;
import it.apice.sapere.api.ecolaws.filters.HasNotFilter;
import it.apice.sapere.api.ecolaws.filters.OpFilter;
import it.apice.sapere.api.ecolaws.formulas.IsFormula;
import it.apice.sapere.api.ecolaws.terms.AnnotatedVarTerm;
import it.apice.sapere.api.ecolaws.terms.PatternNameTerm;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.ecolaws.terms.impl.SDescTermImpl;
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

	/** Reference to the current pattern's name. */
	private transient PatternNameTerm currPatternName;

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

	@Override
	public void visit(final Rate<?> rate) {
		final Object rVal = rate.getRateValue();
		if (rVal instanceof VarTerm<?> && ((VarTerm<?>) rVal).isVar()) {
			ruleW((VarTerm<?>) rVal);
		}
	}

	@Override
	public void visit(final Term<?> term) {
		// Ignored because following rules order
	}

	@Override
	public void visit(final Filter filt) {
		if (filt instanceof OpFilter) {
			ruleLO((OpFilter) filt);
		} else if (filt instanceof ClonesFilter) {
			ruleLC((ClonesFilter) filt);
		} else if (filt instanceof ExtendsFilter) {
			ruleLE((ExtendsFilter) filt);
		}
	}

	@Override
	public void visit(final Product product) {
		// Ignored because producing a SPARQL query for matching phase
	}

	@Override
	public void visit(final Reactant reactant) {
		ruleLP(reactant);
	}

	/* ==================== Rules (begin) ==================== */

	/* ==================== [W] ==================== */

	/**
	 * <p>
	 * Applies rule [W]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param var
	 *            Input term
	 */
	private void ruleW(final Term<?> var) {
		if (var instanceof AnnotatedVarTerm<?>) {
			ruleW((AnnotatedVarTerm<?>) var);
		}
	}

	/**
	 * <p>
	 * Applies rule [W]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param var
	 *            Input variable term
	 */
	private void ruleW(final VarTerm<?> var) {
		if (var instanceof AnnotatedVarTerm<?>) {
			ruleW((AnnotatedVarTerm<?>) var);
		}
	}

	/**
	 * <p>
	 * Applies rule [W]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param var
	 *            Input annotated variable term
	 */
	private void ruleW(final AnnotatedVarTerm<?> var) {
		if (var.getFormula() instanceof IsFormula) {
			// BIND rule
			query.append(String.format("BIND( %s AS ?%s)", var.getFormula()
					.getRightOp(), var.getVarName()));
		} else {
			// FILTER rule
			query.append(String.format("FILTER(%s)", var.getFormula()
					.getStringRepr()));
		}

		query.append(" ");
	}

	/* ==================== [TL] ==================== */

	/**
	 * <p>
	 * Applies rule [TL]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param term
	 *            Input term
	 */
	private void ruleTL(final Term<?> term) {
		if (term instanceof VarTerm<?>) {
			ruleTL((VarTerm<?>) term);
		}
	}

	/**
	 * <p>
	 * Applies rule [TL]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param term
	 *            Input variable term
	 */
	private void ruleTL(final VarTerm<?> term) {
		if (term.isGround()) {
			query.append(term.toString());
		} else {
			query.append("?" + term.getVarName());
		}

		query.append(" ");
	}

	/* ==================== [LP] ==================== */

	/**
	 * <p>
	 * Applies rule [LP]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param react
	 *            Input reactant
	 */
	private void ruleLP(final Reactant react) {
		currPatternName = react.getName();
		for (Filter filt : react.filters()) {
			filt.accept(this);
		}
	}

	/* ==================== [LC] ==================== */

	/**
	 * <p>
	 * Applies rule [LC]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input clones filter
	 */
	private void ruleLC(final ClonesFilter filter) {
		ruleLE(new ExtendsFilterImpl(filter.getSource()));
		ruleLE(new ExtendsFilterImpl(new SDescTermImpl(currPatternName)));
	}

	/* ==================== [LE] ==================== */

	/**
	 * <p>
	 * Applies rule [LE]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input extends filter
	 */
	private void ruleLE(final ExtendsFilter filter) {

	}

	/* ==================== [LO] ==================== */

	/**
	 * <p>
	 * Applies rule [LO]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input op filter
	 */
	private void ruleLO(final OpFilter filter) {
		if (filter instanceof HasFilter) {
			ruleLOHas((HasFilter) filter);
		} else if (filter instanceof HasNotFilter) {
			ruleLOHasNot((HasNotFilter) filter);
		} else if (filter instanceof AssignFilter) {
			ruleLOAssign((AssignFilter) filter);
		}
	}

	/* ===>> [LO-has] <<=== */

	/**
	 * <p>
	 * Applies rule [LOh]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input has filter
	 */
	private void ruleLOHas(final HasFilter filter) {
		if (filter.getRightTerm().isVar()) {
			ruleLOHasVar(filter);
		} else {
			ruleLOHasGround(filter);
		}
	}

	/**
	 * <p>
	 * Applies rule [LOh]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input ground has filter
	 */
	private void ruleLOHasGround(final HasFilter filter) {
		for (Term<?> obj : filter.getRightTerm().getValue()) {
			query.append("\t").append(currPatternName).append(" ");
			ruleTL(filter.getLeftTerm());
			ruleTL(obj);
			query.append(".\n");

			ruleW(obj);
		}

		ruleW(filter.getLeftTerm());
	}

	/**
	 * <p>
	 * Applies rule [LOh]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input var has filter
	 */
	private void ruleLOHasVar(final HasFilter filter) {
		// TODO Auto-generated method stub

	}

	/* ===>> [LO-has-not] <<=== */

	/**
	 * <p>
	 * Applies rule [LOn]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input has-not filter
	 */
	private void ruleLOHasNot(final HasNotFilter filter) {
		if (filter.getRightTerm().isVar()) {
			ruleLOHasNotVar(filter);
		} else {
			ruleLOHasNotGround(filter);
		}
	}

	/**
	 * <p>
	 * Applies rule [LOn]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input ground has-not filter
	 */
	private void ruleLOHasNotGround(final HasNotFilter filter) {
		query.append("FILTER NOT EXISTS { ");
		for (Term<?> obj : filter.getRightTerm().getValue()) {
			query.append(currPatternName).append(" ");
			ruleTL(filter.getLeftTerm());
			ruleTL(obj);
			ruleW(obj);
		}

		query.append("}");
		ruleW(filter.getLeftTerm());
	}

	/**
	 * <p>
	 * Applies rule [LOn]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input var has-not filter
	 */
	private void ruleLOHasNotVar(final HasNotFilter filter) {
		// TODO Auto-generated method stub

	}

	/* ===>> [LO-assign] <<=== */

	/**
	 * <p>
	 * Applies rule [LO=]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input assign filter
	 */
	private void ruleLOAssign(final AssignFilter filter) {
		if (filter.getRightTerm().isVar()) {
			ruleLOAssignVar(filter);
		} else {
			ruleLOAssignGround(filter);
		}
	}

	/**
	 * <p>
	 * Applies rule [LO=]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input ground assign filter
	 */
	private void ruleLOAssignGround(final AssignFilter filter) {
		ruleLOHas(new HasFilterImpl(filter.getLeftTerm(), 
				filter.getRightTerm()));

		query.append("FILTER NOT EXISTS { ").append(currPatternName)
				.append(" ");
		ruleTL(filter.getLeftTerm());
		query.append("?o ");
		query.append("FILTER (");

		boolean notFirst = false;
		for (Term<?> obj : filter.getRightTerm().getValue()) {
			if (notFirst) {
				query.append(" && ");
			}

			query.append("o != ");
			ruleTL(obj);
			notFirst = true;
		}

		query.append(")").append("}");
	}

	/**
	 * <p>
	 * Applies rule [LO=]. See TR.WP1.2011.06.
	 * </p>
	 * 
	 * @param filter
	 *            Input ground assign filter
	 */
	private void ruleLOAssignVar(final AssignFilter filter) {
		// TODO Auto-generated method stub

	}

	/* ===================== Rules (end) ===================== */

	/**
	 * <p>
	 * Creates a SPARQL query.
	 * </p>
	 * 
	 * @return The query
	 */
	private String createQuery() {
		query.append("{\n");
		law.accept(this);

		return query.append("}").toString();
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

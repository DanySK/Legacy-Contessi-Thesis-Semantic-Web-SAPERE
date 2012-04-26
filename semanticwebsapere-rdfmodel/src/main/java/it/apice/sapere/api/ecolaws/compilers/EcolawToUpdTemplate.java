package it.apice.sapere.api.ecolaws.compilers;

import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Filter;
import it.apice.sapere.api.ecolaws.Product;
import it.apice.sapere.api.ecolaws.Rate;
import it.apice.sapere.api.ecolaws.Reactant;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;
import it.apice.sapere.api.space.match.MatchingEcolawTemplate;

/**
 * <p>
 * This class represents an {@link EcolawVisitor}, capable of translating an
 * eco-law (whose variables has been bound) into a set of SPARQL Update
 * statements.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class EcolawToUpdTemplate implements EcolawVisitor {

	@Override
	public void visit(final Ecolaw law) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(final Rate<?> rate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(final Product prod) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(final Reactant react) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(final Filter filter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(final Term<?> term) {
		// TODO Auto-generated method stub

	}

	//
	// /** Eco-law to be translated in SPARQL. */
	// private final transient Ecolaw law;
	//
	// /** The query builder. */
	// private final transient StringBuilder query;
	//
	// /** Reactants of the eco-law. */
	// private transient Reactant[] lawReactants;
	//
	// /** OpFilters of current Reactant. */
	// private transient List<OpFilter> opFilters;
	//
	// /** Reference to the current pattern's name. */
	// private transient PatternNameTerm currPatternName;
	//
	// /**
	// * <p>
	// * Builds a new {@link EcolawToUpdTemplate}.
	// * </p>
	// *
	// * @param anEcolaw
	// * The ecolaw to be translated
	// */
	// private EcolawToUpdTemplate(final Ecolaw anEcolaw) {
	// if (anEcolaw == null) {
	// throw new IllegalArgumentException("Cannot translate null");
	// }
	//
	// law = anEcolaw;
	// query = new StringBuilder("SELECT DISTINCT * WHERE ");
	// }
	//
	// @Override
	// public void visit(final Ecolaw elaw) {
	// elaw.getRate().accept(this);
	// lawReactants = elaw.reactants();
	// for (Reactant react : lawReactants) {
	// react.accept(this);
	// }
	// }
	//
	// @Override
	// public void visit(final Rate<?> rate) {
	// final Object rVal = rate.getRateValue();
	// if (rVal instanceof VarTerm<?> && ((VarTerm<?>) rVal).isVar()) {
	// ruleW((VarTerm<?>) rVal);
	// }
	// }
	//
	// @Override
	// public void visit(final Term<?> term) {
	// // Ignored because following rules order
	// }
	//
	// @Override
	// public void visit(final Filter filt) {
	// if (filt instanceof OpFilter) {
	// ruleLO((OpFilter) filt);
	// } else if (filt instanceof ClonesFilter) {
	// ruleLC((ClonesFilter) filt);
	// } else if (filt instanceof ExtendsFilter) {
	// ruleLE((ExtendsFilter) filt, opFilters);
	// }
	// }
	//
	// @Override
	// public void visit(final Product product) {
	// // Ignored because producing a SPARQL query for matching phase
	// }
	//
	// @Override
	// public void visit(final Reactant reactant) {
	// opFilters = new LinkedList<OpFilter>();
	// for (Filter filt : reactant.filters()) {
	// if (filt instanceof OpFilter) {
	// opFilters.add((OpFilter) filt);
	// }
	// }
	//
	// ruleLP(reactant);
	// }
	//
	// /* ==================== Rules (begin) ==================== */
	//
	// /* ==================== [W] ==================== */
	//
	// /**
	// * <p>
	// * Applies rule [W]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param var
	// * Input term
	// */
	// private void ruleW(final Term<?> var) {
	// if (var instanceof AnnotatedVarTerm<?>) {
	// ruleW((AnnotatedVarTerm<?>) var);
	// }
	// }
	//
	// /**
	// * <p>
	// * Applies rule [W]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param var
	// * Input variable term
	// */
	// private void ruleW(final VarTerm<?> var) {
	// if (var instanceof AnnotatedVarTerm<?>) {
	// ruleW((AnnotatedVarTerm<?>) var);
	// }
	// }
	//
	// /**
	// * <p>
	// * Applies rule [W]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param var
	// * Input annotated variable term
	// */
	// private void ruleW(final AnnotatedVarTerm<?> var) {
	// if (var.getFormula() == null) {
	// return;
	// }
	//
	// if (var.getFormula() instanceof IsFormula) {
	// // BIND rule
	// query.append(String.format("\tBIND( %s AS ?%s)\n", var.getFormula()
	// .getRightOp(), var.getVarName()));
	// } else {
	// // FILTER rule
	// query.append(String.format("\tFILTER(%s)\n", var.getFormula()
	// .getStringRepr()));
	// }
	//
	// query.append(" ");
	// }
	//
	// /* ==================== [TL] ==================== */
	//
	// /**
	// * <p>
	// * Applies rule [TL]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param term
	// * Input term
	// */
	// private void ruleTL(final Term<?> term) {
	// ruleTL(term, true);
	// }
	//
	// /**
	// * <p>
	// * Applies rule [TL]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param term
	// * Input term
	// * @param spaceAfter
	// * True in order to append a whitespace after, false otherwise
	// */
	// private void ruleTL(final Term<?> term, final boolean spaceAfter) {
	// if (term instanceof VarTerm<?>) {
	// ruleTL((VarTerm<?>) term, spaceAfter);
	// }
	// }
	//
	// /**
	// * <p>
	// * Applies rule [TL]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param term
	// * Input variable term
	// */
	// private void ruleTL(final VarTerm<?> term) {
	// ruleTL(term, true);
	// }
	//
	// /**
	// * <p>
	// * Applies rule [TL]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param term
	// * Input variable term
	// * @param spaceAfter
	// * True in order to append a whitespace after, false otherwise
	// */
	// private void ruleTL(final VarTerm<?> term, final boolean spaceAfter) {
	// if (term.isGround()) {
	// query.append(term.toString());
	// } else {
	// query.append("?" + term.getVarName());
	// }
	//
	// if (spaceAfter) {
	// query.append(" ");
	// }
	// }
	//
	// /* ==================== [LP] ==================== */
	//
	// /**
	// * <p>
	// * Applies rule [LP]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param react
	// * Input reactant
	// */
	// private void ruleLP(final Reactant react) {
	// currPatternName = react.getName();
	// for (Filter filt : react.filters()) {
	// filt.accept(this);
	// }
	// }
	//
	// /* ==================== [LC] ==================== */
	//
	// /**
	// * <p>
	// * Applies rule [LC]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input clones filter
	// */
	// private void ruleLC(final ClonesFilter filter) {
	// ruleLE(new ExtendsFilterImpl(filter.getSource()), opFilters);
	// ruleLE(new ExtendsFilterImpl(new SDescTermImpl(currPatternName)),
	// AuxFunctions.getInstance().c(opFilters));
	// }
	//
	// /* ==================== [LE] ==================== */
	//
	// /**
	// * <p>
	// * Applies rule [LE]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input extends filter
	// * @param ops
	// * List of OpFilters
	// */
	// private void ruleLE(final ExtendsFilter filter, final List<OpFilter> ops)
	// {
	// query.append("\tFILTER NOT EXISTS {\n");
	// query.append("\t\t").append(currPatternName).append(" ?p ?o .\n");
	// // w
	// for (OpFilter opFilt : ops) {
	// ruleEE(opFilt, filter.getSource());
	// }
	//
	// // w'
	// query.append("\t\tFILTER NOT EXISTS {\n");
	// query.append("\t\t\t?")
	// .append(filter.getSource().getVarName().replace(".D", ""))
	// .append(" ?p ?o\n");
	// query.append("\t\t}\n");
	//
	// query.append("\t}\n");
	//
	// // w''
	// for (OpFilter opFilt : ops) {
	// ruleEEVar(opFilt, filter.getSource());
	// }
	// }
	//
	// /* ==================== [EE] ==================== */
	//
	// /**
	// * <p>
	// * Applies rule [EE]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filt
	// * Input extends filter
	// * @param y
	// * Semantic Description from y.D
	// */
	// private void ruleEE(final OpFilter filt, final SDescTerm y) {
	// if (filt instanceof AssignFilter) {
	// ruleEEAssign((AssignFilter) filt);
	// } else if (filt instanceof HasNotFilter) {
	// if (filt.getRightTerm().isVar()) {
	// ruleEEVar((HasNotFilter) filt, y);
	// } else {
	// ruleEEHasNot((HasNotFilter) filt);
	// }
	// }
	// }
	//
	// /* ===>> [EE=] <<=== */
	//
	// /**
	// * <p>
	// * Applies rule [EE=]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filt
	// * Input extends filter
	// */
	// private void ruleEEAssign(final AssignFilter filt) {
	// query.append("\t\tFILTER (?p != ");
	// ruleTL(filt.getLeftTerm(), false);
	// query.append(")\n");
	// }
	//
	// /* ===>> [EEn] <<=== */
	//
	// /**
	// * <p>
	// * Applies rule [EEn]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filt
	// * Input extends filter
	// */
	// private void ruleEEHasNot(final HasNotFilter filt) {
	// query.append("\t\tFILTER (?p != ");
	// ruleTL(filt.getLeftTerm());
	// query.append("|| ");
	// if (filt.getRightTerm().isGround()) {
	// query.append("(");
	// boolean notFirst = false;
	// for (Term<?> t : filt.getRightTerm().getValue()) {
	// if (notFirst) {
	// query.append(" && ");
	// }
	//
	// query.append("?o != ");
	// ruleTL(t, false);
	// notFirst = true;
	// }
	// query.append(")");
	// } else {
	// query.append("true");
	// }
	// query.append(")\n");
	// }
	//
	// /* ===>> [EE?] <<=== */
	//
	// /**
	// * <p>
	// * Applies rule [EE?]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filt
	// * Input op filter
	// * @param y
	// * Semantic Description from y.D
	// */
	// private void ruleEEVar(final OpFilter filt, final SDescTerm y) {
	// final List<LookUpResult> triples = AuxFunctions.getInstance().look(
	// lawReactants, filt.getRightTerm());
	// query.append("\tFILTER NOT EXISTS {\n");
	// query.append("\t\t").append(currPatternName).append(" ");
	// ruleTL(filt.getLeftTerm());
	// query.append(" ?o .\n");
	// query.append("\t\t").append(y.getVarName().replace(".D", ""))
	// .append(" ");
	// ruleTL(filt.getLeftTerm());
	// query.append(" ?o .\n\t\tFILTER NOT EXISTS {\n");
	// for (LookUpResult res : triples) {
	// query.append("\t\t\t")
	// .append(res.getY().toString().replace(".D", ""))
	// .append(" ").append(res.getT()).append(" ?o\n");
	// }
	// query.append("\t\t}\n");
	// query.append("\t}\n");
	//
	// ruleW(filt.getLeftTerm());
	// }
	//
	// /* ==================== [LO] ==================== */
	//
	// /**
	// * <p>
	// * Applies rule [LO]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input op filter
	// */
	// private void ruleLO(final OpFilter filter) {
	// if (filter instanceof HasFilter) {
	// ruleLOHas((HasFilter) filter);
	// } else if (filter instanceof HasNotFilter) {
	// ruleLOHasNot((HasNotFilter) filter);
	// } else if (filter instanceof AssignFilter) {
	// ruleLOAssign((AssignFilter) filter);
	// }
	// }
	//
	// /* ===>> [LO-has] <<=== */
	//
	// /**
	// * <p>
	// * Applies rule [LOh]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input has filter
	// */
	// private void ruleLOHas(final HasFilter filter) {
	// if (filter.getRightTerm().isVar()) {
	// ruleLOHasVar(filter);
	// } else {
	// ruleLOHasGround(filter);
	// }
	// }
	//
	// /**
	// * <p>
	// * Applies rule [LOh]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input ground has filter
	// */
	// private void ruleLOHasGround(final HasFilter filter) {
	// for (Term<?> obj : filter.getRightTerm().getValue()) {
	// query.append("\t").append(currPatternName).append(" ");
	// ruleTL(filter.getLeftTerm());
	// ruleTL(obj);
	// query.append(".\n");
	//
	// ruleW(obj);
	// }
	//
	// ruleW(filter.getLeftTerm());
	// }
	//
	// /**
	// * <p>
	// * Applies rule [LOh?]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input var has filter
	// */
	// private void ruleLOHasVar(final HasFilter filter) {
	// final List<LookUpResult> triples = AuxFunctions.getInstance().look(
	// lawReactants, filter.getRightTerm());
	// query.append("\tFILTER NOT EXISTS {\n");
	// query.append("\t\t").append(currPatternName).append(" ");
	// ruleTL(filter.getLeftTerm());
	// query.append(" ?o .\n\t\tFILTER NOT EXISTS {\n");
	// for (LookUpResult res : triples) {
	// query.append("\t\t\t").append(res.getY()).append(" ")
	// .append(res.getT()).append(" ?o .\n");
	// }
	// query.append("\t\t}\n");
	// query.append("\t}\n");
	// ruleW(filter.getLeftTerm());
	// }
	//
	// /* ===>> [LO-has-not] <<=== */
	//
	// /**
	// * <p>
	// * Applies rule [LOn]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input has-not filter
	// */
	// private void ruleLOHasNot(final HasNotFilter filter) {
	// if (filter.getRightTerm().isVar()) {
	// ruleLOHasNotVar(filter);
	// } else {
	// ruleLOHasNotGround(filter);
	// }
	// }
	//
	// /**
	// * <p>
	// * Applies rule [LOn]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input ground has-not filter
	// */
	// private void ruleLOHasNotGround(final HasNotFilter filter) {
	// query.append("\tFILTER NOT EXISTS {\n");
	// for (Term<?> obj : filter.getRightTerm().getValue()) {
	// query.append("\t\t").append(currPatternName).append(" ");
	// ruleTL(filter.getLeftTerm());
	// ruleTL(obj);
	// query.append(".\n");
	// ruleW(obj);
	// }
	//
	// query.append("\t}\n");
	// ruleW(filter.getLeftTerm());
	// }
	//
	// /**
	// * <p>
	// * Applies rule [LOn?]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input var has-not filter
	// */
	// private void ruleLOHasNotVar(final HasNotFilter filter) {
	// final List<LookUpResult> triples = AuxFunctions.getInstance().look(
	// lawReactants, filter.getRightTerm());
	// query.append("\tFILTER NOT EXISTS {\n");
	// query.append("\t\t").append(currPatternName).append(" ");
	// ruleTL(filter.getLeftTerm());
	// query.append(" ?o .\n");
	// for (LookUpResult res : triples) {
	// query.append("\t\t").append(res.getY()).append(" ")
	// .append(res.getT()).append(" ?o .\n");
	// }
	// query.append("\t}\n");
	// ruleW(filter.getLeftTerm());
	// }
	//
	// /* ===>> [LO-assign] <<=== */
	//
	// /**
	// * <p>
	// * Applies rule [LO=]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input assign filter
	// */
	// private void ruleLOAssign(final AssignFilter filter) {
	// if (filter.getRightTerm().isVar()) {
	// ruleLOAssignVar(filter);
	// } else {
	// ruleLOAssignGround(filter);
	// }
	// }
	//
	// /**
	// * <p>
	// * Applies rule [LO=]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input ground assign filter
	// */
	// private void ruleLOAssignGround(final AssignFilter filter) {
	// ruleLOHas(new HasFilterImpl(filter.getLeftTerm(),
	// filter.getRightTerm()));
	//
	// query.append("\tFILTER NOT EXISTS {\n\t\t").append(currPatternName)
	// .append(" ");
	// ruleTL(filter.getLeftTerm());
	// query.append("?o ");
	// query.append(".\n");
	// query.append("\t\tFILTER (");
	//
	// boolean notFirst = false;
	// for (Term<?> obj : filter.getRightTerm().getValue()) {
	// if (notFirst) {
	// query.append(" && ");
	// }
	//
	// query.append("o != ");
	// ruleTL(obj, false);
	// notFirst = true;
	// }
	//
	// query.append(")").append("\n\t}\n");
	// }
	//
	// /**
	// * <p>
	// * Applies rule [LO=?]. See TR.WP1.2011.06.
	// * </p>
	// *
	// * @param filter
	// * Input ground assign filter
	// */
	// private void ruleLOAssignVar(final AssignFilter filter) {
	// ruleLOHas(new HasFilterImpl(filter.getLeftTerm(),
	// filter.getRightTerm()));
	//
	// final List<LookUpResult> triples = AuxFunctions.getInstance().look(
	// lawReactants, filter.getRightTerm());
	// for (LookUpResult res : triples) {
	// if (!res.getY().equals(currPatternName)
	// && !res.getT().equals(filter.getLeftTerm())) {
	// query.append("\tFILTER NOT EXISTS {\n");
	// query.append("\t\t").append(res.getY()).append(" ")
	// .append(res.getT())
	// .append(" ?o .\n\t\tFILTER NOT EXISTS {\n");
	//
	// query.append("\t\t\t").append(currPatternName).append(" ");
	// ruleW(filter.getLeftTerm());
	// query.append(" ?o .\n");
	// query.append("\t\t}\n");
	// query.append("\t}\n");
	// }
	// }
	//
	// ruleW(filter.getLeftTerm());
	// }
	//
	// /* ===================== Rules (end) ===================== */
	//
	// /**
	// * <p>
	// * Creates a SPARQL query.
	// * </p>
	// *
	// * @return The query
	// */
	// private String createQuery() {
	// query.append("{\n");
	// law.accept(this);
	//
	// return query.append("}").toString();
	// }
	
	/**
	 * <p>
	 * Converts the law in a SPARQL query.
	 * </p>
	 * 
	 * @param law
	 *            The ecolaw to be converted
	 * @return The resulting SPARQL query
	 */
	public static MatchingEcolawTemplate convert(final Ecolaw law) {
		// TODO Implement it
		return null;
	}
}

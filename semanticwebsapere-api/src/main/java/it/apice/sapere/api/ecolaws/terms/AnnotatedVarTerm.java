package it.apice.sapere.api.ecolaws.terms;


public interface AnnotatedVarTerm<Type> extends VarTerm<Type> {

	Formula getFormula();
}

package it.apice.sapere.api.ecolaws.terms;

import it.apice.sapere.api.ecolaws.Term;

public interface VarTerm<Type> extends Term {

	Type getBoundValue();

	String getVarName();

	boolean isBound();
}

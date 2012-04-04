package it.apice.sapere.api.ecolaws.terms;

import it.apice.sapere.api.ecolaws.Term;

public interface Formula {

	boolean accept(Term term);
}

package it.apice.sapere.api.ecolaws.terms.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.ecolaws.terms.observers.BindingObserver;
import it.apice.sapere.api.ecolaws.visitor.EcolawVisitor;
import it.apice.sapere.api.lsas.impl.PropertyNameImpl;
import it.apice.sapere.api.lsas.values.URIValue;
import it.apice.sapere.api.lsas.values.impl.URIValueImpl;

/**
 * <p>
 * This class provides a wrapping which converts from URIValue to PropertyName
 * (for binding) and viceversa (for value retrieval).
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class URItoPNameConverter implements VarTerm<URIValue> {

	/** Wrapped term. */
	private final transient PropertyTermImpl wrapped;

	/**
	 * <p>
	 * Builds a new {@link URItoPNameConverter}.
	 * </p>
	 * 
	 * @param aTerm
	 *            The term to be wrapped
	 */
	public URItoPNameConverter(final PropertyTermImpl aTerm) {
		wrapped = aTerm;
	}

	@Override
	public final boolean isGround() {
		return wrapped.isGround();
	}

	@Override
	public final boolean isVar() {
		return wrapped.isVar();
	}

	@Override
	public final boolean isBound() {
		return wrapped.isBound();
	}

	@Override
	public final URIValue getValue() {
		return new URIValueImpl(wrapped.getValue().getValue());
	}

	@Override
	public final void accept(final EcolawVisitor visitor) {
		visitor.visit(wrapped);
	}

	@Override
	public final void addBindingObserver(final BindingObserver<URIValue> obs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final void removeBindingObserver(
			final BindingObserver<URIValue> obs) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final String getVarName() {
		return wrapped.getVarName();
	}

	@Override
	public final void bindTo(final URIValue value) throws SAPEREException {
		wrapped.bindTo(new PropertyNameImpl(value.getValue()));
	}

	@Override
	public final void clearBinding() throws SAPEREException {
		wrapped.clearBinding();
	}

	@Override
	public final void bindTo(final Term<?> value) throws SAPEREException {
		try {
			bindTo((URIValue) value.getValue());
		} catch (Exception cause) {
			throw new SAPEREException("Cannot complete binding", cause);
		}
	}

}

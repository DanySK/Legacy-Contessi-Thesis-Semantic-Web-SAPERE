package it.apice.sapere.api.ecolaws.match.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.ecolaws.Ecolaw;
import it.apice.sapere.api.ecolaws.Term;
import it.apice.sapere.api.ecolaws.match.MatchResult;
import it.apice.sapere.api.ecolaws.match.MutableMatchResult;
import it.apice.sapere.api.ecolaws.terms.VarTerm;
import it.apice.sapere.api.space.LSAspace;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * <p>
 * This class implements {@link MutableMatchResult} interfaces.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class MatchResultImpl implements MutableMatchResult {

	/** The original eco-law. */
	private final transient Ecolaw law;

	/** The LSA-space that processed the matching. */
	private final transient LSAspace space;

	/** All Mappings. */
	private final transient Map<String, Term<?>> mappings = 
			new HashMap<String, Term<?>>();

	/**
	 * <p>
	 * Builds a new {@link MatchResultImpl}.
	 * </p>
	 * 
	 * @param ecolaw
	 *            The original eco-law
	 * @param lsaSpace
	 *            The LSA-space which produced the matching
	 */
	public MatchResultImpl(final Ecolaw ecolaw, final LSAspace lsaSpace) {
		law = ecolaw;
		space = lsaSpace;
	}

	/**
	 * <p>
	 * Clone constructor.
	 * </p>
	 * 
	 * @param src
	 *            The source to be cloned
	 */
	protected MatchResultImpl(final MatchResult src) {
		law = src.getRelativeEcolaw();
		space = src.getLSAspace();
		for (Entry<String, Term<?>> entry : src.entries()) {
			mappings.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public final Term<?> lookup(final VarTerm<?> var) {
		return mappings.get(var.getVarName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Entry<String, Term<?>>[] entries() {
		final Set<Entry<String, Term<?>>> entries = mappings.entrySet();

		return entries.toArray(new Entry[entries.size()]);
	}

	@Override
	public final void assign(final VarTerm<?> var) throws SAPEREException {
		var.bindTo(lookup(var));
	}

	@Override
	public final Ecolaw getRelativeEcolaw() {
		return law;
	}

	@Override
	public final LSAspace getLSAspace() {
		return space;
	}

	@Override
	public final void register(final String varName, final Term<?> term) {
		mappings.put(varName, term);
	}

	@Override
	public final void reset() {
		mappings.clear();
	}
}

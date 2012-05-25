package it.apice.sapere.api.space.match.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.match.MatchResult;
import it.apice.sapere.api.space.match.MatchingEcolaw;
import it.apice.sapere.api.space.match.MatchingEcolawTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * This class implements the {@link MatchingEcolawTemplate} interface.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class MatchingEcolawTemplateImpl implements MatchingEcolawTemplate {

	/** Variable name pattern (in template). */
	private static final transient Pattern VARNAME_PATTERN = Pattern
			.compile("!(\\w+)");

	/** SPARUL query template. */
	private final String template;

	/** List of all template's variables. */
	private final String[] vars;

	/** Eco-law's label. */
	private final String label;

	/** Plain SPARUL query. */
	private final String plainQuery;

	/**
	 * <p>
	 * Builds a new {@link MatchingEcolawTemplateImpl}.
	 * </p>
	 * 
	 * @param queryTemplate
	 *            The template of the update query
	 */
	public MatchingEcolawTemplateImpl(final String queryTemplate) {
		this(queryTemplate, null);
	}

	/**
	 * <p>
	 * Builds a new {@link MatchingEcolawTemplateImpl}.
	 * </p>
	 * 
	 * @param queryTemplate
	 *            The template of the update query
	 * @param lLabel
	 *            Eco-law's label
	 */
	public MatchingEcolawTemplateImpl(final String queryTemplate,
			final String lLabel) {
		if (queryTemplate == null || queryTemplate.equals("")) {
			throw new IllegalArgumentException("Invalid template provided");
		}

		template = queryTemplate;
		vars = initVariablesArray();
		plainQuery = extractQuery();
		if (lLabel == null) {
			label = "";
		} else {
			label = lLabel;
		}
	}

	/**
	 * <p>
	 * Searches all variables (!&lt;var-name&gt;) contained in the template and
	 * stores them.
	 * </p>
	 * 
	 * @return The list of variables
	 */
	private String[] initVariablesArray() {
		final Set<String> res = new HashSet<String>();
		final Matcher matcher = VARNAME_PATTERN.matcher(template);

		while (matcher.find()) {
			res.add(matcher.group(1));
		}

		return res.toArray(new String[res.size()]);
	}

	@Override
	public final MatchingEcolaw bind(final MatchResult bindings)
			throws SAPEREException {
		return new MatchingEcolawImpl(applyTemplate(bindings), bindings);
	}

	/**
	 * <p>
	 * Replaces all variables name with provided values.
	 * </p>
	 * 
	 * @param bindings
	 *            Proposed variable's values from matching phase
	 * @return The concrete query
	 * @throws SAPEREException
	 *             Unknown variable (no proposed binding)
	 */
	private String applyTemplate(final MatchResult bindings)
			throws SAPEREException {
		final Matcher repl = VARNAME_PATTERN.matcher(template);
		final StringBuffer buff = new StringBuffer(template.length());

		while (repl.find()) {
			final String varName = repl.group(1);
			repl.appendReplacement(buff,
					Matcher.quoteReplacement(bindings.lookup(varName)));
		}

		repl.appendTail(buff);
		return buff.toString();
	}

	/**
	 * <p>
	 * Computes the SPARUL query with all unbound variables.
	 * </p>
	 * 
	 * @return The concrete query
	 */
	private String extractQuery() {
		final Matcher repl = VARNAME_PATTERN.matcher(template);
		final StringBuffer buff = new StringBuffer(template.length());

		while (repl.find()) {
			final String varName = repl.group(1);
			repl.appendReplacement(buff,
					Matcher.quoteReplacement("?" + varName));
		}

		repl.appendTail(buff);
		return buff.toString();
	}

	@Override
	public final String[] variablesNames() {
		return vars;
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result *= prime;
		if (template != null) {
			result += template.hashCode();
		}

		result *= prime;
		result += Arrays.hashCode(vars);

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
		final MatchingEcolawTemplateImpl other = 
				(MatchingEcolawTemplateImpl) obj;
		if (template == null) {
			if (other.template != null) {
				return false;
			}
		} else if (!template.equals(other.template)) {
			return false;
		}
		if (!Arrays.equals(vars, other.vars)) {
			return false;
		}
		return true;
	}

	@Override
	public final String toString() {
		return template;
	}

	@Override
	public final String getLabel() {
		return label;
	}

	@Override
	public String getPlainQuery() {
		return plainQuery;
	}
}

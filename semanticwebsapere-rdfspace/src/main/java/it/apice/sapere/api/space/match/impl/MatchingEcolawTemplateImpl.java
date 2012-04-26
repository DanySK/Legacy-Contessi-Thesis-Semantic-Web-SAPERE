package it.apice.sapere.api.space.match.impl;

import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.space.match.MatchResult;
import it.apice.sapere.api.space.match.MatchingEcolaw;
import it.apice.sapere.api.space.match.MatchingEcolawTemplate;

import java.util.LinkedList;
import java.util.List;
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

	/**
	 * <p>
	 * Builds a new {@link MatchingEcolawTemplateImpl}.
	 * </p>
	 * 
	 * @param queryTemplate
	 *            The template of the update query
	 */
	public MatchingEcolawTemplateImpl(final String queryTemplate) {
		if (queryTemplate == null || queryTemplate.equals("")) {
			throw new IllegalArgumentException("Invalid template provided");
		}

		template = queryTemplate;
		vars = initVariablesArray();
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
		final List<String> res = new LinkedList<String>();
		final Matcher matcher = VARNAME_PATTERN.matcher(template);

		while (matcher.find()) {
			res.add(matcher.group(1));
		}

		return res.toArray(new String[res.size()]);
	}

	@Override
	public final MatchingEcolaw bind(final MatchResult bindings)
			throws SAPEREException {
		return new MatchingEcolawImpl(applyTemplate(bindings),
				bindings.getLSAspace());
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

	@Override
	public final String[] variablesNames() {
		return vars;
	}

}

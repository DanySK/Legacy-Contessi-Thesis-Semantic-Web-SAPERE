package it.apice.sapere.api.space.match;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * <p>
 * Test case for Extended LSA-space entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestMatchingEcolawTemplate extends TestCase {

	/**
	 * <p>
	 * Tests the correctness of the application of variable bindings to the
	 * template, in order to obtain the final {@link MatchingEcolaw}.
	 * </p>
	 */
	@Test
	public final void testBinding() {
		final List<MatchingEcolawTemplate> templs = createTemplates();
		final List<MatchResult> bnds = createBindings();

		for (int idx = 0; idx < templs.size(); idx++) {
			final MatchingEcolawTemplate templ = templs.get(idx);
			assertNotNull(templ);

			try {
				final MatchingEcolaw mLaw = templ.bind(bnds.get(idx));
				assertNotNull(mLaw);
				assertTrue(
						String.format("Wrong binding: %s",
								mLaw.getUpdateQuery()),
						isExpectedResult(templ, mLaw));
			} catch (Exception ex) {
				fail(ex.getMessage());
			}
		}
	}

	/**
	 * <p>
	 * Creates a list of {@link MatchingEcolawTemplate}s to be tested.
	 * </p>
	 * 
	 * @return A list of templates
	 */
	protected abstract List<MatchingEcolawTemplate> createTemplates();

	/**
	 * <p>
	 * Creates a list of {@link MatchResult}s to be used for binding.
	 * </p>
	 * <p>
	 * The i-th {@link MatchResult} will be used to bind the i-th
	 * {@link MatchingEcolawTemplate}.
	 * </p>
	 * 
	 * @return A list of bindings
	 */
	protected abstract List<MatchResult> createBindings();

	/**
	 * <p>
	 * Verifies that the application of the template to a concrete law is
	 * correct.
	 * </p>
	 * 
	 * @param templ
	 *            The template that has been bound
	 * @param mLaw
	 *            The resulting matching law
	 * @return True if correct, false otherwise
	 */
	protected abstract boolean isExpectedResult(MatchingEcolawTemplate templ,
			MatchingEcolaw mLaw);
}

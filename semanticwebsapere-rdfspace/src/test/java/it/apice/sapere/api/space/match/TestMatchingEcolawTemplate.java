package it.apice.sapere.api.space.match;

import it.apice.sapere.api.PrivilegedLSAFactory;
import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.Property;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.values.BooleanValue;
import it.apice.sapere.api.lsas.values.DateTimeValue;
import it.apice.sapere.api.lsas.values.DoubleValue;
import it.apice.sapere.api.lsas.values.FloatValue;
import it.apice.sapere.api.lsas.values.IntegerValue;
import it.apice.sapere.api.lsas.values.LSAidValue;
import it.apice.sapere.api.lsas.values.LiteralValue;
import it.apice.sapere.api.lsas.values.LongValue;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.lsas.values.SDescValue;
import it.apice.sapere.api.lsas.values.URIValue;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.api.space.core.LSACompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.api.space.match.impl.MatchingEcolawTemplateImpl;
import it.apice.sapere.api.space.match.impl.MutableMatchResultImpl;
import it.apice.sapere.space.impl.LSAspaceImpl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * <p>
 * Tests {@link MatchingEcolawTemplateImpl}.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TestMatchingEcolawTemplate extends
		AbstractTestMatchingEcolawTemplate {

	/** Expected results. */
	private static final transient String[] EXPECTED_VALUES = new String[] {
			"INSERT DATA { }",
			"DELETE DATA { sapere:lsa01 rdf:type sapere:LSA }",
			"INSERT DATA { sapere:lsa01 foaf:knows sapere:lsa01 }" };

	/** Expected results index. */
	private transient int expIdx = 0;

	@Override
	protected final List<MatchingEcolawTemplate> createTemplates() {
		final List<MatchingEcolawTemplate> list = 
				new ArrayList<MatchingEcolawTemplate>();

		list.add(new MatchingEcolawTemplateImpl("INSERT DATA { }"));
		list.add(new MatchingEcolawTemplateImpl(
				"DELETE DATA { !x rdf:type sapere:LSA }"));
		list.add(new MatchingEcolawTemplateImpl("INSERT DATA { !x !y !x }"));

		return list;
	}

	@Override
	protected final List<MatchResult> createBindings() {
		final List<MatchResult> list = new ArrayList<MatchResult>();

		MutableMatchResult res = createMatchResult();
		list.add(res);

		try {
			res = createMatchResult();
			res.register("x", "sapere:lsa01", 1.0);
			list.add(res);

			res = createMatchResult();
			res.register("x", "sapere:lsa01", 1.0);
			res.register("y", "foaf:knows", 1.0);
			list.add(res);
		} catch (SAPEREException e) {
			Assert.fail(e.getMessage());
		}
		
		return list;
	}

	/**
	 * <p>
	 * Creates a {@link MatchResult}.
	 * </p>
	 * 
	 * @return a {@link MutableMatchResult}
	 */
	private MutableMatchResultImpl createMatchResult() {
		return new MutableMatchResultImpl(createDummySpace());
	}

	/**
	 * <p>
	 * Creates a dummy LSA-space for testing purposes.
	 * </p>
	 * 
	 * @return An LSA-space
	 */
	private LSAspaceCore<StmtIterator> createDummySpace() {
		return new LSAspaceImpl(new LSACompiler<StmtIterator>() {

			@Override
			public CompiledLSA<StmtIterator> parse(final String rdf,
					final RDFFormat format) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CompiledLSA<StmtIterator> compile(final LSA lsa) {
				// TODO Auto-generated method stub
				return null;
			}
		}, new PrivilegedLSAFactory() {

			@Override
			public String getNodeID() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DateTimeValue createPropertyValue(final Date value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public DoubleValue createPropertyValue(final double value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public FloatValue createPropertyValue(final float value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public BooleanValue createPropertyValue(final boolean value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LongValue createPropertyValue(final long value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public IntegerValue createPropertyValue(final int value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LiteralValue createPropertyValue(final String value,
					final String languageCode) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LiteralValue createPropertyValue(final String value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LSAidValue createPropertyValue(final LSAid value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public URIValue createPropertyValue(final URI value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public PropertyName createPropertyName(final URI value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Property createProperty(final URI name,
					final PropertyValue<?, ?>... initialValues) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Property createProperty(final URI name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SDescValue createNestingPropertyValue() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LSAid createLSAid() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LSA createLSA() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LSAid createLSAid(final URI id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LSA createLSA(final LSAid lsaId) {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	@Override
	protected final boolean isExpectedResult(
			final MatchingEcolawTemplate templ, final MatchingEcolaw mLaw) {
		return mLaw.getUpdateQuery().equals(EXPECTED_VALUES[expIdx++]);
	}

}

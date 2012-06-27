package it.apice.sapere.distdemo.analysis.impl;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.SemanticDescription;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPEREAgentSpec;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.observation.LSAEvent;
import it.apice.sapere.api.space.observation.LSAObserver;

import java.net.URI;
import java.util.Date;
import java.util.concurrent.Semaphore;

/**
 * <p>
 * This class models an agent which sense temperature.
 * </p>
 * <p>
 * Temperature sensing is simulated with a random generator.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TempAnalyzer implements SAPEREAgentSpec {

	/** SAPERE ontology namespace. */
	private static final transient String SAPERE_NS = "http://"
			+ "www.sapere-project.eu/ontologies/2012/0/sapere-model.owl#";

	/** Sensing ontology namespace. */
	private static final transient String SENSING_NS = "http://"
			+ "www.sapere-project.eu/distdemo#";

	/** Situation ontology namespace. */
	private static final transient String SITUATION_NS = "http://"
			+ "www.sapere-project.eu/distdemo/analyzer#";

	/** RDF namespace. */
	private static final transient String RDF_NS = "http://"
			+ "www.w3.org/1999/02/22-rdf-syntax-ns#";

	/** Provenance namespace. */
	private static final transient String PROVENANCE_NS = "http://"
			+ "www.sapere-project.eu/distdemo/provenance#";

	@Override
	public void behaviour(final LSAFactory factory, final LSAParser parser,
			final LSAspace space, final LogUtils out, final SAPEREAgent me)
			throws Exception {
		init(factory, space, out);

		while (me.isRunning()) {
			try {
				new Semaphore(0).acquire();
			} catch (InterruptedException ex) {
				out.spy("interrupted by the user");
			}
		}
	}

	/**
	 * <p>
	 * Handles initialization: enables eco-law and registers an observer for the
	 * aggregated value.
	 * </p>
	 * 
	 * @param fact
	 *            Reference to LSAFactory
	 * @param space
	 *            Reference to LSA-space
	 * @param out
	 *            Reference to sysout
	 * @throws Exception
	 *             Cannot init
	 */
	private void init(final LSAFactory fact, final LSAspace space,
			final LogUtils out) throws Exception {
		try {
			out.log("Defining situation..");
			final LSA lsa = fact.createLSA();
			lsa.getSemanticDescription()
					.addProperty(
							fact.createProperty(URI.create(RDF_NS + "type"),
									fact.createPropertyValue(URI
											.create(SITUATION_NS + "situation"
													+ "WithConfidence"))))
					.addProperty(
							fact.createProperty(
									URI.create(SENSING_NS + "confidence"),
									fact.createPropertyValue(1.0)))
					.addProperty(
							fact.createProperty(
									URI.create(SAPERE_NS + "updateTime"),
									fact.createPropertyValue(new Date())))
					.addProperty(
							fact.createProperty(URI.create(PROVENANCE_NS
									+ "deriverdBy"), fact
									.createPropertyValue(URI.create(fact
											.getNodeID()))))
					.addProperty(
							fact.createProperty(URI.create(SITUATION_NS
									+ "label"), fact
									.createPropertyValue("Max. temperature")));

			space.inject(lsa);

			out.log("Setting up observation..");
			space.observe(lsa.getLSAId(), new AnalyzerObserver(fact, out));

			out.log("Analyzer ready.");
		} catch (Exception ex) {
			out.error("failed", ex);
			throw ex;
		}
	}

	/**
	 * <p>
	 * {@link LSAObserver} implementation, ad hoc for the demo analyzer.
	 * </p>
	 * 
	 * @author Paolo Contessi
	 * 
	 */
	private static class AnalyzerObserver implements LSAObserver {

		/** Reference to output. */
		private final transient LogUtils out;

		/** situation:label property name. */
		private final transient PropertyName labelProp;

		/** situation:situation property name (it's the value). */
		private final transient PropertyName valueProp;

		/** sapere:updateTime property name. */
		private final transient PropertyName timeProp;

//		/** provenance:derivedFrom property name. */
//		private final transient PropertyName fromProp;

		/**
		 * <p>
		 * Builds a new {@link AnalyzerObserver}.
		 * </p>
		 * 
		 * @param fact
		 *            Reference to Factory
		 * @param log
		 *            Reference to stdout
		 */
		public AnalyzerObserver(final LSAFactory fact, final LogUtils log) {
			labelProp = fact.createPropertyName(URI.create(SITUATION_NS
					+ "label"));
			valueProp = fact.createPropertyName(URI.create(SITUATION_NS
					+ "situation"));
			timeProp = fact.createPropertyName(URI.create(SAPERE_NS
					+ "updateTime"));
//			fromProp = fact.createPropertyName(URI.create(PROVENANCE_NS
//					+ "derivedFrom"));

			out = log;
		}

		@Override
		public void eventOccurred(final LSAEvent ev) {
			final SemanticDescription sdesc = ev.getLSA()
					.getSemanticDescription();

			out.spy("notified: " + ev.getLSA());

			try {
				final String label = sdesc.get(labelProp).values()[0]
						.getValue().toString();
				final String val = sdesc.get(valueProp).values()[0].getValue()
						.toString();
				final String time = sdesc.get(timeProp).values()[0].getValue()
						.toString();
//				final String from = sdesc.get(fromProp).values()[0].getValue()
//						.toString();

				out.log(String.format("%s = %s (updated on: %s)",
						label.toUpperCase(), val, time));
			} catch (Exception ex) {
				out.spy(String.format("event dropped (%s): %s",
						ev.getOperationType(), ev.getLSA()));
			}
		}
	}
}

package it.apice.sapere.internal;

import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.node.LogUtils;
import it.apice.sapere.api.node.LoggerFactory;
import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.testcase.DisplayVLCService;
import it.apice.sapere.testcase.ResVLCIniter;

import java.net.URI;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * <p>
 * Bundle Activator class, used as warning and in order to avoid build cycle
 * failure.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class ITestsActivator implements BundleActivator {

	/** Sleep time between agents spawning. */
	private static final int SLEEP_TIME = 2000;

	/** Content property URI. */
	private static final transient URI NS_URI = URI
			.create("http://www.example.org/scenario#");

	/** Reference to logger. */
	private LogUtils log;

	@SuppressWarnings("rawtypes")
	@Override
	public final void start(final BundleContext context) throws Exception {
		final ServiceReference<LoggerFactory> lRef = context
				.getServiceReference(LoggerFactory.class);
		if (lRef != null) {
			log = context.getService(lRef).getLogger(getClass());
		}

		final ServiceReference<ReactionManager> ref = context
				.getServiceReference(ReactionManager.class);
		if (ref != null) {
			final ReactionManager rMan = context.getService(ref);
			final ServiceReference<EcolawCompiler> refL = context
					.getServiceReference(EcolawCompiler.class);
			if (refL != null) {
				final EcolawCompiler comp = context.getService(refL);

				rMan.addEcolaw(comp.create(getQuery(), getUpdateTemplate(),
						getRateVal()));

				context.ungetService(refL);
			}
			context.ungetService(ref);
		}

		final ServiceReference<SAPEREAgentsFactory> ref2 = context
				.getServiceReference(SAPEREAgentsFactory.class);
		if (ref2 != null) {
			final SAPEREAgentsFactory fact = context.getService(ref2);
			// fact.createAgent("test_agent", getSpec()).spawn();
			// Thread.sleep(SLEEP_TIME);
			// fact.createSysAgent("test_sys_agent", getSysSpec()).spawn();
			// Thread.sleep(2 * SLEEP_TIME);

			fact.createAgent("display", new DisplayVLCService()).spawn();
			fact.createAgent("scenario_initer", new ResVLCIniter()).spawn();
			context.ungetService(ref2);
		}

		Thread.sleep(2 * SLEEP_TIME);
		final ServiceReference<LSAspaceCore> ref3 = context
				.getServiceReference(LSAspaceCore.class);
		if (ref3 != null) {
			final LSAspaceCore space = context.getService(ref3);
			log.log(space.toString());
			context.ungetService(ref3);
		}

		if (lRef != null) {
			context.ungetService(lRef);
		}
	}

	/**
	 * <p>
	 * Retrieves a SPARQL Query.
	 * </p>
	 * 
	 * @return A SPARQL Query
	 */
	private String getQuery() {
		return String.format("PREFIX ex: <%s> PREFIX xsd: <%s> "
				+ "SELECT DISTINCT * WHERE { "
				+ "?disp ex:type \"display\"^^xsd:string; "
				+ "ex:state \"ready\"^^xsd:string . "
				+ "FILTER NOT EXISTS { ?disp ex:content ?disp_movie . } "
				+ "?res ex:type \"resource\"^^xsd:string; "
				+ "ex:content ?movie . }", NS_URI,
				"http://www.w3.org/2001/XMLSchema#");
	}

	/**
	 * <p>
	 * Retrieves a SPARUL Query Template.
	 * </p>
	 * 
	 * @return A SPARUL Query Template
	 */
	private String getUpdateTemplate() {
		return String.format("PREFIX ex: <%s> PREFIX xsd: <%s> MODIFY DELETE {"
				+ "!disp ex:state \"ready\"^^xsd:string . } "
				+ "INSERT { !disp ex:content !movie; "
				+ "ex:state \"on\"^^xsd:string . } WHERE { }", NS_URI,
				"http://www.w3.org/2001/XMLSchema#");
	}

	/**
	 * <p>
	 * Retrieves a Rate value.
	 * </p>
	 * 
	 * @return A Rate value
	 */
	private String getRateVal() {
		return "10.0";
	}

	@Override
	public void stop(final BundleContext context) throws Exception {

	}

	// /**
	// * <p>
	// * Retrieves an agent specification.
	// * </p>
	// *
	// * @return A {@link SAPEREAgentSpec}
	// */
	// private SAPEREAgentSpec getSpec() {
	// return new SAPEREAgentSpec() {
	//
	// @Override
	// public void behaviour(final LSAFactory factory,
	// final LSAspace space, final LogUtils out,
	// final SAPEREAgent me) throws Exception {
	// out.log("Testing Multi-Level LSA..");
	//
	// final LSA lsa = factory.createLSA();
	//
	// lsa.getSemanticDescription().addProperty(
	// factory.createProperty(
	// URI.create("http://www.example.org#prop"),
	// factory.createPropertyValue(true)));
	// final SDescValue v = factory.createNestingPropertyValue();
	// v.getValue().addProperty(
	// factory.createProperty(
	// URI.create("http://www.example.org#innerProp"),
	// factory.createPropertyValue("ciao")));
	// lsa.getSemanticDescription().addProperty(
	// factory.createProperty(
	// URI.create("http://www.example.org#bnode"), v));
	//
	// space.beginWrite();
	// try {
	// space.inject(lsa);
	// out.log(space.read(lsa.getLSAId()).toString());
	// } finally {
	// space.done();
	// }
	//
	// out.log("Done.");
	// }
	// };
	// }
	//
	// /**
	// * <p>
	// * Retrieves a system agent specification.
	// * </p>
	// *
	// * @return A {@link SAPEREAgentSpec}
	// */
	// private SAPERESysAgentSpec getSysSpec() {
	// return new SAPERESysAgentSpec() {
	//
	// @Override
	// public void behaviour(final NodeServices services,
	// final LogUtils out, final SAPEREAgent me) throws Exception {
	// out.log("Hello World!");
	// }
	// };
	// }
}

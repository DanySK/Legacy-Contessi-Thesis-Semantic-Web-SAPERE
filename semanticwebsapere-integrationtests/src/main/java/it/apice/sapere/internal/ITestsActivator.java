package it.apice.sapere.internal;

import java.net.URI;

import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.core.LSAspaceCore;
import it.apice.sapere.management.ReactionManager;
import it.apice.sapere.node.LogUtils;
import it.apice.sapere.node.LoggerFactory;
import it.apice.sapere.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.testcase.DisplayVLCService;
import it.apice.sapere.testcase.ResVLCIniter;

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
	private static final transient URI CONTENT_URI = URI
			.create("http://www.example.org/scenario#content");

	/** Time property URI. */
	private static final transient URI TIME_URI = URI
			.create("http://www.example.org/scenario#time");

	/** State property URI. */
	private static final transient URI STATE_URI = URI
			.create("http://www.example.org/scenario#state");

	/** Type property URI. */
	private static final transient URI TYPE_URI = URI
			.create("http://www.example.org/scenario#type");

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

		// log.warn("THIS IS AN INTEGRATION TESTS BUNDLE. "
		// + "IT SHOULD NOT BE DEPLOYED WITH THE APPLICATION.", null);

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
		return String.format("SELECT DISTINCT * WHERE { "
				+ "?res <%s> ?movie; <%s> \"resource\". "
				+ "?disp <%s> \"display\"; <%s> \"ready\". "
				+ "OPTIONAL {?disp <%s> ?disp_movie} "
				+ "FILTER(!BOUND(?disp_movie) || (?disp_movie!=?movie)). }",
				CONTENT_URI, TYPE_URI, TYPE_URI, STATE_URI, CONTENT_URI);
	}

	/**
	 * <p>
	 * Retrieves a SPARUL Query Template.
	 * </p>
	 * 
	 * @return A SPARUL Query Template
	 */
	private String getUpdateTemplate() {
		return String.format("DELETE { !disp "
				+ "<%s> \"ready\"^^<http://www.w3.org/2001/XMLSchema#string>; "
				//+ "<%s> ?t; <%s> ?disp_movie } WHERE { "
				//+ "!disp <%s> ?t; <%s> ?disp_movie }" 
				+ "} INSERT { !disp "
				+ "<%s> \"on\"^^<http://www.w3.org/2001/XMLSchema#string>; "
				+ "<%s> \"0\"^^<http://www.w3.org/2001/XMLSchema#int>; "
				+ "<%s> !movie. } ", STATE_URI, //TIME_URI, CONTENT_URI,
				//TIME_URI, CONTENT_URI, 
				STATE_URI, TIME_URI, CONTENT_URI);
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
	// final LSAspace space, final LogUtils out) throws Exception {
	// out.log("Hello World!");
	//
	// out.log("Bye bye.");
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
	// final LogUtils out) throws Exception {
	// out.log("Hello World!");
	//
	// out.log("Bye bye.");
	// }
	// };
	// }
}

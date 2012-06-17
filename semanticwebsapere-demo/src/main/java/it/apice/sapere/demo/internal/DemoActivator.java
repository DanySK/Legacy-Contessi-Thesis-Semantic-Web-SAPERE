package it.apice.sapere.demo.internal;

import java.net.URI;

import it.apice.sapere.api.management.ReactionManager;
import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.api.space.core.EcolawCompiler;
import it.apice.sapere.api.space.match.functions.MatchFunctRegistry;
import it.apice.sapere.demo.functions.impl.DistanceFunction;
import it.apice.sapere.demo.impl.SAPEREdemo;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.hp.hpl.jena.sparql.function.Function;

/**
 * <p>
 * SAPERE demo entry point.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class DemoActivator implements BundleActivator {

	/** Reference to {@link SAPEREAgentsFactory} service. */
	private transient ServiceReference<SAPEREAgentsFactory> factRef;

	/** {@link SAPEREAgentsFactory} instance. */
	private transient SAPEREAgentsFactory agentsFact;

	@Override
	public void start(final BundleContext context) throws Exception {
		factRef = context.getServiceReference(SAPEREAgentsFactory.class);
		if (factRef != null) {
			agentsFact = context.getService(factRef);
			new SAPEREdemo(agentsFact).execute();
		}

		@SuppressWarnings("rawtypes")
		final ServiceReference<MatchFunctRegistry> ref = context
				.getServiceReference(MatchFunctRegistry.class);
		if (ref != null) {
			@SuppressWarnings("unchecked")
			final MatchFunctRegistry<Function> mfr = context.getService(ref);
			
			mfr.register(URI.create("http://www.example.org/demo#distance"),
					DistanceFunction.class);
			
			context.ungetService(ref);
		}

		final ServiceReference<ReactionManager> rref = context
				.getServiceReference(ReactionManager.class);
		final ServiceReference<EcolawCompiler> cref = context
				.getServiceReference(EcolawCompiler.class);
		if (rref != null && cref != null) {
			final ReactionManager mng = context.getService(rref);
			final EcolawCompiler cmp = context.getService(cref);

			mng.addEcolaw(cmp.create(getNEARMatch(), getNEARUpdate(),
					getNEARRate()));

			context.ungetService(rref);
		}
	}

	/**
	 * <p>
	 * Provides NEAR eco-law match query.
	 * </p>
	 * 
	 * @return SPARQL query
	 */
	private String getNEARMatch() {
		return "PREFIX ex: <http://www.example.org/demo#> "
				+ "SELECT DISTINCT * WHERE { "
				+ "?plsa ex:type \"Person\"; ex:x ?px; ex:y ?py. "
				+ "?dlsa ex:type \"Display\"; ex:x ?dx; ex:y ?dy. "
				+ "FILTER (ex:distance(?px, ?py, ?dx, ?dy) < 7.0). }";
	}

	/**
	 * <p>
	 * Provides NEAR eco-law update query.
	 * </p>
	 * 
	 * @return SPARQL/Update query
	 */
	private String getNEARUpdate() {
		return "PREFIX ex: <http://www.example.org/demo#> "
				+ "INSERT { !dlsa ex:user !plsa. } WHERE { } ";
	}

	/**
	 * <p>
	 * Provides NEAR eco-law rate.
	 * </p>
	 * 
	 * @return Eco-law rate
	 */
	private String getNEARRate() {
		return "1.0";
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		if (factRef != null) {
			context.ungetService(factRef);
			factRef = null;
		}
	}

}

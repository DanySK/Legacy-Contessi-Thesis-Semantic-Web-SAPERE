package it.apice.sapere.profiling.agents;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.node.NodeServices;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPERESysAgentSpec;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.space.core.CompiledLSA;
import it.apice.sapere.profiling.utils.Utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * Profile agent which is capable of parse and compile a bunch of LSA provided
 * in the form of RDF statements written in a File.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class InjectReadUpdateRemoveAgent implements SAPERESysAgentSpec {

	/** Source of data. */
	private final transient File source;

	/**
	 * <p>
	 * Builds a new {@link InjectReadUpdateRemoveAgent}.
	 * </p>
	 * 
	 * @param dataFile
	 *            Source of data
	 */
	public InjectReadUpdateRemoveAgent(final File dataFile) {
		if (dataFile == null) {
			throw new IllegalArgumentException("Invalid source provided");
		}

		source = dataFile;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void behaviour(final NodeServices services, final LogUtils out,
			final SAPEREAgent me) throws Exception {
		out.log(String.format(
				"Inject-Read profiling with data from \"%s\"..", source));

		final List<CompiledLSA> cLsas = new LinkedList<CompiledLSA>();
		for (LSA lsa : Utils.parseInfo(services.getLSAParser(), source)) {
			final CompiledLSA cLsa = services.getLSACompiler().compile(lsa);
			cLsas.add(cLsa);

			services.getLSAspace().inject(cLsa);
			services.getLSAspace().read(cLsa.getLSAid());
			services.getLSAspace().update(cLsa);
		}

		out.log("Remove profiling with injected data");
		for (CompiledLSA cLsa : cLsas) {
			services.getLSAspace().remove(cLsa);
		}

		out.log("Done. Inject-Read-Remove profiling completed");
	}
}

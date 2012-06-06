package it.apice.sapere.profiling.agents;

import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.node.NodeServices;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPERESysAgentSpec;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.profiling.utils.Utils;

import java.io.File;

/**
 * <p>
 * Profile agent which is capable of parse and compile a bunch of LSA provided
 * in the form of RDF statements written in a File.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class ParseCompileAgent implements SAPERESysAgentSpec {

	/** Source of data. */
	private final transient File source;

	/**
	 * <p>
	 * Builds a new {@link ParseCompileAgent}.
	 * </p>
	 * 
	 * @param dataFile
	 *            Source of data
	 */
	public ParseCompileAgent(final File dataFile) {
		if (dataFile == null) {
			throw new IllegalArgumentException("Invalid source provided");
		}

		source = dataFile;
	}

	@Override
	public void behaviour(final NodeServices services, final LogUtils out,
			final SAPEREAgent me) throws Exception {
		for (LSA lsa : Utils.parseInfo(services.getLSAParser(), source)) {
			services.getLSACompiler().compile(lsa);
		}

	}
}

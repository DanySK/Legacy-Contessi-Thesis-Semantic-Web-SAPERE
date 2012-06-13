package it.apice.sapere.profiling.agents;

import it.apice.sapere.api.RDFFormat;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.node.NodeServices;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPERESysAgentSpec;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.space.core.CompiledLSA;
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
		out.log(String.format(
				"Parse-Compile profiling with data from \"%s\"..", source));

		for (LSA lsa : Utils.parseInfo(services.getLSAParser(), source)) {
			@SuppressWarnings("rawtypes")
			final CompiledLSA cLsa = services.getLSACompiler().compile(lsa);
			services.getLSACompiler().parse(cLsa.toString(RDFFormat.RDF_XML),
					RDFFormat.RDF_XML);
		}

		out.log("Done. Parse-Compile profiling completed");
	}
}

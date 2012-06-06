package it.apice.sapere.profiling.agents;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPEREAgentSpec;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.profiling.utils.Utils;

import java.io.File;
import java.util.Set;

/**
 * <p>
 * Profile agent which is capable of injecting a set of LSA provided in the form
 * of RDF statements written in a File.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class FillerAgent implements SAPEREAgentSpec {

	/** Source of data. */
	private final transient File source;

	/**
	 * <p>
	 * Builds a new {@link FillerAgent}.
	 * </p>
	 * 
	 * @param dataFile
	 *            Source of data
	 */
	public FillerAgent(final File dataFile) {
		if (dataFile == null) {
			throw new IllegalArgumentException("Invalid source provided");
		}

		source = dataFile;
	}

	@Override
	public void behaviour(final LSAFactory factory, final LSAParser parser,
			final LSAspace space, final LogUtils out, final SAPEREAgent me)
			throws Exception {
		out.log(String.format("Filling LSA-space with data from \"%s\"..",
				source));

		final Set<LSA> lsas = Utils.parseInfo(parser, source);
		for (LSA lsa : lsas) {
			space.inject(lsa);
		}

		out.log("Done. LSA-space filled");
	}
}

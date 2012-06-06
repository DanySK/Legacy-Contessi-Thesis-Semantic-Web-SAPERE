package it.apice.sapere.profiling.utils;

import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.lsas.LSA;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Set;

/**
 * <p>
 * Class providing Utilities.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public final class Utils {

	/**
	 * <p>
	 * Constructor obfuscator.
	 * </p>
	 */
	private Utils() {
		
	}

	/**
	 * <p>
	 * Parses LSAs from provided files.
	 * </p>
	 * 
	 * @param parser
	 *            Agent's {@link LSAParser} references
	 * @param source
	 *            File to be parsed
	 * @return All read LSAs
	 * @throws Exception
	 *             Cannot parse information
	 */
	public static Set<LSA> parseInfo(final LSAParser parser, final File source)
			throws Exception {
		InputStream in = null;
		try {
			in = new FileInputStream(source);
			return parser.parseLSAs(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
}

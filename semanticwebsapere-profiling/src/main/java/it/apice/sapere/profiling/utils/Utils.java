package it.apice.sapere.profiling.utils;

import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.lsas.LSA;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
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

	/**
	 * <p>
	 * Prints RDF prefixes in the provided writer.
	 * </p>
	 * 
	 * @param out
	 *            Destination
	 */
	public static void printPrefixes(final PrintWriter out) {
		out.println("@prefix rdf:\t<http://www.w3.org/1999/02/"
				+ "22-rdf-syntax-ns#> .");
		out.println("@prefix rdfs:\t<http://www.w3.org/2000/01/"
				+ "rdf-schema#> .");
		out.println("@prefix owl:\t<http://www.w3.org/2002/07/owl#> .");
		out.println("@prefix xsd:\t<http://www.w3.org/2001/XMLSchema#> .");
		out.println("@prefix ex:\t<http://www.example.org#> .");
		out.println("@prefix sapere:\t<http://www.sapere-project.eu/"
				+ "ontologies/2012/0/sapere-model.owl#> .");
		out.println("");
	}
}

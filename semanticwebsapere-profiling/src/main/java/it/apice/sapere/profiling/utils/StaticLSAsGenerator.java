package it.apice.sapere.profiling.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * <p>
 * Utility app that creates a set of LSAs.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class StaticLSAsGenerator {

	/** Destination filename. */
	private final transient File _dest;

	/** Max number of properties per LSA. */
	private final transient int _propsDep;

	/** Max number of values per LSA's property. */
	private final transient int _valPerProp;

	/** Max number of values per LSA's property. */
	private final transient int _numLSAs;

	/**
	 * <p>
	 * Builds a new {@link StaticLSAsGenerator}.
	 * </p>
	 * 
	 * @param dest
	 *            Filename to be produces
	 * @param propsDepth
	 *            Max number of properties per LSA
	 * @param valPerPropDepth
	 *            Max number of values per LSA's property
	 * @param numLSAs
	 *            Number of LSAs to be produced
	 */
	public StaticLSAsGenerator(final String dest, final int propsDepth,
			final int valPerPropDepth, final int numLSAs) {
		if (dest == null || dest.length() == 0) {
			throw new IllegalArgumentException("Invalid filename provided");
		}

		if (propsDepth < 0) {
			throw new IllegalArgumentException("Invalid propsDepth provided");
		}

		if (valPerPropDepth < 0) {
			throw new IllegalArgumentException(
					"Invalid valPerPropDepth provided");
		}

		if (numLSAs < 0) {
			throw new IllegalArgumentException("Invalid numLSAs provided");
		}

		_dest = new File(dest);
		_propsDep = propsDepth;
		_valPerProp = valPerPropDepth;
		_numLSAs = numLSAs;
	}

	/**
	 * @param args
	 *            CLI arguments
	 */
	public static void main(final String[] args) {
		try {
			if (args.length < 4) {
				throw new Exception();
			}

			new StaticLSAsGenerator(args[0], Integer.parseInt(args[1]),
					Integer.parseInt(args[2]), Integer.parseInt(args[3]))
					.execute();
		} catch (Exception ex) {
			System.err.println("4 needed parameters: "
					+ "<filename> <props-depth> <val-per-prop-depth> "
					+ "<num-of-lsas>");
		}
	}

	/**
	 * <p>
	 * Business logic.
	 * </p>
	 */
	public void execute() {
		System.out.println("==========================================");
		System.out.println("Static LSAs Generator");
		System.out.println("==========================================");
		System.out.println(String.format("Destination file: %s",
				_dest.getAbsolutePath()));
		System.out
				.println(String.format("Max num properties: %d", _propsDep));
		System.out.println(String
				.format("Max num values: %d", _valPerProp));
		System.out.println(String
				.format("#LSAs: %d", _numLSAs));
		System.out.println("------------------------------------------");
		System.out.println("\nRunning (static)..");

		generate();

		System.out.println("Done.");
	}

	/**
	 * <p>
	 * Generates the file.
	 * </p>
	 */
	private void generate() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(_dest);
			Utils.printPrefixes(out);

			for (int l = 0; l < _numLSAs; l++) {
				out.printf("sapere:lsa%d\n\t a sapere:LSA ", l);

				for (int i = 1; i <= _propsDep; i++) {
					for (int j = 1; j <= _valPerProp; j++) {
						out.printf(";\n\t ex:prop%d \"val%d-%d\" ", i, l, j);
					}
				}

				out.printf(".\n\n");

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}

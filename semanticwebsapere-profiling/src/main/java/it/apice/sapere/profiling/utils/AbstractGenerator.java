package it.apice.sapere.profiling.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * <p>
 * Abstract Utility class (generator).
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class AbstractGenerator {

	/** Destination filename. */
	private final transient File _dest;

	/** Max number of properties per LSA. */
	private final transient int _propsDepth;

	/** Max number of values per LSA's property. */
	private final transient int _valPerPropDepth;

	/** Max number of values per LSA's property. */
	private final transient int _numLSAs;

	/**
	 * <p>
	 * Builds a new {@link AbstractGenerator}.
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
	public AbstractGenerator(final String dest, final int propsDepth,
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
		_propsDepth = propsDepth;
		_valPerPropDepth = valPerPropDepth;
		_numLSAs = numLSAs;
	}

	/**
	 * @param args
	 *            CLI arguments
	 */
	public static void main(final String[] args) {
		try {
			new AbstractGenerator(args[0], Integer.parseInt(args[1]),
					Integer.parseInt(args[2]), Integer.parseInt(args[3]))
					.execute();
		} catch (Exception ex) {
			System.err.println("Needed 4 parameters: "
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
		System.out.println("LSAs Generator");
		System.out.println("==========================================");
		System.out.println(String.format("Destination: %s",
				_dest.getAbsolutePath()));
		System.out
				.println(String.format("Max num properties: %d", _propsDepth));
		System.out.println(String
				.format("Max num values: %d", _valPerPropDepth));
		System.out.println(String
				.format("#LSAs: %d", _numLSAs));
		System.out.println("------------------------------------------");
		System.out.println("\nRunning..");

		produce();

		System.out.println("Done.");
	}

	/**
	 * <p>
	 * Generates the file.
	 * </p>
	 */
	private void produce() {
		PrintWriter out = null;
		try {
			out = new PrintWriter(_dest);

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

			for (int l = 0; l < _numLSAs; l++) {
				out.printf("sapere:lsa%d\n\t a sapere:LSA ", l);

				for (int i = 1; i <= _propsDepth; i++) {
					for (int j = 1; j <= _valPerPropDepth; j++) {
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

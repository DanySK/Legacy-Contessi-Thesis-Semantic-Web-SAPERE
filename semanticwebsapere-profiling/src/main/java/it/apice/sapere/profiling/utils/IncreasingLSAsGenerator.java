package it.apice.sapere.profiling.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * <p>
 * Utility app that creates a set of LSA of increasing dimensions.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class IncreasingLSAsGenerator {

	/** Destination filename. */
	private final transient File _dest;

	/** Max number of properties per LSA. */
	private final transient int _propsDepth;

	/** Max number of values per LSA's property. */
	private final transient int _valPerPropDepth;

	/**
	 * <p>
	 * Builds a new {@link IncreasingLSAsGenerator}.
	 * </p>
	 * 
	 * @param dest
	 *            Filename to be produces
	 * @param propsDepth
	 *            Max number of properties per LSA
	 * @param valPerPropDepth
	 *            Max number of values per LSA's property
	 */
	public IncreasingLSAsGenerator(final String dest, final int propsDepth,
			final int valPerPropDepth) {
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

		_dest = new File(dest);
		_propsDepth = propsDepth;
		_valPerPropDepth = valPerPropDepth;
	}

	/**
	 * @param args
	 *            CLI arguments
	 */
	public static void main(final String[] args) {
		try {
			new IncreasingLSAsGenerator(args[0], Integer.parseInt(args[1]),
					Integer.parseInt(args[2])).execute();
		} catch (Exception ex) {
			System.err.println("Needed 3 parameters: "
					+ "<filename> <props-depth> <val-per-prop-depth>");
		}
	}

	/**
	 * <p>
	 * Business logic.
	 * </p>
	 */
	public void execute() {
		System.out.println("==========================================");
		System.out.println("Increasing LSAs Generator");
		System.out.println("==========================================");
		System.out.println(String.format("Destination: %s",
				_dest.getAbsolutePath()));
		System.out
				.println(String.format("Max num properties: %d", _propsDepth));
		System.out.println(String
				.format("Max num values: %d", _valPerPropDepth));
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
			Utils.printPrefixes(out);

			for (int numProps = 0; numProps <= _propsDepth; numProps++) {
				int lb = 0;
				int ub = 0;
				if (numProps > 0) {
					lb = 1;
					ub = _valPerPropDepth;
				}

				for (int numVals = lb; numVals <= ub; numVals++) {
					out.printf("sapere:lsa%d-%d\n\t a sapere:LSA ", numProps,
							numVals);

					for (int i = 1; i <= numProps; i++) {
						for (int j = 1; j <= numVals; j++) {
							out.printf(";\n\t ex:prop%d \"val%d-%d-%d\" ", i,
									numProps, numVals, j);
						}
					}

					out.printf(".\n\n");
				}
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

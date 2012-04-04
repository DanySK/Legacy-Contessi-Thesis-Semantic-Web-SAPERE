package it.apice.sapere.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * <p>
 * Utility class which translates RDF Graphs between different formats.
 * </p>
 * 
 * @author Semantic Web Programming Book Author
 * 
 */
public final class RDFSerializer {

	/** Number 3. */
	private static final int NUM_3 = 3;
	
	/** Number 4. */
	private static final int NUM_4 = 4;

	/**
	 * Private constructor.
	 */
	private RDFSerializer() {
		
	}

	/**
	 * This program takes 4 parameters: an input file an output file an input
	 * format an output format
	 * 
	 * It will then use Jena to read in the input file in the specified input
	 * format, and write it out in the specified output format.
	 * 
	 * @param args
	 *            CLI arguments
	 */
	public static void main(final String[] args) {
		String inputFileName = null;
		String outputFileName = null;
		String inputFileFormat = null;
		String outputFileFormat = null;
		FileOutputStream outputStream = null;
		FileInputStream inputStream = null;

		if (args.length != NUM_4) {
			System.err.println("Usage: java RDFSerializer "
					+ "<input file> <output file> "
					+ "<input format> <output format>");
			System.err.println("Valid format strings are: "
					+ "RDF/XML, N3, TURTLE, and N-TRIPLE");
			return;
		}

		inputFileName = args[0];
		outputFileName = args[1];
		inputFileFormat = args[2];
		outputFileFormat = args[NUM_3];

		try {
			inputStream = new FileInputStream(inputFileName);
			outputStream = new FileOutputStream(outputFileName);
		} catch (FileNotFoundException e) {
			System.err.println("'" + outputFileName
					+ "' is an invalid file name.");
			return;
		}

		Model rdfModel = ModelFactory.createDefaultModel();
		rdfModel.read(inputStream, null, inputFileFormat);
		rdfModel.write(outputStream, outputFileFormat);

		try {
			outputStream.close();
		} catch (IOException e) {
			System.err.println("Error writing to file.");
			return;
		}
	}
}

package it.apice.sapere;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.apache.log4j.Logger;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.update.GraphStore;
import com.hp.hpl.jena.update.GraphStoreFactory;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateRequest;

public class VisualQueriesTester extends JFrame {

	private static final transient boolean USE_PELLET = Boolean
			.parseBoolean(System.getProperty("use_pellet"));

	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = 3200084067002808904L;

	private final transient JPanel contentPane;

	/** RDF Jena Model. */
	private final transient Model model = initRDFModel();

	/** RDF Graph Store. */
	private final transient GraphStore store = GraphStoreFactory
			.create(DatasetFactory.create(model));

	private JTextArea txtOutput;

	private JLabel lblInsertQuery;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VisualQueriesTester frame = new VisualQueriesTester();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Model initRDFModel() {
		if (USE_PELLET) {
			Logger.getLogger(getClass()).info("Linking to Pellet");

			Reasoner reasoner = PelletReasonerFactory.theInstance().create();
			Model infModel = ModelFactory.createInfModel(reasoner,
					ModelFactory.createDefaultModel());
			return ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM,
					infModel);
		}

		return ModelFactory.createDefaultModel();
	}

	/**
	 * Create the frame.
	 */
	public VisualQueriesTester() {
		setTitle("Queries Tester");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 817, 436);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.2);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		panel.setBorder(new CompoundBorder(UIManager
				.getBorder("TitledBorder.aquaVariant"), new EmptyBorder(5, 0,
				0, 0)));
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));

		lblInsertQuery = new JLabel("Insert SELECT query (%d, %d):");
		lblInsertQuery.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		panel.add(lblInsertQuery, BorderLayout.NORTH);

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);

		final JTextArea queryInput = new JTextArea();
		queryInput.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				// Lets start with some default values for the line and column.
				int linenum = 1;
				int columnnum = 1;

				// We create a try catch to catch any exceptions. We will simply
				// ignore such an error for our demonstration.
				try {
					// First we find the position of the caret. This is the
					// number of where the caret is in relation to the start of
					// the JTextArea
					// in the upper left corner. We use this position to find
					// offset values (eg what line we are on for the given
					// position as well as
					// what position that line starts on.
					int caretpos = queryInput.getCaretPosition();
					linenum = queryInput.getLineOfOffset(caretpos);

					// We subtract the offset of where our line starts from the
					// overall caret position.
					// So lets say that we are on line 5 and that line starts at
					// caret position 100, if our caret position is currently
					// 106
					// we know that we must be on column 6 of line 5.
					columnnum = caretpos
							- queryInput.getLineStartOffset(linenum);

					// We have to add one here because line numbers start at 0
					// for getLineOfOffset and we want it to start at 1 for
					// display.
					linenum += 1;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				// Once we know the position of the line and the column, pass it
				// to a helper function for updating the status bar.
				updateStatus(linenum, columnnum);
			}
		});
		queryInput.setLineWrap(true);
		queryInput.setText("SELECT * WHERE {\n\n}");
		queryInput.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollPane.setViewportView(queryInput);

		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));

		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		panel_2.add(btnClear);

		Component horizontalGlue = Box.createHorizontalGlue();
		panel_2.add(horizontalGlue);

		JButton btnParseAsSparql = new JButton("Parse as SPARQL");
		btnParseAsSparql.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					printStartParsing();
					final String query = queryInput.getText();
					final Query q = QueryFactory.create(query);
					printQuery(q);
					final ResultSet iter = QueryExecutionFactory.create(q,
							model).execSelect();
					printOKOutput(iter);
				} catch (Exception ex) {
					printException(ex);
				}
			}

		});

		JButton btnFillRdfGraph = new JButton("Fill RDF Graph");
		btnFillRdfGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final int res = JOptionPane.showOptionDialog(
						VisualQueriesTester.this, "Choose a format:",
						"Fill RDF Graph", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, new String[] {
								"Turtle (N3)", "RDF/XML" }, "RDF/XML");
				if (res == JOptionPane.CLOSED_OPTION) {
					return;
				}

				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Pick up a "
						+ (res == 0 ? "Turtle" : "RDF/XML") + " specification");
				if (fc.showOpenDialog(VisualQueriesTester.this) == JFileChooser.APPROVE_OPTION
						&& fc.getSelectedFile() != null) {
					InputStream in = null;
					if (JOptionPane.showConfirmDialog(VisualQueriesTester.this,
							"Clear the RDF Graph before filling it?",
							"Fill RDF Graph", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						model.removeAll();
					}
					try {
						in = new FileInputStream(fc.getSelectedFile());
						model.read(in, null, (res == 0 ? "N3" : "RDF/XML"));
						JOptionPane.showMessageDialog(VisualQueriesTester.this,
								"Fill RDF Graph", String.format(
										"\"%s\" merged to the RDF Graph.", fc
												.getSelectedFile()
												.getAbsolutePath()),
								JOptionPane.INFORMATION_MESSAGE);
						printRDFGraphContent();
					} catch (Exception ex) {
						JOptionPane
								.showMessageDialog(
										VisualQueriesTester.this,
										"Cannot fill the RDF Graph: "
												+ ex.getMessage(),
										"Fill RDF Graph",
										JOptionPane.ERROR_MESSAGE);
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			}
		});

		JButton btnLoadVocabulary = new JButton("Load vocabulary");
		btnLoadVocabulary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final String uri = JOptionPane
						.showInputDialog("Specify the URI of the vocabulary/ontology");
				if (uri != null) {
					model.read(uri);
					JOptionPane.showMessageDialog(VisualQueriesTester.this,
							String.format("\"%s\" vocabulary/ontology loaded",
									uri), "Load vocabulary",
							JOptionPane.INFORMATION_MESSAGE);
					printRDFGraphContent();
				} else {
					JOptionPane.showMessageDialog(VisualQueriesTester.this,
							"Operation ABORTED.", "Load vocabulary",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnLoadVocabulary.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		panel_2.add(btnLoadVocabulary);
		btnFillRdfGraph.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		panel_2.add(btnFillRdfGraph);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		panel_2.add(horizontalStrut);
		btnParseAsSparql.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		panel_2.add(btnParseAsSparql);

		JButton btnParseAsSparql_1 = new JButton("Parse as SPARQL Update");
		btnParseAsSparql_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					printStartParsing();
					final String query = queryInput.getText();
					final UpdateRequest req = UpdateFactory.create(query);
					printQuery(req);
					// UpdateExecutionFactory.create(req, store,
					// querySolution).execute();
					UpdateExecutionFactory.create(req, store).execute();
					printOKOutput(null);
				} catch (Exception ex) {
					printException(ex);
				}
			}
		});
		btnParseAsSparql_1.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		panel_2.add(btnParseAsSparql_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new CompoundBorder(UIManager
				.getBorder("TitledBorder.aquaVariant"), new EmptyBorder(5, 0,
				0, 0)));
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		JLabel lblOutput = new JLabel("Output:");
		lblOutput.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		panel_1.add(lblOutput, BorderLayout.NORTH);

		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, BorderLayout.CENTER);

		txtOutput = new JTextArea();
		txtOutput.setLineWrap(true);
		txtOutput.setEditable(false);
		txtOutput.setFont(new Font("Consolas", Font.PLAIN, 12));
		scrollPane_1.setViewportView(txtOutput);
		txtOutput.append(String.format("Pellet enabled: %s%n", USE_PELLET));
	}

	private void printStartParsing() {
		txtOutput.append("Parsing query..\n");
	}

	private void printQuery(Query q) {
		txtOutput.append("Running SPARQL query..\n" + q.toString() + "\n\n");
	}

	private void printQuery(UpdateRequest q) {
		txtOutput.append("Running SPARQL Update query..\n" + q.toString()
				+ "\n\n");
	}

	private void printOKOutput(final ResultSet iter) {
		int counter = 1;
		while (iter != null && iter.hasNext()) {
			final QuerySolution sol = iter.nextSolution();
			final Iterator<?> vars = sol.varNames();
			if (vars != null && vars.hasNext()) {
				txtOutput.append("============== Variable Bindings (#"
						+ counter++ + ") ==============\n");
				while (vars.hasNext()) {
					final String var = (String) vars.next();
					txtOutput.append(String.format("?%s = %s\n", var,
							sol.get(var).toString()));
				}
			}
		}

		printRDFGraphContent();
	}

	/**
	 * 
	 */
	private void printRDFGraphContent() {
		txtOutput
				.append("============= Actual RDF GRAPH status =============\n");
		final StringWriter strW = new StringWriter();
		try {
			model.write(strW, "TURTLE");
			final String content = strW.toString();
			if (content.length() == 0) {
				txtOutput.append("(empty)\n");
			} else {
				txtOutput.append(content);
			}
		} catch (Exception ex) {
			txtOutput.append("ERROR: Cannot retrieve actual RDF Graph status");
		} finally {
			if (strW != null) {
				try {
					strW.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		txtOutput
				.append("===================================================\n\n");
	}

	private void printException(final Exception ex) {
		txtOutput.append("ERROR: " + ex.getMessage() + "\n");
	}

	private void updateStatus(int linenum, int columnnum) {
		lblInsertQuery.setText(String.format(
				"Insert SELECT query (line:%d, column: %d):", linenum,
				columnnum));
	}
}

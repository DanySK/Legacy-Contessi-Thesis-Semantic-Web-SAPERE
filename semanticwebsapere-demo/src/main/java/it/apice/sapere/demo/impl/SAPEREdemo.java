package it.apice.sapere.demo.impl;

import it.apice.sapere.api.node.agents.SAPEREAgentsFactory;
import it.apice.sapere.demo.agents.impl.DisplayAgent;
import it.apice.sapere.demo.agents.impl.PersonAgent;
import it.apice.sapere.demo.objs.impl.Display;
import it.apice.sapere.demo.objs.impl.Person;
import it.apice.sapere.demo.rendering.impl.MainFrame;

import javax.swing.SwingUtilities;

/**
 * <p>
 * SAPERE demo business logic.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class SAPEREdemo {

	/** Reference to the {@link SAPEREAgentsFactory}. */
	private final transient SAPEREAgentsFactory _fact;

	/** Main frame instance. */
	private final transient MainFrame frame;

	/**
	 * <p>
	 * Builds a new {@link SAPEREdemo}.
	 * </p>
	 * 
	 * @param factory
	 *            {@link SAPEREAgentsFactory} instance
	 */
	public SAPEREdemo(final SAPEREAgentsFactory factory) {
		if (factory == null) {
			throw new IllegalArgumentException("Invalid agents factory");
		}

		_fact = factory;
		frame = new MainFrame();
	}

	/**
	 * <p>
	 * Launches the business logic.
	 * </p>
	 * 
	 * @throws Exception Unexpected situation
	 */
	public void execute() throws Exception {
		final Display disp1 = new Display(10.0D, 22.0D);
		final Display disp2 = new Display(22.0D, 8.0D);
		final Person bobPerson = new Person("bob", 10.0D, 10.0D);
		final Person alicePerson = new Person("alice", 5.0D, 5.0D);

		_fact.createAgent("bob_agent", new PersonAgent(bobPerson)).spawn();
		_fact.createAgent("alice_agent", new PersonAgent(alicePerson)).spawn();

		_fact.createAgent("disp1_agent", new DisplayAgent(disp1)).spawn();
		_fact.createAgent("disp2_agent", new DisplayAgent(disp2)).spawn();

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.register(disp1);
				frame.register(disp2);

				frame.register(bobPerson);
				frame.register(alicePerson);

				frame.setVisible(true);
			}
		});
	}
}

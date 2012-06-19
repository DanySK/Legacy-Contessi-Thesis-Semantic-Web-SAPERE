package it.apice.sapere.demo.agents.impl;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPEREAgentSpec;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.demo.objs.impl.Display;

import java.net.URI;

/**
 * <p>
 * SAPEREagent that represents a Person.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class DisplayAgent implements SAPEREAgentSpec {

	/** Reference to Display model entity. */
	private final transient Display _display;

	/** Example type property. */
	private static final transient URI EX_TYPE = URI
			.create("http://www.example.org/demo#type");

	/** Display X-coord property. */
	private static final transient URI LOC_X = URI
			.create("http://www.example.org/demo#x");

	/** Display Y-coord property. */
	private static final transient URI LOC_Y = URI
			.create("http://www.example.org/demo#y");

	/** Nearsest Users (Person) property. */
	private static final transient URI USER_LSA_ID = URI
			.create("http://www.example.org/demo#user");

	/** Person name property. */
	private static final transient URI PERSON_NAME = URI
			.create("http://www.example.org/demo#name");

	/**
	 * <p>
	 * Builds a new {@link DisplayAgent}.
	 * </p>
	 * 
	 * @param display
	 *            The Display model entity
	 */
	public DisplayAgent(final Display display) {
		if (display == null) {
			throw new IllegalArgumentException("Invalid display provided");
		}

		_display = display;
	}

	@Override
	public void behaviour(final LSAFactory factory, final LSAParser parser,
			final LSAspace space, final LogUtils out, final SAPEREAgent me)
			throws Exception {

		// Defining personal LSA
		LSA myLsa = factory.createLSA();

		myLsa.getSemanticDescription()
				.addProperty(
						factory.createProperty(EX_TYPE,
								factory.createPropertyValue("Display")))
				.addProperty(
						factory.createProperty(LOC_X,
								factory.createPropertyValue(_display.getX())))
				.addProperty(
						factory.createProperty(LOC_Y,
								factory.createPropertyValue(_display.getY())));

		// Injecting personal LSA
		space.inject(myLsa);
		myLsa = space.read(myLsa.getLSAId());

		final DisplayLSAObserver obs = new DisplayLSAObserver(
				factory.createPropertyName(USER_LSA_ID));
		space.observe(myLsa.getLSAId(), obs);

		final PropertyName personNameProp = factory
				.createPropertyName(PERSON_NAME);
		while (me.isRunning()) {
			try {
				final StringBuilder builder = new StringBuilder();
				boolean notFirst = false;
				for (LSAid id : obs.getUsersLSAids()) {
					final LSA userLSA = space.read(id);
					final PropertyValue<?, ?>[] vals = userLSA
							.getSemanticDescription().get(personNameProp)
							.values();
					if (vals.length > 0) {
						if (notFirst) {
							builder.append(",\n");
						}

						builder.append(vals[0].getValue());
						notFirst = true;
					}
				}

				final String names = builder.toString();
				if (names.length() > 0) {
					_display.showGreetings(names);
				} else {
					_display.turnOff();
				}
			} catch (InterruptedException ex) {
				assert ex != null;
				out.spy("Task completed");
			}
		}
	}
}

package it.apice.sapere.demo.agents.impl;

import java.net.URI;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPEREAgentSpec;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.demo.objs.impl.Location;
import it.apice.sapere.demo.objs.impl.Person;

/**
 * <p>
 * SAPEREagent that represents a Person.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class PersonAgent implements SAPEREAgentSpec {

	/** Reference to Person model entity. */
	private final transient Person _person;

	/** Example type property. */
	private static final transient URI EX_TYPE = URI
			.create("http://www.example.org/demo#type");

	/** Person name property. */
	private static final transient URI PERSON_NAME = URI
			.create("http://www.example.org/demo#name");

	/** Person name property. */
	private static final transient URI LOC_X = URI
			.create("http://www.example.org/demo#x");

	/** Person name property. */
	private static final transient URI LOC_Y = URI
			.create("http://www.example.org/demo#y");

	/**
	 * <p>
	 * Builds a new {@link PersonAgent}.
	 * </p>
	 * 
	 * @param person
	 *            The Person model entity
	 */
	public PersonAgent(final Person person) {
		if (person == null) {
			throw new IllegalArgumentException("Invalid person provided");
		}

		_person = person;
	}

	@Override
	public void behaviour(final LSAFactory factory, final LSAParser parser,
			final LSAspace space, final LogUtils out, final SAPEREAgent me)
			throws Exception {

		// Defining personal LSA
		LSA myLsa = factory.createLSA();
		final PropertyName locXProp = factory.createPropertyName(LOC_X);
		final PropertyName locYProp = factory.createPropertyName(LOC_Y);

		PropertyValue<?, ?> lastKnownX = factory.createPropertyValue(0.0D);
		PropertyValue<?, ?> lastKnownY = factory.createPropertyValue(0.0D);

		myLsa.getSemanticDescription()
				.addProperty(
						factory.createProperty(PERSON_NAME,
								factory.createPropertyValue(_person.getName())))
				.addProperty(
						factory.createProperty(EX_TYPE,
								factory.createPropertyValue("Person")))
				.addProperty(factory.createProperty(LOC_X, lastKnownX))
				.addProperty(factory.createProperty(LOC_Y, lastKnownY));

		// Injecting personal LSA
		space.inject(myLsa);
		myLsa = space.read(myLsa.getLSAId());

		try {
			// Updating it each time a movement is made
			while (me.isRunning()) {
				final Location loc = _person.getNextMovement();

				out.log("Updating location in LSA-space");
				final PropertyValue<?, ?> actualX = factory
						.createPropertyValue(loc.getX());
				final PropertyValue<?, ?> actualY = factory
						.createPropertyValue(loc.getY());

				myLsa.getSemanticDescription().get(locXProp)
						.changeValue(lastKnownX, actualX);
				myLsa.getSemanticDescription().get(locYProp)
						.changeValue(lastKnownY, actualY);

				space.update(myLsa);

				lastKnownX = actualX;
				lastKnownY = actualY;
			}
		} catch (InterruptedException ex) {
			assert ex != null;
			out.spy("Task completed");
		}
	}

}

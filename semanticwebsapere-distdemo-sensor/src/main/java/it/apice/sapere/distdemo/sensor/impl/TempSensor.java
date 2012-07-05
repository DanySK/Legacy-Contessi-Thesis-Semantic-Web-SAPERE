package it.apice.sapere.distdemo.sensor.impl;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.SAPEREException;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.LSAid;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.lsas.values.SDescValue;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPEREAgentSpec;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.space.LSAspace;

import java.net.URI;
import java.util.Date;
import java.util.Random;

/**
 * <p>
 * This class models an agent which sense temperature.
 * </p>
 * <p>
 * Temperature sensing is simulated with a random generator.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class TempSensor implements SAPEREAgentSpec {

	/** Sensing ontology namespace. */
	private static final transient String SENSING_NS = "http://"
			+ "www.sapere-project.eu/distdemo#";

	/** RDF namespace. */
	private static final transient String RDF_NS = "http://"
			+ "www.w3.org/1999/02/22-rdf-syntax-ns#";

	/** XSD namespace. */
	private static final transient String XSD_NS = "http://"
			+ "www.w3.org/2001/XMLSchema#";

	/** UCUM namespace. */
	private static final transient String UCUM_NS = "http://"
			+ "idi.fundacionctic.org/muo/ucum-instances.owl&level=DL#";

	/** sensing:updateTime property name. */
	private transient PropertyName updTimePropName;

	/** sensing:value property name. */
	private transient PropertyName valuePropName;

	/** Actual rate. */
	private transient double rate;

	/** Rate increment. */
	private final transient double step;

	/** How many cycles before dec. */
	private final transient int cycles;

	/** Random Number Generator (RNG). */
	private final transient Random rng;

	/** Random seed. */
	private static final transient long SEED = 12345;

	/** Temperature range: from 0 to (TEMP_RANGE - 1) celsius degrees. */
	private static final transient int TEMP_RANGE = 50;

	/** The LSA-id of the sensor (when inited). */
	private transient LSAid sensorLSAid;

	/** Observation LSA. */
	private transient LSA obsLsa = null;

	/**
	 * <p>
	 * Builds a new {@link TempSensor}.
	 * </p>
	 * <p>
	 * Default values are:
	 * </p>
	 * <ul>
	 * <li>startingRate = 0.2 (5 sec)</li>
	 * <li>incStep = 0.0 (no increment)</li>
	 * <li>cyclesBeforeInc = 1</li>
	 * <li>counter = 0</li>
	 * </ul>
	 * 
	 * @see TempSensor#TempSensor(double, double, int, int)
	 */
	public TempSensor() {
		this(0.2, 0.0, 1, 0);
	}

	/**
	 * <p>
	 * Builds a new {@link TempSensor}.
	 * </p>
	 * <p>
	 * Default values are:
	 * </p>
	 * <ul>
	 * <li>startingRate = 0.2 (5 sec)</li>
	 * <li>incStep = 0.0 (no increment)</li>
	 * <li>cyclesBeforeInc = 1</li>
	 * </ul>
	 * 
	 * @see TempSensor#TempSensor(double, double, int, int)
	 * @param counter
	 *            Unique number of sensor, used for RNG's seed modification
	 */
	public TempSensor(final int counter) {
		this(0.2, 0.0, 1, counter);
	}

	/**
	 * <p>
	 * Builds a new {@link TempSensor}.
	 * </p>
	 * <p>
	 * Default values are:
	 * </p>
	 * <ul>
	 * <li>startingRate = 0.2 (5 sec)</li>
	 * </ul>
	 * 
	 * @param incStep
	 *            How much the rate should be incremented (1/s)
	 * @param cyclesBeforeInc
	 *            How many times the sensor should sense temperature at the
	 *            current rate before incrementing the rate
	 * @param counter
	 *            Unique number of sensor, used for RNG's seed modification
	 * 
	 * @see TempSensor#TempSensor(double, double, int)
	 */
	public TempSensor(final double incStep, final int cyclesBeforeInc,
			final int counter) {
		this(0.2, incStep, cyclesBeforeInc, counter);
	}

	/**
	 * <p>
	 * Builds a new {@link TempSensor}.
	 * </p>
	 * 
	 * @param startingRate
	 *            Initial sensing rate (1/s)
	 * @param incStep
	 *            How much the rate should be incremented (1/s)
	 * @param cyclesBeforeInc
	 *            How many times the sensor should sense temperature at the
	 *            current rate before incrementing the rate
	 * @param counter
	 *            Unique number of sensor, used for RNG's seed modification
	 */
	public TempSensor(final double startingRate, final double incStep,
			final int cyclesBeforeInc, final int counter) {
		if (startingRate <= 0.0) {
			throw new IllegalArgumentException("Invalid startingRate provided");
		}

		if (incStep < 0.0) {
			throw new IllegalArgumentException("Invalid incStep provided");
		}

		if (cyclesBeforeInc <= 0) {
			throw new IllegalArgumentException(
					"Invalid cyclesBeforeInc provided");
		}

		rate = startingRate;
		step = incStep;

		cycles = cyclesBeforeInc;
		rng = new Random(SEED + counter);
	}

	/**
	 * <p>
	 * Converts rate (1/s) to milliseconds (ms).
	 * </p>
	 * 
	 * @param aRate
	 *            Rate to be converted
	 * @return Converted value (time, in ms)
	 */
	private long rateToMillis(final double aRate) {
		long millis = Math.round(1.0 / aRate * 1000L);
		if (millis < 1) {
			millis = 1;
		}

		return millis;
	}

	@Override
	public void behaviour(final LSAFactory factory, final LSAParser parser,
			final LSAspace space, final LogUtils out, final SAPEREAgent me)
			throws Exception {
		init(factory, space, out);

		int counter = cycles;
		while (me.isRunning()) {
			if (cycles > 1 && step > 0.0 && counter == 0) {
				counter = cycles;
				rate += step;
			}

			try {
				Thread.sleep(rateToMillis(rate));
			} catch (InterruptedException ex) {
				out.spy("interrupted while sleeping");
			}

			sense(factory, space, out);
			counter--;
		}
	}

	/**
	 * <p>
	 * Handles sensing action.
	 * </p>
	 * 
	 * @param factory
	 *            Reference to LSAFactory
	 * @param space
	 *            Reference to LSA-space
	 * @param out
	 *            Reference to sysout
	 */
	private void sense(final LSAFactory factory, final LSAspace space,
			final LogUtils out) {
		try {
			// Sensed temperature
			final int temp = rng.nextInt(TEMP_RANGE);

			out.log("Publishing temperature value (" + temp + "), next in "
					+ rateToMillis(rate) + "ms..");
			publishTemperature(factory, space, temp);
			// out.spy(space.toString());
		} catch (Exception ex) {
			out.error("failed", ex);
		}
	}

	/**
	 * <p>
	 * Injects observation LSA, then keeps it updated.
	 * </p>
	 * 
	 * @param fact
	 *            Reference to LSAFactory
	 * @param space
	 *            Reference to LSA-space
	 * @param temp
	 *            Actual temperature
	 * @throws SAPEREException
	 *             Cannot complete (LSA-space interaction problems)
	 */
	private void publishTemperature(final LSAFactory fact,
			final LSAspace space, final int temp) throws SAPEREException {
		final PropertyValue<?, ?> val = fact.createPropertyValue(temp);
		final Date now = new Date();

		if (obsLsa == null) {
			obsLsa = fact.createLSA();

			updTimePropName = fact.createPropertyName(URI.create(SENSING_NS
					+ "updateTime"));
			valuePropName = fact.createPropertyName(URI.create(SENSING_NS
					+ "value"));

			obsLsa.getSemanticDescription()
					.addProperty(
							fact.createProperty(
									URI.create(RDF_NS + "type"),
									fact.createPropertyValue(URI
											.create(SENSING_NS + "Observation")
											)))
					.addProperty(
							fact.createProperty(
									URI.create(SENSING_NS + "startingTime"),
									fact.createPropertyValue(now)))
					.addProperty(
							fact.createProperty(updTimePropName.getValue(),
									fact.createPropertyValue(now)))
					.addProperty(
							fact.createProperty(
									URI.create(SENSING_NS + "observedBy"),
									fact.createPropertyValue(sensorLSAid)))
					.addProperty(
							fact.createProperty(valuePropName.getValue(), val));
			space.inject(obsLsa);
		} else {
			// See Issue tracking: #1
			obsLsa = space.read(obsLsa.getLSAId());
			obsLsa.getSemanticDescription().get(valuePropName)
					.clearAndAddValue(val);
			obsLsa.getSemanticDescription().get(updTimePropName)
					.clearAndAddValue(fact.createPropertyValue(now));
			space.update(obsLsa);
		}

		// See Issue tracking: #1
		obsLsa = space.read(obsLsa.getLSAId());
	}

	/**
	 * <p>
	 * Handles initialization: sensor information publication.
	 * </p>
	 * 
	 * @param factory
	 *            Reference to LSAFactory
	 * @param space
	 *            Reference to LSA-space
	 * @param out
	 *            Reference to sysout
	 * @throws Exception
	 *             Cannot init
	 */
	private void init(final LSAFactory factory, final LSAspace space,
			final LogUtils out) throws Exception {
		try {
			out.log("Declaring sensor type..");
			final LSAid stLsaId = declareSensorType(factory, space);
			out.log("Publishing sensor..");
			sensorLSAid = declareSensor(factory, space, stLsaId);
			out.log("Sensor ready.");
		} catch (Exception ex) {
			out.error("failed", ex);
			throw ex;
		}
	}

	/**
	 * <p>
	 * Injects SensorType LSA.
	 * </p>
	 * 
	 * @param fact
	 *            Reference to LSAFactory
	 * @param space
	 *            Reference to LSA-space
	 * @return The SensorType LSA-id
	 * @throws SAPEREException
	 *             Cannot complete declaration (LSA-space interaction problems)
	 */
	private LSAid declareSensorType(final LSAFactory fact, final LSAspace space)
			throws SAPEREException {
		final LSA stLsa = fact.createLSA();

		final SDescValue techSpec = fact.createNestingPropertyValue();
		techSpec.getValue()
				.addProperty(
						fact.createProperty(
								URI.create(SENSING_NS + "manufacturer"),
								fact.createPropertyValue("APICe Lab.", "it")))
				.addProperty(
						fact.createProperty(URI.create(SENSING_NS + "model"),
								fact.createPropertyValue("BetaTemp", "it")))
				.addProperty(
						fact.createProperty(
								URI.create(SENSING_NS + "unitOfMeasurement"),
								fact.createPropertyValue(URI.create(UCUM_NS
										+ "celsius"))));

		stLsa.getSemanticDescription()
				.addProperty(
						fact.createProperty(
								URI.create(RDF_NS + "type"),
								fact.createPropertyValue(URI.create(SENSING_NS
										+ "SensorType"))))
				.addProperty(
						fact.createProperty(
								URI.create(SENSING_NS + "valueType"),
								fact.createPropertyValue(URI.create(SENSING_NS
										+ "numeric"))))
				.addProperty(
						fact.createProperty(
								URI.create(SENSING_NS + "contextDomain"),
								fact.createPropertyValue(URI.create(XSD_NS
										+ "integer"))))
				.addProperty(
						fact.createProperty(
								URI.create(SENSING_NS
										+ "technicalSpecification"), techSpec));

		space.inject(stLsa);
		return stLsa.getLSAId();
	}

	/**
	 * <p>
	 * Injects Sensor LSA.
	 * </p>
	 * 
	 * @param fact
	 *            Reference to LSAFactory
	 * @param space
	 *            Reference to LSA-space
	 * @param stLsaId
	 *            The SensorType LSA-id
	 * @return The Sensor LSA-id
	 * @throws SAPEREException
	 *             Cannot complete declaration (LSA-space interaction problems)
	 */
	private LSAid declareSensor(final LSAFactory fact, final LSAspace space,
			final LSAid stLsaId) throws SAPEREException {
		final LSA sLsa = fact.createLSA();
		sLsa.getSemanticDescription()
				.addProperty(
						fact.createProperty(
								URI.create(RDF_NS + "type"),
								fact.createPropertyValue(URI.create(SENSING_NS
										+ "Sensor"))))
				.addProperty(
						fact.createProperty(
								URI.create(SENSING_NS + "sensorType"),
								fact.createPropertyValue(stLsaId)));

		space.inject(sLsa);
		return sLsa.getLSAId();
	}

}

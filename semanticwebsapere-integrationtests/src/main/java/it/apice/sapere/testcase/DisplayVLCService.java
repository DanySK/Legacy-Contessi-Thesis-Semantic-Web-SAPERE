package it.apice.sapere.testcase;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.LSAParser;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.lsas.PropertyName;
import it.apice.sapere.api.lsas.values.PropertyValue;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPEREAgentSpec;
import it.apice.sapere.api.node.logging.LogUtils;
import it.apice.sapere.api.space.LSAspace;
import it.apice.sapere.api.space.observation.LSAEvent;
import it.apice.sapere.api.space.observation.LSAObserver;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Semaphore;

/**
 * <p>
 * This class is part of a test case scenario.
 * </p>
 * <p>
 * It represents a SAPERE-agent which displays something when stimulated by the
 * environment.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class DisplayVLCService implements SAPEREAgentSpec, LSAObserver {

	/** The folder in which the movie should be found. */
	private static final String FOLDER_PATH = "/Users/conteit";

	/** Type property URI. */
	private static final transient URI TYPE_URI = URI
			.create("http://www.example.org/scenario#type");

	/** Content property URI. */
	private static final transient URI CONTENT_URI = URI
			.create("http://www.example.org/scenario#content");

	/** Time property URI. */
	private static final transient URI TIME_URI = URI
			.create("http://www.example.org/scenario#time");

	/** State property URI. */
	private static final transient URI STATE_URI = URI
			.create("http://www.example.org/scenario#state");

	/** User property URI. */
	private static final transient URI USER_URI = URI
			.create("http://www.example.org/scenario#user");

	/** Display Status LSA. */
	private transient LSA statusLsa;

	/** Agent's Standard Output. */
	private transient LogUtils stdOut;

	/** State property. */
	private transient PropertyName stateProp;

	/** Content property. */
	private transient PropertyName contentProp;

//	/** Time property. */
//	private transient PropertyName timeProp;
//
//	/** Paused state value. */
//	private transient PropertyValue<?, ?> pausedStateVal;
//
//	/** Reference to the LSA-space. */
//	private transient LSAspace lsaSpace;
//
//	/** Reference to the LSA Factory. */
//	private transient LSAFactory lsaFactory;
//
//	/** Starting time for the video. */
//	private transient double startingTime;
//
//	/** When playback has been started. */
//	private transient long startCounting;

	@Override
	public final void behaviour(final LSAFactory factory,
			final LSAParser parser, final LSAspace space,
			final LogUtils out, final SAPEREAgent me) throws Exception {
		stdOut = out;
//		lsaSpace = space;
//		lsaFactory = factory;

		stateProp = factory.createPropertyName(STATE_URI);
		contentProp = factory.createPropertyName(CONTENT_URI);
//		timeProp = factory.createPropertyName(TIME_URI);
//		pausedStateVal = factory.createPropertyValue("paused");

		out.log("Creating status LSA..");
		statusLsa = factory.createLSA();
		statusLsa
				.getSemanticDescription()
				.addProperty(
						factory.createProperty(TYPE_URI,
								factory.createPropertyValue("display")))
				.addProperty(factory.createProperty(CONTENT_URI))
				.addProperty(factory.createProperty(TIME_URI))
				.addProperty(
						factory.createProperty(STATE_URI,
								factory.createPropertyValue("ready")))
				.addProperty(factory.createProperty(USER_URI));

		out.log("LSA created. Injecting it..");
		space.inject(statusLsa);

		out.log("Observing status LSA..");
		space.observe(statusLsa.getLSAId(), this);

		out.log("Done.");

		while (me.isRunning()) {
			try {
				new Semaphore(0).acquire();
			} catch (InterruptedException ex) {
				assert ex != null;
			}
		}
	}

	@Override
	public final void eventOccurred(final LSAEvent ev) {
		stdOut.log("Event occurred on Status LSA");

		statusLsa = ev.getLSA();
//		stdOut.spy("notified LSA: " + statusLsa);
		try {
			updateDisplay();
		} catch (Exception ex) {
			stdOut.warn("Event dropped", ex);
		}
	}

	/**
	 * <p>
	 * Updates the display according to the new status.
	 * </p>
	 * 
	 * @return True if updated
	 */
	private boolean updateDisplay() {
		final PropertyValue<?, ?> status = statusLsa.getSemanticDescription()
				.get(stateProp).values()[0];
//		final PropertyValue<?, ?> time = statusLsa.getSemanticDescription()
//				.get(timeProp).values()[0];
		final String content = statusLsa.getSemanticDescription()
				.get(contentProp).values()[0].getValue().toString();

//		stdOut.spy(String.format("status: %s; time: %s; content: %s;", status,
//				time, content));

		if (status.toString().equals("on")) {
//			stdOut.spy("Activating..");
			try {
//				startingTime = Double.parseDouble(time.getValue().toString()
//						.trim());
//				startCounting = System.currentTimeMillis();
				final String cmd = String.format(
						"/Applications/VLC.app/Contents/MacOS/VLC "
								+ "%s/%s",
						FOLDER_PATH, content);
				stdOut.log("Playing.. " + cmd);
				Runtime.getRuntime().exec(cmd);

				return true;
			} catch (IOException e) {
				stdOut.error("Cannot update display", e);
			}
//		} else if (status.toString().equals("idle")) {
//			stdOut.spy("Stopping..");
//			try {
//				Runtime.getRuntime().exec("killall vlc");
//			} catch (IOException e) {
//				stdOut.error("Cannot update display", e);
//			}
//
//			final double timeViewed = startingTime + 1.0
//					* ((System.currentTimeMillis() - startCounting) / MS_IN_S);
//
//			statusLsa.getSemanticDescription().get(stateProp)
//					.changeValue(status, pausedStateVal);
//			statusLsa
//					.getSemanticDescription()
//					.get(timeProp)
//					.changeValue(time,
//							lsaFactory.createPropertyValue(timeViewed));
//
//			try {
//				lsaSpace.update(statusLsa);
//				return true;
//			} catch (SAPEREException e) {
//				stdOut.error("Cannot update status in LSA-space", e);
//			}
		}

		return false;
	}
}

package it.apice.sapere.testcase;

import it.apice.sapere.api.LSAFactory;
import it.apice.sapere.api.lsas.LSA;
import it.apice.sapere.api.node.LogUtils;
import it.apice.sapere.api.node.agents.SAPEREAgent;
import it.apice.sapere.api.node.agents.SAPEREAgentSpec;
import it.apice.sapere.api.space.LSAspace;

import java.net.URI;

/**
 * <p>
 * This class is part of a test case scenario.
 * </p>
 * <p>
 * It represents a SAPERE-agent which initializes the VLC service, providing a
 * movie to be displayed.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public class ResVLCIniter implements SAPEREAgentSpec {

	/** Time before behaviour starts. */
	private static final transient int SLEEP_TIME = 2000;

	/** Type property URI. */
	private static final transient URI TYPE_URI = URI
			.create("http://www.example.org/scenario#type");

	/** Topic property URI. */
	private static final transient URI TOPIC_URI = URI
			.create("http://www.example.org/scenario#topic");

	/** Content property URI. */
	private static final transient URI CONTENT_URI = URI
			.create("http://www.example.org/scenario#content");

	@Override
	public final void behaviour(final LSAFactory factory, final LSAspace space,
			final LogUtils out, final SAPEREAgent me) throws Exception {
		Thread.sleep(SLEEP_TIME);

		out.log("Creating initialization LSA..");
		final LSA lsa = factory.createLSA();
		lsa.getSemanticDescription()
				.addProperty(
						factory.createProperty(TYPE_URI,
								factory.createPropertyValue("resource")))
				.addProperty(
						factory.createProperty(TOPIC_URI,
								factory.createPropertyValue("movies")))
				.addProperty(
						factory.createProperty(CONTENT_URI,
								factory.createPropertyValue("trailer.mov")));

		out.log("LSA created. Injecting it..");
		space.inject(lsa);

		out.log("Done.");
	}
}

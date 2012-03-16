package it.apice.sapere.api.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import it.apice.sapere.api.AbstractModelTest;
import it.apice.sapere.api.ExtSAPEREFactory;

import java.net.URI;

import org.junit.Test;

/**
 * <p>
 * Test case for SAPERENode entity.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestSAPERENode extends AbstractModelTest {

	/**
	 * <p>
	 * Should create a new node with the provided URI.
	 * </p>
	 * 
	 * @param nodeId
	 *            Identifier of the new node
	 * @param factory
	 *            A reference to an Extended Factory
	 * @return A new SAPERENode
	 * @throws Exception
	 *             Invalid node-id provided
	 */
	protected abstract SAPERENode createNode(URI nodeId,
			ExtSAPEREFactory factory) throws Exception;

	/**
	 * <p>
	 * Test that node creation.
	 * </p>
	 * 
	 * @throws Exception
	 *             Something went wrong during test
	 */
	@Test
	public final void testNode() throws Exception {
		final URI nodeId = new URI("http://localhost:8080/sapere#node");
		final SAPERENode node = createNode(nodeId, createExtFactory());

		assertNotNull("Why no node has been created?", node);
		assertNotNull("Why no LSA-space has been created?",
				node.getLocalLSASpace());
		assertNotNull("Why no factory has been created?",
				node.getModelFactory());
		assertNotNull("Why URI has not been registered?", node.getNodeId());

		assertEquals("Why node-id is different by the provided one",
				node.getNodeId(), nodeId);

		try {
			createNode(null, createExtFactory());
			fail("NULL should not be a valid node-id");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			createNode(nodeId, null);
			fail("NULL should not be a valid extended factory");
		} catch (Exception ex) {
			assertTrue(true);
		}

		try {
			createNode(null, null);
			fail("NULL should not be a valid extended factory or node-id");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}
}

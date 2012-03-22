package it.apice.sapere.api.space;

import static org.junit.Assert.assertTrue;
import it.apice.sapere.api.Trying;

import org.junit.Test;

public class ITestExp {

	@Test
	public void testExp() {
		System.out.println("!!!!!!!!!!!!!!! INTEGRATION TEST !!!!!!!!!!!!!!!!!!!!!");
		assertTrue(Trying.foo());
	}

}

package it.apice.sapere.api.lsas.values;

import static org.junit.Assert.fail;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class test Property Value for a specific type: URI.
 * </p>
 * 
 * @author Paolo Contessi
 * 
 */
public abstract class AbstractTestURIValue extends
		AbstractTestPropertyValue<URI, URIValue> {

	@Override
	protected final List<URI> createValues() {
		final List<URI> res = new ArrayList<URI>(5);

		try {
			res.add(new URI("http://localhost"));
			res.add(new URI("http://localhost:8080/sapere"));
			res.add(new URI("file:///folder"));
			res.add(new URI("http://localhost/sapere#fragment"));
			res.add(new URI("http://localhost:8080/sapere#fragment"));
		} catch (Exception ex) {
			fail(ex.getMessage());
		}

		return res;
	}

	@Override
	protected final PropertyValue<URI, URIValue> createPropertyValue(
			final URI val) {
		return createFactory().createPropertyValue(val);
	}

}

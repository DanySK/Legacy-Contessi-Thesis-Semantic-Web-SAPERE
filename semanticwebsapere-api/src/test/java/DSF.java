import it.apice.sapere.api.EcolawFactory;


public abstract class DSF {

	abstract EcolawFactory fact();

	public void resd() {
		fact().createEcolaw().addProduct(fact().createProduct(""));
	}
}

import it.apice.sapere.api.EcolawFactory;

public abstract class InternalExps {

	abstract EcolawFactory factory();

	public void exp() {
		factory().createEcolaw("PROVA").product(
				factory().createProduct("FIRST"));
	}
}

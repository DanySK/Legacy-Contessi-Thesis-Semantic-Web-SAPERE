import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFVisitor;
import com.hp.hpl.jena.rdf.model.Resource;


public class Prova {

	public void foo() {
		new RDFNode() {
			
			@Override
			public Node asNode() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object visitWith(RDFVisitor rv) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean isURIResource() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isResource() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isLiteral() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAnon() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public RDFNode inModel(Model m) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Model getModel() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <T extends RDFNode> boolean canAs(Class<T> view) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Resource asResource() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Literal asLiteral() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public <T extends RDFNode> T as(Class<T> view) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}

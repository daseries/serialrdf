package anno;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;

public class Main {

	
	public static void main(String[] args) throws RDFParseException, UnsupportedRDFormatException, FileNotFoundException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		Model model = Rio.parse(new FileReader("book.ttl"),"",RDFFormat.TURTLE);

		IRI name = SimpleValueFactory.getInstance().createIRI("http://schema.org/name");
		IRI b = SimpleValueFactory.getInstance().createIRI("http://example.org/book/");
		Model books = model.filter(b, null, null );

		List<Book> Books = new ArrayList();
		List<Offer> Offers = new ArrayList();
		
		Class BookClass = Book.class;
		RDFObject BookAnno = (RDFObject) BookClass.getAnnotation(RDFObject.class);
		
		//System.out.println(BookAnno);
		//System.out.println(methods[0].getAnnotation(RDF.class));
		
		String id = "";
		int bCount = -1;
		int oCount = -1;
		Boolean a = true;
		
		
		for(Statement st: model) {
			String currentSub = st.getSubject().toString();
			String currentPred = st.getPredicate().stringValue();
			String currentObj = st.getObject().stringValue();
			Method[] methods = Helper.getMethods(currentSub);
			String pred = "";
			
				
				//System.out.println(currentSub);
				if(currentSub == id) {
					// Loop through the methods and check if it fits the currentObject
					for(Method m: methods) {
						Class<?>[] types = m.getParameterTypes();
						if(types.length < 1) {
							continue;
						}
						
						//System.out.println(st.getSubject().toString());
						RDF l = m.getAnnotation(RDF.class);
						
						if(l != null) {
							pred = l.value();
							//System.out.println(currentPred + " " + pred);
						}
						if(pred.equals(currentPred)) {
							//System.out.println(pred + " " + currentPred);
							//Invoke the correct method
							if(currentSub.contains(BookAnno.value())) {
									m.invoke(Books.get(bCount), Helper.getData(types[0], currentObj));
								
							} else {
									m.invoke(Offers.get(oCount), Helper.getData(types[0], currentObj));

							}
							
						}

						//System.out.println(m.getName());
						
						//m.invoke(Books.get(0), "");
					}
					
				} else {
					// Handle a new Object and store it in an array
					id = currentSub;
					if(currentSub.contains(BookAnno.value())) {
					Books.add(new Book());
					Books.get(bCount+1).setId(currentSub);
					bCount++;
					} else {
					Offers.add(new Offer());
					Offers.get(oCount+1).setId(currentSub);
					oCount++;
					}
					for(Method m: methods) {
						
						Class<?>[] types = m.getParameterTypes();
						if(types.length < 1) {
							continue;
						}
						RDF l = m.getAnnotation(RDF.class);

						if(l != null) {
							pred = l.value();
						}

						if(pred.equals(currentPred)) {
							if(currentSub.contains(BookAnno.value())) {
								m.invoke(Books.get(bCount), Helper.getData(types[0], currentObj));
								pred = "";
							} else {
								
								m.invoke(Offers.get(oCount), Helper.getData(types[0], currentObj));
								pred = "";
							}
						}
					}
					
				}
			
			
			//System.out.println(st.getSubject() + " " + st.getPredicate().toString() + " " + st.getObject().toString() );
		}
		
		for(Book bo: Books) {
			System.out.println(bo.toString());
		}
		for(Offer oo: Offers) {
			System.out.println(oo.toString());
		}
	}

}

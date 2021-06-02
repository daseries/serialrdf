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

	
	public static void main(String[] args) throws RDFParseException, UnsupportedRDFormatException, FileNotFoundException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		// TODO Auto-generated method stub
		Model model = Rio.parse(new FileReader("book.ttl"),"",RDFFormat.TURTLE);

		List<Class> classes = new ArrayList();
		
		classes.add(Book.class);
		classes.add(Offer.class);
		
		List<Object> objects = Helper.createInstances("book.ttl", classes);
		
		for(Object o: objects) {
			System.out.println(o.toString());
		}
		
		Helper.convertToRDF(objects);
	}
}

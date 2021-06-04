package anno;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Main {

	private static final Logger LOG = LogManager.getLogger(Main.class);
	
	public static void main(String[] args) throws RDFParseException, UnsupportedRDFormatException, FileNotFoundException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchMethodException, SecurityException {
		// TODO Auto-generated method stub
		Model model = Rio.parse(new FileReader("book.ttl"),"",RDFFormat.TURTLE);

		List<Class> classes = new ArrayList();
		
		classes.add(Book.class);
		classes.add(Offer.class);
		
		List<Object> objects = PojoRdfHelper.createInstances("book.ttl", classes);

		LOG.debug("Test of conversion FROM RDF TO Java");

		for(Object o: objects) {
//			System.out.println(o.toString());
			LOG.debug(o.toString());
		}
		
		String test = PojoRdfHelper.convertToRDF(objects,false);
		LOG.debug("Test of conversion FROM JAVA TO RDF");
		LOG.debug(test);
	}
}

package anno;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;

public class PojoRdfHelper {

    public static Object getDatatype(Type type, String cur) {
        if (type.getTypeName() == "java.lang.Double") {
            return Double.parseDouble(cur);
        }
        return cur;
    }

    public static String convertToRDF(List<Object> objects) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, IOException, NoSuchMethodException, SecurityException {
        return convertToRDF(objects, false);
    }


    public static String convertToRDF(List<Object> objects, boolean writeToFile) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException, NoSuchMethodException,
			SecurityException {
        ModelBuilder mb = new ModelBuilder();
        for (Object o : objects) {
            Class c = o.getClass();
            RDFObject objectAnnotation = (RDFObject) c.getAnnotation(RDFObject.class);
            RDFSubject subjectAnnotation = (RDFSubject) c.getAnnotation(RDFSubject.class);
            RDFNamespaces namespaces = (RDFNamespaces) c.getAnnotation(RDFNamespaces.class);
            mb.setNamespace(namespaces.value()[0], namespaces.value()[1]);

            Method methods[] = c.getDeclaredMethods();
            Method getID = c.getDeclaredMethod("getId");
            String subject = (String) getID.invoke(o);
            for (Method m : methods) {     	
                RDF methodAnnotation = m.getAnnotation(RDF.class);
                if (methodAnnotation != null && m.getParameterCount() < 1) {
                	if(m.getReturnType().equals(List.class)) {
                		List<Object> item = (List<Object>) m.invoke(o);
                		for(Object i: item) {
                			mb.add(subject, methodAnnotation.value().toString(), i);
                		}
                		continue;
                	}
                	
                	
                    mb.add(subject, methodAnnotation.value().toString(), m.invoke(o));
                } else {
                    continue;
                }
            }
        }
        Model model = mb.build();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Rio.write(model, byteArrayOutputStream, RDFFormat.TURTLE);
        if (writeToFile) {
			try (FileOutputStream out = new FileOutputStream("file.ttl")) {
				Rio.write(model, out, RDFFormat.TURTLE);
			}
		}
        return byteArrayOutputStream.toString();

    }

    public static List<Object> createInstances(String rdfFile, List<Class> classes) throws RDFParseException,
			UnsupportedRDFormatException, FileNotFoundException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException {
        Model model = Rio.parse(new FileReader(rdfFile), "", RDFFormat.TURTLE);

        List<Object> obj = new ArrayList();

        String id = "";
        int Count = -1;
        Boolean a = true;

        for (Statement st : model) {
            String currentSub = st.getSubject().toString();
            String currentPred = st.getPredicate().stringValue();
            String currentObj = st.getObject().stringValue();
            Class currentClass = null;
            String pred = "";

            for (Class c : classes) {
                RDFObject anno = (RDFObject) c.getAnnotation(RDFObject.class);
                if (currentSub.contains(anno.value())) {
                    currentClass = c;
                    break;
                }
            }

            if (currentClass != null) {
                Method[] methods = currentClass.getDeclaredMethods();
                if (currentSub == id) {
                    // Loop through the methods and check if it fits the currentObject
                    for (Method m : methods) {
                        Class<?>[] types = m.getParameterTypes();
                        if (types.length < 1) {
                            continue;
                        }

                        RDF l = m.getAnnotation(RDF.class);

                        if (l != null) {
                            pred = l.value();
                        } else {
                            continue;
                        }
                        if (pred.equals(currentPred)) {
                            m.invoke(obj.get(Count), PojoRdfHelper.getDatatype(types[0], currentObj));
                        }
                    }

                } else {
                    // Handle a new Object and store it in an array
                    id = currentSub;
                    obj.add(currentClass.newInstance());
                    Count++;

                    for (Method m : methods) {
                        Class<?>[] types = m.getParameterTypes();
                        if (types.length < 1) {
                            continue;
                        }

                        if (m.getAnnotation(RDFSubject.class) != null) {
                            m.invoke(obj.get(Count), PojoRdfHelper.getDatatype(types[0], currentSub));
                            continue;
                        }

                        RDF l = m.getAnnotation(RDF.class);

                        if (l != null) {
                            pred = l.value();
                        }

                        if (pred.equals(currentPred)) {
                            m.invoke(obj.get(Count), PojoRdfHelper.getDatatype(types[0], currentObj));
                        }

                    }
                }
            }
        }
        return obj;
    }

}

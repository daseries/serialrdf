package anno;

import java.lang.reflect.Method;

public class Helper {

	public static Method[] getMethods(String currentSub) {
		Class BookClass = Book.class;
		Method[] methodsB = BookClass.getDeclaredMethods();
		RDFObject BookAnno = (RDFObject) BookClass.getAnnotation(RDFObject.class);
		
		if(currentSub.contains(BookAnno.value())) {
			return methodsB;
		} 
		
		Class OfferClass = Offer.class;
		Method[] methodsO = OfferClass.getDeclaredMethods();
		return methodsO;
		
	}
	
	
}

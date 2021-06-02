package anno;

import java.util.ArrayList;
import java.util.List;

@RDFNamespaces({
    "book",
    "http://example.org/book/"
})

@RDFObject("http://example.org/book/")
public class Book {
	private String id; //Identifier
    private String name; //Name of the Book
    List<String> offers = new ArrayList<String>(); //All the offers for the book
    private String publisher; //The publisher of the book
    
    public Book() {
    }
    
    @RDFSubject("book:")
    public String getId() {
        return id;
    }

    @RDFSubject("book:")
    public void setId(String id) {
        this.id = id;
    }
	
    @RDF("http://schema.org/name")
    public String getName() {
        return name;
    }
    
    @RDF("http://schema.org/name")
    public void setName(String name) {
        this.name = name;
    }  
    
    @RDF("http://schema.org/offers")
	public List getOffers() {
		return offers;
	}
    
    @RDF("http://schema.org/offers")
    public void setOffers(String offers) {
    	this.offers.add(offers);
    }
    
    @RDF("http://schema.org/publisher")
	public String getPublisher() {
		return publisher;
	}
    
    @RDF("http://schema.org/publisher")
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("ID: " + this.id);
    	sb.append(" Name: " + this.name);
    	sb.append(" Offers: " + this.offers);
    	sb.append(" Publisher: " + this.publisher);
    	return sb.toString();
    }
    
    
}

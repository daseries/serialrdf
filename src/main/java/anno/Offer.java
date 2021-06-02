package anno;


@RDFNamespaces({
    "offer", 
    "http://example.org/offer/"
})

@RDFObject("http://example.org/offer/")
public class Offer {
	private String id; //Identifier
    private Double price; //Amount of the offer
    private String priceCurrency; //Currency of the price
    
    public Offer() {
    }
    
    @RDFSubject("offer:")
    public String getId() {
        return id;
    }

    @RDFSubject("offer:")
    public void setId(String id) {
        this.id = id;
    }
    
    @RDF("http://schema.org/price")
    public Double getPrice() {
        return price;
    }

    @RDF("http://schema.org/price")
    public void setPrice(Double price) {
        this.price = price;
    } 
    
    @RDF("http://schema.org/priceCurrency")
    public String getPriceCurrency() {
        return priceCurrency;
    }

    @RDF("http://schema.org/priceCurrency")
    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    } 
    
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("ID: " + this.id);
    	sb.append(" Price: " + this.price);
    	sb.append(" PriceCurrency: " + this.priceCurrency);
    	return sb.toString();
    }

}

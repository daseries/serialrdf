package anno;


@RDFNamespaces({
    "offer = http://example.org/offer/"
})

@RDF("offer:Offer")
public class Offer {
	private String id;
    private String price;
    private String priceCurrency;
    
    public Offer() {
    }
    
    @RDFSubject("offer:")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @RDF("http://schema.org/price")
    public String getPrice() {
        return price;
    }

    @RDF("http://schema.org/price")
    public void setPrice(String price) {
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

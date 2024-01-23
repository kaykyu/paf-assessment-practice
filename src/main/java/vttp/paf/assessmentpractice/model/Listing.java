package vttp.paf.assessmentpractice.model;

import org.bson.Document;

public class Listing {

    private String id;
    private String description;
    private String address;
    private Double price;
    private String amenities;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public Listing() {
    }

    public Listing(String id, String description, String address, Double price, String amenities) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.price = price;
        this.amenities = amenities;
    }

    public static Listing fromDocument(Document doc) {
        return new Listing(
            doc.getString("_id"),
            doc.getString("description"), 
            doc.getString("address"), 
            doc.getDouble("price"), 
            doc.getString("amenities"));
    }
}

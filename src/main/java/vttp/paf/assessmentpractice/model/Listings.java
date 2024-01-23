package vttp.paf.assessmentpractice.model;

import org.bson.Document;

public class Listings {

    private String name;
    private Double price;
    private String image;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Listings() {
    }

    public Listings(String id, String name, Double price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public static Listings fromDocument(Document doc) {
        return new Listings(
                doc.getString("_id"),
                doc.getString("name"),
                doc.getDouble("price"),
                doc.getString("image"));
    }


}

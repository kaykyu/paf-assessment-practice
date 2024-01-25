package vttp.paf.assessmentpractice.service;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp.paf.assessmentpractice.model.Booking;
import vttp.paf.assessmentpractice.model.Listing;
import vttp.paf.assessmentpractice.model.Listings;
import vttp.paf.assessmentpractice.model.Search;
import vttp.paf.assessmentpractice.repository.ListingsRepository;

@Service
public class ListingsService {
    
    @Autowired
    ListingsRepository listRepo;

    public List<String> getCountries() {
        return listRepo.getCountries();
    }
    
    public List<Listings> getListings(Search search) {
        return listRepo.getListings(search)
                .stream()
                .map(doc -> Listings.fromDocument(doc))
                .toList();
    }
    
    public Listing getListing(String id) {

        Document doc = listRepo.getListing(id);
        StringBuilder sb = new StringBuilder();

        Document add = doc.get("address", Document.class);
        String address = "%s, %s, %s".formatted(add.getString("street"), add.getString("suburb"), add.getString("country"));

        List<String> amenities = doc.getList("amenities", String.class);
        for (int i = 0; i < amenities.size() - 1; i++) {
            sb.append(amenities.get(i));
            sb.append(", ");
        }
        sb.append(amenities.get(amenities.size() - 1));

        return new Listing(doc.getString("_id"),
                doc.getString("description"),
                address,
                doc.getDouble("price"),
                sb.toString());
    }
    
    public String getSearchUrl(Search search) {
        return "search?country=%s&pax=%d&min=%.2f&max=%.2f".formatted(search.getCountry(), search.getPax(),
                search.getMin(),
                search.getMax());
    }
    
    public String booking(String id, Booking booking) {

        Integer updVacancy = listRepo.getVacancy(id) - booking.getDuration();

        if (updVacancy < 0)
            return null;
        
        try {
            return listRepo.makeBooking(id, booking, updVacancy);

        } catch (Exception e) {
            return null;
        }
    }
}

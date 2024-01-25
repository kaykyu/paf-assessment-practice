package vttp.paf.assessmentpractice.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import vttp.paf.assessmentpractice.model.Booking;
import vttp.paf.assessmentpractice.model.Search;

@Repository
public class ListingsRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JdbcTemplate sqlTemplate;

    /*
     * Mongodb query to get country list:
     * db.listings.distinct("address.country")
     */
    public List<String> getCountries() {
        return mongoTemplate.findDistinct(new Query(), "address.country", "listings", String.class);
    }

    /*
    Mongodb query to get listings:
    db.listings.aggregate([
    {
        $match: {
            price: {
                $gte: 1,
                $lte: 10000
            },
            "address.country": {$regex: "Australia", $options: "i"},
            accommodates: {$gte: 5}
        }
    },
    {
        $project: {
            _id: "$_id",
            name: "$name",
            price: "$price",
            image: "$images.picture_url"        
        }
    },
    {
        $sort: {price: -1}
    }
    ]);
     */
    public List<Document> getListings(Search search) {

        MatchOperation matchOps = Aggregation.match(Criteria.where("price").lte(search.getMax()).gte(search.getMin())
                .and("address.country").regex(search.getCountry(), "i")
                .and("accommodates").gte(search.getPax()));
        ProjectionOperation projectOps = Aggregation.project("_id")
                .and("$name").as("name")
                .and("$price").as("price")
                .and("$images.picture_url").as("image");
        SortOperation sortOps = Aggregation.sort(Sort.by(Direction.DESC, "price"));

        Aggregation pipeline = Aggregation.newAggregation(matchOps, projectOps, sortOps);
        return mongoTemplate.aggregate(pipeline, "listings", Document.class).getMappedResults();
    }

    /*
    Mongodb query to get listing
    db.listings.aggregate([
    {
        $match: {_id: "30167073"}
    },
    {
        $project: {
            _id: "$_id",
            description: "$description",
            address: "$address.street",
            price: "$price",
            amenities: "$amenities"
        }
    }]);
    */
    public Document getListing(String id) {

        MatchOperation matchOps = Aggregation.match(Criteria.where("_id").is(id));
        ProjectionOperation projectOps = Aggregation.project("_id")
                .and("$description").as("description")
                .and("$address.street").as("address")
                .and("$price").as("price")
                .and("$amenities").as("amenities");

        Aggregation pipeline = Aggregation.newAggregation(matchOps, projectOps);
        return mongoTemplate.aggregate(pipeline, "listings", Document.class).getMappedResults().get(0);
    }

    public Integer getVacancy(String id) {

        SqlRowSet rs = sqlTemplate.queryForRowSet(Queries.SQL_GET_VACANCY, id);
        if (rs.next())
            return rs.getInt("vacancy");
        return 0;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public String makeBooking(String id, Booking booking, Integer vacancy) throws Exception {
        
        String rid = UUID.randomUUID().toString().substring(0, 8);
        Integer update = sqlTemplate.update(Queries.SQL_UPDATE_VACANCY, vacancy, id);
        Integer save = sqlTemplate.update(Queries.SQL_SAVE_RESERVATION,
                rid,
                booking.getName(),
                booking.getEmail(),
                id,
                new Date(booking.getDate().getTime()),
                booking.getDuration());
       
        if (update == 1 && save == 1)
            return rid;
        else 
            throw new Exception();
    }
}

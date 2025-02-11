package VTTPpaf.cheatsheet.repository;

import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;

import VTTPpaf.cheatsheet.exception.ResourceNotFoundException;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Repository
public class Mongorepository {
    @Autowired
    private MongoTemplate template;

    //db.series.distinct('genres')
    //.distinct almost always returns an array/list
    public List<String> getAllGenres(){
        Query query = new Query();
        return template.findDistinct(query, "genres", "series", String.class);   
    }

    // db.series.find({
    //     'network.country.name':{$regex:'Canada', $options:"i"}
    // })
    public List<String> genresByCountry(String country){
        // Criteria criteria = Criteria.where(F_NETWORK_COUNTRY_NAME).is(country);
        Criteria criteria = Criteria.where("network.country.name")
            .regex(country, "i");

        Query query = Query.query(criteria);

        return template.findDistinct(query, "genres", "series", String.class);
    }


    // db.series.find({
    //     "rating.average": { $gte: 8 },  // Filter by rating >= 8
    //     "name": { $regex: "ar", $options: "i" }  // Case-insensitive regex filter on name
    // })
    // .sort({ "rating.average": -1 })  // Sort by rating.average in descending order
    // .skip(5)  // Skip the first 5 documents (pagination)
    // .limit(5)  // Limit the result to 5 documents
    // .projection({
    //     "_id": 0,  // Exclude the _id field
    //     "name": 1,  // Include the name field
    //     "summary": 1,  // Include the summary field
    //     "imageOriginal": 1,  // Include the imageOriginal field
    //     "rating.average": 1  // Include the rating.average field
    // })

    public List<Document> findSeriesByName(String name){
        //Define the search criteria
        Criteria criteria_by_rating = Criteria.where("rating.average")
            .gte(8);

        Criteria criteria_by_name = Criteria.where("name")
            .regex(name, "i")
            .andOperator(criteria_by_rating); //andOperator for 1 or more additional conditions, .and for only 2 total conditions

        //more compact but lose some readability
            // Criteria criteria_by_name = Criteria.andOperator(
            //     Criteria.where(F_NAME).regex(name, "i"),
            //     Criteria.where(F_RATING_AVERAGE).gte(8)
            // );



        //create the query based on the defined criteria
        Query query = Query.query(criteria_by_name)
            .with(
                Sort.by(Sort.Direction.DESC, "rating.average")    //sorting
            )
            .limit(5) //pagination
            .skip(5L); //pagination, L being long data type
        query.fields()
            .include("name", "summary", "image.original", "rating.average") //includes these fields
            .exclude("id"); //excludes these fields

        //perform the search, return list of document objects(each document is a row record)
        return template.find(query,Document.class, "series");
        // template.find(query, Document.class); DO NOT use this, will use default collection name based on java class name('Document')
    }



    
    // db.games.find({
    //     "name": { $regex: "gamename", $options: "i" }  // Case-insensitive regex filter on name
    // })
    // .sort({ "ranking": -1 })  // Sort by rating.average in descending order
    // .skip(0)  // Skip the first 5 documents (pagination)
    // .limit(5)  // Limit the result to 5 documents

    public List<JsonObject> getGames(String name, int limit, int offset){
        //grab game id and name, put into json array as value to games key
        Criteria criteria = Criteria.where("name")
            .regex(name, "i");

        Query query = Query.query(criteria)
            .with(Sort.by(Sort.Direction.ASC, "ranking"))
            .limit(limit)
            .skip(offset);

        List<Document> docList = template.find(query, Document.class, "games");
        // for(Document d: docList){
        //     String jString = d.toJson(); //from Document to JsonString
        //     Document doc = Document.parse(jString); //from JsonString to Document
        // }

        //turn list of documents, into list of jsonobjects(each objects hold 1 gid and 1 name)
        return docList.stream()
            .map(doc ->
                Json.createObjectBuilder()
                .add("game_id", doc.getInteger("gid"))
                .add("name", doc.getString("name"))
                .add("ranking", doc.getInteger("ranking"))
                .build()
                ).toList();
    }



    //db.tv_shows.find({ _id: ObjectId(‘abc123’) })
    public Document getGameById(String id) {
        try{
        ObjectId docId = new ObjectId(id);
        Document doc = template.findById(docId,Document.class, "games");
        return doc;
        }catch(IllegalArgumentException ex){
            throw new ResourceNotFoundException("Document with ID: " + id + " cannot be found");
        }
    }


    //drop collection
    // db.comment.drop();
    public void dropCollection(String collectionName){
        template.dropCollection(collectionName);
    }

    //count number of documents in collection
    //db.comment.countDocuments();
    public long countCollection(String collectionName){
        MongoCollection<Document> collectionTable = template.getCollection(collectionName);
        return collectionTable.countDocuments();
    }    


    //insert comment into collection
    // db.comment.insertOne({ "_id": "12345", "c_text": "This is a comment" });
    public <T> T insertComments(T doc, String collectionName){
        return template.insert(doc, collectionName);
    }


    //create an index on text for text searches
        //db.comment.createIndex({ c_text: "text", title: "text" });
    //searching text see day27/inclass
        //db.commentCL.find({ $text: { $search: "amazing love" } })
    public void createTextIndex(String fieldname, String collectionName){
        MongoCollection<Document> collectionTable = template.getCollection(collectionName);

        collectionTable.createIndex(Indexes.text(fieldname));
    }


    // New method for multi batch insert
        // var collection = db.getCollection(collectionName);
        // collection.insertMany(documents);
    public void insertCommentsBatch(List<Document> documents, String collectionName) {
        if (documents != null && !documents.isEmpty()) {
            MongoCollection<Document> collection = template.getCollection(collectionName);
            // Use insertMany to batch insert the documents list
            collection.insertMany(documents);
            System.out.println("Inserted " + documents.size() + " documents into collection " + collectionName);
        } else {
            System.out.println("No documents to insert into collection " + collectionName);
        }
    }



    
    // db.comments.updateMany(
    //     { c_id: "uuid" }, // Filter condition: match documents where c_id is "uuid"
    //     {
    //         $push: {
    //             edited: { // Push new object into the "edited" array
    //                 comment: "test",
    //                 rating: 8,
    //                 date: "2025-01-31"
    //             }
    //         }
    //     },
    //     {upsert:true}
    // )
    public void updateOldComment(String commentId, String comment, int rating, Date date){
        //check whether comment id is valid
        Criteria commentCriteria = Criteria.where("c_id").is(commentId);
        Query commentQuery = Query.query(commentCriteria);
        List<Document> docList = template.find(commentQuery, Document.class, "comments");
        if(docList.isEmpty()){
            throw new ResourceNotFoundException("Comment ID: " + commentId + " does not exist" );
        }


        Criteria criteria = Criteria.where("c_id").is(commentId);
        Query query = Query.query(criteria);

        Update updateOps = new Update()
            .push("edited", new Document("comment", comment)
            .append("rating", rating)
            .append("date", date));
        
        System.out.println(commentId + "\n" +  comment + "\n" + rating + "\n" + date);
        template.upsert(query, updateOps, "comments");

    }


}

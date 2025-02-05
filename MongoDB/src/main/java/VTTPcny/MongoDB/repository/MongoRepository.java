package VTTPcny.MongoDB.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.client.MongoCollection;

@Repository
public class MongoRepository {
    
    @Autowired
    private MongoTemplate template;

    public void insertPODocument(Document doc){
        template.insert(doc, "purchase_order");
    }



    public void insertLIDocument(List<Document> docList){
        MongoCollection<Document> collection = template.getCollection("line_item");
        // Use insertMany to batch insert the documents
        collection.insertMany(docList);
    }

}

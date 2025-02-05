package VTTPcny.MongoDB.service;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import VTTPcny.MongoDB.model.SalesSubscribe;
import VTTPcny.MongoDB.repository.MongoRepository;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;

@Service
public class MongoService {
    @Autowired
    private MongoRepository mongoRepository;
    
    public void jsonToDocument(JsonObject jObj) {

        try{
        Document purchaseOrder = new Document();
        purchaseOrder.append("po_id", jObj.getString("poId"))
            .append("name", jObj.getString("name"))
            .append("address", jObj.getString("address"))
            .append("delivery_date", SalesSubscribe.strToDate(jObj.getString("deliveryDate")));
        mongoRepository.insertPODocument(purchaseOrder);
        System.out.println("\n\npurchase order has been inserted");

        }catch(ParseException e){
            System.out.println("wrong date format used");
        }

        List<Document> docList = new LinkedList<>();
        JsonArray jArray = jObj.getJsonArray("lineItems");
        System.out.println(jArray);

        for(JsonValue value: jArray){
            JsonObject lijObj = (JsonObject) value;
            Document lineItem = new Document();
            lineItem.append("name", lijObj.getString("name"))
            .append("quantity", lijObj.getInt("quantity"))
            .append("unit_price", lijObj.getInt("unitPrice"))
            .append("po_id", jObj.getString("poId"));
            docList.add(lineItem);
        }
        mongoRepository.insertLIDocument(docList);
        System.out.println("\n\nline item has been inserted");


    }


}

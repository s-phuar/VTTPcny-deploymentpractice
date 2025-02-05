package VTTPcny.MongoDB.model;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import VTTPcny.MongoDB.service.MongoService;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Component
public class SalesSubscribe implements MessageListener{
    @Autowired
    private MongoService mongoService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        byte[] data = (byte[]) message.getBody();
        JsonReader reader = Json.createReader(new ByteArrayInputStream(data));
        JsonObject jsonData = reader.readObject();
        System.out.println("message received from pub channel: " + jsonData.toString());
        
        //turn json into document using service and insert document using repo
        mongoService.jsonToDocument(jsonData);
        
    }

     public static Date strToDate(String date) throws ParseException{
        Date convertedDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(date);
        // Date convertedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        // Date convertedDate = new SimpleDateFormat("EEE, MM/dd/yyyy").parse(date);    

        // Use a pattern that includes both date and time
            // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // For example: "2025-01-20 14:30:00"
        
        // If the string has milliseconds, use this pattern: "yyyy-MM-dd HH:mm:ss.SSS"
            // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


        //if I want to have nanosecond, need to use a mix of java.Time.Instant + java.sql.TimeStamp
            //java.time.Instant
                // Get the current instant with nanosecond precision
                    // Instant instant = Instant.now();
                // Get the epoch second and nanoseconds
                    // long epochSecond = instant.getEpochSecond(); // seconds since the Unix epoch
                    // int nanos = instant.getNano(); // nanoseconds part
            //convert to java.sql.Timestamp
                // Convert to Timestamp
                    // Timestamp timestamp = Timestamp.from(instant);
                // Print the Timestamp
                    // System.out.println("Current Timestamp: " + timestamp);
                    // Current Timestamp: 2025-01-20 14:30:00.123456789
                // You can also access the nanosecond part via getNanos()
                    // System.out.println("Nanoseconds part: " + timestamp.getNanos());
                    // Nanoseconds part: 123456789
                
        return convertedDate;
    }



}

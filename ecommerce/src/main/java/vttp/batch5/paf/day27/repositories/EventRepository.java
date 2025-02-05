package vttp.batch5.paf.day27.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;
import vttp.batch5.paf.day27.models.Event;
import vttp.batch5.paf.day27.models.PurchaseOrder;

@Repository
public class EventRepository {
    @Autowired @Qualifier("redis-0")
    private RedisTemplate<String, String> template;

    //pushing into redis
    // LPUSH messageC event1

    //checking redis list
    // LRANGE messageC  0 -1

    public void pushRedisMessage(Event event){
        ListOperations<String, String> listOps = template.opsForList();

        //convert event to JsonObject
        JsonObject jObj = PurchaseOrder.toJson(event.getPo());

        listOps.leftPush("eventStore1", jObj.toString());
        System.out.println("\n\npushed message to eventStore1" + jObj.toString());
    }    




}

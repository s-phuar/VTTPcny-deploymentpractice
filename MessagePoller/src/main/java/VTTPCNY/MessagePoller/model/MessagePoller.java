package VTTPCNY.MessagePoller.model;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MessagePoller {
    @Autowired @Qualifier("redis-message-list")
    private RedisTemplate<String, String> template;

    @Async
    public void start(){
        Runnable run = () -> {
            while (true) {

                System.out.println("Polling Redis list...");
                String message = template.opsForList().rightPop("eventStore1", Duration.ofSeconds(5));
                if(message != null){
                    System.out.println("message poller popped message: " + message);
                    //push to to pubsub
                    template.convertAndSend("pub", message);
                    System.out.println("message published to pub channel");
                }else{
                    System.out.println("No message received, waiting 5 seconds...");
                }

            }

        };
        //create fixed thread pool with 1 thread, executes run one task at a time
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(run);

    }
    
}

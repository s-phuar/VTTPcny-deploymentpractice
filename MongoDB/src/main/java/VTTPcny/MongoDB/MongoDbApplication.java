package VTTPcny.MongoDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//https://github.com/kenken64/vttp-paf-b-workshop25
//1. run method in InclassApplication calls the messagePoller.start()method
	//Message polling starts as soon as applciation starts
//2. MessagePoller class starts polling the redis channel 'sales'
	//looks for messages with 10 second timeout, if message is found it gets printed out in terminal
	//polling happens in the background
//3. SalesSubcribe class listens for messages that are PUBLISHED on Redis channels, this has been setup in the AppConfig file
	//when a message is published to 'sales', the onMessage method is triggered

//How to use redis list polling
	//start springboot application
	//open redis cli with ubuntu, and use the command
		//LPUSH [sales] [message]

//How to use redis pub/sub messaging
	//start springboot application
	//open redis cli with ubuntu, and use the command
		//PUBLISH [sales] [message]


@SpringBootApplication
public class MongoDbApplication {


	public static void main(String[] args) {
		SpringApplication.run(MongoDbApplication.class, args);
	}


}

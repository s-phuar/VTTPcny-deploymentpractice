package VTTPCNY.MessagePoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import VTTPCNY.MessagePoller.model.MessagePoller;


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
@EnableAsync
public class MessagePollerApplication implements CommandLineRunner {

	@Autowired
	private MessagePoller messagePoller;

	public static void main(String[] args) {
		SpringApplication.run(MessagePollerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		messagePoller.start();
	}

}

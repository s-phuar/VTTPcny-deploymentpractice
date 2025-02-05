package VTTPcny.MongoDB;


// import java.util.Objects;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
// import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import VTTPcny.MongoDB.model.SalesSubscribe;


@Configuration
public class AppConfig {

    private final Logger logger = Logger.getLogger(AppConfig.class.getName());

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.database}")
    private int redisDatabase;

    @Value("${spring.data.redis.username}")
    private String redisUsername;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    //new for PAF day 25
    @Autowired
    private SalesSubscribe salesSubscribe;

    //RedisMessageListenerContainer does not need to run indefinitely like MessagePoller in inclassApplication
    //this listens passively and automatically triggers when something is published
    //the message listener is event driven, it responds to event(message publication) and does not need to keep running

    @Bean("redis-pubsub")
    public RedisMessageListenerContainer redisMessageListenerContainer(){
        //1.
        //responsible for defining redis database connection settings like host/port/database etc.
        //sets the database index - redisDatabase has been hardcoded as 0 in application.properties
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setDatabase(redisDatabase);
        //set the username and password (authentication) if they are present
        if(!redisUsername.trim().equals("")){
            logger.info("Setting Redis username and password: ");
            config.setUsername(redisUsername);
            config.setPassword(redisPassword);
        }

        //2.
        //responsible for defining client-specific configurations, and how Jedis behaves when connecting to Redis
        //this one-liner uses default configuration
        JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

        //3.
        //creates and manages connection to the Redis server
        //create factory to connect to Redis, with the configuration provided earlier
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        //4.
        //create listener container, this listens to Redis channels for incoming messages
        //salesSubscribe object handles the actual message processing
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(jedisFac);
        redisMessageListenerContainer.addMessageListener(salesSubscribe, ChannelTopic.of("pub"));
        return redisMessageListenerContainer;

    }



    @Bean("redis-message-list")
    public RedisTemplate<String, String> createRedisMessageTemplate(){
        //1.
        //responsible for defining redis database connection settings like host/port/database etc.
        //sets the database index - redisDatabase has been hardcoded as 0 in application.properties
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setDatabase(redisDatabase);
        //set the username and password (authentication) if they are present
        if(!redisUsername.trim().equals("")){
            logger.info("Setting Redis username and password: ");
            config.setUsername(redisUsername);
            config.setPassword(redisPassword);
        }

        //2.
        //responsible for defining client-specific configurations, and how Jedis behaves when connecting to Redis
        //this one-liner uses default configuration
        JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

        //3.
        //creates and manages connection to the Redis server
        //create factory to connect to Redis, with the configuration provided earlier
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        //4.
        //create redis template, we will handle only string data
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisFac);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }


    //a method to create redis template, named redis-object, that is used for interacting with Redis db when the values are object typed
    @Bean("redis-object")
    public RedisTemplate<String, Object> createRedisTemplateObject(){
        //create basic redis database connection paramaters like host/port/database etc.
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        //sets the database index - redisDatabase has been hardcoded as 0 in application.properties
        config.setDatabase(redisDatabase);
        //set the username and password if they are set
        if(!redisUsername.trim().equals("")){
            logger.info("Setting Redis username and password: ");
            config.setUsername(redisUsername);
            config.setPassword(redisPassword);
        }

        //create a connection to the database
        JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

        //create factory to connect to Redis, with the configuration provided earlier
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        //create the Redis template
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisFac);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());        
        // redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        // redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }


    
    //a method to create redis template, named redis-0, assuming all values are strings
    @Bean("redis-0")
    public RedisTemplate<String, String> createRedisTemplate(){
        //create database configuration
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        //sets the database - equivalent to select 0 in redis-cli
        config.setDatabase(redisDatabase);
        //set the username and password if they are set
        if(!redisUsername.trim().equals("")){
            logger.info("Setting Redit username and password: ");
            config.setUsername(redisUsername);
            config.setPassword(redisPassword);
        }

        //create a connection to the database
        JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

        //create factory to connect to Redis
        JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
        jedisFac.afterPropertiesSet();

        //create the Redis template
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisFac);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer()); //assume value are all strings
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer()); //assume value are all strings

        return redisTemplate;
    }



}
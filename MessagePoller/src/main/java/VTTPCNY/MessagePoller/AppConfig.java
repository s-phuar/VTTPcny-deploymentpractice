package VTTPCNY.MessagePoller;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

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


    //MessagePoller will run indefinitely
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
        // redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        // redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return redisTemplate;
    }


}

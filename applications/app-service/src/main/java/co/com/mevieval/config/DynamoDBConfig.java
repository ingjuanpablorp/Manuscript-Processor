package co.com.mevieval.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;

@Configuration
public class DynamoDBConfig {
    
    @Bean
    @Profile("local")
    public DynamoDbAsyncClient dynamoDbAsyncClientLocal(@Value("${aws.dynamodb.endpoint}") String endpoint){        
        return DynamoDbAsyncClient.builder()
        .endpointOverride(URI.create(endpoint))
        .build();
    }

    @Bean
    @Profile("!local")
    public DynamoDbAsyncClient dynamoDbAsyncClientProd(){
        return DynamoDbAsyncClient.builder()
        .credentialsProvider(DefaultCredentialsProvider.create())
        .region(Region.US_EAST_1)
        .build();
    }
}

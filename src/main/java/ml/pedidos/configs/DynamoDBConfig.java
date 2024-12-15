package ml.pedidos.configs;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import ml.pedidos.configs.data.UsuarioServicoDynamodb;
import ml.pedidos.util.constants.DynamodbConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {

    @Autowired
    private DynamodbConstants dynamodbConstants;

    @Autowired
    private UsuarioServicoDynamodb usuarioServicoDynamodb;
    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB());
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(dynamodbConstants.getAmazonDynamoDBEndpoint(), dynamodbConstants.getAmazonDynamoDBRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(usuarioServicoDynamodb.getAccessKey(), usuarioServicoDynamodb.getSecretKey())))
                .build();
    }
}
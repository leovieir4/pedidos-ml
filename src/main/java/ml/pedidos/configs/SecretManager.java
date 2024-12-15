package ml.pedidos.configs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ml.pedidos.configs.data.ChaveAssinaturaToken;
import ml.pedidos.configs.data.UsuarioServicoDynamodb;
import ml.pedidos.util.constants.DynamodbConstants;
import ml.pedidos.util.constants.ProfileConstants;
import ml.pedidos.util.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Configuration
public class SecretManager {


    @Autowired
    private SecurityConstants securityConstants;

    @Autowired
    private DynamodbConstants dynamodbConstants;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Region region = Region.of(Region.US_EAST_2.toString());

    private SecretsManagerClient client = SecretsManagerClient.builder()
            .region(region)
            .credentialsProvider(DefaultCredentialsProvider.create())
            .build();

    @Bean
    @Profile(ProfileConstants.NOT_LOCAL)
    public ChaveAssinaturaToken getChaveAssinaturaToken() throws JsonProcessingException {

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(securityConstants.getSecretArn())
                .build();

        GetSecretValueResponse getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        String secret = getSecretValueResult.secretString();

        return this.objectMapper.readValue(secret, ChaveAssinaturaToken.class);

    }

    @Bean
    @Profile(ProfileConstants.LOCAL)
    public ChaveAssinaturaToken getChaveAssinaturaTokenLocal() throws JsonProcessingException {

        ChaveAssinaturaToken chaveAssinaturaToken = new ChaveAssinaturaToken();
        chaveAssinaturaToken.setSecret(securityConstants.getLocalSecret());

        return chaveAssinaturaToken;

    }

    @Bean
    @Profile(ProfileConstants.NOT_LOCAL)
    public UsuarioServicoDynamodb getUsuarioServicoDynamoDB() throws JsonProcessingException {
        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(dynamodbConstants.getSecretArn())
                .build();

        GetSecretValueResponse getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        String secret = getSecretValueResult.secretString();

        return this.objectMapper.readValue(secret, UsuarioServicoDynamodb.class);
    }

    @Bean
    @Profile(ProfileConstants.LOCAL)
    public UsuarioServicoDynamodb getUsuarioServicoDynamoDBLocal() throws JsonProcessingException {

        UsuarioServicoDynamodb usuarioServicoDynamodb = new UsuarioServicoDynamodb();
        usuarioServicoDynamodb.setSecretKey(dynamodbConstants.getAmazonAWSSecretKeyLocal());
        usuarioServicoDynamodb.setAccessKey(dynamodbConstants.getAmazonAWSAccessKeyLocal());

        return usuarioServicoDynamodb;
    }

}

package ml.pedidos.util.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DynamodbConstants {

    @Value("${aws.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${aws.dynamodb.region}")
    private String amazonDynamoDBRegion;

    @Value("${aws.dynamodb.accessKey-local}")
    private String amazonAWSAccessKeyLocal;

    @Value("${aws.dynamodb.secretKey-local}")
    private String amazonAWSSecretKeyLocal;

    @Value("${aws.dynamodb.secret-arn}")
    private String secretArn;

}

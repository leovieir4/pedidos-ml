package ml.pedidos.util.constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SecurityConstants {

    @Value("${security.secret}")
    private String localSecret;

    @Value("${security.expiration-time}")
    private long expiration_time;

    @Value("${security.token-prefix}")
    private String tokenPrefix;

    @Value("${security.header}")
    private String header;

    @Value("${security.rule-user}")
    private String ruleUser;

    @Value("${security.secret-arn}")
    private String secretArn;

}
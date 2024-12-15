package ml.pedidos.util.constants;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class GatewayConstants {

    @Value("${gateway.via-cep-endpoint}")
    private String indpoint;

}

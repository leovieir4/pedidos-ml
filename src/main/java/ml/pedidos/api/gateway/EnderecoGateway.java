package ml.pedidos.api.gateway;

import ml.pedidos.api.response.EnderecoResponseDTO;
import ml.pedidos.util.constants.GatewayConstants;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EnderecoGateway {
    private final GatewayConstants gatewayConstants;
    private final RestTemplate restTemplate;

    public EnderecoGateway(RestTemplateBuilder restTemplateBuilder,
                           GatewayConstants gatewayConstants) {
        this.restTemplate = restTemplateBuilder.build();
        this.gatewayConstants = gatewayConstants;
    }

    public EnderecoResponseDTO buscarEnderecoPorCEP(String cep) {
        return this.restTemplate.getForObject(String.format(gatewayConstants.getIndpoint(),
                        cep.replace("-", "")),
                EnderecoResponseDTO.class);
    }
}

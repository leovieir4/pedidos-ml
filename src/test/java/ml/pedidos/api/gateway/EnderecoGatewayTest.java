package ml.pedidos.api.gateway;

import ml.pedidos.api.gateway.EnderecoGateway;
import ml.pedidos.api.response.EnderecoResponseDTO;
import ml.pedidos.util.constants.GatewayConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnderecoGatewayTest {

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private GatewayConstants gatewayConstants;

    @Test
    public void testBuscarEnderecoPorCEP() {

        String cep = "01001-000";
        String cepSemHifen = "01001000";
        String endpoint = "https://viacep.com.br/ws/%s/json/";
        EnderecoResponseDTO enderecoResponseDTO = EnderecoResponseDTO.builder().build();

        when(gatewayConstants.getIndpoint()).thenReturn(endpoint);
        // Corrigindo o problema do RestTemplate nulo:
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        when(restTemplate.getForObject(String.format(endpoint, cepSemHifen), EnderecoResponseDTO.class))
                .thenReturn(enderecoResponseDTO);

        EnderecoGateway enderecoGateway = new EnderecoGateway(restTemplateBuilder, gatewayConstants);

        EnderecoResponseDTO resultado = enderecoGateway.buscarEnderecoPorCEP(cep);

        assertEquals(enderecoResponseDTO, resultado);
        verify(restTemplate, times(1)).getForObject(String.format(endpoint, cepSemHifen), EnderecoResponseDTO.class);
    }
}
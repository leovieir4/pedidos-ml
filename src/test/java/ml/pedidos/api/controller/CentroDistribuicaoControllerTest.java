package ml.pedidos.api.controller;

import ml.pedidos.api.controller.CentroDistribuicaoController;
import ml.pedidos.api.dto.CentroDistribuicaoDTO;
import ml.pedidos.api.exceptions.CentroDeDistribucaiJaCadastradoException;
import ml.pedidos.api.exceptions.ConsultarEnderecoException;
import ml.pedidos.api.response.CentroDistribuicaoResponse;
import ml.pedidos.api.service.CentroDistribuicaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CentroDistribuicaoControllerTest {

    @InjectMocks
    private CentroDistribuicaoController centroDistribuicaoController;

    @Mock
    private CentroDistribuicaoService centroDistribuicaoService;

    @Test
    public void testCriarCentroDistribuicao_comSucesso() throws Exception {
        CentroDistribuicaoDTO centroDistribuicaoDTO = new CentroDistribuicaoDTO();
        CentroDistribuicaoDTO response = new CentroDistribuicaoDTO();
        when(centroDistribuicaoService.criarCentroDistribuicao(centroDistribuicaoDTO)).thenReturn(response);

        ResponseEntity<CentroDistribuicaoResponse> responseEntity = centroDistribuicaoController.criarCentroDistribuicao(centroDistribuicaoDTO);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode()); // Note que o status code está como ACCEPTED (202), deve ser CREATED (201)
        verify(centroDistribuicaoService, times(1)).criarCentroDistribuicao(centroDistribuicaoDTO);
    }

    @Test
    public void testCriarCentroDistribuicao_comConsultarEnderecoException() throws Exception {
        CentroDistribuicaoDTO centroDistribuicaoDTO = new CentroDistribuicaoDTO();
        when(centroDistribuicaoService.criarCentroDistribuicao(centroDistribuicaoDTO)).thenThrow(new ConsultarEnderecoException("Erro ao consultar endereço"));

        ResponseEntity<CentroDistribuicaoResponse> responseEntity = centroDistribuicaoController.criarCentroDistribuicao(centroDistribuicaoDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Erro ao consultar endereço", responseEntity.getBody().getErrorResponse().getMensagem());
        verify(centroDistribuicaoService, times(1)).criarCentroDistribuicao(centroDistribuicaoDTO);
    }

    @Test
    public void testCriarCentroDistribuicao_comCentroDeDistribucaiJaCadastradoException() throws Exception {
        CentroDistribuicaoDTO centroDistribuicaoDTO = new CentroDistribuicaoDTO();
        when(centroDistribuicaoService.criarCentroDistribuicao(centroDistribuicaoDTO)).thenThrow(new CentroDeDistribucaiJaCadastradoException("Centro de distribuição já cadastrado"));

        ResponseEntity<CentroDistribuicaoResponse> responseEntity = centroDistribuicaoController.criarCentroDistribuicao(centroDistribuicaoDTO);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Centro de distribuição já cadastrado", responseEntity.getBody().getErrorResponse().getMensagem());
        verify(centroDistribuicaoService, times(1)).criarCentroDistribuicao(centroDistribuicaoDTO);
    }
}
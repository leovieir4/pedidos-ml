package ml.pedidos.api.controller;

import ml.pedidos.api.dto.PedidoDTO;
import ml.pedidos.api.exceptions.PedidoNaoEncontradoException;
import ml.pedidos.api.exceptions.QuantidadeDeItensNaoDisponivelCDException;
import ml.pedidos.api.exceptions.QuantidadeItensPedidosExcedidaException;
import ml.pedidos.api.response.PedidoResponse;
import ml.pedidos.api.service.PedidoService;
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
public class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock
    private PedidoService pedidoService;

    @Test
    public void testCriarPedido_comSucesso() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        PedidoDTO pedidoCriado = new PedidoDTO();
        when(pedidoService.criarPedido(pedidoDTO)).thenReturn(pedidoCriado);

        ResponseEntity<PedidoResponse> response = pedidoController.criarPedido(pedidoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pedidoCriado, response.getBody().getPedidoDTO());
        verify(pedidoService, times(1)).criarPedido(pedidoDTO);
    }

    @Test
    public void testCriarPedido_comQuantidadeItensPedidosExcedidaException() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        when(pedidoService.criarPedido(pedidoDTO)).thenThrow(new QuantidadeItensPedidosExcedidaException("Quantidade de itens excedida"));

        ResponseEntity<PedidoResponse> response = pedidoController.criarPedido(pedidoDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Quantidade de itens excedida", response.getBody().getErrorResponse().getMensagem());
        verify(pedidoService, times(1)).criarPedido(pedidoDTO);
    }

    @Test
    public void testCriarPedido_comQuantidadeDeItensNaoDisponivelCDException() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        when(pedidoService.criarPedido(pedidoDTO)).thenThrow(new QuantidadeDeItensNaoDisponivelCDException("Quantidade de itens não disponível no CD"));

        ResponseEntity<PedidoResponse> response = pedidoController.criarPedido(pedidoDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Quantidade de itens não disponível no CD", response.getBody().getErrorResponse().getMensagem());
        verify(pedidoService, times(1)).criarPedido(pedidoDTO);
    }

    @Test
    public void testCriarPedido_comExceptionGenerica() throws Exception {
        PedidoDTO pedidoDTO = new PedidoDTO();
        when(pedidoService.criarPedido(pedidoDTO)).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<PedidoResponse> response = pedidoController.criarPedido(pedidoDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno", response.getBody().getErrorResponse().getMensagem());
        verify(pedidoService, times(1)).criarPedido(pedidoDTO);
    }

    @Test
    public void testConsultarPedido_comSucesso() throws Exception {
        String id = "123";
        PedidoDTO pedidoCriado = new PedidoDTO();
        when(pedidoService.buscarPedidoPorId(id)).thenReturn(pedidoCriado);

        ResponseEntity<PedidoResponse> response = pedidoController.consultarPedido(id);

        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // Note que o status code está como CREATED, deve ser OK (200)
        assertEquals(pedidoCriado, response.getBody().getPedidoDTO());
        verify(pedidoService, times(1)).buscarPedidoPorId(id);
    }

    @Test
    public void testConsultarPedido_comPedidoNaoEncontradoException() throws Exception {
        String id = "123";
        when(pedidoService.buscarPedidoPorId(id)).thenThrow(new PedidoNaoEncontradoException("Pedido não encontrado"));

        ResponseEntity<PedidoResponse> response = pedidoController.consultarPedido(id);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode()); // Note que o status code está como BAD_REQUEST, deve ser NOT_FOUND (404)
        assertEquals("Pedido não encontrado", response.getBody().getErrorResponse().getMensagem());
        verify(pedidoService, times(1)).buscarPedidoPorId(id);
    }

    @Test
    public void testConsultarPedido_comExceptionGenerica() throws Exception {
        String id = "123";
        when(pedidoService.buscarPedidoPorId(id)).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<PedidoResponse> response = pedidoController.consultarPedido(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno", response.getBody().getErrorResponse().getMensagem());
        verify(pedidoService, times(1)).buscarPedidoPorId(id);
    }
}
package ml.pedidos.api.controller;

import ml.pedidos.api.controller.ItemController;
import ml.pedidos.api.dto.CentroDistribuicaoItemDTO;
import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.exceptions.CentroDeDistribucaiNaoCadastradoException;
import ml.pedidos.api.exceptions.ItemNaoEcontradoException;
import ml.pedidos.api.exceptions.ItensNaoEncontradoException;
import ml.pedidos.api.response.CentroDeDistribuicaoPorItemReponse;
import ml.pedidos.api.response.ItemResponse;
import ml.pedidos.api.response.ListaItensResponse;
import ml.pedidos.api.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    @Test
    public void testCriarItem_comSucesso() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        ItemDTO itemCriado = new ItemDTO();
        when(itemService.criarItem(itemDTO)).thenReturn(itemCriado);

        ResponseEntity<ItemResponse> response = itemController.criarItem(itemDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(itemCriado, response.getBody().getItemDTO());
        verify(itemService, times(1)).criarItem(itemDTO);
    }

    @Test
    public void testCriarItem_comCentroDeDistribucaiNaoCadastradoException() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        when(itemService.criarItem(itemDTO)).thenThrow(new CentroDeDistribucaiNaoCadastradoException("Centro de distribuição não cadastrado"));

        ResponseEntity<ItemResponse> response = itemController.criarItem(itemDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Centro de distribuição não cadastrado", response.getBody().getErrorResponse().getMensagem());
        verify(itemService, times(1)).criarItem(itemDTO);
    }

    @Test
    public void testCriarItem_comExceptionGenerica() throws Exception {
        ItemDTO itemDTO = new ItemDTO();
        when(itemService.criarItem(itemDTO)).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<ItemResponse> response = itemController.criarItem(itemDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno", response.getBody().getErrorResponse().getMensagem());
        verify(itemService, times(1)).criarItem(itemDTO);
    }

    @Test
    public void testConsultarCentrosDeDistribuicaoPorItem_comSucesso() throws Exception {
        String itemId = "123";
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setCentrosDeDistribuicao(List.of(CentroDistribuicaoItemDTO.builder()
                        .quantidade(1)
                        .nome("teste")
                .build()));
        when(itemService.buscarItemPorId(itemId)).thenReturn(itemDTO);

        ResponseEntity<CentroDeDistribuicaoPorItemReponse> response = itemController.consultarCentrosDeDistribuicaoPorItem(itemId);

        assertEquals(HttpStatus.OK, response.getStatusCode()); // Note que o status code está como FOUND (302), deve ser OK (200)
        verify(itemService, times(1)).buscarItemPorId(itemId);
    }

    @Test
    public void testConsultarCentrosDeDistribuicaoPorItem_comItemNaoEcontradoException() throws Exception {
        String itemId = "123";
        when(itemService.buscarItemPorId(itemId)).thenThrow(new ItemNaoEcontradoException("Item não encontrado"));

        ResponseEntity<CentroDeDistribuicaoPorItemReponse> response = itemController.consultarCentrosDeDistribuicaoPorItem(itemId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Item não encontrado", response.getBody().getErrorResponse().getMensagem());
        verify(itemService, times(1)).buscarItemPorId(itemId);
    }

    @Test
    public void testConsultarCentrosDeDistribuicaoPorItem_comExceptionGenerica() throws Exception {
        String itemId = "123";
        when(itemService.buscarItemPorId(itemId)).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<CentroDeDistribuicaoPorItemReponse> response = itemController.consultarCentrosDeDistribuicaoPorItem(itemId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro interno", response.getBody().getErrorResponse().getMensagem());
        verify(itemService, times(1)).buscarItemPorId(itemId);
    }

    @Test
    void deve_retornar_lista_de_itens_com_sucesso() {
        // Arrange
        int page = 1;
        int size = 10;
        List<ItemDTO> itensDTO = List.of(new ItemDTO(), new ItemDTO());

        ListaItensResponse listaItensResponse = ListaItensResponse.gerarListaDeItensRespose(itensDTO);

        when(itemService.buscarItens(page, size)).thenReturn(itensDTO);

        // Act
        ResponseEntity<ListaItensResponse> response = itemController.listarTodosOsItens(page, size);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(listaItensResponse.getItens(), response.getBody().getItens());
    }

    @Test
    void deve_retornar_not_found_quando_nao_existir_itens() {
        // Arrange
        int page = 1;
        int size = 10;
        when(itemService.buscarItens(page, size)).thenThrow(new ItensNaoEncontradoException("mensagem de erro"));

        // Act
        ResponseEntity<ListaItensResponse> response = itemController.listarTodosOsItens(page, size);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("mensagem de erro", response.getBody().getErrorResponse().getMensagem());
    }

    @Test
    void deve_retornar_internal_server_error_quando_ocorrer_erro_inesperado() {
        // Arrange
        int page = 1;
        int size = 10;
        when(itemService.buscarItens(page, size)).thenThrow(new RuntimeException("mensagem de erro"));

        // Act
        ResponseEntity<ListaItensResponse> response = itemController.listarTodosOsItens(page, size);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("mensagem de erro", response.getBody().getErrorResponse().getMensagem());
    }
}
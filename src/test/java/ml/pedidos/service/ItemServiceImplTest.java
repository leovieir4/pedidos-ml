package ml.pedidos.service;

import ml.pedidos.api.dto.CentroDistribuicaoItemDTO;
import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.exceptions.CentroDeDistribucaiNaoCadastradoException;
import ml.pedidos.api.exceptions.ItemNaoEcontradoException;
import ml.pedidos.api.exceptions.ItensNaoEncontradoException;
import ml.pedidos.api.mapper.ItemMapper;
import ml.pedidos.api.service.impl.ItemServiceImpl;
import ml.pedidos.domain.CentroDistribuicao;
import ml.pedidos.domain.Item;
import ml.pedidos.repository.CentroDistribuicaoRepository;
import ml.pedidos.repository.ItemRepository;
import ml.pedidos.util.MensagensDeErros;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    private ItemServiceImpl itemService;


    @Test
    void deve_retornar_lista_de_itens_quando_existir_itens() {
        // Arrange
        int page = 1;
        int size = 10;
        List<Item> itens = List.of(new Item(), new Item());
        List<ItemDTO> itensDTO = List.of(new ItemDTO(), new ItemDTO());
        when(itemRepository.buscarTodosPaginado(page, size)).thenReturn(itens);
        when(itemMapper.toDto(itens.get(0))).thenReturn(itensDTO.get(0));
        when(itemMapper.toDto(itens.get(1))).thenReturn(itensDTO.get(1));

        // Act
        List<ItemDTO> resultado = itemService.buscarItens(page, size);

        // Assert
        assertEquals(itensDTO, resultado);
    }

    @Test
    void deve_lancar_excecao_quando_nao_existir_itens() {
        // Arrange
        int page = 1;
        int size = 10;
        when(itemRepository.buscarTodosPaginado(page, size)).thenReturn(Collections.emptyList());

        // Act & Assert
        ItensNaoEncontradoException exception = assertThrows(ItensNaoEncontradoException.class,
                () -> itemService.buscarItens(page, size));
        assertEquals(MensagensDeErros.MENSAGEM_ITENS_NAO_ENCONTRADO, exception.getMessage());
    }

    @Test
    void criarItem_comSucesso() {
        // Arrange
        ItemDTO itemDTO = criarItemDTOValido();
        Item item = criarItemValido();
        when(itemMapper.toEntity(itemDTO)).thenReturn(item);
        when(itemRepository.salvar(item)).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(itemDTO);
        when(centroDistribuicaoRepository.buscarPorNomes(any()))
                .thenReturn(List.of(CentroDistribuicao.builder()
                                .nome("NOME")
                        .build()));

        // Act
        ItemDTO resultado = itemService.criarItem(itemDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(itemDTO, resultado);
        verify(itemMapper).toEntity(itemDTO);
        verify(itemRepository).salvar(item);
        verify(itemMapper).toDto(item);
    }

    @Test
    void buscarItensPorId_comSucesso() {
        // Arrange
        List<String> ids = Arrays.asList("1", "2");
        List<Item> itens = Arrays.asList(criarItemValido(), criarItemValido());
        when(itemRepository.buscarPorIds(ids)).thenReturn(itens);
        when(itemMapper.toDto(any(Item.class))).thenReturn(criarItemDTOValido());

        // Act
        List<ItemDTO> resultado = itemService.buscarItensPorId(ids);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(itemRepository).buscarPorIds(ids);
        verify(itemMapper, times(2)).toDto(any(Item.class));
    }

    @Test
    void criarItem_comCentroDeDistribuicaoNaoCadastrado_deveLancarExcecao() {
        // Arrange
        ItemDTO itemDTO = criarItemDTOValido();
        OngoingStubbing<List<CentroDistribuicao>> listOngoingStubbing = when(centroDistribuicaoRepository.buscarPorNomes(any())).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(CentroDeDistribucaiNaoCadastradoException.class, () -> itemService.criarItem(itemDTO));
        verify(itemRepository, never()).salvar(any());
    }

    @Test
    void buscarItensPorId_comItensNaoEncontrados_deveLancarExcecao() {
        // Arrange
        List<String> ids = Arrays.asList("1", "2");
        when(itemRepository.buscarPorIds(ids)).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(ItemNaoEcontradoException.class, () -> itemService.buscarItensPorId(ids));
    }

    @Test
    void buscarItemPorId_comSucesso() {
        // Arrange
        String id = "1";
        Item item = criarItemValido();
        ItemDTO itemDTO = criarItemDTOValido();
        when(itemRepository.buscarItemPorId(id)).thenReturn(item);
        when(itemMapper.toDto(item)).thenReturn(itemDTO);

        // Act
        ItemDTO resultado = itemService.buscarItemPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(itemDTO, resultado);
        verify(itemRepository).buscarItemPorId(id);
        verify(itemMapper).toDto(item);
    }

    @Test
    void buscarItemPorId_comItemNaoEncontrado_deveLancarExcecao() {
        // Arrange
        String id = "1";
        when(itemRepository.buscarItemPorId(id)).thenReturn(null);

        // Act & Assert
        assertThrows(ItemNaoEcontradoException.class, () -> itemService.buscarItemPorId(id));
    }

    // Métodos auxiliares para criação de objetos de teste
    private ItemDTO criarItemDTOValido() {
        return ItemDTO.builder()
                .centrosDeDistribuicao(List.of(CentroDistribuicaoItemDTO.builder()
                                .nome("NOME")
                        .build()))
                .preco(10.0)
                .nome("Item 1")
                .id("1")
                .build();
    }

    private Item criarItemValido() {
        return Item.builder()
                .preco(10.0)
                .nome("Item 1")
                .id("1")
                .build();
    }
}
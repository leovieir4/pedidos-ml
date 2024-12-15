package ml.pedidos.service;

import ml.pedidos.api.dto.CentroDistribuicaoItemDTO;
import ml.pedidos.api.dto.ItemPedidoDTO;
import ml.pedidos.api.dto.PedidoDTO;
import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.exceptions.ItemDoPedidoNaoCadastradoException;
import ml.pedidos.api.exceptions.PedidoNaoEncontradoException;
import ml.pedidos.api.exceptions.QuantidadeDeItensNaoDisponivelCDException;
import ml.pedidos.api.exceptions.QuantidadeItensPedidosExcedidaException;
import ml.pedidos.api.mapper.PedidoMapper;
import ml.pedidos.api.service.ItemService;
import ml.pedidos.api.service.impl.PedidoServiceImpl;
import ml.pedidos.domain.ItemPedido;
import ml.pedidos.domain.Pedido;
import ml.pedidos.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private PedidoMapper pedidoMapper;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    void criarPedido_comSucesso() throws QuantidadeItensPedidosExcedidaException {
        // Arrange
        PedidoDTO pedidoDTO = criarPedidoDTOValido();
        Pedido pedido = criarPedidoValido();
        when(pedidoMapper.toEntity(pedidoDTO)).thenReturn(pedido);
        when(pedidoRepository.salvar(pedido)).thenReturn(pedido);
        when(pedidoMapper.toDto(any(), anyList())).thenReturn(pedidoDTO);
        when(itemService.buscarItensPorId(any())).thenReturn(criarListaItemDTOValido());

        // Act
        PedidoDTO resultado = pedidoService.criarPedido(pedidoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedidoDTO, resultado);
    }

    @Test
    void criarPedido_quantidade_itensIndisponivelCDS() throws QuantidadeItensPedidosExcedidaException {
        // Arrange
        PedidoDTO pedidoDTO = criarPedidoDTOValido();
        Pedido pedido = criarPedidoValido();
        when(pedidoMapper.toEntity(pedidoDTO)).thenReturn(pedido);
        when(pedidoMapper.toDto(any(), anyList())).thenReturn(pedidoDTO);
        when(itemService.buscarItensPorId(any())).thenReturn(criarListaItemDTOValido());

        // Act
        pedidoDTO.getItens().stream().forEach(itemPedidoDTO -> itemPedidoDTO.getCentrosDistribuicaoDTO()
                .forEach(centroDistribuicaoItemDTO -> centroDistribuicaoItemDTO.setQuantidade(0)));

        assertThrows(QuantidadeDeItensNaoDisponivelCDException.class, () -> pedidoService.criarPedido(pedidoDTO));
    }

    @Test
    void criarPedido_comQuantidadeItensExcedida_deveLancarExcecao() {
        // Arrange
        PedidoDTO pedidoDTO = criarPedidoDTOComQuantidadeItensExcedida();

        // Act & Assert
        assertThrows(QuantidadeItensPedidosExcedidaException.class, () -> pedidoService.criarPedido(pedidoDTO));
        verify(pedidoRepository, never()).salvar(any());
    }

    @Test
    void criarPedido_comItensNaoCadastrados_deveLancarExcecao() {
        // Arrange
        PedidoDTO pedidoDTO = criarPedidoDTOValido();
        when(itemService.buscarItensPorId(any())).thenReturn(new ArrayList<>()); // Retorna lista vazia

        // Act & Assert
        assertThrows(ItemDoPedidoNaoCadastradoException.class, () -> pedidoService.criarPedido(pedidoDTO));
        verify(pedidoRepository, never()).salvar(any());
    }

    @Test
    void buscarPedidoPorId_comSucesso() throws PedidoNaoEncontradoException {
        // Arrange
        String id = "123";
        Pedido pedido = criarPedidoValido();
        List<ItemDTO> itensDTO = criarListaItemDTOValido();
        PedidoDTO pedidoDTO = criarPedidoDTOValido();

        when(pedidoRepository.buscarPedidoPorId(id)).thenReturn(pedido);
        when(itemService.buscarItensPorId(any())).thenReturn(itensDTO);
        when(pedidoMapper.toDto(pedido, itensDTO)).thenReturn(pedidoDTO);

        // Act
        PedidoDTO resultado = pedidoService.buscarPedidoPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(pedidoDTO, resultado);

        verify(pedidoRepository).buscarPedidoPorId(id);
        verify(itemService).buscarItensPorId(any());
        verify(pedidoMapper).toDto(pedido, itensDTO);
    }

    @Test
    void buscarPedidoPorId_comPedidoNaoEncontrado_deveLancarExcecao() {
        // Arrange
        String id = "123";
        when(pedidoRepository.buscarPedidoPorId(id)).thenReturn(null);

        // Act & Assert
        assertThrows(PedidoNaoEncontradoException.class, () -> pedidoService.buscarPedidoPorId(id));
    }

    // Métodos auxiliares para criação de objetos de teste
    private PedidoDTO criarPedidoDTOValido() {
        return PedidoDTO.builder()
                .itens(Arrays.asList(
                        new ItemPedidoDTO("1", "nome", 1, 10.0,
                                List.of(CentroDistribuicaoItemDTO.builder().quantidade(1).build())),
                        new ItemPedidoDTO("2", "nome",2, 20.0,
                                List.of(CentroDistribuicaoItemDTO.builder().quantidade(10).build()))
                ))
                .build();
    }

    private Pedido criarPedidoValido() {
        return Pedido.builder()
                .itens(Arrays.asList(
                        new ItemPedido("1", 1),
                        new ItemPedido("2", 2)
                ))
                .build();
    }

    private PedidoDTO criarPedidoDTOComQuantidadeItensExcedida() {
        return PedidoDTO.builder()
                .itens(Arrays.asList(
                        new ItemPedidoDTO("1", "nome", 99, 10.0,
                                List.of(CentroDistribuicaoItemDTO.builder().build())),
                        new ItemPedidoDTO("2", "nome",2, 20.0,
                                List.of(CentroDistribuicaoItemDTO.builder().build()))
                ))
                .build();
    }

    private List<ItemDTO> criarListaItemDTOValido() {
        return Arrays.asList(
                ItemDTO.builder()
                        .nome("item 1")
                        .id("1")
                        .preco(10.0)
                        .build(),
                ItemDTO.builder()
                        .nome("item 2")
                        .id("2")
                        .preco(20.0)
                        .build()
        );
    }
}
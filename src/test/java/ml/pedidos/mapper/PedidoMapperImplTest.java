package ml.pedidos.mapper;

import ml.pedidos.api.dto.*;
import ml.pedidos.api.mapper.impl.PedidoMapperImpl;
import ml.pedidos.domain.ItemPedido;
import ml.pedidos.domain.Pedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PedidoMapperImplTest {

    @InjectMocks
    private PedidoMapperImpl pedidoMapper;

    @Test
    void toEntity_deveConverterPedidoDTOParaPedido() {
        // Arrange
        PedidoDTO pedidoDTO = criarPedidoDTO();

        // Act
        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);

        // Assert
        assertNotNull(pedido);
        assertEquals(pedidoDTO.getItens().size(), pedido.getItens().size());
        assertItensPedidos(pedidoDTO.getItens(), pedido.getItens());
    }

    @Test
    void toDto_deveConverterPedidoParaPedidoDTO() {
        // Arrange
        Pedido pedido = criarPedido();

        // Act
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido, List.of(ItemDTO.builder()
                        .preco(10.0)
                        .centrosDeDistribuicao(List.of(CentroDistribuicaoItemDTO.builder()
                                        .nome("NOME")
                                .build()))
                        .id("1")
                .build()));


        // Assert
        assertNotNull(pedidoDTO);
        assertEquals(pedido.getId(), pedidoDTO.getId());
        assertEquals(pedido.getItens().size(), pedidoDTO.getItens().size());
        assertItensPedidos(pedidoDTO.getItens(), pedido.getItens());
    }

    // Método auxiliar para verificar a igualdade entre listas de ItemPedido e ItemPedidoDTO
    private void assertItensPedidos(List<ItemPedidoDTO> itemPedidoDTOs, List<ItemPedido> itemPedidos) {
        for (int i = 0; i < itemPedidoDTOs.size(); i++) {
            ItemPedidoDTO dto = itemPedidoDTOs.get(i);
            ItemPedido entity = itemPedidos.get(i);
            assertEquals(dto.getId(), entity.getId());
            assertEquals(dto.getQuantidade(), entity.getQuantidade());
        }
    }

    // Método auxiliar para criar um PedidoDTO
    private PedidoDTO criarPedidoDTO() {
        return PedidoDTO.builder()
                .id("1")
                .itens(Arrays.asList(
                        new ItemPedidoDTO("1", "nome", 1, 10.0, List.of(CentroDistribuicaoItemDTO.builder().build())),
                        new ItemPedidoDTO("2", "nome", 2,20,  List.of(CentroDistribuicaoItemDTO.builder().build()))
                ))
                .build();
    }

    // Método auxiliar para criar um Pedido
    private Pedido criarPedido() {
        return Pedido.builder()
                .id("1")
                .itens(Arrays.asList(
                        ItemPedido.builder().id("1").quantidade(1).build(),
                        ItemPedido.builder().id("2").quantidade(2).build()
                ))
                .build();
    }
}
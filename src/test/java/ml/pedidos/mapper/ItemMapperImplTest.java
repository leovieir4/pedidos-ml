package ml.pedidos.mapper;

import ml.pedidos.api.dto.CentroDistribuicaoItemDTO;
import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.mapper.impl.ItemMapperImpl;
import ml.pedidos.domain.CentroDistribuicaoItem;
import ml.pedidos.domain.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ItemMapperImplTest {

    private final ItemMapperImpl itemMapper = new ItemMapperImpl();

    @Test
    void toEntity_deveConverterItemDTOParaItem() {
        // Arrange
        ItemDTO itemDTO = criarItemDTO();

        // Act
        Item item = itemMapper.toEntity(itemDTO);
        item.setCentrosDeDistribuicao(List.of(CentroDistribuicaoItem.builder()
                        .nome("NOME")
                .build()));

        // Assert
        assertNotNull(item);
        assertEquals(itemDTO.getId(), item.getId());
        assertEquals(itemDTO.getNome(), item.getNome());
        assertEquals(itemDTO.getPreco(), item.getPreco());
    }

    @Test
    void toDto_deveConverterItemParaItemDTO() {
        // Arrange
        Item item = criarItem();

        // Act
        ItemDTO itemDTO = itemMapper.toDto(item);
        item.setCentrosDeDistribuicao(List.of(CentroDistribuicaoItem.builder().build()));

        // Assert
        assertNotNull(itemDTO);
        assertEquals(item.getId(), itemDTO.getId());
        assertEquals(item.getNome(), itemDTO.getNome());
        assertEquals(item.getPreco(), itemDTO.getPreco());
    }

    // Método auxiliar para criar um ItemDTO
    private ItemDTO criarItemDTO() {
        return ItemDTO.builder()
                .id("1")
                .centrosDeDistribuicao(List.of(CentroDistribuicaoItemDTO.builder()
                                .nome("NOME")
                        .build()))
                .nome("Item 1")
                .preco(10.0)
                .build();
    }

    // Método auxiliar para criar um Item
    private Item criarItem() {
        return Item.builder()
                .id("1")
                .centrosDeDistribuicao(List.of(CentroDistribuicaoItem.builder().build()))
                .nome("Item 1")
                .preco(10.0)
                .build();
    }
}
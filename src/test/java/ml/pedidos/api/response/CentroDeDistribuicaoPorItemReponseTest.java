package ml.pedidos.api.response;

import ml.pedidos.api.dto.CentroDistribuicaoItemDTO;
import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.response.CentroDeDistribuicaoPorItemReponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CentroDeDistribuicaoPorItemReponseTest {

    @Test
    void gerarCentrosDeDistribuicaoPorItem_deveRetornarListaDeNomes() {
        // Arrange
        ItemDTO itemDTO = ItemDTO.builder()
                .centrosDeDistribuicao(Arrays.asList(
                        CentroDistribuicaoItemDTO.builder().nome("Centro A").build(),
                        CentroDistribuicaoItemDTO.builder().nome("Centro B").build()
                ))
                .build();

        // Act
        CentroDeDistribuicaoPorItemReponse response = CentroDeDistribuicaoPorItemReponse
                .gerarCentrosDeDistribuicaoPorItem(itemDTO);

        // Assert
        assertNotNull(response);
        List<String> expectedNomes = Arrays.asList("Centro A", "Centro B");
        assertEquals(expectedNomes, response.getDistribuitionCenters());
    }
}
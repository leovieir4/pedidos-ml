package ml.pedidos.api.response;

import ml.pedidos.api.dto.CentroDistribuicaoDTO;
import ml.pedidos.api.dto.CentroDistribuicaoDTOTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CentroDistribuicaoResponseTest {

    @Test
    void testarGerarCentroDistribuicaoResponse() {
        // Arrange
        CentroDistribuicaoDTO centroDistribuicaoDTO = CentroDistribuicaoDTO.builder().build();

        // Act
        CentroDistribuicaoResponse response = CentroDistribuicaoResponse.gerarCentroDistribuicaoResponse(centroDistribuicaoDTO);

        // Assert
        assertNotNull(response);
        assertEquals(centroDistribuicaoDTO, response.getCentroDistribuicaoDTO());
    }

    @Test
    void testarConstrutorComLombok() {
        // Arrange
        CentroDistribuicaoDTO centroDistribuicaoDTO = CentroDistribuicaoDTO.builder().build();
        ErrorResponse errorResponse = ErrorResponse.builder().build();

        // Act
        CentroDistribuicaoResponse response = CentroDistribuicaoResponse.builder()
                .centroDistribuicaoDTO(centroDistribuicaoDTO)
                .errorResponse(errorResponse)
                .build();

        // Assert
        assertNotNull(response);
        assertEquals(centroDistribuicaoDTO, response.getCentroDistribuicaoDTO());
        assertEquals(errorResponse, response.getErrorResponse());
    }

    @Test
    void testarGettersAndSetters() {
        // Arrange
        CentroDistribuicaoDTO centroDistribuicaoDTO = CentroDistribuicaoDTO.builder().build();
        ErrorResponse errorResponse = ErrorResponse.builder().build();
        CentroDistribuicaoResponse response = CentroDistribuicaoResponse.builder().build();

        // Act
        response.setCentroDistribuicaoDTO(centroDistribuicaoDTO);
        response.setErrorResponse(errorResponse);

        // Assert
        assertEquals(centroDistribuicaoDTO, response.getCentroDistribuicaoDTO());
        assertEquals(errorResponse, response.getErrorResponse());
    }
}
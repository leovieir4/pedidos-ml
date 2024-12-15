package ml.pedidos.api.response;

import ml.pedidos.api.response.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ErrorResponseTest {

    @Test
    void gerarError_deveCriarErrorResponseComMensagem() {
        // Arrange
        String mensagem = "Erro!";

        // Act
        ErrorResponse errorResponse = ErrorResponse.gerarError(mensagem);

        // Assert
        assertNotNull(errorResponse);
        assertEquals(mensagem, errorResponse.getMensagem());
    }
}
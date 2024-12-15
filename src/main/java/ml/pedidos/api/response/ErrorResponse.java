package ml.pedidos.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String mensagem;

    public static ErrorResponse gerarError(String mensagem) {
        return ErrorResponse.builder()
                .mensagem(mensagem)
                .build();
    }

}

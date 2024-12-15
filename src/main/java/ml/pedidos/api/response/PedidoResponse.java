package ml.pedidos.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ml.pedidos.api.dto.PedidoDTO;

@Data
@Builder
public class PedidoResponse {

    @JsonProperty("pedido")
    private PedidoDTO pedidoDTO;
    private ErrorResponse errorResponse;
}

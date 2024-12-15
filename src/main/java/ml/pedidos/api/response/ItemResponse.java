package ml.pedidos.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ml.pedidos.api.dto.ItemDTO;

@Data
@Builder
public class ItemResponse {

    @JsonProperty("item")
    private ItemDTO itemDTO;
    private ErrorResponse errorResponse;

}

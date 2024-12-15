package ml.pedidos.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ml.pedidos.api.dto.CentroDistribuicaoDTO;

@Data
@Builder
public class CentroDistribuicaoResponse {
    @JsonProperty("centroDistribuicao")
    private CentroDistribuicaoDTO centroDistribuicaoDTO;
    private ErrorResponse errorResponse;

    public final static CentroDistribuicaoResponse gerarCentroDistribuicaoResponse(CentroDistribuicaoDTO centroDistribuicaoDTO){
        return CentroDistribuicaoResponse.builder()
                .centroDistribuicaoDTO(centroDistribuicaoDTO)
                .build();
    }

}

package ml.pedidos.api.response;

import lombok.Builder;
import lombok.Data;
import ml.pedidos.api.dto.CentroDistribuicaoItemDTO;
import ml.pedidos.api.dto.ItemDTO;

import java.util.List;

@Data
@Builder
public class CentroDeDistribuicaoPorItemReponse {

    List<String> distribuitionCenters;
    private ErrorResponse errorResponse;

    public static final CentroDeDistribuicaoPorItemReponse gerarCentrosDeDistribuicaoPorItem(ItemDTO itemDTO){
        return CentroDeDistribuicaoPorItemReponse.builder()
                .distribuitionCenters(itemDTO.getCentrosDeDistribuicao().stream()
                        .map(CentroDistribuicaoItemDTO::getNome).toList())
                .build();
    }

}

package ml.pedidos.api.response;

import lombok.Builder;
import lombok.Data;
import ml.pedidos.api.dto.ItemDTO;

import java.util.List;

@Data
@Builder
public class ListaItensResponse {

    List<ItemResponse> itens;
    ErrorResponse errorResponse;

    public static ListaItensResponse gerarListaDeItensRespose(List<ItemDTO> response){
        return ListaItensResponse.builder()
                .itens(response.stream().map(itemDTO -> ItemResponse.builder()
                        .itemDTO(itemDTO)
                        .build()).toList())
                .build();
    }
}

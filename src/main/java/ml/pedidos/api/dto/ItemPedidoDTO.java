package ml.pedidos.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemPedidoDTO {
    private String id;
    @Schema(hidden = true)
    private String nome;
    private Integer quantidade;
    @Schema(hidden = true)
    private double preco;
    @JsonProperty("centrosDeDistribuicao")
    @Schema(hidden = true)
    private List<CentroDistribuicaoItemDTO> centrosDistribuicaoDTO;
}
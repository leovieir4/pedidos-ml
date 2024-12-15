package ml.pedidos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {

    @Schema(hidden = true)
    private String id;

    @NotEmpty(message = "A lista de itens não pode ser nula ou vazia.")
    private List<ItemPedidoDTO> itens;

    @Schema(hidden = true)
    private Double valorTotal;

    @AssertTrue(message = "A lista de itens possui IDs inválidos (nulos ou vazios).")
    @Schema(hidden = true)
    public boolean isListaDeItensIdValido() {
        if (itens == null || itens.isEmpty()) {
            return true; // Lista nula ou vazia, não precisa validar IDs
        }

        return itens.stream().allMatch(item -> item.getId() != null);
    }

    @AssertTrue(message = "A quantidade de itens não pode ser menor que 1")
    @Schema(hidden = true)
    public boolean isListaQuantidadeDeItensValida() {
        if (itens == null || itens.isEmpty()) {
            return true; // Lista nula ou vazia, não precisa validar IDs
        }

        return !itens.stream().allMatch(itemPedidoDTO -> itemPedidoDTO.getQuantidade() < 1);
    }
}
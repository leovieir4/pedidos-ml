package ml.pedidos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {

    @Schema(hidden = true)
    private String id;
    private String nome;
    private Double preco;

    private List<CentroDistribuicaoItemDTO> centrosDeDistribuicao;

    @Schema(hidden = true)
    @AssertTrue(message = "Não podem ter centros de distribuções com o mesmo nome")
    public boolean isNomeCentroDeDistribuicaoValido(){
        if (centrosDeDistribuicao == null || centrosDeDistribuicao.isEmpty()) {
            return false; // Lista vazia ou nula, não há duplicatas
        }

        List<String> nomesDistintos = centrosDeDistribuicao.stream()
                .map(CentroDistribuicaoItemDTO::getNome)
                .distinct()
                .toList();

        return nomesDistintos.size() == centrosDeDistribuicao.size();
    }

    @Schema(hidden = true)
    @AssertTrue(message = "O preço do item não pode ser menor que 1")
    public boolean isPrecoValido(){


        return !(isNull(preco) || preco < 1);
    }

    @Schema(hidden = true)
    @AssertTrue(message = "A quantidade de itens dos centros de distribuição não pode ser menor que 1")
    public boolean isQuantidadeDeCentroValida(){
        if (centrosDeDistribuicao == null || centrosDeDistribuicao.isEmpty()) {
            return false; // Lista vazia ou nula, não há duplicatas
        }

        boolean isMenorQueUm = centrosDeDistribuicao.stream()
                .map(CentroDistribuicaoItemDTO::getQuantidade)
                .anyMatch(valor -> valor < 1);

        return !isMenorQueUm;
    }



}

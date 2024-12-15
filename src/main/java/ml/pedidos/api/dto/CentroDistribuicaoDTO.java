package ml.pedidos.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentroDistribuicaoDTO {

    private String nome;

    @JsonProperty("endereco")
    private EnderecoDTO enderecoDTO;


    @AssertTrue(message = "O endereco não pode ser nulo")
    private boolean isEnderecoPreenchido(){
        return Objects.nonNull(enderecoDTO);
    }


    @AssertTrue(message = "O nome do CED deve nao pode ser nulo ou vazio")
    private boolean isNomePreenchido(){
        return Objects.nonNull(nome) && !nome.isBlank();
    }


    @AssertTrue(message = "O numero não pode ser vazio ou 0")
    private boolean isNumeroPreenchido(){
        return Objects.nonNull(enderecoDTO) && enderecoDTO.getNumero() != 0;
    }

    @AssertTrue(message = "O cep informado esta invalido.")
    private boolean isCepfValido(){

        if (Objects.isNull(enderecoDTO)) {
            return false;
        }
        String cepNumerico = enderecoDTO.getCep().replaceAll("[^0-9]", "");

        if (cepNumerico.length() != 8) {
            return false; // CEP inválido
        }

        char primeiroDigito = cepNumerico.charAt(0);
        for (int i = 1; i < 8; i++) {
            if (cepNumerico.charAt(i) != primeiroDigito) {
                return true;
            }
        }

        return false;
    }

}

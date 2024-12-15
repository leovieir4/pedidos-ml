package ml.pedidos.api.dto;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoDTO {
    private String cep;
    private int numero;

    @Schema(hidden = true)
    private String cidade;

    @Schema(hidden = true)
    private String estado;

    @Schema(hidden = true)
    private String bairro;

    @Schema(hidden = true)
    private String uf;

    @Schema(hidden = true)
    private String logradouro;

}

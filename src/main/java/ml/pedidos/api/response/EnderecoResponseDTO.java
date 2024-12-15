package ml.pedidos.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnderecoResponseDTO {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String estado;
    private String uf;
    private String ibge;
    private String gia;
    private String ddd;
    private String siafi;


}

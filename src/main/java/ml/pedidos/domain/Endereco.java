package ml.pedidos.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBDocument
@Builder
public class Endereco {

    @DynamoDBAttribute(attributeName = "cep")
    private String cep;

    @DynamoDBAttribute(attributeName = "numero")
    private int numero;

    @DynamoDBAttribute(attributeName = "cidade")
    private String cidade;

    @DynamoDBAttribute(attributeName = "estado")
    private String estado;

    @DynamoDBAttribute(attributeName = "bairro")
    private String bairro;

    @DynamoDBAttribute(attributeName = "uf")
    private String uf;

    @DynamoDBAttribute(attributeName = "logradouro")
    private String logradouro;

}

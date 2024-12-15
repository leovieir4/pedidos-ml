package ml.pedidos.domain;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "CentrosDistribuicao")
@Builder
public class CentroDistribuicao {

    public static final String NOME_TABELA = "CentrosDistribuicao";

    @DynamoDBHashKey(attributeName = "nome")
    private String nome;

    @DynamoDBAttribute(attributeName = "endereco")
    private Endereco endereco;

}

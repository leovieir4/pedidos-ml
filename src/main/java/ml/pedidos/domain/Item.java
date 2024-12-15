package ml.pedidos.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ml.pedidos.api.dto.CentroDistribuicaoItemDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDBTable(tableName = "Itens")
@Builder
public class Item {

    public final static String NOME_TABELA = "Itens";

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    private String id;

    @DynamoDBAttribute(attributeName = "nome")
    private String nome;

    @DynamoDBAttribute(attributeName = "preco")
    private Double preco;

    @DynamoDBAttribute(attributeName = "centrosDeDistribuicao")
    private List<CentroDistribuicaoItem> centrosDeDistribuicao;
}

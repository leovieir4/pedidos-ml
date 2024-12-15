package ml.pedidos.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import ml.pedidos.domain.CentroDistribuicao;
import ml.pedidos.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CentroDistribuicaoRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    public CentroDistribuicao salvar(CentroDistribuicao centroDistribuicao) {
        centroDistribuicao.setNome(centroDistribuicao.getNome().toUpperCase());
        dynamoDBMapper.save(centroDistribuicao);
        return centroDistribuicao;
    }

    public CentroDistribuicao buscarPorNome(String nome) {
        return dynamoDBMapper.load(CentroDistribuicao.class, nome.toUpperCase());
    }

    public List<CentroDistribuicao> buscarPorNomes(List<String> nomes) {

        nomes = nomes.stream().map(String::toUpperCase).toList();

        Map<String, KeysAndAttributes> requestItems = new HashMap<>();

        KeysAndAttributes keysAndAttributes = new KeysAndAttributes();
        keysAndAttributes.setKeys(nomes.stream()
                .map(id -> Map.of("nome", new AttributeValue(id)))
                .collect(Collectors.toList()));
        requestItems.put(CentroDistribuicao.NOME_TABELA, keysAndAttributes);

        BatchGetItemRequest batchGetItemRequest = new BatchGetItemRequest()
                .withRequestItems(requestItems);

        BatchGetItemResult result = amazonDynamoDB.batchGetItem(batchGetItemRequest);

        return result.getResponses().get(CentroDistribuicao.NOME_TABELA).stream()
                .map(centroMap -> dynamoDBMapper.marshallIntoObject(CentroDistribuicao.class, centroMap))
                .collect(Collectors.toList());
    }

}

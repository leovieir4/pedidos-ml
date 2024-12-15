package ml.pedidos.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import ml.pedidos.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private AmazonDynamoDB amazonDynamoDB;

    public Item salvar(Item item){
        dynamoDBMapper.save(item);
        return item;
    }

    public Item buscarItemPorId(String id){
        return dynamoDBMapper.load(Item.class, id);
    }

    public List<Item> buscarPorIds(List<String> ids) {
        Map<String, KeysAndAttributes> requestItems = new HashMap<>();

        KeysAndAttributes keysAndAttributes = new KeysAndAttributes();
        keysAndAttributes.setKeys(ids.stream()
                .map(id -> Map.of("id", new AttributeValue(id)))
                .collect(Collectors.toList()));
        requestItems.put(Item.NOME_TABELA, keysAndAttributes);

        BatchGetItemRequest batchGetItemRequest = new BatchGetItemRequest()
                .withRequestItems(requestItems);

        BatchGetItemResult result = amazonDynamoDB.batchGetItem(batchGetItemRequest);

        return result.getResponses().get(Item.NOME_TABELA).stream()
                .map(itemMap -> dynamoDBMapper.marshallIntoObject(Item.class, itemMap))
                .collect(Collectors.toList());
    }

    public List<Item> buscarTodosPaginado(int page, int size) {
        Map<String, AttributeValue> exclusiveStartKey = null;
        if (page > 1) {
            exclusiveStartKey = calcularExclusiveStartKey(page, size);
        }

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withLimit(size)
                .withExclusiveStartKey(exclusiveStartKey);

        List<Item> itens = dynamoDBMapper.scan(Item.class, scanExpression);

        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, itens.size());

        return itens.subList(startIndex, endIndex);
    }

    private Map<String, AttributeValue> calcularExclusiveStartKey(int page, int size) {
        DynamoDBQueryExpression<Item> queryExpression = new DynamoDBQueryExpression<Item>()
                .withHashKeyValues(new Item())
                .withLimit((page - 1) * size);

        List<Item> itens = dynamoDBMapper.query(Item.class, queryExpression);
        if (!itens.isEmpty()) {
            Item ultimoItem = itens.get(itens.size() - 1);
            Map<String, AttributeValue> lastEvaluatedKey = new HashMap<>();
            lastEvaluatedKey.put("id", new AttributeValue(ultimoItem.getId()));
            lastEvaluatedKey.put("nome", new AttributeValue(ultimoItem.getNome()));
            return lastEvaluatedKey;
        } else {
            return null;
        }
    }


}

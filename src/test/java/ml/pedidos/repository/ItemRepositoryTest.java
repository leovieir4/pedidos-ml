package ml.pedidos.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import ml.pedidos.domain.Item;
import ml.pedidos.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemRepositoryTest {

    @InjectMocks
    private ItemRepository itemRepository;

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Mock
    private AmazonDynamoDB amazonDynamoDB;

    @Test
    public void testSalvar() {
        Item item = new Item();
        itemRepository.salvar(item);

        verify(dynamoDBMapper, times(1)).save(item);
    }

    @Test
    public void testBuscarItemPorId() {
        String id = "123";
        Item item = new Item();
        when(dynamoDBMapper.load(Item.class, id)).thenReturn(item);

        Item resultado = itemRepository.buscarItemPorId(id);

        assertEquals(item, resultado);
        verify(dynamoDBMapper, times(1)).load(Item.class, id);
    }

    @Test
    public void testBuscarPorIds() {
        List<String> ids = List.of("1", "2", "3");
        Map<String, KeysAndAttributes> requestItems = new HashMap<>();
        KeysAndAttributes keysAndAttributes = new KeysAndAttributes();
        keysAndAttributes.setKeys(ids.stream()
                .map(id -> Map.of("id", new AttributeValue(id)))
                .collect(Collectors.toList()));
        requestItems.put(Item.NOME_TABELA, keysAndAttributes);
        BatchGetItemRequest batchGetItemRequest = new BatchGetItemRequest()
                .withRequestItems(requestItems);

        // Simulando a resposta do DynamoDB
        Map<String, AttributeValue> itemMap1 = new HashMap<>();
        itemMap1.put("id", new AttributeValue("1"));
        Map<String, AttributeValue> itemMap2 = new HashMap<>();
        itemMap2.put("id", new AttributeValue("2"));
        Map<String, AttributeValue> itemMap3 = new HashMap<>();
        itemMap3.put("id", new AttributeValue("3"));
        BatchGetItemResult batchGetItemResult = new BatchGetItemResult();
        batchGetItemResult.addResponsesEntry(Item.NOME_TABELA, List.of(itemMap1, itemMap2, itemMap3));

        when(amazonDynamoDB.batchGetItem(batchGetItemRequest)).thenReturn(batchGetItemResult);

        // Simulando o mapeamento dos itens
        Item item1 = new Item();
        item1.setId("1");
        Item item2 = new Item();
        item2.setId("2");
        Item item3 = new Item();
        item3.setId("3");
        when(dynamoDBMapper.marshallIntoObject(Item.class, itemMap1)).thenReturn(item1);
        when(dynamoDBMapper.marshallIntoObject(Item.class, itemMap2)).thenReturn(item2);
        when(dynamoDBMapper.marshallIntoObject(Item.class, itemMap3)).thenReturn(item3);

        List<Item> resultado = itemRepository.buscarPorIds(ids);

        assertEquals(3, resultado.size());
        assertEquals(item1, resultado.get(0));
        assertEquals(item2, resultado.get(1));
        assertEquals(item3, resultado.get(2));

        verify(amazonDynamoDB, times(1)).batchGetItem(batchGetItemRequest);
        verify(dynamoDBMapper, times(1)).marshallIntoObject(Item.class, itemMap1);
        verify(dynamoDBMapper, times(1)).marshallIntoObject(Item.class, itemMap2);
        verify(dynamoDBMapper, times(1)).marshallIntoObject(Item.class, itemMap3);
    }
}
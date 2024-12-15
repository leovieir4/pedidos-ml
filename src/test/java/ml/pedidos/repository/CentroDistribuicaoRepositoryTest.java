package ml.pedidos.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import ml.pedidos.domain.CentroDistribuicao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CentroDistribuicaoRepositoryTest {

    @InjectMocks
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Mock
    private AmazonDynamoDB amazonDynamoDB;

    @Test
    public void testSalvar() {
        CentroDistribuicao centroDistribuicao = new CentroDistribuicao();
        centroDistribuicao.setNome("nome");
        centroDistribuicaoRepository.salvar(centroDistribuicao);

        verify(dynamoDBMapper, times(1)).save(centroDistribuicao);
    }

    @Test
    public void testBuscarPorNome() {
        String nome = "CD1";
        CentroDistribuicao centroDistribuicao = new CentroDistribuicao();
        when(dynamoDBMapper.load(CentroDistribuicao.class, nome)).thenReturn(centroDistribuicao);

        CentroDistribuicao resultado = centroDistribuicaoRepository.buscarPorNome(nome);

        assertEquals(centroDistribuicao, resultado);
        verify(dynamoDBMapper, times(1)).load(CentroDistribuicao.class, nome);
    }

    @Test
    public void testBuscarPorNomes() {
        List<String> nomes = List.of("CD1", "CD2", "CD3");
        Map<String, KeysAndAttributes> requestItems = new HashMap<>();
        KeysAndAttributes keysAndAttributes = new KeysAndAttributes();
        keysAndAttributes.setKeys(nomes.stream()
                .map(nome -> Map.of("nome", new AttributeValue(nome)))
                .collect(Collectors.toList()));
        requestItems.put(CentroDistribuicao.NOME_TABELA, keysAndAttributes);
        BatchGetItemRequest batchGetItemRequest = new BatchGetItemRequest()
                .withRequestItems(requestItems);

        // Simulando a resposta do DynamoDB
        Map<String, AttributeValue> centroMap1 = new HashMap<>();
        centroMap1.put("nome", new AttributeValue("CD1"));
        Map<String, AttributeValue> centroMap2 = new HashMap<>();
        centroMap2.put("nome", new AttributeValue("CD2"));
        Map<String, AttributeValue> centroMap3 = new HashMap<>();
        centroMap3.put("nome", new AttributeValue("CD3"));
        BatchGetItemResult batchGetItemResult = new BatchGetItemResult();
        batchGetItemResult.addResponsesEntry(CentroDistribuicao.NOME_TABELA, List.of(centroMap1, centroMap2, centroMap3));

        when(amazonDynamoDB.batchGetItem(batchGetItemRequest)).thenReturn(batchGetItemResult);

        // Simulando o mapeamento dos centros de distribuição
        CentroDistribuicao cd1 = new CentroDistribuicao();
        cd1.setNome("CD1");
        CentroDistribuicao cd2 = new CentroDistribuicao();
        cd2.setNome("CD2");
        CentroDistribuicao cd3 = new CentroDistribuicao();
        cd3.setNome("CD3");
        when(dynamoDBMapper.marshallIntoObject(CentroDistribuicao.class, centroMap1)).thenReturn(cd1);
        when(dynamoDBMapper.marshallIntoObject(CentroDistribuicao.class, centroMap2)).thenReturn(cd2);
        when(dynamoDBMapper.marshallIntoObject(CentroDistribuicao.class, centroMap3)).thenReturn(cd3);

        List<CentroDistribuicao> resultado = centroDistribuicaoRepository.buscarPorNomes(nomes);

        assertEquals(3, resultado.size());
        assertEquals(cd1, resultado.get(0));
        assertEquals(cd2, resultado.get(1));
        assertEquals(cd3, resultado.get(2));

        verify(amazonDynamoDB, times(1)).batchGetItem(batchGetItemRequest);
        verify(dynamoDBMapper, times(1)).marshallIntoObject(CentroDistribuicao.class, centroMap1);
        verify(dynamoDBMapper, times(1)).marshallIntoObject(CentroDistribuicao.class, centroMap2);
        verify(dynamoDBMapper, times(1)).marshallIntoObject(CentroDistribuicao.class, centroMap3);
    }
}
package ml.pedidos.repository;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import ml.pedidos.domain.Pedido;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoRepositoryTest {

    @InjectMocks
    private PedidoRepository pedidoRepository;

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @Test
    public void testSalvar() {
        Pedido pedido = new Pedido();
        pedidoRepository.salvar(pedido);

        verify(dynamoDBMapper, times(1)).save(pedido);
    }

    @Test
    public void testBuscarPedidoPorId() {
        String id = "123";
        Pedido pedido = new Pedido();
        when(dynamoDBMapper.load(Pedido.class, id)).thenReturn(pedido);

        Pedido resultado = pedidoRepository.buscarPedidoPorId(id);

        assertEquals(pedido, resultado);
        verify(dynamoDBMapper, times(1)).load(Pedido.class, id);
    }
}
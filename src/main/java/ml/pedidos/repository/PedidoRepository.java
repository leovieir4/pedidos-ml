package ml.pedidos.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import ml.pedidos.domain.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PedidoRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    public Pedido salvar(Pedido pedido) {
        dynamoDBMapper.save(pedido);
        return pedido;
    }

    public Pedido buscarPedidoPorId(String id) {
        return dynamoDBMapper.load(Pedido.class, id);
    }
}

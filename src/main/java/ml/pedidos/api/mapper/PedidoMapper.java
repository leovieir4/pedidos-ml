package ml.pedidos.api.mapper;

import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.dto.PedidoDTO;
import ml.pedidos.domain.Pedido;

import java.util.List;

public interface PedidoMapper {
    Pedido toEntity(PedidoDTO dto);
    PedidoDTO toDto(Pedido entitym, List<ItemDTO> itens);

//    ItemPedido toEntity(ItemPedidoDTO dto);
//    ItemPedidoDTO toDto(ItemPedido entity);
}
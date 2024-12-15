package ml.pedidos.api.mapper;

import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.dto.ItemPedidoDTO;
import ml.pedidos.domain.Item;
import ml.pedidos.domain.ItemPedido;

public interface ItemMapper {
    Item toEntity(ItemDTO dto);
    ItemDTO toDto(Item entity);
}

package ml.pedidos.api.mapper;

import ml.pedidos.api.dto.ItemPedidoDTO;
import ml.pedidos.domain.ItemPedido;


public interface ItemPedidoMapper {
    ItemPedido toEntity(ItemPedidoDTO dto);
    ItemPedidoDTO toDto(ItemPedido entity);
}
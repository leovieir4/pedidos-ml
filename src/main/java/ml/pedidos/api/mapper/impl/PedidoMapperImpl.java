package ml.pedidos.api.mapper.impl;

import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.dto.ItemPedidoDTO;
import ml.pedidos.api.dto.PedidoDTO;
import ml.pedidos.api.mapper.PedidoMapper;
import ml.pedidos.domain.ItemPedido;
import ml.pedidos.domain.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PedidoMapperImpl implements PedidoMapper {
    @Override
    public Pedido toEntity(PedidoDTO dto) {
        return Pedido.builder()
                .itens(gerarListaItemPedidos(dto.getItens()))
                .build();
    }

    @Override
    public PedidoDTO toDto(Pedido entity, List<ItemDTO> itens) {
        PedidoDTO pedidoDTO = PedidoDTO.builder()
                .id(entity.getId())
                .itens(gerarListaItemPedidosDTO(entity.getItens()))
                .build();

        pedidoDTO.setItens(anexarCentrosDeDistribuicoesAosItens(pedidoDTO.getItens(), itens));
        pedidoDTO.setValorTotal(adicionarValorTotalDoPedido(pedidoDTO));

        return pedidoDTO;
    }

    private Double adicionarValorTotalDoPedido(PedidoDTO pedidoDTO){
        return pedidoDTO.getItens().stream().mapToDouble(value ->
                value.getPreco() * value.getQuantidade()).sum();
    }

    private List<ItemPedidoDTO> anexarCentrosDeDistribuicoesAosItens(List<ItemPedidoDTO> itensSemCD, List<ItemDTO> itensComCD){
        itensSemCD.forEach(itemPedidoDTO -> {
            itensComCD.stream().forEach(itemDTO -> {
                if(itemDTO.getId().equals(itemPedidoDTO.getId())){
                    itemPedidoDTO.setCentrosDistribuicaoDTO(itemDTO.getCentrosDeDistribuicao());

                    itemPedidoDTO.getCentrosDistribuicaoDTO().forEach(centroDistribuicaoItemDTO ->
                            centroDistribuicaoItemDTO.setNome(centroDistribuicaoItemDTO.getNome().toUpperCase()));

                    itemPedidoDTO.setNome(itemDTO.getNome());
                    itemPedidoDTO.setPreco(itemDTO.getPreco());
                    itemPedidoDTO.setQuantidade(itemPedidoDTO.getQuantidade());
                }
            });
        });

        return itensSemCD;
    }

    private List<ItemPedido> gerarListaItemPedidos(List<ItemPedidoDTO> dtos) {
        return dtos.stream().map(itemPedidoDTO ->
                        ItemPedido.builder()
                                .id(itemPedidoDTO.getId())
                                .quantidade(itemPedidoDTO.getQuantidade())
                                .build())
                .toList();
    }

    private List<ItemPedidoDTO> gerarListaItemPedidosDTO(List<ItemPedido> itemPedidos) {
        return itemPedidos.stream().map(itemPedido -> ItemPedidoDTO.builder()
                .id(itemPedido.getId())
                .quantidade(itemPedido.getQuantidade())
                .build()).toList();
    }
}
package ml.pedidos.api.mapper.impl;

import ml.pedidos.api.dto.CentroDistribuicaoItemDTO;
import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.mapper.ItemMapper;
import ml.pedidos.domain.CentroDistribuicaoItem;
import ml.pedidos.domain.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapperImpl implements ItemMapper {
    @Override
    public Item toEntity(ItemDTO dto) {
        return Item.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .preco(dto.getPreco())
                .centrosDeDistribuicao(dto.getCentrosDeDistribuicao().stream().map(centroDistribuicaoItemDTO ->
                        CentroDistribuicaoItem.builder()
                                .quantidade(centroDistribuicaoItemDTO.getQuantidade())
                                .nome(centroDistribuicaoItemDTO.getNome().toUpperCase())
                                .build()).toList())
                .build();
    }

    @Override
    public ItemDTO toDto(Item entity) {
        return ItemDTO.builder()
                .id(entity.getId())
                .preco(entity.getPreco())
                .nome(entity.getNome())
                .centrosDeDistribuicao(entity.getCentrosDeDistribuicao().stream().map(centroDistribuicaoItem ->
                        CentroDistribuicaoItemDTO.builder()
                                .quantidade(centroDistribuicaoItem.getQuantidade())
                                .nome(centroDistribuicaoItem.getNome())
                                .build()).toList())
                .build();
    }
}

package ml.pedidos.api.mapper;

import ml.pedidos.api.dto.CentroDistribuicaoDTO;
import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.domain.CentroDistribuicao;
import ml.pedidos.domain.Item;

public interface CentroDistribuicaoMapper {

    CentroDistribuicao toEntity(CentroDistribuicaoDTO dto);
    CentroDistribuicaoDTO toDto(CentroDistribuicao entity);

}

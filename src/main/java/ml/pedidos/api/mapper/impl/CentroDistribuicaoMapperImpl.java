package ml.pedidos.api.mapper.impl;

import ml.pedidos.api.dto.CentroDistribuicaoDTO;
import ml.pedidos.api.dto.EnderecoDTO;
import ml.pedidos.api.mapper.CentroDistribuicaoMapper;
import ml.pedidos.domain.CentroDistribuicao;
import ml.pedidos.domain.Endereco;
import org.springframework.stereotype.Component;

@Component
public class CentroDistribuicaoMapperImpl implements CentroDistribuicaoMapper {

    @Override
    public CentroDistribuicao toEntity(CentroDistribuicaoDTO dto) {
        return CentroDistribuicao.builder()
                .nome(dto.getNome())
                .endereco(Endereco.builder()
                        .cep(dto.getEnderecoDTO().getCep())
                        .numero(dto.getEnderecoDTO().getNumero())
                        .build())
                .build();
    }

    @Override
    public CentroDistribuicaoDTO toDto(CentroDistribuicao entity) {
        return CentroDistribuicaoDTO.builder()
                .nome(entity.getNome())
                .enderecoDTO(EnderecoDTO.builder()
                        .cep(entity.getEndereco().getCep())
                        .numero(entity.getEndereco().getNumero())
                        .uf(entity.getEndereco().getUf())
                        .bairro(entity.getEndereco().getBairro())
                        .logradouro(entity.getEndereco().getLogradouro())
                        .cidade(entity.getEndereco().getCidade())
                        .estado(entity.getEndereco().getEstado())
                        .build())
                .build();
    }
}

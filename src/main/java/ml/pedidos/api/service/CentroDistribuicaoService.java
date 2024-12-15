package ml.pedidos.api.service;

import ml.pedidos.api.dto.CentroDistribuicaoDTO;
import ml.pedidos.api.exceptions.CentroDeDistribucaiJaCadastradoException;
import ml.pedidos.api.exceptions.ConsultarEnderecoException;

public interface CentroDistribuicaoService {
    CentroDistribuicaoDTO criarCentroDistribuicao(CentroDistribuicaoDTO centroDistribuicaoDTO)
            throws ConsultarEnderecoException, CentroDeDistribucaiJaCadastradoException;
}

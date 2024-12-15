package ml.pedidos.api.service.impl;

import ml.pedidos.api.dto.CentroDistribuicaoDTO;
import ml.pedidos.api.dto.EnderecoDTO;
import ml.pedidos.api.exceptions.CentroDeDistribucaiJaCadastradoException;
import ml.pedidos.api.exceptions.ConsultarEnderecoException;
import ml.pedidos.api.gateway.EnderecoGateway;
import ml.pedidos.api.mapper.CentroDistribuicaoMapper;
import ml.pedidos.api.response.EnderecoResponseDTO;
import ml.pedidos.api.service.CentroDistribuicaoService;
import ml.pedidos.domain.CentroDistribuicao;
import ml.pedidos.repository.CentroDistribuicaoRepository;
import ml.pedidos.util.MensagensDeErros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CentroDistribuicaoServiceImpl implements CentroDistribuicaoService {

    @Autowired
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Autowired
    private CentroDistribuicaoMapper centroDistribuicaoMapper;

    @Autowired
    private EnderecoGateway enderecoGateway;

    @Override
    public CentroDistribuicaoDTO criarCentroDistribuicao(CentroDistribuicaoDTO centroDistribuicaoDTO) throws ConsultarEnderecoException {

        isCentroDistribuicaoValido(centroDistribuicaoDTO.getNome());

        CentroDistribuicao centroDistribuicao = centroDistribuicaoMapper.toEntity(centroDistribuicaoDTO);

        EnderecoResponseDTO enderecoResponseDTO = consultarEndereco(centroDistribuicaoDTO.getEnderecoDTO().getCep());

        adicionarInformacoesConsultaCep(centroDistribuicao, enderecoResponseDTO);

        centroDistribuicao = centroDistribuicaoRepository.salvar(centroDistribuicao);
        return centroDistribuicaoMapper.toDto(centroDistribuicao);
    }

    private void isCentroDistribuicaoValido(String nome){
        CentroDistribuicao centroDistribuicao = centroDistribuicaoRepository.buscarPorNome(nome);
        if(Objects.nonNull(centroDistribuicao)) {
            throw new CentroDeDistribucaiJaCadastradoException(
                    String.format(MensagensDeErros.MENSAGEM_ERRO_CENTRO_DISTRIBUICAO_CADASTRADO, nome));
        }
    }

    private EnderecoResponseDTO consultarEndereco(String cep) throws ConsultarEnderecoException {
        try {
            return enderecoGateway.buscarEnderecoPorCEP(cep);
        } catch (Exception ex){
            throw new ConsultarEnderecoException(String.format(MensagensDeErros.MENSAGEM_ERRO_CONSULTA_CEP,
                    ex.getMessage()));
        }
    }

    private void adicionarInformacoesConsultaCep(CentroDistribuicao centroDistribuicao,
                                                 EnderecoResponseDTO enderecoResponseDTO){
        centroDistribuicao.getEndereco().setUf(enderecoResponseDTO.getUf());
        centroDistribuicao.getEndereco().setBairro(enderecoResponseDTO.getBairro());
        centroDistribuicao.getEndereco().setCidade(enderecoResponseDTO.getLocalidade());
        centroDistribuicao.getEndereco().setLogradouro(enderecoResponseDTO.getLogradouro());
        centroDistribuicao.getEndereco().setEstado(enderecoResponseDTO.getEstado());
    }

}

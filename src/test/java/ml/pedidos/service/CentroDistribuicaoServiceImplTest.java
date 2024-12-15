package ml.pedidos.service;

import ml.pedidos.api.dto.CentroDistribuicaoDTO;
import ml.pedidos.api.dto.CentroDistribuicaoDTOTest;
import ml.pedidos.api.dto.EnderecoDTO;
import ml.pedidos.api.exceptions.CentroDeDistribucaiJaCadastradoException;
import ml.pedidos.api.exceptions.ConsultarEnderecoException;
import ml.pedidos.api.gateway.EnderecoGateway;
import ml.pedidos.api.mapper.CentroDistribuicaoMapper;
import ml.pedidos.api.response.EnderecoResponseDTO;
import ml.pedidos.api.service.impl.CentroDistribuicaoServiceImpl;
import ml.pedidos.domain.CentroDistribuicao;
import ml.pedidos.domain.Endereco;
import ml.pedidos.repository.CentroDistribuicaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CentroDistribuicaoServiceImplTest {

    @Mock
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Mock
    private CentroDistribuicaoMapper centroDistribuicaoMapper;

    @Mock
    private EnderecoGateway enderecoGateway;

    @InjectMocks
    private CentroDistribuicaoServiceImpl centroDistribuicaoService;

    @Test
    void criarCentroDistribuicao_comSucesso() throws ConsultarEnderecoException {
        // Arrange
        CentroDistribuicaoDTO centroDistribuicaoDTO = criarCentroDistribuicaoDTOValido();
        CentroDistribuicao centroDistribuicao = criarCentroDistribuicaoValido();
        EnderecoResponseDTO enderecoResponseDTO = criarEnderecoResponseDTOValido();

        when(centroDistribuicaoRepository.buscarPorNome(any())).thenReturn(null);
        when(centroDistribuicaoMapper.toEntity(centroDistribuicaoDTO)).thenReturn(centroDistribuicao);
        when(enderecoGateway.buscarEnderecoPorCEP(any())).thenReturn(enderecoResponseDTO);
        when(centroDistribuicaoRepository.salvar(centroDistribuicao)).thenReturn(centroDistribuicao);
        when(centroDistribuicaoMapper.toDto(centroDistribuicao)).thenReturn(centroDistribuicaoDTO);

        // Act
        CentroDistribuicaoDTO resultado = centroDistribuicaoService.criarCentroDistribuicao(centroDistribuicaoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(centroDistribuicaoDTO, resultado);

        verify(centroDistribuicaoRepository).buscarPorNome(any());
        verify(centroDistribuicaoMapper).toEntity(centroDistribuicaoDTO);
        verify(enderecoGateway).buscarEnderecoPorCEP(any());
        verify(centroDistribuicaoRepository).salvar(centroDistribuicao);
        verify(centroDistribuicaoMapper).toDto(centroDistribuicao);
    }

    @Test
    void criarCentroDistribuicao_comCentroJaCadastrado_deveLancarExcecao() {
        // Arrange
        CentroDistribuicaoDTO centroDistribuicaoDTO = criarCentroDistribuicaoDTOValido();
        CentroDistribuicao centroDistribuicao = criarCentroDistribuicaoValido();

        when(centroDistribuicaoRepository.buscarPorNome(any())).thenReturn(centroDistribuicao);

        // Act & Assert
        assertThrows(CentroDeDistribucaiJaCadastradoException.class,
                () -> centroDistribuicaoService.criarCentroDistribuicao(centroDistribuicaoDTO));

        verify(centroDistribuicaoRepository, never()).salvar(any());
    }

    @Test
    void criarCentroDistribuicao_comErroNaConsultaDoEndereco_deveLancarExcecao() throws ConsultarEnderecoException {
        // Arrange
        CentroDistribuicaoDTO centroDistribuicaoDTO = criarCentroDistribuicaoDTOValido();
        CentroDistribuicao centroDistribuicao = criarCentroDistribuicaoValido();

        when(centroDistribuicaoRepository.buscarPorNome(any())).thenReturn(null);
        when(centroDistribuicaoMapper.toEntity(centroDistribuicaoDTO)).thenReturn(centroDistribuicao);
        when(enderecoGateway.buscarEnderecoPorCEP(any())).thenThrow(new ConsultarEnderecoException("Erro na consulta"));

        // Act & Assert
        assertThrows(ConsultarEnderecoException.class,
                () -> centroDistribuicaoService.criarCentroDistribuicao(centroDistribuicaoDTO));

        verify(centroDistribuicaoRepository, never()).salvar(any());
    }

    // Métodos auxiliares para criação de objetos de teste
    private CentroDistribuicaoDTO criarCentroDistribuicaoDTOValido() {
        return CentroDistribuicaoDTO.builder()
                .nome("Centro A")
                .enderecoDTO(new EnderecoDTO("01001-000", 21, "123", "estado", "bairro",
                        "uf", "logradouro"))
                .build();
    }

    private CentroDistribuicao criarCentroDistribuicaoValido() {
        return CentroDistribuicao.builder()
                .nome("Centro A")
                .endereco(new Endereco("01001-000", 33, "123",
                        "São Paulo", "SP", "bairro", "complemento"))
                .build();
    }

    private EnderecoResponseDTO criarEnderecoResponseDTOValido() {
        return EnderecoResponseDTO.builder()
                .cep("01001-000")
                .logradouro("Rua A")
                .complemento("complemento")
                .bairro("bairro")
                .localidade("São Paulo")
                .uf("SP")
                .ibge("3550308")
                .gia("1")
                .ddd("11")
                .siafi("7107")
                .build();
    }
}
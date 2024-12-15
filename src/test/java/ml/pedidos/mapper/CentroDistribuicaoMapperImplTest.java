package ml.pedidos.mapper;

import ml.pedidos.api.dto.CentroDistribuicaoDTO;
import ml.pedidos.api.dto.CentroDistribuicaoDTOTest;
import ml.pedidos.api.dto.EnderecoDTO;
import ml.pedidos.api.mapper.impl.CentroDistribuicaoMapperImpl;
import ml.pedidos.domain.CentroDistribuicao;
import ml.pedidos.domain.Endereco;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CentroDistribuicaoMapperImplTest {

    private final CentroDistribuicaoMapperImpl mapper = new CentroDistribuicaoMapperImpl();

    @Test
    void testToEntity() {
        // Arrange
        CentroDistribuicaoDTO dto = CentroDistribuicaoDTO.builder()
                .nome("CD Teste")
                .enderecoDTO(EnderecoDTO.builder()
                        .cep("30000000")
                        .numero(123)
                        .build())
                .build();

        // Act
        CentroDistribuicao entity = mapper.toEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals("CD Teste", entity.getNome());
        assertEquals("30000000", entity.getEndereco().getCep());
        assertEquals(123, entity.getEndereco().getNumero());
    }

    @Test
    void testToDto() {
        // Arrange
        CentroDistribuicao entity = CentroDistribuicao.builder()
                .nome("CD Teste")
                .endereco(Endereco.builder()
                        .cep("30000000")
                        .numero(123)
                        .uf("MG")
                        .bairro("Centro")
                        .logradouro("Rua Teste")
                        .cidade("Belo Horizonte")
                        .estado("Minas Gerais")
                        .build())
                .build();

        // Act
        CentroDistribuicaoDTO dto = mapper.toDto(entity);

        // Assert
        assertNotNull(dto);
        assertEquals("CD Teste", dto.getNome());
        assertEquals("30000000", dto.getEnderecoDTO().getCep());
        assertEquals(123, dto.getEnderecoDTO().getNumero());
        assertEquals("MG", dto.getEnderecoDTO().getUf());
        assertEquals("Centro", dto.getEnderecoDTO().getBairro());
        assertEquals("Rua Teste", dto.getEnderecoDTO().getLogradouro());
        assertEquals("Belo Horizonte", dto.getEnderecoDTO().getCidade());
        assertEquals("Minas Gerais", dto.getEnderecoDTO().getEstado());
    }
}
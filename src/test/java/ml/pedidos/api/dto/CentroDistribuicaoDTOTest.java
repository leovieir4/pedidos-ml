package ml.pedidos.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CentroDistribuicaoDTOTest {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testValidacoesComSucesso() {
        // Arrange
        CentroDistribuicaoDTO dto = CentroDistribuicaoDTO.builder()
                .nome("CD Teste")
                .enderecoDTO(EnderecoDTO.builder()
                        .cep("30000-000")
                        .numero(123)
                        .build())
                .build();

        // Act
        Set<ConstraintViolation<CentroDistribuicaoDTO>> violations = validator.validate(dto);

        // Assert
        assertTrue(violations.isEmpty());
    }

    @Test
    void testEnderecoNulo() {
        // Arrange
        CentroDistribuicaoDTO dto = CentroDistribuicaoDTO.builder()
                .nome("CD Teste")
                .build();

        // Act
        Set<ConstraintViolation<CentroDistribuicaoDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(3, violations.size());
    }

    @Test
    void testNomeNulo() {
        // Arrange
        CentroDistribuicaoDTO dto = CentroDistribuicaoDTO.builder()
                .enderecoDTO(EnderecoDTO.builder().cep("30000000").numero(123).build())
                .build();

        // Act
        Set<ConstraintViolation<CentroDistribuicaoDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("O nome do CED deve nao pode ser nulo ou vazio", violations.iterator().next().getMessage());
    }

    @Test
    void testNomeVazio() {
        // Arrange
        CentroDistribuicaoDTO dto = CentroDistribuicaoDTO.builder()
                .nome(" ")
                .enderecoDTO(EnderecoDTO.builder().cep("30000000").numero(123).build())
                .build();

        // Act
        Set<ConstraintViolation<CentroDistribuicaoDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("O nome do CED deve nao pode ser nulo ou vazio", violations.iterator().next().getMessage());
    }

    @Test
    void testNumeroZero() {
        // Arrange
        CentroDistribuicaoDTO dto = CentroDistribuicaoDTO.builder()
                .nome("CD Teste")
                .enderecoDTO(EnderecoDTO.builder().cep("30000000").numero(0).build())
                .build();

        // Act
        Set<ConstraintViolation<CentroDistribuicaoDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("O numero não pode ser vazio ou 0", violations.iterator().next().getMessage());
    }

    @Test
    void testCepInvalido() {
        // Arrange
        CentroDistribuicaoDTO dto = CentroDistribuicaoDTO.builder()
                .nome("CD Teste")
                .enderecoDTO(EnderecoDTO.builder().cep("1234567").numero(123).build())
                .build();

        // Act
        Set<ConstraintViolation<CentroDistribuicaoDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("O cep informado esta invalido.", violations.iterator().next().getMessage());
    }

    @Test
    void testCepComTodosDigitosIguais() {
        // Arrange
        CentroDistribuicaoDTO dto = CentroDistribuicaoDTO.builder()
                .nome("CD Teste")
                .enderecoDTO(EnderecoDTO.builder().cep("11111111").numero(123).build())
                .build();

        // Act
        Set<ConstraintViolation<CentroDistribuicaoDTO>> violations = validator.validate(dto);

        // Assert
        assertEquals(1, violations.size());
        assertEquals("O cep informado esta invalido.", violations.iterator().next().getMessage());
    }

    @Test
    void testConstrutorPadrao() {
        // Act
        CentroDistribuicaoDTO dto = new CentroDistribuicaoDTO();

        // Assert
        assertNotNull(dto);
    }

    @Test
    void testConstrutorComParametros() {
        // Arrange
        EnderecoDTO enderecoDTO = EnderecoDTO.builder().build();

        // Act
        CentroDistribuicaoDTO dto = new CentroDistribuicaoDTO("CD Teste", enderecoDTO);

        // Assert
        assertNotNull(dto);
        assertEquals("CD Teste", dto.getNome());
        assertEquals(enderecoDTO, dto.getEnderecoDTO());
    }

    @Test
    void testBuilder() {
        // Act
        CentroDistribuicaoDTO dto = CentroDistribuicaoDTO.builder()
                .nome("CD Teste")
                .enderecoDTO(EnderecoDTO.builder().build())
                .build();

        // Assert
        assertNotNull(dto);
        assertEquals("CD Teste", dto.getNome());
    }

    @Test
    void testGettersAndSetters() {
        // Arrange
        CentroDistribuicaoDTO dto = new CentroDistribuicaoDTO();
        EnderecoDTO enderecoDTO = EnderecoDTO.builder().build();

        // Act
        dto.setNome("CD Teste");
        dto.setEnderecoDTO(enderecoDTO);

        // Assert
        assertEquals("CD Teste", dto.getNome());
        assertEquals(enderecoDTO, dto.getEnderecoDTO());
    }
}
package ml.pedidos.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItemDTOTest {

    @Test
    public void testIsNomeCentrosDeDistribucoesValidos_comNomesValidos() {
        ItemDTO itemDTO = new ItemDTO();
        List<CentroDistribuicaoItemDTO> centrosDeDistribuicao = new ArrayList<>();
        centrosDeDistribuicao.add(new CentroDistribuicaoItemDTO("Centro A", 10));
        centrosDeDistribuicao.add(new CentroDistribuicaoItemDTO("Centro B", 20));
        itemDTO.setCentrosDeDistribuicao(centrosDeDistribuicao);

        boolean resultado = itemDTO.isNomeCentroDeDistribuicaoValido();

        assertTrue(resultado);
    }

    @Test
    public void testIsNomeCentrosDeDistribucoesValidos_comNomesInvalidos() {
        ItemDTO itemDTO = new ItemDTO();
        List<CentroDistribuicaoItemDTO> centrosDeDistribuicao = new ArrayList<>();
        centrosDeDistribuicao.add(new CentroDistribuicaoItemDTO("Centro A", 10));
        centrosDeDistribuicao.add(new CentroDistribuicaoItemDTO("Centro A", 20));
        itemDTO.setCentrosDeDistribuicao(centrosDeDistribuicao);
        itemDTO.setPreco(1.0);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ItemDTO>> violations = validator.validate(itemDTO);

        assertEquals(1, violations.size());
        assertEquals("Não podem ter centros de distribuções com o mesmo nome", violations.iterator().next().getMessage());
    }

    @Test
    public void testIsNomeCentrosDeDistribucoesValidos_comListaVazia() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setCentrosDeDistribuicao(new ArrayList<>());

        boolean resultado = itemDTO.isNomeCentroDeDistribuicaoValido();

        assertFalse(resultado); // Lista vazia é considerada válida
    }

    @Test
    public void testIsNomeCentrosDeDistribucoesValidos_comListaNula() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setCentrosDeDistribuicao(null);

        boolean resultado = itemDTO.isNomeCentroDeDistribuicaoValido();

        assertFalse(resultado); // Lista nula é considerada válida
    }

    @Test
    public void testIsQuantidadeCentrosMenorQueUm_comQuantidadesValidas() {
        ItemDTO itemDTO = new ItemDTO();
        List<CentroDistribuicaoItemDTO> centrosDeDistribuicao = new ArrayList<>();
        centrosDeDistribuicao.add(new CentroDistribuicaoItemDTO("Centro A", 10));
        centrosDeDistribuicao.add(new CentroDistribuicaoItemDTO("Centro B", 20));
        itemDTO.setCentrosDeDistribuicao(centrosDeDistribuicao);

        boolean resultado = itemDTO.isQuantidadeDeCentroValida();

        assertTrue(resultado);
    }

    @Test
    public void testIsQuantidadeCentrosMenorQueUm_comQuantidadesInvalidas() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setPreco(2.0);
        List<CentroDistribuicaoItemDTO> centrosDeDistribuicao = new ArrayList<>();
        centrosDeDistribuicao.add(new CentroDistribuicaoItemDTO("Centro A", 10));
        centrosDeDistribuicao.add(new CentroDistribuicaoItemDTO("Centro B", 0));
        itemDTO.setCentrosDeDistribuicao(centrosDeDistribuicao);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ItemDTO>> violations = validator.validate(itemDTO);

        assertEquals(1, violations.size());
        assertEquals("A quantidade de itens dos centros de distribuição não pode ser menor que 1", violations.iterator().next().getMessage());
    }

    @Test
    public void testIsQuantidadeCentrosMenorQueUm_comListaVazia() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setCentrosDeDistribuicao(new ArrayList<>());

        boolean resultado = itemDTO.isQuantidadeDeCentroValida();

        assertFalse(resultado); // Lista vazia é considerada válida
    }

    @Test
    public void testIsQuantidadeCentrosMenorQueUm_comListaNula() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setCentrosDeDistribuicao(null);

        boolean resultado = itemDTO.isQuantidadeDeCentroValida();

        assertFalse(resultado); // Lista nula é considerada válida
    }
}

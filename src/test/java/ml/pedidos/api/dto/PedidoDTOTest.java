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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PedidoDTOTest {

    @Test
    public void testIsListaDeItensIdValido_comIdsValidos() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        List<ItemPedidoDTO> itens = new ArrayList<>();
        itens.add(new ItemPedidoDTO("1", "Item 1", 1, 10.0,
                List.of(CentroDistribuicaoItemDTO.builder().build())));
        itens.add(new ItemPedidoDTO("2", "Item 2", 1, 20.0,
                List.of(CentroDistribuicaoItemDTO.builder().build())));
        pedidoDTO.setItens(itens);

        boolean resultado = pedidoDTO.isListaDeItensIdValido();

        assertTrue(resultado);
    }

    @Test
    public void testIsListaDeItensIdValido_comIdsInvalidos() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        List<ItemPedidoDTO> itens = new ArrayList<>();
        itens.add(new ItemPedidoDTO("1", "Item 1", 1, 10.0,
                List.of(CentroDistribuicaoItemDTO.builder().build())));
        itens.add(new ItemPedidoDTO(null, null, null, 0.0, null));
        pedidoDTO.setItens(itens);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PedidoDTO>> violations = validator.validate(pedidoDTO);

        assertEquals(1, violations.size());
        assertEquals("A lista de itens possui IDs inválidos (nulos ou vazios).", violations.iterator().next().getMessage());
    }

    @Test
    public void testIsListaDeItensIdValido_comListaVazia() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setItens(new ArrayList<>());

        boolean resultado = pedidoDTO.isListaDeItensIdValido();

        assertTrue(resultado);
    }

    @Test
    public void testIsListaDeItensIdValido_comListaNula() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setItens(null);

        boolean resultado = pedidoDTO.isListaDeItensIdValido();

        assertTrue(resultado);
    }

    @Test
    public void testNotEmpty_comListaVazia() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setItens(new ArrayList<>());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PedidoDTO>> violations = validator.validate(pedidoDTO);

        assertEquals(1, violations.size());
        assertEquals("A lista de itens não pode ser nula ou vazia.", violations.iterator().next().getMessage());
    }

    @Test
    public void testNotEmpty_comListaNula() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setItens(null);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<PedidoDTO>> violations = validator.validate(pedidoDTO);

        assertEquals(1, violations.size());
        assertEquals("A lista de itens não pode ser nula ou vazia.", violations.iterator().next().getMessage());
    }
}
package ml.pedidos.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import ml.pedidos.api.dto.PedidoDTO;
import ml.pedidos.api.exceptions.ItemNaoEcontradoException;
import ml.pedidos.api.exceptions.PedidoNaoEncontradoException;
import ml.pedidos.api.exceptions.QuantidadeDeItensNaoDisponivelCDException;
import ml.pedidos.api.exceptions.QuantidadeItensPedidosExcedidaException;
import ml.pedidos.api.response.ErrorResponse;
import ml.pedidos.api.response.PedidoResponse;
import ml.pedidos.api.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class PedidoController extends ControllerValidadorDeRequests {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/process")
    @Operation(summary = "Criar um novo pedido", description = "Cria um novo pedido com os itens especificados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Requisição inválida ou quantidade de itens excedida",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PedidoResponse> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        try {
            PedidoDTO pedidoCriado = pedidoService.criarPedido(pedidoDTO);
            return new ResponseEntity<>(PedidoResponse.builder()
                    .pedidoDTO(pedidoCriado)
                    .build(), HttpStatus.CREATED);
        } catch (ItemNaoEcontradoException ex) {
            return new ResponseEntity<>(PedidoResponse.builder()
                    .errorResponse(ErrorResponse.gerarError(ex.getMessage()))
                    .build(), HttpStatus.NOT_FOUND);
        }catch (QuantidadeItensPedidosExcedidaException
                 | QuantidadeDeItensNaoDisponivelCDException ex) {
            return new ResponseEntity<>(PedidoResponse.builder()
                    .errorResponse(ErrorResponse.gerarError(ex.getMessage()))
                    .build(), HttpStatus.BAD_REQUEST);
        }  catch (Exception ex) {
            return new ResponseEntity<>(PedidoResponse.builder()
                    .errorResponse(ErrorResponse.gerarError(ex.getMessage()))
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar um pedido", description = "Consulta um pedido pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PedidoResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PedidoResponse> consultarPedido(@PathVariable String id) {
        try {
            PedidoDTO pedidoCriado = pedidoService.buscarPedidoPorId(id);
            return new ResponseEntity<>(PedidoResponse.builder()
                    .pedidoDTO(pedidoCriado)
                    .build(), HttpStatus.CREATED);
        } catch (PedidoNaoEncontradoException ex) {
            return new ResponseEntity<>(PedidoResponse.builder()
                    .errorResponse(ErrorResponse.gerarError(ex.getMessage()))
                    .build(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(PedidoResponse.builder()
                    .errorResponse(ErrorResponse.gerarError(ex.getMessage()))
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
package ml.pedidos.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.exceptions.CentroDeDistribucaiNaoCadastradoException;
import ml.pedidos.api.exceptions.ItemNaoEcontradoException;
import ml.pedidos.api.exceptions.ItensNaoEncontradoException;
import ml.pedidos.api.response.CentroDeDistribuicaoPorItemReponse;
import ml.pedidos.api.response.ErrorResponse;
import ml.pedidos.api.response.ItemResponse;
import ml.pedidos.api.response.ListaItensResponse;
import ml.pedidos.api.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens")
public class ItemController extends ControllerValidadorDeRequests {

    @Autowired
    private ItemService itemService;

    @PostMapping
    @Operation(summary = "Criar um novo item", description = "Cria um novo item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item criado com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Erro centro de distribuicao não cadastrado",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ItemResponse> criarItem(@Valid @RequestBody ItemDTO itemDTO) {
        try {
            ItemDTO itemCriado = itemService.criarItem(itemDTO);
            return new ResponseEntity<>(ItemResponse.builder()
                    .itemDTO(itemCriado)
                    .build(), HttpStatus.CREATED);
        } catch (CentroDeDistribucaiNaoCadastradoException ex) {
            return new ResponseEntity<>(ItemResponse.builder()
                    .errorResponse(ErrorResponse.gerarError(ex.getMessage()))
                    .build(), HttpStatus.BAD_REQUEST);
        }catch (Exception ex) {
            return new ResponseEntity<>(ItemResponse.builder()
                    .errorResponse(ErrorResponse.gerarError(ex.getMessage()))
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/distributioncenters")
    @Operation(summary = "Consultar centros de distribuição por item",
            description = "Retorna os centros de distribuição que podem entregar o item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Centros de distribuição encontrados com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CentroDeDistribuicaoPorItemReponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Item não encontrado ou nenhum centro de distribuição disponível",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CentroDeDistribuicaoPorItemReponse> consultarCentrosDeDistribuicaoPorItem(@RequestParam String itemId) {
       try{
           ItemDTO itemDTO = itemService.buscarItemPorId(itemId);
           return new ResponseEntity<>(CentroDeDistribuicaoPorItemReponse.gerarCentrosDeDistribuicaoPorItem(itemDTO),
                   HttpStatus.FOUND);
       } catch (ItemNaoEcontradoException ex) {
           return new ResponseEntity<>(CentroDeDistribuicaoPorItemReponse.builder()
                   .errorResponse(ErrorResponse.gerarError(ex.getMessage()))
                   .build(), HttpStatus.NOT_FOUND);
       } catch (Exception ex){
           return new ResponseEntity<>(CentroDeDistribuicaoPorItemReponse.builder()
                   .errorResponse(ErrorResponse.gerarError(ex.getMessage()))
                   .build(), HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @GetMapping
    @Operation(summary = "Listar todos os itens", description = "Retorna uma lista com todos os itens cadastrados, paginada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de itens retornada com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ListaItensResponse> listarTodosOsItens(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            List<ItemDTO> response = itemService.buscarItens(page, size);

            return new ResponseEntity<>(ListaItensResponse.gerarListaDeItensRespose(response),
                    HttpStatus.OK);
        } catch (ItensNaoEncontradoException ex) {
            return new ResponseEntity<>(ListaItensResponse.builder()
                    .errorResponse(ErrorResponse.builder()
                            .mensagem(ex.getMessage())
                            .build())
                    .build(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ListaItensResponse.builder()
                    .errorResponse(ErrorResponse.builder()
                            .mensagem(ex.getMessage())
                            .build())
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

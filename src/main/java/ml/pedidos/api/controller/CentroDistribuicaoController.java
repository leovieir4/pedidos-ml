package ml.pedidos.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import ml.pedidos.api.dto.CentroDistribuicaoDTO;
import ml.pedidos.api.exceptions.CentroDeDistribucaiJaCadastradoException;
import ml.pedidos.api.exceptions.ConsultarEnderecoException;
import ml.pedidos.api.response.CentroDistribuicaoResponse;
import ml.pedidos.api.response.ErrorResponse;
import ml.pedidos.api.response.ItemResponse;
import ml.pedidos.api.response.PedidoResponse;
import ml.pedidos.api.service.CentroDistribuicaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/distributioncenters")
public class CentroDistribuicaoController extends ControllerValidadorDeRequests{


    @Autowired
    private CentroDistribuicaoService centroDistribuicaoService;

    @PostMapping
    @Operation(summary = "Criar um novo centro de distribuicao", description = "Cria um novo centro de distribuicao.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Centro com sucesso",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CentroDistribuicaoResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)) })
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CentroDistribuicaoResponse> criarCentroDistribuicao(@Valid @RequestBody CentroDistribuicaoDTO centroDistribuicaoDTO) {
        try {
            CentroDistribuicaoDTO response = centroDistribuicaoService.criarCentroDistribuicao(centroDistribuicaoDTO);
            return new ResponseEntity<>(CentroDistribuicaoResponse
                    .gerarCentroDistribuicaoResponse(response),
                    HttpStatus.ACCEPTED);
        } catch (ConsultarEnderecoException | CentroDeDistribucaiJaCadastradoException ex) {
            return new ResponseEntity<>(CentroDistribuicaoResponse.builder()
                    .errorResponse(ErrorResponse.gerarError(ex.getMessage()))
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }
}

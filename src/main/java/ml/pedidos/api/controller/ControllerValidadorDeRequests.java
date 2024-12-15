package ml.pedidos.api.controller;

import ml.pedidos.api.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

public class ControllerValidadorDeRequests {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorResponse> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorResponse.gerarError(error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
    }
}

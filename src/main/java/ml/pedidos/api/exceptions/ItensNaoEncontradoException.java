package ml.pedidos.api.exceptions;

public class ItensNaoEncontradoException extends RuntimeException {
    public ItensNaoEncontradoException(String message) {
        super(message);
    }
}

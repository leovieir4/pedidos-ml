package ml.pedidos.api.exceptions;

public class ItemNaoEcontradoException extends RuntimeException {
    public ItemNaoEcontradoException(String message) {
        super(message);
    }
}

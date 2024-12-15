package ml.pedidos.api.exceptions;

public class QuantidadeItensPedidosExcedidaException extends RuntimeException {
    public QuantidadeItensPedidosExcedidaException(String message) {
        super(message);
    }
}
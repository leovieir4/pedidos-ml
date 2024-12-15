package ml.pedidos.api.exceptions;

public class ItemDoPedidoNaoCadastradoException extends RuntimeException {
    public ItemDoPedidoNaoCadastradoException(String message) {
        super(message);
    }
}

package ml.pedidos.api.exceptions;

public class QuantidadeDeItensNaoDisponivelCDException extends RuntimeException{
    public QuantidadeDeItensNaoDisponivelCDException(String message) {
        super(message);
    }
}

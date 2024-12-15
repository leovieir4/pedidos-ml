package ml.pedidos.api.exceptions;

public class CentroDeDistribucaiNaoCadastradoException extends RuntimeException{
    public CentroDeDistribucaiNaoCadastradoException(String message) {
        super(message);
    }
}

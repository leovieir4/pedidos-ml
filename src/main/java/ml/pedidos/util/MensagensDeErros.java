package ml.pedidos.util;

public class MensagensDeErros {
    public static final String MENSAGEM_ERRO_QUANTIDADE_ITENS_EXCEDIDOS =
            "A quantidade de itens excedeu o limite máximo de %s";

    public static final String MENSAGEM_ERRO_IDS_ITENS_NAO_CADASTRADOS =
            "Os seguintes ids dos itens não estão cadastrados: %s";

    public static final String MENSAGEM_ERRO_CONSULTA_CEP =
            "Erro encontrao ao consultar o cep informado: %s";

    public static final String MENSAGEM_ERRO_CENTRO_DISTRIBUICAO_CADASTRADO =
            "O centro de distribuicao com nome %s já esta cadastrado";

    public static final String MENSAGEM_ERRO_CENTRO_DISTRIBUICAO_NAO_CADASTRADO =
            "O centro de distribuicao com nome %s não esta cadastrado";

    public static final String MENSAGEM_ERRO_PEDIDO_NAO_ENCONTRADO =
            "Pedido com id %s não econtrado";

    public static final String MENSAGEM_ERRO_ITEM_NAO_ENCONTRADO =
            "Item com id %s não econtrado";

    public static final String MENSAGEM_ERRO_QUANTIDADE_ITENS_INDISPONIVEL =
            "Item com id %s não tem a quantidade %d de itens disponivel, quantidade maxima disponivel é %s";

    public static final String MENSAGEM_ITENS_NAO_ENCONTRADO =
            "Nenhum item encontrado!";
}

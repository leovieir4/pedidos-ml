package ml.pedidos.api.service;

import ml.pedidos.api.dto.PedidoDTO;
import ml.pedidos.api.exceptions.ItemDoPedidoNaoCadastradoException;
import ml.pedidos.api.exceptions.PedidoNaoEncontradoException;
import ml.pedidos.api.exceptions.QuantidadeItensPedidosExcedidaException;

public interface PedidoService {

    PedidoDTO criarPedido(PedidoDTO pedidoDTO) throws QuantidadeItensPedidosExcedidaException,
            ItemDoPedidoNaoCadastradoException;

    PedidoDTO buscarPedidoPorId(String id) throws PedidoNaoEncontradoException;

}

package ml.pedidos.api.service.impl;

import ml.pedidos.api.dto.*;
import ml.pedidos.api.exceptions.ItemDoPedidoNaoCadastradoException;
import ml.pedidos.api.exceptions.PedidoNaoEncontradoException;
import ml.pedidos.api.exceptions.QuantidadeDeItensNaoDisponivelCDException;
import ml.pedidos.api.exceptions.QuantidadeItensPedidosExcedidaException;
import ml.pedidos.api.mapper.PedidoMapper;
import ml.pedidos.api.service.ItemService;
import ml.pedidos.api.service.PedidoService;
import ml.pedidos.domain.ItemPedido;
import ml.pedidos.domain.Pedido;
import ml.pedidos.repository.PedidoRepository;
import ml.pedidos.util.MensagensDeErros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Override
    public PedidoDTO criarPedido(PedidoDTO pedidoDTO) throws QuantidadeItensPedidosExcedidaException,
            QuantidadeDeItensNaoDisponivelCDException {


        validarQuantidadeItens(pedidoDTO);
        validarIdsDosItensDoPedido(pedidoDTO);

        List<ItemDTO> itens = buscarIntensDoPedido(pedidoDTO.getItens().stream()
                .map(ItemPedidoDTO::getId).toList());

        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);

        PedidoDTO response = pedidoMapper.toDto(pedido, itens);

        verificarQuantidadeDeItensDisponiveisNoCD(response);


        var resultado = pedidoRepository.salvar(pedido);

        response.setId(resultado.getId());

        return response;
    }

    private void verificarQuantidadeDeItensDisponiveisNoCD(PedidoDTO pedidoDTO)
            throws QuantidadeDeItensNaoDisponivelCDException {
        List<ItemPedidoDTO> itens = pedidoDTO.getItens();

        for (int i = 0; i < itens.size(); i ++){
            ItemPedidoDTO itemPedidoDTO = itens.get(i);
            validarQuantidadeDeItensDisponivel(itemPedidoDTO.getQuantidade(),
                    itemPedidoDTO.getCentrosDistribuicaoDTO(), itemPedidoDTO.getId());
        }
    }

    private void validarQuantidadeDeItensDisponivel(int quantidade,
                                                    List<CentroDistribuicaoItemDTO> centros,
                                                    String id){


        int quantidadeMaxima = centros.stream().mapToInt(CentroDistribuicaoItemDTO::getQuantidade).sum();

        if(quantidade > quantidadeMaxima){
            throw new QuantidadeDeItensNaoDisponivelCDException(String.format(
                    MensagensDeErros.MENSAGEM_ERRO_QUANTIDADE_ITENS_INDISPONIVEL, id, quantidade, quantidadeMaxima));
        }

    }

    @Override
    public PedidoDTO buscarPedidoPorId(String id) throws PedidoNaoEncontradoException {

        Pedido pedido = pedidoRepository.buscarPedidoPorId(id);

        if(Objects.isNull(pedido)){
            throw new PedidoNaoEncontradoException(
                    String.format(MensagensDeErros.MENSAGEM_ERRO_PEDIDO_NAO_ENCONTRADO, id));
        }

        List<ItemDTO> itens = buscarIntensDoPedido(pedido.getItens().stream().map(ItemPedido::getId).toList());

        return pedidoMapper.toDto(pedido, itens);
    }



    private List<ItemDTO> buscarIntensDoPedido(List<String> ids){
        return itemService.buscarItensPorId(ids);
    }

    private void validarQuantidadeItens(PedidoDTO pedidoDTO) {
        if (verificarQuantidadeDeItens(pedidoDTO.getItens())) {
            throw new QuantidadeItensPedidosExcedidaException(String.format(
                    MensagensDeErros.MENSAGEM_ERRO_QUANTIDADE_ITENS_EXCEDIDOS,
                    Pedido.NUMERO_MAXIMO_ITENS));
        }
    }

    private void validarIdsDosItensDoPedido(PedidoDTO pedidoDTO) {
        String mensagemErro = verificarIdsDosItens(pedidoDTO.getItens());
        if(Objects.nonNull(mensagemErro)){
            throw new ItemDoPedidoNaoCadastradoException(mensagemErro);
        }
    }

    private String verificarIdsDosItens(List<ItemPedidoDTO> itens){
        List<String> ids = itens.stream().map(ItemPedidoDTO::getId).toList();
        List<String> idsConsulta = itemService.buscarItensPorId(ids).stream().map(ItemDTO::getId).toList();


        return verificarSeExisteDiferencaId(ids, idsConsulta);
    }

    private String verificarSeExisteDiferencaId(List<String> ids, List<String> idsConsulta){
        List<String> resultado = new ArrayList<>(ids);

        resultado.removeAll(idsConsulta);

        return resultado.isEmpty() ? null :
                String.format(MensagensDeErros.MENSAGEM_ERRO_IDS_ITENS_NAO_CADASTRADOS, resultado);
    }

    private boolean verificarQuantidadeDeItens(List<ItemPedidoDTO> itens){
        return itens.stream().mapToInt(ItemPedidoDTO::getQuantidade).sum() > Pedido.NUMERO_MAXIMO_ITENS;
    }



}

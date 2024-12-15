package ml.pedidos.api.service;

import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.exceptions.CentroDeDistribucaiNaoCadastradoException;
import ml.pedidos.api.exceptions.ItemNaoEcontradoException;
import ml.pedidos.api.exceptions.ItensNaoEncontradoException;

import java.util.List;

public interface ItemService {
    ItemDTO criarItem(ItemDTO itemDTO) throws CentroDeDistribucaiNaoCadastradoException;
    List<ItemDTO> buscarItensPorId(List<String> ids);
    ItemDTO buscarItemPorId(String id) throws ItemNaoEcontradoException;
    List<ItemDTO> buscarItens(int page, int size) throws ItensNaoEncontradoException;
}

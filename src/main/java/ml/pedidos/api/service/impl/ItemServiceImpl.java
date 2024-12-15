package ml.pedidos.api.service.impl;

import io.jsonwebtoken.lang.Objects;
import ml.pedidos.api.dto.CentroDistribuicaoItemDTO;
import ml.pedidos.api.dto.ItemDTO;
import ml.pedidos.api.exceptions.CentroDeDistribucaiNaoCadastradoException;
import ml.pedidos.api.exceptions.ItemNaoEcontradoException;
import ml.pedidos.api.exceptions.ItensNaoEncontradoException;
import ml.pedidos.api.mapper.ItemMapper;
import ml.pedidos.api.service.ItemService;
import ml.pedidos.domain.CentroDistribuicao;
import ml.pedidos.domain.Item;
import ml.pedidos.repository.CentroDistribuicaoRepository;
import ml.pedidos.repository.ItemRepository;
import ml.pedidos.util.MensagensDeErros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CentroDistribuicaoRepository centroDistribuicaoRepository;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public ItemDTO criarItem(ItemDTO itemDTO) throws CentroDeDistribucaiNaoCadastradoException{
        validarExistenciaDosCentrosDeDistribuicao(itemDTO);
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.salvar(item);
        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDTO> buscarItensPorId(List<String> ids){
        List<Item> itens = itemRepository.buscarPorIds(ids);

        if(isNull(itens) || itens.isEmpty()){
            throw new ItemNaoEcontradoException(String.format(MensagensDeErros.MENSAGEM_ERRO_ITEM_NAO_ENCONTRADO, ids));
        }

        return itens.stream().map(item -> itemMapper.toDto(item)).toList();
    }

    @Override
    public ItemDTO buscarItemPorId(String id) {
        Item item = itemRepository.buscarItemPorId(id);

        if(isNull(item)){
            throw new ItemNaoEcontradoException(String.format(MensagensDeErros.MENSAGEM_ERRO_ITEM_NAO_ENCONTRADO, id));
        }

        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDTO> buscarItens(int page, int size) {
        List<Item> resultado = itemRepository.buscarTodosPaginado(page, size);

        if (resultado.isEmpty()){
            throw new ItensNaoEncontradoException(MensagensDeErros.MENSAGEM_ITENS_NAO_ENCONTRADO);
        }

        return resultado.stream().map(item -> itemMapper.toDto(item)).toList();
    }

    private void validarExistenciaDosCentrosDeDistribuicao(ItemDTO itemDTO) throws CentroDeDistribucaiNaoCadastradoException{

        List<String> nomes = itemDTO.getCentrosDeDistribuicao().stream().map(CentroDistribuicaoItemDTO::getNome)
                .toList().stream().map(String::toUpperCase).toList();

        List<String> centros = centroDistribuicaoRepository.buscarPorNomes(nomes).stream().map(CentroDistribuicao::getNome).toList();

        if(centros.isEmpty()){
            throw new CentroDeDistribucaiNaoCadastradoException(
                    String.format(MensagensDeErros.MENSAGEM_ERRO_CENTRO_DISTRIBUICAO_NAO_CADASTRADO, nomes));
        }

        String mensagemErro = verificarSeExisteDiferencaNomes(nomes, centros);

        if(nonNull(mensagemErro)){
            throw new CentroDeDistribucaiNaoCadastradoException(mensagemErro);
        }

    }

    private String verificarSeExisteDiferencaNomes(List<String> ids, List<String> idsConsulta){
        List<String> resultado = new ArrayList<>(ids);

        resultado.removeAll(idsConsulta);

        return resultado.isEmpty() ? null :
                String.format(MensagensDeErros.MENSAGEM_ERRO_CENTRO_DISTRIBUICAO_NAO_CADASTRADO, resultado);
    }
}

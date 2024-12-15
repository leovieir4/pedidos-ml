package ml.pedidos.configs.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioServicoDynamodb {

    private String accessKey;
    private String secretKey;
}

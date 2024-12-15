# Sistema de Gerenciamento de Pedidos 📦

## Descrição 📝

Este projeto implementa uma API REST para gerenciar pedidos de um sistema de e-commerce. A API permite criar e consultar pedidos, gerenciar itens e centros de distribuição, além de determinar o centro de distribuição ideal para cada item do pedido, utilizando Spring Boot, Java e Amazon DynamoDB.

## Funcionalidades 🚀

* Criar novos pedidos com múltiplos itens (até 100 itens por pedido).
* Consultar pedidos existentes, com detalhes dos itens e centros de distribuição.
* Gerenciar o estoque de produtos.
* Determinar o centro de distribuição ideal para cada item do pedido, com base na disponibilidade de estoque e na proximidade do cliente.
* **Gerenciar itens:** Criar novos itens e consultar centros de distribuição por item.
* **Listar itens:** Obter uma lista paginada de todos os itens cadastrados.
* **Gerenciar centros de distribuição:** Criar novos centros de distribuição, com integração com a API ViaCEP para preenchimento automático do endereço.

## Tecnologias Utilizadas 💻

* Linguagem de programação: Java (versão 21)
* Framework web: Spring Boot (versão 3.4.0)
* Gerenciador de dependências: Gradle (versão 8.5)
* Banco de dados: Amazon DynamoDB
* Containerização: Docker
* Orquestração: Kubernetes
* Autenticação: Bearer Token JWT
* Integração: ViaCEP API
* Outras tecnologias:
    * Spring Data JPA
    * Spring Security
    * Spring Boot Actuator (para monitoramento)
    * OAuth 2.0 Resource Server (para segurança)
    * Swagger (para documentação da API)
    * AWS SDK for Java (para integração com o DynamoDB e Secrets Manager)
    * JWT (para autenticação)
    * MapStruct (para mapeamento de objetos)
    * Lombok (para simplificar o código)

## Instalação 🔧

1. Clone o repositório: `git clone https://github.com/leovieir4/pedidos-ml`
2. Navegue até o diretório do projeto: `cd pedidos-ml`

## Execução ▶️

### Localmente com Docker

1. Construa a imagem Docker: `docker build -t pedidos-ml .`
2. Execute o container Docker: `docker run -p 8080:8080 pedidos-ml`
3. A API estará disponível em: `http://localhost:8080` (ou em outra porta que você tenha configurado)
4. A documentação da API estará disponível em: `http://localhost:8080/swagger-ui/index.html`

## 🐳 Executar com Docker Compose:

Para executar a aplicação via Docker Compose:

1.  Clone o repositório do projeto.
2.  Navegue até o diretório raiz do projeto no terminal.
3.  Execute o comando `docker-compose up -d --build `. Isso construirá a imagem Docker, caso ainda não exista, e iniciará o contêiner em segundo plano.

## 🔎 Visualizar os logs:

Para visualizar os logs da aplicação em tempo real, execute o seguinte comando:

```bash
docker-compose logs -f api-tokens
```

## 🛑 Parar a aplicação (Docker):

Para parar a aplicação e remover os contêineres, execute o seguinte comando:

```bash
docker-compose down
```

## 🔄 Reconstruir a imagem (Docker):

### Em um cluster Kubernetes

1. Crie o segredo JWT: `kubectl apply -f k8s/secret-jwt.yaml`
2. Faça o deploy da aplicação: `kubectl apply -f k8s/deployment.yaml`
3. Exponha o serviço: `kubectl apply -f k8s/service.yaml`

### Localmente com Gradle 🔨

1. **Limpe e construa o projeto:** `./gradlew clean build`
2. **Execute a aplicação:** `./gradlew bootRun`
3. A API estará disponível em: `http://localhost:8080` (ou em outra porta que você tenha configurado)
4. A documentação da API estará disponível em: `http://localhost:8080/swagger-ui/index.html`


## Gradle 🐘

O projeto utiliza o Gradle como gerenciador de dependências e ferramenta de build. O arquivo `build.gradle` define as dependências do projeto, plugins e tarefas para construir, testar e executar a aplicação.

Para construir o projeto, execute o comando `./gradlew build`.

Para executar a aplicação localmente, execute o comando `./gradlew bootRun`.

## Docker 🐳

O projeto utiliza um Dockerfile de múltiplos estágios para construir e executar a aplicação.

* **Estágio de build:**
    * Utiliza a imagem `ubuntu:latest` como base.
    * Instala o Java JDK 21, unzip e wget.
    * Faz o download e instala o Gradle 8.5.
    * Copia o código-fonte da aplicação.
    * Executa o comando `gradle clean build` para construir o projeto.
* **Estágio de runtime:**
    * Utiliza a imagem `ubuntu:latest` como base.
    * Instala o Java JRE 21.
    * Copia o arquivo JAR gerado no estágio de build.
    * Define as permissões do arquivo JAR.
    * Copia as credenciais da AWS (se necessário).
    * Expõe a porta 8080.
    * Define o comando de entrada para executar a aplicação com o perfil `not_local`.

## Kubernetes 🚢

A pasta `k8s` contém os arquivos de configuração para deploy da aplicação em um cluster Kubernetes:

* **`deployment.yaml`:** Define o deployment da aplicação, incluindo a imagem Docker, número de réplicas, portas, variáveis de ambiente e recursos.
* **`service.yaml`:** Define o serviço Kubernetes para expor a aplicação internamente ou externamente.
* **`secret-jwt.yaml`:** Define o segredo Kubernetes para armazenar a chave secreta utilizada para assinar o JWT.

## Autenticação 🔐

A API utiliza Bearer Token JWT para autenticação. Você precisa obter o token de uma API de autenticação externa e incluí-lo no header `Authorization` de cada requisição.

## Local para adicionra o token gerado na API de autenticação

![Adicionar token](https://i.ibb.co/s5ccV70/Captura-de-tela-2024-12-15-185854.png)

**Swagger da API de Geração de Token:**

[Link para a documentação Swagger da API de Geração de Token](http://13.59.156.55:8081/swagger-ui/index.html)

## Modelo de Dados 📊

O modelo de dados da aplicação é composto pelas seguintes entidades:

* **`CentroDistribuicao`:** Representa um centro de distribuição, com atributos como nome e endereço.
* **`CentroDistribuicaoItem`:** Representa a relação entre um centro de distribuição e um item, armazenando a quantidade do item disponível naquele centro.
* **`Item`:** Representa um item do catálogo, com atributos como nome, preço e uma lista de `CentroDistribuicaoItem` que indica onde o item está disponível e em quais quantidades.
* **`Endereco`:** Representa um endereço, com atributos como CEP, número, cidade, estado, bairro, UF e logradouro.
* **`ItemPedido`:** Representa um item em um pedido, com atributos como ID do item e quantidade.
* **`Pedido`:** Representa um pedido, com uma lista de `ItemPedido`.

**Associações:**

* **`CentroDistribuicao` 1:1 `Endereco`:** Um centro de distribuição possui um endereço.
* **`Item` 1:N `CentroDistribuicaoItem`:** Um item pode estar disponível em vários centros de distribuição, cada um com sua própria quantidade em estoque.
* **`Pedido` 1:N `ItemPedido`:** Um pedido contém vários itens (até 100 itens).

## Diagramas C4 Model 📈


* **Gerenciamento de Pedidos:**

  ![Gerenciamento de pedidos](https://i.ibb.co/gt85vXc/Captura-de-tela-2024-12-15-164442.png)

* **Gerenciamento de Itens:**

  ![Gerenciamento de intens](https://i.ibb.co/y07k3Gm/Captura-de-tela-2024-12-15-164828.png)

* **Gerenciamento de Centros de Distribuição:**

  ![Gerenciamento de CDS](https://i.ibb.co/w4DhCTn/Captura-de-tela-2024-12-15-165044.png)


## Endpoints da API 🌐

### Pedidos

* `POST /orders/process`: Criar um novo pedido.
    * Request Body:
    ```json
    {
      "itens": [
        {
          "itemId": "123",
          "quantidade": 2
        },
        {
          "itemId": "456",
          "quantidade": 1
        }
      ]
    }
    ```
    * Response Codes:
        * `201 Created`: Pedido criado com sucesso.
        * `400 Bad Request`: Requisição inválida ou quantidade de itens excedida.
        * `500 Internal Server Error`: Erro interno do servidor.
* `GET /orders/{id}`: Consultar um pedido pelo ID.
    * Path Variables:
        * `id`: ID do pedido.
    * Response Codes:
        * `200 OK`: Pedido encontrado com sucesso.
        * `404 Not Found`: Pedido não encontrado.
        * `500 Internal Server Error`: Erro interno do servidor.

### Itens

* `POST /itens`: Criar um novo item.
    * Request Body:
    ```json
    {
      "nome": "Nome do item",
      "descricao": "Descrição do item",
      "preco": 10.0,
      "centrosDeDistribuicao": [
        {
          "nome": "CD1",
          "quantidade": 10
        },
        {
          "nome": "CD2",
          "quantidade": 5
        }
      ]
    }
    ```
    * Response Codes:
        * `201 Created`: Item criado com sucesso.
        * `400 Bad Request`: Erro centro de distribuição não cadastrado.
        * `500 Internal Server Error`: Erro interno do servidor.
* `GET /itens/distributioncenters`: Consultar centros de distribuição por item.
    * Query Parameters:
        * `itemId`: ID do item.
    * Response Codes:
        * `200 OK`: Centros de distribuição encontrados com sucesso.
        * `404 Not Found`: Item não encontrado ou nenhum centro de distribuição disponível.
        * `500 Internal Server Error`: Erro interno do servidor.
* `GET /itens`: Listar todos os itens.
    * Query Parameters:
        * `page`: Número da página (padrão: 1).
        * `size`: Tamanho da página (padrão: 10).
    * Response Codes:
        * `200 OK`: Lista de itens retornada com sucesso.
        * `500 Internal Server Error`: Erro interno do servidor.

### Centros de Distribuição

* `POST /distributioncenters`: Criar um novo centro de distribuição.
    * Request Body:
    ```json
    {
      "nome": "Nome do CD",
      "endereco": {
        "cep": "00000-000" 
      }
    }
    ```
    * Response Codes:
        * `201 Created`: Centro de distribuição criado com sucesso.
        * `400 Bad Request`: Erro ao consultar endereço ou centro de distribuição já cadastrado.
        * `500 Internal Server Error`: Erro interno do servidor.

## Integração com a ViaCEP 📍

A API se integra com a ViaCEP para preencher automaticamente as informações de endereço ao cadastrar um novo centro de distribuição. Basta informar o CEP no atributo `endereco.cep` do request body.

**Observação:** A API da ViaCEP pode apresentar instabilidades. Caso ocorra algum problema durante a consulta, tente novamente mais tarde.

## Exemplo de Uso ⌨️

**Criar um novo centro de distribuição:**

```bash
curl -X POST -H "Content-Type: application/json" -H "Authorization: Bearer <seu_token_de_acesso>" -d '{
  "nome": "Nome do CD",
  "endereco": {
    "cep": "00000-000"
  }
}' http://localhost:8080/distributioncenters
```

## 📖 Documentação da API (Swagger):
[Documentação](http://13.59.156.55:8080/swagger-ui/index.html)

## 🚀 Deploy na AWS:
[Link da API](http://13.59.156.55:8080)

## 🧑‍💻 Autor:
Leonardo Vieira da Silva

## 📞 Contato:

Email: vieraleonardosilva@gmail.com

Telefone: (11) 994419472
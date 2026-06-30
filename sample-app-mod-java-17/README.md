# ModResorts — Java 17 (Open Liberty)

## Visão geral
Esta é a versão **Java 17** da aplicação de exemplo ModResorts, **migrada a partir da versão base em Java 8** (`sample-app-mod`) e, em seguida, **modernizada para remover as dependências exclusivas do WebSphere**.

Continua sendo uma aplicação **Jakarta/Java EE baseada em servlets**, empacotada como **WAR** e executada sobre o **Open Liberty**, mas agora:

- **não depende mais do `was_public.jar`** (APIs proprietárias do WebSphere);
- APIs que estavam **quebradas foram corrigidas** (clima e "upper").

A aplicação preserva o comportamento funcional do ModResorts (consulta de clima por cidade, verificação de disponibilidade, etc.).

## O que mudou nesta modernização

| Aspecto | Antes (legado / quebrado) | Agora |
|--------|---------------------------|-------|
| API `/resorts/upper` | `com.ibm.websphere.servlet.response.ResponseUtils` (WebSphere, `was_public.jar`) | Escape de HTML portável, sem dependências externas |
| API `/resorts/weather` | Falhava: MBean com `impact` inválido (`10`) quebrava o `init()` do servlet | `DMBeanUtils` normaliza `impact` inválido para `UNKNOWN` |
| API `/resorts/weather` | Falhava: dados `data/*.json` não estavam no classpath | Arquivos de clima copiados para `src/main/resources/data/` (classpath) |
| Dependência `was_public.jar` | Necessária | **Removida** do `pom.xml` |
| Runtime | WebSphere / Liberty | **Open Liberty** (`server.xml` enxuto: `servlet`, `jsp`, `cdi`, `ejbLite`, `jndi`) |
| Empacotamento | WAR | WAR (mantido) |

> **Nota:** a camada HTTP continua em servlets `javax.servlet.*` e o bean de clientes [`ModResortsCustomerInformation`](src/main/java/com/acme/modres/db/ModResortsCustomerInformation.java) permanece **intencionalmente na forma legada** (`@Singleton @Startup`, sem banco de dados) — este projeto representa a etapa "Java 8 → Java 17 sem lock-in de fornecedor". A migração de framework (JAX-RS/Spring/Quarkus) e de persistência é demonstrada nos outros projetos do repositório.

## Requisitos
- Java 17
- Maven 3.9+
- (O Open Liberty é baixado automaticamente pelo Maven; **não** é necessário WebSphere nem `was_public.jar`.)

## Build
```
mvn clean package
```

## Executando
Em modo de desenvolvimento (Open Liberty via plugin Maven), a partir da pasta do projeto:
```
mvn liberty:run
```
A aplicação fica disponível em **http://localhost:9080/resorts/**.

> O `Containerfile` na raiz permite construir uma imagem do servidor e rodar a aplicação em contêiner.

## Endpoints
| Método | Caminho | Descrição |
|--------|---------|-----------|
| GET | `/resorts/` | Página inicial (SPA estática) |
| GET | `/resorts/weather?selectedCity=<cidade>` | Dados de clima da cidade selecionada |
| GET | `/resorts/availability?date=MM/dd/yyyy` | Disponibilidade para a data |
| GET | `/resorts/upper?input=<texto>` | Converte o texto para maiúsculas (com escape de HTML) |
| GET | `/resorts/welcome` | Mensagem de boas-vindas |
| GET | `/resorts/logout` | Encerra a sessão |

Cidades suportadas: `Paris`, `Las_Vegas`, `San_Francisco`, `Miami`, `Cork`, `Barcelona`.

## Testes (Playwright)
As páginas e APIs foram validadas de ponta a ponta com Playwright (Chromium), com o servidor rodando em `mvn liberty:run`:

- carregamento da página inicial e presença do seletor de cidades;
- seleção de cidade no UI dispara `/resorts/weather` (200) e popula o painel de clima;
- `/resorts/availability`, `/resorts/upper` e `/resorts/welcome` retornam 200 com o conteúdo esperado.

Capturas de tela: [`docs/screenshot-home.png`](docs/screenshot-home.png) e [`docs/screenshot-weather.png`](docs/screenshot-weather.png).

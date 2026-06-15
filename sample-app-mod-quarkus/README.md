# ModResorts — Quarkus

## Visão geral
Esta é a versão **Quarkus** da aplicação de exemplo ModResorts, **migrada a partir da versão IBM WebSphere / Java EE** (`sample-app-mod-java-17`).

Diferente da aplicação original, esta versão **não depende mais do IBM WebSphere Application Server (nem do Liberty)**. Todas as APIs específicas do WebSphere foram removidas e substituídas por equivalentes portáveis, e a aplicação agora roda como um serviço autônomo sobre o [Quarkus 3](https://quarkus.io), executando em Java 17.

A aplicação preserva o comportamento funcional do ModResorts (consulta de clima por cidade, verificação de disponibilidade, etc.), mas com uma stack moderna e cloud-native.

## O que mudou na migração (a partir do WebSphere)

| Aspecto | Original (WebSphere / Java EE) | Esta versão (Quarkus) |
|--------|-------------------------------|------------------------|
| Runtime | IBM WebSphere Application Server / Liberty | Quarkus 3 (`quarkus-rest` + `quarkus-arc`) |
| Empacotamento | WAR implantado no servidor de aplicações | JAR executável autônomo |
| Camada HTTP | Servlets `HttpServlet` + `web.xml` | Recursos **Jakarta REST (JAX-RS)** com `@Path` / `@GET` / `@QueryParam` |
| Filtros de servlet | `FirstFilter` / `SecondFilter` mapeados no `web.xml` | Comportamento incorporado ao endpoint `/resorts/welcome` |
| Namespaces | `javax.servlet.*`, `javax.inject.*` | `jakarta.ws.rs.*`, `jakarta.inject.*` |
| `ModResortsCustomerInformation` | EJB `@Singleton @Startup` | CDI `@ApplicationScoped` |
| Registro do MBean (`AppInfo`) | Em `WeatherServlet#init()` | Bean CDI no ciclo de vida do Quarkus (`@Observes StartupEvent`) |
| API exclusiva do WebSphere | `com.ibm.websphere.servlet.response.ResponseUtils` (dependência `was_public.jar`) | método portável de escape de HTML |
| Conteúdo estático | `WebContent/` (raiz do WAR) | `src/main/resources/META-INF/resources/` |
| Dependência `was_public.jar` | Necessária (instalada manualmente do WebSphere) | **Removida** |

> **Importante:** esta versão **não requer** o `was_public.jar` nem qualquer instalação do WebSphere/Liberty para compilar ou executar.

## Requisitos
- Java 17
- Maven 3.9+

## Executando em modo de desenvolvimento
```
mvn quarkus:dev
```
A aplicação fica disponível em http://localhost:8080 (com live coding habilitado).

## Build e execução como JAR
```
mvn clean package
java -jar target/quarkus-app/quarkus-run.jar
```

## Testes
Os testes de integração (`@QuarkusTest` + REST Assured) sobem a aplicação e exercitam os endpoints:
```
mvn test
```

## Endpoints
| Método | Caminho | Descrição |
|--------|---------|-----------|
| GET | `/` | Página inicial (SPA estática) |
| GET | `/resorts/weather?selectedCity=<cidade>` | Dados de clima da cidade selecionada |
| GET | `/resorts/availability?date=MM/dd/yyyy` | Disponibilidade para a data |
| GET | `/resorts/upper?input=<texto>` | Converte o texto para maiúsculas (com escape de HTML) |
| GET | `/resorts/welcome` | Mensagem de boas-vindas (passa pelos filtros) |

Cidades suportadas: `Paris`, `Las_Vegas`, `San_Francisco`, `Miami`, `Cork`, `Barcelona`.

## Captura de tela
![ModResorts rodando em Quarkus](docs/screenshot-home.png)

# ModResorts — Java 17 (WebSphere Liberty)

## Visão geral
Esta é a versão **Java 17** da aplicação de exemplo ModResorts, **migrada a partir da versão base em Java 8** (`sample-app-mod`).

Diferente da versão base — uma aplicação Java EE tradicional pensada para o IBM WebSphere Application Server — esta versão foi atualizada para **Java 17** e modernizada para rodar sobre o **WebSphere Liberty**. Ainda se trata de uma aplicação **Java EE baseada em servlets**, empacotada como **WAR**, mas com a linguagem, as dependências e a configuração de runtime atualizadas.

A aplicação preserva o comportamento funcional do ModResorts (consulta de clima por cidade, verificação de disponibilidade, etc.).

## O que mudou na migração (a partir do Java 8)

| Aspecto | Versão base (Java 8) | Esta versão (Java 17) |
|--------|----------------------|------------------------|
| Versão Java | Java 8 (1.8) | **Java 17** |
| Compilação | `maven.compiler` 1.8 | `maven.compiler` 17 |
| Runtime alvo | WebSphere Application Server tradicional | **WebSphere Liberty** (`src/main/liberty/config/server.xml`) |
| Camada HTTP | Servlets `HttpServlet` + `web.xml` | Servlets `HttpServlet` + `web.xml` (mantido) |
| Namespaces | `javax.servlet.*`, `javax.inject.*` | `javax.servlet.*`, `javax.inject.*` (mantido) |
| APIs auxiliares | `javaee-api` | `javaee-api` + `jakarta.annotation-api`, `jakarta.inject-api` |
| Ajustes de código | — | Correções para compatibilidade com Java 17 |
| Empacotamento | WAR | WAR (mantido) |
| Dependência `was_public.jar` | Necessária | Necessária (mantida) |

> **Importante:** esta versão **ainda depende do `was_public.jar`** (APIs exclusivas do WebSphere) e de um servidor de aplicações compatível (WebSphere Liberty). Veja [Dependências do WebSphere](#dependências-do-websphere).

## Requisitos
- Java 17
- Maven 3.9+
- WebSphere Liberty (ou outro container de servlets, ex.: Tomcat 9 para testes locais)
- `was_public.jar` instalado no repositório Maven local

### Dependências do WebSphere
O `pom.xml` referencia a dependência `was_public`. Para compilar, o jar `was_public.jar` e seu `pom` precisam estar disponíveis no repositório Maven. Eles podem ser encontrados na instalação do WebSphere (ex.: `/opt/WebSphere/AppServer/dev`):

```
mvn install:install-file -Dfile=<local>/was_public.jar -DpomFile=<local>/was_public-9.0.0.pom
```

## Build
Aplicação Maven de módulo único; o WAR é gerado com:
```
mvn clean package
```

## Executando
Faça o build do WAR e implante-o no WebSphere Liberty (a configuração `server.xml` já está incluída em `src/main/liberty/config/`). Em modo de desenvolvimento, com o Liberty Tools:
```
mvn io.openliberty.tools:liberty-maven-plugin:dev
```
Por padrão a aplicação fica disponível em http://localhost:9080/resorts/.

> O `Containerfile` na raiz permite construir uma imagem WebSphere Liberty e rodar a aplicação em contêiner.

## Endpoints
| Método | Caminho | Descrição |
|--------|---------|-----------|
| GET | `/` | Página inicial (SPA estática) |
| GET | `/resorts/weather?selectedCity=<cidade>` | Dados de clima da cidade selecionada |
| GET | `/resorts/availability?date=MM/dd/yyyy` | Disponibilidade para a data |
| GET | `/resorts/upper?input=<texto>` | Converte o texto para maiúsculas |
| GET | `/resorts/welcome` | Mensagem de boas-vindas (passa pelos filtros) |
| GET | `/logout` | Encerra a sessão |

Cidades suportadas: `Paris`, `Las_Vegas`, `San_Francisco`, `Miami`, `Cork`, `Barcelona`.

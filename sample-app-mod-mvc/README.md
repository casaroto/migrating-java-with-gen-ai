# ModResorts — Spring Web MVC

## Visão geral
Esta é a versão **Spring Web MVC** da aplicação de exemplo ModResorts, **migrada do modelo Java EE baseado em servlets para Spring MVC**, rodando em **Java 17**.

A migração foi concluída: **todos os servlets e filtros legados foram removidos** e cada endpoint passou a ser um **controller Spring** (`@RestController` / `@Controller`). As requisições são tratadas pelo `DispatcherServlet` do Spring, montado sob o caminho `/mvc/*`.

A aplicação continua empacotada como **WAR**, rodando em um container de servlets (Tomcat 9 / WebSphere Liberty).

## O que mudou na migração (Java EE → Spring MVC)

| Aspecto | Original (Java EE / servlets) | Esta versão (Spring MVC) |
|--------|-------------------------------|---------------------------|
| Camada HTTP | Servlets `HttpServlet` | Controllers Spring (`@RestController` / `@Controller`) |
| Roteamento | `@WebServlet` / `web.xml` | `DispatcherServlet` em `/mvc/*` + `@GetMapping` / `@PostMapping` |
| Filtros | `FirstFilter` / `SecondFilter` (servlet, via `web.xml`) | Comportamento incorporado ao `WelcomeController` |
| Logout | `LogoutServlet` (`request.logout()` + `sendRedirect`) | `LogoutController` (`return "redirect:/login.jsp"`) |
| Injeção de dependências | `@Inject` (CDI) | `@Autowired` (Spring) |
| Framework | Java EE | `spring-webmvc`, `spring-context` |
| Escape de HTML (upper) | API do WebSphere | `commons-text` (`StringEscapeUtils`) |
| Namespaces | `javax.servlet.*` | `javax.servlet.*` (mantido) |
| Empacotamento | WAR | WAR (mantido) |

> **Importante:** a aplicação **não contém mais servlets nem filtros** — toda a camada HTTP é Spring MVC. Ainda é empacotada como **WAR** e implantada em um container de servlets; o `web.xml` agora declara apenas o `DispatcherServlet`. A dependência `was_public.jar` permanece declarada no `pom.xml` (não é mais usada pelo código). Veja [Dependências do WebSphere](#dependências-do-websphere).

> **Observação:** apesar de uma mensagem de exemplo citar "Spring Boot", esta aplicação usa **Spring Web MVC** sobre um container de servlets — **não** é Spring Boot.

## Controllers
| Controller | Substitui | Caminho (`/mvc/*`) |
|-----------|-----------|--------------------|
| `WeatherController` | `WeatherServlet` | `/mvc/weather` |
| `AvailabilityCheckerController` | `AvailabilityCheckerServlet` | `/mvc/availability` |
| `UpperController` | `UpperServlet` | `/mvc/upper` |
| `WelcomeController` | `WelcomeServlet` + `FirstFilter` + `SecondFilter` | `/mvc/welcome` |
| `LogoutController` | `LogoutServlet` | `/mvc/logout` |
| `HelloController` | — (endpoint de verificação) | `/mvc/test` |

## Requisitos
- Java 17
- Maven 3.9+
- Container de servlets: Tomcat 9 ou WebSphere Liberty
- `was_public.jar` instalado no repositório Maven local (ainda declarado no `pom.xml`)

### Dependências do WebSphere
O `pom.xml` ainda referencia a dependência `was_public`. Para compilar, instale o jar no repositório Maven (encontrado na instalação do WebSphere, ex.: `/opt/WebSphere/AppServer/dev`):

```
mvn install:install-file -Dfile=<local>/was_public.jar -DpomFile=<local>/was_public-9.0.0.pom
```

## Build
```
mvn clean package
```

## Executando
Faça o build do WAR e implante-o em um container de servlets. O script `run.sh` da raiz sobe a aplicação em um Tomcat 9 local:
```
./run.sh
```
Alternativamente, use o `Containerfile` para construir uma imagem WebSphere Liberty e rodar em contêiner.

## Endpoints

Todos os endpoints são controllers Spring MVC servidos pelo `DispatcherServlet` (prefixo `/mvc/`). Considerando o context root `/resorts`, o caminho completo fica, por exemplo, `/resorts/mvc/weather`.

| Método | Caminho | Descrição |
|--------|---------|-----------|
| GET/POST | `/mvc/weather?selectedCity=<cidade>` | Dados de clima da cidade (`WeatherController`) |
| GET/POST | `/mvc/availability?date=MM/dd/yyyy` | Disponibilidade para a data (`AvailabilityCheckerController`) |
| GET | `/mvc/upper?input=<texto>` | Converte o texto para maiúsculas, com escape de HTML (`UpperController`) |
| GET | `/mvc/welcome?user=<nome>` | Mensagem de boas-vindas (`WelcomeController`) |
| GET | `/mvc/logout` | Encerra a sessão e redireciona para `login.jsp` (`LogoutController`) |
| GET | `/mvc/test` | Endpoint de verificação (`HelloController`) |

Cidades suportadas: `Paris`, `Las_Vegas`, `San_Francisco`, `Miami`, `Cork`, `Barcelona`.

# Migrando Java Legado com IA Generativa

Este repositório reúne projetos de exemplo usados para demonstrar, na prática, **como migrar aplicações Java legado** para versões modernas da plataforma e para arquiteturas mais atuais — apoiando-se em **Inteligência Artificial Generativa** para acelerar a jornada de modernização.

Todos os projetos partem de uma mesma aplicação base (a aplicação de exemplo **ModResorts**) e mostram diferentes estágios da migração, permitindo comparar o código antes e depois de cada etapa.

> [!IMPORTANT]
> **Aviso de propriedade e licenciamento**
>
> O código-fonte destes projetos é **propriedade da IBM**. Quem desejar usar, copiar, modificar ou distribuir este material **deve obrigatoriamente ler e aplicar os termos dos arquivos de licença** presentes em cada um dos diretórios:
> - [`sample-app-mod/LICENSE`](sample-app-mod/LICENSE)
> - [`sample-app-mod-java-17/LICENSE`](sample-app-mod-java-17/LICENSE)
> - [`sample-app-mod-mvc/LICENSE`](sample-app-mod-mvc/LICENSE)
> - [`sample-app-mod-quarkus/LICENSE`](sample-app-mod-quarkus/LICENSE)
>
> Cada diretório possui seu próprio arquivo `LICENSE`. Leia e respeite essas licenças antes de qualquer utilização.

## Projetos

| # | Projeto | Versão Java | Stack / Framework | Empacotamento | Descrição |
|---|---------|-------------|-------------------|---------------|-----------|
| 1 | [`sample-app-mod`](sample-app-mod/) | Java 8 (1.8) | Java EE (`javaee-api`) | WAR | **Projeto base.** Aplicação legada original rodando em Java 8, ponto de partida da migração. |
| 2 | [`sample-app-mod-java-17`](sample-app-mod-java-17/) | Java 17 | Java EE / Jakarta (`jakarta.annotation-api`, `jakarta.inject-api`) | WAR | Mesma aplicação **migrada para Java 17**, atualizando linguagem, dependências e APIs (Java EE → Jakarta). |
| 3 | [`sample-app-mod-mvc`](sample-app-mod-mvc/) | Java 17 | Spring (`spring-webmvc`, `spring-context`) | WAR | Versão **modernizada com Spring**, rodando em Java 17, demonstrando a migração para um framework moderno. |
| 4 | [`sample-app-mod-quarkus`](sample-app-mod-quarkus/) | Java 17 | Quarkus 3 (`quarkus-rest`, `quarkus-arc`) | JAR executável | Versão **modernizada com Quarkus**, rodando em Java 17, demonstrando a migração para um framework cloud-native (endpoints REST com Jakarta REST/JAX-RS, CDI via Arc, namespace Jakarta). |

## Sobre este repositório

Os exemplos deste repositório têm como foco demonstrar, de forma prática, a jornada de migração de uma aplicação Java legado — partindo de uma base em Java 8 até versões modernas da plataforma e arquiteturas mais atuais. A partir de uma mesma aplicação (ModResorts), cada projeto representa um estágio dessa evolução, permitindo comparar o código em diferentes momentos da modernização.

Os projetos ilustram por que migrar aplicações Java legado deixou de ser apenas uma melhoria técnica e passou a ser uma necessidade para segurança, inovação e continuidade do negócio. O fim do suporte público do Java 8 e a dependência de versões antigas aumentam riscos operacionais, custos de manutenção e vulnerabilidades, além de dificultarem a adoção de arquiteturas modernas, cloud-native e práticas DevSecOps.

Os exemplos também servem de base para mostrar como a inteligência artificial generativa pode acelerar jornadas de modernização, apoiando a análise de código legado, a identificação de dependências, a refatoração, a criação de testes, a documentação, a migração de frameworks, a revisão de segurança e a geração de pipelines automatizados.

Essa jornada pode utilizar tanto ferramentas pagas quanto gratuitas — incluindo IDEs com assistentes de IA, modelos locais, soluções Open Source e plataformas corporativas — permitindo que as empresas escolham o melhor caminho conforme seu nível de maturidade, orçamento e requisitos de governança.

## Itens legados do projeto base (`sample-app-mod`)

O projeto base [`sample-app-mod`](sample-app-mod/) concentra as características de uma aplicação Java legada típica — exatamente os pontos que a jornada de modernização (projetos 2 a 4) busca eliminar ou substituir. A tabela abaixo lista esses itens, por que são considerados legados/problemáticos e para o que costumam ser migrados.

| Item legado | Onde aparece | Por que é um problema | Para onde migrar |
|-------------|--------------|-----------------------|------------------|
| **Java 8** | `pom.xml` (`maven.compiler` 1.8) | Fim do suporte público; sem recursos modernos da linguagem e da JVM; riscos de segurança | Java 17+ (LTS) |
| **Servlets Java EE** (`HttpServlet`, `@WebServlet`, `web.xml`) | `WeatherServlet`, `AvailabilityCheckerServlet`, `UpperServlet`, `WelcomeServlet`, `LogoutServlet` | API verbosa e de baixo nível, acoplada ao container de servlets | Spring MVC ou Jakarta REST (JAX-RS) |
| **Filtros de servlet** (`javax.servlet.Filter`) | `FirstFilter`, `SecondFilter` | Acoplados ao `web.xml`/container; difíceis de testar isoladamente | Interceptors/filtros do framework moderno |
| **Namespace `javax.*`** | Todo o código (`javax.servlet`, `javax.inject`) | Anterior ao Jakarta EE 9+; incompatível com runtimes modernos | Namespace `jakarta.*` |
| **APIs exclusivas do WebSphere** (`com.ibm.websphere.servlet.response.ResponseUtils`, `was_public.jar`) | `UpperServlet`, `pom.xml` | Lock-in de fornecedor; só compila/roda no WebSphere; jar instalado manualmente | Bibliotecas portáveis (ex.: `commons-text`, utilitários próprios) |
| **EJB 3.2** (`@Singleton`, `@Startup` — via `javaee-api` 7.0 / Java EE 7) | `db/ModResortsCustomerInformation` | Modelo de componentes pesado e legado; exige container EE completo | CDI (`@ApplicationScoped`) |
| **JNDI** (`InitialContext`) | `WeatherServlet`, `AvailabilityCheckerServlet` | Dependente de configuração do servidor de aplicações | Injeção de configuração/dependências do framework |
| **JMX `DynamicMBean`** com `ops.json` inválido | `mbean/AppInfo`, `mbean/DMBeanUtils`, `ops.json` | Valor de `impact` inválido (10) quebra a inicialização fora do WebSphere; bug latente | Métricas/observabilidade modernas (ex.: Micrometer) |
| **Segurança FORM gerida pelo container** | `web.xml` (`login-config`), `login.jsp` | Acoplada ao servidor; já desativada no demo | Segurança do framework / IdP externo (OIDC) |
| **`TrustManager` que confia em tudo** | `security/FakeX509TrustManager`, `security/SSLUtils` | Desativa a validação de certificados TLS — falha grave de segurança | Validação TLS padrão da JVM |
| **`SecurityManager`** | `security/Service` | Depreciado e marcado para remoção (JEP 411) | Remover; usar políticas de SO/container |
| **JDBC manual com `DataSource` via JNDI** | `db/ModResortsCustomerInformation` | Boilerplate de conexão; lookup acoplado ao servidor (já comentado) | Camada de persistência moderna (JPA/Panache) |
| **`SimpleDateFormat`, `Hashtable`, tipos crus** | `AvailabilityCheckerServlet`, `WeatherServlet` | `SimpleDateFormat` não é thread-safe; coleções/APIs antigas | `java.time` (`DateTimeFormatter`), coleções com genéricos |
| **API externa descontinuada** (Weather Underground) | `Constants`, `WeatherServlet` | Serviço extinto; chamadas falham (a app cai em dados estáticos) | API de clima ativa ou serviço configurável |
| **Empacotamento WAR para servidor de aplicações** | `pom.xml` (`<packaging>war</packaging>`) | Exige servidor externo; ciclo de deploy pesado | JAR executável autônomo (cloud-native) |

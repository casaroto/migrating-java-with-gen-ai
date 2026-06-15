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

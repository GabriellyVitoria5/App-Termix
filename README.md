# Termix

Termix é um jogo mobile desenvolvido para a plataforma Android, inspirado no jogo Termo. O jogo
desafia os jogadores a adivinharem palavras dentro de diferentes modos de minijogos, utilizando uma
base de palavras local armazenada no dispositivo.

## Funcionalidades Principais

- **Jogo Diário**: Um desafio diário onde os jogadores devem adivinhar a palavra do dia.
- **Modo Infinito**: Modo de jogo sem limite de tentativas, permitindo que os jogadores joguem
  continuamente.
- **Modo Turbo**: Um modo de jogo cronometrado onde os jogadores devem adivinhar o maior número
  possível de palavras dentro do tempo limite.
- **Modo Limite de Tempo**: Um modo de jogo onde os jogadores devem adivinhar a palavra dentro de um
  limite de tempo.
- **Banco de Dados Local**: Armazenamento das palavras, dias jogados, palavras acertadas e palavras
  erradas dentro do dispositivo utilizando SQLite.
- **Interface Dinâmica**: Layout otimizado para experiência mobile, com teclados personalizados e
  interface de usuário interativa.

## Diferenciais do Projeto

- Diferentes modos de jogo para aumentar a jogabilidade.
- Experiência offline sem necessidade de conexão com a internet.

## Estrutura do Projeto

O projeto está organizado dentro do Android Studio, utilizando a linguagem Kotlin. Abaixo está uma visão
geral da estrutura do código:

### Pacotes Principais

- **adapter**: Contém adaptadores para manipulação de elementos de interface, como o
  `CalendarAdapter.kt`.
- **controller**: Contém a lógica do jogo, incluindo `GameController`, `KeyboardController`, `ProfileController`,  e `WordController`.
- **localdata**: Contém as classes de banco de dados, incluindo `DatabaseContract` e
  `DatabaseSQLite`.
- **model**: Contém as classes modelo do jogo, como `Word`, `PlayerWords`, `CalendarDay`, `GameSession`, `LettersGrid` `Profile` e `KeyboardGrid`.
- **repository**: Contém as classes responsáveis pelo acesso a dados, como `WordRepository`, `PlayerWordRepository` `ProfileRepository` e `GameSessionRepository`.
- **utils**: Contém utilitários como `CalendarUtils` para ajudar na manipulação de datas.
- **Atividades Principais**: Inclui `MainActivity`, `DailyGame`, `InfiniteMinigame`,
  `MinigamesHome`, `Profile`, `TermixTurboMinigame` e `TimeLimitMinigame`.

## Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **Banco de Dados**: SQLite

## Banco de Dados

O jogo utiliza SQLite para armazenamento de palavras e progresso dos jogadores. A estrutura do banco
de dados é definida nas classes:

- `DatabaseContract`: Define a estrutura das tabelas.
- `DatabaseSQLite`: Implementa a lógica para manipulação do banco de dados.

Além disso, há uma base de palavras localizada dentro da pasta `assets/WordsDatabase`, que contém
todas as palavras possíveis para o jogo.

## Melhorias Futuras

- Terminar implementação dos minijogos e funcionalidade do calenário.
- Implementação de rankings e estatísticas de desempenho.
- Integração com serviços online para jogar com amigos.
- Personalização do tema, interface do jogo e melhorar o perfil.

## Layout e Recursos Visuais

A pasta `res/` contém os recursos gráficos e layouts XML para a interface do jogo, incluindo:

- **drawable/**: Imagens e fundos personalizados.
- **layout/**: Arquivos XML para a construção das telas do jogo.
- **values/**: Arquivos de configuração, como `strings.xml`, `colors.xml` e `themes.xml`.

### Capturas de Tela

Para uma melhor visualização do projeto, abaixo estão algumas capturas de tela do jogo:

1. Tela Inicial:
![Imagem da tela inicial](screenshots/telaInicial.png)

2. Tela de Perfil:
![Imagem da tela de perfil](screenshots/perfil.png)

3. Tela de Jogo Diário:
![Imagem do jogo diário](screenshots/modoDiario.png)

4. Tela de Minijogos:
![Imagem da tela de minijogos](screenshots/minijogos.png)

5. Tela de regras do Modo Infinito:
![Imagem das regras do modo Infinito](screenshots/exemploRegras.png)

6. Tela do modo Jogo Infinito:
![Imagem do modo Infinito](screenshots/modoInfinito.png)

7. Tela do Modo Limite de Tempo:
![Imagem do modo Limite de Tempo](screenshots/modoLimiteTempo.png)

8. Tela do Modo Turbo:
![Imagem do modo turbo](screenshots/modoTurbo.png)

## Configuração e Build

O projeto utiliza Gradle para gerenciamento de dependências. Os principais arquivos de configuração
incluem:

- `build.gradle.kts` (Projeto e Módulo app)
- `settings.gradle.kts`
- `gradle.properties`
- `libs.versions.toml` (Catálogo de versões das dependências)

## Como Executar o Projeto

1. Clone este repositório:
   ```sh
   git clone https://github.com/GabriellyVitoria5/App-Termix.git
   ```
2. Abra o projeto no Android Studio.
3. Compile e execute o projeto em um emulador ou dispositivo físico.
4. Aproveite o jogo Termix!

## Considerações Finais

Termix é um jogo divertido e desafiador para fãs de jogos de palavras. Com diferentes modos de jogo
e um banco de dados local, ele oferece uma experiência dinâmica offline e viciante para os jogadores.

# Space Invaders - Java Arcade Clone 👾

Este projeto é uma recriação do clássico jogo de fliperama **Space Invaders** (1978), desenvolvido inteiramente em Java utilizando as bibliotecas nativas `Swing` e `AWT`. 

O desenvolvimento serviu como um estudo prático e aprofundado sobre a arquitetura de jogos 2D, sendo guiado e inspirado pelos conceitos do livro *"A Lógica do Jogo - Recriando Clássicos da História dos Videogames"* de Marcus Becker.

## 🚀 Funcionalidades e Mecânicas Implementadas

*   **Game Loop Contínuo:** Sistema de renderização e atualização a 50 FPS, garantindo movimentação fluida independente dos eventos de teclado.
*   **Horda Inteligente:** Matriz de invasores com sistema de caixa de colisão global. A horda marcha, detecta as bordas da tela, desce e inverte a direção de forma sincronizada.
*   **Ondas Infinitas (Progressão):** O jogo não possui um fim natural para a vitória. Ao destruir todos os alienígenas, o "Level" avança e uma nova onda é gerada, aumentando a dificuldade progressivamente.
*   **IA de Disparos Inimigos:** Os alienígenas calculam a posição do jogador no eixo X para disparar de forma estratégica.
*   **Disco Voador (Chefe):** Aparição aleatória na parte superior da tela com um sistema de tiro direto quando alinhado perfeitamente com o jogador no eixo X.
*   **Barreiras de Defesa Destrutíveis:** 4 escudos verdes que sofrem desgaste progressivo (derretem) ao receberem disparos inimigos.
*   **Sistema de Game Over e Reinício Limpo:** O jogo detecta a derrota (seja por um tiro inimigo ou pela horda tocando a base da tela) e exibe uma caixa de diálogo interativa (`JOptionPane`) perguntando se o jogador deseja jogar novamente, realizando a limpeza da memória e recarregando os objetos sem fechar a aplicação.
*   **Motor de Áudio Integrado:** Sistema nativo para carregamento e reprodução de efeitos sonoros `.wav` utilizando a interface `Clip` da API de áudio do Java.

## 🛠️ Tecnologias Utilizadas

*   **Linguagem:** Java (JDK 8+)
*   **Interface Gráfica:** `javax.swing.*` e `java.awt.*` (Renderização via `Graphics2D`)
*   **IDE Recomendada:** Apache NetBeans

## 📁 Estrutura do Projeto

O código está organizado da seguinte forma para separar a lógica de negócio das ferramentas utilitárias:

*   `br.com.mvbos.lgj`
    *   `Jogo.java`: Classe principal, contém o Game Loop, renderização da tela e orquestração da partida.
    *   `Tanque.java`: Classe do jogador, desenhada com formas geométricas primitivas.
    *   `Invader.java`: Classe dos inimigos (incluindo o chefe) com sistema de animação baseado em frames binários.
    *   `Tiro.java`: Classe que gerencia a trajetória dos disparos aliados e inimigos.
*   `br.com.mvbos.lgj.base`
    *   `Elemento.java`: Superclasse que contém propriedades físicas (X, Y, largura, altura, velocidade) herdadas por todos os atores do jogo.
    *   `Util.java`: Caixa de ferramentas com métodos estáticos, incluindo detecção de colisão entre elementos.
    *   `Audio.java`: Gerenciador de efeitos sonoros.

## 🎮 Como Executar

1.  Clone este repositório para a sua máquina local.
2.  Abra o projeto no **Apache NetBeans** (ou na sua IDE Java de preferência).
3.  Certifique-se de que a classe `Jogo.java` está configurada como a classe principal (Main Class) nas propriedades do projeto.
4.  Execute o projeto (`F6` no NetBeans).

**Controles:**
*   `Seta para Esquerda`: Move o tanque para a esquerda.
*   `Seta para Direita`: Move o tanque para a direita.
*   `Barra de Espaço`: Dispara o laser.
*   `ESC`: Encerra o jogo instantaneamente.

---
**Autor:** Higor Rafael
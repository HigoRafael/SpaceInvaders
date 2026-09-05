package br.com.mvbos.lgj;

import br.com.mvbos.lgj.base.Audio;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import br.com.mvbos.lgj.base.Util;
import br.com.mvbos.lgj.base.Elemento;

public class Jogo extends JFrame {

    private JPanel tela;
    private boolean jogando = true;
    private final int FPS = 1000 / 20; // 50 frames por segundo

    private boolean[] controleTecla = new boolean[5];

    // Atores do Jogo
    private Tanque tanque;
    private Tiro tiroTanque;
    private Invader[][] invasores;
    private Invader chefe;
    private Tiro tiroChefe;
    private Tiro[] tirosInimigos;
    private Elemento[] barreiras;
    private Audio somTiro;
    private Audio somExplosao;

    private Invader.Tipos[] tipoPorLinha = {
        Invader.Tipos.PEQUENO, Invader.Tipos.MEDIO,
        Invader.Tipos.MEDIO, Invader.Tipos.GRANDE, Invader.Tipos.GRANDE
    };
    
    private int espacamento = 15;
    private int linhaBase = 60;
    private int destruidos = 0;
    private int pontos = 0;
    private int level = 1;
    
    // Controle da Marcha
    private int contador = 0;
    private boolean moverInimigos;
    private boolean novaLinha;
    private int dir = 1; 
    

    private java.util.Random rand = new java.util.Random();

    public Jogo() {
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) { setaTecla(e.getKeyCode(), false); }
            @Override
            public void keyPressed(KeyEvent e) { setaTecla(e.getKeyCode(), true); }
        });

        carregarJogo();

        tela = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(Color.WHITE);
                g2d.drawString("Pontos: " + pontos, 10, 20);
                g2d.drawString("Level: " + level, getWidth() - 80, 20);

                if (!jogando) {
                    g2d.setColor(Color.RED);
                    g2d.drawString("GAME OVER", getWidth() / 2 - 40, getHeight() / 2);
                }

                tanque.desenha(g2d);
                tiroTanque.desenha(g2d);
                chefe.desenha(g2d);
                tiroChefe.desenha(g2d);

                for (Tiro t : tirosInimigos) {
                    t.desenha(g2d);
                }

                for (Elemento b : barreiras) {
                    if (b.isAtivo()) {
                        g2d.setColor(b.getCor());
                        g2d.fillRect(b.getPx(), b.getPy(), b.getLargura(), b.getAltura());
                    }
                }

                for (int i = 0; i < invasores.length; i++) {
                    for (int j = 0; j < invasores[i].length; j++) {
                        invasores[i][j].desenha(g2d);
                    }
                }
            }
        };

        getContentPane().add(tela);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);
        setResizable(false);
        setVisible(true);
    }

    private void carregarJogo() {
        
        somTiro = new Audio("/som/tiro.wav");
        somExplosao = new Audio("/som/explosao.wav");
        
        // Constrói e reconstrói os objetos na memória
        tanque = new Tanque();
        tanque.setPx(320);
        tanque.setPy(400);
        tanque.setAtivo(true);
        tanque.setVel(5);

        tiroTanque = new Tiro(false);
        tiroTanque.setVel(10);

        chefe = new Invader(Invader.Tipos.CHEFE);
        tiroChefe = new Tiro(true);
        tiroChefe.setVel(8);

        tirosInimigos = new Tiro[3];
        for (int i = 0; i < tirosInimigos.length; i++) {
            tirosInimigos[i] = new Tiro(true);
            tirosInimigos[i].setVel(5);
        }

        barreiras = new Elemento[4];
        for (int i = 0; i < barreiras.length; i++) {
            barreiras[i] = new Elemento(60 + (i * 140), 340, 60, 30);
            barreiras[i].setAtivo(true);
            barreiras[i].setCor(Color.GREEN);
        }

        invasores = new Invader[11][5];
        for (int i = 0; i < invasores.length; i++) {
            for (int j = 0; j < invasores[i].length; j++) {
                invasores[i][j] = new Invader(tipoPorLinha[j]);
            }
        }
        
        iniciarNovaOnda();
    }

    private void iniciarNovaOnda() {
        destruidos = 0;
        dir = 1;
        contador = 0;
        novaLinha = false;

        for (int i = 0; i < invasores.length; i++) {
            for (int j = 0; j < invasores[i].length; j++) {
                Invader e = invasores[i][j];
                e.setAtivo(true);
                e.setPx(i * e.getLargura() + (i + 1) * espacamento);
                e.setPy(j * e.getAltura() + j * espacamento + linhaBase);
            }
        }
        
        tiroChefe.setAtivo(false);
        chefe.setAtivo(false);
        for(Tiro t : tirosInimigos) t.setAtivo(false);
        tiroTanque.setAtivo(false);
    }

    public void inicia() {
        while (true) {
            jogando = true;
            long prxAtualizacao = 0;
            
            // Loop principal da partida
            while (jogando) {
                if (System.currentTimeMillis() >= prxAtualizacao) {
                    atualizaJogo();
                    tela.repaint();
                    prxAtualizacao = System.currentTimeMillis() + FPS;
                }
            }
            
            // Quando a partida acaba (jogando = false), desenha o Game Over
            tela.repaint(); 
            
            // Pergunta ao jogador usando botões (ConfirmDialog)
            int resposta = JOptionPane.showConfirmDialog(this, 
                    "GAME OVER!\nSua pontuação final: " + pontos + "\n\nDeseja jogar novamente?",
                    "Fim de Jogo",
                    JOptionPane.YES_NO_OPTION);
            
            if (resposta == JOptionPane.YES_OPTION) {
                // Limpa variáveis globais
                pontos = 0;
                level = 1;
                for(int i = 0; i < controleTecla.length; i++) controleTecla[i] = false;
                
                // Reconstrói a memória
                carregarJogo();
            } else {
                // Encerra a janela e o programa
                dispose();
                System.exit(0);
                break;
            }
        }
    }

    private void atualizaJogo() {
        int totalInimigos = invasores.length * invasores[0].length;
        if (destruidos == totalInimigos) {
            level++;
            iniciarNovaOnda();
            return;
        }

        if (tanque.isAtivo()) {
            if (controleTecla[2] && tanque.getPx() > 0) {
                tanque.incPx(-tanque.getVel());
            } else if (controleTecla[3] && tanque.getPx() + tanque.getLargura() < tela.getWidth()) {
                tanque.incPx(tanque.getVel());
            }
        }

        if (controleTecla[4] && !tiroTanque.isAtivo()) {
            tiroTanque.setPx(tanque.getPx() + tanque.getLargura() / 2 - tiroTanque.getLargura() / 2);
            tiroTanque.setPy(tanque.getPy() - tiroTanque.getAltura());
            tiroTanque.setAtivo(true);
            somTiro.tocar();
        }

        if (tiroTanque.isAtivo()) {
            tiroTanque.incPy(-tiroTanque.getVel());
            if (tiroTanque.getPy() < 0) tiroTanque.setAtivo(false);
            
            for (int i = 0; i < invasores.length; i++) {
                for (int j = 0; j < invasores[0].length; j++) {
                    Invader inv = invasores[i][j];
                    if (inv.isAtivo() && Util.colide(tiroTanque, inv)) {
                        inv.setAtivo(false);
                        tiroTanque.setAtivo(false);
                        destruidos++;
                        pontos += inv.getPremio() * level;
                        somExplosao.tocar();
                        break;
                    }
                }
            }
        }

        if (tiroChefe.isAtivo()) {
            tiroChefe.incPy(tiroChefe.getVel());
            if (Util.colide(tiroChefe, tanque)) jogando = false; 
            else if (tiroChefe.getPy() > tela.getHeight()) tiroChefe.setAtivo(false);
        }

        for (Tiro t : tirosInimigos) {
            if (t.isAtivo()) {
                t.incPy(t.getVel());
                if (Util.colide(t, tanque)) jogando = false; 
                else if (t.getPy() > tela.getHeight()) t.setAtivo(false);
            }
        }

        for (Elemento b : barreiras) {
            if (!b.isAtivo()) continue;
            
            if (tiroTanque.isAtivo() && Util.colide(tiroTanque, b)) {
                tiroTanque.setAtivo(false); 
            }
            if (tiroChefe.isAtivo() && Util.colide(tiroChefe, b)) {
                tiroChefe.setAtivo(false);
                b.setAltura(b.getAltura() - 10);
                b.setPy(b.getPy() + 10); 
            }
            for (Tiro t : tirosInimigos) {
                if (t.isAtivo() && Util.colide(t, b)) {
                    t.setAtivo(false);
                    b.setAltura(b.getAltura() - 10);
                    b.setPy(b.getPy() + 10);
                }
            }
            if (b.getAltura() <= 0) b.setAtivo(false);
        }

        if (!chefe.isAtivo() && rand.nextInt(500) == destruidos && destruidos > 0) {
            chefe.setPx(0);
            chefe.setAtivo(true);
        }

        if (chefe.isAtivo()) {
            chefe.incPx(tanque.getVel() - 1);
            if (!tiroChefe.isAtivo() && Util.colideX(chefe, tanque)) {
                addTiroInimigo(chefe, tiroChefe);
            }
            if (chefe.getPx() > tela.getWidth()) chefe.setAtivo(false);
        }

        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        boolean invasaoConcluida = false;

        for (int i = 0; i < invasores.length; i++) {
            for (int j = 0; j < invasores[i].length; j++) {
                Invader inv = invasores[i][j];
                if (inv.isAtivo()) {
                    if (inv.getPx() < minX) minX = inv.getPx();
                    if (inv.getPx() + inv.getLargura() > maxX) maxX = inv.getPx() + inv.getLargura();
                    if (inv.getPy() + inv.getAltura() >= tanque.getPy()) invasaoConcluida = true;
                }
            }
        }

        if (invasaoConcluida) {
            jogando = false; 
        }

        int contadorEspera = totalInimigos - destruidos - (level * 2);
        if (contadorEspera < 0) contadorEspera = 0; 

        if (contador >= contadorEspera) {
            moverInimigos = true;
            contador = 0;
        } else {
            contador++;
            moverInimigos = false;
        }

        if (moverInimigos) {
            if (novaLinha) {
                dir *= -1; 
                for (int i = 0; i < invasores.length; i++) {
                    for (int j = 0; j < invasores[0].length; j++) {
                        if (invasores[i][j].isAtivo()) {
                            invasores[i][j].incPy(espacamento);
                            invasores[i][j].atualiza();
                        }
                    }
                }
                novaLinha = false;
            } else {
                if ((dir == 1 && maxX + espacamento >= tela.getWidth() - 20) || 
                    (dir == -1 && minX - espacamento <= 0)) {
                    novaLinha = true;
                } else {
                    for (int i = 0; i < invasores.length; i++) {
                        for (int j = 0; j < invasores[0].length; j++) {
                            if (invasores[i][j].isAtivo()) {
                                invasores[i][j].incPx(espacamento * dir);
                                invasores[i][j].atualiza();
                                
                                if (!tirosInimigos[0].isAtivo() && invasores[i][j].getPx() < tanque.getPx()) {
                                    addTiroInimigo(invasores[i][j], tirosInimigos[0]);
                                } else if (!tirosInimigos[1].isAtivo() && 
                                           invasores[i][j].getPx() > tanque.getPx() && 
                                           invasores[i][j].getPx() < tanque.getPx() + tanque.getLargura()) {
                                    addTiroInimigo(invasores[i][j], tirosInimigos[1]);
                                } else if (!tirosInimigos[2].isAtivo() && invasores[i][j].getPx() > tanque.getPx()) {
                                    addTiroInimigo(invasores[i][j], tirosInimigos[2]);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void addTiroInimigo(Elemento inimigo, Elemento tiro) {
        tiro.setAtivo(true);
        tiro.setPx(inimigo.getPx() + inimigo.getLargura() / 2 - tiro.getLargura() / 2);
        tiro.setPy(inimigo.getPy() + inimigo.getAltura());
    }

    private void setaTecla(int tecla, boolean pressionada) {
        switch (tecla) {
            case KeyEvent.VK_ESCAPE:
                jogando = false;
                System.exit(0);
                break;
            case KeyEvent.VK_LEFT:
                controleTecla[2] = pressionada;
                break;
            case KeyEvent.VK_RIGHT:
                controleTecla[3] = pressionada;
                break;
            case KeyEvent.VK_SPACE:
                controleTecla[4] = pressionada;
                break;
        }
    }

    public static void main(String[] args) {
        Jogo jogo = new Jogo();
        jogo.inicia();
    }
}
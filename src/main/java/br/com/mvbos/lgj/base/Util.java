package br.com.mvbos.lgj.base;

public class Util {

    public static boolean colide(Elemento a, Elemento b) {
        if (!a.isAtivo() || !b.isAtivo())
            return false;

        // posição no eixo X + largura do elemento A e B
        final int plA = a.getPx() + a.getLargura();
        final int plB = b.getPx() + b.getLargura();
        
        // posição no eixo Y + altura do elemento A e B
        final int paA = a.getPy() + a.getAltura();
        final int paB = b.getPy() + b.getAltura();

        if (plA > b.getPx() && a.getPx() < plB && paA > b.getPy() && a.getPy() < paB) {
            return true;
        }
        return false;
    }

    // Colisão apenas no eixo X (útil para o Disco Voador Chefe saber se o jogador está na mira)
    public static boolean colideX(Elemento a, Elemento b) {
        if (!a.isAtivo() || !b.isAtivo())
            return false;
            
        final int plA = a.getPx() + a.getLargura();
        final int plB = b.getPx() + b.getLargura();
        
        return plA > b.getPx() && a.getPx() < plB;
    }
}
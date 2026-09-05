package br.com.mvbos.lgj;

import java.awt.Color;
import java.awt.Graphics2D;
import br.com.mvbos.lgj.base.Elemento;

public class Tanque extends Elemento {
    
    private final int cano = 8;
    private final int escotilha = 10;

    public Tanque() {
        setLargura(30);
        setAltura(15);
    }

    @Override
    public void atualiza() {
    }

    @Override
    public void desenha(Graphics2D g) {
        g.setColor(Color.GREEN);
        
        // Desenha o cano do tanque
        g.fillRect(getPx() + getLargura() / 2 - cano / 2, getPy() - cano, cano, cano);
        
        // Desenha a carcaça do tanque
        g.fillRect(getPx(), getPy(), getLargura(), getAltura());
        
        g.setColor(Color.YELLOW);
        
        // Desenha a escotilha
        g.fillOval(getPx() + getLargura() / 2 - escotilha / 2, 
                   getPy() + getAltura() / 2 - escotilha / 2, 
                   escotilha, escotilha);
    }
}
package br.com.mvbos.lgj;

import java.awt.Color;
import java.awt.Graphics2D;
import br.com.mvbos.lgj.base.Elemento;

public class Invader extends Elemento {
    
    // O Enum define os tipos de invasores disponíveis
    enum Tipos {
        PEQUENO, MEDIO, GRANDE, CHEFE
    }

    private Tipos tipo;
    private boolean aberto;

    public Invader(Tipos t) {
        this.tipo = t;
        setLargura(20);
        setAltura(20);
    }

    @Override
    public void atualiza() {
        // Inverte o estado da variável toda vez que o método é chamado (Animação)
        aberto = !aberto;
    }

    @Override
    public void desenha(Graphics2D g) {
        if (!isAtivo())
            return;
        
        int larg = getLargura();
        
        if (tipo == Tipos.PEQUENO) {
            larg = larg - 2;
            g.setColor(Color.BLUE);
            if (aberto) {
                g.drawOval(getPx(), getPy(), larg, getAltura());
            } else {
                g.fillRect(getPx(), getPy(), larg, getAltura());
            }
            
        } else if (tipo == Tipos.MEDIO) {
            g.setColor(Color.ORANGE);
            if (aberto) {
                g.drawRect(getPx(), getPy(), larg, getAltura());
            } else {
                g.fillRect(getPx(), getPy(), larg, getAltura());
            }
            
        } else if (tipo == Tipos.GRANDE) {
            larg = larg + 4;
            if (aberto) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(getPx() + 4, getPy(), larg - 8, getAltura());
            } else {
                g.setColor(Color.GRAY);
                g.fillRect(getPx(), getPy() + 4, larg, getAltura() - 8);
            }
            
        } else {
            // Disco Voador (Chefe)
            g.setColor(Color.RED);
            g.fillOval(getPx(), getPy(), larg, getAltura());
            
            // Luzes piscantes do chefe
            if (aberto) {
                g.setColor(Color.WHITE);
                g.fillRect(getPx() + 2, getPy() + 8, 4, 4);
                g.fillRect(getPx() + 8, getPy() + 8, 4, 4);
                g.fillRect(getPx() + 14, getPy() + 8, 4, 4);
            }
        }
    }

    public int getPremio() {
        switch (tipo) {
            case PEQUENO:
                return 300;
            case MEDIO:
                return 200;
            case GRANDE:
                return 100;
            default:
                return 1000;
        }
    }
}
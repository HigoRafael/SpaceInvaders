package br.com.mvbos.lgj.base;

import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio {
    
    private Clip clip;

    public Audio(String caminhoArquivo) {
        try {
            // getClass().getResource() busca o arquivo empacotado dentro do .jar
            URL url = getClass().getResource(caminhoArquivo);
            
            if (url == null) {
                System.out.println("Áudio não encontrado no .jar: " + caminhoArquivo);
                return;
            }
            
            AudioInputStream as = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(as);
        } catch (Exception e) {
            System.out.println("Erro ao carregar o som: " + caminhoArquivo);
            e.printStackTrace();
        }
    }

    public void tocar() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
package br.com.feapps.asking.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by F.Einstein on 022, 22/1/2016.
 */
public class Questao {
    private int id;
    private String questao;
    private Area area;
    private SubArea subArea;
    private Resposta resposta;

    public Questao() {
    }

    public Questao(int id, String questao, Area area, SubArea subArea, Resposta resposta) {
        this.id = id;
        this.questao = questao;
        this.area = area;
        this.subArea = subArea;
        this.resposta = resposta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestao() {
        return questao;
    }

    public void setQuestao(String questao) {
        this.questao = questao;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public SubArea getSubArea() {
        return subArea;
    }

    public void setSubArea(SubArea subArea) {
        this.subArea = subArea;
    }

    public Resposta getResposta() {
        return resposta;
    }

    public void setResposta(Resposta resposta) {
        this.resposta = resposta;
    }

    @Override
    public String toString() {
        //List<Usuario> usuarios = this.usuarios;
        String[] vectorBuffer = new String[8];

        vectorBuffer[0] = String.valueOf(this.getId());
        vectorBuffer[1] = this.getQuestao();
        vectorBuffer[2] = String.valueOf(this.getArea().getId());
        vectorBuffer[3] = this.getArea().getNome();
        vectorBuffer[4] = String.valueOf(this.getSubArea().getId());
        vectorBuffer[5] = this.getSubArea().getNome();

        vectorBuffer[6] = String.valueOf(this.getResposta().getId());
        vectorBuffer[7] = this.getResposta().getResposta();

        String buffer="";
        for(int i = 0; i < vectorBuffer.length; i++) {
            if(i!=(vectorBuffer.length-1))
                buffer=buffer+vectorBuffer[i]+"$flag";
            else
                buffer=buffer+vectorBuffer[i];
        }

        return buffer;
    }

    public void toQuestao(String str) {
        String[] vectorBuffer = str.split(Pattern.quote("$flag"));
        //for (String s : vectorBuffer)
        //Log.d("questao", s);

        this.setId(Integer.parseInt(vectorBuffer[0]));
        this.setQuestao(vectorBuffer[1]);
        Area a = new Area(Integer.parseInt(vectorBuffer[2]), vectorBuffer[3]);
        this.setArea(a);
        SubArea sb = new SubArea(Integer.parseInt(vectorBuffer[4]), vectorBuffer[5]);
        this.setSubArea(sb);
        this.setResposta(new Resposta(Integer.parseInt(vectorBuffer[6]), vectorBuffer[7], a, sb));

        //Log.d("questao", this.getQuestao());
    }
}

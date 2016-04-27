package br.com.feapps.asking.model;

import android.util.Log;

import java.util.regex.Pattern;

/**
 * Created by F.Einstein on 022, 22/1/2016.
 */
public class Resposta {
    private int id;
    private String resposta;
    private Area area;
    private SubArea subArea;

    public Resposta() {
    }

    public Resposta(int id, String resposta, Area area, SubArea subArea) {
        this.id = id;
        this.resposta = resposta;
        this.area = area;
        this.subArea = subArea;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
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

    @Override
    public String toString() {
        //List<Usuario> usuarios = this.usuarios;
        String[] vectorBuffer = new String[6];

        vectorBuffer[0] = String.valueOf(this.getId());
        vectorBuffer[1] = this.getResposta();
        vectorBuffer[2] = String.valueOf(this.getArea().getId());
        vectorBuffer[3] = this.getArea().getNome();
        vectorBuffer[4] = String.valueOf(this.getSubArea().getId());
        vectorBuffer[5] = this.getSubArea().getNome();

        String buffer="";
        for(int i = 0; i < vectorBuffer.length; i++) {
            if(i!=(vectorBuffer.length-1))
                buffer=buffer+vectorBuffer[i]+"$flag";
            else
                buffer=buffer+vectorBuffer[i];
        }

        return buffer;
    }

    public void toResposta(String str) {
        String[] vectorBuffer = str.split(Pattern.quote("$flag"));

        this.setId(Integer.parseInt(vectorBuffer[0]));
        this.setResposta(vectorBuffer[1]);
        Area a = new Area(Integer.parseInt(vectorBuffer[2]), vectorBuffer[3]);
        this.setArea(a);
        SubArea sb = new SubArea(Integer.parseInt(vectorBuffer[4]), vectorBuffer[5]);
        this.setSubArea(sb);
    }
}

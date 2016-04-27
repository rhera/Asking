package br.com.feapps.asking.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by F.Einstein on 009, 9/1/2016.
 */
public class Area {
    int id;
    String nome;
    List<SubArea> subAreas;

    public Area() {
        this.id = 0;
        this.nome = "";
        this.subAreas = new ArrayList<>();
    }

    public Area(String nome) {
        this.nome = nome;
    }

    public Area(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.subAreas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<SubArea> getSubAreas() {
        return subAreas;
    }

    public void setSubAreas(List<SubArea> subAreas) {
        this.subAreas = subAreas;
    }

    @Override
    public String toString() {
        List<SubArea> subAreas = this.getSubAreas();
        String[] vectorBuffer = new String[2+(subAreas.size()*2)];

        vectorBuffer[0] = String.valueOf(this.getId());
        vectorBuffer[1] = this.getNome();

        int i = 2;
        for (SubArea sa : subAreas) {
            vectorBuffer[i++] = String.valueOf(sa.getId());
            vectorBuffer[i++] = sa.getNome();
        }

        String buffer="";
        for(i = 0; i < vectorBuffer.length; i++) {
            buffer += vectorBuffer[i];
            if(i < vectorBuffer.length)
                buffer += "##";
        }

        return buffer;
    }

    public void toArea(String str) {
        String[] vectorBuffer = str.split(Pattern.quote("##"));
        List<SubArea> subAreas = new ArrayList<>();

        this.setId(Integer.parseInt(vectorBuffer[0]));
        this.setNome(vectorBuffer[1]);

        for (int i = 2; i < vectorBuffer.length; i+=2) {
            int id = Integer.parseInt(vectorBuffer[i]);
            String nome = vectorBuffer[i+1];
            SubArea sa = new SubArea(id, nome);
            subAreas.add(sa);
        }
        this.setSubAreas(subAreas);
    }
}
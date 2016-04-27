package br.com.feapps.asking.model;

import com.google.gson.Gson;

/**
 * Created by F.Einstein on 009, 9/1/2016.
 */
public class SubArea {
    int id;
    String nome;

    public SubArea() {
        id = 0;
        nome = "Vazia";
        //area_id = 0;
    }

    public SubArea(String nome) {
        this.nome = nome;
    }

    public SubArea(int id, String nome) {
        this.id = id;
        this.nome = nome;
        //this.area_id = area_id;
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

    @Override
    public String toString() {
        String buffer = "";
        buffer += this.getId();
        buffer += "$$";
        buffer += this.getNome();
        return buffer;
    }

    public void toSubArea(String str) {
        this.toSubArea(str.split("$$"));
    }

    public void toSubArea(String[] vectorStr) {
        this.setId(Integer.parseInt(vectorStr[0]));
        this.setNome(vectorStr[1]);
    }

}

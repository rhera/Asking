package br.com.feapps.asking.model;

import android.util.Log;

import java.util.regex.Pattern;

/**
 * Created by F.Einstein on 010, 10/2/2016.
 */
public class Ranking {
    Usuario usuario;
    float media5acertos;
    float mediaTempo;

    public Ranking() {
        this.usuario = new Usuario();
        this.media5acertos = 0;
        this.mediaTempo = 0;
    }

    public Ranking(String string) {
        //Log.e("ranking", string);
        this.toRanking(string);
    }

    public Ranking(Usuario usuario) {
        this.usuario = usuario;
        this.media5acertos = 0;
        this.mediaTempo = 0;
    }

    public Ranking(Usuario usuario, int media5questoes, int mediaTempo) {
        this.usuario = usuario;
        this.media5acertos = media5questoes;
        this.mediaTempo = mediaTempo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public float getMedia5acertos() {
        return media5acertos;
    }

    public void setMedia5acertos(float media5acertos) {
        this.media5acertos = media5acertos;
    }

    public float getMediaTempo() {
        return mediaTempo;
    }

    public void setMediaTempo(float mediaTempo) {
        this.mediaTempo = mediaTempo;
    }

    public String toString(Usuario user) {
        String retorno = "";

        retorno += user.toString();
        retorno += "$$";

        retorno += this.getMedia5acertos();
        retorno += "$$";

        retorno += this.getMediaTempo();

        return retorno;
    }

    public void toRanking(String str) {
        String[] strVect = str.split(Pattern.quote("$$"));
        Usuario u = new Usuario();
        u.toUsuario(strVect[0]);
        this.setUsuario(u);
        this.setMedia5acertos(Float.valueOf(strVect[1]));
        this.setMediaTempo(Float.valueOf(strVect[2]));
    }
}

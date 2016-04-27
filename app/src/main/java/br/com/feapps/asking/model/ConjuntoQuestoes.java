package br.com.feapps.asking.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by F.Einstein on 022, 22/1/2016.
 */
public class ConjuntoQuestoes {
    private int id;
    private List<Questao> questoes;
    private List<Resposta> respostas;

    public ConjuntoQuestoes() {
        id = 0;
        questoes = new ArrayList<>();
        respostas = new ArrayList<>();
    }

    public ConjuntoQuestoes(int id, List<Questao> questoes, List<Resposta> respostas) {
        this.id = id;
        this.questoes = questoes;
        this.respostas = respostas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Questao> getQuestoes() {
        return questoes;
    }

    public void setQuestoes(List<Questao> questoes) {
        this.questoes = questoes;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public void setRespostas(List<Resposta> respostas) {
        this.respostas = respostas;
    }

    public String[] toVectorString() {
        String[] vectorBuffer = new String[2+questoes.size()+respostas.size()];
        vectorBuffer[0] = String.valueOf(this.getId());

        int i = 1;
        for (Questao q : questoes) {
            vectorBuffer[i++] = q.toString();
        }

        vectorBuffer[i++] = "$fimquestoes";

        for (Resposta r : respostas) {
            vectorBuffer[i++] = r.toString();
        }

        return vectorBuffer;
    }

    @Override
    public String toString() {
        String[] vectorBuffer = this.toVectorString();
        String buffer = "";
        for (int i = 0; i < vectorBuffer.length; i++) {
            buffer += vectorBuffer[i];
            buffer += "%%";
        }
        return buffer;
    }

    public void toConjunto(String vectorBuffer[]) {
        Questao q = new Questao();
        Resposta r = new Resposta();

        //for (String s : vectorBuffer)
        //Log.d("conjunto", s);

        this.setId(Integer.parseInt(vectorBuffer[0]));

        int i = 1;
        //Log.d("conjunto", "Antes de transformar as partes do vectorbuffer que são questões em questões");
        while (!vectorBuffer[i].equals("$fimquestoes")) {
            //Log.d("vectorbuffer",i + " - " + vectorBuffer[i]);
            q = new Questao();
            q.toQuestao(vectorBuffer[i++]);
            questoes.add(q);
        }
        i++;

        //Log.d("conjunto", "Antes de transformar as partes do vectorbuffer que são respostas em respostas");
        for (int j = i; j < vectorBuffer.length; j++) {
            //Log.d("vectorbuffer",i + " - " + vectorBuffer[i]);
            r = new Resposta();
            r.toResposta(vectorBuffer[i++]);
            respostas.add(r);
        }

        //for (Questao ques: this.getQuestoes())
        //Log.d("conjunto", "Questao: " + ques.getQuestao());
    }

    public void toConjunto(String str) {
        String[] vectorBuffer = str.split("%%");
        toConjunto(vectorBuffer);
    }

}

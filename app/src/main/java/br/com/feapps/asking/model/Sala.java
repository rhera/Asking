package br.com.feapps.asking.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by F.Einstein on 013, 13/1/2016.
 */
public class Sala {
    private int id;
    private String nome;
    private String tipo;
    private String senha;
    private Area area;
    private SubArea subArea;
    private List<Usuario> usuarios;
    private int maxUserSala;
    private ConjuntoQuestoes conjuntoQuestoes;
    private boolean visivel;

    public Sala() {
        id = 0;
        nome = "";
        tipo = "";
        senha = "";
        area = new Area();
        subArea = new SubArea();
        usuarios = new ArrayList<>();
        maxUserSala = 0;
        conjuntoQuestoes = new ConjuntoQuestoes();
        this.visivel = true;
    }

    public Sala(int id, String nome, String tipo, String senha, Area area, SubArea subArea, List<Usuario> usuarios, int maxUserSala, ConjuntoQuestoes conjuntoQuestoes, boolean visivel) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.senha = senha;
        this.area = area;
        this.subArea = subArea;
        this.usuarios = usuarios;
        this.maxUserSala = maxUserSala;
        this.conjuntoQuestoes = conjuntoQuestoes;
        this.visivel = visivel;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getTipoInt() {
        switch(this.tipo) {
            case "1 Vs 1":
                return 1;
            case "2 Vs 2":
                return 1;
            case "3 Vs 3":
                return 1;
            default:
                return 0;
        }
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public int getMaxUserSala() {
        return maxUserSala;
    }

    public void setMaxUserSala(int maxUserSala) {
        this.maxUserSala = maxUserSala;
    }

    public ConjuntoQuestoes getConjuntoQuestoes() {
        return conjuntoQuestoes;
    }

    public void setConjuntoQuestoes(ConjuntoQuestoes conjuntoQuestoes) {
        this.conjuntoQuestoes = conjuntoQuestoes;
    }

    public boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

    public String[] toVectorString() {
        String[] vectorBuffer = new String[14 + this.getArea().getSubAreas().size()*2 + (this.usuarios.size()*9)+this.getConjuntoQuestoes().getQuestoes().size()+this.getConjuntoQuestoes().getRespostas().size()];

        int j = 0;

        vectorBuffer[j++] = String.valueOf(this.isVisivel());
        vectorBuffer[j++] = String.valueOf(this.getId());
        vectorBuffer[j++] = this.getNome();
        vectorBuffer[j++] = this.getTipo();
        vectorBuffer[j++] = this.getSenha();

        //Pegando Area
        vectorBuffer[j++] = String.valueOf(this.getArea().getId());
        vectorBuffer[j++] = this.getArea().getNome();

        //Pegando subAreas de Area
        List<SubArea> lsa = new ArrayList<>(this.getArea().getSubAreas());

        for (SubArea sa : lsa) {
            vectorBuffer[j++] = String.valueOf(sa.getId());
            vectorBuffer[j++] = sa.getNome();
        }

        vectorBuffer[j++] = "$flag";

        vectorBuffer[j++] = String.valueOf(this.getSubArea().getId());
        vectorBuffer[j++] = this.getSubArea().getNome();

        for (Usuario u : this.getUsuarios()) {
            vectorBuffer[j++] = String.valueOf(u.getId());
            vectorBuffer[j++] = u.getNome();
            vectorBuffer[j++] = u.getTelefone();
            vectorBuffer[j++] = u.getEmail();
            vectorBuffer[j++] = u.getNascimento();
            vectorBuffer[j++] = u.getLogin();
            vectorBuffer[j++] = u.getSenha();
            vectorBuffer[j++] = String.valueOf(u.getTipo());
            vectorBuffer[j++] = String.valueOf(u.getFoto());
        }

        vectorBuffer[j++] = "$flag";

        vectorBuffer[j++] = String.valueOf(this.getMaxUserSala());

        String[] tempBuffer = this.getConjuntoQuestoes().toVectorString();

        for (int i = 0; i < tempBuffer.length; i++) {
            vectorBuffer[j++] = tempBuffer[i];
        }

        return vectorBuffer;
    }

    @Override
    public String toString() {
        //List<Usuario> usuarios = this.usuarios;
        String[] vectorBuffer = toVectorString();

        String buffer="";
        for(int i = 0; i < vectorBuffer.length; i++) {
            buffer += vectorBuffer[i];
            if (i < vectorBuffer.length)
                buffer += "##";
        }
        //System.out.println(buffer);
        return buffer;
    }

    public void toSala (String[] vectorBuffer) {
        ConjuntoQuestoes cq = new ConjuntoQuestoes();
        List<Usuario> usuarios = new ArrayList<>();

        int j = 0;

        Log.e("sala", vectorBuffer[j]);
        this.setVisivel(Boolean.valueOf(vectorBuffer[j++]));
        this.setId(Integer.parseInt(vectorBuffer[j++]));
        this.setNome(vectorBuffer[j++]);
        this.setTipo(vectorBuffer[j++]);
        this.setSenha(vectorBuffer[j++]);

        Area a = new Area(Integer.parseInt(vectorBuffer[j++]), vectorBuffer[j++]);

        //Setando subareas de area
        while (!vectorBuffer[j].equals("$flag")) {
            SubArea sa = new SubArea();
            sa.setId(Integer.parseInt(vectorBuffer[j++]));
            sa.setNome(vectorBuffer[j++]);
            a.getSubAreas().add(sa);
        }
        this.setArea(a);
        j++;

        SubArea sa = new SubArea(Integer.parseInt(vectorBuffer[j++]), vectorBuffer[j++]);
        this.setSubArea(sa);

        while (!vectorBuffer[j].equals("$flag")) {
            Usuario u = new Usuario(
                    Integer.parseInt(vectorBuffer[j++]),
                    vectorBuffer[j++],
                    vectorBuffer[j++],
                    vectorBuffer[j++],
                    vectorBuffer[j++],
                    vectorBuffer[j++],
                    vectorBuffer[j++],
                    Integer.parseInt(vectorBuffer[j++]),
                    vectorBuffer[j++].getBytes());
            usuarios.add(u);
        }
        j++;

        this.setMaxUserSala(Integer.parseInt(vectorBuffer[j++]));

        String[] tempVecStr = new String[vectorBuffer.length-j];

        int k = 0;
        for (int i = j; i < vectorBuffer.length; i++)
            tempVecStr[k++] = vectorBuffer[i];

        cq.toConjunto(tempVecStr);

        this.setConjuntoQuestoes(cq);

        this.setUsuarios(usuarios);
    }

    /*public void toSala(String str) {
        String[] vectorBuffer = str.split("##");
        toSala(vectorBuffer);
    }*/
}

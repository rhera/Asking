package br.com.feapps.asking.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by F.Einstein on 002, 2/12/15.
 */
public class Usuario {

    private int id;
    private String nome;
    private String telefone;
    private String nascimento;
    private String email;
    private String login;
    private String senha;
    private int tipo;
    private byte [] foto;
    //private List<Usuario> amigos;
    //TODO criar variavel logado na talbela de usuários

    public Usuario() {
        super();
        id = -1;
        nome = "";
        telefone = "";
        nascimento = "";
        email = "";
        login = "";
        senha = "";
        tipo = -1;
        foto = null;
        //amigos = new ArrayList<>();
    }

    public Usuario(int id,
                   String nome,
                   String telefone,
                   String nascimento,
                   String email,
                   String login,
                   String senha,
                   int tipo,
                   byte[] foto) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.nascimento = nascimento;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
        this.foto = foto;
        //this.amigos = amigos;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNascimento() {
        return nascimento;
    }

    public void setNascimento(String nascimento) {
        this.nascimento = nascimento;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public byte [] getFoto() {
        return foto;
    }

    public void setFoto(byte [] foto) {
        this.foto = foto;
    }

    /*public List<Usuario> getAmigos() {
        return amigos;
    }

    public void setAmigos(List<Usuario> amigos) {
        this.amigos = amigos;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;

        Usuario usuario = (Usuario) o;

        return getSenha().equals(usuario.getSenha());

    }

    @Override
    public int hashCode() {
        return getSenha().hashCode();
    }

    //TODO adicionar opção de fazer amigos
    @Override
    public String toString() {
        String buffer = "";
        buffer += getId();
        buffer += "##";
        buffer += getNome();
        buffer += "##";
        buffer += getTelefone();
        buffer += "##";
        buffer += getNascimento();
        buffer += "##";
        buffer += getEmail();
        buffer += "##";
        buffer += getLogin();
        buffer += "##";
        buffer += getSenha();
        buffer += "##";
        buffer += getTipo();
        buffer += "##";
        buffer += getFoto();
        return buffer;
    }

    public void toUsuario(String buffer) {
        String[] vectorBuffer = buffer.split(Pattern.quote("##"));
        //Log.e("usuario", buffer);
        this.setId(Integer.parseInt(vectorBuffer[0]));
        this.setNome(vectorBuffer[1]);
        this.setTelefone(vectorBuffer[2]);
        this.setNascimento(vectorBuffer[3]);
        this.setEmail(vectorBuffer[4]);
        this.setLogin(vectorBuffer[5]);
        this.setSenha(vectorBuffer[6]);
        this.setTipo(Integer.parseInt(vectorBuffer[7]));
        this.setFoto(vectorBuffer[8].getBytes());
    }
}

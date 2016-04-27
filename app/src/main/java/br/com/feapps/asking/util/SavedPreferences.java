package br.com.feapps.asking.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import br.com.feapps.asking.R;
import br.com.feapps.asking.model.Area;
import br.com.feapps.asking.model.ConjuntoQuestoes;
import br.com.feapps.asking.model.Questao;
import br.com.feapps.asking.model.Ranking;
import br.com.feapps.asking.model.Resposta;
import br.com.feapps.asking.model.Sala;
import br.com.feapps.asking.model.SubArea;
import br.com.feapps.asking.model.Usuario;


/**
 * Created by F.Einstein on 007, 7, 12, 15.
 */
public class SavedPreferences extends _Message{
    private Bundle savedInstanceState;
    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private String MyPREFERENCES = "myPrefs";
    private String buffer;
    private String[] vectorBuffer;


    public SavedPreferences(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    public static String toString(String[] strings) {
        String buffer="";
        for(int i=0;i<strings.length;i++) {
            buffer += strings[i];
            if(i < strings.length)
                buffer += "@@";
        }
        return buffer;
    }

    public static String[] toVectorString (String str) {
        return str.split(Pattern.quote("@@"));
    }

    public void saveUser (Usuario u) {
        getEditor().putString("user", u.toString());
        getEditor().commit();
    }

    public Usuario getUser() {
        Usuario u = new Usuario();
        if (hasKey("user")) {
            buffer = sharedpreferences.getString("user", "vazio");
            u.toUsuario(buffer);
        }
        return u;
    }

    public void saveListUsers (List<Usuario> users) {
        vectorBuffer = new String[users.size()];

        int i = 0;
        for (Usuario u : users) {
            vectorBuffer[i++] = u.toString();

        }

        getEditor().putString("users", toString(vectorBuffer));
        getEditor().commit();
    }

    public List<Usuario> getListUsers () {
        List<Usuario> listUsers = new ArrayList<>();
        Usuario u = new Usuario();
        if (hasKey("users")) {
            buffer = sharedpreferences.getString("users", "vazio");
            vectorBuffer = toVectorString(buffer);

            for (int i = 0; i < vectorBuffer.length; i++) {
                u = new Usuario();
                //Log.d("SavedPreferences>users", u.getNome());
                u.toUsuario(vectorBuffer[i]);
                listUsers.add(u);
            }
        }
        return listUsers;
    }

    /*public UsuarioArrayAdapter getUsersArrayAdapter(Context context, Sala sala) {
        UsuarioArrayAdapter usersAdp = new UsuarioArrayAdapter(context, R.layout.activity_lista_usuario_sala);
        List<Usuario> users;

        if (sala != null) {
            users = sala.getUsuarios();
        } else {

            //TODO implementar pegar todos os usuários do banco

            //users = getUsers();
        }

        for (Usuario u : users) {
            usersAdp.add(u);
        }

        return usersAdp;
    }*/

    public void setAreas (List<Area> listAreas) {
        vectorBuffer = new String[listAreas.size()];

        int i = 0;
        for (Area a : listAreas) {
            vectorBuffer[i++] = a.toString();
        }

        //Log.d("ListaArrayAreas", toString(vectorBuffer));
        getEditor().putString("areas", toString(vectorBuffer));
        getEditor().commit();
    }

    public List<Area> getAreas () {
        List<Area> listArea = new ArrayList<>();
        Area a = new Area();
        if (hasKey("areas")) {
            buffer = sharedpreferences.getString("areas", "vazio");
            vectorBuffer = toVectorString(buffer);

            for (int i = 0; i < vectorBuffer.length; i++) {
                a = new Area();
                //Log.d("getVectorStr" + i, vectorBuffer[i]);
                //if (!vectorBuffer[i].equals(""))
                a.toArea(vectorBuffer[i]);

                listArea.add(a);
            }
        }
        return listArea;
    }

    public void setSala (Sala s) {
        //Log.d("saveprefs", "Antes de transformar sala em vetor");
        //for (Questao q : s.getConjuntoQuestoes().getQuestoes())
        //Log.e("saveprefs", q.getQuestao());
        vectorBuffer = s.toVectorString();
        //Log.d("saveprefs", "Depois de transformar sala em vetor");
        getEditor().putString("sala", String.valueOf(vectorBuffer.length));
        for (int i = 0; i < vectorBuffer.length; i++) {
            //Log.d("saveprefs", i + " - " + vectorBuffer[i]);
            getEditor().putString("sala" + i, vectorBuffer[i]);
            getEditor().commit();
        }
    }

    public Sala getSala() {
        Sala s = new Sala();
        int tamanho = 0;
        if (hasKey("sala")) {
            tamanho = Integer.parseInt(sharedpreferences.getString("sala", "vazio"));
            vectorBuffer = new String[tamanho];
            for (int i = 0; i < tamanho; i++) {
                vectorBuffer[i] = sharedpreferences.getString("sala" + i, "vazio");
                //Log.i("savedprefs", vectorBuffer[i]);
            }
            s.toSala(vectorBuffer);
        }
        return s;
    }

    public void removeSala() {
        int tamanho = 0;
        if (hasKey("sala")) {
            tamanho = Integer.parseInt(sharedpreferences.getString("sala", "vazio"));
            this.remove("sala");
            //vectorBuffer = new String[tamanho];
            for (int i = 0; i < tamanho; i++) {
                String str = sharedpreferences.getString("sala" + i, "vazio");
                //Log.e("saveprefs", str);
                this.remove("sala" + i);
            }
        }
    }

    /*public void setListSala (List<Sala> listSalas) {
        buffer = "";

        int i = 0;
        for (Sala s : listSalas) {
            if (i < listSalas.size() - 1)
                buffer = buffer + s.toString() + "@@";
            else
                buffer += s.toString();
            i++;
        }

        if (!buffer.equals("")) {
            getEditor().putString("salas", buffer);
            getEditor().commit();
        }
    }*/

    public void setListSala (String[] str) {
        if (str != null) {
            int tamanho = str.length;
            getEditor().putString("salas", String.valueOf(tamanho));
            getEditor().commit();
            for (int i = 0; i < tamanho; i++) {
                getEditor().putString("salas" + i, str[i]);
                getEditor().commit();
            }
        }
    }

    public List<Sala> getListSala() {
        List<Sala> listSalas = new ArrayList<>();
        List<String> listStr = new ArrayList<>();
        Sala s = new Sala();
        int tamanho = 0;
        //System.out.printf("SavedPrefs>getListSala: Antes do if hasKey(salas)");
        if (hasKey("salas")) {
            tamanho = Integer.parseInt(sharedpreferences.getString("salas", "vazio"));
            vectorBuffer = new String[tamanho];
            //System.out.printf("SavedPrefs>getListSala: Antes do for");
            for (int i = 0; i < tamanho; i++) {
                buffer = sharedpreferences.getString("salas" + i, "vazio");
                //Log.d("SavedPrefs",buffer);
                if (!buffer.equals("$concat")) {
                    //System.out.printf("SavedPrefs>getListSala: No if !buffer.equals($concat)");
                    listStr.add(buffer);
                } else {
                    String[] vect = new String[listStr.size()];
                    int j = 0;
                    for (String str : listStr)
                        vect[j++] = str;
                    listStr.clear();
                    s = new Sala();
                    s.toSala(vect);
                    listSalas.add(s);
                }
            }
            //System.out.printf("SavedPrefs>getListSala: depois do for");
        }
        /*for (Sala sala : listSalas)
            Log.d("savedprefsSal", sala.getNome());*/
        return listSalas;
    }

    public void removeListSalas() {
        List<Sala> listSalas = new ArrayList<>();
        List<String> listStr = new ArrayList<>();
        Sala s = new Sala();
        int tamanho = 0;
        if (hasKey("salas")) {
            tamanho = Integer.parseInt(sharedpreferences.getString("salas", "vazio"));
            this.remove("salas");
            vectorBuffer = new String[tamanho];
            for (int i = 0; i < tamanho; i++) {
                this.remove("salas" + i);
            }
        }
    }

    /*public void setConjunto (ConjuntoQuestoes cq) {
        vectorBuffer = cq.toVectorString();
        getEditor().putString("conjunto", String.valueOf(2+cq.getQuestoes().size()+cq.getRespostas().size()));
        getEditor().commit();
        for (int i = 0; i < vectorBuffer.length; i++) {
            getEditor().putString("conjunto" + i, vectorBuffer[i]);
            getEditor().commit();
        }
        //for (int i = 0; i < vectorBuffer.length; i++) {
          //  Log.d("SavePrefsetCOnj", vectorBuffer[i]);
        //}
    }*/

    /*public ConjuntoQuestoes getConjunto() {
        ConjuntoQuestoes cq = new ConjuntoQuestoes();
        Questao q = new Questao();
        Resposta r = new Resposta();
        List<Questao> questoes = new ArrayList<>();
        List<Resposta> respostas = new ArrayList<>();
        boolean adcQuestoes = true;
        if (hasKey("conjunto")) {
            buffer = sharedpreferences.getString("conjunto", "vazio");
            //Log.d("SavePrefBuffer", buffer);
            int tamanho = Integer.parseInt(buffer);
            buffer = sharedpreferences.getString("conjunto0", "vazio");
            //Log.d("SavePrefBuffer", buffer);
            cq.setId(Integer.parseInt(buffer));

            for (int i = 1; i < tamanho; i++) {
                buffer = sharedpreferences.getString("conjunto"+i, "vazio");
                Log.d("SavePrefBuffer"+i, buffer);
                if (adcQuestoes) {
                    if (buffer.equals("$$"))
                        adcQuestoes = false;
                    else {
                        q = new Questao();
                        q.toQuestao(buffer);
                        cq.getQuestoes().add(q);
                    }
                } else {
                    r = new Resposta();
                    r.toResposta(buffer);
                    cq.getRespostas().add(r);
                }
            }
        }
        Log.d("saveprefsCQid", cq.getId() + "");
        for (Questao q1 : cq.getQuestoes())
            Log.d("saveprefsCQQuest", q1.getQuestao());
        for (Resposta r1 : cq.getRespostas())
            Log.d("saveprefsCQResp", r1.getResposta());
        return cq;
    }*/

    public void setRanking(Ranking ran, Usuario user) {
        getEditor().putString("ranking", ran.toString(user));
        getEditor().commit();
    }

    public Ranking getRanking() {
        Ranking r = new Ranking();
        if (hasKey("ranking")) {
            buffer = sharedpreferences.getString("ranking", "vazio");
            Log.e("saveprefs", buffer);
            r.toRanking(buffer);
        }
        return r;
    }

    public void setRankings (List<Ranking> listRanking) {
        vectorBuffer = new String[listRanking.size()];
        int i = 0;
        for (Ranking r : listRanking) {
            vectorBuffer[i++] = r.toString(r.getUsuario());
        }
        getEditor().putString("rankings", this.toString(vectorBuffer));
        getEditor().commit();
    }

    public List<Ranking> getRankings() {
        List<Ranking> lr = new ArrayList();
        Ranking r;
        if (hasKey("rankings")) {
            buffer = sharedpreferences.getString("rankings", "vazio");
            if (buffer != "") {
                vectorBuffer = toVectorString(buffer);
                for (int i = 0; i < vectorBuffer.length; i++) {
                    r = new Ranking();
                    r.toRanking(vectorBuffer[i]);
                    lr.add(r);
                }
            }
        }
        return lr;
    }

    public void setRankingsTemp (List<Ranking> listRanking) {
        vectorBuffer = new String[listRanking.size()];
        int i = 0;
        for (Ranking r : listRanking) {
            vectorBuffer[i++] = r.toString(r.getUsuario());
        }
        getEditor().putString("rankingstemp", this.toString(vectorBuffer));
        getEditor().commit();
    }

    public List<Ranking> getRankingsTemp() {
        List<Ranking> lr = new ArrayList();
        if (hasKey("rankingstemp")) {
            buffer = sharedpreferences.getString("rankingstemp", "vazio");
            if (buffer != "") {
                vectorBuffer = toVectorString(buffer);
                for (int i = 0; i < vectorBuffer.length; i++) {
                    if (vectorBuffer[i] != "") {
                        lr.add(new Ranking(vectorBuffer[i]));
                    }
                }
            }
        }
        return lr;
    }


    /*public boolean salvarStr (String key, String value) {
        try {
            editor.putString(key, value);
        } catch (Exception ex) {
            setMsg(ex.getMessage());
            setOkay(false);
        } finally {
            return true;
        }
    }

    public boolean salvarInt (String key, int value) {
        try {
            editor.putInt(key, value);
        } catch (Exception ex) {
            setMsg(ex.getMessage());
            setOkay(false);
        } finally {
            return true;
        }
    }

    //TODO verificar depois erros de consistência de dados

    public boolean salvarDados (String key, byte [] value) {
        try {
            editor.putString(key, value.toString());
        } catch (Exception ex) {
            setMsg(ex.getMessage());
            setOkay(false);
        } finally {
            return true;
        }
    }*/

    public boolean hasKey (String key) {
        return sharedpreferences.contains(key);
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public void remove(String key) {
        getEditor().remove(key);
        getEditor().commit();
    }

    public void clear() {
        getEditor().clear();
        getEditor().commit();
    }
}

package br.com.feapps.asking.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.feapps.asking.DAO.ConjuntoDAO;
import br.com.feapps.asking.DAO.SalaDAO;
import br.com.feapps.asking.DAO.UsuarioDAO;
import br.com.feapps.asking.R;
import br.com.feapps.asking.model.ConjuntoQuestoes;
import br.com.feapps.asking.model.Sala;
import br.com.feapps.asking.model.Usuario;
import br.com.feapps.asking.util.Cript;
import br.com.feapps.asking.util.SavedPreferences;
import br.com.feapps.asking.util.UsuarioArrayAdapter;
import br.com.feapps.asking.util.ViewHelper;

public class SalaActivity extends AppCompatActivity implements Runnable{

    private TextView txtNomeSala;
    private TextView txtTipoSala;
    private ListView listViewLadoA;
    private ListView listViewLadoB;
    private ImageButton imgButtonAtualiza;

    private ArrayAdapter<Usuario> adpUsersA;
    private ArrayAdapter<Usuario> adpUsersB;

    SavedPreferences savedPreferences;

    private Sala sala;
    private List<Usuario> usuarios;
    Usuario user;

    String tipoExecThread;
    Context context;
    Thread thread;

    SalaDAO salaDAO;
    UsuarioDAO usuarioDAO;
    ConjuntoDAO conjuntoDAO;

    Cript cript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        //Inciando salaDAO, classe criptografica e conjuntoDAO
        salaDAO = new SalaDAO();
        usuarioDAO = new UsuarioDAO();
        conjuntoDAO = new ConjuntoDAO();
        cript = new Cript("");

        //Pegando informações salvas
        savedPreferences = new SavedPreferences(this);
        //Log.d("Sala", "Pegando sala de preferencias no oncreate");
        sala = savedPreferences.getSala();
        usuarios = sala.getUsuarios();

        //Ligando variáveis da activity
        txtNomeSala = (TextView) findViewById(R.id.txtNomeSala);
        txtTipoSala = (TextView) findViewById(R.id.txtTipoSala);
        listViewLadoA = (ListView) findViewById(R.id.listViewLadoA);
        listViewLadoB = (ListView) findViewById(R.id.listViewLadoB);
        imgButtonAtualiza = (ImageButton) findViewById(R.id.imgButtonAtualiza);

        //Setando variáveis da activity
        txtNomeSala.setText(sala.getNome());
        if (sala.getSubArea().getId() == 0)
            txtTipoSala.setText(sala.getArea().getNome() + "(" + sala.getTipo() + ")");
        else
            txtTipoSala.setText(sala.getArea().getNome() + ">" + sala.getSubArea().getNome() + "(" + sala.getTipo() + ")");

        //Definindo arraysadapters
        adpUsersA = new UsuarioArrayAdapter(this, R.layout.activity_lista_usuario_sala);
        adpUsersB = new UsuarioArrayAdapter(this, R.layout.activity_lista_usuario_sala);
        List<Usuario> listUser = sala.getUsuarios();

        int i =0;
        for (Usuario u : listUser) {
            if (i < sala.getMaxUserSala()/2)
                adpUsersA.add(u);
            else
                adpUsersB.add(u);
            i++;
        }
        listViewLadoA.setAdapter(adpUsersA);
        listViewLadoB.setAdapter(adpUsersB);

        context = this;
        imgButtonAtualiza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipoExecThread = "atualizaSala";
                thread = new Thread((Runnable)context);
                thread.start();
            }
        });

    }

    @Override
    public void run() {
        if (tipoExecThread.equals("atualizaSala")) {
            //sala = savedPreferences.getSala();
            //System.out.printf("ID DA SALA A SER ATUALIZADA: " + sala.getId());
            //List<Usuario> users = new ArrayList<>();
            try {
                sala = salaDAO.getSalaForId(sala.getId(), cript.getKey());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Log.d("SalaActivity>run()", sala.toString());
            //Log.d("Sala", "Removendo sala de preferencias depois de atualizar do banco de dadoss");
            savedPreferences.setSala(sala);

            if (sala.getUsuarios().size() == sala.getMaxUserSala()) {
                Intent i = new Intent(SalaActivity.this, JogoActivity.class);
                startActivity(i);
                thread.interrupt();
                finish();
            } else if (sala.getUsuarios().isEmpty()) {
                //Log.d("Sala", "Removendo sala de preferencias depois de ver que não há usuários na sala");
                savedPreferences.removeSala();

                Intent intent = new Intent(SalaActivity.this, NavigationActivity.class);
                startActivity(intent);
                thread.interrupt();
                finish();
            } else {
                Intent i = new Intent(SalaActivity.this, SalaActivity.class);
                startActivity(i);
                thread.interrupt();
                finish();
            }

            /*int i = 0;
            this.adpUsersA.clear();
            this.adpUsersB.clear();
            for (Usuario u : users) {
                if (i < sala.getMaxUserSala() / 2)
                    this.adpUsersA.add(u);
                else
                    this.adpUsersB.add(u);
                i++;
            }*/


            /*Sala sala = savedPreferences.getSala();
            List<Usuario> usersAtuais = sala.getUsuarios();
            List<Usuario> usersNovos = new ArrayList<>();
            try {
                usersNovos = salaDAO.getSalaForId(sala.getId(), cript.getKey()).getUsuarios();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int i = 0;
            if (!usersNovos.isEmpty()) {
                for (Usuario ua : usersAtuais) {
                    if (ua.getId() != usersNovos.get(i).getId()) {
                        sala.setUsuarios(usersNovos);
                        savedPreferences.setSala(sala);
                        adpUsersA.clear();
                        adpUsersB.clear();
                        int j = 0;
                        for (Usuario u : usersNovos) {
                            if (j < usersNovos.size() / 2)
                                adpUsersA.add(u);
                            else
                                adpUsersB.add(u);
                            j++;
                        }
                        listViewLadoA.setAdapter(adpUsersA);
                        listViewLadoB.setAdapter(adpUsersB);
                    }
                    i++;
                }
            }*/
        } else if (tipoExecThread.equals("apagarSalaESair")) {
            try {
                salaDAO.deletarSala(sala.getId(), cript.getKey());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Log.d("Sala", "Removendo sala de preferencias depois de apagar e sair da sala");
            savedPreferences.removeSala();

            Intent intent = new Intent(SalaActivity.this, NavigationActivity.class);
            startActivity(intent);
            thread.interrupt();
            finish();
        } else if (tipoExecThread.equals("removeUsuario")) {
            /*try {
                usuarioDAO.saveUser(user.toString(), cript.getKey());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            try {
                salaDAO.removeUserSala(user.getId(), sala.getId(), cript.getKey());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //Log.d("Sala", "Removendo sala de preferencias depois de remover usuários");
            savedPreferences.removeSala();

            Intent intent = new Intent(SalaActivity.this, NavigationActivity.class);
            startActivity(intent);
            thread.interrupt();
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        user = savedPreferences.getUser();
        //Log.d("Sala", "Pegando sala de preferencias depois de apertão botão de voltar");
        sala = savedPreferences.getSala();

        sala.getUsuarios().remove(user);

        if (sala.getUsuarios().isEmpty()) {
            tipoExecThread = "apagarSalaESair";
            thread = new Thread(this);
            thread.start();
        } else {
            tipoExecThread = "removeUsuario";
            thread = new Thread(this);
            thread.start();
        }
    }
}

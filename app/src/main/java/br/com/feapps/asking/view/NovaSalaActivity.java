package br.com.feapps.asking.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.NetworkOnMainThreadException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.feapps.asking.DAO.AreaDAO;
import br.com.feapps.asking.DAO.ConjuntoDAO;
import br.com.feapps.asking.DAO.SalaDAO;
import br.com.feapps.asking.DAO.WebServiceAskingDB;
import br.com.feapps.asking.R;
import br.com.feapps.asking.model.Area;
import br.com.feapps.asking.model.ConjuntoQuestoes;
import br.com.feapps.asking.model.Questao;
import br.com.feapps.asking.model.Resposta;
import br.com.feapps.asking.model.Sala;
import br.com.feapps.asking.model.SubArea;
import br.com.feapps.asking.model.Usuario;
import br.com.feapps.asking.util.CONST;
import br.com.feapps.asking.util.Cript;
import br.com.feapps.asking.util.MessageBox;
import br.com.feapps.asking.util.SavedPreferences;
import br.com.feapps.asking.util.ViewHelper;

public class NovaSalaActivity extends AppCompatActivity implements Runnable, AdapterView.OnItemSelectedListener, View.OnClickListener{

    ProgressDialog dialog;

    private View formSalvar;
    private View cadProgress;

    AutoCompleteTextView edtNomeSala;

    Spinner spnTipoSala;
    Spinner spnAreas;
    Spinner spnSubAreas;

    private ArrayAdapter<String> adpTipoSala;
    private ArrayAdapter<String> adpAreas;
    private ArrayAdapter<String> adpSubAreas;

    EditText edtSenha1;
    EditText edtSenha2;

    View focusView;

    FloatingActionButton fabNovaSala;

    //Variáveis para servidor
    private Handler handler = new Handler();
    Cript cript;
    Thread thread;
    String tipeExecThread;
    List<Area> areas = new ArrayList<>();
    int idArea;
    List<SubArea> subAreas;
    String[] listSala;
    Sala sala;

    //Variáveis de uso local
    SavedPreferences savedPreferences;
    int numAreas;
    int numSubAreas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_sala);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Tratamento de mensagens de erro
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("msg")) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        extras.getString("msg"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        }

        //Pegando preferencias salvas, setando o encriptamento e inicializando sala
        savedPreferences = new SavedPreferences(this);
        cript = new Cript("");

        //Setando variáveis de Activity
        formSalvar = findViewById(R.id.formSalvar);
        cadProgress = findViewById(R.id.cad_progress);
        edtNomeSala = (AutoCompleteTextView) findViewById(R.id.edtNomeSala);
        spnTipoSala = (Spinner) findViewById(R.id.spnTipoSala);
        spnAreas = (Spinner) findViewById(R.id.spnAreas);
        spnSubAreas = (Spinner) findViewById(R.id.spnSubAreas);
        edtSenha1 = (EditText) findViewById(R.id.edtSenha1);
        edtSenha2 = (EditText) findViewById(R.id.edtSenha2);

        //Definindo ação para seleção de item do spnAreas
        spnAreas.setOnItemSelectedListener(this);

        adpTipoSala = ViewHelper.createArrayAdapter(this, spnTipoSala);
        adpAreas = ViewHelper.createArrayAdapter(this, spnAreas);
        adpSubAreas = ViewHelper.createArrayAdapter(this, spnSubAreas);

        //adpTipoSala.add("Tipo de sala");
        //adpAreas.add("Área do conhecimento");
        //adpSubAreas.add("");

        adpTipoSala.add("1 Vs 1");

        //Pegando lista de áreas do SavedPreferences
        /*tipeExecThread = "listarAreas";
        final Context context = this;
        final Runnable runnable = this;
        thread = new Thread(runnable);
        thread.start();*/
        areas = savedPreferences.getAreas();
        //Log.d("AreasTamanho", areas.size()+"");
        for (Area a : areas) {
            adpAreas.add(a.getNome());
            //Log.d("Area adicionada:", a.getNome());
        }

        /*FloatingActionButton fabCancelar = (FloatingActionButton) findViewById(R.id.fabCancelar);
        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
                Intent i = new Intent ( NovaSalaActivity.this, NavigationActivity.class);
                startActivity(i);
                //thread.interrupt();
                finish();
            }
        });*/


        fabNovaSala = (FloatingActionButton) findViewById(R.id.fabNovaSala);
        fabNovaSala.setOnClickListener(this);
    }

    @Override
    public void run() {
        /*if (tipeExecThread.equals("listarAreas")) {
            try {
                tipeExecThread = "";
                //WebServiceAskingDB ws = new WebServiceAskingDB();
                AreaDAO aDAO = new AreaDAO();

                areas = aDAO.listarAreas(cript.getKey());
                numAreas = areas.size();
                try {
                    for (Area a : areas) {
                        adpAreas.add(a.getNome());
                        Log.d("Area adicionada", a.getId() + " - " + a.getNome());
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }


                if (!aDAO.isOkay()) {
                    Intent it = new Intent(NovaSalaActivity.this, NovaSalaActivity.class);
                    it.putExtra("msg", aDAO.getMsg());
                    startActivity(it);
                    finish();
                    MessageBox.show(this,"Erro", aDAO.getMsg(), null);
                }

            } catch (Exception ex) {
                Log.e("NovaSalaActivity", "Erro", ex);
            } finally {
                thread.interrupt();
            }
        } else if (tipeExecThread.equals("listarSubAreas")) {
            try {
                //WebServiceAskingDB ws = new WebServiceAskingDB();
                AreaDAO aDAO = new AreaDAO();

                subAreas = aDAO.listarSubAreas(idArea, cript.getKey());

                numSubAreas = subAreas.size();
                Log.d("numSubAreas", "Tamanho: " + subAreas.size());
                try {
                    for (SubArea sa : subAreas) {
                        adpSubAreas.add(sa.getNome());
                        Log.d("SubArea adicionada", sa.getId() + " - " + sa.getNome());
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                    Log.e("Exception", ex.getMessage());
                }


                if (!aDAO.isOkay()) {
                    Intent it = new Intent(NovaSalaActivity.this, NovaSalaActivity.class);
                    it.putExtra("msg", aDAO.getMsg());
                    startActivity(it);
                    finish();
                    MessageBox.show(this,"Erro", aDAO.getMsg(), null);
                }

            } catch (Exception ex) {
                Log.e("NovaSalaActivity", "Erro", ex);
            } finally {
                thread.interrupt();
            }
        } else */if (tipeExecThread.equals("CriarNovaSala")) {
            try {
                //WebServiceAskingDB ws = new WebServiceAskingDB();
                SalaDAO salaDAO = new SalaDAO();
                ConjuntoDAO conjuntoDAO = new ConjuntoDAO();

                Sala sala = salaDAO.criarSala(5, listSala, cript.getKey());
                //ConjuntoQuestoes cq = conjuntoDAO.getConjuntoQuestoes(sala.getId(), cript.getKey());

                /*for (Questao q : cq.getQuestoes())
                    Log.d("NovaSalaActQuestao", q.getQuestao());

                for (Resposta r : cq.getRespostas())
                    Log.d("NovaSalaActQuestao", r.getResposta());*/

                //TODO adicionar modo de escolher quantidade de questão por sala

                //ConjuntoQuestoes conjuntoQuestoes = conjuntoDAO.gerarConjunto(
                //        5, sala.getArea().getId(), sala.getSubArea().getId(), cript.getKey());

                //Atualizando questões da sala
                //salaDAO.atualizaSalaIdConjQuestoes(sala.getId(), conjuntoQuestoes.getId(), cript.getKey());

                if (!salaDAO.isOkay()) {
                    Intent it = new Intent(NovaSalaActivity.this, NovaSalaActivity.class);
                    it.putExtra("msg", salaDAO.getMsg());
                    startActivity(it);
                    finish();
                    MessageBox.show(this,"Erro", salaDAO.getMsg(), null);
                } else {
                    if (sala.getId() != 0) {
                        //Mudar de para SalaActivity
                        Intent it = new Intent(NovaSalaActivity.this, SalaActivity.class);

                        //Log.d("newSala", "Mostrando questões de nova sala em preferencias");
                        //for (Questao questao : sala.getConjuntoQuestoes().getQuestoes())
                        //Log.d("newSala", questao.getQuestao());

                        //Log.d("newSala", "Salvando sala em preferencias");
                        savedPreferences.setSala(sala);
                        //savedPreferences.setConjunto(cq);
                        it.putExtra("msg", "Criado com sucesso!");
                        startActivity(it);
                        finish();
                    } else if (sala.getId() == 0) {
                        Intent it = new Intent(NovaSalaActivity.this, NavigationActivity.class);
                        it.putExtra("msg", "ERRO: Nome de sala já existe.");
                        //SalvaExtras(it);
                        startActivity(it);
                        finish();
                    }else if (sala.getId() == -1){
                        Intent it = new Intent(NovaSalaActivity.this, NavigationActivity.class);
                        it.putExtra("msg", "ERRO: falha ao cadastrar usuário. Atualize seu relógio e tente novamente.");
                        //SalvaExtras(it);
                        startActivity(it);
                        finish();
                    }
                }

            } catch (Exception ex) {
                Log.e("NovaSalaActivity", "Erro", ex);
            } finally {
                thread.interrupt();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String areaStr = (String) parent.getItemAtPosition(position);
        //Log.d("Nome", "nome: " + areaStr);
        Area area = new Area();
        for (Area a : areas) {
            if (a.getNome().equals(areaStr))
                area = a;
            //Log.d("Comparou", areaStr + " com " + a.getNome() + ", cujo id é: " + a.getId());
        }
        //Log.d("Resultado do idArea", "" + idArea);

        //Pegando lista de subáreas da lista de áreas já carregada na activity
        /*tipeExecThread = "listarSubAreas";
        thread = new Thread(this);
        thread.start();*/
        adpSubAreas = ViewHelper.createArrayAdapter(this, spnSubAreas);
        //adpSubAreas.add("");
        for (SubArea sa : area.getSubAreas()) {
            adpSubAreas.add(sa.getNome());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("onNothingSelected", "onNothingSelected");
    }

    @Override
    public void onClick(View v) {
        fabNovaSala.hide();

        boolean cancel = false;

        listSala = new String[8];

        listSala[0] = edtNomeSala.getText().toString();
        listSala[1] = String.valueOf(spnTipoSala.getSelectedItem());

        //TODO criar salas com senhas

        String senha1 = ""; //edtSenha1.getText().toString();
        String senha2 = ""; //edtSenha2.getText().toString();

        //Descobrindo Area por nome
        Area tempa = new Area();
        for (Area a : areas) {
            if (spnAreas.getSelectedItem().toString().equals(a.getNome()))
                tempa = a;
        }
        listSala[3] = String.valueOf(tempa.getId());
        listSala[4] = tempa.getNome();

        //Descobrindo subArea
        subAreas = tempa.getSubAreas();
        SubArea tempsa = new SubArea();
        for (SubArea sa : subAreas) {
            if (spnSubAreas.getSelectedItem().toString().equals(sa.getNome()))
                tempsa = sa;
        }
        listSala[5] = String.valueOf(tempsa.getId());
        listSala[6] = tempsa.getNome();

        //Pegando Usuário criador da sala
        Usuario user = savedPreferences.getUser();

        listSala[7] = String.valueOf(user.getId());

        if (TextUtils.isEmpty(listSala[0])) {
            edtNomeSala.setError(getString(R.string.error_field_required));
            focusView = edtNomeSala;
            cancel = true;
        } else if (!senha1.equals(senha2)) {
            edtSenha1.setError("As senhas não coincidem.");
            focusView = edtSenha1;
            cancel = true;
        }
        cript = new Cript(senha1);
        listSala[2] = cript.getCript();

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
            tipeExecThread = "CriarNovaSala";
            thread = new Thread(this);
            thread.start();
        }

    }

    /**
     * Shows the progress UI and hides the cadastro form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            formSalvar.setVisibility(show ? View.GONE : View.VISIBLE);
            formSalvar.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    formSalvar.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            cadProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            cadProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cadProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            cadProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            formSalvar.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent ( NovaSalaActivity.this, NavigationActivity.class);
        startActivity(i);
        finish();
    }
}

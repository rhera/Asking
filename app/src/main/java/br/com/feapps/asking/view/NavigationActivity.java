package br.com.feapps.asking.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapFault;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

import br.com.feapps.asking.DAO.AreaDAO;
import br.com.feapps.asking.DAO.ConjuntoDAO;
import br.com.feapps.asking.DAO.SalaDAO;
import br.com.feapps.asking.DAO.WebServiceAskingDB;
import br.com.feapps.asking.R;
import br.com.feapps.asking.model.Area;
import br.com.feapps.asking.model.ConjuntoQuestoes;
import br.com.feapps.asking.model.Questao;
import br.com.feapps.asking.model.Sala;
import br.com.feapps.asking.model.SubArea;
import br.com.feapps.asking.model.Usuario;
import br.com.feapps.asking.util.CONST;
import br.com.feapps.asking.util.Cript;
import br.com.feapps.asking.util.MessageBox;
import br.com.feapps.asking.util.SalaArrayAdapter;
import br.com.feapps.asking.util.SavedPreferences;
import br.com.feapps.asking.util.ViewHelper;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    Runnable,
                    AdapterView.OnItemClickListener{

    private AutoCompleteTextView edtPesquisa;
    private ImageButton imgButPesquisa;
    private Spinner spnArea;
    private Spinner spnSubArea;
    private ListView listViewSalas;

    private Context context;

    private ArrayAdapter<String> adpArea;
    private ArrayAdapter<String> adpSubArea;
    private ArrayAdapter<Sala> adpSalas;

    //private SavedPreferences savedPreferences = new SavedPreferences();
    //SharedPreferences sharedpreferences;
    private SavedPreferences savedPreferences;

    String tipoExecThread;
    Thread thread;
    Cript cript = new Cript("");

    private List<Area> listAreas;
    private List<Sala> listSala;

    Sala sala;
    Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Define qual o layout a ser inflado
        /*vs = new ViewStub(this);
        vs.setLayoutResource(R.layout.content_pessoas);
        View conteudoPrincipal = (View) vs.inflate();*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Pegando preferencias salvas
        //sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        savedPreferences = new SavedPreferences(this);

        //Redireciona se usuário não estiuver logado
        if (!savedPreferences.hasKey("user")) {
            /*SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();*/
            savedPreferences.clear();
            Intent i = new Intent ( NavigationActivity.this, LoginActivity.class);
            startActivity(i);
            this.finish();
        }

        //Tratamento de mensagens de erro e parametros entre activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("msg")) {
                /*Toast toast= Toast.makeText(getApplicationContext(),
                        extras.getString("msg"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();*/
                MessageBox.show(this, "Mensagem", extras.getString("msg"), null);
            }
            if (extras.containsKey("menuId")) {

            }
            extras.clear();
        }

        //Inicializando objetos da activity
        edtPesquisa = (AutoCompleteTextView) findViewById(R.id.edtPesquisa);
        imgButPesquisa = (ImageButton) findViewById(R.id.imgButPesquisa);
        spnArea = (Spinner) findViewById(R.id.spnArea);
        spnSubArea = (Spinner) findViewById(R.id.spnSubArea);
        adpSalas = new SalaArrayAdapter(this, R.layout.activity_lista_salas);
        listViewSalas = (ListView) findViewById(R.id.listViewSalas);

        //Definindo ação para seleção de item do spnArea e spnSubArea
        spnArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Definindo ação para seleção de item do spnArea e spnSubArea
        spnSubArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Definindo ação para botão de atualizar salas
        imgButPesquisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NavigationActivity.this, LoadActivity.class);
                startActivity(i);
                finish();
            }
        });

        adpArea = ViewHelper.createArrayAdapter(this, spnArea);
        adpSubArea = ViewHelper.createArrayAdapter(this, spnSubArea);

        //Populando adpArea
        listAreas = savedPreferences.getAreas();
        adpArea.add("Áreas");
        //Log.d("NavActListAreas", listAreas.size() + "");
        for (Area a : listAreas) {
            adpArea.add(a.getNome());
        }
        adpSubArea.add("");

        //Atualiza adpSala
        listSala = savedPreferences.getListSala();
        for (Sala s : listSala) {
            if (s.isVisivel())
                adpSalas.add(s);
        }
        listViewSalas.setAdapter(adpSalas);

        //Filtrar informação atravez da caixa de pesquisa
        FiltraDados filtraDados = new FiltraDados(adpSalas);
        edtPesquisa.addTextChangedListener(filtraDados);

        //Botões
        listViewSalas.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent i = new Intent(NavigationActivity.this, NovaSalaActivity.class);
                startActivity(i);
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Thread que verifica número de salas
        /*tipoExecThread = "verificaNumSalas";
        context = this;
        thread = new Thread(this);
        thread.start();*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        int searchViewResId;

        if (id == R.id.nav_salas) {
            Intent i = new Intent ( NavigationActivity.this, LoadActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_perfil) {
            Intent i = new Intent ( NavigationActivity.this, PerfilActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_pessoas) {
            Intent i = new Intent ( NavigationActivity.this, UsuariosActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_ranking) {
            Intent i = new Intent ( NavigationActivity.this, RankingActivity.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_sair) {
            //Encerrando conexão e voltando para o Activity de login
            /*SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();*/
            savedPreferences.clear();
            Intent it = new Intent(NavigationActivity.this, LoginActivity.class);
            startActivity(it);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void run() {
        if (tipoExecThread.equals("verificaNumSalas")) {
            /*SalaDAO salaDAO = new SalaDAO();
            int numSalasBD = 0;
            try {
                numSalasBD = salaDAO.getNumSalasAtivas(cript.getKey());
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            }

            int numSalasApp = listSala.size();

            if (numSalasBD != numSalasApp) {
                String strSalas = "";
                try {
                    strSalas = salaDAO.listarSalas();
                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                }
                //Salvar nova lista nas preferencias
                savedPreferences.setListSala(strSalas);

                listSala = savedPreferences.getListSala();

                //Atualiza adpSala
                for (Sala s : listSala) {
                    adpSalas.add(s);
                }
                listViewSalas.setAdapter(adpSalas);
            }*/
        } else if (tipoExecThread.equals("entraSalaBD")) {
            SalaDAO salaDAO = new SalaDAO();
            try {
                sala = salaDAO.adcUsuario(sala.getId(), user.toString(), cript.getKey());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*ConjuntoDAO cDAO = new ConjuntoDAO();
            ConjuntoQuestoes cq = new ConjuntoQuestoes();
            try {
                cq = cDAO.getConjuntoQuestoes(sala.getId(), cript.getKey());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            if (sala.getId() != 0) {
                /*for (Usuario usuario : sala.getUsuarios())
                    Log.d("NavAct", usuario.getNome());*/
                Intent it = new Intent(context, SalaActivity.class);

                //it.putExtra(CONST.EXTRA_SALA, (Parcelable) sala);
                //Log.e("naviact", "Mostrando sala depois de adicionar usuário");
                //for (Questao q : sala.getConjuntoQuestoes().getQuestoes())
                //Log.e("naviact", q.getQuestao());
                //Log.e("naviact", "Salvando sala depois de adicionar usuário");
                savedPreferences.setSala(sala);
                //savedPreferences.setConjunto(cq);

                thread.interrupt();

                startActivityForResult(it, 0);
            } else {
                Intent it = new Intent(context, LoadActivity.class);
                it.putExtra("msg", "Sala não existe mais ou está cheia. Crie uma nova para jogar.");
                startActivity(it);
                thread.interrupt();
                finish();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Log.d("naviact", "Pegando sala selecionada");
        sala = adpSalas.getItem(position);
        user = savedPreferences.getUser();
        //sala.getUsuarios().add(user);

        context = this;
        tipoExecThread = "entraSalaBD";
        thread = new Thread(this);
        thread.start();
    }

    private class FiltraDados implements TextWatcher {

        private ArrayAdapter<Sala> arrayAdapter;

        private FiltraDados (ArrayAdapter<Sala> arrayAdapter) {
            this.arrayAdapter = arrayAdapter;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            arrayAdapter.getFilter().filter(s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}

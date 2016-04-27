package br.com.feapps.asking.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.NetworkOnMainThreadException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import org.ksoap2.SoapFault;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.feapps.asking.DAO.AreaDAO;
import br.com.feapps.asking.DAO.RankingDAO;
import br.com.feapps.asking.DAO.SalaDAO;
import br.com.feapps.asking.DAO.UsuarioDAO;
import br.com.feapps.asking.DAO.WebServiceAskingDB;
import br.com.feapps.asking.R;
import br.com.feapps.asking.model.Area;
import br.com.feapps.asking.model.Ranking;
import br.com.feapps.asking.model.Sala;
import br.com.feapps.asking.model.SubArea;
import br.com.feapps.asking.model.Usuario;
import br.com.feapps.asking.util.Cript;
import br.com.feapps.asking.util.MessageBox;
import br.com.feapps.asking.util.SavedPreferences;

public class LoadActivity extends AppCompatActivity implements Runnable {

    View mProgressView;
    LinearLayout linLayLoad;

    //SharedPreferences sharedpreferences;
    SavedPreferences savedPreferences;

    String tipoExecThread;
    Thread thread;
    Context context;

    Bundle extras;

    Cript cript;

    DialogInterface.OnClickListener listener = null;

    boolean isServerOff = true;

    List<Area> areas;
    List<SubArea> subAreas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        mProgressView = (View) findViewById(R.id.login_progress);
        showProgress(true);

        linLayLoad = (LinearLayout) findViewById(R.id.linLayLoad);
        linLayLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoadActivity.this, LoadActivity.class);
                startActivity(it);
                finish();
            }
        });

        tipoExecThread = "carregaDados";
        context = this;
        thread = new Thread(this);
        thread.start();

        listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent it = new Intent(LoadActivity.this, LoadActivity.class);
                startActivity(it);
                finish();
            }
        };

        if (isServerOff) {
            MessageBox.show(this, "Servidor:", "Servidor fora de alcance. Aguarde alguns minutos e tente novamente.", listener);
        }

    }

    @Override
    public void run() {
        if (tipoExecThread.equals("verificaServidor")) {

        } else if (tipoExecThread.equals("carregaDados")) {
            cript = new Cript("");
            WebServiceAskingDB ws = new WebServiceAskingDB();
            String resultHello = "0";
            try {
                resultHello = ws.hello("app", cript.getKey());
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NetworkOnMainThreadException nomte) {
            }
            Log.d("Resposta do servidor: ", resultHello);
            if (resultHello.equals("online")) {
                isServerOff = false;

                //TODO Salvar dados de aplicação

                //Pegando preferencias de usuário
                //sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                savedPreferences = new SavedPreferences(this);

                //Pegar lista de salas atuais e salvar
                SalaDAO sDAO = new SalaDAO();
                String[] listSala = new String[1];
                try {
                    listSala = sDAO.listarSalas();
                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                }
                if (savedPreferences.hasKey("salas")) {
                    savedPreferences.removeListSalas();
                }
                if (listSala!=null) {
                    savedPreferences.setListSala(listSala);
                }

                //Limpa rankings temporários
                savedPreferences.remove("rankingstemp");

                //Pegar lista de áreas com suas subáreas e salvar
                AreaDAO aDAO = new AreaDAO();
                List<Area> listArea = new ArrayList<>();
                try {
                    listArea = aDAO.listarAreas(cript.getKey());
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                savedPreferences.setAreas(listArea);

                //Pega lista de usuarios e salva
                UsuarioDAO uDAO = new UsuarioDAO();
                List<Usuario> listUsers = new ArrayList<>();
                try {
                    listUsers = uDAO.listarUsuarios();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                savedPreferences.saveListUsers(listUsers);

                //Pega lista de rankings e salva
                RankingDAO rDAO = new RankingDAO();
                List<Ranking> lRank = new ArrayList<>();
                try {
                    lRank = rDAO.listarRankings();
                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                }
                savedPreferences.setRankings(lRank);

                //Verifica se usuario esta logado
                if (savedPreferences.hasKey("user")) {
                    /*try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/

                    /*thread.interrupt();
                    tipoExecThread = "carregaNavigationActivity";
                    thread = new Thread((Runnable) context);
                    thread.start();*/

                    //Atualizar sala de usuario como nula
                    Usuario u = savedPreferences.getUser();
                    try {
                        u = uDAO.resetarSala(u.getId());
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (u != null)
                        savedPreferences.saveUser(u);

                    Intent i = new Intent(LoadActivity.this, NavigationActivity.class);
                    extras = getIntent().getExtras();
                    if (extras != null) {
                        if (extras.containsKey("msg")) {
                            String msg = getIntent().getExtras().getString("msg");
                            i.putExtra("msg", msg);
                        }
                    }
                    startActivity(i);
                    thread.interrupt();
                    finish();

                } else {
                    Intent i = new Intent(LoadActivity.this, LoginActivity.class);
                    extras = getIntent().getExtras();
                    if (extras != null) {
                        if (extras.containsKey("msg")) {
                            String msg = getIntent().getExtras().getString("msg");
                            i.putExtra("msg", msg);
                        }
                    }
                    startActivity(i);
                    thread.interrupt();
                    finish();
                    //thread.interrupt();
                }
            }
        } else if (tipoExecThread.equals("carregaNavigationActivity")) {
            if (savedPreferences.hasKey("areas")) {

                Intent i = new Intent(LoadActivity.this, NavigationActivity.class);
                if (extras != null) {
                    if (extras.containsKey("msg")) {
                        String msg = getIntent().getExtras().getString("msg");
                        i.putExtra("msg", msg);
                    }
                }
                startActivity(i);
                thread.interrupt();
                finish();
            }
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            /*mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });*/

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

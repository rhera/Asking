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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.feapps.asking.DAO.UsuarioDAO;
import br.com.feapps.asking.DAO.WebServiceAskingDB;
import br.com.feapps.asking.R;
import br.com.feapps.asking.model.Usuario;
import br.com.feapps.asking.util.CONST;
import br.com.feapps.asking.util.Cript;
import br.com.feapps.asking.util.MessageBox;
import br.com.feapps.asking.util.SavedPreferences;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener ,Runnable {

    private View formSalvar;
    private View formProgress;

    private AutoCompleteTextView edtMudarNome;
    private EditText edtMudarTelefone;
    private EditText edtMudarEmail;
    private EditText edtMudarNascimento;
    private EditText edtSenha1;
    private EditText edtSenha2;
    private EditText edtLogin;
    private EditText edtSenhaAtual;

    Usuario user;

    //SharedPreferences shareprefs;
    SavedPreferences savedPreferences;

    //Variáveis para ajudar no acesso webservice
    private Handler handler = new Handler();
    private ProgressDialog dialog;
    Cript cript = new Cript("");
    //String[] listUser = new String[9];
    Thread thread;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Pega formularios
        formSalvar = findViewById(R.id.formSalvar);
        formProgress = findViewById(R.id.cad_progress);

        //Pega preferências salvas
        //shareprefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        savedPreferences = new SavedPreferences(this);
        user = savedPreferences.getUser();

        //Pegando campos
        edtMudarNome = (AutoCompleteTextView) findViewById(R.id.edtMudarNome);
        edtMudarTelefone = (EditText) findViewById(R.id.edtMudarTelefone);
        edtMudarEmail = (EditText) findViewById(R.id.edtMudarEmail);
        edtMudarNascimento = (EditText) findViewById(R.id.edtMudarNascimento);
        edtSenha1 = (EditText) findViewById(R.id.edtSenha1);
        edtSenha2 = (EditText) findViewById(R.id.edtSenha2);
        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtSenhaAtual = (EditText) findViewById(R.id.edtSenhaAtual);
        //Setando valores
        edtMudarNome.setText(user.getNome());
        edtMudarTelefone.setText(user.getTelefone());
        edtMudarEmail.setText(user.getEmail());
        edtMudarNascimento.setText(user.getNascimento());

        FloatingActionButton fabSalvar = (FloatingActionButton) findViewById(R.id.fabSalvar);
        fabSalvar.setOnClickListener(this);

        /*FloatingActionButton fabCancelar = (FloatingActionButton) findViewById(R.id.fabCancelar);
        fabCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent ( PerfilActivity.this, NavigationActivity.class);
                startActivity(i);
                finish();
            }
        });*/

        //Tratamento de mensagens de erro
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("msg")) {
                /*Toast toast= Toast.makeText(getApplicationContext(),
                        extras.getString("msg"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();*/
                MessageBox.show(this,"Mensagem", extras.getString("msg"),null);
                edtMudarNome.setText(extras.getString(CONST.USERTABNOME));
                edtMudarTelefone.setText(extras.getString(CONST.USERTABTELEFONE));
                edtMudarEmail.setText(extras.getString(CONST.USERTABEMAIL));
                edtMudarNascimento.setText(extras.getString(CONST.USERTABNASCIMENTO));
                edtLogin.setText(extras.getString(CONST.USERTABLOGIN));
                edtSenha1.requestFocus();
                extras.clear();
            }
            if (extras.containsKey("erroLogin")) {
                Toast toast= Toast.makeText(getApplicationContext(),
                        extras.getString("erroLogin"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                edtMudarNome.setText(extras.getString(CONST.USERTABNOME));
                edtMudarTelefone.setText(extras.getString(CONST.USERTABTELEFONE));
                edtMudarEmail.setText(extras.getString(CONST.USERTABEMAIL));
                edtMudarNascimento.setText(extras.getString(CONST.USERTABNASCIMENTO));
                edtLogin.setText(extras.getString(""));
                edtLogin.requestFocus();
                extras.clear();
            } else if (extras.containsKey("erroEmail")) {
                Toast toast= Toast.makeText(getApplicationContext(),
                        extras.getString("erroEmail"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                edtMudarNome.setText(extras.getString(CONST.USERTABNOME));
                edtMudarTelefone.setText(extras.getString(CONST.USERTABTELEFONE));
                edtMudarEmail.setText(extras.getString(""));
                edtMudarNascimento.setText(extras.getString(CONST.USERTABNASCIMENTO));
                edtLogin.setText(extras.getString(CONST.USERTABLOGIN));
                edtMudarEmail.requestFocus();
                extras.clear();
            }

        }
    }

    public void voltar() {
        Intent i = new Intent ( PerfilActivity.this, NavigationActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onClick(View v) {
        /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

        //Usuario user = new Usuario();
        cript = new Cript("");

        // List<String> listUser = new ArrayList<String>();
        //String[] listUser = new String[8];

        boolean cancel = false;
        View focusView = null;

        /*listUser[0] = String.valueOf(user.getId());
        listUser[1] = edtMudarNome.getText().toString();
        listUser[2] = edtMudarTelefone.getText().toString();
        listUser[3] = edtMudarEmail.getText().toString();
        listUser[4] = edtMudarNascimento.getText().toString();
        listUser[5] = edtLogin.getText().toString();*/
        user.setNome(edtMudarNome.getText().toString());
        user.setTelefone(edtMudarTelefone.getText().toString());
        user.setEmail(edtMudarEmail.getText().toString());
        user.setNascimento(edtMudarNascimento.getText().toString());
        String login = edtLogin.getText().toString();
        String senha1 = edtSenha1.getText().toString();
        String senha1cri = "";
        String senha2 = edtSenha2.getText().toString();
        String senha2cri = "";
        String senha = edtSenhaAtual.getText().toString();

        if (!TextUtils.isEmpty(senha)) {
            cript = new Cript(senha);
            senha = cript.getCript();
        }

        if (!TextUtils.isEmpty(senha1) && !TextUtils.isEmpty(senha2)) {
            cript = new Cript(senha1);
            senha1cri = cript.getCript();
            cript = new Cript(senha2);
            senha2cri = cript.getCript();
        }

        //Verificando campos vazios
        if (TextUtils.isEmpty(user.getNome())) {
            edtMudarNome.setError(getString(R.string.error_field_required));
            focusView = edtMudarNome;
            cancel = true;
        } else if (TextUtils.isEmpty(user.getTelefone())) {
            edtMudarTelefone.setError(getString(R.string.error_field_required));
            focusView = edtMudarTelefone;
            cancel = true;
        } else if (TextUtils.isEmpty(user.getEmail())) {
            edtMudarEmail.setError(getString(R.string.error_field_required));
            focusView = edtMudarEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(user.getNascimento())) {
            edtMudarNascimento.setError(getString(R.string.error_field_required));
            focusView = edtMudarNascimento;
            cancel = true;
        } else if (TextUtils.isEmpty(login)) {
            edtLogin.setError(getString(R.string.error_field_required));
            focusView = edtLogin;
            cancel = true;
        //Verifica senhas
        } else if (!(TextUtils.isEmpty(senha1)&&TextUtils.isEmpty(senha1))) {
            if (senha1.length() < 6 || senha2.length() < 6) {
                edtSenha1.setError(getString(R.string.error_invalid_password));
                focusView = edtSenha1;
                cancel = true;
            } else if (!senha1.equals(senha2)) {
                edtSenha1.setError("As novas senhas não coincidem.");
                focusView = edtSenha1;
                cancel = true;
            }
        } else if (TextUtils.isEmpty(senha)) {
            edtSenhaAtual.setError(getString(R.string.error_field_required));
            focusView = edtSenhaAtual;
            cancel = true;
        //Verifica se senha confere
        } else if (!senha.equals(user.getSenha())) {
            edtSenhaAtual.setError("A senha atual não confere.");
            focusView = edtSenhaAtual;
            cancel = true;
        //Verifica se login confere
        } else if (!login.equals(user.getLogin())) {
            edtLogin.setError("O login não confere.");
            focusView = edtLogin;
            cancel = true;
        }

        if (senha1cri != "" && senha2cri != "") {
            cript = new Cript(senha1);
            user.setSenha(cript.getCript());
        }

        //TODO lidar com usuário colaborador
        //listUser[7] = String.valueOf(user.getTipo());
        //TODO adicionar foto de usuário
        //user.getFoto();

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
            context = this;
            thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        try {
            //WebServiceAskingDB ws = new WebServiceAskingDB();
            UsuarioDAO uDAO = new UsuarioDAO();
            //final String[] tempListUser = listUser;
            final Usuario userRetorno = uDAO.saveUser(user.toString(), cript.getKey());
            //Log.d("AQUI!", String.valueOf(ws.isOkay()));

            if (!uDAO.isOkay()) {
                this.thread.interrupt();
                Intent it = new Intent(PerfilActivity.this, PerfilActivity.class);
                it.putExtra("msg", uDAO.getMsg());
                startActivity(it);
                finish();
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (userRetorno != null) {
                            atualizaDadosUsuario();
                            Intent it = new Intent(PerfilActivity.this, NavigationActivity.class);
                            it.putExtra("msg", "Salvo com sucesso!");
                            startActivity(it);
                            finish();
                        } else {
                            //listUser = tempListUser;
                            Intent it = new Intent(PerfilActivity.this, PerfilActivity.class);
                            it.putExtra("msg", "ERRO: falha ao cadastrar usuário. Servidor fora de serviço ou hora errada.");
                            startActivity(it);
                            finish();
                        }
                    }
                });
            }
        } catch (Exception ex) {
            Log.e("PerfilActivity", "Erro", ex);
        } finally {
            //dialog.dismiss();
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

            formProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            formProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    formProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            formProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            formSalvar.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void atualizaDadosUsuario() {
        /*SharedPreferences.Editor editor = shareprefs.edit();
        editor.putInt(Const.USERTABID, Integer.parseInt((String)listUser[0]));
        editor.putString(Const.USERTABNOME, listUser[1].toString());
        editor.putString(Const.USERTABTELEFONE, listUser[2].toString());
        editor.putString(Const.USERTABEMAIL, listUser[3].toString());
        editor.putString(Const.USERTABNASCIMENTO, listUser[4].toString());
        editor.putString(Const.USERTABLOGIN, listUser[5].toString());
        editor.putString(Const.USERTABSENHA, listUser[6].toString());
        editor.putInt(Const.USERTABTIPO, Integer.parseInt(listUser[7].toString()));
        //TODO foto
        //editor.putString(Const.USERTABFOTO, listUser[8].toString());
        editor.commit();*/
        Usuario u = new Usuario(
                user.getId(),
                user.getNome(),
                user.getTelefone(),
                user.getNascimento(),
                user.getEmail(),
                user.getLogin(),
                user.getSenha(),
                user.getTipo(),
                user.getFoto());
        savedPreferences.saveUser(u);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent ( PerfilActivity.this, NavigationActivity.class);
        startActivity(i);
        finish();
    }
}

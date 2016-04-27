package br.com.feapps.asking.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
import java.util.List;

import br.com.feapps.asking.DAO.UsuarioDAO;
import br.com.feapps.asking.DAO.WebServiceAskingDB;
import br.com.feapps.asking.R;
import br.com.feapps.asking.model.Usuario;
import br.com.feapps.asking.util.CONST;
import br.com.feapps.asking.util.Cript;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private View formCadastro;
    private View formProgress;

    private AutoCompleteTextView edtNome;
    private EditText edtTelefone;
    private EditText edtNascimento;
    private EditText edtEmail;
    private EditText edtLogin;
    private EditText edtSenha1;
    private EditText edtSenha2;

    private TextInputLayout tilNome;

    //Variáveis para ajudar no acesso webservice
    private Handler handler = new Handler();
    private ProgressDialog dialog;
    Cript cript = new Cript("");
    Usuario user = new Usuario();
    //String[] listUser = new String[8];

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        formCadastro = findViewById(R.id.formCadastro);
        formProgress = findViewById(R.id.cad_progress);

        //seta variáveis de dados da activity
        edtNome = (AutoCompleteTextView) findViewById(R.id.edtNome);
        edtTelefone = (EditText) findViewById(R.id.edtTelefone);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtNascimento = (EditText) findViewById(R.id.edtNascimento);
        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtSenha1 = (EditText) findViewById(R.id.edtSenha1);
        edtSenha2 = (EditText) findViewById(R.id.edtSenha2);

        //Tratamento de mensagens de erro
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("msg")) {
                Toast toast= Toast.makeText(getApplicationContext(),
                        extras.getString("msg"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                edtNome.setText(extras.getString(CONST.USERTABNOME));
                edtTelefone.setText(extras.getString(CONST.USERTABTELEFONE));
                edtEmail.setText(extras.getString(CONST.USERTABEMAIL));
                edtNascimento.setText(extras.getString(CONST.USERTABNASCIMENTO));
                edtLogin.setText(extras.getString(CONST.USERTABLOGIN));
                edtSenha1.requestFocus();
                extras.clear();
            }
            if (extras.containsKey("erroLogin")) {
                Toast toast= Toast.makeText(getApplicationContext(),
                        extras.getString("erroLogin"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                edtNome.setText(extras.getString(CONST.USERTABNOME));
                edtTelefone.setText(extras.getString(CONST.USERTABTELEFONE));
                edtEmail.setText(extras.getString(CONST.USERTABEMAIL));
                edtNascimento.setText(extras.getString(CONST.USERTABNASCIMENTO));
                edtLogin.setText(extras.getString(""));
                edtLogin.requestFocus();
                extras.clear();
            } else if (extras.containsKey("erroEmail")) {
                Toast toast= Toast.makeText(getApplicationContext(),
                        extras.getString("erroEmail"), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                edtNome.setText(extras.getString(CONST.USERTABNOME));
                edtTelefone.setText(extras.getString(CONST.USERTABTELEFONE));
                edtEmail.setText(extras.getString(""));
                edtNascimento.setText(extras.getString(CONST.USERTABNASCIMENTO));
                edtLogin.setText(extras.getString(CONST.USERTABLOGIN));
                edtEmail.requestFocus();
                extras.clear();
            }

        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fabc = (FloatingActionButton) findViewById(R.id.fabCancelar);
        fabc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
                voltar();
            }
        });*/

        FloatingActionButton faba = (FloatingActionButton) findViewById(R.id.fabAdicionar);
        faba.setOnClickListener(this);
    }

    /*public void voltar() {
        Intent i = new Intent ( CadastroActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }*/

    @Override
    public void run() {
        try {
            //WebServiceAskingDB ws = new WebServiceAskingDB();
            UsuarioDAO uDAO = new UsuarioDAO();
            //Log.d("AAAAAAAAAAA: ",cript.getKey());
            final int result = uDAO.addUser(user.toString(), cript.getKey());

            if (!uDAO.isOkay()) {
                this.thread.interrupt();
                //Log.d("AAAAAAAAAAA: ", ws.getMsg());
                Intent it = new Intent(CadastroActivity.this, CadastroActivity.class);
                it.putExtra("msg", uDAO.getMsg());
                SalvaExtras(it);
                startActivity(it);
                finish();
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Se usuário não existe, redireciona para activity de cadastro.
                        if (result == CONST.SUCESS) {
                            Intent it = new Intent(CadastroActivity.this, LoginActivity.class);
                            it.putExtra("msg", "Cadastrado com sucesso!");
                            startActivity(it);
                        } else if (result == CONST.KEYERROR){
                            Intent it = new Intent(CadastroActivity.this, CadastroActivity.class);
                            it.putExtra("msg", "ERRO: falha ao cadastrar usuário. Atualize seu relógio e tente novamente.");
                            SalvaExtras(it);
                            startActivity(it);
                            finish();
                        } else if (result == CONST.USERLOGINEXIST) {
                            Intent it = new Intent(CadastroActivity.this, LoginActivity.class);
                            it.putExtra("msg", "ERRO: Usuário já existente, tente outro login.");
                            SalvaExtras(it);
                            startActivity(it);
                        } else if (result == CONST.USEREMAILEXIST) {
                            Intent it = new Intent(CadastroActivity.this, LoginActivity.class);
                            it.putExtra("msg", "ERRO: Usuário já existente, tente outro email.");
                            SalvaExtras(it);
                            startActivity(it);
                        }
                    }
                });
            }
        } catch (Exception ex) {
            Log.e("LoginActivity", "Erro", ex);
        } finally {
            //dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {

        user = new Usuario();
        cript = new Cript("");

        // List<String> listUser = new ArrayList<String>();
        //String[] listUser = new String[8];

        boolean cancel = false;
        View focusView = null;

        /*listUser[0] = edtNome.getText().toString();
        listUser[1] = edtTelefone.getText().toString();
        listUser[2] = edtNascimento.getText().toString();
        listUser[3] = edtEmail.getText().toString();
        listUser[4] = edtLogin.getText().toString();*/
        user.setNome(edtNome.getText().toString());
        user.setTelefone(edtTelefone.getText().toString());
        user.setNascimento(edtNascimento.getText().toString());
        user.setEmail(edtEmail.getText().toString());
        user.setLogin(edtLogin.getText().toString());
        String senha1 = edtSenha1.getText().toString();
        String senha2 = edtSenha2.getText().toString();

        //Verificando campos vazios
        if (TextUtils.isEmpty(user.getNome())) {
            edtNome.setError(getString(R.string.error_field_required));
            focusView = edtNome;
            cancel = true;
        } else if (TextUtils.isEmpty(user.getTelefone())) {
            edtTelefone.setError(getString(R.string.error_field_required));
            focusView = edtTelefone;
            cancel = true;
        } else if (TextUtils.isEmpty(user.getNascimento())) {
            edtEmail.setError(getString(R.string.error_field_required));
            focusView = edtEmail;
            cancel = true;
        } else if (TextUtils.isEmpty(user.getEmail())) {
            edtNascimento.setError(getString(R.string.error_field_required));
            focusView = edtNascimento;
            cancel = true;
        } else if (TextUtils.isEmpty(user.getLogin())) {
            edtLogin.setError(getString(R.string.error_field_required));
            focusView = edtLogin;
            cancel = true;
        } else if (senha1.length() < 5 || senha2.length() < 5) { //Verifica senhas
            //this.thread.interrupt();
            /*Intent it = new Intent(CadastroActivity.this, CadastroActivity.class);
            it.putExtra("msg", "ERRO: Senha muito curta");
            startActivity(it);*/

            edtSenha1.setError(getString(R.string.error_invalid_password));
            focusView = edtSenha1;
            cancel = true;

            //finish();
        } else if (!senha1.equals(senha2)) {
            //this.thread.interrupt();
            /*Intent it = new Intent(CadastroActivity.this, CadastroActivity.class);
            it.putExtra("msg", "ERRO: Senhas não coincidem.");
            startActivity(it);*/

            edtSenha1.setError("As senhas não coincidem.");
            focusView = edtSenha1;
            cancel = true;

            //finish();
        }
        cript = new Cript(senha1);
        user.setSenha(cript.getCript());

        user.setTipo(1);
        //TODO adicionar foto de usuário
        //listUser[7] = "foto";

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            showProgress(true);
            thread = new Thread(this);
            thread.start();
        }
        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Cadastrado com sucesso!");

        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
            }
        });*/

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

            formCadastro.setVisibility(show ? View.GONE : View.VISIBLE);
            formCadastro.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    formCadastro.setVisibility(show ? View.GONE : View.VISIBLE);
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
            formCadastro.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void SalvaExtras (Intent it) {
        it.putExtra(CONST.USERTABNOME, user.getNome());
        it.putExtra(CONST.USERTABTELEFONE, user.getTelefone());
        it.putExtra(CONST.USERTABEMAIL, user.getNascimento());
        it.putExtra(CONST.USERTABNASCIMENTO, user.getEmail());
        it.putExtra(CONST.USERTABLOGIN, user.getLogin());
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent ( CadastroActivity.this, LoadActivity.class);
        startActivity(i);
        finish();
    }
}

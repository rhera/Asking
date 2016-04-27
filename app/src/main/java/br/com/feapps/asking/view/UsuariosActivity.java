package br.com.feapps.asking.view;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.List;

import br.com.feapps.asking.R;
import br.com.feapps.asking.model.Usuario;
import br.com.feapps.asking.util.SavedPreferences;
import br.com.feapps.asking.util.UsuarioArrayAdapter;

public class UsuariosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    AutoCompleteTextView edtPesquisa;
    ImageButton imgButtonAtualiza;
    ListView listViewPessoas;

    private ArrayAdapter<Usuario> adpUsuarios;
    List<Usuario> users;

    SavedPreferences savedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

        //Pegando preferencias salvas
        savedPreferences = new SavedPreferences(this);

        //Inicializando variáveis
        edtPesquisa = (AutoCompleteTextView) findViewById(R.id.edtPesquisa);
        imgButtonAtualiza = (ImageButton) findViewById(R.id.imgButtonAtualiza);
        listViewPessoas = (ListView) findViewById(R.id.listViewPessoas);
        adpUsuarios = new UsuarioArrayAdapter(this, R.layout.activity_lista_usuarios);

        //Atualizar adp Usuario
        users = savedPreferences.getListUsers();
        for (Usuario u : users) {
            adpUsuarios.add(u);
        }
        listViewPessoas.setAdapter(adpUsuarios);

        //Filtrar informação atravez da caixa de pesquisa
        FiltraDados filtraDados = new FiltraDados(adpUsuarios);
        edtPesquisa.addTextChangedListener(filtraDados);

        //Botões
        listViewPessoas.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent ( UsuariosActivity.this, NavigationActivity.class);
        startActivity(i);
        finish();
    }

    private class FiltraDados implements TextWatcher {

        private ArrayAdapter<Usuario> arrayAdapter;

        private FiltraDados (ArrayAdapter<Usuario> arrayAdapter) {
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

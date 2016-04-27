package br.com.feapps.asking.view;

import android.content.Intent;
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
import br.com.feapps.asking.model.Ranking;
import br.com.feapps.asking.util.RankingArrayAdapter;
import br.com.feapps.asking.util.SavedPreferences;

public class RankingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    AutoCompleteTextView edtPesquisa;
    ImageButton imgButtonAtualiza;
    ListView listViewRankingPessoas;

    SavedPreferences savedPreferences;

    private ArrayAdapter<Ranking> adpRanking;
    private List<Ranking> rankingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        //Inicializando variáveis
        edtPesquisa = (AutoCompleteTextView) findViewById(R.id.edtPesquisa);
        imgButtonAtualiza = (ImageButton) findViewById(R.id.imgButtonAtualiza);
        listViewRankingPessoas = (ListView) findViewById(R.id.listViewRankingPessoas);
        savedPreferences = new SavedPreferences(this);
        adpRanking = new RankingArrayAdapter(this, R.layout.activity_lista_ranking);

        //Atualizando adpRanking
        rankingList = savedPreferences.getRankings();
        for (Ranking r : rankingList) {
            adpRanking.add(r);
        }
        listViewRankingPessoas.setAdapter(adpRanking);

        //Filtrar informação atravez da caixa de pesquisa
        FiltraDados filtraDados = new FiltraDados(adpRanking);
        edtPesquisa.addTextChangedListener(filtraDados);

        //Botões
        listViewRankingPessoas.setOnItemClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent ( RankingActivity.this, NavigationActivity.class);
        startActivity(i);
        finish();
    }

    private class FiltraDados implements TextWatcher {

        private ArrayAdapter<Ranking> arrayAdapter;

        private FiltraDados (ArrayAdapter<Ranking> arrayAdapter) {
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

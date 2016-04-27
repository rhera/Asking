package br.com.feapps.asking.view;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapFault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.feapps.asking.DAO.RankingDAO;
import br.com.feapps.asking.R;
import br.com.feapps.asking.model.ConjuntoQuestoes;
import br.com.feapps.asking.model.Questao;
import br.com.feapps.asking.model.Ranking;
import br.com.feapps.asking.model.Resposta;
import br.com.feapps.asking.model.Sala;
import br.com.feapps.asking.model.Usuario;
import br.com.feapps.asking.util.MessageBox;
import br.com.feapps.asking.util.SavedPreferences;
import br.com.feapps.asking.util.ViewHelper;

public class JogoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, Runnable {

    private TextView txtQuestionNum;
    //private TextView txtTempo;
    private ImageButton btnAtualiza;
    private TextView textViewQuestao;
    private ListView listViewRespostas;
    private Button btnSair;

    private ConjuntoQuestoes conjuntoQuestoes;
    private List<Questao> questoes;
    private Questao questao;
    private ArrayAdapter<String> adpRespostas;
    private List<Resposta> respostas;

    private int indexQuestao;
    private int indexResposta;

    SavedPreferences savedPreferences;

    //private CountDownTimer countDownTimer;
    private Chronometer chronometer;
    private long Time = 0;

    Usuario usuario;
    Sala sala;
    RankingDAO rDAO;
    Ranking ranking;
    List<Ranking> rankings;
    int numeroDeQuestoes;
    int acertos;
    float[] mediaTempo;

    String tipoExecThread;
    Thread thread;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        //Pegando preferências salvas e apaga rankings temporarios
        savedPreferences = new SavedPreferences(this);

        //Inicializando variaveis
        rDAO = new RankingDAO();
        txtQuestionNum = (TextView) findViewById(R.id.txtQuestionNum);
        //txtTempo = (TextView) findViewById(R.id.txtTempo);
        btnAtualiza = (ImageButton) findViewById(R.id.bntAtualiza);
        textViewQuestao = (TextView) findViewById(R.id.textViewQuestao);
        listViewRespostas = (ListView) findViewById(R.id.listViewRespostas);
        adpRespostas = ViewHelper.createArrayAdapter(this, listViewRespostas);
        btnSair = (Button) findViewById(R.id.bntSairSala);
        btnSair.setEnabled(false);
        chronometer = (Chronometer) findViewById(R.id.cronometro);
        indexQuestao = 0;
        indexResposta = 0;
        acertos = 0;
        mediaTempo = new float[5];
        usuario = savedPreferences.getUser();
        sala = savedPreferences.getSala();
        numeroDeQuestoes = sala.getConjuntoQuestoes().getQuestoes().size();
        context = this;

        //TODO definir o que será carregado, se questões ou fim de jogo
        if (!savedPreferences.hasKey("rankingstemp")) {

            //Setando rankiamento
            Usuario user = savedPreferences.getUser();
            ranking = new Ranking(user);

            //Pegando conjunto de questões
            conjuntoQuestoes = sala.getConjuntoQuestoes();

            //Embraralhando respostas para uso
            Collections.shuffle(conjuntoQuestoes.getRespostas());

            //Definindo ordem das respostas a serem apresentadas
            questoes = conjuntoQuestoes.getQuestoes();
            //for (Questao q : questoes) {
                //Log.d("QUestao",q.getQuestao());
            //}
            int numeroQuestoes = questoes.size();
            //for (int i = 0; i < numeroQuestoes; i++) {
                //Log.d("jogoact", questoes.get(i).getQuestao());
            //}
            respostas = new ArrayList<>();
            List<Resposta> tempRespostas;
            int respostaNumero = 0;
            int j;
            for (int i = 0; i < numeroQuestoes; i++) {
                tempRespostas = new ArrayList<>();
                tempRespostas.add(questoes.get(i).getResposta());
                for (j = respostaNumero; j < respostaNumero + 3; j++) {
                    tempRespostas.add(conjuntoQuestoes.getRespostas().get(j));
                    //Log.d("jogoactQuest"+j, conjuntoQuestoes.getRespostas().get(j).getResposta());
                }
                respostaNumero = j;
                Collections.shuffle(tempRespostas);
                for (Resposta r : tempRespostas) {
                    respostas.add(r);
                }
            }

            /*for (Questao questao : questoes)
                Log.d("jogo", "Questao: " + questao.getQuestao());

            for (Resposta resposta : respostas)
                Log.d("jogo", "Resposta: " + resposta.getResposta());*/

            //Carregando primeiro conjunto de pergunta e respostas
            proximaQuestao();
        } else {
            adpRespostas.clear();
            rankings = savedPreferences.getRankingsTemp();
            int i = 1;
            for (Ranking r : rankings) {
                //Log.e("jogo", "Usuário: " + r.getUsuario().getNome());
                adpRespostas.add(i + "º - " + r.getUsuario().getNome() + " - Média: " + r.getMedia5acertos() + " - Tempo: " + r.getMediaTempo());
                i++;
            }

            txtQuestionNum.setText("");
            //txtTempo.setText("");
            chronometer.setText("Fim de jogo");
            textViewQuestao.setText("Resultado:");
            btnAtualiza.setVisibility(View.VISIBLE);
            context = this;
            btnAtualiza.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tipoExecThread = "AtualizaListRanking";
                    thread = new Thread((Runnable) context);
                    thread.start();
                }
            });
            //adpRespostas.clear();

            btnSair.setVisibility(View.VISIBLE);
            if (rankings.size() == sala.getMaxUserSala()) {
                btnSair.setEnabled(true);
            } else {
                btnSair.setEnabled(false);
            }
            //btnSair.setEnabled(false);
            btnSair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tipoExecThread = "Sair";
                    thread = new Thread((Runnable) context);
                    thread.start();
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //countDownTimer.onFinish();
        //countDownTimer.cancel();
        //int index = questaoNumero - 1;
        chronometer.stop();
        String respostaDada = adpRespostas.getItem(position);
        //Log.d("jogo", chronometer.getText().toString() + ": " + respostaDada);
        long timeElapsed = SystemClock.elapsedRealtime() - chronometer.getBase();
        long hours = (long) (timeElapsed / 3600000);
        long minutes = (long) (timeElapsed - hours * 3600000) / 60000;
        long seconds = (long) (timeElapsed - hours * 3600000 - minutes * 60000) / 1000;
        Log.d("jogo", respostaDada + " = " + questoes.get(indexQuestao-1).getQuestao());
        if (respostaDada.equals(questoes.get(indexQuestao-1).getResposta().getResposta())) {
            acertos++;
            Log.d("jogo", "Entrou: " + acertos);
            mediaTempo[indexQuestao-1] = seconds;
        }
        if (indexQuestao < numeroDeQuestoes)
            proximaQuestao();
        else {
            ranking.setUsuario(savedPreferences.getUser());
            ranking.setMedia5acertos(acertos);
            float mediaT = 0;
            for (int i = 0; i < mediaTempo.length; i++) {
                mediaT += mediaTempo[i];
            }
            mediaT /= mediaTempo.length;
            ranking.setMediaTempo(mediaT);
            savedPreferences.setRanking(ranking, usuario);

            txtQuestionNum.setText("");
            chronometer.setText("Fim de jogo");

            //TODO definir modo de aumentar número de questões

            textViewQuestao.setText("Resultado:");
            btnAtualiza.setVisibility(View.VISIBLE);
            context = this;
            btnAtualiza.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tipoExecThread = "AtualizaListRanking";
                    thread = new Thread((Runnable) context);
                    thread.start();
                }
            });
            adpRespostas.clear();
            //for (int i = 0; i < rankings.size(); i++)
            adpRespostas.add(1 + "º - " + usuario.getNome() + " - Média: " + ranking.getMedia5acertos() + " - Tempo: " + ranking.getMediaTempo());

            btnSair.setVisibility(View.VISIBLE);
            btnSair.setEnabled(false);
            btnSair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tipoExecThread = "Sair";
                    thread = new Thread((Runnable) context);
                    thread.start();
                }
            });

            tipoExecThread = "SalvaRanking";
            thread = new Thread(this);
            thread.start();
        }
    }

    public void proximaQuestao() {
        //Log.d("sala", "Questão de número: " + (indexQuestao + 1));

        txtQuestionNum.setText((indexQuestao + 1) + "/" + numeroDeQuestoes);

        /*countDownTimer = new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtTempo.setText(String.valueOf(millisUntilFinished / 1000));
            }

            public void onFinish() {
                //txtTempo.setText("fim");
                countDownTimer.cancel();
                if (questaoNumero < 5) {
                    mediaTempo[questaoNumero] = 0;
                    proximaQuestao();
                }
            }
        };
        countDownTimer.start();*/
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        textViewQuestao.setText(conjuntoQuestoes.getQuestoes().get(indexQuestao).getQuestao());
        adpRespostas.clear();
        for (int i = indexResposta; i < indexResposta+4; i++)
            adpRespostas.add(respostas.get(i).getResposta());
        listViewRespostas.setAdapter(adpRespostas);

        listViewRespostas.setOnItemClickListener(this);
        indexQuestao++;
        indexResposta+=4;
    }

    @Override
    public void run() {
        if (tipoExecThread.equals("SalvaRanking")) {

            /*ranking.setUsuario(savedPreferences.getUser());
            ranking.setMedia5acertos(acertos);
            float mediaT = 0;
            for (int i = 0; i < mediaTempo.length; i++) {
                mediaT += mediaTempo[i];
            }
            mediaT /= mediaTempo.length;
            ranking.setMediaTempo(mediaT);
            savedPreferences.setRanking(ranking);*/

            ranking = savedPreferences.getRanking();

            Log.e("jogo", ranking.toString(usuario));
            try {
                rankings = rDAO.salvaRankingTemp(ranking.toString(usuario), sala.getId());
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            }

            //Redesenhando sala para mostrar resultados.

            /*txtQuestionNum.setText("");
            chronometer.setText("");

            //TODO definir modo de aumentar número de questões

            textViewQuestao.setText("Resultado:");
            btnAtualiza.setVisibility(View.VISIBLE);
            context = this;
            btnAtualiza.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tipoExecThread = "AtualizaListRanking";
                    thread = new Thread((Runnable) context);
                    thread.start();
                }
            });
            adpRespostas.clear();
            for (int i = 0; i < rankings.size(); i++)
                adpRespostas.add((i+1) + "º - " + rankings.get(i).getUsuario().getNome());

            if (rankings.size() == sala.getMaxUserSala()){
                btnSair.setVisibility(View.VISIBLE);
                //btnSair.setEnabled(false);
                btnSair.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tipoExecThread = "Sair";
                        thread = new Thread((Runnable) context);
                        thread.start();
                    }
                });
            }

            MessageBox.show(context,"Sistema:", "Clique em atualizar até que todos os participantes tenham finalizado o game.", null);*/

        } else if (tipoExecThread.equals("AtualizaListRanking")) {
            rankings = new ArrayList<>();
            //sala = savedPreferences.getSala();
            /*String ids = "";
            int i = 0;
            List<Usuario> listUsers = sala.getUsuarios();
            for (Usuario u : listUsers) {
                ids += u.getId();
                if (i < listUsers.size())
                    ids += "##";
                i++;
            }*/
            try {
                rankings = rDAO.retornaRankingsTemps(sala.getId());
            } catch (SoapFault soapFault) {
                soapFault.printStackTrace();
            }
            /*for (Ranking ranking : rankings)
                Log.e("jogo", ranking.getUsuario().getNome());*/
            savedPreferences.setRankingsTemp(rankings);

            Intent it = new Intent(JogoActivity.this, JogoActivity.class);
            startActivity(it);
            finish();

        } else if (tipoExecThread.equals("Sair")) {
            //Usuario user = savedPreferences.getUser();
            //sala = savedPreferences.getSala();
            String ids = "";
            int i = 0;
            /*List<Usuario> listUsers = sala.getUsuarios();
            for (Usuario u : listUsers) {
                ids += u.getId();
                if (i < listUsers.size())
                    ids += "##";
                i++;
            }*/
            //if (user.getId() == listUsers.get(0).getId()) {
                try {
                    rDAO.salvaRankings(sala.getId(), usuario.getId());
                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                }
            //}
            savedPreferences.remove("rankingstemp");
            Intent it = new Intent(JogoActivity.this, LoadActivity.class);
            startActivity(it);
            finish();
        }
        thread.interrupt();
    }
}

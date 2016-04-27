package br.com.feapps.asking.DAO;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import br.com.feapps.asking.model.Area;
import br.com.feapps.asking.model.ConjuntoQuestoes;
import br.com.feapps.asking.model.Questao;
import br.com.feapps.asking.model.Resposta;
import br.com.feapps.asking.model.SubArea;

/**
 * Created by F.Einstein on 022, 22/1/2016.
 */
public class ConjuntoDAO extends WebServiceAskingDB {
    private ConjuntoQuestoes conjuntoQuestoes;

    public ConjuntoDAO() {
        super();
        conjuntoQuestoes = new ConjuntoQuestoes();
    }

    public ConjuntoQuestoes novoConjuntoQuestoes(int idSala) throws SoapFault {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "novoConjuntoQuestoes");
        soap.addProperty("idSala", idSala);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("novoConjuntoQuestoes", envelope);
        } catch (SocketTimeoutException ste) {
            setMsg("ERRO: não foi possível conectar ao servidor. Tente novamente mais tarde.");
            setOkay(false);
        } catch (ConnectException ce) {
            setMsg("ERRO: não foi possível conectar ao servidor. Tente novamente mais tarde.");
            setOkay(false);
        } catch (HttpResponseException hre) {
            setMsg("ERRO: não foi possível conectar ao servidor. Tente novamente mais tarde.");
            setOkay(false);
        } catch (RuntimeException re) {
            setMsg(re.getMessage());
            setOkay(false);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SoapObject spret = (SoapObject) envelope.getResponse();

        return conjuntoQuestoes;
    }

    public void setConjuntoQuestoes(ConjuntoQuestoes conjuntoQuestoes) {
        this.conjuntoQuestoes = conjuntoQuestoes;
    }

    public ConjuntoQuestoes getConjuntoQuestoes (int idSala, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "getConjuntoQuestoes");
        soap.addProperty("idSala", idSala);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("getConjuntoQuestoes", envelope);
        } catch (SocketTimeoutException ste) {
            setMsg("ERRO: não foi possível conectar ao servidor. Tente novamente mais tarde.");
            setOkay(false);
        } catch (ConnectException ce) {
            setMsg("ERRO: não foi possível conectar ao servidor. Tente novamente mais tarde.");
            setOkay(false);
        } catch (HttpResponseException hre) {
            setMsg("ERRO: não foi possível conectar ao servidor. Tente novamente mais tarde.");
            setOkay(false);
        } catch (RuntimeException re) {
            setMsg(re.getMessage());
            setOkay(false);
        }

        Vector resposta = (Vector) envelope.getResponse();
        conjuntoQuestoes = new ConjuntoQuestoes();
        if (resposta != null) {
            conjuntoQuestoes.setId(Integer.parseInt(resposta.get(0).toString()));
            boolean adcQuestoes = true;
            for (int i = 1; i < resposta.size(); i++) {
                String str = resposta.get(i).toString();
                if (adcQuestoes) {
                    if (str.equals("$$")) {
                        adcQuestoes = false;
                    } else {
                        Questao q = new Questao();
                        q.toQuestao(str);
                        conjuntoQuestoes.getQuestoes().add(q);
                    }
                } else {
                    Resposta r = new Resposta();
                    r.toResposta(str);
                    conjuntoQuestoes.getRespostas().add(r);
                }
            }

        }

        return conjuntoQuestoes;
    }

    public void deletarConjunto (int idConjQuest, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "deletarConjunto");
        soap.addProperty("idConjQuest", idConjQuest);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("deletarConjunto", envelope);
        } catch (SocketTimeoutException ste) {
            setMsg("ERRO: não foi possível conectar ao servidor. Tente novamente mais tarde.");
            setOkay(false);
        } catch (ConnectException ce) {
            setMsg("ERRO: não foi possível conectar ao servidor. Tente novamente mais tarde.");
            setOkay(false);
        } catch (HttpResponseException hre) {
            setMsg("ERRO: não foi possível conectar ao servidor. Tente novamente mais tarde.");
            setOkay(false);
        } catch (RuntimeException re) {
            setMsg(re.getMessage());
            setOkay(false);
        }
    }
}

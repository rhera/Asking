package br.com.feapps.asking.DAO;

import android.util.Log;

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

import br.com.feapps.asking.model.Ranking;

/**
 * Created by F.Einstein on 012, 12/2/2016.
 */
public class RankingDAO extends WebServiceAskingDB {
    Ranking ranking;
    List<Ranking> rankingList;

    public RankingDAO() {
        super();
        ranking = new Ranking();
        rankingList = new ArrayList<>();
    }

    public List<Ranking> salvaRankingTemp(String str, int salaId) throws SoapFault {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "salvaRankingTemp");
        soap.addProperty("str", str);
        soap.addProperty("salaId", salaId);
        soap.addProperty("key", getObjectCript().getKey());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("salvaRankingTemp", envelope);
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

        Vector responseVec = null;
        SoapPrimitive soapPrimitive = null;
        try {
            responseVec = (Vector) envelope.getResponse();
        } catch (ClassCastException cce) {
            soapPrimitive = (SoapPrimitive) envelope.getResponse();
        }

        List<Ranking> retorno = new ArrayList<>();

        if (responseVec != null) {
            for (int i = 0; i < responseVec.size(); i++) {
                retorno.add(new Ranking(responseVec.get(i).toString()));
            }
        }
        if (soapPrimitive != null) {
            Log.e("rankdao", soapPrimitive.toString());
            retorno.add(new Ranking(soapPrimitive.toString()));
        }

        return retorno;
    }

    public List<Ranking> retornaRankingsTemps(int idSala) throws SoapFault {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "retornaRankingsTemps");
        soap.addProperty("idSala", idSala);
        soap.addProperty("key", getObjectCript().getKey());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        ChamaFuncaoWeb("retornaRankingsTemps", envelope);


        Vector responseVec = null;
        SoapPrimitive soapPrimitive = null;
        try {
            responseVec = (Vector) envelope.getResponse();
        } catch (ClassCastException cce) {
            soapPrimitive = (SoapPrimitive) envelope.getResponse();
        }

        this.rankingList = new ArrayList<>();

        if (responseVec != null) {
            for (int i = 0; i < responseVec.size(); i++) {
                this.rankingList.add(new Ranking(responseVec.get(i).toString()));
            }
        }

        if (soapPrimitive != null) {
            this.rankingList.add(new Ranking(soapPrimitive.toString()));
        }

        return this.rankingList;
    }

    public void salvaRankings(int salaId, int userId) throws SoapFault {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "salvaRankings");
        soap.addProperty("salaId", salaId);
        soap.addProperty("userId", userId);
        soap.addProperty("key", getObjectCript().getKey());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        ChamaFuncaoWeb("salvaRankings", envelope);
    }

    public List<Ranking> listarRankings() throws SoapFault {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "listarRankings");
        soap.addProperty("key", getObjectCript().getKey());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        ChamaFuncaoWeb("listarRankings", envelope);

        Vector vector = null;
        SoapPrimitive soapPrimitive = null;

        try {
            vector = (Vector) envelope.getResponse();
        }catch (ClassCastException cce) {
            soapPrimitive = (SoapPrimitive) envelope.getResponse();
        }

        if (vector != null) {
            this.rankingList = new ArrayList<>();
            for (int i = 0; i < vector.size(); i++) {
                this.ranking = new Ranking();
                this.ranking.toRanking(vector.get(i).toString());
                this.rankingList.add(this.ranking);
            }
        }

        if (soapPrimitive != null) {
            this.rankingList = new ArrayList<>();
            this.ranking = new Ranking();
            this.ranking.toRanking(soapPrimitive.toString());
            this.rankingList.add(this.ranking);
        }

        return this.rankingList;
    }

    public void ChamaFuncaoWeb (String func, SoapSerializationEnvelope envelope) {
        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call(func, envelope);
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
    }
}

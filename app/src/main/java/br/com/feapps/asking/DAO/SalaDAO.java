package br.com.feapps.asking.DAO;

import android.content.Context;
import android.database.Cursor;
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
import java.util.Date;
import java.util.List;
import java.util.Vector;

import br.com.feapps.asking.R;
import br.com.feapps.asking.model.Area;
import br.com.feapps.asking.model.Questao;
import br.com.feapps.asking.model.Sala;
import br.com.feapps.asking.model.SubArea;
import br.com.feapps.asking.model.Usuario;
import br.com.feapps.asking.util.CONST;
import br.com.feapps.asking.util.SalaArrayAdapter;

/**
 * Created by F.Einstein on 018, 18/1/2016.
 */
public class SalaDAO extends WebServiceAskingDB {

    Sala sala;
    List<Sala> salas;

    public SalaDAO() {
        super();
        sala = new Sala();
        salas = new ArrayList<>();
    }

    public Sala criarSala (int numQuestoes, String[] listSala, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "criarSala");
        String commasepratedString="";
        for(int i=0;i<listSala.length;i++) {
            if(i!=(listSala.length-1))
                commasepratedString=commasepratedString+listSala[i]+"@@";
            else
                commasepratedString=commasepratedString+listSala[i];
        }
        soap.addProperty("numQuestoes", numQuestoes);
        soap.addProperty("listSala", commasepratedString);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("criarSala", envelope);
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

        Vector soapResult = (Vector) envelope.getResponse();
        String[] vectorString = new String[soapResult.size()];
        for (int i = 0; i < soapResult.size(); i++) {
            if (soapResult.get(i) != null)
                vectorString[i] = soapResult.get(i).toString();
        }

        //for (String s : vectorString)
        //Log.e("saladao", s);

        sala = new Sala();
        sala.toSala(vectorString);

        //for (Questao q : sala.getConjuntoQuestoes().getQuestoes())
        //Log.d("saladao", q.getQuestao());

        return sala;
    }

    public Sala atualizaSala (int idSala, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "atualizaSala");
        soap.addProperty("idSala", idSala);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("atualizaSala", envelope);
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

        Vector response = (Vector) envelope.getResponse();

        sala = new Sala();

        String[] tempVectStr = new String[response.size()];

        for (int i = 0; i < tempVectStr.length; i++) {
            tempVectStr[i] = response.get(i).toString();
        }

        sala.toSala(tempVectStr);

        return sala;
    }

    public Sala adcUsuario (int salaId, String strUser, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "adcUsuario");
        soap.addProperty("salaId", salaId);
        soap.addProperty("strUser", strUser);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("adcUsuario", envelope);
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

        Vector response = (Vector) envelope.getResponse();

        sala = new Sala();

        String[] tempVectStr = new String[response.size()];

        for (int i = 0; i < tempVectStr.length; i++) {
            tempVectStr[i] = response.get(i).toString();
        }

        sala.toSala(tempVectStr);

        return sala;
    }

    public Sala removeUserSala (int userId, int salaId, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "removeUserSala");
        soap.addProperty("userId", userId);
        soap.addProperty("salaId", salaId);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("removeUserSala", envelope);
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

        Vector response = (Vector) envelope.getResponse();

        sala = new Sala();

        String[] tempVectStr = new String[response.size()];

        for (int i = 0; i < tempVectStr.length; i++) {
            tempVectStr[i] = response.get(i).toString();
        }

        sala.toSala(tempVectStr);

        return sala;
    }

    public Sala getSalaForId (int idSala, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "getSalaForId");
        soap.addProperty("idSala", idSala);
        soap.addProperty("key", key);

        Sala sala = new Sala();

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("getSalaForId", envelope);
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

        Vector response = (Vector) envelope.getResponse();

        String[] tempvectStr = new String[response.size()];

        for (int i = 0; i < response.size(); i++) {
            tempvectStr[i] = response.get(i).toString();
        }

        sala = new Sala();

        sala.toSala(tempvectStr);

        return sala;
    }

    public String[] listarSalas () throws SoapFault {

        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "listarSalas");
        soap.addProperty("key", getObjectCript().getKey());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("listarSalas", envelope);
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

        /*try {
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
            return soapPrimitive.toString();
        } catch (ClassCastException cce) {
            return "";
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return "";
        }*/

        Vector response = (Vector) envelope.getResponse();

        if (response != null) {
            String[] retorno = new String[response.size()];

            for (int i = 0; i < response.size(); i++)
                retorno[i] = String.valueOf(response.get(i));
            return retorno;
        } else {
            return null;
        }
    }

    public void atualizaSalaIdConjQuestoes (int idSala, int idConjuntoQuestoes, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "atualizaSalaIdConjQuestoes");
        soap.addProperty("idSala", idSala);
        soap.addProperty("idConjuntoQuestoes", idConjuntoQuestoes);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("atualizaSalaIdConjQuestoes", envelope);
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

    public int getNumSalasAtivas (String key) throws SoapFault {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "getNumSalasAtivas");
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("getNumSalasAtivas", envelope);
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

        int num = 0;

        SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

        if (soapPrimitive != null)
            num = Integer.parseInt(soapPrimitive.toString());

        return num;
    }

    public int deletarSala (int idSala, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "deletarSala");
        soap.addProperty("idSala", idSala);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("deletarSala", envelope);
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

        SoapPrimitive res = (SoapPrimitive) envelope.getResponse();

        return Integer.parseInt(res.toString());
    }

}

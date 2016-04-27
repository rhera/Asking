package br.com.feapps.asking.DAO;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Vector;

import br.com.feapps.asking.util.Cript;
import br.com.feapps.asking.util._Message;

/**
 * Created by F.Einstein on 028, 28/12/2015.
 */
public class WebServiceAskingDB extends _Message {

    private String url;
    private Cript cript;

    public WebServiceAskingDB () {
        super();
        cript = new Cript("");
        url = "http://10.0.0.24:8080/WAskingService/WebServiceAskingDB?WSDL";
    }

    public String getUrl() {
        return url;
    }

    public Cript getObjectCript() {
        return cript;
    }

    public String hello (String txt, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/","hello");
        soap.addProperty("name", txt);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(url);
            httpTrans.call("hello", envelope);
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

        SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

        String retorno = "";

        if (soapPrimitive != null)
            retorno = "online";
        else
            retorno = "offline";

        return retorno;
    }

    /*public boolean addUser (String[] listUser, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "addUser");
        String commasepratedString="";
        for(int i=0;i<listUser.length;i++) {
            if(i!=(listUser.length-1))
                commasepratedString=commasepratedString+listUser[i]+"@@";
            else
                commasepratedString=commasepratedString+listUser[i];
        }
        soap.addProperty("listUser", commasepratedString);
        soap.addProperty("key", key);

        //for (int i = 0; i <= 6; i++)
            //Log.d("AAAAAAAAAAA: ",commasepratedString);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(url);
            httpTrans.call("addUser", envelope);
        } catch (SocketTimeoutException ste) {
            setMsg("ERRO: não foi possível conectar ao servidor. Tente novamente mais tarde.");
            setOkay(false);
        } catch (RuntimeException re) {
            setMsg(re.getMessage());
            setOkay(false);
        }

        boolean retorno;
        if (envelope != null) {
            retorno = true;
        } else {
            retorno = false;
        }
        return retorno;
    }*/

    /*public int criarSala (String[] listSala, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "criarSala");
        String commasepratedString="";
        for(int i=0;i<listSala.length;i++) {
            if(i!=(listSala.length-1))
                commasepratedString=commasepratedString+listSala[i]+"@@";
            else
                commasepratedString=commasepratedString+listSala[i];
        }
        soap.addProperty("listSala", commasepratedString);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(url);
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

        SoapPrimitive res = (SoapPrimitive) envelope.getResponse();

        return Integer.parseInt(res.toString());
    }*/

    /*public List<Sala> listarSalas (String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "listarSalas");
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(url);
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
        }

        Vector vector = new Vector();

        try {
            vector = (Vector) envelope.getResponse();
        } catch (ClassCastException cce) {

        }


        List<Sala> retorno = new ArrayList<>();


        for (int i = 0; i < vector.size(); i+=5) {
            Sala s = new Sala();
            s.setId(Integer.parseInt(vector.get(i).toString()));
            s.setNome(vector.get(i + 1).toString());
            s.setTipo(vector.get(i + 2).toString());
            s.setSenha(vector.get(i + 3).toString());
            SoapObject soapObject = (SoapObject) vector.get(i + 4);
            Area a = new Area();
            a.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
            a.setNome(soapObject.getProperty("nome").toString());
            s.setArea(a);
            Log.d("TesteSala", s.toString());
            retorno.add(s);
        }

        /*for (Area area : retorno) {
            area.setSubAreas(listarSubAreasStr(area.getId(), cript.getKey()));
        }*/

        //return retorno;
    //}
}

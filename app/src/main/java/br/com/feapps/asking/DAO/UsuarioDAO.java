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

import br.com.feapps.asking.model.Usuario;
import br.com.feapps.asking.util.Cript;
import br.com.feapps.asking.util.SavedPreferences;

/**
 * Created by F.Einstein on 010, 10/2/2016.
 */
public class UsuarioDAO extends WebServiceAskingDB {
    Usuario user;
    List<Usuario> usuarios;

    public UsuarioDAO() {
        super();
        user = new Usuario();
        usuarios = new ArrayList<>();
    }

    public int addUser (String listUser, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "addUser");
        soap.addProperty("listUser", listUser);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("addUser", envelope);
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

    public Usuario login (String login, String senha, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/","login");
        soap.addProperty("login", login);
        soap.addProperty("senha", senha);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("login", envelope);
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
        } catch (SoapFault sf) {
            user = null;
            setMsg("Erro ao receber informações do servidor.");
            setOkay(false);
            return user;
        }


        SoapPrimitive res = (SoapPrimitive) envelope.getResponse();

        user = new Usuario();
        if (res != null) {
            if (!res.toString().equals("errohorario"))
                user.toUsuario(res.toString());
            else
                user.setNome("errohorario");
        }else
            user = null;

        return user;
    }

    public Usuario saveUser (String listUser, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "saveUser");
        soap.addProperty("listUser", listUser);
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("saveUser", envelope);
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

        user = new Usuario();
        if (res != null) {
            user.toUsuario(res.toString());
        }else
            user = null;

        return user;
    }

    public List<Usuario> listarUsuarios () throws SoapFault {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/","listarUsuarios");
        soap.addProperty("key", getObjectCript().getKey());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("listarUsuarios", envelope);
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

        usuarios = new ArrayList<>();
        String resposta = "";

        try {
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
            resposta = soapPrimitive.toString();
        } catch (ClassCastException cce) {
            resposta = "";
        }

        if (!resposta.equals("")) {
            String[] listVectorUser = SavedPreferences.toVectorString(resposta);
            for (int i = 0; i < listVectorUser.length; i++) {
                Usuario u = new Usuario();
                u.toUsuario(listVectorUser[i]);
                usuarios.add(u);
            }
        }

        return usuarios;
    }

    public Usuario resetarSala (int idUser) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "resetarSala");
        soap.addProperty("idUser", idUser);
        soap.addProperty("key", getObjectCript().getKey());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("resetarSala", envelope);
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

        user = new Usuario();
        if (res != null) {
            user.toUsuario(res.toString());
        }else
            user = null;

        return user;
    }
}

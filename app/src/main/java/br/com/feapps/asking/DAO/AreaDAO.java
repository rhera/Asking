package br.com.feapps.asking.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
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
import br.com.feapps.asking.model.SubArea;
import br.com.feapps.asking.util.Cript;
import br.com.feapps.asking.util.MessageBox;
import br.com.feapps.asking.view.NavigationActivity;

/**
 * Created by F.Einstein on 010, 10/1/2016.
 */
public class AreaDAO extends WebServiceAskingDB {

    Area area;
    List<Area> listAreas;
    SubArea subArea;
    List<SubArea> listSubAreas;
    Cript cript = new Cript("");

    Thread thread;
    List<Area> areas = new ArrayList<>();

    public AreaDAO() {
        super();
    }

    public List<Area> listarAreas (String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "listarAreas");
        soap.addProperty("key", key);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("listarAreas", envelope);
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

        List<Area> retorno = new ArrayList<>();
        Vector spret = (Vector) envelope.getResponse();

        if (spret != null) {
            //Log.d("Tamanho vector", spret.size()+"");
            //Log.d("Conteúdo do vector", spret.toString());
            for (int i = 0; i < spret.size(); i++) {
                SoapObject soapObject = (SoapObject) spret.get(i);
                //Log.d("Teste", soapObject.getProperty("id").toString() + " - " + soapObject.getProperty("nome").toString());
                Area a = new Area(Integer.parseInt(soapObject.getProperty("id").toString()), soapObject.getProperty("nome").toString());

                //Log.d("tamanho soapObject",soapObject.getPropertyCount()+"");
                //Log.d("conteúdo",soapObject.toString());

                List<SubArea> lsa = new ArrayList<>();
                for (int j = 2; j < soapObject.getPropertyCount(); j++) {
                    SoapObject soapObjectProperty = (SoapObject) soapObject.getProperty(j);
                    //Log.d("tamanho soapObjectProperty",soapObjectProperty.getPropertyCount()+"");
                    //Log.d("conteúdo",soapObjectProperty.toString());
                    SubArea sa = new SubArea(
                            Integer.parseInt(soapObjectProperty.getProperty("id").toString()),
                            soapObjectProperty.getProperty("nome").toString());
                    lsa.add(sa);
                }
                a.setSubAreas(lsa);

                //Log.d("Teste2", soapObject.getProperty(2).toString());
                retorno.add(a);
            }

        /*for (Area area : retorno) {
            area.setSubAreas(listarSubAreasStr(area.getId(), cript.getKey()));
        }*/

            return retorno;
        } else
            return new ArrayList<>();
    }

    /*public List<SubArea> listarSubAreas (int idArea, String key) throws XmlPullParserException, IOException {
        SoapObject soap = new SoapObject("http://dao.feapps.com.br/", "listarSubAreas");
        soap.addProperty("idArea", idArea);
        soap.addProperty("key", key);



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soap);

        try {
            HttpTransportSE httpTrans = new HttpTransportSE(getUrl());
            httpTrans.call("listarSubAreas", envelope);
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

        List<SubArea> retorno = new ArrayList<>();
        Vector spret = (Vector) envelope.getResponse();

        if (spret != null) {
            for (int i = 0; i < spret.size(); i++) {
                SoapObject soapObject = (SoapObject) spret.get(i);
                Log.d("Teste", soapObject.getProperty(0).toString());
                Log.d("Teste", soapObject.getProperty(1).toString());
                Log.d("Teste", soapObject.getProperty(2).toString());
                SubArea a = new SubArea(Integer.parseInt(soapObject.getProperty(1).toString()), soapObject.getProperty(2).toString());

                retorno.add(a);
            }
        } else
            retorno = new ArrayList<>();

        //for (Area area : retorno) {
            //area.setSubAreas(listarSubAreasStr(area.getId(), cript.getKey()));
        //}

        return retorno;
    }*/

}

package br.com.feapps.asking.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.GregorianCalendar;

/**
 * Created by F.Einstein on 009, 9/12/2015.
 */
public class Cript {
    private String cript;
    private String key;

    public Cript(String cript) {
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte messageDigest[] = new byte[0];
        try {
            messageDigest = algorithm.digest(cript.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            hexString.append(String.format("%02X", 0xFF & b));
        }
        this.cript = hexString.toString();
    }

    public String getCript() {
        return cript;
    }

    /*public void setCript(String cript) {
        this.cript = cript;
    }*/

    public String getKey() {
        GregorianCalendar calendar = new GregorianCalendar();
        Integer dia = calendar.get(GregorianCalendar.DAY_OF_MONTH);
        Integer mes = calendar.get(GregorianCalendar.MONTH);
        Integer ano = calendar.get(GregorianCalendar.YEAR);
        String tempKey = dia.toString() + mes.toString() + ano.toString();
        //System.out.println(dia.toString() + "/" + mes.toString() + "/" + ano.toString());
        tempKey += "chavefeappsasking";
        Cript cript = new Cript(tempKey);
        key = cript.getCript();
        return key;
    }
}

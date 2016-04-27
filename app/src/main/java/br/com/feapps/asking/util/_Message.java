package br.com.feapps.asking.util;

import android.app.AlertDialog;
import android.view.View;

/**
 * Created by F.Einstein on 002, 2/12/15.
 */
public class _Message {
    protected String _msg;
    protected boolean _okay;

    public _Message() {
        this._msg = "";
        this._okay = true;
    }

    public String getMsg() {
        return _msg;
    }

    public void setMsg(String _msg) {
        this._msg = _msg;
    }

    public boolean isOkay() {
        return _okay;
    }

    public void setOkay(boolean ok) {
        this._okay = ok;
    }

    public void showMsg(View view, String url) {
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Erro");
        builder.setMessage("Mensagem: " + this._msg);
        alerta = builder.create();
        alerta.show();
    }

    public void showDefMsg (View view, String msg) {
        AlertDialog alerta;
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Mensagem:");
        builder.setMessage(msg);
        alerta = builder.create();
        alerta.show();
    }
}

package br.com.feapps.asking.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by F.Einstein on 026, 26/10/15.
 */
public class MessageBox {

    public static void showInfo (Context ctx, String title, String msg, DialogInterface.OnClickListener listener) {
        show(ctx, title, msg, android.R.drawable.ic_dialog_info, listener);
    }

    public static void showAlert (Context ctx, String title, String msg, DialogInterface.OnClickListener listener) {
        show(ctx, title, msg, android.R.drawable.ic_dialog_alert, listener);
    }

    public static void show (Context ctx, String title, String msg, DialogInterface.OnClickListener listener) {
        show(ctx, title, msg, 0, listener);
    }

    public static void show (Context ctx, String title, String msg, int iconId, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(ctx);
        dlg.setIcon(iconId);
        dlg.setTitle(title);
        dlg.setMessage(msg);
        dlg.setNeutralButton("Ok", listener);
        dlg.show();
    }
}

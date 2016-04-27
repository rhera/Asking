package br.com.feapps.asking.util;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import br.com.feapps.asking.R;

/**
 * Created by F.Einstein on 026, 26/10/15.
 */
public class ViewHelper {

    public static ArrayAdapter<String> createArrayAdapter (Context ctx, Spinner spinner) {
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

        return arrayAdapter;
    }

    public static ArrayAdapter<String> createArrayAdapter (Context ctx, ListView listView) {
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(ctx, R.layout.activity_lista_jogo_questao);
        //arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_multiple_choice);

        listView.setAdapter(arrayAdapter);

        return arrayAdapter;
    }
}

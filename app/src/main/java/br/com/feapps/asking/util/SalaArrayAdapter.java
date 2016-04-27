package br.com.feapps.asking.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.feapps.asking.R;
import br.com.feapps.asking.model.Sala;

/**
 * Created by F.Einstein on 016, 16/1/2016.
 */
public class SalaArrayAdapter extends ArrayAdapter<Sala> {

    private int resource = 0;
    private LayoutInflater inflater;

    public SalaArrayAdapter (Context context, int resource) {
        super(context, resource);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(resource, parent, false);

            viewHolder.imageViewCadeado = (ImageView) view.findViewById(R.id.imageViewCadeado);
            viewHolder.textViewNomeSala = (TextView) view.findViewById(R.id.textViewNomeSala);
            viewHolder.textViewTipoSala = (TextView) view.findViewById(R.id.textViewTipoSala);
            viewHolder.textViewNomeArea = (TextView) view.findViewById(R.id.textViewNomeArea);
            viewHolder.textViewSeparador = (TextView) view.findViewById(R.id.textViewSeparador);
            viewHolder.textViewNomeSubArea = (TextView) view.findViewById(R.id.textViewNomeSubArea);

            view.setTag(viewHolder);

            convertView = view;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

            view = convertView;
        }

        Sala sala = (Sala) getItem(position);

        //TODO Colocar cadeado
        viewHolder.imageViewCadeado.setImageResource(R.drawable.ic_menu_salas);
        viewHolder.textViewNomeSala.setText(sala.getNome());
        viewHolder.textViewTipoSala.setText(sala.getTipo());
        viewHolder.textViewNomeArea.setText(sala.getArea().getNome());
        if (sala.getSubArea().getId() != 0) {
            viewHolder.textViewSeparador.setText("/");
            viewHolder.textViewNomeSubArea.setText(sala.getSubArea().getNome());
        } else {
            viewHolder.textViewSeparador.setText("");
            viewHolder.textViewNomeSubArea.setText("");
        }

        return view;
    }

    static class ViewHolder {
        ImageView imageViewCadeado;
        TextView textViewNomeSala;
        TextView textViewTipoSala;
        TextView textViewNomeArea;
        TextView textViewSeparador;
        TextView textViewNomeSubArea;
    }
}

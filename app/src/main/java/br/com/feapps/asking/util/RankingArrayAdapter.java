package br.com.feapps.asking.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.feapps.asking.R;
import br.com.feapps.asking.model.Ranking;
import br.com.feapps.asking.model.Sala;
import br.com.feapps.asking.model.Usuario;

/**
 * Created by F.Einstein on 022, 22/1/2016.
 */
public class RankingArrayAdapter extends ArrayAdapter<Ranking> {

    private int resource = 0;
    private LayoutInflater inflater;

    public RankingArrayAdapter(Context context, int resource) {
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

            viewHolder.textViewPosicao = (TextView) view.findViewById(R.id.textViewPosicao);
            viewHolder.imageViewFoto = (ImageView) view.findViewById(R.id.imageViewFoto);
            viewHolder.textViewNome = (TextView) view.findViewById(R.id.textViewNome);
            viewHolder.txtMedia5 = (TextView) view.findViewById(R.id.txtMedia5);
            viewHolder.txtMediaTempo = (TextView) view.findViewById(R.id.txtMediaTempo);

            view.setTag(viewHolder);

            convertView = view;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

            view = convertView;
        }

        Ranking ranking = (Ranking) getItem(position);

        viewHolder.textViewPosicao.setText(String.valueOf(position + 1) + "º");
        viewHolder.imageViewFoto.setColorFilter(View.DRAWING_CACHE_QUALITY_AUTO);
        if (ranking.getUsuario().getNome().length() > 7)
            viewHolder.textViewNome.setText(ranking.getUsuario().getNome().substring(0,7));
        else
            viewHolder.textViewNome.setText(ranking.getUsuario().getNome());
        viewHolder.txtMedia5.setText(String.valueOf(ranking.getMedia5acertos()));
        viewHolder.txtMediaTempo.setText(String.valueOf(ranking.getMediaTempo()));

        return view;
    }

    static class ViewHolder {
        TextView textViewPosicao;
        ImageView imageViewFoto;
        TextView textViewNome;
        TextView txtMedia5;
        TextView txtMediaTempo;
    }
}


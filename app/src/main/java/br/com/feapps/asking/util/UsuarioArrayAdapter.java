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
        import br.com.feapps.asking.model.Usuario;

/**
 * Created by F.Einstein on 022, 22/1/2016.
 */
public class UsuarioArrayAdapter extends ArrayAdapter<Usuario> {

    private int resource = 0;
    private LayoutInflater inflater;

    public UsuarioArrayAdapter(Context context, int resource) {
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

            viewHolder.imageViewFoto = (ImageView) view.findViewById(R.id.imageViewFoto);
            viewHolder.textViewNome = (TextView) view.findViewById(R.id.textViewNome);

            view.setTag(viewHolder);

            convertView = view;
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

            view = convertView;
        }

        Usuario user = (Usuario) getItem(position);

        viewHolder.imageViewFoto.setColorFilter(View.DRAWING_CACHE_QUALITY_AUTO);
        viewHolder.textViewNome.setText(user.getNome());
        //viewHolder.textViewTipoSala.setText(user.get);

        //TODO parei aqui 22/01/2016

        return view;
    }

    static class ViewHolder {
        ImageView imageViewFoto;
        TextView textViewNome;
    }
}


package es.uma.inftel.eyemandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.DownloadImageFromDropboxAsyncTask;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Grupo;
import es.uma.inftel.eyemandroid.entity.Usuario;

/**
 * Created by maramec on 23/03/2015.
 */
public class GruposAdapter extends BaseAdapter  {

    private Activity activity;
    private DialogManager dialogManager;
    private final LayoutInflater inflater;
    private List<Grupo> listaGrupos = new ArrayList<>();
    private Usuario usuario;

    public GruposAdapter(List<Grupo> listaGrupos, Usuario usuario, Activity activity) {
        this.activity = activity;
        //this.dialogManager = dialogManager;
        this.listaGrupos = listaGrupos;
        this.inflater = LayoutInflater.from(activity);
        this.usuario = usuario;
    }

    @Override
    public int getCount() {
        return listaGrupos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaGrupos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuEntryViewHolder viewHolder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_grupos, null);
            viewHolder = new MenuEntryViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MenuEntryViewHolder) convertView.getTag();
        }

        final Grupo grupo = listaGrupos.get(position);

        viewHolder.textoNombre = (TextView) convertView.findViewById(R.id.group_name);
        viewHolder.textoNombre.setText(grupo.getNombreGrupo());

        if (grupo.getImagenMiniatura() != null && !grupo.getImagenMiniatura().equals(".")) {
            viewHolder.imagenMiniatura = (ImageView) convertView.findViewById(R.id.imagen_miniatura_grupo);
            viewHolder.imagenMiniatura.setVisibility(View.VISIBLE);
            new DownloadImageFromDropboxAsyncTask(activity, viewHolder.imagenMiniatura).execute(grupo.getImagenMiniatura());
        }else {
            viewHolder.imagenMiniatura = (ImageView) convertView.findViewById(R.id.imagen_miniatura_grupo);
            viewHolder.imagenMiniatura.setVisibility(View.VISIBLE);
            viewHolder.imagenMiniatura.setImageDrawable(activity.getResources().getDrawable(R.drawable.google_grupos));
        }
        return convertView;
    }

    private static class MenuEntryViewHolder {

        private ImageView imagenMiniatura;
        private TextView textoNombre;
        //private ImageButton borrar;

    }
}
package es.uma.inftel.eyemandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.DownloadImageFromDropboxAsyncTask;
import es.uma.inftel.eyemandroid.asynctask.LoadImageAsyncTask;
import es.uma.inftel.eyemandroid.entity.Post;

/**
 * Created by Emilio on 21/03/2015.
 */
public class PerfilAdapter extends BaseAdapter {

    private Activity activity;
    private final LayoutInflater inflater;
    private List<Post> listaPost = new ArrayList<>();

    public PerfilAdapter(Activity activity, List<Post> listaPost) {
        this.activity = activity;
        this.listaPost = listaPost;
        this.inflater = LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return listaPost.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPost.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuEntryViewHolder viewHolder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_perfil, null);
            viewHolder = new MenuEntryViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MenuEntryViewHolder) convertView.getTag();
        }

        Post post = listaPost.get(position);

        if (post.getCreador().getImagen() != null) {
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.user_post_avatar);
            new LoadImageAsyncTask(viewHolder.avatar).execute(post.getCreador().getImagen());
        }

        viewHolder.textoNombre = (TextView) convertView.findViewById(R.id.user_post_name);
        viewHolder.textoNombre.setText(post.getCreador().getNombre());

        viewHolder.contenido = (TextView) convertView.findViewById(R.id.user_post_content);
        viewHolder.contenido.setText(post.getContenido());

        viewHolder.fecha = (TextView) convertView.findViewById(R.id.user_post_date);
        SimpleDateFormat fmt = new SimpleDateFormat("dd MMM - HH:mm");
        fmt.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        String fechabien = fmt.format(post.getIdPost());
        viewHolder.fecha.setText(fechabien);

        if (post.getImagen() != null && !post.getImagen().equals(".")) {
            viewHolder.imagen = (ImageView) convertView.findViewById(R.id.user_post_image);
            viewHolder.imagen.setVisibility(View.VISIBLE);
            new DownloadImageFromDropboxAsyncTask(activity, viewHolder.imagen).execute(post.getImagen());
        }

        return convertView;
    }

    private static class MenuEntryViewHolder {

        private ImageView avatar;
        private ImageView imagen;
        private TextView textoNombre;
        private TextView contenido;
        private TextView fecha;

    }
}

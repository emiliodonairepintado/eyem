package es.uma.inftel.eyemandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.BorrarPostAsyncTask;
import es.uma.inftel.eyemandroid.asynctask.LoadImageAsyncTask;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Post;
import es.uma.inftel.eyemandroid.entity.Usuario;

/**
 * Created by maramec on 24/03/2015.
 */
public class PostGrupoAdapter extends EyemBaseAdapter {

    private Activity activity;
    private DialogManager dialogManager;
    private final LayoutInflater inflater;
    private List<Post> listaPost = new ArrayList<>();
    private Usuario usuario;

    public PostGrupoAdapter(Activity activity, DialogManager dialogManager, List<Post> listaPost, Usuario usuario) {
        super(listaPost);
        this.activity = activity;
        this.dialogManager = dialogManager;
        this.listaPost = listaPost;
        this.inflater = LayoutInflater.from(activity);
        this.usuario = usuario;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PostGrupoViewHolder viewHolder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_post_grupo, null);
            viewHolder = new PostGrupoViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PostGrupoViewHolder) convertView.getTag();
        }

        final Post post = listaPost.get(position);

        if (post.getCreador().getImagen() != null) {
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.user_post_avatar);
            new LoadImageAsyncTask(viewHolder.avatar).execute(post.getCreador().getImagen());
        }

        viewHolder.borrar = (ImageButton) convertView.findViewById(R.id.btnborrar);

        String emailUsuario = usuario.getEmail();
        String emailPost = post.getCreador().getEmail();

        if (emailUsuario.equals(emailPost)) {
            viewHolder.borrar.setVisibility(View.VISIBLE);
            viewHolder.borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BorrarPostAsyncTask(activity, dialogManager, post, PostGrupoAdapter.this, position).execute();
                }
            });
        }else{
            viewHolder.borrar.setVisibility(View.GONE);
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
            viewHolder.imagen = (ImageView) convertView.findViewById(R.id.indfoto);
            viewHolder.imagen.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imagen = (ImageView) convertView.findViewById(R.id.indfoto);
            viewHolder.imagen.setVisibility(View.GONE);
        }

        if (post.getVideo() != null && !post.getImagen().equals("")) {
            viewHolder.estadoVideo = (ImageView) convertView.findViewById(R.id.indvideo);
            viewHolder.estadoVideo.setVisibility(View.VISIBLE);
        } else {
            viewHolder.estadoVideo = (ImageView) convertView.findViewById(R.id.indvideo);
            viewHolder.estadoVideo.setVisibility(View.GONE);
        }
        return convertView;
    }

    private static class PostGrupoViewHolder {

        private ImageView avatar;
        private ImageView imagen;
        private ImageView estadoVideo;
        private TextView textoNombre;
        private TextView contenido;
        private TextView fecha;
        //private ImageButton compartir;
        private ImageButton borrar;

    }

}

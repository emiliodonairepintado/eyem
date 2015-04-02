package es.uma.inftel.eyemandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.LoadImageAsyncTask;
import es.uma.inftel.eyemandroid.entity.Usuario;

/**
 * Created by maramec on 24/03/2015.
 */
public class ListUsersGroupAdapter extends BaseAdapter {
    private Activity activity;
    private final LayoutInflater inflater;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private Usuario usuarioRegistrado;

    public ListUsersGroupAdapter(Activity activity, ArrayList<Usuario> listaUsuarios, Usuario usuarioRegistrado){
        this.listaUsuarios = listaUsuarios;
        inflater = LayoutInflater.from(activity);
        this.usuarioRegistrado = usuarioRegistrado;
    }

    @Override
    public int getCount() {
        return listaUsuarios.size();
    }

    @Override
    public Object getItem(int position) {
        return listaUsuarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MenuEntryViewHolder viewHolder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_usuario, null);
            viewHolder = new MenuEntryViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MenuEntryViewHolder) convertView.getTag();
        }

        Usuario usuario = listaUsuarios.get(position);

        viewHolder.avatar = (ImageView) convertView.findViewById(R.id.ivAvatarUsuario);
        new LoadImageAsyncTask(viewHolder.avatar).execute(usuario.getImagen());

        viewHolder.textNombre = (TextView) convertView.findViewById(R.id.tvNombreUsuario);
        viewHolder.textNombre.setText(usuario.getNombre());

        viewHolder.textEmail = (TextView) convertView.findViewById(R.id.tvEmailUsuario);
        viewHolder.textEmail.setText(usuario.getEmail());


        return convertView;
    }

    private static class MenuEntryViewHolder {

        private ImageView avatar;
        private TextView textNombre;
        private TextView textEmail;

    }
}

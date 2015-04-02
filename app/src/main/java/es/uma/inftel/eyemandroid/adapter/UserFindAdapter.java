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
 * Created by inftel21 on 20/3/15.
 */
public class UserFindAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private final ArrayList<Usuario> listaUsuarios;

    public UserFindAdapter(Activity activity, ArrayList listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
        this.inflater = LayoutInflater.from(activity);
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

        UserFindViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_usuario, null);
            viewHolder = new UserFindViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UserFindViewHolder) convertView.getTag();
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

    private static class UserFindViewHolder {

        private ImageView avatar;
        private TextView textNombre;
        private TextView textEmail;

    }
}

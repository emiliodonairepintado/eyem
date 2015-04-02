package es.uma.inftel.eyemandroid.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.LoadImageAsyncTask;
import es.uma.inftel.eyemandroid.entity.Usuario;

/**
 * Created by maramec on 20/03/2015.
 */
public class ListUsersAdapter extends BaseAdapter {

    private Activity activity;
    private final LayoutInflater inflater;
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private Usuario usuarioRegistrado;
    private List<Usuario> usuariosAntiguos;

    public ListUsersAdapter(Activity activity, ArrayList<Usuario> listaUsuarios, Usuario usuarioRegistrado,List<Usuario> usuariosAntiguos){
        this.listaUsuarios = listaUsuarios;
        inflater = LayoutInflater.from(activity);
        this.usuarioRegistrado = usuarioRegistrado;
        this.usuariosAntiguos = usuariosAntiguos;
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
        int i;
        boolean estaSeleccionado=false;
        for(i=0;i<usuariosAntiguos.size();i++){
            if(usuario.getEmail().compareTo(usuariosAntiguos.get(i).getEmail())==0){
                estaSeleccionado = true;
                break;
            }

        }

            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.ivAvatarUsuario);
            new LoadImageAsyncTask(viewHolder.avatar).execute(usuario.getImagen());

            viewHolder.textNombre = (TextView) convertView.findViewById(R.id.tvNombreUsuario);
            viewHolder.textNombre.setText(usuario.getNombre());

            viewHolder.textEmail = (TextView) convertView.findViewById(R.id.tvEmailUsuario);
            viewHolder.textEmail.setText(usuario.getEmail());
        if(estaSeleccionado){
            convertView.setBackgroundColor(Color.parseColor("#42727272"));
        }
        else{
            convertView.setBackgroundColor(Color.TRANSPARENT);
        }

        return convertView;
    }

    private static class MenuEntryViewHolder {

        private ImageView avatar;
        private TextView textNombre;
        private TextView textEmail;

    }
}

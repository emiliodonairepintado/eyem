package es.uma.inftel.eyemandroid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.activity.PerfilActivity;
import es.uma.inftel.eyemandroid.adapter.ListUsersGroupAdapter;
import es.uma.inftel.eyemandroid.entity.Grupo;
import es.uma.inftel.eyemandroid.entity.Usuario;

/**
 * Created by maramec on 24/03/2015.
 */
public class UsuariosGrupoFragment extends UsuarioLogeadoFragment {


    private static Grupo grupoSeleccionado;
    private ListView listView;
    //private ProgressDialog progressDialog;

    public static UsuariosGrupoFragment newInstance(Usuario usuario, Grupo grupo) {
        UsuariosGrupoFragment usuariosGrupoFragment = new UsuariosGrupoFragment();
        Bundle args = new Bundle();
        args.putParcelable("usuario", usuario);
        args.putParcelable("grupo", grupo);
        usuariosGrupoFragment.setArguments(args);
        grupoSeleccionado = new Grupo();
        grupoSeleccionado = grupo;
        return usuariosGrupoFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_usuarios_grupo, container, false);

        final ArrayList<Usuario> listaUsuarios = (ArrayList<Usuario>) grupoSeleccionado.getListaUsuarios();

        listView=(ListView) rootView.findViewById(R.id.listaUsuariosGrupo);
        listView.setAdapter(new ListUsersGroupAdapter(getActivity(),listaUsuarios, getUsuario()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PerfilActivity.class);
                intent.putExtra("usuario", listaUsuarios.get(position));
                getActivity().startActivity(intent);
            }
        });

        //FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabButton);
        //fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));
        //fab.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));

        /*fab.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPost();
            }
        });
        Log.d("hola", getUsuario().getEmail());*/

        //
        //new FindAllPostAsyncTask(getActivity(), this, listView, getUsuario()).execute();

        return rootView;
    }

}

package es.uma.inftel.eyemandroid.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.activity.CrearPostGruposActivity;
import es.uma.inftel.eyemandroid.asynctask.LoadPostGrupoAsyncTask;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Grupo;
import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.widget.FloatingActionButton;

/**
 * Created by maramec on 24/03/2015.
 */
public class PostGrupoFragment extends UsuarioLogeadoFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static Grupo grupoSeleccionado;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private ProgressDialog progressDialog;

    public static PostGrupoFragment newInstance(Usuario usuario, Grupo grupo) {
        PostGrupoFragment postGrupoFragment = new PostGrupoFragment();
        Bundle args = new Bundle();
        args.putParcelable("usuario", usuario);
        args.putParcelable("grupo", grupo);
        postGrupoFragment.setArguments(args);
        grupoSeleccionado = new Grupo();
        grupoSeleccionado = grupo;
        return postGrupoFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_grupo, container, false);

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        swipeLayout.setOnRefreshListener(this);

        listView = (ListView) rootView.findViewById(R.id.listaPostGrupo);
        findPosts();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));
        fab.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));

        fab.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPostGrupo();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        findPosts();
    }

    //reutilizar crearPost
    private void crearPostGrupo() {
        Intent intent = new Intent(getActivity(), CrearPostGruposActivity.class);
        intent.putExtra("usuario", getUsuario());
        intent.putExtra("idGrupo", grupoSeleccionado.getIdGrupo().toString());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                findPosts();
            }
        }, 0);
    }

    private void findPosts() {
        new LoadPostGrupoAsyncTask(getActivity(), (DialogManager) getActivity(), swipeLayout, listView, getUsuario()).execute(grupoSeleccionado.getIdGrupo());
    }
}

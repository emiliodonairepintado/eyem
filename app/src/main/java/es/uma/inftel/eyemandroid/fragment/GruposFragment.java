package es.uma.inftel.eyemandroid.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.activity.CrearGrupoActivity;
import es.uma.inftel.eyemandroid.asynctask.FindGroupsAsyncTask;
import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.widget.FloatingActionButton;


public class GruposFragment extends UsuarioLogeadoFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeLayout;
    private ListView listView;

    public static GruposFragment newInstance(Usuario usuario) {
        GruposFragment gruposFragment = new GruposFragment();
        Bundle args = new Bundle();
        args.putParcelable("usuario", usuario);
        gruposFragment.setArguments(args);
        return gruposFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grupos, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));
        fab.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));

        fab.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearGrupo();
            }
        });

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        swipeLayout.setOnRefreshListener(this);

        listView=(ListView) rootView.findViewById(R.id.listaGrupos);
        findGroups();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        findGroups();
    }

    private void crearGrupo() {
        Intent intent = new Intent(getActivity(), CrearGrupoActivity.class);
        intent.putExtra("usuario",getUsuario());
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                findGroups();
            }
        }, 0);
    }

    private void findGroups() {
        new FindGroupsAsyncTask(getActivity(), swipeLayout, listView, getUsuario()).execute(getUsuario().getEmail());
    }
}
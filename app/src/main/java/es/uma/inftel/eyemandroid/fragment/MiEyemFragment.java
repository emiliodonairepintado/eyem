package es.uma.inftel.eyemandroid.fragment;

import android.app.Activity;
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
import es.uma.inftel.eyemandroid.activity.CrearPostActivity;
import es.uma.inftel.eyemandroid.asynctask.FindUserPostsAsyncTask;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.widget.FloatingActionButton;


public class MiEyemFragment extends UsuarioLogeadoFragment implements SwipeRefreshLayout.OnRefreshListener, DialogManager {

    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private ProgressDialog progressDialog;

    public static MiEyemFragment newInstance(Usuario usuario) {
        MiEyemFragment miEyemFragment = new MiEyemFragment();
        Bundle args = new Bundle();
        args.putParcelable("usuario", usuario);
        miEyemFragment.setArguments(args);
        return miEyemFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mieyem, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.plus));
        fab.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));

        fab.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPost();
            }
        });

        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refreshLayout);
        swipeLayout.setOnRefreshListener(this);

        listView = (ListView) rootView.findViewById(R.id.listaPost);
        findPosts();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        findPosts();
    }

    private void crearPost() {
        Intent intent = new Intent(getActivity(), CrearPostActivity.class);
        intent.putExtra("Usuario",getUsuario());
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
        new FindUserPostsAsyncTask(getActivity(), this, swipeLayout, listView, getUsuario()).execute(getUsuario().getEmail());
    }

    @Override
    public void createDialog() {
        dismissDialog();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.creando_post));
    }

    @Override
    public void createDialog(String message) {
        dismissDialog();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(message);
    }

    @Override
    public void showDialog() {
        if (progressDialog != null) {
            progressDialog.show();
        }
    }

    @Override
    public void hideDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    @Override
    public void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public Activity getDialogManagerActivity() {
        return getActivity();
    }
}
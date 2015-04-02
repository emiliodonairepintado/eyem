package es.uma.inftel.eyemandroid.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.InsertGroupAsyncTask;
import es.uma.inftel.eyemandroid.asynctask.LoadUsersAsyncTask;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Grupo;
import es.uma.inftel.eyemandroid.entity.Usuario;

public class ListaUsuariosGrupoActivity extends ActionBarActivity implements DialogManager {

    private Toolbar toolbar;
    private ListView listaUsuarios;
    private List<Usuario> listaSeleccionados;
    private Usuario usuarioRegistrado;
    private String foto;
    private String fotoPeq;
    private String nombreGrupo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios_grupo);
        listaUsuarios = (ListView) findViewById(R.id.listaUsuariosGrupo);
        nombreGrupo = getIntent().getStringExtra("nombreGrupo");
        foto = getIntent().getStringExtra("imagenGrupo");
        fotoPeq = getIntent().getStringExtra("imagenGrupoPeq");
        usuarioRegistrado = getIntent().getParcelableExtra("usuario");
        //listaUsuarios.setAdapter(new ListUsersAdapter(this, ));
        List<Usuario> lista = new ArrayList<>();
        new LoadUsersAsyncTask(this, listaUsuarios, usuarioRegistrado, lista).execute();
        initToolbar();

        listaSeleccionados = new ArrayList<>();
        //listaUsuarios.setSelector(R.drawable.selector_user_list);
        listaUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Usuario usuarioSeleccionado = (Usuario) listaUsuarios.getAdapter().getItem(position);

                if(listaSeleccionados.contains(usuarioSeleccionado)) {
                    listaSeleccionados.remove(usuarioSeleccionado);
                    view.setBackgroundColor(Color.TRANSPARENT);
                }else{
                    listaSeleccionados.add(usuarioSeleccionado);
                    view.setBackgroundColor(Color.parseColor("#42727272"));
                }
            }
        });
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.abc_tab_indicator_material);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_usuarios_grupo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnCrearGrupo:
                Grupo grupo = new Grupo();
                grupo.setCreador(usuarioRegistrado);
                grupo.setNombreGrupo(nombreGrupo);
                if (listaSeleccionados.isEmpty()) {
                    Toast.makeText(this, getString(R.string.msg_usuarios_insuficientes), Toast.LENGTH_LONG).show();
                    return true;
                }
                listaSeleccionados.add(usuarioRegistrado);
                grupo.setListaUsuarios(listaSeleccionados);
                grupo.setIdGrupo(System.currentTimeMillis());
                grupo.setImagen(foto);
                grupo.setImagenMiniatura(fotoPeq);
                new InsertGroupAsyncTask(this, this).execute(grupo);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void createDialog() {
        dismissDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.creando_grupo));
    }

    @Override
    public void createDialog(String message) {
        dismissDialog();
        progressDialog = new ProgressDialog(this);
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
        return this;
    }
}

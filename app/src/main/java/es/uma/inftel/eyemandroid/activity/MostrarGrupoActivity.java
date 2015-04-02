package es.uma.inftel.eyemandroid.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.adapter.GroupPagerAdapter;
import es.uma.inftel.eyemandroid.asynctask.DeleteGroupAsyncTask;
import es.uma.inftel.eyemandroid.asynctask.OperacionGrupo;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Grupo;
import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.widget.SlidingTabLayout;

public class MostrarGrupoActivity extends ActionBarActivity implements DialogManager {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private Usuario usuario;
    private Grupo grupo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_grupo);

        usuario = getIntent().getParcelableExtra("usuario");
        grupo = getIntent().getParcelableExtra("grupoSeleccionado");

        initToolbar();
        initTabs(usuario);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (usuario.getEmail().equals(grupo.getCreador().getEmail())) {
            getMenuInflater().inflate(R.menu.menu_mostrar_grupo_admin, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_mostrar_grupo, menu);
        }
        return true;
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

    private void initTabs(Usuario usuario) {
        final ViewPager pager = (ViewPager) findViewById(R.id.viewpager_group);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs_group);

        pager.setAdapter(new GroupPagerAdapter(getSupportFragmentManager(), usuario, grupo));

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnEliminarGrupo:
                Grupo g = new Grupo();
                g.setIdGrupo(1L);
                new DeleteGroupAsyncTask(this, this, grupo, g).execute();
                return true;
            case R.id.btnEditarGrupo:
                Intent intent = new Intent(this, EditGroupActivity.class);
                intent.putExtra("grupo", grupo);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                return true;
            case R.id.btnSalirGrupo:
                salirGrupo(usuario, grupo);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void salirGrupo(Usuario u, Grupo g){
        for(int i = 0; i < g.getListaUsuarios().size(); i++){
            if(u.getEmail().compareTo(g.getListaUsuarios().get(i).getEmail())==0){
                g.getListaUsuarios().remove(i);
            }
        }
        Grupo grupoActualizado = new Grupo();
        grupoActualizado.setIdGrupo(g.getIdGrupo());
        grupoActualizado.setImagenMiniatura(g.getImagenMiniatura());
        grupoActualizado.setImagen(g.getImagen());
        grupoActualizado.setCreador(g.getCreador());
        grupoActualizado.setNombreGrupo(g.getNombreGrupo());
        grupoActualizado.setListaUsuarios(g.getListaUsuarios());
        new DeleteGroupAsyncTask(this, this, grupo, grupoActualizado, OperacionGrupo.SALIR).execute();
    }

    @Override
    public void createDialog() {
        dismissDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.editando_grupo));
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

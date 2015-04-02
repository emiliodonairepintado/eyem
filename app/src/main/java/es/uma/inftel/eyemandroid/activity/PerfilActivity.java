package es.uma.inftel.eyemandroid.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.FindUserPublicPostsAsyncTask;
import es.uma.inftel.eyemandroid.asynctask.LoadImageAsyncTask;
import es.uma.inftel.eyemandroid.entity.Usuario;

public class PerfilActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeLayout;
    private LinearLayout linearLayout;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        initToolbar();

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        swipeLayout.setOnRefreshListener(this);

        usuario = getIntent().getParcelableExtra("usuario");

        if (usuario.getImagen() != null) {
            ImageView ivUserAvatar = (ImageView) findViewById(R.id.user_avatar);
            new LoadImageAsyncTask(ivUserAvatar).execute(usuario.getImagen());
        }

        if (usuario.getImagenCover() != null) {
            ImageView ivUserCoverImage = (ImageView) findViewById(R.id.user_cover_image);
            new LoadImageAsyncTask(ivUserCoverImage).execute(usuario.getImagenCover());
        }

        TextView tvUserName = (TextView) findViewById(R.id.user_name);
        tvUserName.setText(usuario.getNombre());

        TextView tvUserEmail = (TextView) findViewById(R.id.user_email);
        tvUserEmail.setText(usuario.getEmail());

        linearLayout = (LinearLayout) findViewById(R.id.listaPost);
        new FindUserPublicPostsAsyncTask(this, swipeLayout, linearLayout).execute(usuario.getEmail());
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
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        final Activity activity = this;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                new FindUserPublicPostsAsyncTask(activity, swipeLayout, linearLayout).execute(usuario.getEmail());
            }
        }, 0);
    }
}

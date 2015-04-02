package es.uma.inftel.eyemandroid.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.BorrarPostAsyncTask;
import es.uma.inftel.eyemandroid.asynctask.DownloadImageFromDropboxAsyncTask;
import es.uma.inftel.eyemandroid.asynctask.LoadImageAsyncTask;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Post;
import es.uma.inftel.eyemandroid.entity.Usuario;

/**
 * Created by Emilio on 23/03/2015.
 */
public class VerPostActivity extends ActionBarActivity implements DialogManager {

    private Toolbar toolbar;

    private TextView nombre;
    private TextView contenido;
    private TextView fecha;
    private ImageView imagen;
    private ImageView avatar;
    private TextView sitio;
    private ImageView borrar;

    private String post_contenido;
    private String post_nombre;
    private String post_fecha;
    private String post_imagen;
    private String post_avatar;
    private String post_localizacion;
    private String emailUsuario;
    private String emailPost;

    private Post post;
    private Usuario usuario;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Activity activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verpost);

        //Datos del post
        post = getIntent().getParcelableExtra("Post");
        usuario = getIntent().getParcelableExtra("Usuario");
        post_contenido = post.getContenido();
        post_nombre = post.getCreador().getNombre();
        post_avatar = post.getCreador().getImagen();
        post_imagen = post.getImagen();
        post_fecha = post.getIdPost().toString();
        post_localizacion = post.getLocalizacion();

        nombre = (TextView) findViewById(R.id.VP_user_name);
        nombre.setText(post_nombre);
        contenido = (TextView) findViewById(R.id.VP_post_content);
        contenido.setText(post_contenido);

        SimpleDateFormat fmt = new SimpleDateFormat("dd MMM - HH:mm");
        fmt.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        String fechabien = fmt.format(post.getIdPost());
        fecha = (TextView) findViewById(R.id.VP_post_date);
        fecha.setText(fechabien);

        if (post.getCreador().getImagen() != null) {
            avatar = (ImageView) findViewById(R.id.MV_user_avatar);
            new LoadImageAsyncTask(avatar).execute(post.getCreador().getImagen());
        }

        if (post.getImagen() != null && !post.getImagen().equals(".")) {
            imagen = (ImageView) findViewById(R.id.VP_image);
            RelativeLayout fondo = (RelativeLayout) findViewById(R.id.fondogradiente);
            fondo.setVisibility(View.VISIBLE);
            imagen.setVisibility(View.VISIBLE);
            new DownloadImageFromDropboxAsyncTask(this, imagen).execute(post.getImagen());
        }else{
            RelativeLayout fondo = (RelativeLayout) findViewById(R.id.fondogradiente);
            fondo.setVisibility(View.INVISIBLE);
        }

        sitio = (TextView) findViewById(R.id.VP_post_localizacion);
        if (post_localizacion != null && !post_localizacion.equals("Sin datos de gps")){
            sitio.setText(post_localizacion);
            sitio.setVisibility(View.VISIBLE);
        }else{
            sitio.setVisibility(View.INVISIBLE);
        }

        if (post.getVideo() != null && !post.getVideo().equals("")) {
            ImageButton btnVerVideo = (ImageButton) findViewById(R.id.btnVerVideo);
            btnVerVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VerPostActivity.this, YouTubePlayerActivity.class);
                    intent.putExtra("VIDEO_ID", post.getVideo());
                    startActivity(intent);
                }
            });
            btnVerVideo.setVisibility(View.VISIBLE);
        }

        borrar = (ImageButton) findViewById(R.id.botonborrarverpost);
        String emailUsuario = usuario.getEmail();
        String emailPost = post.getCreador().getEmail();

        if (emailUsuario.equals(emailPost)) {
            borrar.setVisibility(View.VISIBLE);
            borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new BorrarPostAsyncTask(activity, VerPostActivity.this, post).execute();
                }
            });
        }else{
            borrar.setVisibility(View.GONE);
        }

        initToolbar();
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void createDialog() {
        dismissDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.eliminando_post));
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

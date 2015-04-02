package es.uma.inftel.eyemandroid.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.util.FileUtils;
import es.uma.inftel.eyemandroid.util.ImageUtils;
import es.uma.inftel.eyemandroid.widget.FloatingActionButton;

public class CrearGrupoActivity extends ActionBarActivity {

    private static final String TAG = "Log" ;
    private static final int MAX_IMAGE_SIZE = 512;
    private static final int MAX_IMAGE_SMALL_SIZE = 64;

    private static final int CAPTURAR_IMAGEN = 1;
    private static final int SELECCIONAR_IMAGEN = 2;
    private static final int CONTENIDO_TECLADO = 4;

    private Toolbar toolbar;
    private Usuario usuarioRegistrado;

    private ImageView imageViewPhoto;
    private Uri imageUri;
    private File groupImageFile;
    private File groupImageFileSmall;
    private Bitmap scaledGroupImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_grupo);
        usuarioRegistrado = getIntent().getParcelableExtra("usuario");
        imageViewPhoto = (ImageView) findViewById(R.id.foto_crear);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.ic_camera_alt_white_24dp));
        fab.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crear_grupo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.btnSigGrupo:
                EditText nombreGrupo = (EditText) findViewById(R.id.nombreGrupo);
                String nombre = nombreGrupo.getText().toString();
                if (nombre.equals("")) {
                    Toast.makeText(this, getString(R.string.msg_nombre_grupo_vacio), Toast.LENGTH_LONG).show();
                    return true;
                }
                Intent intent = new Intent(this, ListaUsuariosGrupoActivity.class);
                intent.putExtra("nombreGrupo", nombre);
                intent.putExtra("imagenGrupo", (groupImageFile != null) ? groupImageFile.getAbsolutePath() : ".");
                intent.putExtra("imagenGrupoPeq", (groupImageFileSmall != null) ? groupImageFileSmall.getAbsolutePath() : ".");
                intent.putExtra("usuario", usuarioRegistrado);
                startActivity(intent);
                this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectPhoto(View view) {
        final CharSequence[] opcionesCamara = getResources().getTextArray(R.array.camara);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_camera);
        builder.setTitle(R.string.options);
        builder.setItems(opcionesCamara, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (opcionesCamara[item].equals("Camara")) {
                    captureCameraImage();
                } else if (opcionesCamara[item].equals("Galeria")) {
                    Log.i("CAMARA", "entra aqui");
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, SELECCIONAR_IMAGEN);
                }
            }
        });
        builder.show();
    }

    private void captureCameraImage() {
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                groupImageFile = FileUtils.createTemporaryFile("picture", ".jpg");
            } catch(Exception e) {
                Log.e(TAG, "Can't create file to take picture!", e);
                Toast.makeText(this, getString(R.string.err_check_sdcard), Toast.LENGTH_LONG).show();
                return;
            }
            imageUri = Uri.fromFile(groupImageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, CAPTURAR_IMAGEN);
        } catch (Exception e) {
            Log.e(TAG, "Can't create file to take picture!", e);
            Toast.makeText(getApplicationContext(), "Error al tomar la foto", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURAR_IMAGEN) {
                loadImageFromUri(imageUri);
            } else if (requestCode == SELECCIONAR_IMAGEN) {
                Uri selectedImageUri = data.getData();
                loadImageFromUri(selectedImageUri);
            } else if (requestCode == CONTENIDO_TECLADO){
                EditText contenido = (EditText) findViewById(R.id.nombreGrupo);
                contenido.setText(data.getStringExtra("texto"));
            }
        }
    }

    private void loadImageFromUri(Uri imageUri) {
        Bitmap image = ImageUtils.getImageFromUri(this, imageUri);
        if (image != null) {
            image = ImageUtils.createScaledBitmap(image, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE);
            if (groupImageFile != null) {
                groupImageFile.delete();
            }
            groupImageFile = null;
            try {
                groupImageFile = ImageUtils.storeImage(image, FileUtils.createTemporaryFile("scaled-picture", ".jpg"));
                groupImageFileSmall = ImageUtils.storeImage(image, MAX_IMAGE_SMALL_SIZE, MAX_IMAGE_SMALL_SIZE, FileUtils.createTemporaryFile("scaled-picture-small", ".jpg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            setupScaledPostImage(image);
            imageViewPhoto.setImageBitmap(scaledGroupImage);
        }
    }

    private void setupScaledPostImage(Bitmap bitmap) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;

        if (displayWidth < MAX_IMAGE_SIZE) {
            scaledGroupImage = ImageUtils.createScaledBitmap(bitmap, displayWidth, MAX_IMAGE_SIZE);
        } else {
            scaledGroupImage = bitmap;
        }
    }

    public void mostrarTeclado(View view) {
        EditText editText = (EditText) findViewById(R.id.nombreGrupo);
        Intent intent = new Intent (this, TecladoActivity.class);
        intent.putExtra("texto", editText.getText().toString());
        startActivityForResult(intent,CONTENIDO_TECLADO);
    }
}

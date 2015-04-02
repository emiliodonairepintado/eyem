package es.uma.inftel.eyemandroid.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.asynctask.CrearPostAsyncTask;
import es.uma.inftel.eyemandroid.dialog.DialogManager;
import es.uma.inftel.eyemandroid.entity.Post;
import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.googleapi.VideoItem;
import es.uma.inftel.eyemandroid.util.FileUtils;
import es.uma.inftel.eyemandroid.util.ImageUtils;
import es.uma.inftel.eyemandroid.widget.FloatingActionButton;

/**
 * Created by Emilio on 17/03/2015.
 */
public class CrearPostActivity extends ActionBarActivity implements
        CompoundButton.OnCheckedChangeListener, DialogManager {

    private static final String TAG = "CrearPostActivity" ;
    private static final int MAX_IMAGE_SIZE = 512;
    private static final int MAX_IMAGE_SMALL_SIZE = 64;
    private static final int CAPTURAR_IMAGEN = 1;
    private static final int SELECCIONAR_IMAGEN = 2;
    private static final int SELECCIONAR_VIDEO = 3;
    private static final int CONTENIDO_TECLADO = 4;


    private Toolbar toolbar;
    private TextView textoestado;
    private String estado;
    private LocationManager locManager;
    private LocationListener locListener;
    private TextView textCiudad;
    private Double latitud;
    private Double longitud;
    private ImageView fotoestado;
    private String contenido;
    private String ciudad;
    private VideoItem videoItem;

    private ImageView imageViewPhoto;
    private Uri imageUri;
    private File postImageFile;
    private File postImageSmall;
    private Bitmap scaledPostImage;

    private ProgressDialog progressDialog;

    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_post);

        usuario = getIntent().getParcelableExtra("Usuario");

        imageViewPhoto = (ImageView) findViewById(R.id.foto_crear);
        fotoestado = (ImageView) findViewById(R.id.imagenEstado);

        textCiudad = (TextView) findViewById(R.id.ciudad);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.ic_video_collection_white_36dp));
        fab.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));

        SwitchCompat switchCompat = (SwitchCompat) findViewById(R.id.switchcompat);
        switchCompat.setSwitchPadding(40);
        switchCompat.setOnCheckedChangeListener(this);

        textoestado = (TextView) findViewById(R.id.textview_estado);
        estado = "publico";
        textoestado.setText(estado);
        fotoestado.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_public));

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {

            case R.id.switchcompat:
                if(isChecked == false){
                    estado = "publico";
                    textoestado.setText(estado);
                    fotoestado.setImageDrawable(getResources().getDrawable(R.drawable.ic_public_grey600_48dp));
                    Log.i("Switch: ", estado);
                }else{
                    estado = "privado";
                    textoestado.setText(estado);
                    fotoestado.setImageDrawable(getResources().getDrawable(R.drawable.ic_https_grey600_48dp));
                    Log.i("Switch: ", estado);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crearpost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;

            case R.id.BtnActualizar:
                try {
                    comenzarLocalizacion();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnGuardar:

                Post nuevopost = new Post();
                EditText contenidoET = (EditText) findViewById(R.id.edit_text);
                TextView ciudadET = (TextView) findViewById(R.id.ciudad);
                ImageView imagenUsuario = (ImageView) findViewById(R.id.user_post_avatar);

                contenido = contenidoET.getText().toString();
                ciudad = ciudadET.getText().toString();

                nuevopost.setIdPost(System.currentTimeMillis());
                nuevopost.setContenido(contenido);
                nuevopost.setLocalizacion(ciudad);
                nuevopost.setTipo(estado);
                nuevopost.setImagen((postImageFile != null) ? postImageFile.getAbsolutePath() : ".");
                if (videoItem != null){
                    nuevopost.setVideo(videoItem.getId());
                }
                nuevopost.setCreador(usuario);

                new CrearPostAsyncTask(this, this).execute(nuevopost);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void selectPhoto(View view) {
        final CharSequence[] opcionesCamara = getResources().getTextArray(R.array.imagenes_y_video);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_menu_camera);
        builder.setTitle(R.string.options);
        builder.setItems(opcionesCamara, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0: // Foto de la camara
                        captureCameraImage();
                        break;
                    case 1: // Foto de la galeria
                        Log.i("CAMARA", "entra aqui");
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, SELECCIONAR_IMAGEN);
                        break;
                    case 2: // Video de YouTube
                        startActivityForResult(new Intent(CrearPostActivity.this, YouTubeSearchActivity.class), SELECCIONAR_VIDEO);
                        break;
                }
            }
        });
        builder.show();
    }

    private void captureCameraImage() {
        try {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                postImageFile = FileUtils.createTemporaryFile("picture", ".jpg");
            } catch(Exception e) {
                Log.e(TAG, "Can't create file to take picture!", e);
                Toast.makeText(this, getString(R.string.err_check_sdcard), Toast.LENGTH_LONG).show();
                return;
            }
            imageUri = Uri.fromFile(postImageFile);
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
            } else if (requestCode == SELECCIONAR_VIDEO) {
                videoItem = data.getParcelableExtra("video");
                if (videoItem != null) {
                    View videoInfo = findViewById(R.id.video_info);
                    videoInfo.setVisibility(View.VISIBLE);

                    ImageView thumbnail = (ImageView) findViewById(R.id.video_thumbnail);
                    Picasso.with(getApplicationContext()).load(videoItem.getThumbnailURL()).into(thumbnail);

                    TextView title = (TextView) findViewById(R.id.video_title);
                    title.setText(videoItem.getTitle());

                    TextView description = (TextView) findViewById(R.id.video_description);
                    description.setText(videoItem.getDescription());
                }
            } else if (requestCode == CONTENIDO_TECLADO){
                EditText contenido = (EditText) findViewById(R.id.edit_text);
                contenido.setText(data.getStringExtra("texto"));
            }
        }
    }

    private void loadImageFromUri(Uri imageUri) {
        Bitmap image = ImageUtils.getImageFromUri(this, imageUri);
        if (image != null) {
            image = ImageUtils.createScaledBitmap(image, MAX_IMAGE_SIZE, MAX_IMAGE_SIZE);
            if (postImageFile != null) {
                postImageFile.delete();
            }
            postImageFile = null;
            try {
                postImageFile = ImageUtils.storeImage(image, FileUtils.createTemporaryFile("scaled-picture", ".jpg"));
                postImageSmall = ImageUtils.storeImage(image, MAX_IMAGE_SMALL_SIZE, MAX_IMAGE_SMALL_SIZE, FileUtils.createTemporaryFile("scaled-picture-small", ".jpg"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            setupScaledPostImage(image);
            imageViewPhoto.setImageBitmap(scaledPostImage);
        }
    }

    private void setupScaledPostImage(Bitmap bitmap) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;

        if (displayWidth < MAX_IMAGE_SIZE) {
            scaledPostImage = ImageUtils.createScaledBitmap(bitmap, displayWidth, MAX_IMAGE_SIZE);
        } else {
            scaledPostImage = bitmap;
        }
    }

    public void comenzarLocalizacion() throws IOException {

        //Obtenemos una referencia al LocationManager
        locManager =
                (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //Obtenemos la ultima posicion conocida
        Location loc =
                locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //Mostramos la ultima posicion conocida
        mostrarPosicion(loc);

        //Nos registramos para recibir actualizaciones de la posicion
        locListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                try {
                    mostrarPosicion(location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            public void onProviderDisabled(String provider){
            }
            public void onProviderEnabled(String provider){
            }
            public void onStatusChanged(String provider, int status, Bundle extras){
                Log.i("", "Provider Status: " + status);
            }
        };

        locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 30000, 0, locListener);
    }

    public void mostrarPosicion(Location loc) throws IOException {
        if(loc != null) {

            Log.i("", String.valueOf(loc.getLatitude() + " - " + String.valueOf(loc.getLongitude())));

            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            latitud = Double.valueOf(loc.getLatitude());
            longitud = Double.valueOf(loc.getLongitude());
            addresses = geocoder.getFromLocation(latitud,longitud, 1);

            String city = addresses.get(0).getLocality();
            textCiudad.setText(city);
        } else {
            textCiudad.setText("Sin datos de gps");
        }
    }

    @Override
    public void createDialog() {
        dismissDialog();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.creando_post));
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

    public void mostrarTeclado(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_text);
        Intent intent = new Intent (this, TecladoActivity.class);
        intent.putExtra("texto", editText.getText().toString());
        startActivityForResult(intent, CONTENIDO_TECLADO);
    }
}

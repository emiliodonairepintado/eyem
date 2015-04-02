package es.uma.inftel.eyemandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.File;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.adapter.MainActivityPagerAdapter;
import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.googleapi.GoogleApiClientManager;
import es.uma.inftel.eyemandroid.util.QRUtils;
import es.uma.inftel.eyemandroid.widget.SlidingTabLayout;
import es.uma.inftel.eyemandroid.widget.menu.DrawerMenu;


public class MainActivity extends ActionBarActivity implements GoogleApiClientManager.ConnectionCallback {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private DrawerMenu drawerMenu;

    private GoogleApiClientManager googleApiClientManager;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleApiClientManager = new GoogleApiClientManager(this, this);
        googleApiClientManager.restoreResolutionState(savedInstanceState);

        usuario = getIntent().getParcelableExtra("usuario");

        initToolbar();
        initTabs(usuario);
        initDrawerMenu(usuario);
    }

    @Override
    protected void onStop() {
        googleApiClientManager.stopConnection();
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        googleApiClientManager.saveResolutionState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleApiClientManager.onResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnected(GoogleApiClient googleApiClient) {
        Plus.AccountApi.clearDefaultAccount(googleApiClient);
        Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient);
        googleApiClient.disconnect();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater infalter = getMenuInflater();
        infalter.inflate(R.menu.menu_buscar_usuario, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_bar_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscar(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void buscar(String nombreUsuario) {
        Intent intent = new Intent(this, BuscarUsuarioActivity.class);
        intent.putExtra("query", nombreUsuario);
        startActivity(intent);
    }
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
    }

    private void initTabs(Usuario usuario) {
        final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        pager.setAdapter(new MainActivityPagerAdapter(getSupportFragmentManager(), usuario));

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });
    }

    private void initDrawerMenu(Usuario usuario) {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.navdrawer_listview);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);

        drawerMenu = new DrawerMenu(this, drawerLayout, drawerToggle, drawerList, new DrawerMenuItemClickListener(this), usuario);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean consumed = drawerMenu.onOptionsItemSelected(item);
        return consumed || super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerMenu.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerMenu.onConfigurationChanged(newConfig);
    }

    public void showUserProfile(View view) {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    // TODO: Sacar esta clase fuera. O meterla en DrawerMenu cuando se pueda.
    private class DrawerMenuItemClickListener implements AdapterView.OnItemClickListener {

        private final Activity activity;
        private int lastPosition;

        public DrawerMenuItemClickListener(Activity activity) {
            this.activity = activity;
            this.lastPosition = 0;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

            switch (position) {
                case 0:
                    // Timeline
                    viewPager.setCurrentItem(0);
                    updateSelectedItem(position);
                    break;
                case 1:
                    // Mi Eyem
                    viewPager.setCurrentItem(1);
                    updateSelectedItem(position);
                    break;
                case 2:
                    // Grupos
                    viewPager.setCurrentItem(2);
                    updateSelectedItem(position);
                    break;
                case 3:
                    // Generar QR
                    String fileName = "QR.jpg";
                    QRUtils.generarQR(usuario.getEmail() + usuario.getPass(), activity, fileName);

                    //Intent para compartir
                    File root = Environment.getExternalStorageDirectory();
                    Uri qrUri = Uri.parse(root.getAbsolutePath() + "/DCIM/" + fileName);
                    File bitmapFile = new File(String.valueOf(Uri.parse(root.getAbsolutePath() + "/DCIM/" + fileName)));
                    Uri myUri = Uri.fromFile(bitmapFile);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("application/image");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Código QR para EYEM");
                    intent.putExtra(Intent.EXTRA_STREAM, myUri);
                    activity.startActivity(Intent.createChooser(intent, activity.getResources().getString(R.string.enviar_qr)));
                    updateSelectedItem(lastPosition);
                    break;
                case 4:
                    // Desconectarse
                    googleApiClientManager.createConnection();
                    updateSelectedItem(lastPosition);
                    break;
            }

            if (position < 3) {
                lastPosition = position;
            }

            drawerMenu.closeDrawer();
        }

        private void updateSelectedItem(final int position) {
            drawerMenu.getDrawerListView().setItemChecked(position, true);
            drawerMenu.getDrawerListView().clearFocus();
            drawerMenu.getDrawerListView().post(new Runnable() {
                @Override
                public void run() {
                    drawerMenu.getDrawerListView().setSelection(position);
                }
            });
        }
    }

}

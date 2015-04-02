package es.uma.inftel.eyemandroid.widget.menu;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import es.uma.inftel.eyemandroid.R;
import es.uma.inftel.eyemandroid.adapter.MenuEntryAdapter;
import es.uma.inftel.eyemandroid.asynctask.LoadImageAsyncTask;
import es.uma.inftel.eyemandroid.entity.Usuario;

/**
 * Created by Miguel on 18/03/2015.
 */
public class DrawerMenu {

    private final Activity activity;
    private final DrawerLayout drawerLayout;
    private final ActionBarDrawerToggle drawerToggle;
    private final ListView drawerListView;
    private final Usuario usuario;

    public DrawerMenu(Activity activity, DrawerLayout drawerLayout,
                      ActionBarDrawerToggle actionBarDrawerToggle, ListView drawerList,
                      AdapterView.OnItemClickListener menuItemClickListener, Usuario usuario) {

        this.activity = activity;
        this.drawerLayout = drawerLayout;
        this.drawerToggle = actionBarDrawerToggle;
        this.drawerListView = drawerList;
        this.usuario = usuario;

        drawerLayout.setDrawerListener(drawerToggle);

        if (usuario.getImagen() != null) {
            ImageView ivUserAvatar = (ImageView) drawerLayout.findViewById(R.id.user_avatar);
            new LoadImageAsyncTask(ivUserAvatar).execute(usuario.getImagen());
        }

        if (usuario.getImagenCover() != null) {
            ImageView ivUserCoverImage = (ImageView) drawerLayout.findViewById(R.id.user_cover_image);
            new LoadImageAsyncTask(ivUserCoverImage).execute(usuario.getImagenCover());
        }

        TextView tvUserName = (TextView) activity.findViewById(R.id.user_name);
        tvUserName.setText(usuario.getNombre());

        TextView tvUserEmail = (TextView) activity.findViewById(R.id.user_email);
        tvUserEmail.setText(usuario.getEmail());

        drawerListView.setAdapter(new MenuEntryAdapter(activity));
        drawerListView.setOnItemClickListener(menuItemClickListener);

        drawerListView.setItemChecked(0, true);
        drawerListView.clearFocus();
        drawerListView.post(new Runnable() {
            @Override
            public void run() {
                drawerListView.setSelection(0);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return false;
    }

    public void openDrawer() {
        drawerLayout.openDrawer(Gravity.START);
    }

    public void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.START);
    }

    public void syncState() {
        drawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    public ListView getDrawerListView() {
        return drawerListView;
    }

}

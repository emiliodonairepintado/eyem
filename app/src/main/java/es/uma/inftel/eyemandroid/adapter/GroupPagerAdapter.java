package es.uma.inftel.eyemandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import es.uma.inftel.eyemandroid.entity.Grupo;
import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.fragment.PostGrupoFragment;
import es.uma.inftel.eyemandroid.fragment.UsuariosGrupoFragment;

/**
 * Created by maramec on 24/03/2015.
 */
public class GroupPagerAdapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;

    private final String[] titles = new String[] {"Timeline", "Usuarios"};
    private final Usuario usuario;
    private final Grupo grupo;

    public GroupPagerAdapter(FragmentManager fm, Usuario usuario, Grupo grupo) {
        super(fm);
        this.grupo = grupo;
        this.usuario = usuario;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PostGrupoFragment.newInstance(usuario, grupo);
            case 1:
                return UsuariosGrupoFragment.newInstance(usuario, grupo);
        }
        return null;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
package es.uma.inftel.eyemandroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import es.uma.inftel.eyemandroid.entity.Usuario;
import es.uma.inftel.eyemandroid.fragment.GruposFragment;
import es.uma.inftel.eyemandroid.fragment.MiEyemFragment;
import es.uma.inftel.eyemandroid.fragment.TimelineFragment;

public class MainActivityPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;

    private final String[] titles = new String[] {"Timeline", "Mi eyem", "Grupos"};
    private final Usuario usuario;

    public MainActivityPagerAdapter(FragmentManager fm, Usuario usuario) {
        super(fm);
        this.usuario = usuario;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TimelineFragment.newInstance(usuario);
            case 1:
                return MiEyemFragment.newInstance(usuario);
            case 2:
                return GruposFragment.newInstance(usuario);
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
package es.uma.inftel.eyemandroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import es.uma.inftel.eyemandroid.entity.Usuario;


public abstract class UsuarioLogeadoFragment extends Fragment {

    private Usuario usuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        usuario = args.getParcelable("usuario");
    }

    protected Usuario getUsuario() {
        return usuario;
    }
}
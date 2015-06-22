package rp3.marketforce.marcaciones;

import rp3.app.BaseFragment;
import rp3.marketforce.models.Cliente;
import rp3.marketforce.models.marcacion.Permiso;

/**
 * Created by magno_000 on 19/06/2015.
 */
public class PermisoDetailFragment extends BaseFragment {


    public static PermisoDetailFragment newInstance(Permiso permiso) {
        return new PermisoDetailFragment();
    }

    public interface PermisoDetailFragmentListener{
        void onPermisoChanged(Permiso permiso);
    }
}

package rp3.auna.bean;

/**
 * Created by Jesus Villa on 16/10/2017.
 */

import java.util.List;

/**
 * *Recepcionar Informacion*
 * */
public class Informacion {

    private String Aplicacion;
    private String UsuarioSpi;
    private String CanalSpi;
    private List<Documento> Documentos;

    public List<Documento> getDocumentos() {
        return Documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        Documentos = documentos;
    }

    public String getAplicacion() {
        return Aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        Aplicacion = aplicacion;
    }

    public String getUsuarioSpi() {
        return UsuarioSpi;
    }

    public void setUsuarioSpi(String usuarioSpi) {
        UsuarioSpi = usuarioSpi;
    }

    public String getCanalSpi() {
        return CanalSpi;
    }

    public void setCanalSpi(String canalSpi) {
        CanalSpi = canalSpi;
    }
}

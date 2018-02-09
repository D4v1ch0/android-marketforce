package rp3.auna.bean;

/**
 * Created by Jesus Villa on 08/11/2017.
 */

public class FotoVisita {
    public int IdFoto;
    public int IdVisita;
    public String Foto;
    public String Observacion;
    public int Estado;

    public int getIdFoto() {
        return IdFoto;
    }

    public void setIdFoto(int idFoto) {
        IdFoto = idFoto;
    }

    public int getIdVisita() {
        return IdVisita;
    }

    public void setIdVisita(int idVisita) {
        IdVisita = idVisita;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    @Override
    public String toString() {
        return "FotoVisita{" +
                "IdFoto=" + IdFoto +
                ", IdVisita=" + IdVisita +
                ", Foto='" + Foto + '\'' +
                ", Observacion='" + Observacion + '\'' +
                ", Estado=" + Estado +
                '}';
    }
}

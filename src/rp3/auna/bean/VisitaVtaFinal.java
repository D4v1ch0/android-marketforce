package rp3.auna.bean;

import java.util.List;

/**
 * Created by Jesus Villa on 25/01/2018.
 */

public class VisitaVtaFinal {
    public VisitaVta Visita;
    public VentaFisicaData VentaFisicaData;
    public VentaRegularData VentaRegularData;
    public RegistroPago RegistroPago;
    public int IdSolicitud;

    //region Encapsulamiento


    public int getIdSolicitud() {
        return IdSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        IdSolicitud = idSolicitud;
    }

    public VisitaVta getVisita() {
        return Visita;
    }

    public void setVisita(VisitaVta visita) {
        Visita = visita;
    }

    public rp3.auna.bean.VentaFisicaData getVentaFisicaData() {
        return VentaFisicaData;
    }

    public void setVentaFisicaData(rp3.auna.bean.VentaFisicaData ventaFisicaData) {
        VentaFisicaData = ventaFisicaData;
    }

    public rp3.auna.bean.VentaRegularData getVentaRegularData() {
        return VentaRegularData;
    }

    public void setVentaRegularData(rp3.auna.bean.VentaRegularData ventaRegularData) {
        VentaRegularData = ventaRegularData;
    }

    public rp3.auna.bean.RegistroPago getRegistroPago() {
        return RegistroPago;
    }

    public void setRegistroPago(rp3.auna.bean.RegistroPago registroPago) {
        RegistroPago = registroPago;
    }


    //endregion

    @Override
    public String toString() {
        return "VisitaVtaFinal{" +
                "Visita=" + Visita +
                ", VentaFisicaData=" + VentaFisicaData +
                ", VentaRegularData=" + VentaRegularData +
                ", RegistroPago=" + RegistroPago +
                ", IdSolicitud=" + IdSolicitud +
                '}';
    }
}

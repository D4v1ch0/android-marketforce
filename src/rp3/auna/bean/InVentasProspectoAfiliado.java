package rp3.auna.bean;

/**
 * Created by Jesus Villa on 26/10/2017.
 */

public class InVentasProspectoAfiliado {
    public String TipoIdentificacion;
    public String Identificacion;
    public String OrigenCliente;
    public String Categoria;
    public String CodigoGrupoFamiliar;

    public String getTipoIdentificacion() {
        return TipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        TipoIdentificacion = tipoIdentificacion;
    }

    public String getIdentificacion() {
        return Identificacion;
    }

    public void setIdentificacion(String identificacion) {
        Identificacion = identificacion;
    }

    public String getOrigenCliente() {
        return OrigenCliente;
    }

    public void setOrigenCliente(String origenCliente) {
        OrigenCliente = origenCliente;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getCodigoGrupoFamiliar() {
        return CodigoGrupoFamiliar;
    }

    public void setCodigoGrupoFamiliar(String codigoGrupoFamiliar) {
        CodigoGrupoFamiliar = codigoGrupoFamiliar;
    }

    @Override
    public String toString() {
        return "InVentasProspectoAfiliado{" +
                "TipoIdentificacion='" + TipoIdentificacion + '\'' +
                ", Identificacion='" + Identificacion + '\'' +
                ", OrigenCliente='" + OrigenCliente + '\'' +
                ", Categoria='" + Categoria + '\'' +
                ", CodigoGrupoFamiliar='" + CodigoGrupoFamiliar + '\'' +
                '}';
    }
}

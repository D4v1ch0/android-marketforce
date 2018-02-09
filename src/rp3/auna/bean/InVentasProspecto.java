package rp3.auna.bean;

/**
 * Created by Jesus Villa on 26/10/2017.
 */

public class InVentasProspecto {
    public String COD_SEGMENTO;
    public String NRO_DOCIDENTIDAD;
    public String NOM_PROSPECTO;
    public String NRO_HIJOS;
    public String IND_BANCARIZADO;
    public String IND_CALIF_SBS;
    public String IND_CALIF_TDC ;
    public String CNT_TDC_REPORTADAS ;
    public String CNT_TDC_REP_BANCO ;
    public String IND_DISP_TDC ;
    public String IND_DISP_MAYOR_TDC ;
    public String IND_RANGO_ING ;
    public String IND_AFIL_ONC ;
    public InVentasProspectoAfiliado afiliado ;

    public String getCOD_SEGMENTO() {
        return COD_SEGMENTO;
    }

    public void setCOD_SEGMENTO(String COD_SEGMENTO) {
        this.COD_SEGMENTO = COD_SEGMENTO;
    }

    public String getNRO_DOCIDENTIDAD() {
        return NRO_DOCIDENTIDAD;
    }

    public void setNRO_DOCIDENTIDAD(String NRO_DOCIDENTIDAD) {
        this.NRO_DOCIDENTIDAD = NRO_DOCIDENTIDAD;
    }

    public String getNOM_PROSPECTO() {
        return NOM_PROSPECTO;
    }

    public void setNOM_PROSPECTO(String NOM_PROSPECTO) {
        this.NOM_PROSPECTO = NOM_PROSPECTO;
    }

    public String getNRO_HIJOS() {
        return NRO_HIJOS;
    }

    public void setNRO_HIJOS(String NRO_HIJOS) {
        this.NRO_HIJOS = NRO_HIJOS;
    }

    public String getIND_BANCARIZADO() {
        return IND_BANCARIZADO;
    }

    public void setIND_BANCARIZADO(String IND_BANCARIZADO) {
        this.IND_BANCARIZADO = IND_BANCARIZADO;
    }

    public String getIND_CALIF_SBS() {
        return IND_CALIF_SBS;
    }

    public void setIND_CALIF_SBS(String IND_CALIF_SBS) {
        this.IND_CALIF_SBS = IND_CALIF_SBS;
    }

    public String getIND_CALIF_TDC() {
        return IND_CALIF_TDC;
    }

    public void setIND_CALIF_TDC(String IND_CALIF_TDC) {
        this.IND_CALIF_TDC = IND_CALIF_TDC;
    }

    public String getCNT_TDC_REPORTADAS() {
        return CNT_TDC_REPORTADAS;
    }

    public void setCNT_TDC_REPORTADAS(String CNT_TDC_REPORTADAS) {
        this.CNT_TDC_REPORTADAS = CNT_TDC_REPORTADAS;
    }

    public String getCNT_TDC_REP_BANCO() {
        return CNT_TDC_REP_BANCO;
    }

    public void setCNT_TDC_REP_BANCO(String CNT_TDC_REP_BANCO) {
        this.CNT_TDC_REP_BANCO = CNT_TDC_REP_BANCO;
    }

    public String getIND_DISP_TDC() {
        return IND_DISP_TDC;
    }

    public void setIND_DISP_TDC(String IND_DISP_TDC) {
        this.IND_DISP_TDC = IND_DISP_TDC;
    }

    public String getIND_DISP_MAYOR_TDC() {
        return IND_DISP_MAYOR_TDC;
    }

    public void setIND_DISP_MAYOR_TDC(String IND_DISP_MAYOR_TDC) {
        this.IND_DISP_MAYOR_TDC = IND_DISP_MAYOR_TDC;
    }

    public String getIND_RANGO_ING() {
        return IND_RANGO_ING;
    }

    public void setIND_RANGO_ING(String IND_RANGO_ING) {
        this.IND_RANGO_ING = IND_RANGO_ING;
    }

    public String getIND_AFIL_ONC() {
        return IND_AFIL_ONC;
    }

    public void setIND_AFIL_ONC(String IND_AFIL_ONC) {
        this.IND_AFIL_ONC = IND_AFIL_ONC;
    }

    public InVentasProspectoAfiliado getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(InVentasProspectoAfiliado afiliado) {
        this.afiliado = afiliado;
    }

    @Override
    public String toString() {
        return "InVentasProspecto{" +
                "COD_SEGMENTO='" + COD_SEGMENTO + '\'' +
                ", NRO_DOCIDENTIDAD='" + NRO_DOCIDENTIDAD + '\'' +
                ", NOM_PROSPECTO='" + NOM_PROSPECTO + '\'' +
                ", NRO_HIJOS='" + NRO_HIJOS + '\'' +
                ", IND_BANCARIZADO='" + IND_BANCARIZADO + '\'' +
                ", IND_CALIF_SBS='" + IND_CALIF_SBS + '\'' +
                ", IND_CALIF_TDC='" + IND_CALIF_TDC + '\'' +
                ", CNT_TDC_REPORTADAS='" + CNT_TDC_REPORTADAS + '\'' +
                ", CNT_TDC_REP_BANCO='" + CNT_TDC_REP_BANCO + '\'' +
                ", IND_DISP_TDC='" + IND_DISP_TDC + '\'' +
                ", IND_DISP_MAYOR_TDC='" + IND_DISP_MAYOR_TDC + '\'' +
                ", IND_RANGO_ING='" + IND_RANGO_ING + '\'' +
                ", IND_AFIL_ONC='" + IND_AFIL_ONC + '\'' +
                ", afiliado=" + afiliado +
                '}';
    }
}

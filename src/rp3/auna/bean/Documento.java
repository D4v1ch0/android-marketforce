package rp3.auna.bean;

/**
 * Created by Jesus Villa on 16/10/2017.
 */

/***
 *
 *Documento para el WS SPI
 */
public class Documento {

    public int TipoDocumento;
    public String Numero;
    public String Nombre;

    public int getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}

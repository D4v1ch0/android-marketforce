package rp3.auna.bean.virtual;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jesus Villa on 03/04/2018.
 */

public class Authentication {
    @SerializedName("IdAuthentication")
    private int idAuthentication;
    @SerializedName("Nombres")
    private String nombres;
    @SerializedName("Apellidos")
    private String apellidos;
    @SerializedName("Documento")
    private String documento;
    @SerializedName("Imagen")
    private String imagen;

    //region Encapsulamiento

    public int getIdAuthentication() {
        return idAuthentication;
    }

    public void setIdAuthentication(int idAuthentication) {
        this.idAuthentication = idAuthentication;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    //endregion


    @Override
    public String toString() {
        return "Authentication{" +
                "idAuthentication=" + idAuthentication +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", documento='" + documento + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}

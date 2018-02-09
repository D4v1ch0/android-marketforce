package rp3.auna.bean;

/**
 * Created by Jesus Villa on 12/12/2017.
 */

public class ItemFile {
    private String nombre;
    private int flag;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "ItemFile{" +
                "nombre='" + nombre + '\'' +
                ", flag=" + flag +
                '}';
    }
}
